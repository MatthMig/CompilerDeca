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
        // Vérifier la condition de gauche
        //  => si vrai, on vérifie la droite
        //  => sinon on saute au else
        getLeftOperand().codeGenCondition(compiler, neg, label);
        getRightOperand().codeGenCondition(compiler, neg, label);
    }

    @Override
    protected String getOperatorName() {
        return "&&";
    }


}
