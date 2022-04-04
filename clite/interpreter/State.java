package clite.interpreter;

import clite.ast.Variable;
import clite.ast.Value;
import java.util.HashMap;

@SuppressWarnings("serial")
public class State extends HashMap<Variable, Value<?>> {
	private State containingState;
	
	public State( State containingState ) {
		this.containingState = containingState;
	}
	
	public State() {
		this.containingState = null;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public Value put( Variable variable, Value<?> value ) {
		if( this.containsKey(variable) ) {
			return super.put( variable, value);
		} else if( this.containingState != null ) {
			return this.containingState.put(variable,value);
		} else {
			throw new RuntimeException("Undeclared variable");
		}					
	}
	
	public void init( Variable v ) {
		super.put( v, null );
	}
	
	@Override
	public Value<?> get( Object v ) {
		if( this.containsKey(v) ) {
			return super.get(v);
		} else if( this.containingState != null ) {
			return this.containingState.get( v );
		} else {
			return null;
		}
	}
}
