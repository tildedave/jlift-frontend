/*
 * File          : JLiftFrontendClassDeclDel.java
 * Project       : jlift-frontend
 * Description   : <insert description here>
 * Author(s)     : dhking
 *
 * Created       : Feb 11, 2008 11:33:25 AM 
 *
 * Copyright (c) 2007-2008 The Pennsylvania State University
 * Systems and Internet Infrastructure Security Laboratory
 *
 */
package jliftfrontend.extension;

import java.util.Iterator;

import polyglot.ast.ClassDecl_c;
import polyglot.ast.JL_c;
import polyglot.ast.TypeNode;
import polyglot.util.CodeWriter;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.Translator;

public class JLiftFrontendClassDeclDel extends JL_c {

	@Override
	public void translate(CodeWriter w, Translator tr) {
		JLiftFrontendClassDeclExt ext = (JLiftFrontendClassDeclExt) node().ext();
		ClassDecl_c cd = (ClassDecl_c) node();
		if (ext.getParamLabel() != null) {
			prettyPrintHeader(w, tr);
			cd.print(cd.body(), w, tr);
			cd.prettyPrintFooter(w, tr);
		} else {
			super.translate(w, tr);
		}
	}

	public void prettyPrintHeader(CodeWriter w, Translator tr) {
		ClassDecl_c cd = (ClassDecl_c) node();
		JLiftFrontendClassDeclExt ext = (JLiftFrontendClassDeclExt) cd.ext();  

		w.begin(0);
		if (cd.flags().isInterface()) {
			w.write(cd.flags().clearInterface().clearAbstract().translate());
		}
		else {
			w.write(cd.flags().translate());
		}

		if (cd.flags().isInterface()) {
			w.write("interface ");
		}
		else {
			w.write("class ");
		}

		tr.print(cd, cd.id(), w);

		// might need to add a parameterized label now
		if (ext.getParamLabel() != null)
			w.write("[label " + ext.getParamLabel() + "]");


		if (cd.superClass() != null) {
			w.allowBreak(0);
			w.write("extends ");
			cd.print(cd.superClass(), w, tr);
		}

		if (! cd.interfaces().isEmpty()) {
			w.allowBreak(2);
			if (cd.flags().isInterface()) {
				w.write("extends ");
			}
			else {
				w.write("implements ");
			}

			w.begin(0);
			for (Iterator i = cd.interfaces().iterator(); i.hasNext(); ) {
				TypeNode tn = (TypeNode) i.next();
				cd.print(tn, w, tr);

				if (i.hasNext()) {
					w.write(",");
					w.allowBreak(0);
				}
			}
			w.end();
		}
		w.unifiedBreak(0);
		w.end();
		w.write("{");
	}
}
