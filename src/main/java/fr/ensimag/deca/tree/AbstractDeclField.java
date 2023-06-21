package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

public abstract class AbstractDeclField extends Tree{
    /**
     * @param compiler
     * @param localEnv
     * @param currentClass
     */
    protected void verifyDeclField(DecacCompiler compiler,
        EnvironmentExp localEnv, ClassDefinition currentClass, int index)
        throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }

    public void codeGen(DecacCompiler compiler){
        throw new UnsupportedOperationException("not yet implemented");
    }

    public void verifyClassBody(DecacCompiler compiler, EnvironmentExp environmentExp, ClassDefinition defOfClass) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }
}