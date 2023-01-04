package laba.ast;

public class ValueExpression extends Expression {
public Value literal;
	
	
	public ValueExpression( Value literal )
	{
		this.literal = literal;
	}
	
	public Object visit( Visitor v, Object arg )
	{
		return v.visitValueExpression( this, arg );
	}
}
