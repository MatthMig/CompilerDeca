package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

public class DeclMethod extends AbstractDeclMethod{

    final private AbstractIdentifier returnType;
    final private AbstractIdentifier methodName;
    final private ListDeclParam listDeclParam;
    final private AbstractMethodBody methodBody;
    
    public DeclMethod(AbstractIdentifier returnType, AbstractIdentifier methodName, ListDeclParam listDeclParam, AbstractMethodBody methodBody){
        Validate.notNull(returnType);
        Validate.notNull(methodName);
        Validate.notNull(listDeclParam);
        Validate.notNull(methodBody);
        this.returnType = returnType;
        this.methodName = methodName;
        this.listDeclParam = listDeclParam;
        this.methodBody = methodBody;
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        returnType.prettyPrint(s, prefix, false);
        methodName.prettyPrint(s, prefix, false);
        listDeclParam.prettyPrint(s, prefix, false);
        methodBody.prettyPrint(s, prefix, true, false);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("not yet implemented");
    }
}