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
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.SUBSP;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

/**
 *
 * @author gl03
 * @date 13/06/2023
 */
public class Selection extends AbstractLValue {
    private AbstractExpr operand;
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
        operand.decompile(s);
        s.print(".");
        fieldName.decompile(s);
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
        TypeDefinition typeDef;
        if (operand == null) {
            // If operand is null, then we have an implicit This
            This newThis = new This();
            newThis.setLocation(getLocation());
            this.operand = newThis;
            this.operand.verifyExpr(compiler, localEnv, currentClass);
            typeDef = currentClass.getType().getDefinition();
        }
        else {
            // If operand is a field, then we create a new Selection with This as the left operand
            // In the Main, this does not apply
            if (operand instanceof Identifier
                && currentClass != null
                && currentClass.getMembers().get(((Identifier)operand).getName()) != null
                && currentClass.getMembers().get(((Identifier)operand).getName()).isField()
            ) {
                // Creation of a new This
                This newThis = new This();
                newThis.setLocation(getLocation());
                // Creation of a new Selection to the left of the current
                this.operand = new Selection(newThis, (Identifier)operand);
            }
            Type t = operand.verifyExpr(compiler, localEnv, currentClass);
            typeDef = compiler.environmentType.defOfType(t.getName());
            if(!(typeDef instanceof ClassDefinition)) {
                throw new ContextualError("trying to access a parameter on a non object variable", getLocation());
            }
        }
        ClassDefinition classDef = (ClassDefinition)typeDef;
        ExpDefinition expDef = null;
        Signature sig = new Signature(fieldName.getName());

        // Check for definition in the current class
        if (params == null) {
            // If this is a field
            expDef = classDef.getMembers().get(fieldName.getName());
        } else {
            // Else if it is a method
            for (AbstractExpr param : this.params.getList()) {
                sig.addParamType(param.verifyExpr(compiler, localEnv, currentClass));
            }
            expDef = Signature.checkSignatureInheritance(compiler, classDef, this.fieldName.getName(), params);
        }

        // Check for definition in super class if none was found
        if(expDef == null && classDef.getSuperClass() != null){
            ClassDefinition superClassDefinition = classDef.getSuperClass();
            while(expDef == null && superClassDefinition != null ){
                // if this is a field
                if (params == null) {
                    expDef = superClassDefinition.getMembers().get(fieldName.getName());
                }
                // Else if it is a method
                else {
                    expDef = Signature.checkSignatureInheritance(compiler, superClassDefinition, this.fieldName.getName(), params);
                }
                superClassDefinition = superClassDefinition.getSuperClass();
            }
        }

        // if neither nothing nor 'this' as left operand
        // then we check if it is protected and if so, we deny access to the field/method
        if (expDef != null && !(operand instanceof This)) {
            // it is a field
            if (params == null) {
                if (((FieldDefinition) expDef).getVisibility() == Visibility.PROTECTED) {
                    // We act like if we didn't find anything
                    expDef = null;
                }
            // it is a method
            } else {
                if (((MethodDefinition) expDef).getVisibility() == Visibility.PROTECTED) {
                    // We act like if we didn't find anything
                    expDef = null;
                }

            }
        }
        // If we finally found a definition
        if(expDef != null && ( (expDef instanceof FieldDefinition && this.params == null) || (expDef instanceof MethodDefinition && this.params != null))){
            this.fieldName.setType(expDef.getType());

            this.setType(this.fieldName.getType());
            this.fieldName.setType(expDef.getType());
            this.fieldName.setDefinition(expDef);

            if(expDef instanceof MethodDefinition && this.params != null){
                for(AbstractExpr aExpr : this.params.getList()){
                    aExpr.verifyExpr(compiler, localEnv, currentClass);
                }
            }
            return getType();
        }
        else {
            String message;
            // If we tried to call an object method
            if (this.params != null) {
                message = String.format("No visible method %s.%s exists in this context", this.operand.getType().getName().getName(), sig.toString());
            } else {
                // else we wanted to access a field
                message = String.format("No visible field %s.%s exists in this context", this.operand.getType().getName().getName(), fieldName.getName().getName());
            }
            throw new ContextualError(message, getLocation());
        }
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler) {
        this.codeGenExp(compiler, 2);
        // this.operand.codeGenExp(compiler, 2);
        compiler.addInstruction(new LOAD(GPRegister.getR(2), GPRegister.getR(1)));
        // this.fieldName.codeGenPrint(compiler);
    }

    @Override
    protected void codeGenExp(DecacCompiler compiler, int n) {
        // case of a method call
        if(this.params != null){
            int i = 0;
            for(AbstractExpr aExpr : this.params.getList()){
                aExpr.codeGenExp(compiler, n);
                // if we passed an int while a float was expected, we convert it
                if (aExpr.getType().isInt() && this.fieldName.getMethodDefinition().getSignature().getParamTypes().get(i).isFloat()) {
                    compiler.addInstruction(new FLOAT(GPRegister.getR(n), GPRegister.getR(n)));
                }
                compiler.incrementStackSize();
                compiler.addInstruction(new PUSH(GPRegister.getR(n)));
                i++;
            }
            // Generate left selection
            this.operand.codeGenExp(compiler, n);
            // Check for null pointer
            if(!compiler.getNoCheck()) {
                compiler.addInstruction(new CMP(new NullOperand(), GPRegister.getR(n)), "check for null pointer");
                compiler.addInstruction(new BEQ(compiler.getLabelManager().createNullPointerLabel()));
            }

            // Stack "this"
            compiler.incrementStackSize();
            compiler.addInstruction(new PUSH(GPRegister.getR(n)));
            this.fieldName.codeGenExp(compiler, n);
            compiler.addInstruction(new SUBSP(1));
            compiler.decrementStackSize();

            // Load result from sub method
            compiler.addInstruction(new LOAD(GPRegister.R0, GPRegister.getR(n)));

            // foreach param we decrement the stack size
            this.params.getList().forEach(param -> compiler.decrementStackSize());
            if(this.params.getList().size() > 0) {
                compiler.addInstruction(new SUBSP(this.params.getList().size()));
            }
        }
        // case of a field access
        else{
            this.operand.codeGenExp(compiler, n);
            // Check for null pointer
            if(!compiler.getNoCheck()) {
                compiler.addInstruction(new CMP(new NullOperand(), GPRegister.getR(n)), "check for null pointer");
                compiler.addInstruction(new BEQ(compiler.getLabelManager().createNullPointerLabel()));
            }
            this.fieldName.codeGenExp(compiler, n);
        }
    }

    @Override
    public void codeGenCondition(DecacCompiler compiler, Boolean neg, Label label) {
        this.codeGenExp(compiler, 2);
        compiler.addInstruction(new CMP(1, Register.getR(2)));
        compiler.addInstruction(new BNE(label));
    }

    public AbstractIdentifier getFieldName() {
        return fieldName;
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        if(this.params != null){
            this.codeGenExp(compiler, 2);
        }
        else{
            this.operand.codeGenExp(compiler, 2);
        }
    }
}
