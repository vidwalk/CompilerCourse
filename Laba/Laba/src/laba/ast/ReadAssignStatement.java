package laba.ast;

public class ReadAssignStatement extends Statement {
	public IdentifierExpression idE;
	public Operator op;
	public ReadAssignStatement(IdentifierExpression idE, Operator op) {
		this.idE = idE;
		this.op = op;
	}
	public Object visit( Visitor v, Object arg )
	{
		return v.visitReadAssignStatement( this, arg );
	}
}
