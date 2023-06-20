package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.ADDSP;
import fr.ensimag.ima.pseudocode.instructions.SUBSP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.TSTO;
import fr.ensimag.ima.pseudocode.instructions.RTS;


import java.io.PrintStream;

import org.apache.commons.lang.Validate;

public class DeclMethod extends AbstractDeclMethod{

    final private AbstractIdentifier returnType;
    final private AbstractIdentifier methodName;
    final private ListDeclParam listDeclParam;
    final private AbstractMethodBody methodBody;
    public Visibility visib;

    public DeclMethod(Visibility visib, AbstractIdentifier returnType, AbstractIdentifier methodName, ListDeclParam listDeclParam, AbstractMethodBody methodBody){
        Validate.notNull(returnType);
        Validate.notNull(methodName);
        Validate.notNull(listDeclParam);
        Validate.notNull(methodBody);
        this.returnType = returnType;
        this.methodName = methodName;
        this.listDeclParam = listDeclParam;
        this.methodBody = methodBody;
        this.visib = visib;
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        this.returnType.iter(f);
        this.methodName.iter(f);
        this.listDeclParam.iterChildren(f);
        this.methodBody.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        returnType.prettyPrint(s, prefix, false);
        methodName.prettyPrint(s, prefix, false);
        listDeclParam.prettyPrint(s, prefix, false);
        methodBody.prettyPrint(s, prefix, true, false);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("not yet implemented");
    }
    
    @Override
    protected void verifyDeclMethod(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass, int index)
            throws ContextualError {

        // Verify the return type
        Type type = this.returnType.verifyType(compiler);
        this.returnType.setType(type);
        // Verify the List of parameters
        for(AbstractDeclParam declParam : this.listDeclParam.getList()){
            declParam.verifyDeclParam(compiler, localEnv, currentClass);
        }
        // Create a signature for the method
        Signature methodSignature = new Signature(methodName.getName());
        // Add each parameter's type to the signature
        for (AbstractDeclParam param : this.listDeclParam.getList()) {
            methodSignature.addParamType(param.getType().verifyType(compiler));
        }
        this.methodName.setType(type);
        
        FieldDefinition potentialDef = null;
        if (currentClass.getSuperClass() != null) {
            // This cannot be something else than a field if found,
            // since the methods are declared with their whole signatures
            if (currentClass.getSuperClass().getMembers().get(methodName.getName()) != null)
            // that's why we can serenely cast to FieldDefinition
            potentialDef = (FieldDefinition) currentClass.getSuperClass().getMembers().get(methodName.getName());
        }
        if (potentialDef != null) {
                
            // In order to avoid masking a field with a method
            String message = String.format("Can't declare method '%s' in class %s because the super class %s have a field with that name", this.methodName.getName(), currentClass.getType().getName().getName(), currentClass.getSuperClass().getType().getName().getName());
            throw new ContextualError(message, getLocation());
        }
        
        TypeDefinition typeDef = compiler.environmentType.defOfType(returnType.getName());
        returnType.setDefinition(typeDef);

        // Formatting the signature of the method
        String toStringSignature = methodSignature.toString();
        
        MethodDefinition methodDef = new MethodDefinition(type, getLocation(), methodSignature, visib, index);
        methodName.setDefinition(methodDef);

        // By default, we increase the method count
        currentClass.incNumberOfMethods();
        MethodDefinition potentialMethodDef = null;
        if (currentClass.getSuperClass() != null) {
            // Check for the same method signature in the super class
            potentialMethodDef = (MethodDefinition) (currentClass.getSuperClass().getMembers().get(compiler.createSymbol(toStringSignature)));
        }
        // If it exists, then the super class method is not in the current class scope
        // The new method overrides the parent one
        if (potentialMethodDef != null) {
            // So this method does not increase the method count of the class, so we revert the default increment
            currentClass.setNumberOfMethods(currentClass.getNumberOfMethods() - 1);
        }

        // Try to declare the method in the class local environment this time
        try {
            localEnv.declare(compiler.createSymbol(toStringSignature), methodDef);
        } catch(DoubleDefException e) {
            String message = String.format("Method with signature '%s' already declared", toStringSignature);
            throw new ContextualError(message, getLocation());
        }
    }

