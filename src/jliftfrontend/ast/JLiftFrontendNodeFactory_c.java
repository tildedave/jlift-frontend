/*
 * File          : JLiftFrontendNodeFactory_c.java
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

import polyglot.ast.DelFactory;
import polyglot.ast.ExtFactory;
import polyglot.ast.Id;
import polyglot.ast.NodeFactory_c;
import polyglot.util.Position;

/**
 * NodeFactory for jliftfrontend extension.
 */
public class JLiftFrontendNodeFactory_c extends NodeFactory_c implements JliftFrontendNodeFactory {
	
	
	public JLiftFrontendNodeFactory_c() {
		super(new JLiftFrontendExtFactory_c(), new JLiftFrontendDelFactory_c());
	}
	
	@Override
	public Id Id(Position pos, String name) {
		String superName = name;
		if (name.equals("label"))
			superName = "label_";
		else if (name.equals("principal"))
			superName = "principal_";
		
		return super.Id(pos, superName);
	}
	
//	@Override
//	protected ExtFactory extFactory() {
//		return new JLiftFrontendExtFactory_c();
//	}
//	
//	@Override
//	protected DelFactory delFactory() {
//		return new JLiftFrontendDelFactory_c();
//	}
}
