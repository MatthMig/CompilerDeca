package fr.ensimag.deca.context;

import java.util.ArrayList;
import java.util.List;

import fr.ensimag.deca.tools.SymbolTable.Symbol;

/**
 * Signature of a method (i.e. list of arguments)
 *
 * @author gl03
 * @date 14/06/2023
 */
public class Signature {
    Symbol methodName;
    List<Type> paramTypes;

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
}
