package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.instructions.*;
import fr.ensimag.ima.pseudocode.Label;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * Deca complete program (class definition plus main block)
 *
 * @author gl03
 * @date 21/04/2023
 */
public class Program extends AbstractProgram {
    private static final Logger LOG = Logger.getLogger(Program.class);

    public Program(ListDeclClass classes, AbstractMain main) {
        Validate.notNull(classes);
        Validate.notNull(main);
        this.classes = classes;
        this.main = main;
    }
    public ListDeclClass getClasses() {
        return classes;
    }
    public AbstractMain getMain() {
        return main;
    }
    private ListDeclClass classes;
    private AbstractMain main;

    @Override
    public void verifyProgram(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify program: start");
        classes.verifyListClass(compiler);
        classes.verifyListClassMembers(compiler);
        classes.verifyListClassBody(compiler);
        main.verifyMain(compiler);
        LOG.debug("verify program: end");
    }

    @Override
    public void codeGenProgram(DecacCompiler compiler) {
        compiler.addComment("Main program");
        classes.codeGenMethodTable(compiler);
        main.codeGenMain(compiler);
        compiler.addFirst(new ADDSP(compiler.getLBOffset()), "number of vars + size of the VTable");
        compiler.addFirst(new BOV(compiler.getLabelManager().getStackOverflowLabel()),"check for stack overflows");
        compiler.addFirst(new TSTO(compiler.getMaxStackSize() + compiler.getLBOffset()), "size of stack needed");
        compiler.addInstruction(new HALT());
        classes.codeGenEqualsMethod(compiler);
        classes.codeGenClasses(compiler);

        compiler.addLabel(new Label("io_error"));
        compiler.addInstruction(new WSTR("Error : Input/Output error"));
        compiler.addInstruction(new WNL());
        compiler.addInstruction(new ERROR());

        if(!compiler.getNoCheck()){
            compiler.addLabel(compiler.getLabelManager().getOverflowLabel());
            compiler.addInstruction(new WSTR("Error : Overflow on an arithmetic operation"));
            compiler.addInstruction(new WNL());
            compiler.addInstruction(new ERROR());

            compiler.addLabel(compiler.getLabelManager().createNullPointerLabel());
            compiler.addInstruction(new WSTR("Error : Null pointer error"));
            compiler.addInstruction(new WNL());
            compiler.addInstruction(new ERROR());

            compiler.addLabel(compiler.getLabelManager().getZeroDivisionLabel());
            compiler.addInstruction(new WSTR("Error : Division by zero"));
            compiler.addInstruction(new WNL());
            compiler.addInstruction(new ERROR());

            compiler.addLabel(compiler.getLabelManager().getStackOverflowLabel());
            compiler.addInstruction(new WSTR("Error : Stack overflow"));
            compiler.addInstruction(new WNL());
            compiler.addInstruction(new ERROR());

            compiler.addLabel(compiler.getLabelManager().getImpossibleDownCastLabel());
            compiler.addInstruction(new WSTR("Error : Impossible downcast"));
            compiler.addInstruction(new WNL());
            compiler.addInstruction(new ERROR());
        }
    }

    @Override
    public void decompile(IndentPrintStream s) {
        getClasses().decompile(s);
        getMain().decompile(s);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        classes.iter(f);
        main.iter(f);
    }
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        classes.prettyPrint(s, prefix, false);
        main.prettyPrint(s, prefix, true);
    }
}
