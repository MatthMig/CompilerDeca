package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

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
    protected void codeGenExp(DecacCompiler compiler, int n){
        if(this.getRightOperand().getClass() == IntLiteral.class 
        || this.getRightOperand().getClass() == FloatLiteral.class 
        || this.getRightOperand().getClass() == Identifier.class){
            this.getLeftOperand().codeGenExp(compiler, n);
            DVal a = this.getRightOperand().dval(compiler);

            this.codeGenMnemo(compiler, a, GPRegister.getR(n));
        }

        else if (n < 15){
            this.getLeftOperand().codeGenExp(compiler, n);
            this.getRightOperand().codeGenExp(compiler, n+1);

            DVal a = GPRegister.getR(n+1);
            this.codeGenMnemo(compiler, a, GPRegister.getR(n));
        }

        else{
            this.getLeftOperand().codeGenExp(compiler, n);
            compiler.incrementStackSize();
            compiler.addInstruction(new PUSH(GPRegister.getR(n)),"sauvegarde");
            this.getRightOperand().codeGenExp(compiler, n);
            
            compiler.addInstruction(new LOAD(GPRegister.getR(n), GPRegister.getR(1)));
            compiler.addInstruction(new POP(GPRegister.getR(n)),"restauration");
            

            DVal a = GPRegister.getR(1);

            this.codeGenMnemo(compiler, a, GPRegister.getR(n));
        }

    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {

        Type t1;
        Type t2;

        try{
            t1 = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
            t2 = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        } catch (ContextualError e) {
            throw e;
        }

        //if one of the operands is not a number: problem !
        
        if (!t1.isInt()&&!t1.isFloat()) {
            throw new ContextualError("left operand is not an int nor a float",this.getLocation());

        } else if (!t2.isInt()&&!t2.isFloat()){
            throw new ContextualError("right operand is not an int nor a float",this.getLocation());
        
        // both operands are valid numbers
        } else {
            // operands have different types 
            if (t1.isFloat() && t2.isInt()){
                // convert int to float
                this.setRightOperand(this.getRightOperand().verifyRValue(compiler, localEnv, currentClass,t1));
                // we set the type of the expression to float (t1)
                this.setType(t1);
                return t1;
            } else if (t1.isInt() && t2.isFloat()) {
                // convert int to float
                this.setLeftOperand(this.getLeftOperand().verifyRValue(compiler, localEnv, currentClass, t2));
                // we set the type of the expression to float (t2)
                this.setType(t2);
                return t2;
            }
        }
        // here it means operands have the same type, we can set the type of the expression to t1 for example
        this.setType(t1);
        return t1;
    }
}
