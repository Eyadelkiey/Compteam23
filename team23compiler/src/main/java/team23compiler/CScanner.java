package team23compiler;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class CScanner {
	private List<String> words = Arrays.asList("int", "char", "float","double",  "if","then", "else","while", "for","return","auto","break","case","const","continue","default","do","enum","goto","extern","long","short","register","signed","volatile","void","switch","static","struct","sizeof","typedef","union","unsigned","boolean");
	// private SymbolTable symbolTable; // SymbolTable object declared here to allow the Scanner to interact with the symbol table

    /*
	public CScanner(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }
	*/
	

	
	

    //handling Comments and Semicolon
    boolean analyzeLine(String str, FileWriter fw) throws IOException {

        //identify the comments section
        int startOfSingleLineComment = str.indexOf("//");
		int startOfPreprocessor = str.indexOf("#");
        int startOfMultiLineComment = str.indexOf("/*");
        int endOfMultiLineComment = str.indexOf("*/");
        int semiColonIndex = str.indexOf(';');

        // Identify comments --> str represenets everything without the comment
          if (startOfSingleLineComment != -1) { // single comment is found
          // Single-line comment found, str is the context without the comment
              str = str.substring(0, startOfSingleLineComment); //removes the remaining line after //
         } else if (startOfPreprocessor != -1) { // preprocessor   is found and we will treated it as a comment
			// Single-line comment found, str is the context without the comment
				str = str.substring(0, startOfPreprocessor); //removes the remaining line after //
		   } else if (startOfMultiLineComment != -1 && endOfMultiLineComment != -1) {
               // Multi-line comment found, str is the context without the comment
              str = str.substring(0, startOfMultiLineComment) + str.substring(endOfMultiLineComment + 2); //+2 is used to skip the */ .. str.substring(endOfMultiLineComment + 2) is used to get the string after */
         }
             // Handle semicolon
             if (semiColonIndex != -1) { //Semicolon Handling: If a semicolon is found, replaces it with a space.


              str = str.substring(0, semiColonIndex) + ' ' + str.substring(semiColonIndex+1);
          }
            // Part 2: Tokenization
          //Tokenization: Iterates through the string and replaces certain characters with spaces to facilitate tokenization.
         // dh el goz2 el bn3ml feh el tokens 
		  String tokenizedStr = "";
		  boolean isvalidtoken = true ; //for handling erroneous tokens and we set it to true by deafult and change it into false if dosn't match any known token pattern

		  
          for (int j = 0; j < str.length(); j++) {
              char currentChar = str.charAt(j); //bade2 mn awel el string object bylf 3la 7rf 7rf feh
         
                // Add spaces around certain characters for tokenization
  				  if (currentChar == '>' && j < str.length() - 1 && str.charAt(j + 1) == '=') {
						// Handle ">=" as a single token
							tokenizedStr += " >= ";
							j++; // Increment j to skip the next character '='

					}  else if (currentChar == '>' && j < str.length() - 1 && str.charAt(j + 1) == '>') {
						// Handle ">>" as a single token
							tokenizedStr += " >> ";
							j++; // Increment j to skip the next character '>'
					} else if (currentChar == '<' && j < str.length() - 1 && str.charAt(j + 1) == '<') {
						// Handle ">>" as a single token
							tokenizedStr += " << ";
							j++; // Increment j to skip the next character '>'
					} else if (currentChar == '<' && j < str.length() - 1 && str.charAt(j + 1) == '=') {
						// Handle ">>" as a single token
							tokenizedStr += " <= ";
							j++; // Increment j to skip the next character '>'
					} else if (currentChar == '>' && j < str.length() - 1 && str.charAt(j + 1) == '>') {
						// Handle ">>" as a single token
							tokenizedStr += " >> ";
							j++; // Increment j to skip the next character '>'
					} else if (currentChar == '>' && j < str.length() - 1 && str.charAt(j + 1) == '>') {
						// Handle ">>" as a single token
							tokenizedStr += " >> ";
							j++; // Increment j to skip the next character '>'
					}else if (currentChar == '!' && j < str.length() - 1 && str.charAt(j + 1) == '=') {
						// Handle "==" as a single token
							tokenizedStr += " != ";
							j++; // Increment j to skip the next character '='
					}else if (currentChar == '&' && j < str.length() - 1 && str.charAt(j + 1) == '&') {
						// Handle "==" as a single token
							tokenizedStr += " && ";
							j++; // Increment j to skip the next character '='
					}else if (currentChar == '|' && j < str.length() - 1 && str.charAt(j + 1) == '|') {
						// Handle "==" as a single token
							tokenizedStr += " || ";
							j++; // Increment j to skip the next character '='
					}else if (currentChar == '=' && j < str.length() - 1 && str.charAt(j + 1) == '=') {
						// Handle "==" as a single token
							tokenizedStr += " == ";
							j++; // Increment j to skip the next character '='
					} else if (currentChar == '+' && j < str.length() - 1 && str.charAt(j + 1) == '=') {
						// Handle "+=" as a single token
							tokenizedStr += " += ";
							j++; // Increment j to skip the next character '='
					}else if (currentChar == '+' && j < str.length() - 1 && str.charAt(j + 1) == '+') {
						// Handle "+=" as a single token
							tokenizedStr += " ++ ";
							j++; // Increment j to skip the next character '+' 
					}else if (currentChar == '-' && j < str.length() - 1 && str.charAt(j + 1) == '=') {
						// Handle "-=" as a single token
						tokenizedStr += " -= ";
						j++; // Increment j to skip the next character '='
					}  
					//handling [ only if the char after it is space as if it isnot then it will be a part of array lw m3mlnash keda hya5od kol el [ 3la enohm openbracket 7ta lw homa f array
					else if (currentChar == '[' && j < str.length() - 1 && str.charAt(j + 1) == ' ') {
						// Handle "-=" as a single token
						tokenizedStr += " [ ";
						j++; // Increment j to skip the next character ' '}
					}
					//handling ] only if the char before it is space as if it isnot then it will be a part of array lw m3mlnash keda hya5od kol el ] 3la enohm openbracket 7ta lw homa f array
					else if (currentChar == ' ' && j < str.length() - 1 && str.charAt(j + 1) == ']') {
						// Handle "-=" as a single token
						tokenizedStr += " ] ";
						j++; // Increment j to skip the next character ' '}
					}
					//handling * only if the char after it is space as if it isnot then it will be a part of pointer lw m3mlnash keda hya5od kol el * 3la enohm multiply 7ta lw homa f pointer
					else if (currentChar == '*' && j < str.length() - 1 && str.charAt(j + 1) == ' ') {
						// Handle "-=" as a single token
						tokenizedStr += " * ";
						j++; // Increment j to skip the next character ' '}
					}
					else if(currentChar == '(' || currentChar == ')' | currentChar == '|'|| currentChar == '&'|| 
					currentChar == '<' ||currentChar == '=' || currentChar == '+' || currentChar == '-' ||
					 currentChar == '/' || currentChar == ';' || currentChar == '!'|| 
					currentChar == ',' || currentChar == '{' || currentChar == '}'||currentChar == '^'  ) {
					tokenizedStr += " " + currentChar + " ";
					}else {
						tokenizedStr += currentChar; //enta 3amlha else keda 3shan t handle kol el enum constants wel pointers wel klam dh
					}
		  }

        System.out.println(tokenizedStr + '\n');
        str = tokenizedStr;
		/*  Will Looping over each element in the Tokenized String and add identifiers to symbol Table 
		for (String token : tokenizedStr) {
            if (symbolTable.containsIdentifier(token)) {
                // Identifier already exists in the symbol table
                String dataType = symbolTable.getDataType(token);
                // Process the identifier...
                // Example: Write it to a file with its data type
                fw.write(token + "\t" + dataType + "\n");
            } else {
                // Identifier does not exist in the symbol table
                // Add it to the symbol table with a default data type (e.g., "UNKNOWN")
                symbolTable.addIdentifier(token, "UNKNOWN");
            }
		}
		*/

         // Part 3: Writing Tokens
         //Splits the tokenized string into an array and iterates through it to identify and write tokens to the FileWriter.
		 String[] strParts1 = {}; //this array will store the tokenized parts of the input string.
		 if (!tokenizedStr.isEmpty()) { //checks if the tokenizedStr is not empty, then splits it into parts based on space characters and assigns the result to the strParts1 array.
			 strParts1 = tokenizedStr.split(" ");
		 }
		// Part 4: Identify and write tokens
        int i = 0;
        while(i < strParts1.length) { //hnloop 3leh kolo l7d ama y5ls

        	//if(isvalidtoken(tokenizedStr)){
        	if (strParts1[i].isEmpty()) { //lw hwa fady hy7rk el pointer lel b3do
        		i++;
        		continue;
            }
			//for handling functions 
			// Handle function declarations and calls in the C scanner
 	//	if (strParts1[i].equals("int") || strParts1[i].equals("void")  /* Add other return types */) {
   			 // Check if the following tokens indicate a function declaration
  	  //if (i < strParts1.length - 2 && strParts1[i+1].matches("^[_a-zA-Z][_a-zA-Z0-9]*$") && strParts1[i+2].equals("(")) {
       		 // Tokenize as FUNCTION_DECLARATION
       	//		 String functionName = strParts1[i+1]; // Extract the function name
      	//		  fw.write(strParts1[i] + " " + functionName + "\t,\t" + "FUNCTION_DECLARATION" + "\n");
      	//			  i += 2; // Skip the function name and '(' for the next iteration
    //}
//}
				// Handle function calls
		//	else if (strParts1[i].matches("^[_a-zA-Z][_a-zA-Z0-9]*$") && i < strParts1.length - 1 && strParts1[i+1].equals("(")) {
   			 // Tokenize as FUNCTION_CALL
   			// String functionName = strParts1[i]; // Extract the function name
  			  //fw.write(functionName + "\t,\t" + "FUNCTION_CALL" + "\n");
				//}

			//handling the keywords bshof lw 7aga men el keywords mawgoda hya5odha heya
        else  if (words.contains(strParts1[i])) {
            		fw.write(strParts1[i] + "\t,\t" +"Reserved Word {" +strParts1[i] +"}"+ "\n");
            		i++;
            		continue;
            }
            //operators identification and writing
		else if(strParts1[i].equals("=") && isvalidtoken) {
        		fw.write(strParts1[i] + "\t,\t" + "EQUAL" + "\n");
			}else if(strParts1[i].equals("==")&& isvalidtoken)
        	{
        		fw.write(strParts1[i] + "\t,\t" + "ISEQUAL" + "\n");
        	}else if(strParts1[i].equals("<")&& isvalidtoken)
        	{
        		fw.write(strParts1[i] + "\t,\t" + "LESSTHAN" + "\n");
        	}else if(strParts1[i].equals(">")&& isvalidtoken)
        	{
        		fw.write(strParts1[i] + "\t,\t" + "GREATERTHAN" + "\n");
        	} else if(strParts1[i].equals("<=")&& isvalidtoken)
        	{
        		fw.write(strParts1[i] + "\t,\t" + "LESSTHAN_OR_EQUAL" + "\n");
        	} else if(strParts1[i].equals(">=")&& isvalidtoken)
        	{
        		fw.write(strParts1[i] + "\t,\t" + "GREATERTHAN_OR_EQUAL" + "\n");
        	} else if(strParts1[i].equals("!=")&& isvalidtoken)
        	{
        		fw.write(strParts1[i] + "\t,\t" + "NOTEQUAL" + "\n");
        	}  else if(strParts1[i].equals("+")&& isvalidtoken)
        	{
        		fw.write(strParts1[i] + "\t,\t" + "PLUS" +"\n");
        	}else if(strParts1[i].equals("++")&& isvalidtoken)
        	{
        		fw.write(strParts1[i] + "\t,\t" + "increment" +"\n");
        	}else if(strParts1[i].equals("-")&& isvalidtoken) {
        		fw.write(strParts1[i] + "\t,\t" + "MINUS" + "\n");
        	}else if(strParts1[i].equals("*")&& isvalidtoken) {
        		fw.write(strParts1[i] + "\t,\t" + "MULT" + "\n");	
        	}else if(strParts1[i].equals("/")&& isvalidtoken) {
        		fw.write(strParts1[i] + "\t,\t" + "DIV" + "\n");
        	}else if(strParts1[i].equals("&&")&& isvalidtoken) {
        		fw.write(strParts1[i] + "\t,\t" + "LOGICALAND" + "\n");
        	}else if(strParts1[i].equals("||")&& isvalidtoken) {
        		fw.write(strParts1[i] + "\t,\t" + "LOGICALOR" + "\n");
        	}else if(strParts1[i].equals("!")&& isvalidtoken) {
        		fw.write(strParts1[i] + "\t,\t" + "LOGICALNOT" + "\n");
        	}else if(strParts1[i].equals("+=")&& isvalidtoken) {
        		fw.write(strParts1[i] + "\t,\t" + "ADDTOPREVIOUS" + "\n");
        	}else if(strParts1[i].equals("-=")&& isvalidtoken) {
        		fw.write(strParts1[i] + "\t,\t" + "SUBTRACTFROMPREVIOUS" + "\n");
        	}else if(strParts1[i].equals("&")&& isvalidtoken) {
        		fw.write(strParts1[i] + "\t,\t" + "BITWISEAND" + "\n");
        	}else if(strParts1[i].equals("|")&& isvalidtoken) {
        		fw.write(strParts1[i] + "\t,\t" + "BITWISEOR" + "\n");
        	}
			else if(strParts1[i].equals("^")&& isvalidtoken) {
        		fw.write(strParts1[i] + "\t,\t" + "BITWISEXOR" + "\n");
        	}else if(strParts1[i].equals(">>")&& isvalidtoken) {
        		fw.write(strParts1[i] + "\t,\t" + "RIGHTSHIFT" + "\n");
        	}
			else if(strParts1[i].equals("<<")&& isvalidtoken) {
        		fw.write(strParts1[i] + "\t,\t" + "LEFTSHIFT" + "\n");
        	}

			//Seperators identification and writing
           else  if(strParts1[i].equals(";")&& isvalidtoken){
        		fw.write(strParts1[i] + "\t,\t" + "SEMICOLON" +"\n");
        	}else if(strParts1[i].equals("(")&& isvalidtoken) {
        		fw.write(strParts1[i] + "\t,\t" + "OPENBRACKET" + "\n");
        	}else if(strParts1[i].equals(")")&& isvalidtoken) {
        		fw.write(strParts1[i] + "\t,\t" + "CLOSEDBRACKET" + "\n");
        	}
			else if(strParts1[i].equals(",")&& isvalidtoken) {
        		fw.write(strParts1[i] + "\t,\t" + "COMMA" + "\n");
        	}else if(strParts1[i].equals("}")&& isvalidtoken) {
        		fw.write(strParts1[i] + "\t,\t" + "CLOSEDCURLYBRACKET" + "\n");
        	}else if(strParts1[i].equals("{")&& isvalidtoken) {
        		fw.write(strParts1[i] + "\t,\t" + "OPENCURLYBRACKET" + "\n");
        	}else if(strParts1[i].equals("[")&& isvalidtoken) {
				fw.write(strParts1[i] + "\t,\t" + "OPENSQUAREBTACKET" + "\n");
			}else if(strParts1[i].equals("")&& isvalidtoken) {
				fw.write(strParts1[i] + "\t,\t" + "CLOSEDSQUAREBTACKET" + "\n");
		}
            
           		//for function identifiers
		else if (strParts1[i].matches("[a-zA-Z_][a-zA-Z0-9_]*\\(.*\\)")&& isvalidtoken) {
    				fw.write(strParts1[i] + "\t,\t" + "FUNCTION_IDENTIFIER" + "\n");
				}
				//for identifying Character literals as /n
				else if (strParts1[i].matches("^'[a-zA-Z0-9]'$")&& isvalidtoken) {
					fw.write(strParts1[i] + "\t,\t" + "CHARACTER_LITERAL" + "\n");
				} //for identifying String literals as "hello"
				else if (strParts1[i].matches("^\".*\"$")&& isvalidtoken) {
					fw.write(strParts1[i] + "\t,\t" + "STRING_LITERAL" + "\n");
				}//for identifying Pointers
				else if (strParts1[i].matches("^\\*[_a-zA-Z][a-zA-Z0-9_]*$")&& isvalidtoken) {
					fw.write(strParts1[i] + "\t,\t" + "POINTER" + "\n");
				}//for identifying arrays
				 else if (strParts1[i].matches("^_?[a-zA-Z][a-zA-Z0-9_]*\\[\\d*\\]$")&& isvalidtoken) {
					fw.write(strParts1[i] + "\t,\t" + "IDENTIFIER (Array)" + "\n");
				}//for enum comstantselse 
				 else if ((strParts1[i].matches("^[_a-zA-Z][_a-zA-Z0-9]*$") && ((strParts1[i-1].equals("{") || strParts1[i-1].equals(",")) && (strParts1[i+1].equals("}") || strParts1[i+1].equals(","))))&& isvalidtoken ){
    				// Tokenize as ENUM_CONSTANT
   				 fw.write(strParts1[i] + "\t,\t" + "ENUM_CONSTANT" + "\n");
				}//for normal identfiers 
				else if (strParts1[i].matches("^_?[a-zA-Z][a-zA-Z0-9_]*$")&& isvalidtoken) {
					fw.write(strParts1[i] + "\t,\t" + "IDENTIFIER" +" {"+strParts1[i]+"}" + "\n");
					//Token token = new Token("IDENTIFIER", strParts1[i], 0, 123);
					//tokenizedString.add(token);
					//System.out.println(tokenizedString);

				} 
				
			
			//for numbers : 
		/* 	if (strParts1[i].matches("^[0-9]+$")) {
                fw.write(strParts1[i] + "\t,\t" +"NUMBER" + "\n");
            }*/
	 else if (strParts1[i].matches("^[-+]?\\d+$")&& isvalidtoken) {
				fw.write(strParts1[i] + "\t,\t" + "INTEGER" + "\n");
			} else if (strParts1[i].matches("^[-+]?\\d+\\.\\d+$")&& isvalidtoken) {
				fw.write(strParts1[i] + "\t,\t" + "FLOAT" + "\n");
			} else if (strParts1[i].matches("^[-+]?\\d+(\\.\\d+)?(e[+-]?\\d+)?$")&& isvalidtoken) {
				fw.write(strParts1[i] + "\t,\t" + "DOUBLE" + "\n");
			}
			//for handling erroneous token 
	
		else { 
				try {
				// handling Erroneous token --> lw 3ndk 
				fw.write(strParts1[i]+ "\t,\t" + "Erroneous token" + "\n");
				
			} catch (IOException e) {
				e.printStackTrace();
			}}
		
			
			
			
            
            i++;
		}
        return true;
		  
}
    public boolean isComment(String in) {
        if (in.charAt(0) == '{') {
            return true;
        }
        return false;
    }
}
