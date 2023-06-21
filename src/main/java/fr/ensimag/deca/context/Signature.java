package fr.ensimag.deca.context;

import java.util.ArrayList;
import java.util.List;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.ListExpr;
import fr.ensimag.deca.tree.Location;

/**
 * Signature of a method (i.e. list of arguments)
 *
 * @author gl03
 * @date 14/06/2023
 */
public class Signature {
    Symbol methodName;
    ArrayList<Type> paramTypes;

    public Symbol getMethodName() {
        return methodName;
    }

    public Type paramNumber(int n) {
        return paramTypes.get(n);
    }

    public Signature(Symbol methodName) {
        this.methodName = methodName;
        this.paramTypes = new ArrayList<Type>();
    }

    public void addParamType(Type t) {
        paramTypes.add(t);
    }

    public int paramListSize() {
        return paramTypes.size();
    }

    public ArrayList<Type> getParamTypes(){
        return paramTypes;
    } 

    public boolean equals(Signature s){
        if(!this.methodName.getName().equals(s.getMethodName().getName()))
            return false;

        if(this.paramListSize() != s.paramListSize())
            return false;

        int i =0;
        for(Type type : paramTypes){
            if(!type.getName().equals(s.paramNumber(i).getName())){
                return false;
            }
            i++;
        }

        return true;
    }

    /**
     * Builds the formatted string of the method signature
     * @return String formatted signature
     */
    public String toString() {
        String sig = this.methodName.getName() + "(";
        // if no params we show void
        if (this.paramListSize() == 0) {
            sig += "void";
        } else {
            for (int i = 0; i < this.paramListSize(); i++) {
                if (i != 0) sig += ", ";
                sig += this.paramTypes.get(i).getName();
            }
        }
        return sig + ")";
    }

    /**
     * Builds the formatted label string of the params type
     * @return String with label format
     */
    public String toLabelString() {
        String sig = this.methodName.getName();
        // if no params we show void
        if (this.paramListSize() == 0) {
            sig += ".void";
        } else {
            for (int i = 0; i < this.paramListSize(); i++) {
                sig += "." + this.paramTypes.get(i).getName();
            }
        }
        return sig;
    }

    public static ExpDefinition checkSignatureInheritance(DecacCompiler compiler, ClassDefinition classDefinition,Symbol methodName ,ListExpr params){        
        EnvironmentExp envExp = classDefinition.getMembers();
        ArrayList<MethodDefinition> methodDefs = envExp.getMethodsWithSignatureLike(methodName, params.getList().size());

        int methodDefinitionIndex = 0;
        MethodDefinition methodDefinition;
        ExpDefinition found = null;

        // For each method with similar signature
        while(methodDefinitionIndex < methodDefs.size() && found == null){
            // We use the current method with a similar signature
            methodDefinition = methodDefs.get(methodDefinitionIndex);
            found = methodDefinition;

            // For each param
            for(int i = 0 ; i < params.size() -1 ; i++){
                // Get the definition behind the params
                Definition paramTypeDefinition = compiler.environmentType.defOfClass(params.getList().get(i).getType().getName());

                // If our parameter is an Object
                if(paramTypeDefinition != null){
                    // Use the heritage tree to try every superclass
                    while(paramTypeDefinition != null){
                        if(methodDefinition.getSignature().getParamTypes().get(i).getName() == paramTypeDefinition.getType().getName()){
                            break;
                        }
                        paramTypeDefinition = ((ClassDefinition)paramTypeDefinition).getSuperClass();
                    }
                    if(paramTypeDefinition == null){
                        found = null;
                        break;
                    }
                }
                // If our object is a builting int or float...
                else{
                    // Call the associated definition
                    paramTypeDefinition = compiler.environmentType.defOfType(params.getList().get(i).getType().getName());
                    // if we passed an int while a float was expected, we do not remove the method
                    if(paramTypeDefinition.getType().getName() != methodDefinition.getSignature().getParamTypes().get(i).getName()
                        && !(paramTypeDefinition.getType().isInt() && methodDefinition.getSignature().getParamTypes().get(i).isFloat())){
                        found = null;
                    }
                }
            }
            methodDefinitionIndex++;
        }
        return found;
    }
}
