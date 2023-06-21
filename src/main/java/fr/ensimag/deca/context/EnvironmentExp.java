package fr.ensimag.deca.context;

import java.util.ArrayList;
import java.util.HashMap;

import fr.ensimag.deca.tools.SymbolTable.Symbol;

/**
 * Dictionary associating identifier's ExpDefinition to their names.
 *
 * This is actually a linked list of dictionaries: each EnvironmentExp has a
 * pointer to a parentEnvironment, corresponding to superblock (eg superclass).
 *
 * The dictionary at the head of this list thus corresponds to the "current"
 * block (eg class).
 *
 * Searching a definition (through method get) is done in the "current"
 * dictionary and in the parentEnvironment if it fails.
 *
 * Insertion (through method declare) is always done in the "current" dictionary.
 *
 * @author gl03
 * @date 21/04/2023
 */
public class EnvironmentExp {
    private HashMap<Symbol, Definition> definitions = new HashMap<>();

    private EnvironmentExp parentEnvironment;

    public EnvironmentExp getParentEnvironment() {
        return parentEnvironment;
    }

    public EnvironmentExp(EnvironmentExp parentEnvironment) {
        this.parentEnvironment = parentEnvironment;
    }

    public static class DoubleDefException extends Exception {
        private static final long serialVersionUID = -2733379901827316441L;
    }

    /**
     * Return the definition of the symbol in the environment, or null if the
     * symbol is undefined.
     */
    public ExpDefinition get(Symbol key) {
        return (ExpDefinition)this.definitions.get(key);
    }

    /**
     * Add the definition def associated to the symbol name in the environment.
     *
     * Adding a symbol which is already defined in the environment,
     * - throws DoubleDefException if the symbol is in the "current" dictionary
     * - or, hides the previous declaration otherwise.
     *
     * @param name
     *            Name of the symbol to define
     * @param def
     *            Definition of the symbol
     * @throws DoubleDefException
     *             if the symbol is already defined at the "current" dictionary
     *
     */
    public void declare(Symbol name, ExpDefinition def) throws DoubleDefException {
        if(definitions.get(name) != null) {
            throw new DoubleDefException();
        }
        definitions.put(name, def);
    }

    public ArrayList<MethodDefinition> getMethodsWithSignatureLike(Symbol methodName, int size) {
        ArrayList<MethodDefinition> methodDefs = new ArrayList<>();

        // Looking for every definitions in the environment
        for(Symbol symb : definitions.keySet()){

            // Aiming for methods
            if(definitions.get(symb).isMethod()){

                // Checking the method has a similar signature
                if(((MethodDefinition)definitions.get(symb)).getSignature().paramTypes.size() == size && ((MethodDefinition)definitions.get(symb)).getSignature().getMethodName().equals(methodName) ){
                    methodDefs.add((MethodDefinition)definitions.get(symb));
                }
            }
        }
        return methodDefs;
    }

}
