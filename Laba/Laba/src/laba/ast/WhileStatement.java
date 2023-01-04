package laba.ast;

public class WhileStatement extends Statement {
	public Expression whileExp;
	public Statements thenPart;
	
	public WhileStatement(Expression whileExp, Statements thenPart ) 
	{
		this.whileExp = whileExp;
		this.thenPart = thenPart;
	}
	public Object visit( Visitor v, Object arg )
	{
		return v.visitWhileStatement( this, arg );
	}
}
