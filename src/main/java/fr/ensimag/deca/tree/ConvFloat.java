package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 * Conversion of an int into a float. Used for implicit conversions.
 *
 * @author gl03
 * @date 21/04/2023
 */
public class ConvFloat extends AbstractUnaryExpr {
    public ConvFloat(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) {

        // need to do some verification here, to add later
        setType(compiler.environmentType.FLOAT);
        return compiler.environmentType.FLOAT;
    }

    @Override
    protected void codeGenExp(DecacCompiler compiler, int n) {
        this.getOperand().codeGenExp(compiler, n);
        compiler.addInstruction(new FLOAT(GPRegister.getR(n),GPRegister.getR(n)));
    }


    @Override
    protected String getOperatorName() {
        return "/* conv float */";
    }

}
