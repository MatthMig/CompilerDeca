package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ParamDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;

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
        this.paramType.iter(f);
        this.paramName.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        paramType.prettyPrint(s, prefix, false);
        paramName.prettyPrint(s, prefix, true);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(this.getType().getName().getName());
        s.print(" ");
        s.print(this.paramName.getName().getName());
    }

    public AbstractIdentifier getType(){
        return this.paramType;
    }

    @Override
    protected void verifyDeclParam(DecacCompiler compiler,
        EnvironmentExp localEnv, ClassDefinition currentClass)
        throws ContextualError {
            Type t = this.paramType.verifyType(compiler);
            if (t.isVoid()) {
                throw new ContextualError("Parameter cannot be of type void", getLocation());
            }
            this.paramType.setType(t);
            this.paramName.setType(t);
            TypeDefinition typeDef = compiler.environmentType.defOfType(paramType.getName());
            paramType.setDefinition(typeDef);
    }

    @Override
    void verifyClassBody(DecacCompiler compiler,
    EnvironmentExp localEnv, ClassDefinition currentClass)
    throws ContextualError {
        Type t = this.paramType.getType();
        ParamDefinition paramDef = new ParamDefinition(t, getLocation());
        this.paramName.setDefinition(paramDef);
        try {
            localEnv.declare(paramName.getName(), this.paramName.getExpDefinition());
        } catch(DoubleDefException e) {
            String message = String.format("Parameter %s already declared", paramName.getName().getName());
            throw new ContextualError(message, getLocation());
        }
    }

    @Override
    public void setParamAddress(int index) {
        this.paramName.getParamDefinition().setOperand(new RegisterOffset(index, Register.LB));
    }

}
