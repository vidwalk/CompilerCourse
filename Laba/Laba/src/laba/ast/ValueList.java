package laba.ast;

import java.util.ArrayList;
import java.util.Vector;

public class ValueList extends Terminal {
	public Vector<Expression> valueList = new Vector<Expression>();
	public Object visit( Visitor v, Object arg )
	{
		return v.visitValueList( this, arg );
	}
}
