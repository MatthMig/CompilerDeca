package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.VariableDefinition;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.EnvironmentType;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
import fr.ensimag.ima.pseudocode.instructions.INT;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.SETROUND_TOWARDZERO;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;

/**
 *
 * @author gl03
 * @date 20/06/2023
 */
public class Cast extends AbstractExpr {
    final private AbstractIdentifier castType;
    final private AbstractExpr expression;

    public Cast(AbstractIdentifier castType, AbstractExpr expression){
        Validate.notNull(castType);
        Validate.notNull(expression);
        this.castType = castType;
        this.expression = expression;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("(");
        s.print(this.castType.getName().getName());
        s.print(")");
        s.print("(");
        expression.decompile();
        s.print(")");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        castType.iter(f);
        expression.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        castType.prettyPrint(s, prefix, false);
        expression.prettyPrint(s, prefix, true);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
        throws ContextualError {
            Type t = castType.verifyType(compiler);
            Type expressionType = expression.verifyExpr(compiler, localEnv, currentClass); // returns static type of expression

            //Class cast case
            if(expressionType.isClass()){
                ClassDefinition classDef =compiler.environmentType.defOfClass(expressionType.getName());
                ClassDefinition castClass =compiler.environmentType.defOfClass(t.getName());
                castType.setDefinition(castClass);

                if(castClass.isParentClassOf(classDef) || classDef.isParentClassOf(castClass) ){
                    castType.setType(t);
                    expression.setType(expressionType);
                    this.setType(t);
                }else{
                    throw new ContextualError("Cannot cast object of type " + expressionType + " to type " + t , getLocation());
                }
            } else{
                Definition typeDef = compiler.environmentType.defOfType(t.getName());
                if(expressionType.isFloat()){
                    if(t.isInt() || t.isFloat()){
                        castType.setType(t);
                        expression.setType(expressionType);
                        this.setType(t);
                    } else {
                        throw new ContextualError("Cannot cast object of type " + expressionType + " to type " + t , getLocation());
                    }
                }
                else if (expressionType.isInt()) {
                    if(t.isInt() || t.isFloat()){
                        castType.setType(t);
                        expression.setType(expressionType);
                        this.setType(t);
                    } else {
                        throw new ContextualError("Cannot cast object of type " + expressionType + " to type " + t , getLocation());
                    }
                }
                castType.setDefinition(typeDef);
            }



            return t;

    }

    @Override
    protected void codeGenExp(DecacCompiler compiler, int n) {

        expression.codeGenExp(compiler, n);


        Type t = castType.getType();
        Type expressionType = expression.getType();

        if(expressionType.isClass()){
            ClassDefinition classDef;
            if(expressionType.getName().getName().equals("Object")){
                classDef = ((ClassDefinition)compiler.environmentType.defOfType(compiler.environmentType.OBJECT.getName()));
            }
            else{
                classDef =compiler.environmentType.defOfClass(expressionType.getName());
            }
            ClassDefinition castClass =compiler.environmentType.defOfClass(t.getName());


            //Downcast case
            if(classDef.isParentClassOf(castClass) ){
                InstanceOf iOf = new InstanceOf(expression, castType);
                iOf.codeGenExp(compiler, n+1);
                //compare 1 with R2
                compiler.addInstruction(new CMP(1,GPRegister.getR(n+1)));
                //BNE on error
                compiler.addInstruction(new BNE(compiler.getLabelManager().getImpossibleDownCastLabel()));
            }

        } else {
            if(t.isFloat()&&expressionType.isInt()){
                //cast in float
                compiler.addInstruction(new FLOAT(GPRegister.getR(n),GPRegister.getR(n)));
            }else{
                if(t.isInt()&&expressionType.isFloat()){
                //cast in int
                compiler.addInstruction(new SETROUND_TOWARDZERO());
                compiler.addInstruction(new INT(GPRegister.getR(n),GPRegister.getR(n)));
                }
            }
        }
    }


    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        this.codeGenExp(compiler, 2);
    }

}
