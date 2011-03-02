package jliftfrontend.frontend;

import jliftfrontend.JLiftFrontendOptions;
import polyglot.ast.Node;
import polyglot.ast.PackageNode;
import polyglot.ast.SourceFile;
import polyglot.frontend.OutputPass;
import polyglot.frontend.goals.Goal;
import polyglot.types.Package;
import polyglot.util.InternalCompilerError;
import polyglot.visit.Translator;

public class JLiftFrontendOutputPass extends OutputPass {

	public JLiftFrontendOutputPass(Goal goal, Translator translator) {
		super(goal, translator);
	}

	@Override
	public boolean run() {
		Node ast = goal.job().ast();
		if (ast instanceof SourceFile) {
			PackageNode astPackage = ((SourceFile) ast).package_();
			if (astPackage != null && shouldNotOutputPackage(astPackage.package_())) {
				return true;
			}
		}
		
        if (ast == null) {
            throw new InternalCompilerError("AST is null");
        }

        if (translator.translate(ast)) {
            return true;
        }
        
        return false;
	}

	private boolean shouldNotOutputPackage(Package package_) {
		JLiftFrontendOptions options = (JLiftFrontendOptions) JLiftFrontendOptions.global;
		if (options.packages.isEmpty()) {
			return false;
		}
		if (options.packages.contains(package_.toString()))
			return false;
		return true;
	}
	
}
