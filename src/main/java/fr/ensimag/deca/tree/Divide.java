package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.DIV;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.Label;

/**
 *
 * @author gl03
 * @date 21/04/2023
 */
public class Divide extends AbstractOpArith {
    public Divide(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected void codeGenMnemo(DecacCompiler compiler, DVal a, GPRegister b) {
        compiler.addInstruction(new DIV(a, b));
        if(!compiler.getNoCheck())
            compiler.addInstruction(new BOV(compiler.getLabelManager().getZeroDivisionLabel()));
    }


    @Override
    protected String getOperatorName() {
        return "/";
    }

}
