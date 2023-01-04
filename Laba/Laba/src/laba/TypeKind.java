package laba;

public enum TypeKind {
	BOOL("bool"),
	INT("int"),
	DOUBLE("double");
	
private String spelling = null;
	
	
	private TypeKind()
	{
	}
	
	
	private TypeKind( String spelling )
	{
		this.spelling = spelling;
	}
	
	
	public String getSpelling()
	{
		return spelling;
	}
}
