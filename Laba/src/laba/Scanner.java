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

	private boolean isTrueBoolean() {
		char[] trueChars = "rue".toCharArray();
		if (currentChar == 't') {
			for (char ch : trueChars) {
				takeIt();
				if (currentChar != ch)
					return false;
			}
		} else
			return false;
		takeIt();
		return true;
	}

	private boolean isFalseBoolean() {
		char[] falseChars = "alse".toCharArray();
		if (currentChar == 'f') {
			for (char ch : falseChars) {
				takeIt();
				if (currentChar != ch)
					return false;
			}
		} else
			return false;
		takeIt();
		return true;
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

	private boolean isBool() {
		char[] falseChars = "ool".toCharArray();
		if (currentChar == 'b') {
			for (char ch : falseChars) {
				takeIt();
				if (currentChar != ch)
					return false;
			}
		} else
			return false;
		takeIt();
		return true;
	}

	private boolean isInt() {
		char[] falseChars = "nt".toCharArray();
		if (currentChar == 'i') {
			for (char ch : falseChars) {
				takeIt();
				if (currentChar != ch)
					return false;
			}
		} else
			return false;
		takeIt();
		return true;
	}

	private boolean isDouble() {
		char[] falseChars = "ouble".toCharArray();
		if (currentChar == 'd') {
			for (char ch : falseChars) {
				takeIt();
				if (currentChar != ch)
					return false;
			}
		} else
			return false;
		takeIt();
		return true;
	}

	private TokenKind scanToken() {
		if (isBool()) {
			return BOOL;
		}
		else if (isInt()) {
			return INT;
		}
		else if (isDouble()) {
			return DOUBLE;
		} else if (isTrueBoolean() || isFalseBoolean()) {
			return BOOLLITERAL;
		} else if (isLetter(currentChar)) {

			takeIt();
			while (isLetter(currentChar) || isDigit(currentChar))
				takeIt();

			return IDENTIFIER;

		} else if (isDigit(currentChar)) {
			takeIt();
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

		case ';':
			takeIt();
			return SEMICOLON;

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
