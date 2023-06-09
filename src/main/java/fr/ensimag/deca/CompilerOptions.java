package fr.ensimag.deca;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * User-specified options influencing the compilation.
 *
 * @author gl03
 * @date 21/04/2023
 */
public class CompilerOptions {
    public static final int QUIET = 0;
    public static final int INFO  = 1;
    public static final int DEBUG = 2;
    public static final int TRACE = 3;

    public int getDebug() {
        return debug;
    }

    public boolean getParallel() {
        return parallel;
    }

    public boolean getPrintBanner() {
        return printBanner;
    }

    public boolean getVerifyOnly(){
        return verifyOnly;
    }

    public boolean getDecompile(){
        return decompile;
    }

    public boolean getNoCheck(){
        return noCheck;
    }
        
    public List<File> getSourceFiles() {
        return Collections.unmodifiableList(sourceFiles);
    }

    // Getter allowing you to set maximum register count in options
    public void setRMAX(int RMAX){
        this.RMAX = RMAX;
    }

    // Getter returning RMAX value.
    public int getRMAX(){
        return this.RMAX;
    }

    private int debug = 0;
    private int RMAX = 16;
    private boolean parallel = false;
    private boolean printBanner = false;
    private boolean verifyOnly = false;
    private boolean decompile = false;
    private boolean noCheck = false;
    private List<File> sourceFiles = new ArrayList<File>();

    
    public void parseArgs(String[] args) throws CLIException {

        for(int i = 0 ; i < args.length ; i++){
                String arg = args[i];
                if(arg.equals("-v")){
                    verifyOnly = true;
                }
    
                else if(arg.equals("-p")){
                    decompile = true;
                }

                else if(arg.equals("-n")){
                    noCheck = true;
                }
    
                else if(arg.equals("-r")){
                    if( i < args.length -1 ){
                        try{         
                            if(Integer.parseInt(args[i+1]) <= 16 && Integer.parseInt(args[i+1]) > 2){
                                this.setRMAX(Integer.parseInt(args[i+1]) - 1);
                                System.out.println("Nombre maximum de registres mis à " + Integer.parseInt(args[i+1])  + " les registres utilisables sont les registres R0 à R" + this.getRMAX() );
                                i++;
                            } else {
                                throw new CLIException("L'option du compilateur \"-r\" ne prend en charge que les entiers positifs <= 16 et >= 2");
                            }
                        }
                        catch(NumberFormatException nfe){
                            throw new CLIException("L'option du compilateur \"-r\" ne prend en charge que les entiers");
                        }
                    }
                    else{
                        throw new CLIException("L'option du compilateur \"-r\" nécessite un paramètre");
                    }
                }

                else if (arg.equals("-P")){
                    this.parallel = true;
                }
    
                else {
                    sourceFiles.add(new File(args[i]));
                }

        }

        // A FAIRE : parcourir args pour positionner les options correctement.
        Logger logger = Logger.getRootLogger();
        // map command-line debug option to log4j's level.
        switch (getDebug()) {
        case QUIET: break; // keep default
        case INFO:
            logger.setLevel(Level.INFO); break;
        case DEBUG:
            logger.setLevel(Level.DEBUG); break;
        case TRACE:
            logger.setLevel(Level.TRACE); break;
        default:
            logger.setLevel(Level.ALL); break;
        }
        logger.info("Application-wide trace level set to " + logger.getLevel());

        boolean assertsEnabled = false;
        assert assertsEnabled = true; // Intentional side effect!!!
        if (assertsEnabled) {
            logger.info("Java assertions enabled");
        } else {
            logger.info("Java assertions disabled");
        }

        // throw new UnsupportedOperationException("not yet implemented");
    }

    protected void displayUsage() {
        throw new UnsupportedOperationException("not yet implemented");
    }
}
