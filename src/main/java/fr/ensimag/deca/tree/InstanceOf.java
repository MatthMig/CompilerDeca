package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;

import java.io.PrintStream;

public class InstanceOf extends AbstractExpr {

    // represents the expression that needs to be checked for being an instance of a certain class.
    private final AbstractExpr objectLeft;

    // represents the name of the class that is being tested against.
    private final AbstractIdentifier classRight;

    // constructor for InstanceOf
    public InstanceOf(AbstractExpr objectLeft, AbstractIdentifier classRight) {
        this.objectLeft = objectLeft;
        this.classRight = classRight;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError
    {
        // to verify the types of both operands
        Type objectLeftType = objectLeft.verifyExpr(compiler, localEnv, currentClass);
        Type classRightType = classRight.verifyType(compiler);

        if (!objectLeftType.isClass()) {
            throw new ContextualError("instanceOf left operand is not an object",
                    objectLeft.getLocation());
        }

        if (!classRightType.isClass()) {
            throw new ContextualError("instanceOf right operand is not a class",
                    classRight.getLocation());
        }

        setType(compiler.environmentType.BOOLEAN);
        return compiler.environmentType.BOOLEAN;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        objectExpr.decompile(s);
        s.print(" instanceof ");
        className.decompile(s);
    }

    // iterating over the children of the instanceOf expression and applying f to each child. 
    @Override
    protected void iterChildren(TreeFunction f) {
        objectExpr.iter(f);
        className.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        objectExpr.prettyPrint(s, prefix, false);
        className.prettyPrint(s, prefix, true);
    }
}
