package fr.ensimag.deca.tools;

import fr.ensimag.ima.pseudocode.Label;

public class LabelManager {
    
    private int whileCount;
    private int ifCOunt;

    public LabelManager() {
        whileCount = 0;
    }

    public Label[] createWhileLabels() {
        Label[] l = new Label[2];
        l[0] = new Label("while" + whileCount);
        l[1] = new Label("end_while" + whileCount);
        whileCount++;
        return l;
    }
}
