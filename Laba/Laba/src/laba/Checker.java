package laba;

import laba.ast.*;
import java.util.*;
import static laba.TypeKind.*;

public class Checker implements Visitor {
	private IdentificationTable idTable = new IdentificationTable();

	public void check(Program p) {
		p.visit(this, null);
	}

	@Override
	public Object visitProgram(Program p, Object arg) {

		p.decs.visit(this, null);
		p.stats.visit(this, null);

		return null;
	}

	@Override
	public Object visitDeclarations(Declarations d, Object arg) {
		for (Declaration decl : d.dec)
			decl.visit(this, null);

		return null;
	}

	@Override
	public Object visitArrayDeclaration(ArrayDeclaration a, Object arg) {
		String id = (String) a.id.visit(this, null);

		idTable.enter(id, a);

		String type = (String) a.type.visit(this, null);
		String size = (String) a.sizeArray.visit(this, null);

		return null;
	}

	@Override
	public Object visitStatements(Statements s, Object arg) {
		for (Statement stat : s.stat)
			stat.visit(this, null);

		return null;
	}

	@Override
	public Object visitValue(Value v, Object arg) {
		return v.spelling;
	}

	@Override
	public Object visitIdentifier(Identifier i, Object arg) {
		return i.spelling;
	}

	@Override
	public Object visitTypeIdentifier(TypeIdentifier ti, Object arg) {
		if (ti.spelling.equals("bool")) {
			return BOOL;
		} else if (ti.spelling.equals("int")) {
			return INT;
		} else if (ti.spelling.equals("double")) {
			return DOUBLE;
		}
		return null;
	}

	@Override
	public Object visitVariableDeclaration(VariableDeclaration v, Object arg) {
		String id = (String) v.id.visit(this, null);

		idTable.enter(id, v);

		return null;
	}

	@Override
	public Object visitDefDeclaration(DefDeclaration d, Object arg) {
		String id = (String) d.idDef.visit(this, null);

		idTable.enter(id, d);
		if(d.headerList !=null)
		d.headerList.visit(this, null);
		d.statements.visit(this, null);

		return null;
	}

	@Override
	public Object visitAssignStatement(AssignStatement a, Object arg) {
		String id = (String) a.idE.name.visit(this, null);

		Declaration d = idTable.retrieve(id);
		if (d == null)
			System.out.println(id + " is not declared");
		else if (!(d instanceof VariableDeclaration) && !(d instanceof Header))
			System.out.println(id + " is not a variable");
		else {
			if(d instanceof VariableDeclaration) {
			VariableDeclaration v = (VariableDeclaration) d;
			a.idE.decl = v;
			TypeKind t = (TypeKind) a.expr.visit(this, null);
			if (t != null) {
				if (!(t.getSpelling().equals(v.type.spelling))) {
					System.out.println("Expression is of wrong type");
				}
			}}
			else if (d instanceof Header) {
				Header v = (Header) d;
				a.idE.decl = v;
				TypeKind t = (TypeKind) a.expr.visit(this, null);
				if (t != null) {
					if (!(t.getSpelling().equals(v.type.spelling))) {
						System.out.println("Expression is of wrong type");
					}
				}
			}
		}

		return null;
	}

	@Override
	public Object visitValueListAssigntStatement(ValueListAssignStatement v, Object arg) {
		String id = (String) v.idE.name.visit(this, null);

		Declaration d = idTable.retrieve(id);
		if (d == null)
			System.out.println(id + " is not declared");
		else if (!(d instanceof ArrayDeclaration))
			System.out.println(id + " is not an array");
		else {
			ArrayDeclaration a = (ArrayDeclaration) d;
			v.idE.decl = a;
			if (v.valueList.valueList.size() > Integer.parseInt(a.sizeArray.spelling)) {
				System.out.println("More values in value list than declared for array");
			}
			for (Expression e : v.valueList.valueList) {
				{
					TypeKind t = (TypeKind) e.visit(this, null);
					if (!(a.typeDataArray.spelling.equals(t.getSpelling()))) {
						System.out.println("Type in ValueList doesn't match declared type for array");
					}
				}
			}
		}
		v.valueList.visit(this, null);
		return null;
	}

	@Override
	public Object visitReadAssignStatement(ReadAssignStatement r, Object arg) {
		String id = (String) r.idE.name.visit(this, null);

		Declaration d = idTable.retrieve(id);
		if (d == null)
			System.out.println(id + " is not declared");
		else
			r.idE.visit(this, null);
		return null;
	}

	@Override
	public Object visitIfStatement(IfStatement i, Object arg) {
		i.ifPart.visit(this, null);
		i.thenPart.visit(this, null);
		if (i.elsePart != null)
			i.elsePart.visit(this, null);
		return null;
	}

	@Override
	public Object visitWhileStatement(WhileStatement w, Object arg) {
		w.whileExp.visit(this, null);
		w.thenPart.visit(this, null);
		return null;
	}

	@Override
	public Object visitCallStatement(CallStatement c, Object arg) {
		String id = (String) c.idE.visit(this, null);
		Vector<TypeKind> t = new Vector<TypeKind>();
		if(c.exprList !=null)
		t = (Vector<TypeKind>) (c.exprList.visit(this, null));

		Declaration d = idTable.retrieve(id);
		if (d == null)
			System.out.println(id + " is not declared");
		else if (!(d instanceof DefDeclaration))
			System.out.println(id + " is not a function");
		else {
			DefDeclaration def = (DefDeclaration) d;
			
			if (def.headerList.headers.size() != t.size()) {
				System.out.println("Incorrect number of arguments in call " + id);
			} else {
				for (int i = 0; i < t.size(); i++) {
					if (!(def.headerList.headers.get(i).type.spelling.equals(t.get(i).getSpelling()))) {
						System.out.println("Incorrect type in parameters for " + id);
					}
				}
			}

		}
		
		c.exprList.visit(this, null);
		return null;
	}

