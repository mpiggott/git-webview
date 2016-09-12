package ca.piggott.git.webview.server.resources;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.FileMode;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class AbstractGitResource {

	protected static Map<String, Git> repos;
	
	private static final Logger log = LoggerFactory.getLogger(AbstractGitResource.class);

	static {
		Map<String, Git> r = new HashMap<String, Git>();
		for (File candidate : new File("G:\\Programming\\scm").listFiles()) {
			if (candidate.isDirectory()) {
				try {
					r.put(candidate.getName(), Git.open(candidate));
				} catch (IOException e) {
					log.error("Failed to create repository {}", candidate.getName(), e);
				}
			}
		}
		repos = Collections.unmodifiableMap(r);
	}

	protected Git getGit(String repoName) {
		Git repo = repos.get(repoName);
		if (repo == null) 
			throw new IllegalArgumentException("Unknown repository: " + repoName);
		return repo;
	}

	protected ObjectId getObjectId(Git repo, String refid) {
		try {
			if (refid.length() == Constants.OBJECT_ID_STRING_LENGTH) {
				return ObjectId.fromString(refid);
			} else {
				final String branch = "refs/heads/" + refid;
					for (Ref ref : repo.branchList().call()) {
						if (branch.equals(ref.getName())) {
							return ref.getObjectId();
						}
					}
				
				for (Ref tag : repo.tagList().call()) {
					if (tag.getName().equals(refid)) {
						return tag.getObjectId();
					}
				}
			}
		} catch (GitAPIException e) {
			throw new RuntimeException(e);
		}
		throw new IllegalStateException("Missing " + refid);
	}

	protected CanonicalTreeParser locateParser(final ObjectReader reader, Git repo, String path, ObjectId objectId) {
		final String[] pathSegments = (path.startsWith("/") && path.length() > 1 ? path.substring(1) : path).split("/");
		try {
			int depth = 0;
			try (RevWalk walker = new RevWalk(repo.getRepository())) {
				CanonicalTreeParser parser = new CanonicalTreeParser(null, reader, walker.parseTree(objectId));
				while (!parser.eof() && depth < pathSegments.length) {
					byte[] buffer = new byte[parser.getNameLength()];
					parser.getName(buffer, 0);
					final String curName = new String(buffer);
					if (curName.equals(pathSegments[depth])) {
						if (FileMode.TREE.equals(parser.getEntryFileMode())) {
							parser = parser.createSubtreeIterator(reader);
						} else if (depth+1 < pathSegments.length) {
							throw new IllegalStateException("Invalid path: " + path);
						}
						++depth;
					} else {
						parser = parser.next();
					}
				}
				return parser;
			}
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage());
		}
	}
}
