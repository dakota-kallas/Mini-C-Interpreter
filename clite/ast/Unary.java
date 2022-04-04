package clite.ast;

public class Unary extends Expression {
    // Unary = Operator op; Expression term
    private Operator op;
    private Expression term;

    public Unary (Operator o, Expression e) {
        this.op = o; 
        this.term = e;
    }

	public Operator getOp() {
		return op;
	}

	public Expression getTerm() {
		return term;
	}
	
	public String toString() {
		return op + "" + term;
	}
}
