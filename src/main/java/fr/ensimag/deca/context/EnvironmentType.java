package fr.ensimag.deca.context;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;

import java.util.HashMap;
import java.util.Map;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.AbstractIdentifier;
import fr.ensimag.deca.tree.Location;
import fr.ensimag.deca.tree.Visibility;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;

// Lors de l’allocation d’un objet, les champs sont initialisés dans l’ordre de déclaration.
// Un champ non initialisé, ou accédé avant d’être initialisé, a la valeur par défaut 0 pour entier,
// 0.0 pour un flottant, false pour un booléen, ou null pour un objet.
/**
 * Environment containing types. Initially contains predefined identifiers, more
 * classes can be added with declareClass().
 *
 * @author gl03
 * @date 21/04/2023
 */
public class EnvironmentType {
    public EnvironmentType(DecacCompiler compiler) {

        envTypes = new HashMap<Symbol, TypeDefinition>();

        Symbol intSymb = compiler.createSymbol("int");
        INT = new IntType(intSymb);
        envTypes.put(intSymb, new TypeDefinition(INT, Location.BUILTIN));

        Symbol floatSymb = compiler.createSymbol("float");
        FLOAT = new FloatType(floatSymb);
        envTypes.put(floatSymb, new TypeDefinition(FLOAT, Location.BUILTIN));

        Symbol voidSymb = compiler.createSymbol("void");
        VOID = new VoidType(voidSymb);
        envTypes.put(voidSymb, new TypeDefinition(VOID, Location.BUILTIN));

        Symbol booleanSymb = compiler.createSymbol("boolean");
        BOOLEAN = new BooleanType(booleanSymb);
        envTypes.put(booleanSymb, new TypeDefinition(BOOLEAN, Location.BUILTIN));

        Symbol nullSymb = compiler.createSymbol("null");
        NULL = new NullType(nullSymb);
        envTypes.put(nullSymb, new TypeDefinition(NULL, Location.BUILTIN));

        Symbol stringSymb = compiler.createSymbol("string");
        STRING = new StringType(stringSymb);
        //envTypes.put(stringSymb, new TypeDefinition(STRING, Location.BUILTIN));
        // not added to envTypes, it's not visible for the user.

        Symbol objectSymb = compiler.createSymbol("Object");
        OBJECT = new ClassType(objectSymb);
        ClassDefinition objectClassDefinition = new ClassDefinition(OBJECT, Location.BUILTIN, null);
        objectClassDefinition.setMethodTableAddr(new RegisterOffset(1, Register.GB));
        Symbol equals = compiler.createSymbol("equals");
        Signature equalsSignature = new Signature(equals);
        equalsSignature.addParamType(OBJECT);
        MethodDefinition objectEqualsDef = new MethodDefinition(BOOLEAN, Location.BUILTIN, equalsSignature, Visibility.PUBLIC, 1);
        objectEqualsDef.setLabel(compiler.getLabelManager().getObjectEqualsLabel());
        try {
            objectClassDefinition.getMembers().declare(compiler.createSymbol(equalsSignature.toString()), objectEqualsDef);
        }
        // Cannot happen
        catch(DoubleDefException doubleDefException){
            System.out.println("Error when initializing Object class");
        }


        Signature tmpSignature = new Signature(equals);
        tmpSignature.addParamType(OBJECT);

        objectClassDefinition.incNumberOfMethods();
        objectClassDefinition.getMethodTable().addMethod(objectEqualsDef, compiler.getLabelManager().getObjectEqualsLabel());
        envTypes.put(objectSymb, objectClassDefinition);

    }

    private final Map<Symbol, TypeDefinition> envTypes;

    public TypeDefinition defOfType(Symbol s) {
        return envTypes.get(s);
    }

    /**
     * Get the definition of a class from this environment, null if it doesn't exist.
     * @param s : Symbol of the class
     * @return ClassDefinition | null
     */
    public ClassDefinition defOfClass(Symbol s){
        TypeDefinition td = envTypes.get(s);
        if(td instanceof ClassDefinition){
            return (ClassDefinition)td;
        }
        return null;
    }

    public void declareClass(AbstractIdentifier className, ClassType classType){
        envTypes.put(className.getName(), classType.getDefinition());
    }

    public final VoidType    VOID;
    public final IntType     INT;
    public final FloatType   FLOAT;
    public final StringType  STRING;
    public final BooleanType BOOLEAN;
    public final ClassType   OBJECT;
    public final NullType NULL;
}
