package fr.ensimag.deca.tools;

import java.util.HashMap;

import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.ima.pseudocode.Label;

public class MethodTable {
    HashMap<MethodDefinition, Label> methodTable = new HashMap<MethodDefinition, Label>();

    /**
     * Add a method to the methodTable
     * @param methodDefinition the method to be added
     * @param methodLabel the label to be added
     */
    public void addMethod(MethodDefinition methodDefinition, Label methodLabel){
        methodTable.put(methodDefinition, methodLabel);
    }

    public Label getMethodLabel(MethodDefinition methodDefinition){
        return methodTable.get(methodDefinition);
    }

    public Label getLabelByDef(MethodDefinition key) {
        if (methodTable.containsKey(key)) {
            return methodTable.get(key);
        }
        return null;
    }

    public void reindex(){
        int offset = 1;
        for(int i = 1 ; i < this.methodTable.size() +1 ; i++){
            if(this.getMethodByIndex(i) == null){
                MethodDefinition mdef = this.getMethodByIndex(this.methodTable.size() + offset);
                mdef.setIndex(i);
                offset++;
            }
        }
    }
    
    public Label getLabelByIndex(int index){
        for(MethodDefinition mdef : this.getMethodsMap().keySet()){
            if(mdef.getIndex() == index){
                return mdef.getLabel();
            }
        }
        return null;
    }

    public MethodDefinition getMethodByIndex(int index){
        for(MethodDefinition mdef : this.getMethodsMap().keySet()){
            if(mdef.getIndex() == index){
                return mdef;
            }
        }
        return null;
    }

    public HashMap<MethodDefinition, Label> getMethodsMap(){
        return this.methodTable;
    }
}
