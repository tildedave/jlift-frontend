package jliftfrontend.extension;

import polyglot.ast.Formal_c;
import polyglot.ast.Id_c;
import polyglot.ast.JL_c;
import polyglot.util.CodeWriter;
import polyglot.visit.PrettyPrinter;

public class JLiftFrontendFormalDel extends JL_c {
	/** Write the local to an output file. */
	public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
		Formal_c formal = (Formal_c) this.node();
		String nameToUse = formal.name();
		if (nameToUse.equals("to")) {
			nameToUse = nameToUse + "_";
		}
		
		w.write(formal.flags().translate());
	    formal.print(formal.type(), w, tr);
	    w.write(" ");
	    tr.print(formal, new Id_c(formal.position(),nameToUse), w);
	}
}