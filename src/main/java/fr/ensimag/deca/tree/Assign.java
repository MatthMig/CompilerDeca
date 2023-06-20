package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

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

        if(this.getLeftOperand() instanceof Selection && !((Selection)this.getLeftOperand()).getFieldName().getDefinition().isField()){
            throw new ContextualError("Cannot assign a value to anything that isn't a variable or a field", getLocation());
        }
        if((t1 == compiler.environmentType.FLOAT && t2 == compiler.environmentType.INT) || t1 == t2 
            || (t1.isClass() && t2.isClassOrNull())) {
            if (t1.isClass() && t2.isClass()) {
                ClassDefinition classDef1 = compiler.environmentType.defOfClass(t1.getName());
                ClassDefinition classDef2 = compiler.environmentType.defOfClass(t2.getName());
                if (!classDef1.isParentClassOf(classDef2)) {
                    throw new ContextualError("Trying to asign a value of type " + t2 + " to a value of type "+ t1, getLocation());
                }
            }
            this.setType(t1);
            return this.getType();
        }

        throw new ContextualError("Trying to assign value of type "+t2+" to a value of type " + t1, getLocation());
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        this.getRightOperand().codeGenExp(compiler, 2);

        if(this.getLeftOperand() instanceof Identifier){
            Identifier leftOperand  = (Identifier)this.getLeftOperand();
            DAddr leftAddr;

            if(leftOperand.getDefinition().isField()){
                compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB),Register.getR(1)));
                leftAddr = new RegisterOffset(leftOperand.getFieldDefinition().getIndex(), Register.getR(1));
            }

            else if(leftOperand.getDefinition().isParam()){
                leftAddr = leftOperand.getParamDefinition().getOperand();
            }

            else{
                leftAddr = leftOperand.getVariableDefinition().getOperand();
            }

            if( this.getLeftOperand().getType() == compiler.environmentType.FLOAT && this.getRightOperand().getType() == compiler.environmentType.INT){
                compiler.addInstruction(new FLOAT(GPRegister.getR(2), GPRegister.getR(2)));
            }
            compiler.addInstruction(new STORE(GPRegister.getR(2), leftAddr));
        }
        else if (this.getLeftOperand() instanceof Selection){
            Selection leftOperand = (Selection)this.getLeftOperand();
            compiler.incrementStackSize();
            compiler.addInstruction(new PUSH(GPRegister.getR(2)));
            this.getLeftOperand().codeGenInst(compiler);
            compiler.addInstruction(new POP(GPRegister.getR(3)));
            compiler.decrementStackSize();
            if(leftOperand.getFieldName().getDefinition().isField())
                compiler.addInstruction(new STORE(GPRegister.getR(3), new RegisterOffset(leftOperand.getFieldName().getFieldDefinition().getIndex(), Register.getR(2))));
        }
    }
    @Override
    protected String getOperatorName() {
        return "=";
    }

    @Override
    protected void codeGenExp(DecacCompiler compiler, int n) {
        DAddr leftAddr = ((Identifier)this.getLeftOperand()).getVariableDefinition().getOperand();
        this.getRightOperand().codeGenExp(compiler, n);

        if( this.getLeftOperand().getType() == compiler.environmentType.FLOAT && this.getRightOperand().getType() == compiler.environmentType.INT){
            compiler.addInstruction(new FLOAT(GPRegister.getR(n), GPRegister.getR(n)));
        }
        compiler.addInstruction(new STORE(GPRegister.getR(n), leftAddr));
    }

}
