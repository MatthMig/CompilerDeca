package fr.ensimag.deca.context;

import fr.ensimag.deca.tools.MethodTable;
import fr.ensimag.deca.tree.Location;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.DVal;
import org.apache.commons.lang.Validate;

/**
 * Definition of a class.
 *
 * @author gl03
 * @date 21/04/2023
 */
public class ClassDefinition extends TypeDefinition {

    public void setNumberOfFields(int numberOfFields) {
        this.numberOfFields = numberOfFields;
    }

    public int getNumberOfFields() {
        return numberOfFields;
    }

    public void incNumberOfFields() {
        this.numberOfFields++;
    }

    public int getNumberOfMethods() {
        return numberOfMethods;
    }

    public void setNumberOfMethods(int n) {
        Validate.isTrue(n >= 0);
        numberOfMethods = n;
    }

    public int incNumberOfMethods() {
        numberOfMethods++;
        return numberOfMethods;
    }

    public void setMethodTableAddr(DAddr mehtodTableAddr) {
        this.methodTableAddr = mehtodTableAddr;
    }

    public DAddr getMethodTableAddr() {
        return methodTableAddr;
    }

    public MethodTable getMethodTable(){
        return this.methodTable;
    }

    private int numberOfFields = 0;
    private int numberOfMethods = 0;
    private DAddr methodTableAddr = null;
    private final MethodTable methodTable;

    @Override
    public boolean isClass() {
        return true;
    }

    @Override
    public ClassType getType() {
        // Cast succeeds by construction because the type has been correctly set
        // in the constructor.
        return (ClassType) super.getType();
    };

    public ClassDefinition getSuperClass() {
        return superClass;
    }

    private final EnvironmentExp members;
    private final ClassDefinition superClass;

    public EnvironmentExp getMembers() {
        return members;
    }

    public ClassDefinition(ClassType type, Location location, ClassDefinition superClass) {
        super(type, location);
        EnvironmentExp parent;
        this.methodTable = new MethodTable();
        if (superClass != null) {
            parent = superClass.getMembers();
        } else {
            parent = null;
        }
        members = new EnvironmentExp(parent);
        this.superClass = superClass;
    }

    public boolean isParentClassOf(ClassDefinition subClassDefinition){
        while(subClassDefinition != null){
            if(subClassDefinition == this){
                return true;
            }
            subClassDefinition = subClassDefinition.getSuperClass();
        }
        return false;
    }

}
