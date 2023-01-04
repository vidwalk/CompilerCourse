package laba.ast;

import java.util.Vector;

public class Declarations extends AST {
	public Vector<Declaration> dec = new Vector<Declaration>();
	
	public Object visit( Visitor v, Object arg )
	{
		return v.visitDeclarations( this, arg );
	}
}
