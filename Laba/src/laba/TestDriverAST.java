package laba;

import javax.swing.*;
import laba.ast.*;
public class TestDriverAST {
	private static final String EXAMPLES_DIR = "C:\\Users\\Claudiu\\Dropbox\\CMC\\examples";

	public static void main(String[] args) {
		JFileChooser fc = new JFileChooser( EXAMPLES_DIR );
		char check;
		if( fc.showOpenDialog( null ) == fc.APPROVE_OPTION ) {
			SourceFile in = new SourceFile( fc.getSelectedFile().getAbsolutePath() );
			Scanner s = new Scanner( in );
			ParserAST p = new ParserAST( s );
			AST ast = p.parseProgram();
			
			new ASTViewer( ast );
		}
	}

}
