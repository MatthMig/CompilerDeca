package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import net.bytebuddy.dynamic.scaffold.MethodGraph.Compiler;

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


        // If i implicitly initialize a float with an int value.
        if(t2 == compiler.environmentType.INT &&
            t == compiler.environmentType.FLOAT ){
            this.setExpression(new ConvFloat(this.getExpr()));
            t2 = this.getExpression().verifyExpr(compiler, localEnv, currentClass);
        }

        else if(t2 == compiler.environmentType.FLOAT &&
            t == compiler.environmentType.INT ){
                throw new ContextualError("impossible conversion from float to int", getLocation());
        }

        // If i assign anything that isn't same type and isn't convFloat
        else if((t2 != t) && !(t.isClass() && t2.isClassOrNull())){
            throw new ContextualError("trying to asign a var of type " + t2 + " to a variable of type "+ t, getLocation());
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

    public AbstractExpr getExpr(){
        return this.expression;
    }

}
