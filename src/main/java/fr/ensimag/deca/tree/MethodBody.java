package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

import org.apache.commons.lang.Validate;
import java.io.PrintStream;
import fr.ensimag.deca.tools.IndentPrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

public class MethodBody extends AbstractMethodBody{
    final private ListDeclVar listDeclVar;
    final private ListInst listInst;
    final private AbstractIdentifier returnType;

    public MethodBody(ListDeclVar listDeclVar, ListInst listInst, AbstractIdentifier returnType) {
        Validate.notNull(listDeclVar);
        Validate.notNull(listInst);
        Validate.notNull(returnType);
        this.listDeclVar = listDeclVar;
        this.listInst = listInst;
        this.returnType = returnType;
    }
    /**
     * @param compiler
     * @param localEnv
     * @param currentClass
     */
    protected void verifyMethodBody(DecacCompiler compiler,
        EnvironmentExp localEnv, ClassDefinition currentClass, 
        Type returnType)
        throws ContextualError {
            this.listDeclVar.verifyListDeclVariable(compiler, localEnv, currentClass);
            this.listInst.verifyListInst(compiler, localEnv, currentClass, returnType);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        listDeclVar.prettyPrint(s, prefix, false);
        listInst.prettyPrint(s, prefix, true);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    public void codeGen(DecacCompiler compiler){
        throw new UnsupportedOperationException("not yet implemented");
    }
}