package laba.ast;

import java.util.Vector;

public class ExpressionList extends AST {
	public Vector<Expression> expressions = new Vector<Expression>();
	
	public Object visit( Visitor v, Object arg )
	{
		return v.visitExpressionList( this, arg );
	}
}
