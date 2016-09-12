package ca.piggott.git.webview.server.resources;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;

import ca.piggott.git.webview.shared.File;

@Path("/repositories/{repoName}/{refid}/{path}")
@Produces(MediaType.APPLICATION_JSON)
public class FileResource extends AbstractGitResource {

	@GET
	public File getFile(@PathParam("repoName") String repoName, @PathParam("refid") String refid,
			@PathParam("path") String path) throws MissingObjectException, IOException, GitAPIException {
		Git repo = getGit(repoName);
		ObjectId obj = getRefId(repo, refid);
		ObjectReader reader = repo.getRepository().newObjectReader();
		CanonicalTreeParser parser = locateParser(reader, repo, path, obj);
		ObjectLoader loader = reader.open(parser.getEntryObjectId());

		return new File(parser.getEntryObjectId().toString(), path, new String(loader.getBytes()), null);
	}
	
	private ObjectId getRefId(Git repo, String branchOrTagName) throws GitAPIException {
		for (Ref ref: repo.branchList().call()) {
			if (branchOrTagName.equals(ref.getName())) {
				return ref.getPeeledObjectId();
			}
		}
		for (Ref ref: repo.tagList().call()) {
			if (branchOrTagName.equals(ref.getName())) {
				return ref.getPeeledObjectId();
			}
		}
		throw new NotFoundException("Unable to find reference: " + branchOrTagName);
	}
}
