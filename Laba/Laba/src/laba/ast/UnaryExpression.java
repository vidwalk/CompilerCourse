package laba.ast;

public class UnaryExpression extends Expression {
	public Operator operator;
	public Expression operand;
	
	
	public UnaryExpression( Operator operator, Expression operand )
	{
		this.operator = operator;
		this.operand = operand;
	}
	
	@Override
	public Object visit( Visitor v, Object arg )
	{
		return v.visitUnaryExpression( this, arg );
	}


}
