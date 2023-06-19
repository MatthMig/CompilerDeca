package fr.ensimag.deca.tree;

import fr.ensimag.deca.tools.IndentPrintStream;

public class ListDeclParam extends TreeList<AbstractDeclParam> {

    @Override
    public void decompile(IndentPrintStream s) {
        for(AbstractDeclParam declParam : this.getList()){
            declParam.decompile(s);
        }
    }

    public void setParamAddresses(){
        int index = -3;

        // Reading reverse sense the list to push parameters correctly
        for(int i = this.getList().size() - 1 ; i >= 0 ; i--){
            this.getList().get(i).setParamAddress(index);
            index--;
        }
    }
}
