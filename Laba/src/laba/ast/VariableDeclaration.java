package laba.ast;

public class VariableDeclaration extends Declaration {
public Identifier id;
public TypeIdentifier type;
	
	public VariableDeclaration( TypeIdentifier type, Identifier id )
	{
		this.type = type;
		this.id = id;
	}

}
