package laba.ast;

public class ValueListAssignStatement extends Statement {
	public IdentifierExpression idE;
	public Operator operator;
	public ValueList valueList;
	
	public ValueListAssignStatement(IdentifierExpression idE, Operator operator ,ValueList valueList) 
	{
		this.idE = idE;
		this.operator = operator;
		this.valueList = valueList;
	}
	public Object visit( Visitor v, Object arg )
	{
		return v.visitValueListAssigntStatement( this, arg );
	}
}
