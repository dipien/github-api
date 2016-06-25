package com.jdroid.github.service;

import com.jdroid.github.IRepositoryIdProvider;
import com.jdroid.github.Release;
import com.jdroid.github.client.GitHubClient;

import java.io.IOException;

import static com.jdroid.github.client.IGitHubConstants.SEGMENT_RELEASES;
import static com.jdroid.github.client.IGitHubConstants.SEGMENT_REPOS;

public class ReleaseService extends GitHubService {

	public ReleaseService(GitHubClient client) {
		super(client);
	}

	public Release createRelease(IRepositoryIdProvider repositoryIdProvider, Release release) throws IOException {
		String repoId = getId(repositoryIdProvider);

		if (release == null) {
			throw new IllegalArgumentException("Release cannot be null");
		}

		StringBuilder uri = new StringBuilder(SEGMENT_REPOS);
		uri.append('/').append(repoId);
		uri.append(SEGMENT_RELEASES);
		return client.post(uri.toString(), release, Release.class);
	}
}
