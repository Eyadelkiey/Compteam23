package team23compiler;

import java.util.ArrayList;

public class Token {
    private String type;
    private String value; // value = name = tag
    private float numericalvalue; // // The numerical value of the symbol (for variables)
    
    private static int i = 0;
    
    //el arraylist dh gowa objects mesh string 3shan keda kol object feha el value w feha el type bta3ha connected b b3d l2n asln dh array of objects
    public static ArrayList<Token> tokens = new ArrayList<Token>(); //dh hyt7t feh kol el tokens
    
    // dh el symbol table w kol object feh hyb2 symbol entry
    public static ArrayList<Token> symboltable = new ArrayList<Token>();

   
    public static void printsymboltable() {
        System.out.println("\n printsymboltablefunction");
        for (int i = 0; i < symboltable.size(); i++) {
            Token token = symboltable.get(i);
            if (token.type.equals("identifier"))  {
            System.out.println("Index: " + i + ", Token Name: " + token.value +" , Token Type: " + token.type + " , Token Numerical Value: " + token.numericalvalue);
            }else {
                System.out.println("Index: " + i + ", Token Name: " + token.value +" , Token Type: " + token.type + " , Token Numerical Value: Null " );

           }
        }
    }
    

    public static void addOrUpdateSymbolEntry(String type, String value) {
        boolean found = false; // Flag to track if the token was found in the symbol table

        // Iterate through the tokens table
        for (int i = 0; i < tokens.size(); i++) {
            Token entry = tokens.get(i); // Get the current token
            found = false;
            float tempValue; // Initialize tempValue for each symbol entry bs mesh hn5leha b zero ela lma y2bl numerical value

            
            // Check if the token is an identifier followed by :=
            if (entry.getType().equals("identifier") && i + 1 < tokens.size() && tokens.get(i + 1).getValue().equals("=")) {
                System.out.println("This Variable " + entry.getValue() + " is not found before so it will be updated.");
              
                tempValue = 0; // Initialize tempValue only if a new numeric value is encountered

                // Check if there is a numeric token after :=
               // boolean isFirstNumericValue = true; // Flag to indicate if the first numeric value has been found
                System.out.println("hello1-->"+ tempValue);

                for (int j = i + 2; j < tokens.size(); j++) { // Start from the token after ":="
                    Token numberToken = tokens.get(j);
                    System.out.println("hello from"+ entry.getValue() );
                    System.out.println(numberToken);
                    System.out.println("hello2 -->"+ tempValue);

                    // Check if the token is a numerical value
                    if (numberToken.getType().equals("number")) {
                        float numericValue = Float.parseFloat(numberToken.getValue());
                        
                        //if (isFirstNumericValue) {
                            // If it's the first numeric value encountered, assign it to tempValue
                            System.out.println("hello from"+ entry.getValue() );
                            System.out.println("hello3-->"+ tempValue);

                            tempValue = numericValue;
                            System.out.println("hello4 --> "+ tempValue);

                          //  isFirstNumericValue = false;
                      //  }
                            //else {
                            // Apply the operator to the tempValue based on the next token
                            Token operatorToken = tokens.get(j - 1); // Get the operator token
                            switch (operatorToken.getValue()) {
                                case "+":
                                    tempValue += numericValue;
                                    break;
                                case "-":
                                    tempValue -= numericValue;
                                    break;
                                case "*":
                                    tempValue *= numericValue;
                                    break;
                                case "/":
                                    tempValue /= numericValue;
                                    break;
                                default:
                                    // If no valid operator, do nothing
                                    break;
                          //  }

                        }

                    }
                   

                }
             // Search for the token in the symbol table
                for (int k = 0; k < symboltable.size(); k++) {
                    Token existingToken = symboltable.get(k);
                    System.out.println("eYAD"); //for debugging
                    if (existingToken.getValue().equals(entry.getValue())) {
                        System.out.println("Eyad"); //for debugging

                        // Token found, update its numerical value
                        existingToken.setNumericalValue(tempValue);
                        found = true;
                        System.out.println("hello5--->"+ tempValue);

                        tempValue = 0;
                        break;
                    }
                }


                if (!found) {

                // Create a new token with the updated numerical value
                Token tempToken = new Token();
                tempToken.type = entry.type;
                tempToken.value = entry.value;
                tempToken.setNumericalValue(tempValue);
                System.out.println(tempToken.getNumericalValue());
                
                // Add the new token to the symbol table
                symboltable.add(tempToken);
                System.out.println("hello6-->"+ tempValue);

                tempValue = 0;
                
                
                }
            }

        }

    }


