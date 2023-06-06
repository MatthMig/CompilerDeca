package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
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
