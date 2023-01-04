package laba;

import laba.TAM.*;

import java.io.*;
import laba.ast.ArrayAccessExpression;
import laba.ast.ArrayDeclaration;
import laba.ast.AssignStatement;
import laba.ast.CallStatement;
import laba.ast.Declaration;
import laba.ast.Declarations;
import laba.ast.DefDeclaration;
import laba.ast.Expression;
import laba.ast.ExpressionList;
import laba.ast.Header;
import laba.ast.HeaderList;
import laba.ast.Identifier;
import laba.ast.IdentifierExpression;
import laba.ast.IfStatement;
import laba.ast.LogicalExpression;
import laba.ast.LogicalOperator;
import laba.ast.Operator;
import laba.ast.PrimaryExpression;
import laba.ast.PrintStatement;
import laba.ast.Program;
import laba.ast.ReadAssignStatement;
import laba.ast.Statement;
import laba.ast.Statements;
import laba.ast.TypeIdentifier;
import laba.ast.UnaryExpression;
import laba.ast.Value;
import laba.ast.ValueExpression;
import laba.ast.ValueList;
import laba.ast.ValueListAssignStatement;
import laba.ast.VariableDeclaration;
import laba.ast.Visitor;
import laba.ast.WhileStatement;

public class Encoder implements Visitor {
	private int nextAdr = Machine.CB;

	private void emit(int op, int n, int r, int d) {
		if (n > 255) {
			System.out.println("Operand too long");
			n = 255;
		}

		Instruction instr = new Instruction();
		instr.op = op;
		instr.n = n;
		instr.r = r;
		instr.d = d;

		if (nextAdr >= Machine.PB)
			System.out.println("Program too large");
		else
			Machine.code[nextAdr++] = instr;
	}

	private void patch(int adr, int d) {
		Machine.code[adr].d = d;
	}

	public void saveTargetProgram(String fileName) {
		try {
			DataOutputStream out = new DataOutputStream(new FileOutputStream(fileName));

			for (int i = Machine.CB; i < nextAdr; ++i)
				Machine.code[i].write(out);

			out.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Trouble writing " + fileName);
		}
	}

	public void encode(Program p) {
		p.visit(this, null);
	}

	@Override
	public Object visitProgram(Program p, Object arg) {
		// The current address is saved so that we can do backpatching
		int before = nextAdr;
		// JUMP 0 is generated
		emit(Machine.JUMPop, 0, Machine.CB, 0);
		// Check declarations
		p.decs.visit(this, new Address());
		// JUMP n over the declarations with backpatching
		patch(before, nextAdr);
		// Check statements
		p.stats.visit(this, null);
		// Halt for end of program
		emit(Machine.HALTop, 0, 0, 0);

		return null;
	}

	@Override
	public Object visitDeclarations(Declarations d, Object arg) {

		// For each declaration, the displacement will be incremented by 1
		for (Declaration dec : d.dec)
			arg = dec.visit(this, arg);

		return null;
	}

	@Override
	public Object visitVariableDeclaration(VariableDeclaration v, Object arg) {
		// The address of the variable is retrieved and returned incremented by 1
		v.adr = (Address) arg;

		return new Address((Address) arg, 1);
	}

	@Override
	public Object visitArrayDeclaration(ArrayDeclaration a, Object arg) {
		// The address of the array is retrieved and returned incremented by 1
		a.address = (Address) arg;

		return new Address((Address) arg, 1);
	}

	@Override
	public Object visitDefDeclaration(DefDeclaration d, Object arg) {
		// The address of the declaration is set to the next address
		d.adr = new Address(nextAdr);
		// A new address with displacement 0 is set
		Address adr = new Address((Address) arg);
		// The size for the headers is used to set a negative displacement in LB
		int size = ((Integer) d.headerList.visit(this, adr)).intValue();
		d.headerList.visit(this, new Address(adr, -size));
		d.statements.visit(this, new Address(adr, Machine.linkDataSize));
		// it doesn't return anything of significance as there is no return in laba
		emit(Machine.RETURNop, 1, 0, size);
		return arg;
	}

	@Override
	public Object visitStatements(Statements s, Object arg) {
		// the visit has the second argument set to false as the result is not needed
		for (Statement stat : s.stat)
			stat.visit(this, false);
		return null;
	}

	@Override
	public Object visitAssignStatement(AssignStatement a, Object arg) {
		// Get the value of valueNeeded to be used in deciding if the value should be
		// loaded or not
		boolean valueNeeded = ((Boolean) arg).booleanValue();
		// get the spelling of the operator to be able to check if it is the right
		// operation
		String op = (String) a.operator.visit(this, null);

		if (op.equals("8=")) {
			// Get the address and then set that the value of the expression is needed with
			// true
			Address adr = (Address) a.idE.visit(this, false);
			a.expr.visit(this, true);

			emit(Machine.STOREop, 1, Machine.SBr, adr.displacement);

			if (valueNeeded)
				emit(Machine.LOADop, 1, Machine.SBr, adr.displacement);
		}
		return null;
	}

