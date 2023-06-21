package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;

import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

import java.io.PrintStream;

/**
 * Null literal
 * 
 * @author gl03
 * @date 14/06/2023
 */
public class NullLiteral extends AbstractExpr {

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
                           ClassDefinition currentClass)
            throws ContextualError {
        this.setType(compiler.environmentType.NULL);
        return compiler.environmentType.NULL;
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler) {
        // Nothing to do
    }

    @Override
    protected void codeGenExp(DecacCompiler compiler, int n) {
        compiler.addInstruction(new LOAD(new NullOperand(), GPRegister.getR(n)));
    }

    // Since the null literal is a leaf node with no children, these methods don't perform any action.
    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

    @Override
    String prettyPrintNode() {
        return "NullLiteral";
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("null");
    }
}
