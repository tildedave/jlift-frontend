/*
 * File          : JLiftFrontendExtFactory_c.java
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

import jliftfrontend.extension.JLiftFrontendClassDeclExt;
import jliftfrontend.extension.JLiftFrontendExt_c;
import jliftfrontend.extension.JLiftFrontendFieldDeclExt;
import jliftfrontend.extension.JLiftFrontendImportExt;
import jliftfrontend.extension.JLiftFrontendMethodDeclExt;
import jliftfrontend.extension.JLiftFrontendTypeNodeExt;
import polyglot.ast.AbstractExtFactory_c;
import polyglot.ast.Ext;

public class JLiftFrontendExtFactory_c extends AbstractExtFactory_c {

	@Override
	protected Ext extNodeImpl() {
		return new JLiftFrontendExt_c();
	}
	
	@Override
	protected Ext extMethodDeclImpl() {
		return new JLiftFrontendMethodDeclExt();
	}
	
	@Override
	protected Ext extFieldDeclImpl() {
		return new JLiftFrontendFieldDeclExt();
	}
	
	@Override
	protected Ext extClassDeclImpl() {
		return new JLiftFrontendClassDeclExt();
	}
	
	@Override
	protected Ext extTypeNodeImpl() {
		return new JLiftFrontendTypeNodeExt();
	}
	
	@Override
	protected Ext extImportImpl() {
		return new JLiftFrontendImportExt();
	}
}
