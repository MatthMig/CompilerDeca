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
                            int parsedArg = Integer.parseInt(args[i+1]);
                            if( parsedArg <= 16 && parsedArg > 2){
                                this.setRMAX(parsedArg - 1);
                                System.out.println("Nombre maximum de registres mis à " + Integer.parseInt(args[i+1])  + " les registres utilisables sont les registres R0 à R" + this.getRMAX() );
                                i++;
                            } else {
                                throw new CLIException("L'option du compilateur \"-r\" ne prend en charge que les entiers positifs <= 16 et > 2");
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

                else if (arg.equals("-b")){
                    this.printBanner = true;
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

    public static String getLogo() {
        StringBuilder logoBuilder = new StringBuilder();

        // Building the first line of the logo
        logoBuilder.append("  GGGGG   L         0000  3333\n");

        // Building the second line of the logo
        logoBuilder.append(" G     G  L        0    0    3 \n");

        // Building the third line of the logo
        logoBuilder.append("G         L        0    0    3 \n");

        // Building the fourth line of the logo
        logoBuilder.append("G   GGG   L        0    0   3  \n");

        // Building the fifth line of the logo
        logoBuilder.append("G     G   L        0    0   3  \n");

        // Building the sixth line of the logo
        logoBuilder.append(" G     G  L        0    0    3 \n");

        // Building the seventh line of the logo
        logoBuilder.append("  GGGGG   LLLLLLL   0000  3333\n");

        return logoBuilder.toString();

    }


    public static final String DECAC_OPTIONS =
            "Syntaxe : decac [[-p | -v] [-n] [-r X] <fichier deca>...] | " +
			"[-b]\n" +
			"\t-b (banner) : affiche une bannière indiquant le nom de " +
			"l'équipe\n" +
			"\t-p (parse) : arrête decac après l'étape de construction de " +
			"l'arbre, et affiche la décompilation de ce dernier\n" +
			"\t-v (verification) : arrête decac après l'étape de " +
			"vérifications (ne produit aucune sortie en l'absence d'erreur)" +
			"\n" +
			"\t-n (no check) : n'ajoute pas les tests de débordement au " +
			"pogramme compilé (débordement arithmétique, débordement mémoire" +
			" " +
			"et déréférencement de null)\n" +
			"\t-r X (registers) : limite les registres banalisés disponibles" +
			" " +
			"à R0 ... R{X-1}, avec 4 <= X <= 16\n" +
			"\t-d (debug) : active les traces de debug. Répéter l'option " +
			"jusqu'à 3 fois pour avoir plus de traces\n" +
			"\t-P (parallel) : s'il y a plusieurs fichiers sources, lance la" +
			" " +
			"compilation des fichiers en parallèle pour l'accélérer\n" +
			"\t-w (warnings) : affiche les warnings à la compilation\n";

    protected void displayUsage() {
        System.out.println(DECAC_OPTIONS);
    }
}
