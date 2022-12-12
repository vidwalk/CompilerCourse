package laba;

public enum TokenKind {
	IDENTIFIER,
	INTEGERLITERAL,
	BOOLLITERAL,
	DOUBLELITERAL,
	OPERATOR,
	
	DECS("decs"),
	SCED("sced"),
	
	BOOL("bool"),
	INT("int"),
	DOUBLE("double"),
	SEMICOLON(";"),
	LEFTPARAN(")"),
	RIGHTPARAN(")"),
	
	EOT,
	
	ERROR;
private String spelling = null;
	
	
	private TokenKind()
	{
	}
	
	
	private TokenKind( String spelling )
	{
		this.spelling = spelling;
	}
	
	
	public String getSpelling()
	{
		return spelling;
	}
}
