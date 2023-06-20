package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.ADDSP;
import fr.ensimag.ima.pseudocode.instructions.SUBSP;
import fr.ensimag.ima.pseudocode.instructions.RTS;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.TSTO;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.LEA;

import java.io.PrintStream;
import java.util.Map.Entry;

import org.apache.commons.lang.Validate;

/**
 * Declaration of a class (<code>class name extends superClass {members}<code>).
 *
 * @author gl03
 * @date 21/04/2023
 */
public class DeclClass extends AbstractDeclClass {

    final private AbstractIdentifier className;
    private AbstractIdentifier superClassName;
    final private ListDeclField listDeclField;
    final private ListDeclMethod listDeclMethod;

    public DeclClass(AbstractIdentifier className, AbstractIdentifier superClassName, ListDeclField listDeclField, ListDeclMethod listDeclMethod) {
        Validate.notNull(className);
        // No check for superClassName, null is allowed.
        Validate.notNull(listDeclField);
        Validate.notNull(listDeclMethod);
        this.className = className;
        this.superClassName = superClassName;
        this.listDeclField = listDeclField;
        this.listDeclMethod = listDeclMethod;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("class { ... A FAIRE ... }");
    }

    @Override
    protected void verifyClass(DecacCompiler compiler) throws ContextualError {
        if(compiler.environmentType.defOfType(this.className.getName()) == null){
            if(this.superClassName != null){
                // Load the super class definition
                ClassDefinition superClassDef = compiler.environmentType.defOfClass(this.superClassName.getName());
                this.superClassName.setDefinition(superClassDef);
                if(superClassDef != null){
                    // Declare the class in the environment of the compiler
                    compiler.environmentType.declareClass(className, new ClassType(className.getName(), getLocation(), superClassDef));
                    this.className.setDefinition(compiler.environmentType.defOfType(className.getName()));
                    compiler.environmentType.defOfClass(className.getName()).setNumberOfFields(superClassDef.getNumberOfFields());
                    compiler.environmentType.defOfClass(className.getName()).setNumberOfMethods(superClassDef.getNumberOfMethods());
                } else {
                    // Parent class doesn't exist
                    String message = String.format("Trying to extend undefined class %s at %s", this.getSuperClassName().getName(), superClassName.getLocation() );
                    throw new ContextualError(message, this.getLocation());
                }
            } else {
                // No inheritance
                ClassDefinition objectClassDefinition = compiler.environmentType.defOfClass(compiler.environmentType.OBJECT.getName());
                compiler.environmentType.declareClass(className, new ClassType(className.getName() , getLocation(), objectClassDefinition));
                this.superClassName = new Identifier(compiler.environmentType.OBJECT.getName());
                this.superClassName.setLocation(getLocation());
                this.superClassName.setDefinition(objectClassDefinition);
                this.className.setDefinition(compiler.environmentType.defOfType(className.getName()));
                compiler.environmentType.defOfClass(className.getName()).setNumberOfFields(objectClassDefinition.getNumberOfFields());
                compiler.environmentType.defOfClass(className.getName()).setNumberOfMethods(objectClassDefinition.getNumberOfMethods());
            }
        } else {
            String message = String.format("Class %s already defined at %s", this.className.getName().getName(), ((ClassDefinition) (compiler.environmentType.defOfType(this.className.getName()))).getLocation() );
            throw new ContextualError(message, getLocation());
        }
    }

