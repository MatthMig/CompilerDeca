package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
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
        }
        
        setType(compiler.environmentType.BOOLEAN);
        return getType();
    }
}
