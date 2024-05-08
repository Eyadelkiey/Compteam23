package team23compiler;

import team23compiler.*;


import javax.swing.*;


//import scala.xml.Null;


import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.StringTokenizer;
import java.io.BufferedReader;



public class ParserSwingGUI extends JFrame {
	private Treenode Tree;
	public static JTextArea consoleTextArea;
    private String filename;
    private static boolean errorflag = false;
    //private SyntaxTreeViewer syntaxTreeViewer;
    BufferedReader reader;
    
    public ParserSwingGUI() {
        super("Parser GUI");

        consoleTextArea = new JTextArea();
        consoleTextArea.setEditable(false);
        Font f = new Font( Font.SANS_SERIF , Font.BOLD, 13 );
        consoleTextArea.setFont(f);

        JButton chooseFileButton = new JButton("Choose Input File");
        chooseFileButton.addActionListener(e -> chooseFile());

        JButton parseButton = new JButton("Parse");
        parseButton.addActionListener(e -> parse());

        JButton showTreeButton = new JButton("Show Syntax Tree");
        showTreeButton.addActionListener(e -> showSyntaxTree());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(chooseFileButton);
        buttonPanel.add(parseButton);
        buttonPanel.add(showTreeButton);

        //syntaxTreeViewer = new SyntaxTreeViewer();

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(consoleTextArea), BorderLayout.CENTER);

        getContentPane().add(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void chooseFile() {
    	errorflag = false;
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
        	Token.tokens.clear();
            Token.setI(0);
            Tree = null;
            File selectedFile = fileChooser.getSelectedFile();
            filename = selectedFile.getAbsolutePath();
            consoleTextArea.append("Selected file: " + filename + "\n");
        }
    }

    private void parse() {
        if (filename == null) {
            consoleTextArea.append("Please choose a file first.\n");
            return;
        }
        
        try {
        	errorflag = false;
        	Token.tokens.clear();
            Token.setI(0);
            reader = Files.newBufferedReader(new File(filename).toPath());
            
            String line = reader.readLine();
          

            while(line != null) {
                StringTokenizer tokenizer = new StringTokenizer(line, ",");
                if (tokenizer.hasMoreTokens()) {
                    String currentToken = tokenizer.nextToken().trim();
                    String currentTokenType = tokenizer.nextToken().trim();
                    int startOfcomment = currentTokenType.indexOf("{");
                    if (startOfcomment != -1) { // single comment is found
                        // Single-line comment found, str is the context without the comment
                    	currentTokenType = currentTokenType.substring(0, startOfcomment); //removes the remaining line after //
                    }
                    Token.addToken(currentTokenType.toLowerCase(),currentToken);     
                    Token.addOrUpdateSymbolEntry(currentTokenType.toLowerCase(), currentToken);
                    System.out.println("Current Token: " + currentToken); // Add this line for debugging

               }

              
              line = reader.readLine();
        }
            Token.printTokens();
           Token.printsymboltable(); //for debugging 7tena hena mesh fo2 3shan y print mra wa7da mesh y print kol mra bn2ra new line
            Parser parser = new Parser();
            Tree = parser.program();
            if(!errorflag)
            {
            	consoleTextArea.append("\n Parsing is Done successful!\n");            	
            }
            reader.close();

            // Set the syntax tree for visualization
            //syntaxTreeViewer.setSyntaxTree(parser.TinyParser.getSyntaxTree());
        } catch (IOException e) {
            consoleTextArea.append("Error reading the file: " + e.getMessage() + "\n");
        }
    }

    private void showSyntaxTree() {
        //syntaxTreeViewer.showSyntaxTree();
    	Parser parser;
		try {
			if(Tree != null )
			{
				if(!errorflag) {
					parser = new Parser();
					parser.drawTree(Tree);									
				}
				else{
					consoleTextArea.append("Error in generating Syntax Tree: Please correct the wrong tokens first (^_^).\n");
				}
			}
			else {
				consoleTextArea.append("Error in generating Syntax Tree: Please parse the file first (^_^).\n");
			}
				
		} catch (IOException e) {
			consoleTextArea.append("Error in generating Syntax Tree: " + e.getMessage() + " (^_^)\n");
		}
    }
    
    public static void error(String ErrorMessage){
    	errorflag = true;
    	consoleTextArea.append(ErrorMessage);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(ParserSwingGUI::new);
    }
}
