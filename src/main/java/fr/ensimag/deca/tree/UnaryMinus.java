package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.OPP;
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

        t = this.getOperand().verifyExpr(compiler, localEnv, currentClass);
        // if operand is not a number: problem !
        if (!t.isInt() && !t.isFloat()) {
            throw new ContextualError("operand after minus is not a number",this.getOperand().getLocation());
        }

        // we set the type of the expression
        this.setType(t);
        return t;
    }

    @Override
    protected void codeGenExp(DecacCompiler compiler, int n) {

        // Generate code for operand
        this.getOperand().codeGenExp(compiler, n);
        compiler.addInstruction(new OPP(GPRegister.getR(n),GPRegister.getR(n)));
    }

    @Override
    protected String getOperatorName() {
        return "-";
    }

}
