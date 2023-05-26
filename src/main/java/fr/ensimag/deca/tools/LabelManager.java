package fr.ensimag.deca.tools;

import fr.ensimag.ima.pseudocode.Label;

public class LabelManager {
    
    private int whileCount;
    private int ifCount;
    private int ifOnlyCount;

    public LabelManager() {
        whileCount = 0;
        ifCount = 0;
    }

    public Label[] createWhileLabels() {
        Label[] l = new Label[2];
        l[0] = new Label("while_" + whileCount);
        l[1] = new Label("cond_while_" + whileCount);
        whileCount++;
        return l;
    }

    public Label[] createIfLables() {
        Label[] l = new Label[2];
        l[0] = new Label("else_" + ifCount);
        l[1] = new Label("end_if_" + ifCount);
        ifCount++;
        return l;
    }

	public Label createIfLabel() {
        ifOnlyCount++;
        return new Label("if_only_" + ifOnlyCount);
	}
}
