package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.WINT;
import fr.ensimag.ima.pseudocode.instructions.WSTR;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import java.io.PrintStream;

/**
 *
 * @author gl03
 * @date 21/04/2023
 */
public class BooleanLiteral extends AbstractExpr {

    private boolean value;

    public BooleanLiteral(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        this.setType(compiler.environmentType.BOOLEAN);
        return compiler.environmentType.BOOLEAN;
    }

    @Override
    protected void codeGenExp(DecacCompiler compiler, int n) {
        if (this.getValue()) {
            compiler.addInstruction(new LOAD(1, GPRegister.getR(n)));
        } else {
            compiler.addInstruction(new LOAD(0, GPRegister.getR(n)));
        }
    }

    @Override
    protected DVal dval(DecacCompiler compiler) {
        if (this.getValue()) {
            return new ImmediateInteger(1);
        } else {
            return new ImmediateInteger(0);
        }
    }

    @Override
    public void codeGenCondition(DecacCompiler compiler, Boolean neg,Label labelElse) {
        if((!this.getValue() && !neg) || (this.getValue() && neg) ) {
            compiler.addInstruction(new BRA(labelElse));
        }
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler) {
        if(this.value){
            compiler.addInstruction(new WSTR("true"));
        }
        else{
            compiler.addInstruction(new WSTR("false"));
        }
    }


    @Override
    public void decompile(IndentPrintStream s) {
        s.print(Boolean.toString(value));
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

    @Override
    String prettyPrintNode() {
        return "BooleanLiteral (" + value + ")";
    }
}
