package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;

public class ListDeclField extends TreeList<AbstractDeclField> {

    @Override
    public void decompile(IndentPrintStream s) {
        for(AbstractDeclField declField : this.getList()){
            declField.decompile(s);
        }
    }
    
    public void codeGen(DecacCompiler compiler) {
        for(AbstractDeclField declField : this.getList()){
            declField.codeGen(compiler);
        }
    }

}