package jliftfrontend.frontend;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;

import jliftfrontend.ExtensionInfo;
import jliftfrontend.policy.JLiftPolicy;
import jliftfrontend.policy.PrincipalPolicyStatement;
import polyglot.frontend.AbstractPass;
import polyglot.frontend.goals.Goal;
import polyglot.util.InternalCompilerError;

public class PrincipalsOutputPass extends AbstractPass {

	protected ExtensionInfo theExtInfo;

	public PrincipalsOutputPass(Goal g, ExtensionInfo theExtInfo) {
		super(g);
		this.theExtInfo = theExtInfo;
	}

	@Override
	public boolean run() {
		JLiftPolicy thePolicy = theExtInfo.getPolicy();
		if (thePolicy == null)
			throw new InternalCompilerError("null policy on principal output pass");
		
		Collection<PrincipalPolicyStatement> principalsToAdd = thePolicy.getPrincipalPolicyStatements();
		for(PrincipalPolicyStatement pps : principalsToAdd) {
			String pName = pps.getPrincipalName();
			String fileName = pName + ".jif";
			try {
				File outputPath = new File(theExtInfo.getOptions().output_directory.getCanonicalPath() + "/jif/principals/");
				outputPath.mkdirs();
				File outputFile = new File(outputPath.getCanonicalPath() + "/" + fileName);
				FileOutputStream fos = new FileOutputStream(outputFile);
				PrintStream ps = new PrintStream(fos);
				ps.println("package jif.principals;");
				ps.println();
				ps.println("public class " + pName + " extends ExternalPrincipal");
				ps.println("{");
				ps.println("\tpublic "+ pName + "() { super(\"" + pName + "\"); }");
				ps.println();
				ps.println("\tprivate static " + pName + "{*!:*} P;");
				ps.println("\tpublic static Principal getInstance{*!:*}() {");
				ps.println("\t\tif (P == null) {");
				ps.println("\t\t\tP = new " + pName + "();");
				ps.println("\t\t}");
				ps.println("\t\treturn P;");
				ps.println("\t}");
				ps.println("}");
				fos.close();
			} catch (IOException e) {
				throw new InternalCompilerError(e);
			}
		}
		return true;
	}

}
