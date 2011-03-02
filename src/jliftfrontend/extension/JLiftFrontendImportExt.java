package jliftfrontend.extension;

public class JLiftFrontendImportExt extends JLiftFrontendExt_c {

	protected boolean instanceImport;

	public boolean instanceImport() {
		return instanceImport;
	}
	
	public JLiftFrontendImportExt instanceImport(boolean is) {
		JLiftFrontendImportExt copy = (JLiftFrontendImportExt) copy();
		copy.instanceImport = is;
		
		return copy;
	}

}
