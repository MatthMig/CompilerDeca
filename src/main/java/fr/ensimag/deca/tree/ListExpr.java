package fr.ensimag.deca.tree;

import fr.ensimag.deca.tools.IndentPrintStream;

/**
 * List of expressions (eg list of parameters).
 *
 * @author gl03
 * @date 21/04/2023
 */
public class ListExpr extends TreeList<AbstractExpr> {


    @Override
    public void decompile(IndentPrintStream s) {
        for(AbstractExpr expr : this.getList()){
            expr.decompile(s);
            if(getList().indexOf(expr) != getList().size() -1)
                s.print(", ");
        }
    }
}
