package laba.ast;

public interface Visitor {
public Object visitProgram(Program p, Object arg);

public Object visitDeclarations(Declarations d, Object arg);

public Object visitVariableDeclaration(VariableDeclaration v, Object arg);

public Object visitArrayDeclaration(ArrayDeclaration a, Object arg);

public Object visitDefDeclaration(DefDeclaration d, Object arg);

public Object visitStatements(Statements s, Object arg);

public Object visitAssignStatement(AssignStatement a, Object arg);

public Object visitValueListAssigntStatement(ValueListAssignStatement v, Object arg);

public Object visitReadAssignStatement(ReadAssignStatement r, Object arg);

public Object visitIfStatement(IfStatement i, Object arg);

public Object visitWhileStatement(WhileStatement w, Object arg);

public Object visitCallStatement(CallStatement c, Object arg);

public Object visitPrintStatement(PrintStatement p, Object arg);

public Object visitPrimaryExpression(PrimaryExpression p, Object arg);

public Object visitLogicalExpression(LogicalExpression l, Object arg);

public Object visitValueExpression(ValueExpression v, Object arg);

public Object visitIdentifierExpression(IdentifierExpression i, Object arg);

public Object visitArrayAccessExpression(ArrayAccessExpression a, Object arg);

public Object visitHeaderList(HeaderList h, Object arg);

public Object visitExpressionList(ExpressionList e, Object arg);

public Object visitHeader(Header h, Object arg);

public Object visitIdentifier(Identifier i, Object arg);

public Object visitValue(Value v, Object arg);

public Object visitOperator(Operator o, Object arg);

public Object visitLogicalOperator(LogicalOperator l, Object arg);

public Object visitValueList(ValueList v, Object arg);

public Object visitTypeIdentifier(TypeIdentifier t, Object arg);

public Object visitUnaryExpression(UnaryExpression unaryExpression, Object arg);
}
