package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;

import org.apache.commons.lang.Validate;
import java.io.PrintStream;
import fr.ensimag.deca.tools.IndentPrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

public class MethodAsmBody extends AbstractMethodBody{
    final private StringLiteral asmCode;

    public MethodAsmBody(StringLiteral asmCode) {
        Validate.notNull(asmCode);
        this.asmCode = asmCode;
    }

    /**
     * @param compiler
     * @param localEnv
     * @param currentClass
     */
    protected void verifyMethodBody(DecacCompiler compiler,
        EnvironmentExp localEnv, ClassDefinition currentClass)
        throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
    }

    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    public void codeGen(DecacCompiler compiler){
        throw new UnsupportedOperationException("not yet implemented");
    }
}