package clite.ast;

import java.util.List;

public class Block extends Statement {
    // Block = { Declarations Statement* }
	private Declarations declarations;
    private List<Statement> statements;
    
    public Block( Declarations declarations, List<Statement> statements ) {
    	this.declarations = declarations;
    	this.statements = statements;
    }
    
    public Declarations getDeclarations() {
    	return declarations;
    }
    
    public List<Statement> getStatements() {
    	return statements;
    }
    
    public String toString() {
    	StringBuffer sb = new StringBuffer();
    	sb.append("{\n");
    	declarations
    		.forEach( decl -> sb.append( decl ) );
    	statements
    		.forEach( stmt -> sb.append( stmt ) );
    	sb.append("}\n");
    	return sb.toString();
    }
}
