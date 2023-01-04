package laba.ast;

import laba.Address;

public class DefDeclaration extends Declaration {
	public Identifier idDef;
	public HeaderList headerList;
	public Statements statements;
	public Address adr;
	public DefDeclaration(Identifier idDef,HeaderList headerList,Statements statements) {
		this.idDef=idDef;
		this.headerList=headerList;
		this.statements = statements;
	}
	
	public Object visit( Visitor v, Object arg )
	{
		return v.visitDefDeclaration( this, arg );
	}
}
