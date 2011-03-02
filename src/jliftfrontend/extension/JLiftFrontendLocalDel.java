package jliftfrontend.extension;

import polyglot.ast.Id_c;
import polyglot.ast.JL_c;
import polyglot.ast.Local_c;
import polyglot.util.CodeWriter;
import polyglot.visit.PrettyPrinter;

public class JLiftFrontendLocalDel extends JL_c {

	/** Write the local to an output file. */
	public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
		Local_c local = (Local_c) this.node();
		String nameToUse = local.name();
		if (nameToUse.equals("to")) {
			nameToUse = nameToUse + "_";
		}

		tr.print(local, new Id_c(local.position(), nameToUse), w);		
	}
}
