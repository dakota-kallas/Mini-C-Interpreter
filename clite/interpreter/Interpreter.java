/**
 * C-LITE Meaning Calculator
 * Author: Dakota Kallas
 */
package clite.interpreter;

import clite.ast.*;
import clite.parser.Lexer;
import clite.parser.Parser;
import clite.typing.TypeChecker;
import java.util.List;

public class Interpreter {

	/**
	 * Method used to determine the final State of a program
	 * @param program: the program being evaluated
	 * @return the final State of program
	 */
	public static State meaning( Program program ) {
		State state = new State();
		Declarations decpart = program.getDecpart();
		state = initFillState(state, decpart);
		Block body = program.getBody();
		evaluateBlock(body, state);
		return state;
	}

	/**
	 * Method used to evaluate a Block
	 * @param b: The Block being evaluated
	 * @param state: The current state
	 */
	private static void evaluateBlock(Block b, State state) {
		Declarations dec = b.getDeclarations();
		State currentState = new State(state);
		currentState = initFillState(currentState, dec);

		List<Statement> statements = b.getStatements();
		for(int i = 0; i < statements.size(); i++) {
			evaluateStatement(statements.get(i), currentState);
		}
	}

	/**
	 * Method used to evaluate a Statement
	 * @param s: The Statement being evaluated
	 * @param state: The current state
	 */
	private static void evaluateStatement(Statement s, State state) {
		if(s instanceof Assignment) {
			evaluateAssignment((Assignment) s, state);
		}
		else if(s instanceof Loop) {
			evaluateLoop((Loop) s, state);
		}
		else if(s instanceof Block) {
			evaluateBlock((Block) s, state);
		}
		else if(s instanceof Conditional) {
			evaluateConditional((Conditional) s, state);
		}
		else if(s instanceof Skip) {
			return;
		}
	}

	/**
	 * Method used to evaluate an Assignment statement
	 * @param a: The Assignment beign evaluated
	 * @param state: The current state
	 */
	private static void evaluateAssignment(Assignment a, State state) {
		state.put(a.getTarget(), evaluateExpression(a.getSource(), state));
	}

	/**
	 * Method used to evaluate an Loop statement
	 * @param l: The Loop beign evaluated
	 * @param state: The current state
	 */
	private static void evaluateLoop(Loop l, State state) {
		while(Boolean.valueOf(evaluateExpression(l.getTest(), state).toString())) {
			evaluateStatement(l.getBody(), state);
		}
	}

	/**
	 * Method used to evaluate an Conditional statement
	 * @param c: The Conditional beign evaluated
	 * @param state: The current state
	 */
	private static void evaluateConditional(Conditional c, State state) {
		if(Boolean.valueOf(evaluateExpression(c.getTest(), state).toString())){
			evaluateStatement(c.getThenbranch(), state);
		}
		else {
			evaluateStatement(c.getElsebranch(), state);
		}
	}

	/**
	 * Method used to find the Value of an expression
	 * @param e: The expression being evaluated
	 * @param state: The current state
	 * @return the value of the expression
	 */
	private static Value evaluateExpression(Expression e, State state) {
		if(e instanceof Value) {
			return (Value) e;
		}
		else if(e instanceof Variable) {
			try {
				if(!state.get((Variable) e).equals(null)) {
					return state.get((Variable) e);
				}
			} catch (NullPointerException ex) {
				System.err.println("RUN-TIME ERROR: Variable not initialized. (" + (Variable) e + ")");
				System.out.println("Program is not valid.");
				System.exit(0);
			}
			return state.get((Variable) e);
		}
		else if(e instanceof Unary) {
			return evaluateUnary((Unary) e, state);
		}
		else if(e instanceof Binary) {
			return evaluateBinary((Binary) e, state);
		}
		return null;
	}

	/**
	 * Method used to evalute the Value of a Unary expression
	 * @param u: The Unary expression being evaluated.
	 * @param state: The current state of the program
	 * @return the evaluated value of u
	 */
	private static Value evaluateUnary(Unary u, State state) {
		Operator o = u.getOp();
		Value v = evaluateExpression(u.getTerm(), state);

		if(o.NotOp()) {
			if(v.toString().equals("true")) {
				return new Value<>(false);
			}
			else {
				return new Value<>(true);
			}
		}
		else if(o.NegateOp()) {
			return new Value<>((0 - Integer.valueOf(v.toString())));
		}
		
		return null;
	}

	/**
	 * Method used to evalute the Value of a Binary expression
	 * @param b: The Binary expression being evaluated.
	 * @param state: The current state of the program
	 * @return the evaluated value of b
	 */
	private static Value evaluateBinary(Binary b, State state) {
		Operator o = b.getOp();
		Value v1 = evaluateExpression(b.getTerm1(), state);
		Value v2 = evaluateExpression(b.getTerm2(), state);

		if(o.ArithmeticOp()) {
			if(o.is("+")) {
				return new Value<>((Integer.valueOf(v1.toString()) + Integer.valueOf(v2.toString())));
			}
			else if(o.is("-")) {
				return new Value<>((Integer.valueOf(v1.toString()) - Integer.valueOf(v2.toString())));
			}
			else if(o.is("/")) {
				return new Value<>((Integer.valueOf(v1.toString()) / Integer.valueOf(v2.toString())));
			}
			else if(o.is("*")) {
				return new Value<>((Integer.valueOf(v1.toString()) * Integer.valueOf(v2.toString())));
			}
		}
		else if(o.BooleanOp()){
			if(o.is("&&")) {
				return new Value<>(Boolean.valueOf(v1.toString()) && Boolean.valueOf(v2.toString()));
			}
			else if(o.is("||")) {
				return new Value<>(Boolean.valueOf(v1.toString()) || Boolean.valueOf(v2.toString()));
			}
		}
		else if(o.RelationalOp()) {
			if(o.is("<")) {
				return new Value<>((Integer.valueOf(v1.toString()) < Integer.valueOf(v2.toString())));
			}
			else if(o.is("<=")) {
				return new Value<>((Integer.valueOf(v1.toString()) <= Integer.valueOf(v2.toString())));
			}
			else if(o.is("==")) {
				return new Value<>(v1.toString().equals(v2.toString()));
			}
			else if(o.is("!=")) {
				return new Value<>(!v1.toString().equals(v2.toString()));
			}
			else if(o.is(">=")) {
				return new Value<>((Integer.valueOf(v1.toString()) >= Integer.valueOf(v2.toString())));
			}
			else if(o.is(">")) {
				return new Value<>((Integer.valueOf(v1.toString()) > Integer.valueOf(v2.toString())));
			}
		}

		return null;
	}

	/**
	 * Method used to inititalize variables in a state
	 * @param s: The current state
	 * @param dec: The declartations being put in the state
	 * @return the updated state
	 */
	private static State initFillState(State s, Declarations dec) {
		for(int i = 0; i < dec.size(); i++) {
			Declaration d = dec.get(i);
			Variable v = d.getV();
			s.init(v);
		}
		return s;
	}
	
	public static void main(String[] args ) {
		Parser parser  = new Parser(new Lexer(args[0]) );
		Program program = parser.program();
		if( !TypeChecker.isValid( program ) ) {
			System.out.println("Program is not valid.");
		} else {
			System.out.println( meaning( program ) );
		}
	}
	
}
