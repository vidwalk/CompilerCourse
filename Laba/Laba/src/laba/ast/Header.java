package laba.ast;

import laba.Address;

public class Header extends Declaration {
	public Identifier id;
	public TypeIdentifier type;
	public Address adr;
		
		public Header( TypeIdentifier type, Identifier id )
		{
			this.type = type;
			this.id = id;
		}
		
		public Object visit( Visitor v, Object arg )
		{
			return v.visitHeader( this, arg );
		}
}
