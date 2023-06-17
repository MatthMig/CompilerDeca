package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.BSR;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.NEW;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.STORE;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;

/**
 *
 * @author gl03
 * @date 13/06/2023
 */
public class New extends AbstractExpr {
    final private AbstractIdentifier className;

    public New(AbstractIdentifier className) {
        Validate.notNull(className);
        this.className = className;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        className.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        className.prettyPrint(s, prefix, true);
    }

   @Override
   public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
           throws ContextualError {
        ClassDefinition cd  = compiler.environmentType.defOfClass(this.className.getName());
        if(cd != null){
            this.className.setType(cd.getType());
            this.className.setDefinition(cd);
            this.setType(cd.getType());
            return this.getType();
        }
        throw new ContextualError("class " + this.className.getName().getName() + " is not defined.", getLocation());
   }

   @Override
   protected void codeGenExp(DecacCompiler compiler, int n) {
        compiler.addInstruction(new NEW(this.className.getClassDefinition().getNumberOfFields() + 1, GPRegister.getR(2)));
        //compiler.addInstruction(new BOV(new Label(null)));

        // HERE WE NEED TO ADD THE FUTURE ADDRESS OF THE METHOD TABLE compiler.addInstruction(new LEA());
        // compiler.addInstruction(new STORE(GPRegister.R0, new RegisterOffset(0, Register.getR(n))));
        compiler.addInstruction(new PUSH(GPRegister.getR(2)));
        ClassDefinition classDefinition = compiler.environmentType.defOfClass(this.className.getName());
        while(classDefinition != null){
            compiler.addInstruction(new BSR(compiler.getLabelManager().createInitClassLabel(classDefinition.getType().getName().getName())));
            classDefinition = classDefinition.getSuperClass();
        }

        compiler.addInstruction(new POP(GPRegister.getR(2)));
   }


    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        throw new UnsupportedOperationException("not yet implemented");
    }

}
