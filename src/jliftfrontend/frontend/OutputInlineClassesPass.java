package jliftfrontend.frontend;

import java.util.Collection;

import polyglot.ext.siggen.SignatureOptions;
import polyglot.ext.siggen.frontend.SignatureSourceClassResolver;
import polyglot.ext.siggen.reflect.InlineSignatureClass;
import polyglot.ext.siggen.util.SignatureContents;
import polyglot.ext.siggen.util.SignatureOutputter;
import polyglot.frontend.AbstractPass;
import polyglot.frontend.ExtensionInfo;
import polyglot.frontend.goals.Goal;

public class OutputInlineClassesPass extends AbstractPass {

	protected jliftfrontend.ExtensionInfo extInfo;
	protected SignatureSourceClassResolver noRawClassResolver;

	public OutputInlineClassesPass(Goal goal, ExtensionInfo extInfo) {
		super(goal);
		this.extInfo = (jliftfrontend.ExtensionInfo) extInfo;
	}

	@Override
	public boolean run() {
		System.err.println("running output inline classes pass");
		createNoRawClassesResolver();
		
		SignatureContents sc = extInfo.getSignatureContents();
		Collection<InlineSignatureClass> inlineSigs = sc.getInlineClasses();
		SignatureOutputter sigOut = new SignatureOutputter(noRawClassResolver, extInfo.version(), extInfo.compiler().errorQueue(), true);
		for(InlineSignatureClass isc : inlineSigs) {
			System.err.println("output inline signature " + isc);
			sigOut.generateSignature(isc);
		}
		return true;
	}
	
	private void createNoRawClassesResolver() {
		this.noRawClassResolver = 
			new SignatureSourceClassResolver(extInfo.compiler(), extInfo, 
					extInfo.getSignatureExtInfo(), ((SignatureOptions) extInfo.getOptions()).constructSignatureClasspath(),
					extInfo.compiler().loader(), false, // no raw classes
					extInfo.getOptions().compile_command_line_only,
					extInfo.getOptions().ignore_mod_times);
	}

}