    @Override
    protected void verifyClassMembers(DecacCompiler compiler)
            throws ContextualError {
        int i;
        EnvironmentExp envExp = compiler.environmentType.defOfClass(this.className.getName()).getMembers();
        ClassDefinition classDefinition = compiler.environmentType.defOfClass(this.className.getName());

        ClassDefinition superClassDef = compiler.environmentType.defOfClass(this.superClassName.getName());
        classDefinition.setNumberOfFields(superClassDef.getNumberOfFields());
        classDefinition.setNumberOfMethods(superClassDef.getNumberOfMethods());

        i = classDefinition.getNumberOfFields();
        for(AbstractDeclField declField : this.listDeclField.getList()){
            // ++i : increment i before going into the method
            declField.verifyDeclField(compiler, envExp, classDefinition, ++i);
        }

        i = classDefinition.getNumberOfMethods();
        for(AbstractDeclMethod declMethod : this.listDeclMethod.getList()){
            declMethod.verifyDeclMethod(compiler, envExp, compiler.environmentType.defOfClass(this.className.getName()), ++i);
        }

    }

    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
        // Passe 3 for the fields
        for(AbstractDeclField declField : this.listDeclField.getList()){
            EnvironmentExp objectEnv = compiler.environmentType.defOfClass(this.className.getName()).getMembers();
            declField.verifyClassBody(compiler, objectEnv, compiler.environmentType.defOfClass(this.className.getName()));
        }
        // Passe 3 for the methods
        for(AbstractDeclMethod declMethod : this.listDeclMethod.getList()){
            EnvironmentExp objectEnv = compiler.environmentType.defOfClass(this.className.getName()).getMembers();
            declMethod.verifyClassBody(compiler, new EnvironmentExp(objectEnv), compiler.environmentType.defOfClass(this.className.getName()));
        }
    }

    @Override
    public void codeGenClass(DecacCompiler compiler){
        // Storing Class field count
        int fieldCount = this.className.getClassDefinition().getNumberOfFields();

        // Generating the initialization code under a sub compiler
        DecacCompiler initializationCompiler = new DecacCompiler(compiler.getCompilerOptions(), compiler.getSource());
        initializationCompiler.environmentType = compiler.environmentType;
        this.listDeclField.codeGen(initializationCompiler);

        // Start the init subprogram with an adapted label
        Label initLabel = compiler.getLabelManager().createInitClassLabel(className.getName().getName());
        compiler.addLabel(initLabel);

        // Now that the subcompiler generated the code, I can get informations I need to check for the stack
        compiler.addInstruction(new TSTO(fieldCount + initializationCompiler.getMaxStackSize()));
        compiler.addInstruction(new BOV(compiler.getLabelManager().getStackOverflowLabel()),"check for stack overflows");
        compiler.addInstruction(new ADDSP(fieldCount));

        // Now append the subCompiler generated program
        compiler.append(initializationCompiler.getProgram());

        // Reset Stack Pointer
        compiler.addInstruction(new SUBSP(fieldCount));

        // Return
        compiler.addInstruction(new RTS());

        // Generate code for methods
        this.listDeclMethod.codeGen(compiler);
    }


    @Override
    public void codeGenMethodTable(DecacCompiler compiler) {
        compiler.addComment("start : method table for class " + this.className.getName());

        // Generate method table, starting on current GB offset
        this.className.getClassDefinition().setMethodTableAddr(new RegisterOffset(compiler.getLBOffset(), Register.LB));

        compiler.addInstruction(new LEA(this.superClassName.getClassDefinition().getMethodTableAddr(), Register.R0));
        compiler.addInstruction(new STORE(Register.R0, this.className.getClassDefinition().getMethodTableAddr()));

        // Generate the method Table
        listDeclMethod.genMethodTable(compiler, this.className.getClassDefinition());

        // for each method of the super class
        for (Entry<MethodDefinition, Label> entry : this.superClassName.getClassDefinition().getMethodTable().getMethodsMap().entrySet()) {
            // if method doesn't exists in curent class
            if (this.className.getClassDefinition().getMembers().get(compiler.createSymbol(entry.getKey().getSignature().toString())) == null) {
                // we add the super method to the table
                this.className.getClassDefinition().getMethodTable().addMethod(entry.getKey(), entry.getValue());
            }
        }

        // For each method of the table that was just generated
        for (MethodDefinition methodDef : this.className.getClassDefinition().getMethodTable().getMethodsMap().keySet()) {
            Label label = this.className.getClassDefinition().getMethodTable().getLabelByDef(methodDef);
            // Load the PC address for the method
            compiler.addInstruction(new LOAD(label, GPRegister.R0));

            // Store it in the memory, at it's place in the method table
            compiler.addInstruction(new STORE(GPRegister.R0, compiler.allocate()));
        }
        compiler.addComment("end : method table for class " + this.className.getName());
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        className.prettyPrint(s, prefix, false);
        if (superClassName != null) {
            superClassName.prettyPrint(s, prefix, false);
        }
        listDeclField.prettyPrint(s, prefix, false);
        listDeclMethod.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        className.iter(f);
        if(superClassName != null)
            superClassName.iter(f);
        listDeclField.iter(f);
        listDeclMethod.iter(f);
    }

    public AbstractIdentifier getSuperClassName() {
        return superClassName;
    }

}
