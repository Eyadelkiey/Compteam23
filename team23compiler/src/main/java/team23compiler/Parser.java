package team23compiler;



import team23compiler.*;

//These lines import classes IOException and PrintWriter from the java.io package. 
//These classes are used for handling input-output operations, such as writing to files.
import java.io.IOException;
import java.io.IOException;
import java.io.PrintWriter;

//These lines import classes from the GraphStream library, which is used for graph visualization. 
//It imports graph-related classes, such as Graph, Node, and Viewer.
//import org.abego.treelayout.Configuration;
//import org.abego.treelayout.TreeLayout;
//import org.abego.treelayout.util.DefaultConfiguration;
//import org.graphstream.graph.Node;
import org.graphstream.ui.view.Viewer;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
//import org.graphstream.ui.j2dviewer.J2DGraphRenderer;


public class Parser {
    //e7na shelna el drawing function l7d m n test el bta3 dh
    PrintWriter pw;//pw is a PrintWriter object used for writing output to a file
    Token token = Token.getNextToken(); //token is a Token object representing the current token being processed.
    Graph graph = new SingleGraph("ParseTree"); //graph is a Graph object used for representing the syntax tree.
    static int x,y,z = 0 ;//integers  intialized to 0
    
    
    
    //This is the constructor of the TinyParser class.
    //It initializes the PrintWriter pw to write output to a file named "parser_output.txt".
    // After initialization, it immediately closes the PrintWriter.
    Parser() throws IOException {//el constructor w dh awel 7aga bttndh
        pw = new PrintWriter("parser_output.txt");
        //init();
        pw.close();
        
    }
    //This method init() initializes the parsing process by calling the program() method to start parsing the program 
    //and then visualizes the resulting syntax tree using the drawTree() method.
   //Where should this be called? in the Constructor of the  Parser
    public void init(){ ////hyndha el constructor w heya htndh el program function w deh bdayet el Parsing w lma y5ls hy3ml draw lel tree
        Treenode Tree = program(); //// el variable esmo tree w dh el hytrsm w hyb2 no3o Treenode
    
        drawTree(Tree);

    }
    
  //This method program() represents the grammar rule for the program.
    //It prints a message indicating the program start and 
    //then calls stmt_sequence() to parse the sequence of statements in the program.
    public Treenode program(){
        ParserSwingGUI.consoleTextArea.append("Program is found\n");
        
        Treenode nodeProgram = stmt_sequence(); //calls the stmt_sequence() method to parse the sequence of statements in the program.
        return nodeProgram;
    }
    
    // This method stmt_sequence() represents the grammar rule for a sequence of statements.
    // It parses multiple statements until it encounters a token that indicates the end of the sequence (end, else, or until).
    // It calls statement() to parse individual statements and connects them as siblings in the syntax tree.
    
    //deh function mohema gedn gedn btost5dm 3shan lw 3ndk kza statment mortbten bb3d w 3aiz t3mlohm parsing m3 b3d zy msln lw 3ndk goml between {  }
    private Treenode stmt_sequence(){
        ParserSwingGUI.consoleTextArea.append("Statement Sequence is found\n");//This line prints a msg fel output file w dh byb2 tany statment bnswlha b3d el "Program is found"
        
        Treenode nodeStmtSeq = statement();//enta keda 3mlt awel node hena btndh el statment method w bt3ml save lel output feh Treenode ( deh node fel shagra e7na 3arfen en kol object mn Treenode heya node fel Tree)
        Treenode nodeSib = nodeStmtSeq ;//enta n2lt el node el fo2 l node esmha node sibling tb 3mlt keda leh 3shan a3ml link subsequent statements as siblings.
        
        while( token != null && ";".equals(token.getValue()) ){ //This while loop iterates as long as there are more tokens to parse, and the current token is a semicolon (;). This indicates multiple statements in a sequence.
            match(";"); //Matches the semicolon token.
            token = Token.getNextToken(); //Advances to the next token.
            Treenode temp = statement(); // Parses the next statement using the statement() method.
            if( nodeSib != null){ //Links the newly parsed statement as a sibling to the previous one using the setSibling() method of the nodeSib.
            nodeSib.setSibling(temp);
            nodeSib = temp;
            }
        }
        
        if (token != null && !("else".equals(token.getValue()))) { //This if statement checks if the current token is not null and its value is not equal to the string "else".
                               
                    
                                match(";");
        }
        
        return nodeStmtSeq;
    }
    
