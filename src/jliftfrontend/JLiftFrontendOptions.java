/*
 * File          : JLiftFrontendOptions.java
 * Project       : jlift-frontend
 * Description   : <insert description here>
 * Author(s)     : dhking
 *
 * Created       : Feb 11, 2008 11:33:24 AM 
 *
 * Copyright (c) 2007-2008 The Pennsylvania State University
 * Systems and Internet Infrastructure Security Laboratory
 *
 */
package jliftfrontend;

import java.io.File;
import java.io.PrintStream;
import java.util.Set;

import polyglot.ext.siggen.SignatureOptions;
import polyglot.frontend.ExtensionInfo;
import polyglot.main.UsageError;
import polyglot.main.Main.TerminationException;

public class JLiftFrontendOptions extends SignatureOptions {

	private static JLiftFrontendOptions theInstance = null;
	protected String policyFile = null;

	protected JLiftFrontendOptions(ExtensionInfo extension) {
		super(extension);
		theInstance = this;
	}
	
	public synchronized static JLiftFrontendOptions getInstance() {
		return theInstance ;
	}

	@Override
	protected int parseCommand(String[] args, int index, Set source)
			throws UsageError, TerminationException {
		if (args[index].equals("-policyfile")) {
			++index;
			this.policyFile = args[index];
			++index;
		}
		else {
			return super.parseCommand(args, index, source);
		}
		
		return index;
	}
	
	@Override
	public void parseCommandLine(String[] args, Set source) throws UsageError {
		super.parseCommandLine(args, source);
		if (this.policyFile == null) {
			throw new UsageError("Must specify policy file with -policyfile");
		}
	}

	public String getPolicyFile() {
		return policyFile;
	}
	
	@Override
	public void setDefaultValues() {
		super.setDefaultValues();
		String currentDir = System.getProperty("user.dir");
		this.output_directory= new File(currentDir + "/jlift-src");
		this.inline_signature_source_path = currentDir + "/jlift-src";
	}
	
	@Override
	public void usage(PrintStream out) {
		super.usage(out);
		usageSubsection(out, "JLift Frontend Options");
		usageForFlag(out, "-policyfile", "specifies the JLift Policy File");
	}
}
