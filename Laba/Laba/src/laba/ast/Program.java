package laba.ast;

public class Program extends AST {
	public Declarations decs;
	public Statements stats;
	
	
	public Program( Declarations decs, Statements stats )
	{
		this.decs = decs;
		this.stats = stats;
	}
	public Object visit( Visitor v, Object arg )
	{
		return v.visitProgram( this, arg );
	}
}
