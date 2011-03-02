package jliftfrontend.extension;

import polyglot.ast.Expr;
import polyglot.ast.Id_c;
import polyglot.ast.JL_c;
import polyglot.ast.LocalDecl_c;
import polyglot.ast.TypeNode;
import polyglot.types.Flags;
import polyglot.util.CodeWriter;
import polyglot.visit.PrettyPrinter;

public class JLiftFrontendLocalDeclDel extends JL_c {

	@Override
	public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
		LocalDecl_c ld = (LocalDecl_c) this.node();
        boolean printSemi = tr.appendSemicolon(true);
        boolean printType = tr.printType(true);
        
        Flags flags = ld.flags();
        TypeNode type = ld.type();
        String name = ld.name();
        
        if (name.equals("to"))
        	name += "_";

        w.write(flags.translate());
        if (printType) {
            ld.print(type, w, tr);
            w.write(" ");
        }
        tr.print(ld, new Id_c(ld.position(),name), w);

        Expr init = ld.init();
        
        if (init != null) {
            w.write(" =");
            w.allowBreak(2, " ");
            ld.print(init, w, tr);
        }

        if (printSemi) {
            w.write(";");
        }

        tr.printType(printType);
        tr.appendSemicolon(printSemi);
	}	
}
