package ca.piggott.git.webview.server.resources;

import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/repositories")
@Produces(MediaType.APPLICATION_JSON)
public class RepositoryResource extends AbstractGitResource {

	@GET
	public List<String> getRepositories() {
		return new LinkedList<String>(repos.keySet());
	}
}
