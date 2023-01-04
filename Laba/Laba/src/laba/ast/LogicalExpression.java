package laba.ast;

public class LogicalExpression extends Expression {
	public Operator operator;
	public Expression operand1;
	public Expression operand2;
	
	
	public LogicalExpression( Operator operator, Expression operand1, Expression operand2 )
	{
		this.operator = operator;
		this.operand1 = operand1;
		this.operand2 = operand2;
	}
	
	public Object visit( Visitor v, Object arg )
	{
		return v.visitLogicalExpression( this, arg );
	}
}
