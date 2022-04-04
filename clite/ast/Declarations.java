package clite.ast;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class Declarations extends ArrayList<Declaration> {
	public Declarations( List<Declaration> decls ) {
		this.addAll( decls );
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for( Declaration decl : this ) {
			sb.append( decl.toString() );
		}
		return sb.toString();				
	}
}
