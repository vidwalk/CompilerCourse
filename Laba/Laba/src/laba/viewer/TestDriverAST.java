package laba.viewer;
import javax.swing.*;

import laba.Checker;
import laba.Encoder;
import laba.ParserAST;
import laba.Scanner;
import laba.SourceFile;
import laba.ast.*;
public class TestDriverAST {
	private static final String EXAMPLES_DIR = "C:\\Users\\Claudiu\\Dropbox\\CMC\\examples";

	public static void main(String[] args) {
		JFileChooser fc = new JFileChooser( EXAMPLES_DIR );
		char check;
		if( fc.showOpenDialog( null ) == fc.APPROVE_OPTION ) {
			String sourceName = fc.getSelectedFile().getAbsolutePath();
			SourceFile in = new SourceFile( sourceName );
			Scanner s = new Scanner( in );
			ParserAST p = new ParserAST( s );
			AST ast = p.parseProgram();
			Checker c = new Checker();
			Encoder e = new Encoder();
			
			new ASTViewer( ast );
			c.check( (Program) ast );
			e.encode( (Program) ast  );
			String targetName;
			if( sourceName.endsWith( ".txt" ) )
				targetName = sourceName.substring( 0, sourceName.length() - 4 ) + ".tam";
			else
				targetName = sourceName + ".tam";
			
			e.saveTargetProgram( targetName );
		}
	}

}
