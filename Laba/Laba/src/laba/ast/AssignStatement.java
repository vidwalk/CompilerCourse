package laba.ast;

public class AssignStatement extends Statement {
	public IdentifierExpression idE;
	public Operator operator;
	public Expression expr;
	
	public AssignStatement(IdentifierExpression idE, Operator operator, Expression expr) {
		this.idE = idE;
		this.operator = operator;
		this.expr = expr;
	}

	public Object visit( Visitor v, Object arg )
	{
		return v.visitAssignStatement( this, arg );
	}
}
