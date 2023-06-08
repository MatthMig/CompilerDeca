package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BLE;
import fr.ensimag.ima.pseudocode.instructions.BGT;

/**
 *
 * @author gl03
 * @date 21/04/2023
 */
public class Greater extends AbstractOpIneq {

    public Greater(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return ">";
    }

    @Override
    public void codeGenCondition(DecacCompiler compiler, Boolean neg, Label label) throws UnsupportedOperationException {
        super.codeGenCondition(compiler, neg, label);
        
        if (!neg) {
            compiler.addInstruction(new BLE(label));
        } else {
            compiler.addInstruction(new BGT(label));
        }
    }
}
