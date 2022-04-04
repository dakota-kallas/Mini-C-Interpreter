package clite.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import clite.ast.*;
import clite.typing.TypeChecker;

public class Parser {
    Token token;          		// current token from the input stream
    Lexer lexer;

    public Parser(Lexer ts) { 	// Open the Clite source program
        lexer = ts;           	// as a token stream, and
        token = lexer.next(); 	// retrieve its first Token
    }
  
    private String match (TokenType t) {
        String value = token.value();
        if (token.type().equals(t)) {
            token = lexer.next();
        } else {
            error(t);
        }
        return value;
    }
 
    private void error(TokenType tok) {
        System.err.println("Syntax error: expecting: " + tok 
                           + "; saw: " + token);
        System.exit(1);
    }
  
    private void error(String tok) {
        System.err.println("Syntax error: expecting: " + tok 
                           + "; saw: " + token);
        System.exit(1);
    }
  
    public Program program() {
        // Program --> int main ( ) { Declarations Statement* }
        List<TokenType >header = Arrays.asList(
        		TokenType.Int, 
        		TokenType.Main,
                TokenType.LeftParen, 
                TokenType.RightParen
        );
        
        for( TokenType tt : header  ) {
        	match( tt );
        }
        
        match( TokenType.LeftBrace );
        Declarations decls = declarations();
        Block body = block();
        match( TokenType.RightBrace );
        return new Program( decls, body );
    }

    private Declarations declarations() {
        // Declarations --> Declaration*
    	List<Declaration> decls = new ArrayList<>();
        
        while (isType()) {
            decls.add( declaration() );
        }
        return new Declarations( decls );
    }
  
    private Declaration declaration() {
        // Declaration  --> Type Identifier;
        Type type = type();
        String id = match( TokenType.Identifier );
        match( TokenType.Semicolon );
        return new Declaration( new Variable(id), type );
   }
  
    private Type type () {
        // Type  -->  int | bool | float | char 
        Type t = null;
        if (token.type().equals(TokenType.Int)) {
            t = Type.INT;
        } else if (token.type().equals(TokenType.Bool)) {
            t = Type.BOOL;
        } else {
        	error("int | bool | float | char");
        }
        
        token = lexer.next(); // pass over the type
        return t;          
    }
  
    private Statement statement() {
        // Statement --> ; | Block | Assignment | IfStatement | WhileStatement

        if (token.type().equals(TokenType.Semicolon)) { // Skip
        	match( TokenType.Semicolon );
            return new Skip();
        } else if (token.type().equals(TokenType.LeftBrace)) { // Block 
        	return block();
        } else if (token.type().equals(TokenType.If)) {         // IfStatement 
            return ifStatement();
        } else if (token.type().equals(TokenType.While)) {      // WhileStatement 
            return whileStatement();
        } else if (token.type().equals(TokenType.Identifier)) {  // Assignment
            return assignment();
        } else {
        	error("Illegal statement");
        }
        return null;
    }
  
    private Block block() {
        // Block --> '{' Declarations Statements '}'
    	match( TokenType.LeftBrace);
    	
    	// read declarations
    	Declarations decls = declarations();
    	
    	// read statements
    	List<Statement> stmts = new ArrayList<>();    	
        while (! token.type().equals(TokenType.RightBrace)) {
        	stmts.add( statement() );
        }
        match( TokenType.RightBrace );
        return new Block( decls, stmts );
    }

    private Assignment assignment () {
        // Assignment --> Identifier = Expression ;
        Variable target = new Variable(match(TokenType.Identifier));
        match(TokenType.Assign);
        Expression source = expression();
        match(TokenType.Semicolon);
        return new Assignment(target, source);
    }

    private Conditional ifStatement () {
        // IfStatement --> if ( Expression ) Statement [ else Statement ]
        match(TokenType.If);
        Expression test = expression();
        Statement thenbranch = statement();
        Statement elsebranch = new Skip();
        if (token.type().equals(TokenType.Else)){
            token = lexer.next();
            elsebranch = statement();
        }
        return new Conditional(test, thenbranch, elsebranch);
    }
  
    private Loop whileStatement () {
        // WhileStatement --> while ( Expression ) Statement
        match(TokenType.While);
        match(TokenType.LeftParen);
        Expression test = expression();
        match(TokenType.RightParen);
        Statement body = statement();
        return new Loop(test, body);
    }

    private Expression expression () {
        // Expression --> Conjunction { || Conjunction }
        Expression e = conjunction();
        while (token.type().equals(TokenType.Or)) {
            Operator op = new Operator(token.value());
            token = lexer.next();
            Expression term2 = conjunction();
            e = new Binary(op, e, term2);
        }
        return e;     
    }
  
