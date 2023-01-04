package laba.ast;

public class PrintStatement extends Statement {
	public Expression printExp;
	public PrintStatement(Expression printExp)
	{
		this.printExp = printExp;
	}
	public Object visit( Visitor v, Object arg )
	{
		return v.visitPrintStatement( this, arg );
	}
}
