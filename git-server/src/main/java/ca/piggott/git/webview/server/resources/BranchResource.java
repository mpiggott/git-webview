package ca.piggott.git.webview.server.resources;

import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;

import ca.piggott.git.webview.server.util.Conversion;
import ca.piggott.git.webview.shared.Branch;

@Path("/repositories/{repoName}/branches")
@Produces(MediaType.APPLICATION_JSON)
public class BranchResource extends AbstractGitResource {

	@GET
	public List<Branch> getBranches(@PathParam("repoName") String repoName) throws GitAPIException {
		Git repo = getGit(repoName);
		if (repo == null) 
			throw new IllegalArgumentException("Unknown repository: " + repoName);
		
		List<Branch> branches = new LinkedList<Branch>();
		
		for (Ref ref : repo.branchList().call()) {
			branches.add(Conversion.toBranch(ref));
		}
		
		for (Ref ref : repo.tagList().call()) {
			branches.add(Conversion.toBranch(ref));
		}
		return branches;
	}
}
