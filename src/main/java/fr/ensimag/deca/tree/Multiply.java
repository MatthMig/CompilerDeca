package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.MUL;
import fr.ensimag.ima.pseudocode.instructions.BOV;

/**
 * @author gl03
 * @date 21/04/2023
 */
public class Multiply extends AbstractOpArith {
    public Multiply(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected void codeGenMnemo(DecacCompiler compiler, DVal a, GPRegister b) {
        compiler.addInstruction(new MUL(a, b));
        if(!compiler.getNoCheck())
            compiler.addInstruction(new BOV(compiler.getLabelManager().getOverflowLabel()));
    }

    @Override
    protected String getOperatorName() {
        return "*";
    }

}
