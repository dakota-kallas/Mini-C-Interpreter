package clite.ast;

public class Declaration {
	// Declaration = Variable v; Type t
    private Variable v;
    private Type t;

    public Declaration (Variable var, Type type) {
        v = var; t = type;
    }

	public Variable getV() {
		return v;
	}

	public Type getT() {
		return t;
	}
	
	public String toString() {
		String tStr = t == Type.BOOL ? "bool" : "int";
		return tStr + " " + v + ";\n";
	}

}
