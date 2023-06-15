package fr.ensimag.deca.tools;

import fr.ensimag.ima.pseudocode.Label;

public class LabelManager {

    private int labelCount;
    private Label overflowLabel = new Label("overflow_error");
    private Label zeroDivisionLabel = new Label("zeroDivision_error");
    /**
     * Getter of the lowest free label integer
     * @return lowest free label int
     */
    public int getLabelCount() {
        return this.labelCount;
    }

    public LabelManager() {
        this.labelCount = 1;
    }

    /**
     * Creates 2 labels with the given prefix, a dot and the label count as a suffix
     * @param prefix The prefix of the label
     * @return An array of 2 labels
     */
    private Label createLabel(String prefix) {
        return new Label(prefix + "." + labelCount);
    }

    /**
     * Returns 2 labels for a while node
     * @return An array of 2 labels
     */
    public Label[] createWhileLabels() {
        Label[] labels = new Label[]{
            createLabel("whileBody"),
            createLabel("whileCond")
        };
        this.labelCount += 1;
        return labels;
    }

    /**
     * Returns 2 labels for an if node
     * @return An array of 2 labels
     */
    public Label[] createIfLabels() {
        Label[] labels = new Label[]{
            createLabel("else"),
            createLabel("endif")
        };
        this.labelCount += 1;
        return labels;
    }

    /**
     * Returns 2 labels for an AND or OR node
     * @return label
     */
    public Label createAndLabel() {
        Label label = createLabel("endAnd");
        this.labelCount += 1;
        return label;
    }

    /**
     * Returns 2 labels for an AND or OR or NOT node when generating an expression
     * @return label
     */
    public Label [] createBooleanExpLabel() {
        Label[] labels = new Label[]{
            createLabel("false"),
            createLabel("endBooleanExp")
        };
        this.labelCount += 1;
        return labels;
    }

    /**
     * Returns 2 labels for Boolean variable print
     * @return labels
     */
    public Label [] createBooleanVarPrintLabel() {
        Label[] labels = new Label[]{
            createLabel("truePrint"),
            createLabel("endPrint")
        };
        this.labelCount += 1;
        return labels;
    }

    /**
     * Returns the Overflow label
     * @return label
     */
    public Label getOverflowLabel(){
        return this.overflowLabel;
    }

    /**
     * Returns the ZeroDivision label
     * @return label
     */
    public Label getZeroDivisionLabel(){
        return this.zeroDivisionLabel;
    }

    /**
     * Returns the ZeroDivision label
     * @return label
     */
    public Label createInitClassLabel(String className){
        return new Label("init." + className);
    }
}