    private Treenode statement(){
        ParserSwingGUI.consoleTextArea.append("Statement is found\n");
        
        if (token != null) {
            switch (token.getType()) {
                case "reservedword":
                    switch (token.getValue()) {
                        case "if":
                            return if_stmt();
                        case "while":
                            return while_stmt();
                        case "switch":
                            // Handle switch statement
                            break;
                        case "for":
                            // Handle for statement
                            break;
                        case "return":
                            // Handle return statement
                            break;
                        case "do_while":
                            // Handle do-while statement
                            break;
                        case "int":
                            if (true /* condition to check if it's an assignment statement */) {
                                return declaration_stmt();
                            } else {
                                return function_definition();
                            }
                            // No need for break here
                        default:
                            ParserSwingGUI.error("Unexpected Token:" + token.getValue() + "\tLocation (Token No.): " + (Token.getI() + 1) + "\n");
                    }
                    break;
                case "identifier":
                    if (true /* condition to check if it's an assignment statement */) {
                        return assign_stmt();
                    } else {
                        return function_call();
                    }
                    // No need for break here
                default:
                    ParserSwingGUI.error("Unexpected Token:" + token.getValue() + "\tLocation (Token No.): " + (Token.getI() + 1) + "\n");
            }
        }
        ParserSwingGUI.consoleTextArea.append("Statement is Done\n");
        return null;
    }
    
    //This method if_stmt() represents the grammar rule for an if statement.
    //It constructs a syntax tree node for the if statement and its components (test expression, then statement sequence, and optional else statement sequence).

    //if–stm --> if (exp) statement | if (exp) statement else statement
   
    private Treenode if_stmt(){
       // pw.println("If Statement is found"); 
        ParserSwingGUI.consoleTextArea.append("If Statement is found\n");     
        Treenode testChild = null;
        Treenode thenChild = null;
        Treenode elseChild = null;
        Treenode nodeIf = new Treenode("if");
        
        match("if");//matching the if keyword itself
        token = Token.getNextToken(); //advance to the next token
        match("("); // Match the opening parenthesis
        token = Token.getNextToken();
        
        testChild = exp();// Parse the condition expression
        if (testChild != null){       
            nodeIf.addchild(testChild);
            
            match(")"); // Match the closing parenthesis
            token = Token.getNextToken();  
            
            match("{"); //e7na 3arfen en el then hena byb2 between { } e7na hn3tbr delw2ty en lazm yb2 feh { mesh hna5od f e3tbrna el inline if delw2ty
            token = Token.getNextToken();  
            
            thenChild = stmt_sequence();// Parse the statements inside the "then" block
            nodeIf.addchild(thenChild);
            
            match("}"); //e7na 3arfen en el then hena byb2 between { } e7na hn3tbr delw2ty en lazm yb2 feh { mesh hna5od f e3tbrna el inline if delw2ty
            token = Token.getNextToken();  
            
            if(token != null && "else".equals(token.getValue())){
                match("else");
                token = Token.getNextToken();  
                elseChild = stmt_sequence(); //// Parse the statements inside the "else" block
                nodeIf.addchild(elseChild);
            }
            
            match("}");
            token = Token.getNextToken();   
            return nodeIf;
        }
        ParserSwingGUI.consoleTextArea.append("There is a Problem with the IF Statment");     
        return null;
    }
    
    
    //while_stmt--> while (expression){ statement}
    private Treenode while_stmt(){
        pw.println("While Statement is found");
        Treenode nodeWhile = new Treenode("while");
        Treenode testChild = null; // deh hn7ot feha el expression el between ()
        Treenode bodyChild = null; //deh hn7ot feha el statment el between {} el htt3ad fel while loop

        match("while");
        token = Token.getNextToken();

        match("(");
        token = Token.getNextToken();
        
            testChild = exp();
            
            match(")");
            token = Token.getNextToken();

        if (testChild != null){
            nodeWhile.addchild(testChild);

            match("{");
            token = Token.getNextToken();

            bodyChild = stmt_sequence(); // Parse the statement sequence inside the while loop.

            nodeWhile.addchild(bodyChild);

            match("}");
            token = Token.getNextToken();
            return nodeWhile;
        }
    	ParserSwingGUI.error("Unexpected Token:" + token.getValue() + "\tLocation (Token No.): " + (Token.getI()+1) + "\n");
        return null;
    }
    
