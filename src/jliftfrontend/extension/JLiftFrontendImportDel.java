package jliftfrontend.extension;

import polyglot.ast.Import;
import polyglot.ast.JL_c;
import polyglot.main.Options;
import polyglot.util.CodeWriter;
import polyglot.visit.Translator;

public class JLiftFrontendImportDel extends JL_c {

	@Override
	public void translate(CodeWriter w, Translator tr) {
		Import i = (Import) node();
		if (! Options.global.fully_qualified_names) {
			w.write("import ");
			JLiftFrontendImportExt importExt = (JLiftFrontendImportExt) i.ext();
			String name = i.name();
			if (importExt.instanceImport()) {
				// add instance to last .
				int lasti = name.lastIndexOf(".");
				name = name.substring(0, lasti) + "." + "Inline" + name.substring(lasti + 1);
			}
			
			w.write(name);

			if (i.kind() == Import.PACKAGE) {
				w.write(".*");
			}

			w.write(";");
			w.newline(0);
		}
	}
}
