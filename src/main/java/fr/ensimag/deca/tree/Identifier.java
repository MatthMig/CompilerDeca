package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.ParamDefinition;
import fr.ensimag.deca.context.VariableDefinition;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.BSR;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.LabelOperand;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;

/**
 * Deca Identifier
 *
 * @author gl03
 * @date 21/04/2023
 */
public class Identifier extends AbstractIdentifier {

    @Override
    protected void checkDecoration() {
        if (getDefinition() == null) {
            throw new DecacInternalError("Identifier " + this.getName() + " has no attached Definition");
        }
    }

    @Override
    public Definition getDefinition() {
        return definition;
    }

    @Override
    protected DVal dval(DecacCompiler compiler) {
        if(this.getDefinition().isField())
            return this.getFieldDefinition().getOperand();

        if(this.getDefinition().isMethod())
            return this.getMethodDefinition().getOperand();

        if(this.getDefinition().isParam())
            return this.getParamDefinition().getOperand();

        return this.getVariableDefinition().getOperand();
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * ClassDefinition.
     *
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     *
     * @throws DecacInternalError
     *             if the definition is not a class definition.
     */
    @Override
    public ClassDefinition getClassDefinition() {
        try {
            return (ClassDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a class identifier, you can't call getClassDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * MethodDefinition.
     *
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     *
     * @throws DecacInternalError
     *             if the definition is not a method definition.
     */
    @Override
    public MethodDefinition getMethodDefinition() {
        try {
            return (MethodDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a method identifier, you can't call getMethodDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * FieldDefinition.
     *
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     *
     * @throws DecacInternalError
     *             if the definition is not a field definition.
     */
    @Override
    public FieldDefinition getFieldDefinition() {
        try {
            return (FieldDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a field identifier, you can't call getFieldDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * ParamDefinition.
     *
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     *
     * @throws DecacInternalError
     *             if the definition is not a field definition.
     */
    @Override
    public ParamDefinition getParamDefinition() {
        try {
            return (ParamDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a param identifier, you can't call getParamDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * VariableDefinition.
     *
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     *
     * @throws DecacInternalError
     *             if the definition is not a field definition.
     */
    @Override
    public VariableDefinition getVariableDefinition() {
        try {
            return (VariableDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a variable identifier, you can't call getVariableDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a ExpDefinition.
     *
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     *
     * @throws DecacInternalError
     *             if the definition is not a field definition.
     */
    @Override
    public ExpDefinition getExpDefinition() {
        try {
            return (ExpDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a Exp identifier, you can't call getExpDefinition on it");
        }
    }

    @Override
    public void setDefinition(Definition definition) {
        this.definition = definition;
    }

    @Override
    public Symbol getName() {
        return name;
    }

    private Symbol name;

    public Identifier(Symbol name) {
        Validate.notNull(name);
        this.name = name;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        // Seek for the definition of the identifier in the environments (from lowest to highest environment)
        while (localEnv != null) {
            if(localEnv.get(this.name) != null){
                this.setType(localEnv.get(this.name).getType());
                this.setDefinition(localEnv.get(this.name));
                return this.getType();
            }
            localEnv = localEnv.getParentEnvironment();
        }
        throw new ContextualError("undefined variable " + this.getName(),this.getLocation());
    }

    /**
     * Implements non-terminal "type" of [SyntaxeContextuelle] in the 3 passes
     * @param compiler contains "env_types" attribute
     */
    @Override
    public Type verifyType(DecacCompiler compiler) throws ContextualError {
        if(compiler.environmentType.defOfType(this.name) != null){
            return compiler.environmentType.defOfType(this.name).getType();
        }
        throw new ContextualError("class " + this.getName() + " doesn't exist", getLocation());
    }


    private Definition definition;


    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler) {
        if( this.getDefinition().isField()){
            compiler.addInstruction(new LOAD(new RegisterOffset(this.getFieldDefinition().getIndex() ,GPRegister.R1), GPRegister.R1));
        }
        else if (this.getDefinition().isMethod()){
            compiler.addInstruction(new BSR(new LabelOperand(this.getMethodDefinition().getLabel())));
            compiler.addInstruction(new LOAD(GPRegister.getR(0), GPRegister.getR(1)));
        }
        else{
            compiler.addInstruction(new LOAD(this.getVariableDefinition().getOperand(),Register.R1));
        }
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        codeGenExp(compiler, 2);
    }

    @Override
    protected void codeGenExp(DecacCompiler compiler, int n) {
        if(this.getDefinition().isField()) {
            compiler.addInstruction(new LOAD(new RegisterOffset(this.getFieldDefinition().getIndex(), Register.getR(n)),Register.getR(n)));
        }
        else if (this.getDefinition().isMethod()) {
            compiler.addInstruction(new BSR(new LabelOperand(this.getMethodDefinition().getLabel())));
        }
        else if (this.getDefinition().isParam()) {
            compiler.addInstruction(new LOAD(this.getParamDefinition().getOperand(),Register.getR(n)));
        }
        else if (this.getDefinition().isClass()) {
            DAddr classAddr = ((ClassDefinition)this.getDefinition()).getMethodTableAddr();
            compiler.addInstruction(new LEA(classAddr, Register.getR(n)));
        }
        else
            compiler.addInstruction(new LOAD(this.getVariableDefinition().getOperand(),Register.getR(n)));
    }

    @Override
    public void codeGenCondition(DecacCompiler compiler, Boolean neg, Label labelElse) {
        compiler.addInstruction(new LOAD(this.getVariableDefinition().getOperand(),Register.R1));
        compiler.addInstruction(new CMP(new ImmediateInteger(1),Register.R1));
        if(!neg){
            compiler.addInstruction(new BNE(labelElse));
        }
        else{
            compiler.addInstruction(new BEQ(labelElse));
        }
    }

    @Override
    public void verifyCondition(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        Type t = verifyExpr(compiler, localEnv, currentClass);
        if(!t.isBoolean()){
            throw new ContextualError("error when trying to use var " + this.getName() + " of type " + this.getType() + " as a condition.", getLocation());
        }
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(name.toString());
    }

    @Override
    String prettyPrintNode() {
        return "Identifier (" + getName() + ")";
    }

    @Override
    protected void prettyPrintType(PrintStream s, String prefix) {
        Definition d = getDefinition();
        if (d != null) {
            s.print(prefix);
            s.print("definition: ");
            s.print(d);
            s.println();
        }
    }



}
