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
        operand.iter(f);
        fieldName.iter(f);
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
        operand.verifyExpr(compiler, localEnv, currentClass);
        Type t = operand.verifyExpr(compiler, localEnv, currentClass);
        TypeDefinition tdef = compiler.environmentType.defOfType(t.getName()); //compiler.environmentType.defOfType(((Identifier)operand).getVariableDefinition().getType().getName());
        if(!(tdef instanceof ClassDefinition)){
            throw new ContextualError("trying to access a parameter on a non object variable", getLocation());
        }
        ClassDefinition cdef = (ClassDefinition)tdef;
        ExpDefinition edef = cdef.getMembers().get(fieldName.getName());

        if(!(edef instanceof FieldDefinition)){
            if(edef instanceof MethodDefinition)
                throw new ContextualError("trying to access a field but " + fieldName.getName().getName() + " is a method", getLocation());
            else
                throw new ContextualError("trying to access a field but " + fieldName.getName().getName() + " doesn't exists", getLocation());
        }

        FieldDefinition fdef = (FieldDefinition)edef;
        this.fieldName.setType(fdef.getType());
        if(cdef.getMembers().get(fieldName.getName()) != null){
            this.setType(this.fieldName.getType());
            this.fieldName.setType(fdef.getType());
            this.fieldName.setDefinition(fdef);
            return this.getType();
        }
        return getType();
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
        if(this.operand instanceof Identifier){
            DVal addr = ((Identifier)this.operand).getVariableDefinition().getOperand();
            compiler.addInstruction(new LOAD(addr, GPRegister.getR(n)));
        }
        else{
            this.operand.codeGenExp(compiler, n);
        }
        
        this.fieldName.codeGenExp(compiler, n);
    }


    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        throw new UnsupportedOperationException("not yet implemented");
    }

}