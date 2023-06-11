package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
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

    // vous êtes nuls vous avez pas implémentés du coup on doit faire de la merde et Emilien il est pas content
    // Le contenu du if (!compiler.getNoCheck()) devra être remplacé par la gestion des erreurs de la fonction lors de son implémentation
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) {

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
        //return "/* conv float */";
        return "";
    }

        @Override
    public void decompile(IndentPrintStream s){
        this.getOperand().decompile(s);
    }

}
