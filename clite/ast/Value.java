package clite.ast;

public class Value<T> extends Expression {
	private T value;

    public Value( T value ) {
    	this.value = value;
    }

    public String toString( ) {
    	return String.valueOf( this.value );
    }
    
    public Type typeOf( ) {
    	if( value instanceof Boolean ) {
    		return Type.BOOL;
    	} else if( value instanceof Integer ) {
    		return Type.INT;
    	} else {
    		throw new RuntimeException("Unknonw");
    	}
    }
    
    public T getValue() {
    	return this.value;
    }
	
}
