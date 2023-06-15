package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;
import fr.ensimag.deca.tools.IndentPrintStream;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

public class DeclMethod extends AbstractDeclMethod{

    final private AbstractIdentifier returnType;
    final private AbstractIdentifier methodName;
    final private ListDeclParam listDeclParam;
    final private AbstractMethodBody methodBody;
    
    public DeclMethod(AbstractIdentifier returnType, AbstractIdentifier methodName, ListDeclParam listDeclParam, AbstractMethodBody methodBody){
        Validate.notNull(returnType);
        Validate.notNull(methodName);
        Validate.notNull(listDeclParam);
        Validate.notNull(methodBody);
        this.returnType = returnType;
        this.methodName = methodName;
        this.listDeclParam = listDeclParam;
        this.methodBody = methodBody;
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        throw new UnsupportedOperationException("not yet implemented");
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
        // Create a local environment for the method
        EnvironmentExp methodEnv = new EnvironmentExp(localEnv);
        // Verify the List of parameters and declare them in the method's local environment (and not the object's one)
        for(AbstractDeclParam declParam : this.listDeclParam.getList()){
            declParam.verifyDeclParam(compiler, methodEnv, currentClass);
        }
        // Create a signature for the method
        Signature methodSignature = new Signature(type, methodName.getName());
        // Add each parameter's type to the signature
        for (AbstractDeclParam param : this.listDeclParam.getList()) {
            methodSignature.addParamType(param.getType().verifyType(compiler));
        }
        this.methodName.setType(type);
        MethodDefinition methodDef = new MethodDefinition(type, getLocation(), methodSignature, index);
        TypeDefinition typeDef = compiler.environmentType.defOfType(returnType.getName());
        methodName.setDefinition(methodDef);
        returnType.setDefinition(typeDef);

        // Formatting the signature of the method
        String toStringSignature = 
            methodSignature.getReturnType().toString()
            + " " + methodSignature.getMethodName().toString() + "("; // Add the return type and the method name to the signature
        if (methodSignature.paramListSize() == 0)
            toStringSignature += "void";
        else
            for (int i = 1; i < methodSignature.paramListSize(); i++) {
                if (i != 1) toStringSignature += ", ";
                toStringSignature += methodSignature.paramNumber(i).toString();
            }
        toStringSignature += ")";

        // Try to declare the method in the Object local environment this time
        try {
            localEnv.declare(compiler.symbolTable.create(toStringSignature), methodDef);
        } catch(DoubleDefException e) {
            String message = String.format("Method with signature '%s' already declared", toStringSignature);
            throw new ContextualError(message, getLocation());
        }

        // Verify the method body
        if (this.methodBody instanceof MethodBody) {
            ((MethodBody) this.methodBody).verifyMethodBody(compiler, methodEnv, currentClass, type);
        }
        else {
            // ((MethodBody) this.methodBody).verifyAsmMethodBody(compiler, methodEnv, currentClass);
        }
    }
}