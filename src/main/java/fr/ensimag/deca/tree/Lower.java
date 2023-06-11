package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BLT;
import fr.ensimag.ima.pseudocode.instructions.BGE;
import fr.ensimag.ima.pseudocode.instructions.SLT;
import fr.ensimag.ima.pseudocode.GPRegister;
/**
 *
 * @author gl03
 * @date 21/04/2023
 */
public class Lower extends AbstractOpIneq {

    public Lower(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "<";
    }

    @Override
    public void codeGenCondition(DecacCompiler compiler, Boolean neg, Label label) throws UnsupportedOperationException {
        super.codeGenCondition(compiler, neg, label);

        if (!neg) {
            compiler.addInstruction(new BGE(label));
        } else {
            compiler.addInstruction(new BLT(label));
        }
    }

    @Override
    protected void codeGenExp(DecacCompiler compiler, int n) {
        super.codeGenCondition(compiler, false, null);
        compiler.addInstruction(new SLT(GPRegister.getR(n)));
    }
}
