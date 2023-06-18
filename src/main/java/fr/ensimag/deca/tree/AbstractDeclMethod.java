package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

public abstract class AbstractDeclMethod extends Tree{

    protected void verifySuperClassMethods(DecacCompiler compiler,
        EnvironmentExp localEnv, ClassDefinition currentClass)
        throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }

    /**
     * @param compiler
     * @param localEnv
     * @param currentClass
     */
    protected void verifyDeclMethod(DecacCompiler compiler,
        EnvironmentExp localEnv, ClassDefinition currentClass,
        int index) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }

    /**
     * @param compiler
     * @param localEnv
     * @param currentClass
     */
    protected void verifyClassBody(DecacCompiler compiler,
        EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }

    public void codeGen(DecacCompiler compiler){
        throw new UnsupportedOperationException("not yet implemented");
    }

    public void genMethodTableEntry(DecacCompiler compiler, ClassDefinition classDefinition) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    public void codeGenMethodTableEntry(DecacCompiler compiler) {
        throw new UnsupportedOperationException("not yet implemented");
    }
}
