package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;

/**
 *
 * @author gl03
 * @date 21/04/2023
 */
public class And extends AbstractOpBool {

    public And(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public void codeGenCondition(DecacCompiler compiler, Boolean neg, Label label) {
        // 2 different cases for AND whether neg is true or false
        if (neg) {
            // a new label is needed in that case
            Label endLabel = compiler.getLabelManager().createAndLabel();
            getLeftOperand().codeGenCondition(compiler, !neg, endLabel);
            getRightOperand().codeGenCondition(compiler, neg, label);
            compiler.addLabel(endLabel);
        } else {
            getLeftOperand().codeGenCondition(compiler, neg, label);
            getRightOperand().codeGenCondition(compiler, neg, label);
        }
    }

    @Override
    protected String getOperatorName() {
        return "&&";
    }


}
