package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;

import java.io.PrintStream;

public class InstanceOf extends AbstractOpCmp {

    // represents the expression that needs to be checked for being an instance of a certain class.
    private final AbstractExpr leftExpr;

    // represents the name of the class that is being tested against.
    private final AbstractIdentifier rightClass;

    // constructor for InstanceOf
    public InstanceOf(AbstractExpr leftExpr, AbstractIdentifier rightClass) {
        super(leftExpr, rightClass);
        this.leftExpr = leftExpr;
        this.rightClass = rightClass;
    }

    @Override
    protected String getOperatorName() {
        return "instanceof";
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError
    {
        // to verify the types of both operands
        Type leftExprType = leftExpr.verifyExpr(compiler, localEnv, currentClass);
        Type rightClassType = rightClass.verifyType(compiler);

        if (!leftExprType.isClass()) {
            throw new ContextualError("instanceof left operand type is " + leftExprType.getName().getName() + " which is not a class",
                    leftExpr.getLocation());
        }

        if (!rightClassType.isClass()) {
            throw new ContextualError("instanceof right operand " + rightClassType.getName().getName() + " is not a class",
                    rightClass.getLocation());
        }

        setType(compiler.environmentType.BOOLEAN);
        this.rightClass.setDefinition(compiler.environmentType.defOfClass(rightClassType.getName()));
        return compiler.environmentType.BOOLEAN;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        leftExpr.decompile(s);
        s.print(" instanceof ");
        rightClass.decompile(s);
    }

    // iterating over the children of the instanceOf expression and applying f to each child. 
    @Override
    protected void iterChildren(TreeFunction f) {
        leftExpr.iter(f);
        rightClass.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        leftExpr.prettyPrint(s, prefix, false);
        rightClass.prettyPrint(s, prefix, true);
    }

    @Override
    public void codeGenCondition(DecacCompiler compiler, Boolean neg, Label label) throws UnsupportedOperationException {
        codeGenExp(compiler, 2);
        compiler.addInstruction(new CMP(1, GPRegister.getR(2)));
        if (neg) {
            compiler.addInstruction(new BEQ(label));
        } else {
            compiler.addInstruction(new BNE(label));
        }
    }

    @Override
    protected void codeGenExp(DecacCompiler compiler, int n) {
        compiler.addComment("instanceof");
        Label[] labels = compiler.getLabelManager().createInstanceOfLabels();
        rightClass.codeGenExp(compiler, n);
        compiler.incrementStackSize();
        compiler.addInstruction(new PUSH(GPRegister.getR(n)));
        leftExpr.codeGenExp(compiler, n);
        compiler.addInstruction(new POP(GPRegister.getR(n+1)));
        compiler.decrementStackSize();
        // while cond
        compiler.addLabel(labels[0]);
        compiler.addInstruction(new CMP(new RegisterOffset(0, GPRegister.getR(n)), GPRegister.getR(n+1)));
        // is instanceof jump to true
        compiler.addInstruction(new BEQ(labels[1]));
        // go for the parent class
        compiler.addInstruction(new LOAD(new RegisterOffset(0, GPRegister.getR(n)), GPRegister.getR(n)));
        // check for null
        compiler.addInstruction(new CMP(new NullOperand(), GPRegister.getR(n)));
        // if it is null jump to false
        compiler.addInstruction(new BEQ(labels[2]));
        compiler.addInstruction(new BRA(labels[0]));
        // label for true
        compiler.addLabel(labels[1]);
        compiler.addInstruction(new LOAD(1, GPRegister.getR(n)));
        // jump to the end
        compiler.addInstruction(new BRA(labels[3]));
        // label for false
        compiler.addLabel(labels[2]);
        compiler.addInstruction(new LOAD(0, GPRegister.getR(n)));
        // label for end
        compiler.addLabel(labels[3]);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        codeGenExp(compiler, 2);
    }
}
