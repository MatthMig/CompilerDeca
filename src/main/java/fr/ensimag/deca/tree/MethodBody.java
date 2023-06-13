package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

public abstract class MethodBody extends AbstractMethodBody{
    /**
     * @param compiler
     * @param localEnv
     * @param currentClass
     */
    protected abstract void verifyMethodBody(DecacCompiler compiler,
        EnvironmentExp localEnv, ClassDefinition currentClass)
        throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }

    public void codeGen(DecacCompiler compiler){
        throw new UnsupportedOperationException("not yet implemented");
    }
}