    @Override
    protected void verifyClassBody(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
    throws ContextualError {
        // Declare the parameters in the method's local environment (and not the class' one)
        for(AbstractDeclParam declParam : this.listDeclParam.getList()) {
            declParam.verifyClassBody(compiler, localEnv, currentClass);
        }
        // Verify the method body
        if (this.methodBody instanceof MethodBody) {
            ((MethodBody) this.methodBody).verifyMethodBody(compiler, localEnv, currentClass, this.returnType.getType());
        }
        else {
            ((MethodAsmBody) this.methodBody).verifyAsmMethodBody(compiler, localEnv, currentClass);
        }
    }

    @Override
    public void genMethodTableEntry(DecacCompiler compiler, ClassDefinition classDefinition) {
        Label methodLabel = compiler.getLabelManager().createMethodLabel(classDefinition.getType().getName().getName(), ((MethodDefinition)(this.methodName.getDefinition())).getSignature().toLabelString());
        classDefinition.getMethodTable().addMethod(this.methodName.getMethodDefinition(), methodLabel);
        this.methodName.getMethodDefinition().setLabel(methodLabel);
    }

    @Override
    public void codeGenMethodTableEntry(DecacCompiler compiler) {
        // Load the PC address for the method
        compiler.addInstruction(new LOAD(this.methodName.getMethodDefinition().getLabel(), GPRegister.R0));

        // Store it in the memory, at it's place in the method table
        compiler.addInstruction(new STORE(GPRegister.R0, compiler.allocate()), "store method " + this.methodName.getName() + " address");
    }

    @Override
    public void codeGen(DecacCompiler compiler) {
        // Get method label
        Label methodLabel = this.methodName.getMethodDefinition().getLabel();

        // Comment for the start of the method code
        compiler.addComment("start : method " + methodLabel.toString());

        // Set method label
        compiler.addLabel(methodLabel);

        if(this.methodBody instanceof MethodBody){
            // Storing method variable count
            int localVariableCount = this.methodBody.getVarCount();

            // Set params operand (address) in their definition
            this.listDeclParam.setParamAddresses();


            // Generating the method code under a sub compiler
            DecacCompiler methodCompiler = new DecacCompiler(compiler.getCompilerOptions(), compiler.getSource());
            this.methodBody.codeGen(methodCompiler);

            // Set method stack overflow test and stack pointer
            // Total variables to store in the stack + total push made during operations + total push made to save registers
            compiler.addInstruction(new TSTO(localVariableCount + methodCompiler.getMaxStackSize() + methodCompiler.getMaxRegister() - 2));
            compiler.addInstruction(new BOV(compiler.getLabelManager().getStackOverflowLabel()),"check for stack overflows");
            compiler.addInstruction(new ADDSP(localVariableCount));
            // Save registers
            for(int i = 2 ; i <= methodCompiler.getMaxRegister() ; i++){
                compiler.addInstruction(new PUSH(GPRegister.getR(i)),"save register R" + i);
            }

            // Then append the generated code
            compiler.append(methodCompiler.getProgram());

            // Restore saved registers
            for(int i = methodCompiler.getMaxRegister() ; i >= 2 ; i--){
                compiler.addInstruction(new POP(GPRegister.getR(i)),"restore register R" + i);
            }
            // Reset Stack Pointer
            compiler.addInstruction(new SUBSP(localVariableCount));

            // Return to old context
            compiler.addInstruction(new RTS());
        }
        else{            
            this.methodBody.codeGen(compiler);
        }

        // Comment for the end of the method code
        compiler.addComment("end : method " + methodLabel.toString());
    }

}