    private Expression conjunction () {
        // Conjunction --> Equality { && Equality }
        Expression e = equality();
        while (token.type().equals(TokenType.And)) {
            Operator op = new Operator(token.value());
            token = lexer.next();
            Expression term2 = equality();
            e = new Binary(op, e, term2);
        }
        return e;     
    }
  
    private Expression equality () {
        // Equality --> Relation [ EquOp Relation ]
        Expression e = relation();
        while (isEqualityOp()) {
            Operator op = new Operator(token.value());
            token = lexer.next();
            Expression term2 = relation();
            e = new Binary(op, e, term2);
        }
        return e;
    }

    private Expression relation (){
        // Relation --> Addition [RelOp Addition] 
        Expression e = addition();
        while (isRelationalOp()){
            Operator op = new Operator(token.value());
            token = lexer.next();
            Expression term2 = addition();
            e = new Binary(op, e, term2);
        }
        return e;
    }
  
    private Expression addition () {
        // Addition --> Term { AddOp Term }
        Expression e = term();
        while (isAddOp()) {
            Operator op = new Operator(match(token.type()));
            Expression term2 = term();
            e = new Binary(op, e, term2);
        }
        return e;
    }
  
    private Expression term () {
        // Term --> Factor { MultiplyOp Factor }
        Expression e = factor();
        while (isMultiplyOp()) {
            Operator op = new Operator(match(token.type()));
            Expression term2 = factor();
            e = new Binary(op, e, term2);
        }
        return e;
    }
  
    private Expression factor() {
        // Factor --> [ UnaryOp ] Primary 
        if (isUnaryOp()) {
            Operator op = new Operator(match(token.type()));
            Expression term = primary();
            return new Unary(op, term);
        }
        else return primary();
    }
  
    private Expression primary () {
        // Primary --> Identifier | Literal | ( Expression )
        //             | Type ( Expression )
        Expression e = null;
        if (token.type().equals(TokenType.Identifier)) {
            Variable v = new Variable(match(TokenType.Identifier));
            e = v;
        } else if (isLiteral()) {
            e = literal();
        } else if (token.type().equals(TokenType.LeftParen)) {
            token = lexer.next();
            e = expression();       
            match(TokenType.RightParen);
        } else if (isType( )) {
            Operator op = new Operator(match(token.type()));
            match(TokenType.LeftParen);
            Expression term = expression();
            match(TokenType.RightParen);
            e = new Unary(op, term);
        } else error("Identifier | Literal | ( | Type");
        return e;
    }

    @SuppressWarnings("rawtypes")
	private Value literal( ) {
        String s = null;
        switch (token.type()) {
        case IntLiteral:        	
            s = match(TokenType.IntLiteral);
            return new Value<Integer>( Integer.parseInt(s) );
        case True:
            s = match(TokenType.True);
            return new Value<Boolean>(true);
        case False:
            s = match(TokenType.False);
            return new Value<Boolean>(false);
        default:
            throw new IllegalArgumentException( "error" );
        }
    }
  

    private boolean isAddOp( ) {
        return token.type().equals(TokenType.Plus) ||
               token.type().equals(TokenType.Minus);
    }
    
    private boolean isMultiplyOp( ) {
        return token.type().equals(TokenType.Multiply) ||
               token.type().equals(TokenType.Divide);
    }
    
    private boolean isUnaryOp( ) {
        return token.type().equals(TokenType.Not) ||
               token.type().equals(TokenType.Minus);
    }
    
    private boolean isEqualityOp( ) {
        return token.type().equals(TokenType.Equals) ||
            token.type().equals(TokenType.NotEqual);
    }
    
    private boolean isRelationalOp( ) {
        return token.type().equals(TokenType.Less) ||
               token.type().equals(TokenType.LessEqual) || 
               token.type().equals(TokenType.Greater) ||
               token.type().equals(TokenType.GreaterEqual);
    }
    
    private boolean isType( ) {
        return token.type().equals(TokenType.Int)
            || token.type().equals(TokenType.Bool) 
            || token.type().equals(TokenType.Float)
            || token.type().equals(TokenType.Char);
    }
    
    private boolean isLiteral( ) {
        return 
        		token.type().equals(TokenType.IntLiteral) ||
        		token.type().equals(TokenType.True) ||
                token.type().equals(TokenType.False);    
    }
    
    
    public static void main(String args[]) {
    	String dir = "/Users/hunt/Dropbox/UWL/teaching/cs421/OtherMaterials/AuthorMaterials2/softwarestudents/clite/programs/";
    	String file = dir +  "hunt-p2.cpp";
    	
    	Parser parser  = new Parser(new Lexer(file) );
        Program prog = parser.program();
        System.out.println(TypeChecker.isValid( prog ) );
        System.out.println( prog );
    }

} // Parser