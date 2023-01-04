package laba.ast;

public class ArrayAccessExpression extends Expression {
	public IdentifierExpression idE;
	public Value value;
	public ArrayAccessExpression(IdentifierExpression idE, Value value) {
		this.idE = idE;
		this.value = value;
	}
	
	public Object visit( Visitor v, Object arg )
	{
		return v.visitArrayAccessExpression( this, arg );
	}
}
