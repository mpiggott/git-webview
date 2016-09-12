package ca.piggott.git.webview.server.resources;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.FileMode;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.filter.PathFilter;

import ca.piggott.git.webview.shared.FileSummary;

@Path("/repositories/{repoName}/summary/{refid}/{path}")
@Produces(MediaType.APPLICATION_JSON)
public class FileSummaryResource extends AbstractGitResource {

	@GET
	public List<FileSummary> getTree(@PathParam("repoName") String repoName, @PathParam("refid") String refid,
			@PathParam("path") String path) {
		Git repo = getGit(repoName);
		ObjectId objectId = getObjectId(repo, refid);
		return getTree(repo, path, objectId);
	}

	private List<FileSummary> getTree(Git repo, String path, ObjectId objectId) {
		List<FileSummary> files = new LinkedList<FileSummary>();
		try {
			ObjectReader reader = repo.getRepository().newObjectReader();
			CanonicalTreeParser dir = locateParser(reader, repo, path, objectId);

			try (RevWalk rw = new RevWalk(repo.getRepository())) {
				if ( !"/".equals(path)) {
					rw.setTreeFilter(PathFilter.create(path));
				}
				// rw.
				if (dir != null) {
					while(!dir.eof()) {
						byte[] buffer = new byte[dir.getNameLength()];
						dir.getName(buffer, 0);
						files.add(new FileSummary(dir.getEntryObjectId().getName(), new String(buffer), new Date(rw.parseCommit(dir.getEntryObjectId()).getCommitTime()), FileMode.TREE.equals(dir.getEntryFileMode()) ));
						if (dir != ( dir = dir.next()) ) {
							break;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalStateException(e.getMessage());
		}
		Collections.sort(files);
		return files;
	}
}
