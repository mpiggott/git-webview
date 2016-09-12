package ca.piggott.git.webview.server;

import ca.piggott.git.webview.server.resources.BranchResource;
import ca.piggott.git.webview.server.resources.FileResource;
import ca.piggott.git.webview.server.resources.FileSummaryResource;
import ca.piggott.git.webview.server.resources.RepositoryResource;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class GitServer extends Application<GitConfiguration> {
	
	@Override
	public void initialize(Bootstrap<GitConfiguration> bootstrap) {
		bootstrap.addBundle(new AssetsBundle("/assets/", "/"));
	}

	@Override
	public void run(GitConfiguration config, Environment env) throws Exception {
		env.jersey().setUrlPattern("/rest/*");
		env.jersey().register(BranchResource.class);
		env.jersey().register(FileResource.class);
		env.jersey().register(FileSummaryResource.class);
		env.jersey().register(RepositoryResource.class);
	}
	
	public static void main(String[] args) throws Exception {
		new GitServer().run("server");
	}
}
