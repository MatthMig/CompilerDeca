package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
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
            if (!compiler.getNoCheck()) {
                throw new UnsupportedOperationException("not yet implemented");
            }
            throw new UnsupportedOperationException("not yet implemented");
    }


    @Override
    protected String getOperatorName() {
        return "/* conv float */";
    }

}
