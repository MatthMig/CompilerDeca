package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 *
 * @author gl03
 * @date 21/04/2023
 */
public abstract class AbstractOpBool extends AbstractBinaryExpr {

    public AbstractOpBool(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type t1 = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type t2 = getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        if(t1 != compiler.environmentType.BOOLEAN || t2 != compiler.environmentType.BOOLEAN) {
            throw new ContextualError("Condition must be a boolean", getLocation());
        }
        this.setType(compiler.environmentType.BOOLEAN);
        return this.getType();
    }

    @Override
    protected void codeGenExp(DecacCompiler compiler, int n) {
        // Generate new labels for the Boolean expression
        Label [] labels = compiler.getLabelManager().createBooleanExpLabel();

        // Generate unerlying expressions as a condition
        codeGenCondition(compiler, false, labels[0]);

        // Case condition did evalutate as true
        compiler.addInstruction(new LOAD(1, GPRegister.getR(n)));
        compiler.addInstruction(new BRA(labels[1]));

        // Other case
        compiler.addLabel(labels[0]);
        compiler.addInstruction(new LOAD(0, GPRegister.getR(n)));

        // End
        compiler.addLabel(labels[1]);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        codeGenExp(compiler, 2);
    }
}
