package fr.ensimag.deca.tools;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.WSTR;

public class BooleanPrintHelper {

    DecacCompiler compiler;

    public BooleanPrintHelper(DecacCompiler compiler) {
        this.compiler = compiler;
    }

    public void codeGenPrint() {
        Label[] labels = this.compiler.getLabelManager().createBooleanPrintLabel();
        this.compiler.addInstruction(new CMP(1, GPRegister.getR(1)));
        this.compiler.addInstruction(new BEQ(labels[0]));
        this.compiler.addInstruction(new WSTR("false"));
        this.compiler.addInstruction(new BRA(labels[1]));
        this.compiler.addLabel(labels[0]);
        this.compiler.addInstruction(new WSTR("true"));
        this.compiler.addLabel(labels[1]);
    }
}
