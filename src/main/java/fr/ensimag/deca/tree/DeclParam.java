package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.context.VariableDefinition;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.ParamDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.instructions.NEW;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.GPRegister;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;

public class DeclParam extends AbstractDeclParam{

    final private AbstractIdentifier paramType;
    final private AbstractIdentifier paramName;
    
    public DeclParam(AbstractIdentifier paramType, AbstractIdentifier paramName){
        Validate.notNull(paramType);
        Validate.notNull(paramName);
        this.paramType = paramType;
        this.paramName = paramName;
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        paramType.prettyPrint(s, prefix, false);
        paramName.prettyPrint(s, prefix, true);
    }
    
    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    public AbstractIdentifier getType(){
        return this.paramType;
    }

    @Override
    protected void verifyDeclParam(DecacCompiler compiler,
        EnvironmentExp localEnv, ClassDefinition currentClass)
        throws ContextualError {
            Type t = this.paramType.verifyType(compiler);
            this.paramName.setType(t);
    
            ParamDefinition paramDef = new ParamDefinition(t, getLocation());
            TypeDefinition typeDef = compiler.environmentType.defOfType(paramType.getName());
            paramName.setDefinition(paramDef);
            paramType.setDefinition(typeDef);
    
            try {
                localEnv.declare(paramName.getName(), paramDef);
            } catch(DoubleDefException e) {
                String message = String.format("Parameter %s already declared", paramName.getName().getName());
                throw new ContextualError(message, getLocation());
            }
    }
}