package ca.piggott.git.webview.server.util;

import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevTag;

import ca.piggott.git.webview.shared.Branch;

public class Conversion {

	public static Branch toBranch(Ref ref) {
		Branch branch = new Branch();
		
		int index = ref.getName().lastIndexOf('/');
		branch.setName( index != -1 ? ref.getName().substring(index+1) : ref.getName());
		branch.setObjectId(ref.getObjectId().getName());
		return branch;
	}

	public static Branch toBranch(RevTag ref) {
		Branch branch = new Branch();
		
		branch.setName(ref.getTagName());
		branch.setObjectId(ref.getId().getName());
		return branch;
	}
}