  //assign_stmt --> variable_name(identifier) = expression;

    private Treenode assign_stmt(){
        pw.println("Assign Statement is found"); 
        Treenode nodeAssign = new Treenode ("assign") ;
        Treenode temp = null;
        
        match("identifier"); // Check if the next token is an identifier (variable name).
        nodeAssign.setDesc("assign ( " + token.getValue() +" )" );
        token = Token.getNextToken();  
        
        match("="); // Check if the next token is an assignment operator.
        token = Token.getNextToken();  
        
        temp = exp(); // Parse the expression on the right-hand side of the assignment. and if it is not null  we will make it the child
        if (temp!= null){
            nodeAssign.addchild(temp);
            return nodeAssign;
        }
        
        return null;
    }
    
     
  //exp -> simple_exp (comparison_operator simple_exp)?

    private Treenode exp(){
        pw.println("Exp is found"); 
        
        Treenode nodeExp = simple_exp();
        Treenode nodeOperation ;
        Treenode temp1 ;
        Treenode temp2 ;
            //to be edited later again
            // Handle comparison operators: <, <=, >, >=, ==, !=
        if (token != null && (token.getValue().equals("<") || token.getValue().equals("<=") ||
        token.getValue().equals(">") || token.getValue().equals(">=") ||
        token.getValue().equals("==") || token.getValue().equals("!="))) {

            switch (token.getValue()){
                case"<":
                    match("<");
                    token = Token.getNextToken();  
                    nodeOperation = new Treenode("<");
                    temp1 = nodeExp;
                    nodeExp = nodeOperation;
                    temp2 = simple_exp();
                    nodeExp.addchild(temp1);
                    nodeExp.addchild(temp2);
                    break;
                
                case"<=":
                    match("<=");
                    token = Token.getNextToken();  
                    nodeOperation = new Treenode("<=");
                    temp1 = nodeExp;
                    nodeExp = nodeOperation;
                    temp2 = simple_exp();
                    nodeExp.addchild(temp1);
                    nodeExp.addchild(temp2);
                break;

                case">":
                match(">");
                token = Token.getNextToken();  
                nodeOperation = new Treenode(">");
                temp1 = nodeExp;
                nodeExp = nodeOperation;
                temp2 = simple_exp();
                nodeExp.addchild(temp1);
                nodeExp.addchild(temp2);
                break;

                 case">=":
                     match(">=");
                     token = Token.getNextToken();  
                     nodeOperation = new Treenode(">=");
                     temp1 = nodeExp;
                     nodeExp = nodeOperation;
                     temp2 = simple_exp();
                     nodeExp.addchild(temp1);
                     nodeExp.addchild(temp2);
                break;

                case"==":
                     match("==");
                     token = Token.getNextToken();  
                     nodeOperation = new Treenode("==");
                     temp1 = nodeExp;
                     nodeExp = nodeOperation;
                     temp2 = simple_exp();
                     nodeExp.addchild(temp1);
                     nodeExp.addchild(temp2);
                break;

                case"!=":
                     match("!=");
                     token = Token.getNextToken();  
                     nodeOperation = new Treenode("!=");
                     temp1 = nodeExp;
                     nodeExp = nodeOperation;
                     temp2 = simple_exp();
                     nodeExp.addchild(temp1);
                     nodeExp.addchild(temp2);
                break;

                default:
                	ParserSwingGUI.error("Unexpected Token:" + token.getValue() + "\tLocation (Token No.): " + (Token.getI()+1) + "\n");
            }
        }
        return nodeExp;
    }
    
private Treenode simple_exp(){
        
        Treenode nodeSimpleExp = term();
        Treenode nodeOperation ;
        Treenode temp1 ;
        Treenode temp2 ;
        
            // hy5osh hena lw feh + aw - bs
        while( token != null && ( "+".equals(token.getValue()) || "-".equals(token.getValue()) ) ){       
            switch (token.getValue()){
                case "+" : 
                    match("+"); // Match the addition operator.
                    token = Token.getNextToken(); 
                    nodeOperation = new Treenode("+");  // Create a node for addition.
                    temp1 = nodeSimpleExp;
                    nodeSimpleExp = nodeOperation;
                    temp2 = term();   // Parse another term.
                    nodeSimpleExp.addchild(temp1);
                    nodeSimpleExp.addchild(temp2);
                    break;
                    
                case "-" : 
                    match("-");  // Match the subtraction operator.
                    token = Token.getNextToken(); 
                    nodeOperation = new Treenode("-"); // Create a node for subtraction.
                    temp1 = nodeSimpleExp;
                    nodeSimpleExp = nodeOperation;
                    temp2 = term();   // Parse another term.
                    nodeSimpleExp.addchild(temp1);
                    nodeSimpleExp.addchild(temp2);
                    break;
                default:
                	ParserSwingGUI.error("Unexpected Token:" + token.getValue() + "\tLocation (Token No.): " + (Token.getI()+1) + "\n");
            }
        }
        return nodeSimpleExp;
    }
    
//This method term() represents the grammar rule for a term.
//It constructs a syntax tree node for the term and its components (factors and multiplication/division operations).
//It repeatedly parses factors and adds them as children to the term node, along with the corresponding multiplication or division operation nodes.
//deh bta3t el multiply wel division l2nohm higher priority than addition and subtraction
//term --> factor ('*' factor | '/' factor)*

private Treenode term(){
    
    Treenode nodeTerm = factor();  // Parse the first factor.
    Treenode nodeOperation ;
    Treenode temp1 ;
    Treenode temp2 ;
    
        // Parse multiplication and division operations.
    while( token != null && ( "*".equals(token.getValue()) || "/".equals(token.getValue()) ) ){            
        switch (token.getValue()){
            case "*" : 
                match("*");  // Match the multiplication operator.
                token = Token.getNextToken(); 
                nodeOperation = new Treenode("*");// Create a node for multiplication.
                temp1 = nodeTerm;
                nodeTerm = nodeOperation;
                //temp2 = term(); // Parse another factor
                temp2 = factor(); // Parse another factor, not a term.
                nodeTerm.addchild(temp1);
                nodeTerm.addchild(temp2);
                break;
            case "/" : 
                match("/"); // Match the division operator.
                token = Token.getNextToken(); 
                nodeOperation = new Treenode("/"); // Create a node for division.
                temp1 = nodeTerm;
                nodeTerm = nodeOperation;
                //temp2 = term();  // Parse another factor.
                temp2 = factor(); // Parse another factor, not a term.
                nodeTerm.addchild(temp1);
                nodeTerm.addchild(temp2);
                break;
            default:
            	ParserSwingGUI.error("Unexpected Token:" + token.getValue() + "\tLocation (Token No.): " + (Token.getI()+1) + "\n");
        }
    }
    return nodeTerm;
}
    
//This method factor() represents the grammar rule for a factor. 
//It constructs a syntax tree node for the factor and its components (parenthesized expression, number, or identifier).
//Depending on the token type, it constructs the corresponding syntax tree node and returns it.
// el factor hwa el simplest part f ay expression y3ne dh bdayet ay expression
//el factor dh leh 3 anwa3 :
/*
1-Number: A numerical value, such as integers or floating-point numbers. For example, 5, 3.14, -10.
2-Identifier: A variable name that represents a value. It could be a single variable or a more complex expression represented by a variable. For example, x, total, myVariable.
3-Parenthesized Expression: An expression enclosed within parentheses. It can be any valid arithmetic expression, including nested expressions. For example, (3 + x), (2 * (x - 5)), ((a + b) / c). */

//factor --> '(' exp ')' | number | identifier 
private Treenode factor(){
    Treenode nodeFactor = null;
    
        // Check if the current token is a parenthesis, number, or identifier.
    while( token != null && !( "openbracket".equals(token.getType()) || "number".equals(token.getType()) || "identifier".equals(token.getType()) ) ){
        match("'(' or number or identifier"); token = Token.getNextToken();
    }
        // If the current token matches one of the expected types.
    if( token != null ){
        switch (token.getType()){
            case "openbracket" :  // If the token is an open parenthesis.
                match("("); // Match the open parenthesis.
                token = Token.getNextToken(); 
                nodeFactor = exp(); //Parse the expression within parentheses.
                match(")"); // Match the closing parenthesis.
                token = Token.getNextToken(); 
                return nodeFactor;
                
            case "number" : // If the token is a number.
                match("number");  // Match the number.
                nodeFactor = new Treenode (token.getValue()); // Create a node for the number.
                token = Token.getNextToken();
                return nodeFactor;
                
            case "identifier" : //  If the token is an identifier.
                match("identifier"); // Match the identifier.
                nodeFactor = new Treenode (token.getValue()); // Create a node for the identifier.
                token = Token.getNextToken(); 
                return nodeFactor;
            default:
            	ParserSwingGUI.error("Unexpected Token:" + token.getValue() + "\tLocation (Token No.): " + (Token.getI()+1) + "\n");
        }
    }
    return null;
}
//return_stmt --> return exp
private Treenode return_stmt() {
    pw.println("Return Statement is found");
    Treenode nodeReturn = new Treenode("return"); //We create a new syntax tree node nodeReturn with the label "return".
    
    match("return");
    token = Token.getNextToken();
    
    Treenode expressionNode = exp(); //deh el ht3ml el expression node 
    if (expressionNode != null) {
        nodeReturn.addchild(expressionNode);
    } else {
        // Handle the case where no expression is found after return.
    	ParserSwingGUI.error("Expression expected after 'return'.");
    }
   /*  h ncheck b3den hl mafrod as2l 3la el semi colon wla l2
    match(";"); // Ensure that the next token is a semicolon
    token = Token.getNextToken(); // Move to the next token
        */ 
    return nodeReturn;
}
//declaration_stmt --> type identifier ;
//enta hena 3amlha generic l kol el types mesh type mo3yn f lazm t3dl feha b3den
private Treenode declaration_stmt() {
    pw.println("Declaration Statement is found");
    
    // Create a new syntax tree node for the declaration statement
    Treenode nodeDeclaration = new Treenode("declaration_stmt");
    
    // Parse type specifier
    String typeSpecifier = token.getValue();
    match("type"); // Match the type specifier token
    token = Token.getNextToken(); // Move to the next token
    
    // Parse identifier
    String identifierName = token.getValue();
    match("identifier"); // Match the identifier token
    token = Token.getNextToken(); // Move to the next token
    
    // Create syntax tree nodes for type specifier and identifier
    Treenode typeNode = new Treenode(typeSpecifier);
    Treenode identifierNode = new Treenode(identifierName);
    
    // Add type specifier and identifier nodes as children of the declaration statement node
    nodeDeclaration.addchild(typeNode);
    nodeDeclaration.addchild(identifierNode);
    
    return nodeDeclaration;
}
//Grammar rule: for_stmt --> for (initialization; condition; increment) statement
private Treenode for_stmt() {
    pw.println("For Statement is found");

    Treenode nodeFor = new Treenode("for"); // Create a new syntax tree node for the for statement

     match("for");    // Match and consume the 'for' keyword
     token = Token.getNextToken();
     match("(");    // Match and parse the initialization expression
     token = Token.getNextToken();

     //intialization part
     Treenode initializationNode = exp(); // Parse the initialization expression
     nodeFor.addchild(initializationNode); // Add the initialization expression node as a child
     match(";");     // Match and parse the condition expression
     token = Token.getNextToken();

     //Condition part
     Treenode conditionNode = exp(); // Parse the condition expression
     nodeFor.addchild(conditionNode); // Add the condition expression node as a child
     match(";");  // Match and parse the increment expression
     token = Token.getNextToken();

    //increment part
     Treenode incrementNode = exp(); // Parse the increment expression
     nodeFor.addchild(incrementNode); // Add the increment expression node as a child
     match(")");    // Match and parse the closing parenthesis
     token = Token.getNextToken();

    // Parse the statement inside the for loop
    Treenode statementNode = statement(); // Parse the statement inside the for loop
    nodeFor.addchild(statementNode); // Add the statement node as a child


    /*ta3del lw 3aiz t implement parse tree b kol tafaselo 
     
     // Construct the parse tree hierarchy
nodeFor.addchild(new Treenode("for")); // Add for keyword
nodeFor.addchild(new Treenode("(")); // Add opening parenthesis
nodeFor.addchild(initializationNode); // Add initialization expression
nodeFor.addchild(new Treenode(";")); // Add semicolon after initialization
nodeFor.addchild(conditionNode); // Add condition expression
nodeFor.addchild(new Treenode(";")); // Add semicolon after condition
nodeFor.addchild(incrementNode); // Add increment expression
nodeFor.addchild(new Treenode(")")); // Add closing parenthesis
nodeFor.addchild(statementNode); // Add statement inside the loop
     */

     return nodeFor; // Return the syntax tree node for the for statement
}
//do_while_stmt --> do statement while (expression) ;
private Treenode do_while_stmt() {
    pw.println("Do-While Statement is found");
    
    Treenode nodeDoWhile = new Treenode("do-while");  // Create a new syntax tree node for the do-while statement
    match("do");// Match and consume the 'do' keyword
    token = Token.getNextToken();
    
    // Parse the statement inside the do-while loop
    Treenode statementNode = statement(); // Parse the statement inside the do-while loop
    nodeDoWhile.addchild(statementNode); // Add the statement node as a child
    
    match("while"); // Match and consume the 'while' keyword
    token = Token.getNextToken();
    
    match("(");  // Match and consume the opening parenthesis
    token = Token.getNextToken();
    
    // Parse the expression inside the while condition
    Treenode expressionNode = exp(); // Parse the expression inside the while condition
    nodeDoWhile.addchild(expressionNode); // Add the expression node as a child
    
    // Match and consume the closing parenthesis
    match(")");
    token = Token.getNextToken();

    /*
     // Construct the parse tree hierarchy
nodeDoWhile.addchild(new Treenode("do")); // Add do keyword
nodeDoWhile.addchild(statementNode); // Add statement inside the loop
nodeDoWhile.addchild(new Treenode("while")); // Add while keyword
nodeDoWhile.addchild(new Treenode("(")); // Add opening parenthesis
nodeDoWhile.addchild(expressionNode); // Add expression inside the condition
nodeDoWhile.addchild(new Treenode(")")); // Add closing parenthesis
nodeDoWhile.addchild(new Treenode(";")); // Add semicolon

     */
    
    
    return nodeDoWhile; // Return the syntax tree node for the do-while statement
}


// Grammar rule: function_call --> identifier "(" [ assignment_statement {"," assignment_statement} ] ")"
private Treenode function_call() {
      pw.println("Function Call is found");

     Treenode nodeFunctionCall = new Treenode("function_call"); // Create a new syntax tree node for the function call
   
     // Parse the function identifier
     match("identifier"); // Assuming a method to match and consume the identifier token
     Treenode identifierNode = new Treenode(token.getValue()); // Create a node for the function identifier
    nodeFunctionCall.addchild(identifierNode); // Add the identifier node as a child

    match("("); // Match and consume the opening parenthesis
     token = Token.getNextToken(); // Move to the next token


    // Parse assignment statements if any
     if (token != null && !"(".equals(token.getValue()) && !")".equals(token.getValue())) {
     // Parse the first assignment statement
     Treenode assignmentNode = assign_stmt(); // Parse the first assignment statement
      nodeFunctionCall.addchild(assignmentNode); // Add the assignment statement node as a child

        // Parse additional assignment statements separated by commas
        while (token != null && ",".equals(token.getValue())) {
             match(","); // Match and consume the comma
             token = Token.getNextToken(); // Move to the next token

             // Parse the next assignment statement
            assignmentNode = assign_stmt(); // Parse the next assignment statement
             nodeFunctionCall.addchild(assignmentNode); // Add the assignment statement node as a child
                 }
        }

     match(")"); // Match and consume the closing parenthesis
    token = Token.getNextToken(); // Move to the next token

     return nodeFunctionCall; // Return the syntax tree node for the function call
    }




// Function definition grammar rule: 
//function_definition --> type identifier ( declaration,declaration,decleration ) stmt_seq    --> enta 3ndk el declerations el kteer dol 3shan el parameters
private Treenode function_definition() {
 pw.println("Function Definition is found");

 // Create a new syntax tree node for the function definition
 Treenode nodeFunctionDef = new Treenode("function_definition");

  // Parse type specifier
  String typeSpecifier = token.getValue();
 match("type"); // Match the type specifier token
 token = Token.getNextToken(); // Move to the next token

  // Parse identifier ( function name)
  String functionName = token.getValue();
 match("identifier"); // Match the identifier token
 token = Token.getNextToken(); // Move to the next token
 Treenode functionNameNode = new Treenode(functionName); // Create a node for the function name
 nodeFunctionDef.addchild(functionNameNode); // Add the function name node as a child

   // Match and consume the opening parenthesis for parameters
     match("(");
     token = Token.getNextToken();

     // Parse parameters
      if (!")".equals(token.getValue())) {
     // Parse the first assignment statement for the first parameter
     Treenode parameterNode = declaration_stmt(); // Parse the first parameter
    nodeFunctionDef.addchild(parameterNode); // Add the parameter node as a child
    
// Parse additional parameters separated by commas
     while (",".equals(token.getValue())) {
    match(","); // Match and consume the comma
    token = Token.getNextToken(); // Move to the next token

    // Parse the next assignment statement for the next parameter
    parameterNode = declaration_stmt(); // Parse the next parameter
    nodeFunctionDef.addchild(parameterNode); // Add the parameter node as a child
      }
 }

    // Match and consume the closing parenthesis for parameters
 match(")");
token = Token.getNextToken();

// Parse compound statement (function body)
Treenode compoundStatementNode = stmt_sequence(); // Assuming a method to parse statement sequence
nodeFunctionDef.addchild(compoundStatementNode); // Add compound statement node as a child of the function definition node

return nodeFunctionDef; // Return the syntax tree node for the function definition
}




//The primary purpose of the match method is to consume tokens during the parsing process.
//It is typically called after parsing a specific syntactic construct to advance the token stream to the next token.
//The method doesn't perform any explicit comparison between the expected token type and the actual token type, 
//which might be a deviation from traditional parser implementations where token matching is done explicitly.
//Instead, it just advances the token index assuming that the token type has been correctly parsed and validated elsewhere in the parsing logic.


    
    public void match(String expectedTokenType){
        
        if (Token.getI() < Token.tokens.size()){
        	System.out.println(token.getValue() + " " + token.getType());
            Token.incI();
        }
    }
    
