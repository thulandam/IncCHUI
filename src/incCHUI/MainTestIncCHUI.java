package incCHUI;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import util.WriteToCSVFile;

/**
 * Example of how to use the IncCHUI algorithm from the source code by
 * processing a single file in several parts (updates).
 *
 * @author LanDT
 */
public class MainTestIncCHUI {

    public static void main(String[] args) throws IOException {

        //String input = fileToPath("kosarak_utility.txt");  // retails_utilityPFV
        String input;
        //String input = "DB_UtilityIncremental1.txt";
        String output;
        // int min_utility = 30;

        boolean useMinRate = false;
        double min_utility_ratio = 0.005d;
        int min_utility;//(int) (14910915 * min_utility_ratio); 

        // the number of updates to be performed
        //int numberOfUpdates = 4;
        //double addedratio = 1d / ((double) numberOfUpdates);
        double addedratio;
        int hashtablesize;

        if (args.length == 5) {
            input = args[0];
            output = args[1];
            min_utility = Integer.parseInt(args[2]);
            addedratio = Double.parseDouble(args[3]);
            hashtablesize = Integer.parseInt(args[4]);
        } else {
            input = "D:\\Datasets\\utility\\connect_utility.txt";
            output = "incChui-connect.txt";
            min_utility = 12000000;// (int) (14910915 * MinUtilThres);
            addedratio = 0.1d;
            hashtablesize = 50000;
        }
        // scan the database to count the number of lines
        // for our test purpose
        int linecount = countLines(input);
        String[] words = input.split("\\\\");

        int linesForeEachUpdate = (int) Math.ceil(addedratio * linecount);
        System.out.println("Added ratio: " + addedratio);
        if (useMinRate) {
            System.out.println("Minutil Ratio: " + min_utility_ratio);
        } else {
            System.out.println("Minutil : " + min_utility);
        }
        System.out.println("Dataset: " + words[3]);

        // Apply the algorithm several times
        //AlgoIncCHUI algo = new AlgoIncCHUI();
        AlgoIncCHUI algo = new AlgoIncCHUI();
        int firstLine = 0;
        int lineProcessed = 0;
        int i = 1;
        //for(int i = 0; i < numberOfUpdates; i++){
        while (lineProcessed < linecount) {
            int lastLine = firstLine + linesForeEachUpdate;
            System.out.println("" + i + ") Run the algorithm using line " + firstLine + " to before line " + (lastLine < linecount ? lastLine : linecount) + " of the input database.");
            algo.runAlgorithm(input, min_utility, firstLine, lastLine, hashtablesize, useMinRate, min_utility_ratio);

            algo.printStats();            
            firstLine = lastLine;
            i++;
            lineProcessed += linesForeEachUpdate;
            Runtime.getRuntime().gc();
        }
        System.out.println("Total closed high-utility itemsets count : " + algo.getRealCHUICount());
         System.out.println("TOTAL TIME FOR ALL RUNS: " + (algo.totalTimeForAllRuns / 1000.0) + " sec");
        System.out.println("Max memory used: " + AlgoIncCHUI.maxMemoryUsed);
        //algo.timeData.add(algo.totalTimeForAllRuns/1000.0);
        
        //WRITE ALL THE CHUIs found until now to a file
        algo.writeCHUIsToFile(output);
       // WriteToCSVFile.writeCsvFile(addedratio+"", min_utility, algo.timeData, 4,output+".xls");
        WriteToCSVFile.writeCsvFile(min_utility+"", min_utility, algo.memData,4, output+".xls");
    }

    /**
     * This methods counts the number of lines in a text file.
     *
     * @param filepath the path to the file
     * @return the number of lines as an int
     * @throws IOException Exception if error reading/writting file
     */
    public static int countLines(String filepath) throws IOException {
        LineNumberReader reader = new LineNumberReader(new FileReader(filepath));
        while (reader.readLine() != null) {
        }
        int count = reader.getLineNumber();
        reader.close();
        return count;
    }

    public static String fileToPath(String filename)
            throws UnsupportedEncodingException {
        URL url = MainTestIncCHUI.class.getResource(filename);
        return java.net.URLDecoder.decode(url.getPath(), "UTF-8");
    }

    }
