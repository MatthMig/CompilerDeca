package fr.ensimag.deca.tree;

import fr.ensimag.deca.tools.IndentPrintStream;

/**
 * Visibility of a field.
 *
 * @author gl03
 * @date 21/04/2023
 */

public enum Visibility {
    PUBLIC,
    PROTECTED;

    public void decompile(IndentPrintStream s) {
        if (this == Visibility.PROTECTED) {
            s.print(Visibility.PROTECTED.toString().toLowerCase() + " ");
        }
    }
}
