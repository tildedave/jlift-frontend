/*
 * File          : JLiftFrontendMethodDeclDel.java
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

import java.util.Collection;
import java.util.Iterator;

import jliftfrontend.policy.LabelPolicyDescriptor;
import jliftfrontend.policy.PolicyDescriptor;

import polyglot.ast.Formal;
import polyglot.ast.JL_c;
import polyglot.ast.MethodDecl;
import polyglot.ast.MethodDecl_c;
import polyglot.ast.Node_c;
import polyglot.ast.TypeNode;
import polyglot.types.Flags;
import polyglot.util.CodeWriter;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.Translator;

public class JLiftFrontendMethodDeclDel extends JL_c {

	@Override
	public void translate(CodeWriter w, Translator tr) {
		MethodDecl_c md = (MethodDecl_c) node();
		prettyPrintHeader(md.flags(), w, tr);
		if (md.body() != null) {
			md.printSubStmt(md.body(), w, tr);
		}
		else {
			w.write(";");
		}
	}


	/** Write the method to an output file. */
	public void prettyPrintHeader(Flags flags, CodeWriter w, PrettyPrinter tr) {
		MethodDecl_c md = (MethodDecl_c) node();
		JLiftFrontendMethodDeclExt ext = (JLiftFrontendMethodDeclExt) md.ext();

		// copy/pasted from the header
		w.begin(0);
		w.write(flags.translate());
		((MethodDecl_c) md).print(md.returnType(), w, tr);
		w.allowBreak(2, 2, " ", 1);
		w.write(md.name());

		if (ext.beginAspect() != null) {
			w.write(getLabelStringForMethodPolicyDescriptor(ext.beginAspect()));
		}

		w.write("(");

		w.allowBreak(2, 2, "", 0);
		w.begin(0);

		for (Iterator i = md.formals().iterator(); i.hasNext(); ) {
			Formal f = (Formal) i.next();
			md.print(f, w, tr);

			if (i.hasNext()) {
				w.write(",");
				w.allowBreak(0, " ");
			}
		}

		w.end();
		w.write(")");
		
		if (ext.returnAspect() != null) {
			w.allowBreak(6, " ");
			w.write(": ");
			w.write(getLabelStringForMethodPolicyDescriptor(ext.returnAspect()));
		}

		if (! md.throwTypes().isEmpty()) {
			w.allowBreak(6);
			w.write("throws ");

			for (Iterator i = md.throwTypes().iterator(); i.hasNext(); ) {
				TypeNode tn = (TypeNode) i.next();
				md.print(tn, w, tr);

				if (i.hasNext()) {
					w.write(",");
					w.allowBreak(4, " ");
				}
			}
		}

		w.end();
	}


	private String getLabelStringForMethodPolicyDescriptor(
			Collection<PolicyDescriptor> beginAspect) {
		return ((LabelPolicyDescriptor) beginAspect.iterator().next()).getLabelString();
	}
}
