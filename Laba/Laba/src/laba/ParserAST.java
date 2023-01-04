package laba;

import laba.ast.*;
import static laba.TokenKind.*;

import java.util.ArrayList;
import java.util.Vector;

public class ParserAST {
	private Scanner scan;

	public Token currentTerminal;

	public ParserAST(Scanner scan) {
		this.scan = scan;

		currentTerminal = scan.scan();
	}

	public Program parseProgram() {
		accept(DECS);
		Declarations decs = parseDeclarations();
		accept(SCED);
		Statements stats = parseStatements();
		return new Program(decs, stats);
	}

	private Declarations parseDeclarations() {
		Declarations decs = new Declarations();

		while (currentTerminal.kind == BOOL || currentTerminal.kind == INT || currentTerminal.kind == DOUBLE
				|| currentTerminal.kind == ARRAY || currentTerminal.kind == DEF)
			decs.dec.add(parseOneDeclaration());

		return decs;
	}

	private Declaration parseOneDeclaration() {
		switch (currentTerminal.kind) {
		case BOOL:
		case INT:
		case DOUBLE:
			TypeIdentifier type = parseTypeIdentifier();
			Identifier id = parseIdentifier();
			accept(SEMICOLON);
			return new VariableDeclaration(type, id);
		case ARRAY:

			TypeIdentifier typeArray = parseTypeIdentifier();
			accept(LEFTPARAN);

			Value sizeArray = null;
			if (currentTerminal.kind == INTEGERLITERAL) {
				sizeArray = parseValue();	
				
				accept(COMMA);
				TypeIdentifier typeDataArray = parseTypeIdentifier();
				accept(RIGHTPARAN);
				Identifier idArray = parseIdentifier();
				accept(SEMICOLON);
				return new ArrayDeclaration(typeArray, sizeArray, idArray,typeDataArray);
			}
		case DEF:
			accept(DEF);
			Identifier idDef = parseIdentifier();
			accept(LEFTPARAN);
			HeaderList headerList = new HeaderList();
			if(currentTerminal.kind != RIGHTPARAN)
			headerList = parseHeaderList();
			accept(RIGHTPARAN);
			accept(TILDE);
			Statements stats = parseStatements();
			accept(TILDE);
			accept(SEMICOLON);
			return new DefDeclaration(idDef, headerList, stats);
		default:
			System.out.println("var or func expected");
			return null;
		}
	}

	private HeaderList parseHeaderList() {
		TypeIdentifier type = parseTypeIdentifier();
		Identifier id = parseIdentifier();
		Header header = new Header(type, id);
		HeaderList headerList = new HeaderList();
		headerList.headers.add(header);
		while (currentTerminal.kind == COMMA) {
			currentTerminal = scan.scan();
			type = parseTypeIdentifier();
			id = parseIdentifier();
			header = new Header(type, id);
			headerList.headers.add(header);
		}
		return headerList;
	}

	private Identifier parseIdentifier() {
		if (currentTerminal.kind == IDENTIFIER) {

			Identifier res = new Identifier(currentTerminal.spelling);
			currentTerminal = scan.scan();

			return res;
		} else {
			System.out.println("Identifier expected");

			return new Identifier("???");
		}
	}

	private TypeIdentifier parseTypeIdentifier() {
		if (currentTerminal.kind == BOOL || currentTerminal.kind == INT || currentTerminal.kind == DOUBLE
				|| currentTerminal.kind == ARRAY) {

			TypeIdentifier res = new TypeIdentifier(currentTerminal.spelling);
			currentTerminal = scan.scan();

			return res;
		} else {
			System.out.println("TypeIdentifier expected");

			return new TypeIdentifier("???");
		}
	}

	private Statements parseStatements() {
		Statements stats = new Statements();

		while (currentTerminal.kind == IDENTIFIER || currentTerminal.kind == IF || currentTerminal.kind == WHILE
				|| currentTerminal.kind == PRINT || currentTerminal.kind == CALL)
			stats.stat.add(parseOneStatement());

		return stats;
	}

	private Statement parseOneStatement() {
		switch (currentTerminal.kind) {
		case IDENTIFIER:
			Identifier id = parseIdentifier();
			IdentifierExpression idE = new IdentifierExpression(id);
			Operator op = parseOperator();

			if (currentTerminal.kind == LEFTBRACKET && op.spelling.equals("8=")) {
				accept(LEFTBRACKET);
				ValueList valueList = parseValueList();
				accept(RIGHTBRACKET);
				accept(SEMICOLON);
				
				return new ValueListAssignStatement(idE, op, valueList);
			} else if (currentTerminal.kind == READ && op.spelling.equals("8=")) {
				accept(READ);
				accept(LEFTPARAN);
				accept(RIGHTPARAN);
				accept(SEMICOLON);
				return new ReadAssignStatement(idE, op);
			} else if (op.spelling.equals("8=")) {
				Expression expr = parseExpression();
				accept(SEMICOLON);
				
				return new AssignStatement(idE, op, expr);
			}

		case IF:
			accept(IF);

			Expression ifPart = parseLogicalExpression();

			accept(TILDE);
			Statements thenPart = parseStatements();
			accept(TILDE);
			Statements elsePart = null;

			if (currentTerminal.kind == ELSE) {
				accept(ELSE);
				accept(TILDE);
				elsePart = parseStatements();
				accept(TILDE);
			}

			accept(SEMICOLON);

			return new IfStatement(ifPart, thenPart, elsePart);
		case WHILE:
			accept(WHILE);
			Expression whileExp = parseLogicalExpression();
			accept(TILDE);
			Statements stats = parseStatements();
			accept(TILDE);
			accept(SEMICOLON);

			return new WhileStatement(whileExp, stats);
		case CALL:
			accept(CALL);
			Identifier idCall = parseIdentifier();
			IdentifierExpression idECall = new IdentifierExpression(idCall);
			accept(LEFTPARAN);
			ExpressionList exprList = new ExpressionList();
			if(currentTerminal.kind != RIGHTPARAN)
			exprList = parseExpressionList();
			accept(RIGHTPARAN);
			accept(SEMICOLON);
			return new CallStatement(idECall, exprList);
		case PRINT:
			accept(PRINT);
			accept(LEFTPARAN);
			Expression expr = parseExpression();
			accept(RIGHTPARAN);
			accept(SEMICOLON);
			return new PrintStatement(expr);
		default:
			System.out.println("Error in statement");
			return null;
		}
	}

