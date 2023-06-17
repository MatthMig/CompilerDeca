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
import fr.ensimag.ima.pseudocode.IMAProgram;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Line;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.ADDSP;
import fr.ensimag.ima.pseudocode.instructions.SUBSP;
import fr.ensimag.ima.pseudocode.instructions.RTS;
import fr.ensimag.ima.pseudocode.instructions.TSTO;
import fr.ensimag.ima.pseudocode.instructions.BOV;

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
                this.superClassName.setDefinition(superClassDef);
                if(superClassDef != null){
                    // Declare the class in the environment of the compiler
                    compiler.environmentType.declareClass(className, new ClassType(className.getName(), getLocation(), superClassDef));
                    this.className.setDefinition(compiler.environmentType.defOfType(className.getName()));
                    compiler.environmentType.defOfClass(className.getName()).setNumberOfFields(superClassDef.getNumberOfFields());
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
        ClassDefinition classDefinition = compiler.environmentType.defOfClass(this.className.getName());
        int i = 0;
        if(this.superClassName != null){
            ClassDefinition superClassDef = compiler.environmentType.defOfClass(this.superClassName.getName());
            classDefinition.setNumberOfFields(superClassDef.getNumberOfFields());
            i = classDefinition.getNumberOfFields();
        }
        EnvironmentExp envExp = compiler.environmentType.defOfClass(this.className.getName()).getMembers();
        for(AbstractDeclField declField : this.listDeclField.getList()){
            declField.verifyDeclField(compiler, envExp, classDefinition, ++i);
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
        // Storing Class field count
        int fieldCount = this.className.getClassDefinition().getNumberOfFields();

        // Generating the initialization code under a sub compiler
        DecacCompiler initializationCompiler = new DecacCompiler(compiler.getCompilerOptions(), compiler.getSource());
        this.listDeclField.codeGen(initializationCompiler);

        // Start the init subprogram with an adapted label
        Label initLabel = compiler.getLabelManager().createInitClassLabel(className.getName().getName());
        compiler.addLabel(initLabel);

        // Now that the subcompiler generated the code, I can get informations I need to check for the stack
        compiler.addInstruction(new TSTO(fieldCount + initializationCompiler.getMaxStackSize()));
        compiler.addInstruction(new BOV(compiler.getLabelManager().getStackOverflowLabel()),"check for stack overflows");
        compiler.addInstruction(new ADDSP(fieldCount));

        // Now append the subCompiler generated program
        compiler.append(initializationCompiler.getProgram());

        // Return
        compiler.addInstruction(new RTS());

        // Reset Stack Pointer
        compiler.addInstruction(new SUBSP(fieldCount));

        // Generate code for methods
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
