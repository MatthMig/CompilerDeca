package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.SNE;
import fr.ensimag.ima.pseudocode.GPRegister;
/**
 *
 * @author gl03
 * @date 21/04/2023
 */
public class NotEquals extends AbstractOpExactCmp {

    public NotEquals(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "!=";
    }

    @Override
    public void codeGenCondition(DecacCompiler compiler, Boolean neg, Label label) {
        super.codeGenCondition(compiler, neg, label);

        if (!neg) {
            compiler.addInstruction(new BEQ(label));
        } else {
            compiler.addInstruction(new BNE(label));
        }
    }

    @Override
    protected void codeGenExp(DecacCompiler compiler, int n) {
        super.codeGenCondition(compiler, false, null);
        compiler.addInstruction(new SNE(GPRegister.getR(n)));
    }
}