	private ExpressionList parseExpressionList() {
		Expression expr = parseExpression();
		ExpressionList exprList = new ExpressionList();
		exprList.expressions.add(expr);
		while (currentTerminal.kind == COMMA) {
			currentTerminal = scan.scan();
			expr = parseExpression();
			exprList.expressions.add(expr);
		}
		return exprList;
	}

	private Expression parseExpression() {
		Expression res = parsePrimary();
		while (currentTerminal.kind == OPERATOR) {
			Operator op = parseOperator();
			Expression tmp = parsePrimary();

			res = new PrimaryExpression(op, res, tmp);
		}

		return res;
	}

	private Expression parseLogicalExpression() {
		Expression res = parseLogical();
		while (currentTerminal.kind == LOGICALOPERATOR) {
			Operator op = parseLogicalOperator();
			Expression tmp = parseLogical();

			res = new LogicalExpression(op, res, tmp);
		}

		return res;
	}

	private Expression parseLogical() {
		switch (currentTerminal.kind) {
		case IDENTIFIER:
			Identifier name = parseIdentifier();
			return new IdentifierExpression(name);

		case TRUE:
		case FALSE:
		case INTEGERLITERAL:
		case DOUBLELITERAL:
			Value value = parseValue();
			return new ValueExpression(value);
		default:
			System.out.println("Error in Logical");
			return null;
		}
	}

	private Operator parseLogicalOperator() {
		if (currentTerminal.kind == LOGICALOPERATOR) {
			Operator res = new Operator(currentTerminal.spelling);
			currentTerminal = scan.scan();

			return res;
		} else {
			System.out.println("LOGICAL Operator expected");

			return new Operator("???");
		}
	}

	private Expression parsePrimary() {
		switch (currentTerminal.kind) {
		case IDENTIFIER:
			Identifier name = parseIdentifier();
			IdentifierExpression idE = new IdentifierExpression(name);
			if (currentTerminal.kind == LEFTBRACKET) {
				accept(LEFTBRACKET);
				if (currentTerminal.kind == INTEGERLITERAL) {
					Value value = parseValue();
					accept(RIGHTBRACKET);
					return new ArrayAccessExpression(idE, value);
				}
				else
					System.out.println("Can't access array with negative value or any other type than array");
			}
			return new IdentifierExpression(name);

		case TRUE:
		case FALSE:
		case INTEGERLITERAL:
		case DOUBLELITERAL:
			Value value = parseValue();
			return new ValueExpression(value);
		case OPERATOR:
			Operator op = parseOperator();
			Expression exp = parsePrimary();
			return new UnaryExpression( op, exp );
		case LEFTPARAN:
			accept(LEFTPARAN);
			Expression res = parseExpression();
			accept(RIGHTPARAN);
			return res;
		default:
			System.out.println("Error in primary");
			return null;
		}
	}

	private ValueList parseValueList() {
		ValueList valueList = new ValueList();
		Expression value = null;
		if (currentTerminal.kind == OPERATOR ) {
			Operator op = parseOperator();
			Expression exp = parsePrimary();
			value = new UnaryExpression( op, exp );	
		}
		else
		{
			value = new ValueExpression(parseValue());
		}
		valueList.valueList.add(value);
		while (currentTerminal.kind == COMMA) {
			currentTerminal = scan.scan();
			if (currentTerminal.kind == OPERATOR ) {
				Operator op = parseOperator();
				Expression exp = parsePrimary();
				value = new UnaryExpression( op, exp );	
			}
			else
			{
				value =  new ValueExpression(parseValue());
			}
			valueList.valueList.add(value);
		}
		return valueList;
	}

	private Operator parseOperator() {
		if (currentTerminal.kind == OPERATOR ) {
			Operator res = new Operator(currentTerminal.spelling);
			currentTerminal = scan.scan();

			return res;
		} else {
			System.out.println("Operator expected");

			return new Operator("???");
		}
	}

	private Value parseValue() {
		if (currentTerminal.kind == TRUE || currentTerminal.kind == FALSE || currentTerminal.kind == INTEGERLITERAL
				|| currentTerminal.kind == DOUBLELITERAL) {
			// If the type of value is needed add here
			Value res = new Value(currentTerminal.spelling,currentTerminal.kind.getSpelling());
			currentTerminal = scan.scan();
			
			return res;
		} else {
			System.out.println("Operator expected");

			return new Value("???","???");
		}
	}

	private boolean accept(TokenKind expected) {
		if (currentTerminal.kind == expected) {
			currentTerminal = scan.scan();
			return true;
		} else {
			System.out.println("Expected token of kind " + expected);
			return false;
		}
	}
}
