package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.IntType;
import fr.ensimag.deca.tree.ConvFloat;


/**
 * Arithmetic binary operations (+, -, /, ...)
 *
 * @author gl03
 * @date 21/04/2023
 */
public abstract class AbstractOpArith extends AbstractBinaryExpr {

    public AbstractOpArith(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    protected void codeGenMnemo(DecacCompiler compiler, DVal a, GPRegister b) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public void decompile(PrintStream s) {
        this.getLeftOperand().decompile(s);
        s.print(getOperatorName());
        this.getRightOperand().decompile(s);
    }

    @Override
    protected void codeGenExp(DecacCompiler compiler, int n){
        if(this.getRightOperand().getClass() == IntLiteral.class
        || this.getRightOperand().getClass() == FloatLiteral.class
        || this.getRightOperand().getClass() == Identifier.class){
            this.getLeftOperand().codeGenExp(compiler, n);
            DVal a = this.getRightOperand().dval(compiler);

            this.codeGenMnemo(compiler, a, GPRegister.getR(n));
        }

        else if (n < compiler.getRMAX()){
            this.getLeftOperand().codeGenExp(compiler, n);
            this.getRightOperand().codeGenExp(compiler, n+1);

            DVal regN = GPRegister.getR(n+1);
            compiler.incrementMaxRegister();
            this.codeGenMnemo(compiler, regN, GPRegister.getR(n));
            compiler.decrementMaxRegister();
        }

        else{
            this.getLeftOperand().codeGenExp(compiler, n);
            compiler.incrementStackSize();
            compiler.addInstruction(new PUSH(GPRegister.getR(n)),"sauvegarde");
            this.getRightOperand().codeGenExp(compiler, n);

            compiler.addInstruction(new LOAD(GPRegister.getR(n), GPRegister.getR(1)));
            compiler.addInstruction(new POP(GPRegister.getR(n)),"restauration");
            compiler.decrementStackSize();

            DVal regN = GPRegister.getR(1);
            this.codeGenMnemo(compiler, regN, GPRegister.getR(n));
        }

    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {

        Type t1;
        Type t2;

        t1 = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        t2 = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);

        //if one of the operands is not a number: problem !

        if (!t1.isInt()&&!t1.isFloat()) {
            throw new ContextualError("left operand is not an int nor a float",this.getLocation());

        }

        if (!t2.isInt()&&!t2.isFloat()){
            throw new ContextualError("right operand is not an int nor a float",this.getLocation());
        }
        // Check if the operation is division and the divisor is 0
        if (this instanceof Divide && ((this.getRightOperand().getClass() == IntLiteral.class && ((IntLiteral) this.getRightOperand()).getValue() == 0)
                                            || ( this.getRightOperand().getClass() == FloatLiteral.class &&  (((FloatLiteral) this.getRightOperand()).getValue() == 0.0)))){
            throw new ContextualError("division by zero not allowed", this.getLocation());

        }

        if (this instanceof Divide) {
            if(t2.isInt()){
                this.setRightOperand(new ConvFloat(this.getRightOperand()));
                t2 = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
            }

            if(t1.isInt()){
                this.setLeftOperand(new ConvFloat(this.getLeftOperand()));
                t1 = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
            }
        }
        else {
            // Operands have different types
            if (t1.isFloat() && t2.isInt()) {;
                this.setRightOperand(new ConvFloat(this.getRightOperand()));
                t2 = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
            } else if (t1.isInt() && t2.isFloat()) {
                this.setLeftOperand(new ConvFloat(this.getLeftOperand()));
                t1 = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
            }
        }
        // here it means operands have the same type, we can set the type of the expression to t1 for example
        this.setType(t1);
        return t1;
    }
}
