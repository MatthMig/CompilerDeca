package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

public abstract class AbstractDeclParam extends Tree{
    /**
     * @param compiler
     * @param localEnv
     * @param currentClass
     */
    protected void verifyDeclParam(DecacCompiler compiler,
        EnvironmentExp localEnv, ClassDefinition currentClass)
        throws ContextualError {
    }

    void verifyClassBody(DecacCompiler compiler,
    EnvironmentExp localEnv, ClassDefinition currentClass)
    throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }

    public void codeGen(DecacCompiler compiler){
        throw new UnsupportedOperationException("not yet implemented");
    }

    public abstract AbstractIdentifier getType();
}