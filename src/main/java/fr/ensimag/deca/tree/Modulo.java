package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.REM;

/**
 *
 * @author gl03
 * @date 21/04/2023
 */
public class Modulo extends AbstractOpArith {

    public Modulo(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type leftOperand;
        Type rightOperand;
        try {
            leftOperand = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
            rightOperand = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        } catch (ContextualError e) {
            throw e;
        }
        
        // if one of the operands is not an int: problem !
        if ((! leftOperand.isInt()) || (! rightOperand.isInt())) {
            throw new ContextualError("modulo is only allowed for integers",this.getLocation());
        }

        // we set the type of the expression to int
        this.setType(leftOperand);
        return leftOperand;

    }

    @Override
    protected void codeGenMnemo(DecacCompiler compiler, DVal a, GPRegister b) {
        compiler.addInstruction(new REM(a, b));
    }

    @Override
    protected String getOperatorName() {
        return "%";
    }

}
