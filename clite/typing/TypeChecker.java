/**
 * C-LITE Validity Checker
 * Author: Dakota Kallas
 */
package clite.typing;

import clite.ast.*;
import clite.parser.Lexer;
import clite.parser.Parser;
import java.util.List;

public class TypeChecker {

	/**
	 * Method used to determine if a Program is valid
	 * @param p: The Program being checked
	 * @return true or false depending on if the Program is valid
	 */
	public static boolean isValid(Program p) {
		// Create the Type Map for the decpart of Program p
		TypeMap tm = new TypeMap();
		Declarations decpart = p.getDecpart();
		tm = fillTypeMap(tm, decpart);
		Block pBody = p.getBody();
		
		return isBlockValid(pBody, tm);
	}

	/**
	 * Method used to fill a Type Map with a set of declarations
	 * @param m: The current Type Map
	 * @param d: The declarations to be filled into the Type Map
	 * @return The updated Type Map
	 */
	private static TypeMap fillTypeMap(TypeMap m, Declarations d) {
		TypeMap tm = new TypeMap(m);
		for(int i = 0; i < d.size(); i++) {
			Declaration var = d.get(i);
			tm.put(var.getV(), var.getT());
		}
		return tm;
	}

	/**
	 * Method used to check if a Block is valid
	 * @param b: The Block being checked
	 * @param m: The current state of the Type Map
	 * @return: true or false depending on if the Block is valid
	 */
	private static boolean isBlockValid(Block b, TypeMap m) {
		Declarations dec = b.getDeclarations();
		TypeMap tm = m;
		Boolean bool = true;
		
		if(dec.size() > 0) {
			if(isDeclarationDuplicates(dec)) {
				bool = false;
			}
			tm = fillTypeMap(tm, dec);
		}
		List<Statement> statements = b.getStatements();
		for(int i = 0; i < statements.size(); i++) {
			Statement s = statements.get(i);
			if(!isStatementValid(s, tm)) {
				bool = false;
			}
		}
		return bool;
	}

	/**
	 * Method used to check if a Declarations body contains duplicate variables
	 * @param dec: The set of declarations
	 * @return true or false depending on if there are duplicate variables present
	 */
	private static boolean isDeclarationDuplicates(Declarations dec) {
		TypeMap m = new TypeMap();
		for(int i = 0; i < dec.size(); i++) {
			Declaration d = dec.get(i);
			Variable v = d.getV();
			if(m.containsKey(v)) {
				System.err.println("ERROR: Duplicate variables. (" + v + ")");
				return true;
			}
			m.put(v, d.getT());
		}
		return false;
	}