	@Override
	public Object visitValueListAssigntStatement(ValueListAssignStatement v, Object arg) {

		if (v.idE.decl instanceof ArrayDeclaration) { // get the address from the identifier of the array
			Address adr = (Address) v.idE.visit(this, false);
			v.valueList.visit(this, adr);
			return adr;
		}

		return null;
	}

	@Override
	public Object visitReadAssignStatement(ReadAssignStatement r, Object arg) {

		String op = (String) r.op.visit(this, null);

		if (op.equals("8=")) {
			// get the address
			Address adr = (Address) r.idE.visit(this, false);
			// load the data address so it knows where it should be stored
			emit(Machine.LOADAop, 0, Machine.SBr, adr.displacement);
			emit(Machine.CALLop, 0, Machine.PBr, Machine.getintDisplacement);
		}
		return null;

	}

	@Override
	public Object visitIfStatement(IfStatement i, Object arg) {
		// Load result of logical expression
		i.ifPart.visit(this, true);
		// save the address for backpatching
		int jump1Adr = nextAdr;
		// JumpIf that checks if the value is 0. If it is then it enters the if
		// statement
		emit(Machine.JUMPIFop, 1, Machine.CBr, 0);
		// if statements
		i.thenPart.visit(this, null);

		int jump2Adr = nextAdr;
		emit(Machine.JUMPop, 0, Machine.CBr, 0);
		// backpatching so it goes to if statements
		patch(jump1Adr, nextAdr);
		if (i.elsePart != null)
			i.elsePart.visit(this, null);
		// backpatching so it goes to else
		patch(jump2Adr, nextAdr);

		return null;
	}

	@Override
	public Object visitWhileStatement(WhileStatement w, Object arg) {
		// Save address
		int startAdr = nextAdr;
		// evaluate Logical Expression
		w.whileExp.visit(this, true);

		int jumpAdr = nextAdr;
		// If false then jump to after while. JUMPIF(0) 0
		emit(Machine.JUMPIFop, 0, Machine.CBr, 0);

		w.thenPart.visit(this, null);

		emit(Machine.JUMPop, 0, Machine.CBr, startAdr);

		// backpatch to the address after while because everything was created
		patch(jumpAdr, nextAdr);
		return null;
	}

	@Override
	public Object visitCallStatement(CallStatement c, Object arg) {
		// Go through arguments
		c.exprList.visit(this, null);

		Address adr = ((DefDeclaration) c.idE.decl).adr;
		// Get address and then call it
		emit(Machine.CALLop, Machine.SBr, Machine.CB, adr.displacement);
		// Pop Return
		emit(Machine.POPop, 0, 0, 1);
		return null;
	}

	@Override
	public Object visitPrintStatement(PrintStatement p, Object arg) {
		// Load result from print expression and print it
		p.printExp.visit(this, true);

		emit(Machine.CALLop, 0, Machine.PBr, Machine.putintDisplacement);
		emit(Machine.CALLop, 0, Machine.PBr, Machine.puteolDisplacement);
		return null;
	}

	@Override
	public Object visitPrimaryExpression(PrimaryExpression p, Object arg) {
		// if the value is needed check the operator and call the right function
		boolean valueNeeded = ((Boolean) arg).booleanValue();

		String op = (String) p.operator.visit(this, null);
		// if the value is needed then load the value of the operands
		p.operand1.visit(this, arg);
		p.operand2.visit(this, arg);

		if (valueNeeded)
			if (op.equals("+"))
				emit(Machine.CALLop, 0, Machine.PBr, Machine.addDisplacement);
			else if (op.equals("-"))
				emit(Machine.CALLop, 0, Machine.PBr, Machine.subDisplacement);
			else if (op.equals("*"))
				emit(Machine.CALLop, 0, Machine.PBr, Machine.multDisplacement);
			else if (op.equals("/"))
				emit(Machine.CALLop, 0, Machine.PBr, Machine.divDisplacement);
			else if (op.equals("%"))
				emit(Machine.CALLop, 0, Machine.PBr, Machine.modDisplacement);

		return null;
	}

