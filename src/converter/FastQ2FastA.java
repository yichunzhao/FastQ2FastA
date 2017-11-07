/*
 * Converting fastQ to fastA
 * 
 * 
 */
package converter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author YNZ
 */
public class FastQ2FastA {

    public static final String Name_Start_Regx = "@[\\d+]";
    public static final String Name_End_Regx = "\\+[\\d*]";
    public static final String Dna_Seq_Regx = "[ACGT]{2,}";
    public static final String Quality_Scores_Regx = "[\\d+]";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {

        if (args.length == 0 || !(args[0].contains(".fastq"))) {
            System.out.println("Please input a valid file with extenstion .fastq");
            return;
        }

        String fileName = args[0];

        System.out.println(" Input content: ");
        System.out.println("");
        String str = readText2String(new File(fileName));
        System.out.println(str);
        System.out.println("");

        System.out.println(" Start to convert: ");

        //convert FastQ to FastA
        StringBuilder sb = convert(new File(fileName));
        System.out.println("" + sb.toString());

        //write result in another file, which contains "_result"
        String[] fileNameParts = fileName.split("\\.");
        String resultFileName = fileNameParts[0] + "_result." + fileNameParts[1];
        writeResult2File(new File(resultFileName), sb);

    }

    private static boolean isNameStart(String line) {
        Pattern p = Pattern.compile(FastQ2FastA.Name_Start_Regx);
        Matcher matcher = p.matcher(line);
        return matcher.find();
    }

    private static boolean isDnaSeq(String line) {
        Pattern p = Pattern.compile(FastQ2FastA.Dna_Seq_Regx);
        Matcher matcher = p.matcher(line);
        return matcher.find();
    }

    //not used yet
    private static boolean isNameEnd(String line) {
        Pattern p = Pattern.compile(FastQ2FastA.Name_End_Regx);
        Matcher matcher = p.matcher(line);
        return matcher.find();
    }

    //not used yet
    private static boolean isQualityScores(String line) {
        Pattern p = Pattern.compile(FastQ2FastA.Quality_Scores_Regx);
        Matcher matcher = p.matcher(line);
        return matcher.find();
    }

    private static StringBuilder convert(File file) throws IOException {
        StringBuilder sb = new StringBuilder();

        try (
                FileReader fr = new FileReader(file);
                BufferedReader br = new BufferedReader(fr);) {

            String line = null;
            while ((line = br.readLine()) != null) {
                if (isNameStart(line)) {
                    String aLine = line.replace('@', '>');
                    sb.append(aLine);
                    sb.append("\n");
                }

                if (isDnaSeq(line)) {
                    sb.append(line);
                    sb.append("\n");
                }
            }
        }

        return sb;
    }

    private static String readText2String(File file) throws IOException {
        StringBuilder sb = new StringBuilder();

        try (
                FileReader fr = new FileReader(file);
                BufferedReader br = new BufferedReader(fr);) {

            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        }

        return sb.toString();
    }

    private static void writeResult2File(File file, StringBuilder result) throws IOException {

        try (
                FileWriter fw = new FileWriter(file);
                BufferedWriter bw = new BufferedWriter(fw);) {

            bw.write(result.toString());
        }

    }

}
