/*
 * File          : ExtensionInfo.java
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
package jliftfrontend;

import java.io.Reader;
import java.util.LinkedList;
import java.util.List;

import jliftfrontend.ast.JLiftFrontendNodeFactory_c;
import jliftfrontend.policy.JLiftPolicy;
import jliftfrontend.types.JLiftFrontendTypeSystem_c;
import jliftfrontend.visit.ReplaceInstanceVisitor;
import polyglot.ast.NodeFactory;
import polyglot.ext.siggen.util.SignatureContents;
import polyglot.frontend.CupParser;
import polyglot.frontend.CyclicDependencyException;
import polyglot.frontend.FileSource;
import polyglot.frontend.Job;
import polyglot.frontend.Parser;
import polyglot.frontend.Scheduler;
import polyglot.frontend.goals.Goal;
import polyglot.frontend.goals.VisitorGoal;
import polyglot.lex.Lexer;
import polyglot.main.Options;
import polyglot.parse.Grm;
import polyglot.types.TypeSystem;
import polyglot.util.ErrorQueue;
import polyglot.util.InternalCompilerError;

/**
 * Extension information for jliftfrontend extension.
 */
public class ExtensionInfo extends polyglot.ext.siggen.ExtensionInfo {
    static {
        // force Topics to load
        Topics t = new Topics();
    }

	protected JLiftPolicy thePolicy;
    
    public String defaultFileExtension() {
        return "jl";
    }

    public String compilerName() {
        return "jliftfrontendc";
    }

    @Override
    protected Options createOptions() {
    	JLiftFrontendOptions theOptions = new JLiftFrontendOptions(this);
    	theOptions.output_ext = "jif";
    	theOptions.post_compiler = null;
    	theOptions.serialize_type_info = false;
    	theOptions.should_output_inline_classes = false;
		theOptions.inline_signature_source_path = theOptions.output_directory.toString();

    	
    	return theOptions;
    }
    
    public Parser parser(Reader reader, FileSource source, ErrorQueue eq) {
        Lexer lexer = new polyglot.parse.Lexer_c(reader, source, eq);
        Grm grm = new polyglot.parse.Grm(lexer, ts, nf, eq);
        return new CupParser(grm, source, eq);
    }

    protected NodeFactory createNodeFactory() {
        return new JLiftFrontendNodeFactory_c();
    }

    protected TypeSystem createTypeSystem() {
        return new JLiftFrontendTypeSystem_c();
    }

    @Override
    protected Scheduler createScheduler() {
    	return new JLiftFrontendScheduler(this);
    }
    
    @Override
    public Scheduler scheduler() {
    	return super.scheduler();
    }

	public JLiftPolicy getPolicy() {
		return thePolicy;
	}
	
	public void setPolicy(JLiftPolicy thePolicy) {
		this.thePolicy = thePolicy;
	}
	
	@Override
	public Goal getCompileGoal(Job job) {
		Goal signatureGeneratedGoal = super.getCompileGoal(job);
 		Goal outputInlineClasses = ((JLiftFrontendScheduler) scheduler()).OutputInlineClasses();
 		Goal instanceVisitorGoal = scheduler().internGoal(new VisitorGoal(job, new ReplaceInstanceVisitor(job, ts, nf, sc)));
		Goal output = scheduler().internGoal(scheduler().CodeGenerated(job));
		Goal removedAnonymousInnerClassesGoal = ((JLiftFrontendScheduler) scheduler()).RemovedAnonymousInnerClasses(job);
		List<Goal> l = new LinkedList<Goal>();
		l.add(((JLiftFrontendScheduler) scheduler()).MembersRenamed(job));
		l.add(removedAnonymousInnerClassesGoal);
		l.add(signatureGeneratedGoal);
		l.add(outputInlineClasses);
		l.add(instanceVisitorGoal);
		l.add(output);
		try {
			scheduler().addPrerequisiteDependencyChain(l);
		}
		catch (CyclicDependencyException e) {
			throw new InternalCompilerError(e);
		}
		
		return output;
	}

	public SignatureContents getSignatureContents() {
		return sc;
	}
	
	public Goal getGenerateSignaturesGoal() {
		return generateSignaturesGoal;
	}
}
