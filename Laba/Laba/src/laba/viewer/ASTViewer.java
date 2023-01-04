package laba.viewer;

import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import laba.ast.*;
public class ASTViewer
extends JFrame
{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private static final Font NODE_FONT = new Font( "Verdana", Font.PLAIN, 24 );


public ASTViewer( AST ast )
{
	super( "Abstract Syntax Tree" );
	
	DefaultMutableTreeNode root = createTree( ast );
	JTree tree = new JTree( root );
	DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
	renderer.setFont( NODE_FONT );
	tree.setCellRenderer( renderer );
	
	add( new JScrollPane( tree ) );
	
	setSize( 1024, 768 );
	setVisible( true );
	
	setDefaultCloseOperation( EXIT_ON_CLOSE );
}


private DefaultMutableTreeNode createTree( AST ast )
{
	DefaultMutableTreeNode node = new DefaultMutableTreeNode( "*** WHAT??? ***" );
	
	if( ast == null )
		node.setUserObject( "*** NULL ***" );
	else if( ast instanceof Program ) {
		node.setUserObject( "Program" );
		node.add( createTree( ((Program)ast).decs ) );
		node.add( createTree( ((Program)ast).stats ) );
	}  else if( ast instanceof Declarations ) {
		node.setUserObject( "Declarations" );
		
		for( Declaration d: ((Declarations)ast).dec )
			node.add( createTree( d ) );
	} else if( ast instanceof VariableDeclaration ) {
		node.setUserObject( "VariableDeclaration" );
		node.add( createTree( ((VariableDeclaration)ast).type ) );
		node.add( createTree( ((VariableDeclaration)ast).id ) );
	}else if( ast instanceof DefDeclaration ) {
		node.setUserObject( "DefDeclaration" );
		node.add( createTree( ((DefDeclaration)ast).idDef ) );
		node.add( createTree( ((DefDeclaration)ast).headerList ) );
		node.add( createTree( ((DefDeclaration)ast).statements ) );
	}else if( ast instanceof Header ) {
		node.setUserObject( "Header" );
		node.add( createTree( ((Header)ast).type ) );
		node.add( createTree( ((Header)ast).id ) );
	}else if( ast instanceof ArrayDeclaration ) {
		node.setUserObject( "ArrayDeclaration" );
		node.add( createTree( ((ArrayDeclaration)ast).type ) );
		node.add( createTree( ((ArrayDeclaration)ast).sizeArray ) );
		node.add( createTree( ((ArrayDeclaration)ast).id ) );
		node.add( createTree( ((ArrayDeclaration)ast).typeDataArray ) );
	}  else if( ast instanceof Statements ) {
		node.setUserObject( "Statements" );
		
		for( Statement s: ((Statements)ast).stat )
			node.add( createTree( s ) );
	}
	else if( ast instanceof HeaderList ) {
		node.setUserObject( "HeaderList" );
		
		for( Header h: ((HeaderList)ast).headers )
			node.add( createTree( h ) );
	}else if( ast instanceof ExpressionList ) {
		node.setUserObject( "ExpressionList" );
		
		for( Expression e: ((ExpressionList)ast).expressions )
			node.add( createTree( e ) );
	}else if( ast instanceof AssignStatement ) {
		node.setUserObject( "AssignStatement" );
		node.add( createTree( ((AssignStatement)ast).idE ) );
		node.add( createTree( ((AssignStatement)ast).operator ) );
		node.add( createTree( ((AssignStatement)ast).expr ) );
	}else if( ast instanceof ValueListAssignStatement ) {
		node.setUserObject( "ValueListAssignStatement" );
		node.add( createTree( ((ValueListAssignStatement)ast).idE ) );
		node.add( createTree( ((ValueListAssignStatement)ast).operator ) );
		node.add( createTree( ((ValueListAssignStatement)ast).valueList ) );
	
	}else if( ast instanceof ReadAssignStatement ) {
		node.setUserObject( "ReadAssignStatement" );
		node.add( createTree( ((ReadAssignStatement)ast).idE ) );
		node.add( createTree( ((ReadAssignStatement)ast).op ) );
	}else if( ast instanceof ValueList ) {
		node.setUserObject( "ValueList" );
		
		for( Expression v: ((ValueList)ast).valueList )
			node.add( createTree( v ) );}
	else if( ast instanceof PrimaryExpression ) {
		node.setUserObject( "PrimaryExpression" );
		node.add( createTree( ((PrimaryExpression)ast).operator ) );
		node.add( createTree( ((PrimaryExpression)ast).operand1 ) );
		node.add( createTree( ((PrimaryExpression)ast).operand2 ) );
	}else if( ast instanceof UnaryExpression ) {
		node.setUserObject( "UnaryExpression" );
		node.add( createTree( ((UnaryExpression)ast).operator ) );
		node.add( createTree( ((UnaryExpression)ast).operand ) );
	}else if( ast instanceof IfStatement ) {
		node.setUserObject( "IfStatement" );
		node.add( createTree( ((IfStatement)ast).ifPart ) );
		node.add( createTree( ((IfStatement)ast).thenPart ) );
		node.add( createTree( ((IfStatement)ast).elsePart ) );
	}else if( ast instanceof PrintStatement ) {
		node.setUserObject( "PrintStatement" );
		node.add( createTree( ((PrintStatement)ast).printExp ) );
	}else if( ast instanceof WhileStatement ) {
		node.setUserObject( "WhileStatement" );
		node.add( createTree( ((WhileStatement)ast).whileExp ) );
		node.add( createTree( ((WhileStatement)ast).thenPart ) );
	}else if( ast instanceof CallStatement ) {
		node.setUserObject( "CallStatement" );
		node.add( createTree( ((CallStatement)ast).idE ) );
		node.add( createTree( ((CallStatement)ast).exprList ) );
	}else if( ast instanceof LogicalExpression ) {
		node.setUserObject( "LogicalExpression" );
		node.add( createTree( ((LogicalExpression)ast).operator ) );
		node.add( createTree( ((LogicalExpression)ast).operand1 ) );
		node.add( createTree( ((LogicalExpression)ast).operand2 ) );
	}else if( ast instanceof ValueExpression ) {
		node.setUserObject( "ValueExpression" );
		node.add( createTree( ((ValueExpression)ast).literal ) );
	}else if( ast instanceof IdentifierExpression ) {
		node.setUserObject( "IdentifierExpression" );
		node.add( createTree( ((IdentifierExpression)ast).name ) );
	}else if( ast instanceof ArrayAccessExpression ) {
		node.setUserObject( "ArrayAccessExpression" );
		node.add( createTree( ((ArrayAccessExpression)ast).idE ) );
		node.add( createTree( ((ArrayAccessExpression)ast).value ) );
	} else if( ast instanceof TypeIdentifier ) {
		node.setUserObject( "TypeIdentifier " + ((TypeIdentifier)ast).spelling );
	} else if( ast instanceof Identifier ) {
		node.setUserObject( "Identifier " + ((Identifier)ast).spelling );
	} else if( ast instanceof Value ) {
		node.setUserObject( "Value " + ((Value)ast).spelling );
	} else if( ast instanceof Operator ) {
		node.setUserObject( "Operator " + ((Operator)ast).spelling );
	}
		
		
	return node;
}
}