    //hena enta btbd2 el visualization bs mlksh da3wa bel logic bta3 el rasm enta btt7km f shakl el edges wel klam dh bs
    //This "drawTree" method is responsible for initializing the visualization of the syntax tree represented by Treenode 
    //The purpose of this method is to set up the environment for visualizing the syntax tree using GraphStream.
    //It initializes the GraphStream properties, creates a viewer, and begins the visualization process by calling the draw method.
    public void drawTree(Treenode n) {
        //System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.swingViewer.DefaultGraphRenderer"); //enta 3mltlo comment 3shan hwa y5tar el rendered el 3la mzago
        graph.addAttribute("ui.quality"); //attribute controls the rendering quality of the graph. Setting it to true enhances the quality of the visualization by enabling advanced rendering features like smoother edges and improved layout.
        graph.addAttribute("ui.antialias");//attribute enables antialiasing, which smoothens the edges and shapes of graph elements, resulting in a more visually appealing and readable graph.
        Viewer viewer = graph.display();//it creates a Viewer object by calling graph.display(). This viewer is essential for displaying the graph.
        viewer.disableAutoLayout();//Disabling auto-layout prevents the viewer from automatically adjusting the layout of the graph elements.
        viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.HIDE_ONLY);//his policy specifies what should happen when the viewer's frame is closed. In this case, it hides the frame, allowing the application to continue running without terminating when the frame is closed.
        draw(n); //it initiates the visualization process by calling the draw method with the root Treenode n.
    
}
 
