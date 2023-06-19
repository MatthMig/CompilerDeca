package fr.ensimag.deca.tools;

import java.util.HashMap;
import java.util.Set;

import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.ima.pseudocode.Label;

public class MethodTable {
    HashMap<MethodDefinition, Label> methodTable = new HashMap<MethodDefinition, Label>();

    public void addMethod(MethodDefinition methodDefinition, Label methodLabel){
        methodTable.put(methodDefinition, methodLabel);
    }

    public Label getMethodLabel(MethodDefinition methodDefinition){
        return methodTable.get(methodDefinition);
    }

    public void addAll(MethodTable methodTable){
        this.methodTable.putAll(methodTable.getMethodsMap());
    }

    public Label getLabelByIndex(int index){
        Set<MethodDefinition> mdefset = this.getMethodsMap().keySet();
        for(MethodDefinition mdef : mdefset){
            String[] splittedLabel = mdef.getLabel().toString().split("[.]");
            if(splittedLabel[splittedLabel.length-1].contains(Integer.toString(index)))
                return mdef.getLabel();
        }
        return null;
    }

    public HashMap<MethodDefinition, Label> getMethodsMap(){
        return this.methodTable;
    }
}
