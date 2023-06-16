package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.RTS;

import java.io.PrintStream;

/**
 *
 * @author gl03
 * @date 13/06/2023
 */
public class Return extends AbstractInst {
    final private AbstractExpr returnExpr;

    public Return(AbstractExpr returnExpr) {
        // No null verification, null is allowed to return void.
        this.returnExpr = returnExpr;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        this.returnExpr.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        if (returnExpr != null) {
            returnExpr.prettyPrint(s, prefix, true);
        }
    }

   @Override
   protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass,
           Type returnType) throws ContextualError {
        Type t;
        if (this.returnExpr == null) {
            // If there is no return expression, then the returned type is void.
            t = compiler.environmentType.VOID;
        } else {
            // Else we verify the expression and set the returned type to the expression's type.
            t = this.returnExpr.verifyExpr(compiler, localEnv, currentClass);
            this.returnExpr.setType(t);
        }
        // If the returned type is not the same as the method's return type, then there is a contextual error.
        if (!t.sameType(returnType))
            throw new ContextualError("Return type must be " + returnType + ", currently is " + t, this.getLocation());
   } 


    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        this.returnExpr.codeGenExp(compiler, 2);
        compiler.addInstruction(new RTS());
    }

}
