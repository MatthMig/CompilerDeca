package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
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
public class Not extends AbstractUnaryExpr {

    public Not(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type t = this.getOperand().verifyExpr(compiler, localEnv, currentClass);
        this.setType(t);
        return t;
    }

    @Override
    public void codeGenCondition(DecacCompiler compiler, Boolean neg, Label label) {
        this.getOperand().codeGenCondition(compiler, !neg, label);
    }

    @Override
    public void decompile(IndentPrintStream s){
        s.print(this.getOperatorName() + "(");
        this.getOperand().decompile(s);
        s.print(")");
    }

    @Override
    protected String getOperatorName() {
        return "!";
    }
}