public Treenode draw(Treenode n){
    try {   
        if (n == null){ //lw m3mlsh parsing asln y3ne mafesh nodes wla roots yb2 hhyrg3 null
            return null;
        }
        int newy = y; //Assigns the current value of y to a new variable newy. This is used to keep track of the original value of y before any modifications.
        graph.addNode(n.getNodeName());//Adds a node to the graph with the name obtained from n.getNodeName(). This creates a new node in the graph representing the current Treenode
        Node temp = graph.getNode(n.getNodeName());//Retrieves the node object (Node) from the graph corresponding to the current Treenode and stores it in the variable temp.
        temp.addAttribute("ui.label", n.getDesc());//Sets the attribute "ui.label" of the node to the description obtained from n.getDesc(). This description likely represents the content or value of the current Treenode
        temp.setAttribute("xyz", x, newy ,z);//Sets the attribute "xyz" of the node to specify its position in the visualization. The x, newy, and z variables likely represent the coordinates where the node should be placed.
        if (condition1(n)){ //dh el representation nfso lw 3ndk pattern men el mshaher zy msln if , while aw keda hy3mlhom f box 3'er keda hy3mlhom f circle
            temp.addAttribute("ui.style", "shape:box;\n" +
                                          "fill-color: white;\n" +
                                          "stroke-mode: plain;\n" +
                                          "size: 65px, 30px;\n" + 
                                          " text-alignment: center;\n" + 
                                          "text-size: 10;");
        }
        else{ temp.addAttribute("ui.style", "shape:circle;\n" + 
                                            "fill-color: white;\n" + 
                                            "stroke-mode: plain;\n" +
                                            "size: 60px, 30px;\n" + 
                                            "text-alignment: center;\n" +
                                            "text-size: 10;");
        }
        //-----Children...
        y--; //Decrements the y coordinate. This likely adjusts the vertical position for drawing the children.
        //This loop iterates over the children of the current Treenode n, incrementing the x coordinate for each child.
        //It recursively calls the draw method for each child, creates edges between the parent and child nodes in the graph.
        for (int i = 0; i < n.children.size(); i++) {
            if (i!= 0){
                x++;
            }
            Treenode n1 = draw(n.children.get(i));
            Node temp1 = graph.getNode(n1.getNodeName());
            graph.addEdge(n.getNodeName()+","+ n1.getNodeName(), temp, temp1);
        }
        //-----Siblings...    
        y = newy; //This resets the y coordinate to its original value stored in newy.
        //This handles drawing sibling nodes if they exist. It increments the x coordinate,
        //draws the sibling node recursively, and creates an edge between the parent and sibling nodes in the graph.
        if (n.sibling != null){
            x++;
            Treenode n1 = draw(n.sibling);
            Node temp1 = graph.getNode(n1.getNodeName());
            graph.addEdge(n.getNodeName()+","+ n1.getNodeName(), temp, temp1);
        }
    }
    catch (Exception e){ //This catches any exceptions that might occur during the execution of the method and prints an error message.
            ParserSwingGUI.consoleTextArea.append("Error, Incorrect Code"); 
    }
    return n;
    }

//This condition1 method is a helper function that checks whether a given Treenode represents certain types of statements in a programming language
//The purpose of this method is to determine if the description (desc) of the provided Treenode matches certain predefined patterns.
//These patterns correspond to statements commonly found in programming languages, such as read, assign, write, if, and repeat statements.
public boolean condition1(Treenode n){
if (n.desc.startsWith("read") ||
    n.desc.startsWith("assign") ||
    n.desc.startsWith("write") ||
    n.desc.startsWith("if") ||
    n.desc.startsWith("repeat"))
{ 
    return true;
}
else 
   return false;
}
}