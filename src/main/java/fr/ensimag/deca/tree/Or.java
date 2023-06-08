package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;

/**
 *
 * @author gl03
 * @date 21/04/2023
 */
public class Or extends AbstractOpBool {

    public Or(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "||";
    }

    @Override
    public void codeGenCondition(DecacCompiler compiler, Boolean neg, Label label) {
        // 2 different cases for OR whether neg is true or false (same as AND the other way around)
        if (!neg) {
            Label endLabel = new Label("endAnd."+ compiler.getLabelCount());
            compiler.incrementLabelCount();
            getLeftOperand().codeGenCondition(compiler, !neg, endLabel);
            getRightOperand().codeGenCondition(compiler, neg, label);
            compiler.addLabel(endLabel);
        } else {
            getLeftOperand().codeGenCondition(compiler, neg, label);
            getRightOperand().codeGenCondition(compiler, neg, label);
        }
    }
}
