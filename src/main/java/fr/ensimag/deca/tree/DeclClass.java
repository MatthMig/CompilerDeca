package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;

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
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void verifyClassMembers(DecacCompiler compiler)
            throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }
    
    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
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
        throw new UnsupportedOperationException("Not yet supported");
    }

}
