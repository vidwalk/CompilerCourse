package laba.ast;

import java.util.Vector;

public class HeaderList extends AST {
	public Vector<Header> headers = new Vector<Header>();
	
	public Object visit( Visitor v, Object arg )
	{
		return v.visitHeaderList( this, arg );
	}
}
