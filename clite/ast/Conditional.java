package clite.ast;

public class Conditional extends Statement {
	// Conditional = Expression test; Statement thenbranch, elsebranch
    // if elsebranch == SKIP, then one-way conditional
    Expression test;
    Statement thenbranch, elsebranch;
    
    public Conditional (Expression t, Statement tp) {
        test = t; thenbranch = tp; elsebranch = new Skip();
    }
    
    public Conditional (Expression t, Statement tp, Statement ep) {
        test = t; thenbranch = tp; elsebranch = ep;
    }

	public Expression getTest() {
		return test;
	}

	public Statement getThenbranch() {
		return thenbranch;
	}

	public Statement getElsebranch() {
		return elsebranch;
	}
	
	public String toString() {
		return "if(" + test + ")\n" + thenbranch + elsebranch + "\n";
	}
}
