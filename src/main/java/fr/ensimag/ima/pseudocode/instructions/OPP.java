package fr.ensimag.ima.pseudocode.instructions;

import fr.ensimag.ima.pseudocode.BinaryInstructionDValToReg;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;

/**
 * @author Ensimag
 * @date 21/04/2023
 */
public class OPP extends BinaryInstructionDValToReg {
    public OPP(DVal op1, GPRegister op2) {
        super(op1, op2);
    }
}
