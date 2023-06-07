package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;

/**
 * Assignment, i.e. lvalue = expr.
 *
 * @author gl03
 * @date 21/04/2023
 */
public class Assign extends AbstractBinaryExpr {

    @Override
    public AbstractLValue getLeftOperand() {
        // The cast succeeds by construction, as the leftOperand has been set
        // as an AbstractLValue by the constructor.
        return (AbstractLValue)super.getLeftOperand();
    }

    public Assign(AbstractLValue leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type t1 = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type t2 = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        if( t1 == compiler.environmentType.FLOAT && t2 == compiler.environmentType.INT || t1 == t2) {
            this.setType(t1);
            return this.getType();
        }
        throw new ContextualError(getOperatorName(), getLocation());
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        ExpDefinition varDef = compiler.getVar(((Identifier)this.getLeftOperand()).getName());
        DAddr leftAddr = varDef.getOperand();
        this.getRightOperand().codeGenExp(compiler, 2);

        if( this.getLeftOperand().getType() == compiler.environmentType.FLOAT && this.getRightOperand().getType() == compiler.environmentType.INT){
            compiler.addInstruction(new FLOAT(GPRegister.getR(2), GPRegister.getR(2)));
        }
        compiler.addInstruction(new STORE(GPRegister.getR(2), leftAddr));
    }
    @Override
    protected String getOperatorName() {
        return "=";
    }

}
