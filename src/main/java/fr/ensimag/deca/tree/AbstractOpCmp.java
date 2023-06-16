package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 *
 * @author gl03
 * @date 21/04/2023
 */
public abstract class AbstractOpCmp extends AbstractBinaryExpr {

    public AbstractOpCmp(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type t1 = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type t2 = getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        // Si types differents ET que aucun des deux n'est un INT alors on n'est pas dans le cas de 'int CMP float'
        if(t1 != t2) {
            if (
                !( (t1.isInt() && t2.isFloat()) || (t1.isFloat() && t2.isInt()) )
            )
                // Types incomparables
                throw new ContextualError(
                    String.format("Cannot compare %s with %s because types are different", t1, t2),
                    getLocation()
                );

            else if (t1.isInt()){
                this.setLeftOperand(new ConvFloat(this.getLeftOperand()));
                this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
            }

            else if (t2.isInt()){
                this.setRightOperand(new ConvFloat(this.getRightOperand()));
                this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
            }
        }
        else if(t1.isBoolean() && t2.isBoolean()){
            if(!(this instanceof Equals || this instanceof NotEquals)){
                // Cannot compare booleans for anything that isn't equality
                throw new ContextualError(
                    String.format("Cannot compare booleans in the context of an %s", this.getClass()),
                    getLocation()
                );
            }
        }


        else if ((!t1.isFloat() && !t1.isInt()) || (!t2.isFloat() && !t2.isInt())){
            // Type incomparable
            throw new ContextualError(
                String.format("Cannot compare %s or %s", t1, t2),
                getLocation()
            );
        }

        setType(compiler.environmentType.BOOLEAN);
        return getType();
    }

    public void codeGenCondition(DecacCompiler compiler, Boolean neg, Label label) throws UnsupportedOperationException {
        // On the left there is only a variable or a literal or an arithmetic expression
        this.getLeftOperand().codeGenExp(compiler, 2);
        compiler.incrementStackSize();
        compiler.addInstruction(new PUSH(GPRegister.getR(2)), "sauvegarde");
        // On the right there is only a variable or a literal or an arithmetic expression
        this.getRightOperand().codeGenExp(compiler, 2);
        compiler.addInstruction(new POP(GPRegister.getR(3)), "restauration");
        compiler.decrementStackSize();
        // then compare
        compiler.addInstruction(new CMP(GPRegister.getR(2), GPRegister.getR(3)));
    }
}
