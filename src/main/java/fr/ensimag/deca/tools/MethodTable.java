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

    public HashMap<MethodDefinition, Label> getMethodsMap(){
        return this.methodTable;
    }
}
