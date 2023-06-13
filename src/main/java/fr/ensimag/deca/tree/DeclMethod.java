package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.TypeDefinition;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;

public class DeclMethod extends AbstractDeclMethod{

    final private AbstractIdentifier returnType;
    final private AbstractIdentifier methodName;
    final private MethodBody methodBody;
    
    public DeclMethod(AbstractIdentifier returnType, AbstractIdentifier methodName, MethodBody methodBody){
        this.returnType = returnType;
        this.methodName = methodName;
        this.methodBody = methodBody;
    }
}