	/**
	 * Method used to determine if a Statement is valid
	 * @param s: The Statement being checked
	 * @param m: The current state of the Type Map
	 * @return: true or false depending on if the Statement is valid
	 */
	private static boolean isStatementValid(Statement s, TypeMap m) {
		if(s instanceof Assignment) {
			if(!isAssignmentValid((Assignment) s, m)) {
				return false;
			}
		}
		else if(s instanceof Skip) {
			return true;
		}
		else if(s instanceof Loop) {
			if(!isLoopValid((Loop) s, m)) {
				return false;
			}
		}
		else if(s instanceof Block) {
			if(!isBlockValid((Block) s, m)) {
				return false;
			}
		}
		else if(s instanceof Conditional) {
			if(!isConditionalValid((Conditional) s, m)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Method that checks if a conditional expression is valid
	 * @param c: The Conditional being checked
	 * @param m: The current state of the Type Map
	 * @return: true or false depending on if the Conditional is valid
	 */
	private static boolean isConditionalValid(Conditional c, TypeMap m) {
		Expression test = c.getTest();
		Boolean bool = true;

		if(!isExpressionValid(test, m)) {
			bool = false;
		}
		if(expressionType(test, m) != Type.BOOL) {
			System.err.println("ERROR: Invalid test type. (" + test + ")");
			bool = false;
		}
		Statement thenBranch = c.getThenbranch();
		if(!isStatementValid(thenBranch, m)) {
			bool = false;
		}
		Statement elseBranch = c.getElsebranch();
		if(!isStatementValid(elseBranch, m)) {
			bool = false;
		}

		return bool;
	}

	/**
	 * Method that checks if a loop expression is valid
	 * @param l: The Loop being checked
	 * @param m: The current state of the Type Map
	 * @return: true or false depending on if the Loop is valid
	 */
	private static boolean isLoopValid(Loop l, TypeMap m) {
		Expression test = l.getTest();
		Boolean bool = true;

		if(!isExpressionValid(test, m)) {
			bool = false;
		} 
		if(expressionType(test, m) != Type.BOOL) {
			System.err.println("ERROR: Invalid test type. (" + test + ")");
			bool = false;
		}
		Statement s = l.getBody();
		if(!isStatementValid(s, m)) {
			bool = false;
		}
		return bool;
	}

	/**
	 * Method used to check if an assignment expression is valid
	 * @param a: The Assignment being checked
	 * @param m: The current state of the Type Map
	 * @return: true or false depending on if the Assignment is valid
	 */
	private static boolean isAssignmentValid(Assignment a, TypeMap m) {
		Variable v = a.getTarget();
		Expression e = a.getSource();
		Boolean bool = true;

		// If the expression and variable types do not match, the assignment is invalid.
		if(m.get(v) != expressionType(e, m)) {
			System.err.print("ERROR: Type mismatch. " + a);
			bool = false;
		}
		return isExpressionValid(e, m) && bool;
	}

	/**
	 * Method used to check if an expression is valid
	 * @param e: The Expression being checked
	 * @param m: The current state of the Type Map
	 * @return: true or false depending on if the Expression is valid
	 */
	private static boolean isExpressionValid(Expression e, TypeMap m) {
		if(e instanceof Value) {
			return true;
		}
		else if(e instanceof Variable) {
			Variable v = (Variable) e;
			if(!m.containsKey(v)) {
				System.err.println("ERROR: Variable does not exist. (" + v + ")");
			}
			return true;
		}
		else if(e instanceof Binary) {
			Binary b = (Binary) e;
			return isBinaryValid(b, m);

		}
		else if(e instanceof Unary) {
			Unary u = (Unary) e;
			return isUnaryValid(u, m);
		}

		System.err.println("ERROR: Experssion is invalid. (" + e + ")");
		return false;
	}

	/**
	 * Method used to check if a unary expression is valid
	 * @param u: The Unary being checked
	 * @param m: The current state of the Type Map
	 * @return: true or false depending on if the unary is valid
	 */
	private static boolean isUnaryValid(Unary u, TypeMap m) {
		Operator o = u.getOp();
		Expression term = u.getTerm();
		if(isExpressionValid(term, m)) {
			if(o.NegateOp()) {
				if(expressionType(term, m) == Type.INT) {
					return true;
				}
			}
			else if(o.NotOp()) {
				if(expressionType(term, m) == Type.BOOL) {
					return true;
				}
			}
			System.err.println("ERROR: Invalid type. (" + u + ")"); 
		}

		return false;
	}

	/**
	 * Method used to check if a binary expression is valid
	 * @param b: The Binary being checked
	 * @param m: The current state of the Type Map
	 * @return: true or false depending on if the binary is valid
	 */
	private static boolean isBinaryValid(Binary b, TypeMap m) {
		Operator o = b.getOp();
		Expression t1 = b.getTerm1();
		Expression t2 = b.getTerm2();
		if(isExpressionValid(t1, m) && isExpressionValid(t2, m)) {
			if(o.ArithmeticOp()) {
				if((expressionType(t1, m) == Type.INT) && (expressionType(t2, m) == Type.INT)) {
					return true;
				}
			}
			else if(o.BooleanOp()) {
				if((expressionType(t1, m) == Type.BOOL) && (expressionType(t2, m) == Type.BOOL)) {
					return true;
				}
			}
			else if(o.RelationalOp()) {
				if((o.toString() == "==") || (o.toString() == "!=")) {
					if((expressionType(t1, m) != null) && (expressionType(t2, m) != null)) {
						return true;
					}
				}
				else {
					if((expressionType(t1, m) == Type.INT) && (expressionType(t2, m) == Type.INT)) {
						return true;
					}
				}
			}
			System.err.println("ERROR: Invalid binary parameter type. (" + b + ")"); 
		}

		return false;
	}

	/**
	 * Method used to determine the type of an expression
	 * @param e: The expression
	 * @param m: The current Type Map
	 * @return the type of the expression
	 */
	private static Type expressionType(Expression e, TypeMap m) {
		if(e instanceof Value) {
			Value v = (Value) e;
			return v.typeOf();
		}
		else if(e instanceof Variable) {
			Variable v = (Variable) e;
			return m.get(v);
		}
		else if(e instanceof Binary) {
			Binary b = (Binary) e;
			return getOperatorType(b.getOp());
		}
		else if(e instanceof Unary) {
			Unary u = (Unary) e;
			return getOperatorType(u.getOp());
		}

		System.err.println("ERROR: Invalid expression. (" + e + ")"); 
		return null;
	}

	/**
	 * Method used to determine the type returned by an operator
	 * @param o: The operator being checked
	 * @return the type of the operator
	 */
	private static Type getOperatorType(Operator o) {
		if(o.ArithmeticOp() || o.NegateOp()) {
			Type t = Type.INT;
			return t;
		}
		else if(o.BooleanOp() || o.NotOp() || o.RelationalOp()) {
			Type t = Type.BOOL;
			return t;
		}

		System.err.println("ERROR: Invalid operator. (" + o + ")"); 
		return null;
	}

	public static void main(String args[]) {		
		Parser parser  = new Parser(new Lexer(args[0]) );
		Program prog = parser.program();
		System.out.println(isValid( prog ) );
	}
}