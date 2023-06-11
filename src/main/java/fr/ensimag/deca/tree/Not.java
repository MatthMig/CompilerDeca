package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 *
 * @author gl03
 * @date 21/04/2023
 */
public class Not extends AbstractUnaryExpr {

    public Not(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type t = this.getOperand().verifyExpr(compiler, localEnv, currentClass);
        this.setType(t);
        return t;
    }

    @Override
    public void codeGenCondition(DecacCompiler compiler, Boolean neg, Label label) {
        this.getOperand().codeGenCondition(compiler, !neg, label);
    }

    @Override
    protected String getOperatorName() {
        return "!";
    }

    @Override
    protected void codeGenExp(DecacCompiler compiler, int n) {
        // Generate new labels for the Boolean expression
        Label [] labels = compiler.getLabelManager().createBooleanExpLabel();

        // Generate unerlying expression as a condition
        codeGenCondition(compiler, false, labels[0]);

        // Case condition did evalutate as true
        compiler.addInstruction(new LOAD(1, GPRegister.getR(n)));
        compiler.addInstruction(new BRA(labels[1]));

        // Other case
        compiler.addLabel(labels[0]);
        compiler.addInstruction(new LOAD(0, GPRegister.getR(n)));

        // End
        compiler.addLabel(labels[1]);
    }
}
