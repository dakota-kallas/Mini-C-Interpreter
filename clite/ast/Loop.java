package clite.ast;

public class Loop extends Statement {
	// Loop = Expression test; Statement body
    Expression test;
    Statement body;

    public Loop (Expression t, Statement b) {
        test = t; body = b;
    }

	public Expression getTest() {
		return test;
	}

	public Statement getBody() {
		return body;
	}    
	
	public String toString() {
		return "while(" + test + ")\n" + body;
	}
}
