package fr.ensimag.deca;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

/**
 * Main class for the command-line Deca compiler.
 *
 * @author gl03
 * @date 21/04/2023
 */
public class DecacMain {
    private static Logger LOG = Logger.getLogger(DecacMain.class);
    
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        // example log4j message.
        LOG.info("Decac compiler started");
        boolean error = false;
        final CompilerOptions options = new CompilerOptions();
        try {
            options.parseArgs(args);
        } catch (CLIException e) {
            System.err.println("Error during option parsing:\n"
                    + e.getMessage());
            options.displayUsage();
            System.exit(1);
        }
        if (options.getPrintBanner()) {
            System.out.println(options.getLogo());
        }
        else{
            
            if (options.getSourceFiles().isEmpty()) {
                System.err.println("Error in command syntax : No file provided to compile.");
                options.displayUsage();
            }
            if (options.getParallel()) {
                // Initialize threads factory
                ExecutorService execService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
                ArrayList<Future<Boolean>> results = new ArrayList<Future<Boolean>>();

                for (File source : options.getSourceFiles()) {
                    DecacCompiler compiler = new DecacCompiler(options, source);
                    results.add(execService.submit(() -> compiler.compile()));
                }

                // Wait for each thread to end to get their return code
                for (int i = 0 ; i <= results.size() -1 ; i++){
                    if (results.get(i).get()){
                        error = true;
                    }
                }

                // Properly finish threads factory
                execService.shutdown();
                execService.awaitTermination(2, TimeUnit.HOURS);

            } else {
                for (File source : options.getSourceFiles()) {
                    DecacCompiler compiler = new DecacCompiler(options, source);
                    if (compiler.compile()) {
                        error = true;
                    }
                }
            }
            System.exit(error ? 1 : 0);
        }
        System.exit(0);
    }
}
