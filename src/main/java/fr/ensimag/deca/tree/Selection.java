package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.SUBSP;

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
    // If we have a method call within an object, the operand is implicitely 'this'
    // We simulate this by creating a new Selection with operand = null
    public Selection(AbstractIdentifier fieldName, ListExpr params) {
        Validate.notNull(fieldName);
        Validate.notNull(params);
        this.operand = null;
        this.fieldName = fieldName;
        this.params = params;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        if (operand != null)
            operand.iter(f);
        fieldName.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        if (operand != null) {
            operand.prettyPrint(s, prefix, false);
        }
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
        TypeDefinition tdef;
        if (operand == null) {
            // If operand is null, then we have a null operand
            // Left type is set to the current class type
            tdef = currentClass.getType().getDefinition();
        } else {
            Type t = operand.verifyExpr(compiler, localEnv, currentClass);
            tdef = compiler.environmentType.defOfType(t.getName());
            if(!(tdef instanceof ClassDefinition)) {
                throw new ContextualError("trying to access a parameter on a non object variable", getLocation());
            }
        }
        ClassDefinition cdef = (ClassDefinition)tdef;
        ExpDefinition edef = cdef.getMembers().get(fieldName.getName());

        if(edef instanceof FieldDefinition || (edef instanceof MethodDefinition && this.params != null)){
            this.fieldName.setType(edef.getType());
            if(cdef.getMembers().get(fieldName.getName()) != null){
                this.setType(this.fieldName.getType());
                this.fieldName.setType(edef.getType());
                this.fieldName.setDefinition(edef);
            }

            if(edef instanceof MethodDefinition && this.params != null){
                for(AbstractExpr aExpr : this.params.getList()){
                    aExpr.verifyExpr(compiler, localEnv, currentClass);
                }
            }
            return getType();
        }
        else {
            throw new ContextualError("trying to access a field but " + fieldName.getName().getName() + " doesn't exists", getLocation());
        }
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler) {
        if(this.operand instanceof Identifier){
            DVal addr = ((Identifier)this.operand).getVariableDefinition().getOperand();
            compiler.addInstruction(new LOAD(addr, GPRegister.R1));
        }
        else{
            this.operand.codeGenPrint(compiler);
        }

        this.fieldName.codeGenPrint(compiler);
    }

    @Override
    protected void codeGenExp(DecacCompiler compiler, int n) {
        for(AbstractExpr aExpr : this.params.getList()){
            aExpr.codeGenExp(compiler, n);
            compiler.incrementStackSize();
            compiler.addInstruction(new PUSH(GPRegister.getR(n)));
        }
        this.operand.codeGenExp(compiler, n);
        compiler.incrementStackSize();
        compiler.addInstruction(new PUSH(GPRegister.getR(n)));;
        compiler.decrementStackSize();
        this.fieldName.codeGenExp(compiler, n);
        for(AbstractExpr aExpr : this.params.getList()){
            compiler.decrementStackSize();
        }
        if(this.params.getList().size() > 0)
            compiler.addInstruction(new SUBSP(this.params.getList().size()));
    }


    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        this.codeGenExp(compiler,2);
    }

}
