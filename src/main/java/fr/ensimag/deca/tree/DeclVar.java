package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.context.VariableDefinition;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.instructions.NEW;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;

/**
 * @author gl03
 * @date 21/04/2023
 */
public class DeclVar extends AbstractDeclVar {


    final private AbstractIdentifier type;
    final private AbstractIdentifier varName;
    final private AbstractInitialization initialization;

    public DeclVar(AbstractIdentifier type, AbstractIdentifier varName, AbstractInitialization initialization) {
        Validate.notNull(type);
        Validate.notNull(varName);
        Validate.notNull(initialization);
        this.type = type;
        this.varName = varName;
        this.initialization = initialization;
    }

    @Override
    protected void verifyDeclVar(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        Type t = this.type.verifyType(compiler);
        this.initialization.verifyInitialization(compiler, t, localEnv, currentClass);

        VariableDefinition vardef = new VariableDefinition(t, getLocation());
        TypeDefinition tdef = compiler.environmentType.defOfType(compiler.environmentType.INT.getName());
        varName.setDefinition(vardef);
        type.setDefinition(tdef);
        try {
            localEnv.declare(varName.getName(), vardef);
        } catch(DoubleDefException e) {
            String message = String.format("Variable %s already declared", varName.getName().getName());
            throw new ContextualError(message, getLocation());
        }
    }

    @Override
    public void codeGen(DecacCompiler compiler) {
        // Compiler now knows he has this variable
        DAddr addr = compiler.allocate();
        ((ExpDefinition)this.varName.getDefinition()).setOperand(addr);
        compiler.addVar(this.varName.getName(), (ExpDefinition)this.varName.getDefinition());

        // if (initialization.getClass() != NoInitialization ){
        //     // STORE VALUE
        // }
    }

    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        type.iter(f);
        varName.iter(f);
        initialization.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        varName.prettyPrint(s, prefix, false);
        initialization.prettyPrint(s, prefix, true);
    }
}
