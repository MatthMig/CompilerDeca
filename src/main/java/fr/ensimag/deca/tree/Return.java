package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

import java.io.PrintStream;

/**
 *
 * @author gl03
 * @date 13/06/2023
 */
public class Return extends AbstractInst {
    private AbstractExpr returnExpr;

    public Return(AbstractExpr returnExpr) {
        // No null verification, null is allowed to return void.
        this.returnExpr = returnExpr;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        if(this.returnExpr != null)
            this.returnExpr.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        if (returnExpr != null) {
            returnExpr.prettyPrint(s, prefix, true);
        }
    }

   @Override
   protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass,
           Type returnType) throws ContextualError {
        Type t;
        if (this.returnExpr == null) {
            // If there is no return expression, then the returned type is void.
            t = compiler.environmentType.VOID;
        } else {
            t = this.returnExpr.verifyExpr(compiler, localEnv, currentClass);
            if(this.returnExpr instanceof Identifier){
                if(((Identifier)returnExpr).getDefinition().isField()){
                    This newThis = new This();
                    newThis.setLocation(getLocation());
                    this.returnExpr = new Selection(newThis, (Identifier)returnExpr);
                    this.returnExpr.setLocation(getLocation());
                    this.returnExpr.verifyInst(compiler, localEnv, currentClass, t);
                }
            }
            this.returnExpr.setType(t);
        }

        // If the returned type is not the same as the method's return type, then there is a contextual error.
        if (!t.sameType(returnType)){
            if(t.isClass() && returnType.isClass() || returnType.isClass() && t.isNull()){
                if(!t.isNull()){
                    ClassDefinition tDef = compiler.environmentType.defOfClass(t.getName());
                    ClassDefinition returnTypeDef = compiler.environmentType.defOfClass(returnType.getName());
                    if(!returnTypeDef.isParentClassOf(tDef)){
                        System.out.println("ss");
                        throw new ContextualError("Return type must be " + returnType + ", currently is " + t, this.getLocation());
                    }
                }
            }
            else{
                throw new ContextualError("Return type must be " + returnType + ", currently is " + t, this.getLocation());
            }
        }
   }


    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        if(this.returnExpr != null)
            this.returnExpr.codeGenExp(compiler, 2);

        compiler.addInstruction(new LOAD(GPRegister.getR(2), GPRegister.getR(0)));
    }

}
