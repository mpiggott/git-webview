package ca.piggott.git.webview.server;

import io.dropwizard.Configuration;
import io.dropwizard.server.DefaultServerFactory;

public class GitConfiguration extends Configuration {

	public GitConfiguration() {
		((DefaultServerFactory) getServerFactory()).setJerseyRootPath("/rest/*");
	}
}
