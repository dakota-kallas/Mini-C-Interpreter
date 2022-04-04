package clite.typing;

import java.util.HashMap;
import java.util.Map;

import clite.ast.Type;
import clite.ast.Variable;

@SuppressWarnings("serial")
public class TypeMap extends HashMap<Variable, Type> { 
	public TypeMap() {		
	}
	
	public TypeMap(Map<Variable,Type> parent) {
		super( parent );
	}
}