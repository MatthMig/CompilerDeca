package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;

public class ListDeclMethod extends TreeList<DeclMethod> {

    @Override
    public void decompile(IndentPrintStream s) {
        for(AbstractDeclMethod declMethod : this.getList()){
            declVar.decompile(s);
        }
    }

}