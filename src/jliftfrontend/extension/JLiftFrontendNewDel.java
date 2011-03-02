package jliftfrontend.extension;

import polyglot.ast.JL_c;
import polyglot.ast.New_c;
import polyglot.ast.Special;
import polyglot.util.CodeWriter;
import polyglot.visit.PrettyPrinter;

public class JLiftFrontendNewDel extends JL_c {

	@Override
	public void prettyPrint(CodeWriter w, PrettyPrinter pp) {
		New_c newClass = (New_c) this.node();
		if (newClass.qualifier() instanceof Special) {
			newClass.qualifier(null).del().prettyPrint(w, pp);
		}
		else {
			super.prettyPrint(w, pp);
		}
	}
	
}
