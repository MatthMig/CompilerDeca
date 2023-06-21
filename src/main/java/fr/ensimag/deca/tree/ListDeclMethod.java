package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;

public class ListDeclMethod extends TreeList<AbstractDeclMethod> {

    @Override
    public void decompile(IndentPrintStream s) {
        for(AbstractDeclMethod declMethod : this.getList()){
            declMethod.decompile(s);
        }
    }

    public void genMethodTable(DecacCompiler compiler, ClassDefinition classDefinition) {
        for(AbstractDeclMethod dMethod : this.getList()){
            dMethod.genMethodTableEntry(compiler, classDefinition);
        }
    }

    public void codeGenMethodTable(DecacCompiler compiler, ClassDefinition classDefinition) {
        for(AbstractDeclMethod dMethod : this.getList()){
            dMethod.codeGenMethodTableEntry(compiler);
        }
    }

    public void codeGen(DecacCompiler compiler) {        
        for(AbstractDeclMethod dMethod : this.getList()){
            dMethod.codeGen(compiler);
        }
    }
}