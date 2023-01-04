package laba.ast;

import laba.Address;

public class VariableDeclaration extends Declaration {
public Identifier id;
public TypeIdentifier type;
public Address adr;
	
	public VariableDeclaration( TypeIdentifier type, Identifier id )
	{
		this.type = type;
		this.id = id;
	}
	public Object visit( Visitor v, Object arg )
	{
		return v.visitVariableDeclaration( this, arg );
	}
}