	@Override
	public Object visitPrintStatement(PrintStatement p, Object arg) {
		p.printExp.visit(this, null);
		return null;
	}

	@Override
	public Object visitPrimaryExpression(PrimaryExpression p, Object arg) {
		TypeKind t1 = (TypeKind) p.operand1.visit(this, null);
		TypeKind t2 = (TypeKind) p.operand2.visit(this, null);
		String operator = (String) p.operator.visit(this, null);
		if (t2 == null) {
			return t1;
		} else if (!(t1.getSpelling().equals(t2.getSpelling()))) {
			System.out.println("Type mismatch in expression");
		} else if ((operator.equals("8="))) {
			System.out.println("8= in primary expression");
		} else
			return t1;
		return null;
	}

	@Override
	public Object visitLogicalExpression(LogicalExpression l, Object arg) {
		TypeKind t1 = (TypeKind) l.operand1.visit(this, null);
		TypeKind t2 = (TypeKind) l.operand2.visit(this, null);
		l.operator.visit(this, null);
		if (t2 == null) {
			return t1;
		} else if (!(t1.getSpelling().equals(t2.getSpelling()))) {
			System.out.println("Type mismatch in expression");
		} else
			return t1;
		return null;
	}

	@Override
	public Object visitValueExpression(ValueExpression v, Object arg) {
		if (v.literal.type.equals("true") || v.literal.type.equals("false")) {
			return BOOL;
		} else if (v.literal.type.equals("int")) {
			return INT;
		} else if (v.literal.type.equals("double")) {
			return DOUBLE;
		}
		return null;
	}

	@Override
	public Object visitIdentifierExpression(IdentifierExpression i, Object arg) {
		String id = (String) i.name.visit(this, null);

		Declaration d = idTable.retrieve(id);
		if (d == null)
			System.out.println(id + " is not declared");
		else if (!(d instanceof VariableDeclaration) && !(d instanceof Header)&& !(d instanceof DefDeclaration))
			System.out.println(id + " is not a variable");
		else {
			if (d instanceof VariableDeclaration) {
				i.decl = (VariableDeclaration) d;
				TypeKind t = (TypeKind) ((VariableDeclaration)i.decl).type.visit(this, null);
				if (t != null) {
					return t;
				}
			}else if (d instanceof DefDeclaration) {
				i.decl = (DefDeclaration) d;
				return id;
			} else {
				Header h = (Header) d;
				i.decl = (Header) d;
				TypeKind t = (TypeKind) h.type.visit(this, null);
				if (t != null) {
					return t;
				}
			}
		}
		return id;
	}

	@Override
	public Object visitArrayAccessExpression(ArrayAccessExpression a, Object arg) {
		String id = (String) a.idE.name.visit(this, null);

		Declaration d = idTable.retrieve(id);
		if (d == null)
			System.out.println(id + " is not declared");
		else if (!(d instanceof ArrayDeclaration))
			System.out.println(id + " is not a variable");
		else {
			ArrayDeclaration v = (ArrayDeclaration) d;
			a.idE.decl = v;
			int accessValue = Integer.parseInt(a.value.spelling);
			int arraySize = Integer.parseInt(v.sizeArray.spelling);
			if (accessValue > (arraySize - 1)) {
				System.out.println("Access value out of range");
			}
			TypeKind t = (TypeKind) v.typeDataArray.visit(this, null);
			if (t != null) {
				return t;
			}
		}
		return null;
	}

	@Override
	public Object visitHeaderList(HeaderList h, Object arg) {
		for (Header header : h.headers)
			header.visit(this, null);
		return null;
	}

	@Override
	public Object visitExpressionList(ExpressionList e, Object arg) {
		Vector<TypeKind> t = new Vector<TypeKind>();
		if (!e.expressions.isEmpty()) {
			for (Expression expression : e.expressions) {
				if (expression != null)
					t.add((TypeKind) expression.visit(this, null));
			}
		}
		return t;
	}

	@Override
	public Object visitHeader(Header h, Object arg) {
		String id = (String) h.id.visit(this, null);
		idTable.enter(id, h);
		h.type.visit(this, null);
		return null;
	}

	@Override
	public Object visitOperator(Operator o, Object arg) {
		return o.spelling;
	}

	@Override
	public Object visitLogicalOperator(LogicalOperator l, Object arg) {
		return l.spelling;
	}

	@Override
	public Object visitValueList(ValueList v, Object arg) {
		for (Expression expression : v.valueList) {
			expression.visit(this, null);
		}
		return null;
	}

	@Override
	public Object visitUnaryExpression(UnaryExpression unaryExpression, Object arg) {
		if (unaryExpression.operator.spelling.equals("8=")) {
			System.out.println("8= in Unary Expression");
		} else if (unaryExpression.operator.spelling.equals("*") || unaryExpression.operator.spelling.equals("/")) {
			System.out.println("Multiplication or division in Unary Expression");
		} else {
			TypeKind t = (TypeKind) unaryExpression.operand.visit(this, null);
			if (t == BOOL) {
				System.out.println("Only int and double allowed in Unary Expression");
			}
			if (t != null)
				return t;
		}
		return null;
	}
}