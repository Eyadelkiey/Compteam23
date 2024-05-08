package team23compiler;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class MainClass {
	public static void main(String[] args) {
		System.out.println("Please enter the name of the input file: ");
		Scanner c = new Scanner(System.in);
		String inputFileName = c.nextLine();
		
		System.out.println("Please enter the name of the output file: ");
		String outputFileName = c.nextLine();
		
		CScanner lexicalAnalyzer = new CScanner();
        Scanner s;
        File f_in = new File(inputFileName);
        File f_out = new File(outputFileName);
        FileWriter fw;
        try {
            s = new Scanner(f_in);
            fw = new FileWriter(f_out);
            fw.write("Token Value\tToken Type \n\n");
            while (s.hasNext()) {
                String str = s.nextLine();
                    if (!lexicalAnalyzer.analyzeLine(str, fw)) {
                        fw.write(" ERROR");
                }

            }
            fw.close();
        } catch (Exception ex) {
           
        }
    }

}
