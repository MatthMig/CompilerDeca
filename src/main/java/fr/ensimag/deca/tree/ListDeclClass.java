package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.RTS;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.WSTR;

import org.apache.log4j.Logger;

/**
 *
 * @author gl03
 * @date 21/04/2023
 */
public class ListDeclClass extends TreeList<AbstractDeclClass> {
    private static final Logger LOG = Logger.getLogger(ListDeclClass.class);

    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclClass c : getList()) {
            c.decompile(s);
            s.println();
        }
    }

    /**
     * Pass 1 of [SyntaxeContextuelle]
     */
    void verifyListClass(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify listClass: start");
            LOG.debug("passe 1 : start");
        for(AbstractDeclClass dClass : this.getList()){
            dClass.verifyClass(compiler);
        }
            LOG.debug("passe 1 : end");
    }

    /**
     * Pass 2 of [SyntaxeContextuelle]
     */
    public void verifyListClassMembers(DecacCompiler compiler) throws ContextualError {
        LOG.debug("passe 2 : start");
        for(AbstractDeclClass dClass : this.getList()){
            dClass.verifyClassMembers(compiler);
        }
        LOG.debug("passe 2 : end");

    }

    /**
     * Pass 3 of [SyntaxeContextuelle]
     */
    public void verifyListClassBody(DecacCompiler compiler) throws ContextualError {
        LOG.debug("passe 3 : start");
        for(AbstractDeclClass dClass : this.getList()){
            dClass.verifyClassBody(compiler);
        }
        LOG.debug("passe 3 : end");;
    }


    public void codeGenMethodTable(DecacCompiler compiler){
        compiler.addComment("start : method table");
        // Store null address
        compiler.addInstruction(new LOAD(new NullOperand(), Register.R0));
        compiler.addInstruction(new STORE(Register.R0, compiler.allocate()));

        // Store Object.equals
        compiler.addInstruction(new LOAD(compiler.getLabelManager().getObjectEqualsLabel(), Register.R0));
        compiler.addInstruction(new STORE(Register.R0, compiler.allocate()));

        for(AbstractDeclClass dClass : this.getList()){
            dClass.codeGenMethodTable(compiler);
        }
        compiler.addComment("end : method table");
    }

    public void codeGenClasses(DecacCompiler compiler) {
        for(AbstractDeclClass dClass : this.getList()){
            dClass.codeGenClass(compiler);
        }
    }

    public void codeGenEqualsMethod(DecacCompiler compiler) {
        compiler.addLabel(compiler.getLabelManager().getObjectInitLabel());
        compiler.addInstruction(new RTS());
        compiler.addLabel(compiler.getLabelManager().getObjectEqualsLabel());
        compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), Register.R1));
        compiler.addInstruction(new CMP(new RegisterOffset(-3, Register.LB), Register.R1));
        compiler.addInstruction(new BEQ(new Label("are_equals")));
        compiler.addInstruction(new LOAD(0, Register.R0));
        compiler.addInstruction(new BRA(new Label("end_are_equals")));;
        compiler.addLabel(new Label("are_equals"));
        compiler.addInstruction(new LOAD(1, Register.R0));
        compiler.addLabel(new Label("end_are_equals"));;
        compiler.addInstruction(new RTS());
    }


}
