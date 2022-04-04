package clite.ast;

public class Binary extends Expression {
	// Binary = Operator op; Expression term1, term2
	private Operator op;
    private Expression term1, term2;

    public Binary (Operator o, Expression l, Expression r) {
        op = o; term1 = l; term2 = r;
    }

	public Operator getOp() {
		return op;
	}

	public Expression getTerm1() {
		return term1;
	}

	public Expression getTerm2() {
		return term2;
	} 
	
	public String toString() {
		return term1.toString() + op + term2;
	}
}
