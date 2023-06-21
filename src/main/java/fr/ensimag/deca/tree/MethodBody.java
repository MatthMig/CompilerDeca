package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

import org.apache.commons.lang.Validate;
import java.io.PrintStream;

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

    @Override
    public int getVarCount() {
        return this.listDeclVar.size();
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

            if(returnType != compiler.environmentType.VOID){
                boolean noReturn = this.verifyReturn(compiler, localEnv, currentClass, returnType);
                if(!noReturn){
                    throw new ContextualError("method never returns" , getLocation());
                }
            }
    }

    protected boolean verifyReturn(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass, Type returnType){
        boolean methodWillEnd = false;
        for(AbstractInst inst : listInst.getList()){
            if(inst instanceof Return){
                methodWillEnd = true;
            }
            if(inst instanceof IfThenElse){
                methodWillEnd = methodWillEnd || ((IfThenElse)inst).verifyReturn(compiler, localEnv, currentClass, returnType);
            }
        }

        return methodWillEnd;
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        this.listDeclVar.iterChildren(f);
        this.listInst.iterChildren(f);
        this.returnType.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        listDeclVar.prettyPrint(s, prefix, false);
        listInst.prettyPrint(s, prefix, true);
    }

    @Override
    public void codeGen(DecacCompiler compiler) {
        this.listDeclVar.codeGenDeclVar(compiler);
        this.listInst.codeGenListInst(compiler);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        System.out.println("{");
        s.println();
        s.indent();
        listDeclVar.decompile(s);
        listInst.decompile(s);
        s.println();
        s.unindent();
        System.out.println("}");
    }

}
