package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
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
        for(AbstractDeclClass dClass : this.getList()){
            dClass.codeGenMethodTable(compiler);
        }
    }

    public void codeGenClasses(DecacCompiler compiler) {
        for(AbstractDeclClass dClass : this.getList()){
            dClass.codeGenClass(compiler);
        }
    }


}
