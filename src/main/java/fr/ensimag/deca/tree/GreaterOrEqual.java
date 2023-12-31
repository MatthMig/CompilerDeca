package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BLT;
import fr.ensimag.ima.pseudocode.instructions.BGE;
import fr.ensimag.ima.pseudocode.instructions.SGE;
import fr.ensimag.ima.pseudocode.GPRegister;
/**
 * Operator "x >= y"
 *
 * @author gl03
 * @date 21/04/2023
 */
public class GreaterOrEqual extends AbstractOpIneq {

    public GreaterOrEqual(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return ">=";
    }

    @Override
    public void codeGenCondition(DecacCompiler compiler, Boolean neg, Label label) {
        super.codeGenCondition(compiler, neg, label);

        if (!neg) {
            compiler.addInstruction(new BLT(label));
        } else {
            compiler.addInstruction(new BGE(label));
        }
    }

    @Override
    protected void codeGenExp(DecacCompiler compiler, int n) {
        super.codeGenCondition(compiler, false, null);
        compiler.addInstruction(new SGE(GPRegister.getR(n)));
    }
}