    					/*else {
                            // Apply the operator to the temp value based on the next token
                            Token operatorToken = symboltable.get(j - 1); // Get the operator token
                            switch (operatorToken.getValue()) {
                                case "+":
                                    tempvalue += numericValue;
                                    break;
                                case "-":
                                    tempvalue -= numericValue;
                                    break;
                                case "*":
                                    tempvalue *= numericValue;
                                    break;
                                case "/":
                                    tempvalue /= numericValue;
                                    break;
                                default:
                                    // If no valid operator, do nothing
                                    break;
                            }
                        }
                    }
                }
    	        Token tempToken = new Token();
                tempToken.type = entry.type;
                tempToken.value = entry.value;
                //tempToken.setNumericalValue(Float.parseFloat(numberToken.getValue()));
                //System.out.println(entry.getNumericalValue());
    	        tempToken.setNumericalValue(tempvalue);
                break; // Exit the loop after updating the entry
            }
        }
    }*/
    	      /* // if (i + 2 < tokens.size()) {  
    	         //   Token numberToken = tokens.get(i + 2);
    	           // System.out.println(numberToken);
    	            //System.out.println(numberToken.getType());
    	            if (numberToken.getType().equals("number")) {
    	                System.out.println("i entered here");
    	                System.out.println(numberToken.getValue());
    	                // Parse the numerical value and assign it to the entry
    	                Token tempToken = new Token();
    	                tempToken.type = entry.type;
    	                tempToken.value = entry.value;
    	                tempToken.setNumericalValue(Float.parseFloat(numberToken.getValue()));
    	                System.out.println(entry.getNumericalValue());
    	                symboltable.add(tempToken);
    	            }
    	
    }
    	    
    	    }
    	}
    }
*/
    // Method to print Tokens ArrayList to console for testing
    public static void printTokens() {
        System.out.println("\n printtokenfunctionstartshere\n\n");
        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);
            if (token.type.equals("identifier"))  {
            System.out.println("Index: " + i + ", Token Name: " + token.value +" , Token Type: " + token.type + " , Token Numerical Value: " + token.numericalvalue);
            }else {
                System.out.println("Index: " + i + ", Token Name: " + token.value +" , Token Type: " + token.type + " , Token Numerical Value: Null " );

           }
        }
    }
    
    //hnst5dm el addtoken 3shan ndef kol token bngebha men el file w ndefha fel arraylist of tokens el fo2 dh
    public static void addToken(String type, String value){
        Token tempToken = new Token();
        
        tempToken.type = type;
        tempToken.value = value;
        tokens.add(tempToken);
    }

    
    @Override
    public String toString(){
        return this.value + " : " + this.type;
    }
    
    // byt7rk fel arraylist ygeblk el token el b3dha
    public static Token getNextToken(){
        if( i < tokens.size() ){
            return tokens.get(i);
        }
        return null;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setNumericalValue(float numericalvalue) {
        this.numericalvalue = numericalvalue;
    }
    public float getNumericalValue() {
        return numericalvalue;
    }


    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public static int getI() {
        return i;
    }

    public static void setI(int i) {
        Token.i = i;
    }

    public static void incI() {
        i++;
    }

    public static void decI() {
        i--;
    }
}

