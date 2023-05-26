package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.BLE;
import fr.ensimag.ima.pseudocode.instructions.BGT;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

/**
 *
 * @author gl03
 * @date 21/04/2023
 */
public class LowerOrEqual extends AbstractOpIneq {
    public LowerOrEqual(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "<=";
    }

    @Override
    public void codeGenCondition(DecacCompiler compiler, Boolean neg, Label label) {
        ExpDefinition varDef = compiler.getVar(((Identifier)this.getLeftOperand()).getName());
        compiler.addInstruction(new LOAD(varDef.getOperand(), GPRegister.getR(2)));

        if (this.getRightOperand().getClass() == IntLiteral.class) {
            compiler.addInstruction(new CMP( ((IntLiteral)this.getRightOperand()).getValue() , GPRegister.getR(2)));
        } else {
            ExpDefinition rightVar = compiler.getVar(((Identifier)this.getRightOperand()).getName());
            compiler.addInstruction(new LOAD(rightVar.getOperand(), GPRegister.getR(1)));
            compiler.addInstruction(new CMP(GPRegister.getR(1), GPRegister.getR(2)));
        }
        if (!neg) {
            compiler.addInstruction(new BLE(label));
        } else {
            compiler.addInstruction(new BGT(label));
        }
    }
}
