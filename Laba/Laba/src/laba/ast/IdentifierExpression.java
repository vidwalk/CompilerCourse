package laba.ast;

public class IdentifierExpression extends Expression {
public Identifier name;
public Declaration decl;	
	
	public IdentifierExpression( Identifier name )
	{
		this.name = name;
	}
	
	public Object visit( Visitor v, Object arg )
	{
		return v.visitIdentifierExpression( this, arg );
	}
}
