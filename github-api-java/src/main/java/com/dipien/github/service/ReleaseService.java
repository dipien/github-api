package com.dipien.github.service;

import com.google.gson.reflect.TypeToken;
import com.dipien.github.IRepositoryIdProvider;
import com.dipien.github.Release;
import com.dipien.github.client.GitHubClient;
import com.dipien.github.client.GitHubRequest;
import com.dipien.github.client.PageIterator;
import com.dipien.github.client.PagedRequest;
import com.dipien.github.client.RequestException;

import java.io.IOException;
import java.util.List;

import static com.dipien.github.client.IGitHubConstants.SEGMENT_RELEASES;
import static com.dipien.github.client.IGitHubConstants.SEGMENT_REPOS;
import static com.dipien.github.client.IGitHubConstants.SEGMENT_TAGS;
import static com.dipien.github.client.PagedRequest.PAGE_FIRST;
import static com.dipien.github.client.PagedRequest.PAGE_SIZE;

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

	public List<Release> listReleases(IRepositoryIdProvider repositoryIdProvider) throws IOException {
		String repoId = getId(repositoryIdProvider);
		return getAll(pageReleases(repoId));
	}

	public PageIterator<Release> pageReleases(String repoId) {
		return pageReleases(repoId, PAGE_FIRST, PAGE_SIZE);
	}

	public PageIterator<Release> pageReleases(String repoId, int start, int size) {

		StringBuilder uri = new StringBuilder(SEGMENT_REPOS);
		uri.append('/').append(repoId);
		uri.append(SEGMENT_RELEASES);

		PagedRequest<Release> request = createPagedRequest(start, size);
		request.setUri(uri.toString());
		request.setType(new TypeToken<List<Release>>() {}.getType());
		return createPageIterator(request);
	}

	public Release getReleaseByTagName(IRepositoryIdProvider repositoryIdProvider, String tagName) throws IOException {
		String repoId = getId(repositoryIdProvider);

		if (tagName == null) {
			throw new IllegalArgumentException("Tag name cannot be null");
		}

		StringBuilder uri = new StringBuilder(SEGMENT_REPOS);
		uri.append('/').append(repoId);
		uri.append(SEGMENT_RELEASES);
		uri.append(SEGMENT_TAGS);
		uri.append('/').append(tagName);

		GitHubRequest request = createRequest();
		request.setUri(uri);
		request.setType(Release.class);
		try {
			return (Release) client.get(request).getBody();
		} catch (RequestException requestException) {
			if (requestException.getStatus() == 404) {
				return null;
			} else {
				throw requestException;
			}
		}
	}
}
