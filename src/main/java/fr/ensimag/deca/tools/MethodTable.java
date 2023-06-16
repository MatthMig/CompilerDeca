package fr.ensimag.deca.tools;

import java.util.HashMap;

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
}
