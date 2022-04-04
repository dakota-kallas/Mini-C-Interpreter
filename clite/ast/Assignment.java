package clite.ast;

public class Assignment extends Statement {
    // Assignment = Variable target; Expression source
    Variable target;
    Expression source;

    public Assignment (Variable t, Expression e) {
        target = t;
        source = e;
    }

	public Variable getTarget() {
		return target;
	}

	public Expression getSource() {
		return source;
	}
	
	public String toString() {
		return target + " = " + source + ";\n";
	}
}
