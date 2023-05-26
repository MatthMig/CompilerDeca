package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
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
        // Vérifier la condition de gauche
        //  => si vrai, on execute le corps de la condition
        //  => sinon on vérifie le membre de droite
        Label ifLabel = compiler.createIfLabel();
        getLeftOperand().codeGenCondition(compiler, !neg, ifLabel);
        getRightOperand().codeGenCondition(compiler, neg, label);
        compiler.addLabel(ifLabel);
    }
}
