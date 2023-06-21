package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.WFLOAT;
import fr.ensimag.ima.pseudocode.instructions.WINT;
import fr.ensimag.ima.pseudocode.instructions.WSTR;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;

/**
 * Print statement (print, println, ...).
 *
 * @author gl03
 * @date 21/04/2023
 */
public abstract class AbstractPrint extends AbstractInst {

    private boolean printHex;
    private ListExpr arguments = new ListExpr();

    abstract String getSuffix();

    public AbstractPrint(boolean printHex, ListExpr arguments) {
        Validate.notNull(arguments);
        this.arguments = arguments;
        this.printHex = printHex;
    }

    public ListExpr getArguments() {
        return arguments;
    }

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
                
        int i = 0;
        for (AbstractExpr expr : arguments.getList()) {
            expr.verifyExpr(compiler, localEnv, currentClass);
            if(expr instanceof Identifier){
                if(((Identifier)expr).getDefinition().isField()){
                    This newThis = new This();
                    newThis.setLocation(getLocation());
                    expr = new Selection(newThis, (Identifier)expr);
                    expr.setLocation(getLocation());
                    expr.verifyInst(compiler, localEnv, currentClass, returnType);
                    this.arguments.getModifiableList().set(i, expr);
                }
            }
            i++;
        }
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        for (AbstractExpr a : getArguments().getList()) {
            // Do the codegen of the Expression
            a.codeGenPrint(compiler);
            // Common part for the print
            if(a.getType().isInt()) {
                compiler.addInstruction(new WINT());
            }
            else if(a.getType().isFloat()) {
                compiler.addInstruction(new WFLOAT());
            }
            else if (a.getType().isBoolean() && !(a instanceof BooleanLiteral)) {
                Label[] labels = compiler.getLabelManager().createBooleanPrintLabel();
                compiler.addInstruction(new CMP(1, GPRegister.getR(1)));
                compiler.addInstruction(new BEQ(labels[0]));
                compiler.addInstruction(new WSTR("false"));
                compiler.addInstruction(new BRA(labels[1]));
                compiler.addLabel(labels[0]);
                compiler.addInstruction(new WSTR("true"));
                compiler.addLabel(labels[1]);
            }
            else if (a.getType().isNull()) {
                compiler.addInstruction(new WSTR("null"));
            }
            else if (a.getType().isClass()) {
                compiler.addInstruction(new CMP(new NullOperand(),GPRegister.getR(1)));
                Label labels[] = compiler.getLabelManager().createNullLabels();
                compiler.addInstruction(new BEQ(labels[0]));
                compiler.addInstruction(new WSTR("(" + a.getType().getName().getName() + " object)"));
                compiler.addInstruction(new BRA(labels[1]));
                compiler.addLabel(labels[0]);
                compiler.addInstruction(new WSTR("null"));
                compiler.addLabel(labels[1]);
            }
        }
    }

    private boolean getPrintHex() {
        return printHex;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("print"+this.getSuffix()+"(");
        this.getArguments().decompile(s);
        s.print(");");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        arguments.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        arguments.prettyPrint(s, prefix, true);
    }

}
