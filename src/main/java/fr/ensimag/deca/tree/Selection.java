package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;

/**
 *
 * @author gl03
 * @date 13/06/2023
 */
public class Selection extends AbstractExpr {
    final private AbstractExpr operand;
    final private AbstractIdentifier fieldName;
    final private ListExpr params;

    public Selection(AbstractExpr operand, AbstractIdentifier fieldName, ListExpr params) {
        Validate.notNull(operand);
        Validate.notNull(fieldName);
        Validate.notNull(params);
        this.operand = operand;
        this.fieldName = fieldName;
        this.params = params;
    }
    // Difference between the 2 constructors is in the 'params' arg, if it is null then
    // we have a simple selection, otherwise we have a method call.
    public Selection(AbstractExpr operand, AbstractIdentifier fieldName) {
        Validate.notNull(operand);
        Validate.notNull(fieldName);
        this.operand = operand;
        this.fieldName = fieldName;
        this.params = null;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        operand.prettyPrint(s, prefix, false);
        if (params != null) {
            fieldName.prettyPrint(s, prefix, false);
            params.prettyPrint(s, prefix, true);
        } else {
            fieldName.prettyPrint(s, prefix, true);
        }
    }

   @Override
   public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
           throws ContextualError {
       throw new UnsupportedOperationException("not yet implemented");
   }


    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        throw new UnsupportedOperationException("not yet implemented");
    }

}
