package clite.ast;

public class Program {
    // Program = Declarations decpart ; Statement* body
    private Declarations decpart;
    private Block body;

    public Program (Declarations decpart, Block body ) {
    	this.decpart = decpart;
    	this.body = body;
    }
	
	public Block getBody() {
		return this.body;
	}
	
	public Declarations getDecpart() {
		return this.decpart;
	}
	
	public String toString( ) {
		StringBuffer sb = new StringBuffer();			
		sb.append( "int main() {\n" );
		decpart
			.forEach( decl -> sb.append( decl.toString() ));
		
		sb.append( body );

		return sb.toString();
	}
}
