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
    Type returnType;
    Symbol methodName;
    List<Type> paramTypes;
    
    public Type getReturnType() {
        return returnType;
    }

    public Symbol getMethodName() {
        return methodName;
    }
    
    public Type paramNumber(int n) {
        return paramTypes.get(n);
    }

    public Signature(Type returnType, Symbol methodName) {
        this.returnType = returnType;
        this.methodName = methodName;
        this.paramTypes = new ArrayList<Type>();
    }

    public void addParamType(Type t) {
        paramTypes.add(t);
    }
    
    public int paramListSize() {
        return paramTypes.size();
    }

}
