package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import java.io.PrintStream;
import static fr.ensimag.ima.pseudocode.Register.LB;
import fr.ensimag.ima.pseudocode.GPRegister;

/**
 *
 * @author gl03
 * @date 21/04/2023
 */
public class This extends AbstractExpr {
    public This() {}

    ClassDefinition def;
    
    ClassDefinition getClassDefinition() {
        return def;
    }
    
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        if (currentClass == null ){
            throw new ContextualError("cannot call this in main",this.getLocation());
        }
        ClassType t = currentClass.getType();
        this.setType(t);
        this.def = currentClass;
        return t;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("this");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        s.print(prefix);
        s.print("this");
        s.println();
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        //leaf node
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        compiler.addInstruction(new LOAD(new RegisterOffset(-2, LB), GPRegister.getR(1)));
    }

    @Override
    protected void codeGenExp(DecacCompiler compiler, int n){
        compiler.addInstruction(new LOAD(new RegisterOffset(-2, LB), GPRegister.getR(n)));
    }
}
