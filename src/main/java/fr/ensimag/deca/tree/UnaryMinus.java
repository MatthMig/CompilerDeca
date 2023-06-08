package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 * @author gl03
 * @date 21/04/2023
 */
public class UnaryMinus extends AbstractUnaryExpr {

    public UnaryMinus(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        
        Type t;

        try {
            t = this.getOperand().verifyExpr(compiler, localEnv, currentClass);
        } catch (ContextualError e) {
            throw e;
        }
        
        // if operand is not a number: problem !
        if (!t.isInt() && !t.isFloat()) {
            throw new ContextualError("operand after minus is not a number",this.getOperand().getLocation());
        }

        // we set the type of the expression
        this.setType(t);
        return t;
    }


    @Override
    protected String getOperatorName() {
        return "-";
    }

}
