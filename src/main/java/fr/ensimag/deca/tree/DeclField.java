package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;



public class DeclField extends AbstractDeclField{

    public Visibility visib;
    final private AbstractIdentifier fieldType;
    final private AbstractIdentifier fieldName;
    final private AbstractInitialization initialization;

    public DeclField(Visibility visib, AbstractIdentifier fieldType, AbstractIdentifier fieldName, AbstractInitialization initialization){
        Validate.notNull(fieldType);
        Validate.notNull(fieldName);
        Validate.notNull(initialization);
        this.fieldType = fieldType;
        this.fieldName = fieldName;
        this.initialization = initialization;
        this.visib = visib;
    }


    @Override
    protected void verifyDeclField(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass, int index)
            throws ContextualError {

        // Is this field declared as a void ?
        if(!(this.fieldType.getType() != compiler.environmentType.VOID)){
            throw new ContextualError("cannot declare a void field", getLocation());
        }
        
        // Verify type definition
        Type t = this.fieldType.verifyType(compiler);
        TypeDefinition tdef = compiler.environmentType.defOfType(this.fieldType.getName());
        this.fieldType.setType(t);
        this.fieldType.setDefinition(tdef);
        
        // Type is ok, then tag this field as of this type and create a definition for this field
        this.fieldName.setType(t);
        this.fieldName.setDefinition(new FieldDefinition(t, fieldName.getLocation(), visib, currentClass, index));

        // Try to declare the field to the local environment
        try{
            localEnv.declare(fieldName.getName(), fieldName.getExpDefinition());
        }
        catch(DoubleDefException ddE){
            String message = String.format("Field %s already declared in class", fieldName.getName().getName(), currentClass.getClass().getName());
            throw new ContextualError(message, getLocation());
        }

        currentClass.incNumberOfFields();
    }

    @Override
    public void verifyClassBody(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        // Passe 3 : verify the initialization expression
        this.initialization.verifyInitialization(compiler, this.fieldName.getType(), localEnv, currentClass);
    }

    @Override
    public void codeGen(DecacCompiler compiler) {
        compiler.addComment("Initialisation de " + this.fieldName.getName().getName());
        if(this.initialization instanceof NoInitialization){
            if(this.fieldType.getType().isInt()){
                compiler.addInstruction(new LOAD(0,GPRegister.getR(2)));
            }
            else if(this.fieldType.getType().isFloat()){
                compiler.addInstruction(new LOAD((float)0.0,GPRegister.getR(2)));
            }
            else if(this.fieldType.getType().isBoolean()){
                compiler.addInstruction(new LOAD(0,GPRegister.getR(2)));
            }
        }
        else{
            this.initialization.codeGen(compiler);
        }
        compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), GPRegister.R1));
        compiler.addInstruction(new STORE(GPRegister.getR(2) ,new RegisterOffset(this.fieldName.getFieldDefinition().getIndex(), GPRegister.R1)));
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        fieldType.iter(f);
        fieldName.iter(f);
        initialization.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        fieldType.prettyPrint(s, prefix, false);
        fieldName.prettyPrint(s, prefix, false);
        initialization.prettyPrint(s, prefix, true);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        if (visib.equals(Visibility.PROTECTED)) {
            s.print("protected ");
        }
        fieldType.decompile(s);
        s.print(" ");
        fieldName.decompile(s);
        initialization.decompile(s);
        s.print(";");
    }
}