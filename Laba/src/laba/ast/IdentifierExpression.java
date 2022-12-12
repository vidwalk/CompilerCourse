package laba.ast;

public class IdentifierExpression extends Expression {
public Identifier name;
	
	
	public IdentifierExpression( Identifier name )
	{
		this.name = name;
	}
}
