package laba.ast;

public class LogicalOperator extends Terminal {
	public LogicalOperator( String spelling )
	{
		this.spelling = spelling;
	}
	public Object visit( Visitor v, Object arg )
	{
		return v.visitLogicalOperator( this, arg );
	}
}
