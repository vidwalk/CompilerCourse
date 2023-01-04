package laba;

import static laba.TokenKind.*;

public class Scanner {
	private SourceFile source;

	private char currentChar;
	private StringBuffer currentSpelling = new StringBuffer();

	public Scanner(SourceFile source) {
		this.source = source;

		currentChar = source.getSource();
	}

	private void takeIt() {
		currentSpelling.append(currentChar);
		currentChar = source.getSource();
	}

	private boolean isLetter(char c) {
		return c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z';
	}

	private boolean isDigit(char c) {
		return c >= '0' && c <= '9';
	}

	private void scanSeparator() {
		switch (currentChar) {
		case '#':
			takeIt();
			while (currentChar != SourceFile.EOL && currentChar != SourceFile.EOT)
				takeIt();

			if (currentChar == SourceFile.EOL)
				takeIt();
			break;

		case ' ':
		case '\n':
		case '\r':
		case '\t':
			takeIt();
			break;
		}
	}

	private TokenKind scanToken() {
		 if (isLetter(currentChar)) {

			takeIt();
			while (isLetter(currentChar) || isDigit(currentChar))
				takeIt();

			return IDENTIFIER;

		} else if (isDigit(currentChar)) {
			takeIt();
			// add condition digit is 8
			if (currentChar == '=') {
				takeIt();
				return OPERATOR;
			}
			while (isDigit(currentChar))
				takeIt();
			if (currentChar == '.') {
				takeIt();
				while (isDigit(currentChar))
					takeIt();
				return DOUBLELITERAL;
			}
			return INTEGERLITERAL;

		}
		switch (currentChar) {
		case '+':
		case '-':
		case '*':
		case '/':
			takeIt();
			return OPERATOR;
			
		case '<':
		case '>':
			takeIt();
			return LOGICALOPERATOR;
		case '=':
			takeIt();
			//Might have problems because of this one. Demo it. First = is taken and then the next 2 ones are taken to form ==8. Might be big problems with this one
			takeIt();
			takeIt();
			return LOGICALOPERATOR;
			
		case ';':
			takeIt();
			return SEMICOLON;
			
		case '(':
			takeIt();
			return LEFTPARAN;
			
		case ')':
			takeIt();
			return RIGHTPARAN;
			
		case '[':
			takeIt();
			return LEFTBRACKET;
			
		case ']':
			takeIt();
			return RIGHTBRACKET;
			
		case '~':
			takeIt();
			return TILDE;
			
		case ',':
			takeIt();
			return COMMA;
			
		case SourceFile.EOT:
			return EOT;

		default:
			takeIt();
			return ERROR;
		}
	}

	public Token scan() {
		while (currentChar == '#' || currentChar == '\n' || currentChar == '\r' || currentChar == '\t'
				|| currentChar == ' ')
			scanSeparator();

		currentSpelling = new StringBuffer("");
		TokenKind kind = scanToken();

		return new Token(kind, new String(currentSpelling));
	}
}
