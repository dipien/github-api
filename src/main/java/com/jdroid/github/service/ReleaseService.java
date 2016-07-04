package com.jdroid.github.service;

import com.google.gson.reflect.TypeToken;
import com.jdroid.github.IRepositoryIdProvider;
import com.jdroid.github.Release;
import com.jdroid.github.client.GitHubClient;
import com.jdroid.github.client.GitHubRequest;
import com.jdroid.github.client.PageIterator;
import com.jdroid.github.client.PagedRequest;

import java.io.IOException;
import java.util.List;

import static com.jdroid.github.client.IGitHubConstants.SEGMENT_RELEASES;
import static com.jdroid.github.client.IGitHubConstants.SEGMENT_REPOS;
import static com.jdroid.github.client.IGitHubConstants.SEGMENT_TAGS;
import static com.jdroid.github.client.PagedRequest.PAGE_FIRST;
import static com.jdroid.github.client.PagedRequest.PAGE_SIZE;

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
		return (Release) client.get(request).getBody();
	}
}
