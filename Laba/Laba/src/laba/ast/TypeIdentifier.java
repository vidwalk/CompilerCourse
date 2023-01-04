package laba.ast;

public class TypeIdentifier extends Terminal {
	public TypeIdentifier( String spelling )
	{
		this.spelling = spelling;
	}
	public Object visit( Visitor v, Object arg )
	{
		return v.visitTypeIdentifier( this, arg );
	}
}
