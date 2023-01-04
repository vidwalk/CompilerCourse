package laba;

public enum TokenKind {
	IDENTIFIER,
	INTEGERLITERAL("int"),
	TRUE("true"),
	FALSE("false"),
	DOUBLELITERAL("double"),
	OPERATOR,
	LOGICALOPERATOR,
	
	
	DECS("decs"),
	SCED("sced"),
	
	DEF("def8"),
	BOOL("bool"),
	INT("int"),
	DOUBLE("double"),
	ARRAY("arr"),
	
	IF("if8"),
	ELSE("else8"),
	WHILE("while8"),
	CALL("call8"),
	PRINT("plint"),
	READ("lead"),
	
	
	SEMICOLON(";"),
	LEFTPARAN("("),
	RIGHTPARAN(")"),
	LEFTBRACKET("["),
	RIGHTBRACKET("]"),
	TILDE("~"),
	COMMA(","),
	
	EOL,
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
