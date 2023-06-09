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
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
import fr.ensimag.ima.pseudocode.instructions.NEW;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.GPRegister;

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
        TypeDefinition tdef = compiler.environmentType.defOfType(type.getName());
        varName.setDefinition(vardef);
        type.setDefinition(tdef);
        try {
            localEnv.declare(varName.getName(), vardef);
        } catch(DoubleDefException e) {
            String message = String.format("Variable %s already declared", varName.getName().getName());
            throw new ContextualError(message, getLocation());
        }

        if(this.initialization.getClass() != NoInitialization.class ){
            // If i implicitly initialize a float with an int value.
            if(((Initialization)this.initialization).getExpr().getType() == compiler.environmentType.INT &&
                this.varName.getDefinition().getType() == compiler.environmentType.FLOAT ){
                ((Initialization)this.initialization).setExpression(new ConvFloat(((Initialization)this.initialization).getExpr()));
                ((Initialization)this.initialization).getExpression().verifyExpr(compiler, localEnv, currentClass);
            }

            else if(((Initialization)this.initialization).getExpr().getType() == compiler.environmentType.FLOAT &&
                this.varName.getDefinition().getType() == compiler.environmentType.INT ){
                    throw new ContextualError("impossible conversion from float to int", getLocation());
            }

            // If i assign anything that isn't same type and isn't convFloat
            else if(((Initialization)this.initialization).getExpr().getClass() == Identifier.class){
                if(((Identifier)((Initialization)this.initialization).getExpr()).getType() != varName.getDefinition().getType()){
                    throw new ContextualError("trying to asign a var of type " + ((Identifier)((Initialization)this.initialization).getExpr()).getType() + " to a variable of type "+ varName.getDefinition().getType(), getLocation());
                }
            }

        }
        // Compiler now knows he has this variable
        DAddr addr = compiler.allocate();
        ((ExpDefinition)this.varName.getDefinition()).setOperand(addr);
        compiler.addVar(this.varName.getName(), (ExpDefinition)this.varName.getDefinition());
    }

    @Override
    public void codeGen(DecacCompiler compiler) {

        if (initialization.getClass() != NoInitialization.class ){
            this.initialization.codeGen(compiler);
            compiler.addInstruction(new STORE(GPRegister.getR(2), this.varName.getExpDefinition().getOperand()));
        }
    }

    @Override
    public void decompile(IndentPrintStream s) {
        type.decompile(s);
        s.print(" ");
        varName.decompile(s);
        s.print("=");
        initialization.decompile(s);
        s.print(";");
        s.println();
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
