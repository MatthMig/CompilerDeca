package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.EnvironmentType;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.ADDSP;
import fr.ensimag.ima.pseudocode.instructions.RTS;
import fr.ensimag.ima.pseudocode.instructions.TSTO;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;

/**
 * Declaration of a class (<code>class name extends superClass {members}<code>).
 * 
 * @author gl03
 * @date 21/04/2023
 */
public class DeclClass extends AbstractDeclClass {

    final private AbstractIdentifier className;
    final private AbstractIdentifier superClassName;
    final private ListDeclField listDeclField;
    final private ListDeclMethod listDeclMethod;

    public DeclClass(AbstractIdentifier className, AbstractIdentifier superClassName, ListDeclField listDeclField, ListDeclMethod listDeclMethod) {
        Validate.notNull(className);
        // No check for superClassName, null is allowed.
        Validate.notNull(listDeclField);
        Validate.notNull(listDeclMethod);
        this.className = className;
        this.superClassName = superClassName;
        this.listDeclField = listDeclField;
        this.listDeclMethod = listDeclMethod;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("class { ... A FAIRE ... }");
    }

    @Override
    protected void verifyClass(DecacCompiler compiler) throws ContextualError {
        if(compiler.environmentType.defOfType(this.className.getName()) == null){
            if(this.superClassName != null){
                // Load the super class definition
                ClassDefinition superClassDef = compiler.environmentType.defOfClass(this.superClassName.getName());
                if(superClassDef != null){
                    // Declare the class in the environment of the compiler
                    compiler.environmentType.declareClass(className, new ClassType(className.getName(), getLocation(), superClassDef));
                    this.className.setDefinition(compiler.environmentType.defOfType(className.getName()));
                } else {
                    // Parent class doesn't exist
                    throw new ContextualError("Super class " + this.getSuperClassName().getName() + " does not exist", this.getLocation());
                }
            } else {
                // No inheritance
                compiler.environmentType.declareClass(className, new ClassType(className.getName() , getLocation(), null));
                this.className.setDefinition(compiler.environmentType.defOfType(className.getName()));
            }
        }
    }

    @Override
    protected void verifyClassMembers(DecacCompiler compiler)
            throws ContextualError {
        int i = 0;
        EnvironmentExp envExp = compiler.environmentType.defOfClass(this.className.getName()).getMembers();
        for(AbstractDeclField declField : this.listDeclField.getList()){
            declField.verifyDeclField(compiler, envExp, compiler.environmentType.defOfClass(this.className.getName()), ++i);
        }
        i = 1; // The equals method is the first method of the class no matter what.
        // so we start at 2.
        for(AbstractDeclMethod declMethod : this.listDeclMethod.getList()){
            declMethod.verifyDeclMethod(compiler, envExp, compiler.environmentType.defOfClass(this.className.getName()), ++i);
        }

    }
    
    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
        // Passe 3 for the fields 
        for(AbstractDeclField declField : this.listDeclField.getList()){
            EnvironmentExp objectEnv = compiler.environmentType.defOfClass(this.className.getName()).getMembers();
            declField.verifyClassBody(compiler, objectEnv, compiler.environmentType.defOfClass(this.className.getName()));
        }
        // Passe 3 for the methods
        for(AbstractDeclMethod declMethod : this.listDeclMethod.getList()){
            EnvironmentExp objectEnv = compiler.environmentType.defOfClass(this.className.getName()).getMembers();
            declMethod.verifyClassBody(compiler, new EnvironmentExp(objectEnv), compiler.environmentType.defOfClass(this.className.getName()));
        }
    }

    @Override
    public void codeGenClass(DecacCompiler compiler){
        Label initLabel = compiler.getLabelManager().createInitClassLabel(className.getName().getName());
        compiler.addLabel(initLabel);
        compiler.addInstruction(new ADDSP(compiler.environmentType.defOfClass(this.className.getName()).getNumberOfFields()));
        this.listDeclField.codeGen(compiler);
        compiler.addInstruction(new RTS());
        this.listDeclMethod.codeGen(compiler);
    }


    @Override
    public void codeGenMethodTable(DecacCompiler compiler) {
        compiler.addComment("start : method table for class " + this.className.getName());

        // Generate method table, starting on current GB offset 
        this.className.getClassDefinition().setMethodTableAddr(compiler.allocate());
        listDeclMethod.genMethodTable(compiler, this.className.getClassDefinition());

        // Generate the code that actually generates the table
        listDeclMethod.codeGenMethodTable(compiler, this.className.getClassDefinition());

        compiler.addComment("end : method table for class " + this.className.getName());
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        className.prettyPrint(s, prefix, false);
        if (superClassName != null) {
            superClassName.prettyPrint(s, prefix, false);
        }
        listDeclField.prettyPrint(s, prefix, false);
        listDeclMethod.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        className.iter(f);
        if(superClassName != null)
            superClassName.iter(f);
        listDeclField.iter(f);
        listDeclMethod.iter(f);
    }

    public AbstractIdentifier getSuperClassName() {
        return superClassName;
    }

}
