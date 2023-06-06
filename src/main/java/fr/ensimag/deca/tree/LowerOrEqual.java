package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.BLE;
import fr.ensimag.ima.pseudocode.instructions.BGT;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
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
        // Check Left Value's Type
        if (this.getLeftOperand().getClass() == Identifier.class) {
            // Load a variable content to R2
            ExpDefinition leftVar = compiler.getVar(((Identifier)this.getLeftOperand()).getName());
            compiler.addInstruction(new LOAD(leftVar.getOperand(), GPRegister.getR(2)));
        } else if (this.getLeftOperand().getType().isInt()) {
            // Load an Int to R2
            compiler.addInstruction(new LOAD(((IntLiteral)this.getLeftOperand()).getValue(), GPRegister.getR(2)));
        } else if (this.getLeftOperand().getType().isFloat()) {
            // Load a Float to R2
            compiler.addInstruction(new LOAD(((FloatLiteral)this.getLeftOperand()).getValue(), GPRegister.getR(2)));
        }

        // Check Right Value's Type
        if (this.getRightOperand().getClass() == Identifier.class) {
            // Load a variable content to R3
            ExpDefinition rightVar = compiler.getVar(((Identifier)this.getRightOperand()).getName());
            compiler.addInstruction(new LOAD(rightVar.getOperand(), GPRegister.getR(3)));
        } else if (this.getRightOperand().getType().isInt()) {
            // Load an Int to R3
            compiler.addInstruction(new LOAD(((IntLiteral)this.getRightOperand()).getValue(), GPRegister.getR(3)));
        } else if (this.getRightOperand().getType().isFloat()) {
            // Load a Float to R3
            compiler.addInstruction(new LOAD(((FloatLiteral)this.getRightOperand()).getValue(), GPRegister.getR(3)));
        }

        // If we have a float, we need to cast the one that is not
        if (this.getLeftOperand().getType().isFloat() ^ this.getRightOperand().getType().isFloat()) {
            if (this.getLeftOperand().getType().isFloat()) {
                compiler.addInstruction(new FLOAT(GPRegister.getR(3), GPRegister.getR(3)));
            } else {
                compiler.addInstruction(new FLOAT(GPRegister.getR(2), GPRegister.getR(2)));
            }
        }
        
        // then compare
        compiler.addInstruction(new CMP(GPRegister.getR(3), GPRegister.getR(2)));

        if (!neg) {
            compiler.addInstruction(new BLE(label));
        } else {
            compiler.addInstruction(new BGT(label));
        }
    }
}
