package laba.ast;

public class CallStatement extends Statement {
	public IdentifierExpression idE;
	public ExpressionList exprList;
	
	public CallStatement(IdentifierExpression idE,ExpressionList exprList) {
		this.idE = idE;
		this.exprList = exprList;
	}
	
	public Object visit( Visitor v, Object arg )
	{
		return v.visitCallStatement( this, arg );
	}
}
