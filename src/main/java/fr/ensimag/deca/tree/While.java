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
 *
 * @author gl03
 * @date 21/04/2023
 */
public class While extends AbstractInst {
    private AbstractExpr condition;
    private ListInst body;

    public AbstractExpr getCondition() {
        return condition;
    }

    public ListInst getBody() {
        return body;
    }

    public While(AbstractExpr condition, ListInst body) {
        Validate.notNull(condition);
        Validate.notNull(body);
        this.condition = condition;
        this.body = body;
    }

    @Override
    /**
     * Generates
     * Jump whileCond
     * whileLoop:
     * loop body
     * whileCond:
     * condition
     * True : whileLoop
     * @param compiler
     */
    protected void codeGenInst(DecacCompiler compiler) {
        Label[] whenLabels = compiler.getLabelManager().createWhileLabels(); // we create 2 labels for the while statement
        compiler.addInstruction(new BRA(whenLabels[1]));   // add a jump to the 'whileCond' label
        compiler.addLabel(whenLabels[0]);                  // add a 'whileBody' label
        body.codeGenListInst(compiler);                    // generate the 'while' body
        compiler.addLabel(whenLabels[1]);                  // add a 'whileCond' label
        condition.codeGenCondition(compiler, true, whenLabels[0]); // generate the condition
    }

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
            condition.verifyCondition(compiler, localEnv, currentClass);       // verify the condition
            body.verifyListInst(compiler, localEnv, currentClass, returnType); // then the 'while' body
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("while (");
        getCondition().decompile(s);
        s.println(") {");
        s.indent();
        getBody().decompile(s);
        s.unindent();
        s.print("}");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        condition.iter(f);
        body.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        condition.prettyPrint(s, prefix, false);
        body.prettyPrint(s, prefix, true);
    }

}
