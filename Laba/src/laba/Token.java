package laba;

import static laba.TokenKind.*;

public class Token {
	public TokenKind kind;
	public String spelling;
	
	
	public Token( TokenKind kind, String spelling )
	{
		this.kind = kind;
		this.spelling = spelling;
		
		if( kind == IDENTIFIER )
			for( TokenKind tk: KEYWORDS )
				if( spelling.equals( tk.getSpelling() ) ) {
					this.kind = tk;
					break;
				}
	}
	
	public boolean isAssignOperator()
	{
		if( kind == OPERATOR )
			return containsOperator( spelling, ASSIGNOPS );
		else
			return false;
	}
	
	private boolean containsOperator( String spelling, String OPS[] )
	{
		for( int i = 0; i < OPS.length; ++i )
			if( spelling.equals( OPS[i] ) )
				return true;
				
		return false;
	}
	
	private boolean isTypeIdentifier(String spelling, String TypeIdentifier[]) {
		for( int i = 0; i < TypeIdentifier.length; ++i )
			if( spelling.equals( TypeIdentifier[i] ) )
				return true;
				
		return false;
	}
	
private static final TokenKind[] KEYWORDS = { DECS, SCED };
	
	
	private static final String ASSIGNOPS[] =
	{
		"8=",
	};

	
	private static final String ADDOPS[] =
	{
		"+",
		"-",
	};

	
	private static final String MULOPS[] =
	{
		"*",
		"/",
	};
	
	private static final String TYPEIDENTIFIER[]= {
	"bool",
	};
	
}
