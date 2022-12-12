package laba.ast;

public class AssignStatement extends Statement {
	public Identifier id;
	public Operator operator;
	public Expression expr;
	
	public AssignStatement(Identifier id, Operator operator ,Expression expr) 
	{
		this.id = id;
		this.operator = operator;
		this.expr = expr;
	}
}
