package laba.ast;

import laba.Address;

public class ArrayDeclaration extends Declaration {
	public Identifier id;
	public Value sizeArray;
	public TypeIdentifier type;
	public TypeIdentifier typeDataArray;
	public Address address;
		public ArrayDeclaration( TypeIdentifier type, Value sizeArray, Identifier id, TypeIdentifier typeDataArray )
		{
			this.type = type;
			this.sizeArray = sizeArray;
			this.id = id;
			this.typeDataArray = typeDataArray;
		}
		
		public Object visit( Visitor v, Object arg )
		{
			return v.visitArrayDeclaration( this, arg );
		}
}
