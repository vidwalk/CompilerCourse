package laba;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
	

private static final TokenKind[] KEYWORDS = { DECS, SCED, IF,ELSE, WHILE, DEF, BOOL, INT, DOUBLE,TRUE, FALSE,TILDE, ARRAY, CALL,PRINT,READ };

	
}
