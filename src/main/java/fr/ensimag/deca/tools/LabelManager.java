package fr.ensimag.deca.tools;

import fr.ensimag.ima.pseudocode.Label;

public class LabelManager {

    private int labelCount;
    private Label overflowLabel = new Label("overflow_error");
    private Label zeroDivisionLabel = new Label("zeroDivision_error");
    private Label stackOverFlowLabel = new Label("stackOverFlow_error");
    private Label objetcEqualsLabel = new Label("code.Object.equals");
    private Label objectInitLabel = new Label("init.Object");
    private Label impossibleDownCastLabel = new Label("downcast_error");

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
     * Creates a label with : the given prefix, a dot and the index as a suffix
     * @param prefix The prefix of the label
     * @return The formatted label
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
    public Label [] createBooleanPrintLabel() {
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

    public Label getStackOverflowLabel() {
        return this.stackOverFlowLabel;
    }

    /**
     * Returns the fields initialization label
     * @return label
     */
    public Label createInitClassLabel(String className){
        return new Label("init." + className);
    }

    /**
    * Returns the method label
    * @return label
    */
    public Label createMethodLabel(String className, String methodSignature) {
        return new Label("code." + className + "." + methodSignature);
    }

    /**
     * Returns an array of labels for printing Null
     * @return label
     */
    public Label[] createNullLabels() {
        Label[] labels = new Label[]{
            createLabel("nullPrint"),
            createLabel("endNullPrint")
        };
        this.labelCount += 1;
        return labels;
    }
    /**
    * Returns the ObjectEquals label
    * @return label
    */
    public Label getObjectEqualsLabel() {
        return objetcEqualsLabel;
    }

    public Label getObjectInitLabel(){
        return objectInitLabel;
    }

    /**
     * Returns an array of labels for instanceof
     * @return labels
     */
    public Label[] createInstanceOfLabels() {
        Label[] labels = new Label[]{
            createLabel("while.instanceof"),
            createLabel("true.instanceof"),
            createLabel("false.instanceof"),
            createLabel("end.instanceof"),
        };
        this.labelCount += 1;
        return labels;
    }

     /**
     * Returns a label for downcast error
     * @return label
     */
    public Label getImpossibleDownCastLabel(){
        return this.impossibleDownCastLabel;
    }
}