	@Override
	public Object visitLogicalExpression(LogicalExpression l, Object arg) {
		// if the value is needed check the operator and call the right function
		boolean valueNeeded = ((Boolean) arg).booleanValue();

		String op = (String) l.operator.visit(this, null);
		// if the value is needed then load the value of the operands
		l.operand1.visit(this, arg);
		l.operand2.visit(this, arg);

		if (valueNeeded)
			if (op.equals("==8")) {
				// Push the n for the size of the words in equal. Get the value from the default
				// displacement
				Address adr = new Address();
				emit(Machine.PUSHop, 0, 0, adr.displacement);
				emit(Machine.CALLop, 0, Machine.PBr, Machine.eqDisplacement);
			} else if (op.equals("<"))
				emit(Machine.CALLop, 0, Machine.PBr, Machine.ltDisplacement);
			else if (op.equals(">"))
				emit(Machine.CALLop, 0, Machine.PBr, Machine.gtDisplacement);

		return null;
	}

	@Override
	public Object visitValueExpression(ValueExpression v, Object arg) {
		// If the arg is a boolean then the value is needed or not and we know to load
		if (arg instanceof Boolean) {
			boolean valueNeeded = ((Boolean) arg).booleanValue();

			Integer lit = (Integer) v.literal.visit(this, null);

			if (valueNeeded)
				emit(Machine.LOADLop, 1, 0, lit.intValue());
		}
		// If there arg is different from boolean, then we know it is a valuelist call
		// in which
		// the result has to be loaded and then stored in the appropriate function
		else {
			Integer lit = (Integer) v.literal.visit(this, null);
			emit(Machine.LOADLop, 1, 0, lit.intValue());
			return v.literal.visit(this, arg);
		}
		return null;
	}

	@Override
	public Object visitIdentifierExpression(IdentifierExpression i, Object arg) {
		// set address in declaration
		// If the value is needed then load it
		boolean valueNeeded = ((Boolean) arg).booleanValue();
		Address adr = null;
		if (i.decl instanceof VariableDeclaration) {
			adr = ((VariableDeclaration) i.decl).adr;
			if (valueNeeded)
				emit(Machine.LOADop, 1, Machine.SBr, adr.displacement);
		} else if (i.decl instanceof DefDeclaration) {
			adr = ((DefDeclaration) i.decl).adr;
		} else if (i.decl instanceof Header) {
			adr = ((Header) i.decl).adr;
			if (valueNeeded)
				emit(Machine.LOADop, 1, Machine.LBr, adr.displacement);
		} else if (i.decl instanceof ArrayDeclaration) {
			adr = ((ArrayDeclaration) i.decl).address;
		}

		return adr;
	}

	@Override
	public Object visitArrayAccessExpression(ArrayAccessExpression a, Object arg) {
		// get value at the address of the array + value in arrayaccess
		boolean valueNeeded = ((Boolean) arg).booleanValue();
		Address adr = null;
		if (a.idE.decl instanceof ArrayDeclaration) {
			adr = ((ArrayDeclaration) a.idE.decl).address;
			if (valueNeeded)
				emit(Machine.LOADop, 1, Machine.LBr, adr.displacement + Integer.parseInt(a.value.spelling));
		}
		return adr;
	}

	@Override
	public Object visitHeaderList(HeaderList h, Object arg) {
		// similar to declarations, but the size is used for LB

		int startDisplacement = ((Address) arg).displacement;

		for (Header header : h.headers)
			arg = header.visit(this, arg);

		Address adr = (Address) arg;
		int size = adr.displacement - startDisplacement;

		return size;
	}

	@Override
	public Object visitExpressionList(ExpressionList e, Object arg) {
		for (Expression exp : e.expressions)
			if (exp != null)
				exp.visit(this, true);
		return null;
	}

	@Override
	public Object visitHeader(Header h, Object arg) {
		h.adr = (Address) arg;

		return new Address((Address) arg, 1);
	}

	@Override
	public Object visitIdentifier(Identifier i, Object arg) {
		return null;
	}

	@Override
	public Object visitValue(Value v, Object arg) {
		// if the arg is an address, then store the value at the address (case for
		// valuelist call)
		// if the arg is something else, then return an integer based on the spelling
		if (arg instanceof Address) {
			Address adr = (Address) arg;
			emit(Machine.STOREop, 1, Machine.SBr, adr.displacement);
			return new Address((Address) arg, 1);
		} else {
			if (v.spelling.equals("true")) {
				return 1;
			} else if (v.spelling.equals("false")) {
				return 0;
			}
			return Integer.parseInt(v.spelling);
		}
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

		for (Expression e : v.valueList)
			arg = e.visit(this, arg);

		return null;
	}

	@Override
	public Object visitTypeIdentifier(TypeIdentifier t, Object arg) {

		return null;
	}

	@Override
	public Object visitUnaryExpression(UnaryExpression u, Object arg) {
		// Call function to set integer to negative
		boolean valueNeeded = ((Boolean) arg).booleanValue();

		String op = (String) u.operator.visit(this, null);
		u.operand.visit(this, arg);

		if (valueNeeded && op.equals("-"))
			emit(Machine.CALLop, 0, Machine.PBr, Machine.negDisplacement);
		return null;
	}

}
