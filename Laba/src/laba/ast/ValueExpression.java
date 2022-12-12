package laba.ast;

public class ValueExpression extends Expression {
public Value literal;
	
	
	public ValueExpression( Value literal )
	{
		this.literal = literal;
	}
}
