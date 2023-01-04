package laba.ast;

public class IfStatement extends Statement {
	public Expression ifPart;
	public Statements thenPart;
	public Statements elsePart;
	
	public IfStatement(Expression ifPart, Statements thenPart ,Statements elsePart) 
	{
		this.ifPart = ifPart;
		this.thenPart = thenPart;
		this.elsePart = elsePart;
	}
	
	public Object visit( Visitor v, Object arg )
	{
		return v.visitIfStatement( this, arg );
	}
}
