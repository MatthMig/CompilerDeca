package fr.ensimag.deca.tree;


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

}
