package laba.ast;

public class Value extends Terminal {
	public String type;
	public Value( String spelling, String type )
	{
		this.spelling = spelling;
		this.type = type;
	}
	public Object visit( Visitor v, Object arg )
	{
		return v.visitValue( this, arg );
	}
}
