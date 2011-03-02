/*
 * File          : JLiftFrontendDelFactory_c.java
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
package jliftfrontend.ast;

import jliftfrontend.extension.JLiftFrontendCanonicalTypeNodeDel;
import jliftfrontend.extension.JLiftFrontendClassDeclDel;
import jliftfrontend.extension.JLiftFrontendFormalDel;
import jliftfrontend.extension.JLiftFrontendImportDel;
import jliftfrontend.extension.JLiftFrontendLocalDeclDel;
import jliftfrontend.extension.JLiftFrontendLocalDel;
import jliftfrontend.extension.JLiftFrontendMethodDeclDel;
import jliftfrontend.extension.JLiftFrontendNewDel;
import polyglot.ast.AbstractDelFactory_c;
import polyglot.ast.JL;

public class JLiftFrontendDelFactory_c extends AbstractDelFactory_c {

	@Override
	protected JL delMethodDeclImpl() {
		return new JLiftFrontendMethodDeclDel();
	}
	
	@Override
	protected JL delCanonicalTypeNodeImpl() {
		return new JLiftFrontendCanonicalTypeNodeDel();
	}
	
	@Override
	protected JL delClassDeclImpl() {
		return new JLiftFrontendClassDeclDel();
	}
	
	@Override
	protected JL delImportImpl() {
		return new JLiftFrontendImportDel();
	}
	
	@Override
	protected JL delNewImpl() {
		return new JLiftFrontendNewDel();
	}
	
	@Override
	protected JL delLocalImpl() {
		return new JLiftFrontendLocalDel();
	}
	
	@Override
	protected JL delLocalDeclImpl() {
		return new JLiftFrontendLocalDeclDel();
	}
	
	@Override
	protected JL delFormalImpl() {
		return new JLiftFrontendFormalDel();
	}
}
