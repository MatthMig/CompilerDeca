package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BRA;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;

/**
 * Full if/else if/else statement.
 *
 * @author gl03
 * @date 21/04/2023
 */
public class IfThenElse extends AbstractInst {

    private final AbstractExpr condition;
    private final ListInst thenBranch;
    private ListInst elseBranch;

    public IfThenElse(AbstractExpr condition, ListInst thenBranch, ListInst elseBranch) {
        Validate.notNull(condition);
        Validate.notNull(thenBranch);
        Validate.notNull(elseBranch);
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
            condition.verifyCondition(compiler, localEnv, currentClass);             // verify the condition
            thenBranch.verifyListInst(compiler, localEnv, currentClass, returnType); // then the 'then' branch
            elseBranch.verifyListInst(compiler, localEnv, currentClass, returnType); // finally the 'else' branch
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        Label[] ifLabels = compiler.getLabelManager().createIfLabels();            // we create 2 labels for the if statement
        condition.codeGenCondition(compiler, false, ifLabels[0]); // generate the condition
        thenBranch.codeGenListInst(compiler);                     // generate the 'then' branch
        compiler.addInstruction(new BRA(ifLabels[1]));            // add a jump to the 'endif' label when the 'then' is done
        compiler.addLabel(ifLabels[0]);                           // add an 'else' label
        elseBranch.codeGenListInst(compiler);                     // finally generate the 'else' branch
        compiler.addLabel(ifLabels[1]);                           // add an 'endif' label
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("if ");
        this.condition.decompile(s);
        s.println("{");

        s.indent();
        this.thenBranch.decompile(s);
        s.unindent();

        s.println("} else { ");

        s.indent();
        this.elseBranch.decompile(s);
        s.unindent();

        s.print("}");
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        condition.iter(f);
        thenBranch.iter(f);
        elseBranch.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        condition.prettyPrint(s, prefix, false);
        thenBranch.prettyPrint(s, prefix, false);
        elseBranch.prettyPrint(s, prefix, true);
    }
}
