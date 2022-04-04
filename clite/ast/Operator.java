package clite.ast;

public class Operator {
	// Operator = BooleanOp | RelationalOp | ArithmeticOp | UnaryOp
    // BooleanOp = && | ||
    public final static String AND = "&&";
    public final static String OR = "||";
    // RelationalOp = < | <= | == | != | >= | >
    public final static String LT = "<";
    public final static String LE = "<=";
    public final static String EQ = "==";
    public final static String NE = "!=";
    public final static String GT = ">";
    public final static String GE = ">=";
    // ArithmeticOp = + | - | * | /
    public final static String PLUS = "+";
    public final static String MINUS = "-";
    public final static String TIMES = "*";
    public final static String DIV = "/";
    // UnaryOp = !    
    public final static String NOT = "!";
    public final static String NEG = "-";
    
    public String val;
    
    public boolean is( String type ) {
    	return type.equals( val );
    }
    
    public Operator (String s) { val = s; }

    public String toString( ) { return val; }
    public boolean equals(Object obj) { return val.equals(obj); }
    
    public boolean BooleanOp ( ) { return val.equals(AND) || val.equals(OR); }
    public boolean RelationalOp ( ) {
        return val.equals(LT) || val.equals(LE) || val.equals(EQ)
            || val.equals(NE) || val.equals(GT) || val.equals(GE);
    }
    public boolean ArithmeticOp ( ) {
        return val.equals(PLUS) || val.equals(MINUS)
            || val.equals(TIMES) || val.equals(DIV);
    }
    public boolean NotOp ( ) { return val.equals(NOT) ; }
    public boolean NegateOp ( ) { return val.equals(NEG) ; }
    


}