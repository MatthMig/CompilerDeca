package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;

/**
 * @author gl03
 * @date 21/04/2023
 */
public class Initialization extends AbstractInitialization {

    public AbstractExpr getExpression() {
        return expression;
    }

    private AbstractExpr expression;

    public void setExpression(AbstractExpr expression) {
        Validate.notNull(expression);
        this.expression = expression;
    }

    public Initialization(AbstractExpr expression) {
        Validate.notNull(expression);
        this.expression = expression;
    }

    @Override
    protected void verifyInitialization(DecacCompiler compiler, Type t,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        Type t2 = this.expression.verifyExpr(compiler, localEnv, currentClass);
        ClassDefinition tcd = compiler.environmentType.defOfClass(t.getName());

        // If we implicitly initialize a float with an int value.
        if(t2 == compiler.environmentType.INT &&
            t == compiler.environmentType.FLOAT ){
            this.setExpression(new ConvFloat(this.getExpression()));
            t2 = this.getExpression().verifyExpr(compiler, localEnv, currentClass);
        }

        // If we try to do the opposite
        else if(t2 == compiler.environmentType.FLOAT &&
            t == compiler.environmentType.INT ){
                throw new ContextualError("Impossible conversion from float to int", getLocation());
        }

        // else if variable type is a class
        else if(tcd != null){
            // If i assign anything that is a class to a class
            if (t.isClass() && t2.isClass()) {
                ClassDefinition cd = compiler.environmentType.defOfClass(t2.getName());

                // if the variable class is not a super class of the assigned class
                if(!tcd.isParentClassOf(cd)){
                throw new ContextualError("Trying to assign a value of type " + t2 + " to a value of type "+ t, getLocation());
                }
            // If i assign anything that isn't null nor a class
            } else if (!(t.isClass() && t2.isNull())) {
                throw new ContextualError("Trying to asign a value of type " + t2 + " to a value of type "+ t, getLocation());
            }
        }
        
        else if (!t.sameType(t2)) {
            throw new ContextualError("Trying to asign a value of type " + t2 + " to a value of type "+ t, getLocation());
        }
    }

    @Override
    public void codeGen(DecacCompiler compiler) {
        this.expression.codeGenExp(compiler, 2);
    }


    @Override
    public void decompile(IndentPrintStream s) {
        s.print("=");
        this.expression.decompile(s);
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        expression.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        expression.prettyPrint(s, prefix, true);
    }


}
