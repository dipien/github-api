/*******************************************************************************
 *  Copyright (c) 2011 GitHub Inc.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *    Kevin Sawicki (GitHub Inc.) - initial API and implementation
 *******************************************************************************/
package com.dipien.github.service;

import com.google.gson.reflect.TypeToken;
import com.dipien.github.Comment;
import com.dipien.github.IRepositoryIdProvider;
import com.dipien.github.IResourceProvider;
import com.dipien.github.Issue;
import com.dipien.github.IssueEvent;
import com.dipien.github.Label;
import com.dipien.github.Milestone;
import com.dipien.github.RepositoryIssue;
import com.dipien.github.SearchIssue;
import com.dipien.github.User;
import com.dipien.github.client.GitHubClient;
import com.dipien.github.client.GitHubRequest;
import com.dipien.github.client.PageIterator;
import com.dipien.github.client.PagedRequest;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.dipien.github.client.IGitHubConstants.CHARSET_UTF8;
import static com.dipien.github.client.IGitHubConstants.SEGMENT_ASSIGNEES;
import static com.dipien.github.client.IGitHubConstants.SEGMENT_COMMENTS;
import static com.dipien.github.client.IGitHubConstants.SEGMENT_EVENTS;
import static com.dipien.github.client.IGitHubConstants.SEGMENT_ISSUES;
import static com.dipien.github.client.IGitHubConstants.SEGMENT_LEGACY;
import static com.dipien.github.client.IGitHubConstants.SEGMENT_REPOS;
import static com.dipien.github.client.IGitHubConstants.SEGMENT_SEARCH;
import static com.dipien.github.client.PagedRequest.PAGE_FIRST;
import static com.dipien.github.client.PagedRequest.PAGE_SIZE;

/**
 * Issue service class for listing, searching, and fetching {@link Issue}
 * objects using a {@link GitHubClient}.
 *
 * @see <a href="http://developer.github.com/v3/issues">GitHub Issues API
 *      documentation</a>
 */
public class IssueService extends GitHubService {

	/**
	 * Filter field key
	 */
	public static final String FIELD_FILTER = "filter"; //$NON-NLS-1$

	/**
	 * Filter by issue assignee
	 */
	public static final String FILTER_ASSIGNEE = "assignee"; //$NON-NLS-1$

	/**
	 * Filter by issue's milestone
	 */
	public static final String FILTER_MILESTONE = "milestone"; //$NON-NLS-1$

	/**
	 * Filter by user mentioned in issue
	 */
	public static final String FILTER_MENTIONED = "mentioned"; //$NON-NLS-1$

	/**
	 * Filter by subscribed issues for user
	 */
	public static final String FILTER_SUBSCRIBED = "subscribed"; //$NON-NLS-1$

	/**
	 * Filter by created issues by user
	 */
	public static final String FILTER_CREATED = "created"; //$NON-NLS-1$

	/**
	 * Filter by assigned issues for user
	 */
	public static final String FILTER_ASSIGNED = "assigned"; //$NON-NLS-1$

	/**
	 * Filter by issue's labels
	 */
	public static final String FILTER_LABELS = "labels"; //$NON-NLS-1$

	/**
	 * Filter by issue's state
	 */
	public static final String FILTER_STATE = "state"; //$NON-NLS-1$

	/**
	 * Issue open state filter value
	 */
	public static final String STATE_OPEN = "open"; //$NON-NLS-1$

	/**
	 * Issue closed state filter value
	 */
	public static final String STATE_CLOSED = "closed"; //$NON-NLS-1$

	/**
	 * Issue body field name
	 */
	public static final String FIELD_BODY = "body"; //$NON-NLS-1$
	
	public static final String FIELD_ASSIGNEES = "assignees";

	/**
	 * Issue title field name
	 */
	public static final String FIELD_TITLE = "title"; //$NON-NLS-1$

	/**
	 * Since date field
	 */
	public static final String FIELD_SINCE = "since"; //$NON-NLS-1$

	/**
	 * Sort direction of output
	 */
	public static final String FIELD_DIRECTION = "direction"; //$NON-NLS-1$

	/**
	 * Ascending direction sort order
	 */
	public static final String DIRECTION_ASCENDING = "asc"; //$NON-NLS-1$

	/**
	 * Descending direction sort order
	 */
	public static final String DIRECTION_DESCENDING = "desc"; //$NON-NLS-1$

	/**
	 * Sort field key
	 */
	public static final String FIELD_SORT = "sort"; //$NON-NLS-1$

	/**
	 * Sort by created at
	 */
	public static final String SORT_CREATED = "created"; //$NON-NLS-1$

	/**
	 * Sort by updated at
	 */
	public static final String SORT_UPDATED = "updated"; //$NON-NLS-1$

	/**
	 * Sort by commented on at
	 */
	public static final String SORT_COMMENTS = "comments"; //$NON-NLS-1$

	private static class IssueContainer implements
			IResourceProvider<SearchIssue> {

		private List<SearchIssue> issues;

		/**
		 * @see IResourceProvider#getResources()
		 */
		public List<SearchIssue> getResources() {
			return issues;
		}
	}

	/**
	 * Create issue service
	 */
	public IssueService() {
		super();
	}

	/**
	 * Create issue service
	 *
	 * @param client
	 *            cannot be null
	 */
	public IssueService(GitHubClient client) {
		super(client);
	}

	/**
	 * Get issues for currently authenticated user
	 *
	 * @return non-null but possibly empty list of issues
	 * @throws IOException
	 */
	public List<RepositoryIssue> getIssues() throws IOException {
		return getIssues(null);
	}

	/**
	 * Get issues for currently authenticated user
	 *
	 * @param filterData
	 * @return non-null but possibly empty list of issues
	 * @throws IOException
	 */
	public List<RepositoryIssue> getIssues(Map<String, String> filterData)
			throws IOException {
		return getAll(pageIssues(filterData));
	}

	/**
	 * Page issues for currently authenticated user
	 *
	 * @return iterator over pages of issues
	 */
	public PageIterator<RepositoryIssue> pageIssues() {
		return pageIssues((Map<String, String>) null);
	}

	/**
	 * Page issues for currently authenticated user
	 *
	 * @param filterData
	 * @return iterator over pages of issues
	 */
	public PageIterator<RepositoryIssue> pageIssues(
			Map<String, String> filterData) {
		return pageIssues(filterData, PAGE_SIZE);
	}

	/**
	 * Page issues for currently authenticated user
	 *
	 * @param filterData
	 * @param size
	 * @return iterator over pages of issues
	 */
	public PageIterator<RepositoryIssue> pageIssues(
			Map<String, String> filterData, int size) {
		return pageIssues(filterData, PAGE_FIRST, size);
	}

	/**
	 * Page issues for currently authenticated user
	 *
	 * @param filterData
	 * @param start
	 * @param size
	 * @return iterator over pages of issues
	 */
	public PageIterator<RepositoryIssue> pageIssues(
			Map<String, String> filterData, int start, int size) {
		PagedRequest<RepositoryIssue> request = createPagedRequest(start, size);
		request.setParams(filterData);
		request.setUri(SEGMENT_ISSUES);
		request.setType(new TypeToken<List<RepositoryIssue>>() {
		}.getType());
		return createPageIterator(request);
	}

	/**
	 * Get issue
	 *
	 * @param repository
	 * @param issueNumber
	 * @return issue
	 * @throws IOException
	 */
	public Issue getIssue(IRepositoryIdProvider repository, int issueNumber)
			throws IOException {
		
		String repoId = getId(repository);
		StringBuilder uri = new StringBuilder(SEGMENT_REPOS);
		uri.append('/').append(repoId);
		uri.append(SEGMENT_ISSUES);
		uri.append('/').append(Integer.toString(issueNumber));
		GitHubRequest request = createRequest();
		request.setUri(uri);
		request.setType(Issue.class);
		return (Issue) client.get(request).getBody();
	}

	/**
	 * Get an issue's comments
	 *
	 * @param repository
	 * @param issueNumber
	 * @return list of comments
	 * @throws IOException
	 */
	public List<Comment> getComments(IRepositoryIdProvider repository, int issueNumber)
			throws IOException {
		
		String repoId = getId(repository);
		StringBuilder uri = new StringBuilder(SEGMENT_REPOS);
		uri.append('/').append(repoId);
		uri.append(SEGMENT_ISSUES);
		uri.append('/').append(Integer.toString(issueNumber));
		uri.append(SEGMENT_COMMENTS);
		PagedRequest<Comment> request = createPagedRequest();
		request.setUri(uri);
		request.setType(new TypeToken<List<Comment>>() {
		}.getType());
		return getAll(request);
	}

	/**
	 * Get bulk issues request
	 *
	 * @param repoId
	 * @param filterData
	 * @param start
	 * @param size
	 * @return paged request
	 */
	protected PagedRequest<Issue> createPagedRequest(String repoId,
			Map<String, String> filterData, int start, int size) {
		StringBuilder uri = new StringBuilder(SEGMENT_REPOS);
		uri.append('/').append(repoId);
		uri.append(SEGMENT_ISSUES);
		PagedRequest<Issue> request = createPagedRequest(start, size);
		request.setParams(filterData).setUri(uri);
		request.setType(new TypeToken<List<Issue>>() {
		}.getType());
		return request;
	}

	/**
	 * Get a list of {@link Issue} objects that match the specified filter data
	 *
	 * @param repository
	 * @param filterData
	 * @return list of issues
	 * @throws IOException
	 */
	public List<Issue> getIssues(IRepositoryIdProvider repository,
			Map<String, String> filterData) throws IOException {
		return getAll(pageIssues(repository, filterData));
	}

	/**
	 * Get page iterator over issues query
	 *
	 * @param user
	 * @param repository
	 * @return iterator over issue pages
	 */
	public PageIterator<Issue> pageIssues(String user, String repository) {
		return pageIssues(user, repository, null);
	}

	/**
	 * Get page iterator over issues query
	 *
	 * @param user
	 * @param repository
	 * @param filterData
	 * @return iterator
	 */
	public PageIterator<Issue> pageIssues(String user, String repository,
			Map<String, String> filterData) {
		return pageIssues(user, repository, filterData, PAGE_SIZE);
	}

	/**
	 * Get page iterator over issues query
	 *
	 * @param user
	 * @param repository
	 * @param filterData
	 * @param size
	 * @return iterator
	 */
	public PageIterator<Issue> pageIssues(String user, String repository,
			Map<String, String> filterData, int size) {
		return pageIssues(user, repository, filterData, PAGE_FIRST, size);
	}

	/**
	 * Get page iterator over issues query
	 *
	 * @param user
	 * @param repository
	 * @param filterData
	 * @param size
	 *            page size
	 * @param start
	 *            starting page number
	 * @return iterator
	 */
	public PageIterator<Issue> pageIssues(String user, String repository,
			Map<String, String> filterData, int start, int size) {
		verifyRepository(user, repository);
		String repoId = user + '/' + repository;
		PagedRequest<Issue> request = createPagedRequest(repoId, filterData,
				start, size);
		return createPageIterator(request);
	}

	/**
	 * Get page iterator over issues query
	 *
	 * @param repository
	 * @return iterator over issue pages
	 */
	public PageIterator<Issue> pageIssues(IRepositoryIdProvider repository) {
		return pageIssues(repository, null);
	}

	/**
	 * Get page iterator over issues query
	 *
	 * @param repository
	 * @param filterData
	 * @return iterator
	 */
	public PageIterator<Issue> pageIssues(IRepositoryIdProvider repository,
			Map<String, String> filterData) {
		return pageIssues(repository, filterData, PAGE_SIZE);
	}

	/**
	 * Get page iterator over issues query
	 *
	 * @param repository
	 * @param filterData
	 * @param size
	 * @return iterator
	 */
	public PageIterator<Issue> pageIssues(IRepositoryIdProvider repository,
			Map<String, String> filterData, int size) {
		return pageIssues(repository, filterData, PAGE_FIRST, size);
	}

	/**
	 * Get page iterator over issues query
	 *
	 * @param repository
	 * @param filterData
	 * @param size
	 *            page size
	 * @param start
	 *            starting page number
	 * @return iterator
	 */
	public PageIterator<Issue> pageIssues(IRepositoryIdProvider repository,
			Map<String, String> filterData, int start, int size) {
		String repoId = getId(repository);
		PagedRequest<Issue> request = createPagedRequest(repoId, filterData,
				start, size);
		return createPageIterator(request);
	}

	/**
	 * Create issue map for issue
	 *
	 * @param issue
	 * @param newIssue
	 * @return map
	 */
	protected Map<Object, Object> createIssueMap(Issue issue, boolean newIssue) {
		Map<Object, Object> params = new HashMap<Object, Object>();
		if (issue != null) {
			params.put(FIELD_BODY, issue.getBody());
			params.put(FIELD_TITLE, issue.getTitle());
			User assignee = issue.getAssignee();
			if (assignee != null)
				params.put(FILTER_ASSIGNEE, assignee.getLogin());

			Milestone milestone = issue.getMilestone();
			if (milestone != null) {
				int number = milestone.getNumber();
				if (number > 0)
					params.put(FILTER_MILESTONE, Integer.toString(number));
				else {
					if (!newIssue)
						params.put(FILTER_MILESTONE, ""); //$NON-NLS-1$
				}
			}
			List<Label> labels = issue.getLabels();
			if (labels != null) {
				List<String> labelNames = new ArrayList<>(labels.size());
				for (Label label : labels)
					labelNames.add(label.getName());
				params.put(FILTER_LABELS, labelNames);
			}
		}
		return params;
	}

	/**
	 * Create issue
	 *
	 * @param repository
	 * @param issue
	 * @return created issue
	 * @throws IOException
	 */
	public Issue createIssue(IRepositoryIdProvider repository, Issue issue)
			throws IOException {
		String repoId = getId(repository);
		StringBuilder uri = new StringBuilder(SEGMENT_REPOS);
		uri.append('/').append(repoId);
		uri.append(SEGMENT_ISSUES);

		Map<Object, Object> params = createIssueMap(issue, true);
		return client.post(uri.toString(), params, Issue.class);
	}

	/**
	 * Edit issue
	 *
	 * @param repository
	 * @param issue
	 * @return created issue
	 * @throws IOException
	 */
	public Issue editIssue(IRepositoryIdProvider repository, Issue issue)
			throws IOException {
		String repoId = getId(repository);
		if (issue == null)
			throw new IllegalArgumentException("Issue cannot be null"); //$NON-NLS-1$

		StringBuilder uri = new StringBuilder(SEGMENT_REPOS);
		uri.append('/').append(repoId);
		uri.append(SEGMENT_ISSUES);
		uri.append('/').append(issue.getNumber());

		Map<Object, Object> params = createIssueMap(issue, false);
		String state = issue.getState();
		if (state != null)
			params.put(FILTER_STATE, state);
		return client.post(uri.toString(), params, Issue.class);
	}
	
	public Issue addAssignee(IRepositoryIdProvider repository, int number, String assignee) throws IOException {
		String repoId = getId(repository);
		StringBuilder uri = new StringBuilder(SEGMENT_REPOS);
		uri.append('/').append(repoId);
		uri.append(SEGMENT_ISSUES);
		uri.append('/').append(number);
		uri.append(SEGMENT_ASSIGNEES);
		
		Map<Object, Object> params = new HashMap<>();
		params.put(FIELD_ASSIGNEES, assignee);
		return client.post(uri.toString(), params, Issue.class);
	}

	/**
	 * Create comment on specified issue number
	 *
	 * @param user
	 * @param repository
	 * @param issueNumber
	 * @param comment
	 * @return created issue
	 * @throws IOException
	 */
	public Comment createComment(String user, String repository,
			int issueNumber, String comment) throws IOException {
		return createComment(user, repository, Integer.toString(issueNumber),
				comment);
	}

	/**
	 * Create comment on specified issue number
	 *
	 * @param user
	 * @param repository
	 * @param issueNumber
	 * @param comment
	 * @return created issue
	 * @throws IOException
	 */
	public Comment createComment(String user, String repository,
			String issueNumber, String comment) throws IOException {
		verifyRepository(user, repository);

		String repoId = user + '/' + repository;
		return createComment(repoId, issueNumber, comment);
	}

	/**
	 * Create comment on specified issue number
	 *
	 * @param repository
	 * @param issueNumber
	 * @param comment
	 * @return created issue
	 * @throws IOException
	 */
	public Comment createComment(IRepositoryIdProvider repository,
			int issueNumber, String comment) throws IOException {
		return createComment(repository, Integer.toString(issueNumber), comment);
	}

	/**
	 * Create comment on specified issue number
	 *
	 * @param repository
	 * @param issueNumber
	 * @param comment
	 * @return created issue
	 * @throws IOException
	 */
	public Comment createComment(IRepositoryIdProvider repository,
			String issueNumber, String comment) throws IOException {
		String repoId = getId(repository);
		return createComment(repoId, issueNumber, comment);
	}

	/**
	 * Create comment on specified issue number
	 *
	 * @param repoId
	 * @param issueNumber
	 * @param comment
	 * @return created issue
	 * @throws IOException
	 */
	private Comment createComment(String repoId, String issueNumber,
			String comment) throws IOException {
		if (issueNumber == null)
			throw new IllegalArgumentException("Issue number cannot be null"); //$NON-NLS-1$
		if (issueNumber.length() == 0)
			throw new IllegalArgumentException("Issue number cannot be empty"); //$NON-NLS-1$

		StringBuilder uri = new StringBuilder(SEGMENT_REPOS);
		uri.append('/').append(repoId);
		uri.append(SEGMENT_ISSUES);
		uri.append('/').append(issueNumber);
		uri.append(SEGMENT_COMMENTS);

		Map<String, String> params = new HashMap<String, String>(1, 1);
		params.put(FIELD_BODY, comment);

		return client.post(uri.toString(), params, Comment.class);
	}

	/**
	 * Get issue comment
	 *
	 * @param user
	 * @param repository
	 * @param commentId
	 * @return comment
	 * @throws IOException
	 */
	public Comment getComment(String user, String repository, long commentId)
			throws IOException {
		verifyRepository(user, repository);

		GitHubRequest request = createRequest();
		StringBuilder uri = new StringBuilder(SEGMENT_REPOS);
		uri.append('/').append(user).append('/').append(repository);
		uri.append(SEGMENT_ISSUES).append(SEGMENT_COMMENTS);
		uri.append('/').append(commentId);
		request.setUri(uri);
		request.setType(Comment.class);
		return (Comment) client.get(request).getBody();
	}

	/**
	 * Edit issue comment
	 *
	 * @param repository
	 * @param comment
	 * @return edited comment
	 * @throws IOException
	 */
	public Comment editComment(IRepositoryIdProvider repository, Comment comment)
			throws IOException {
		String repoId = getId(repository);
		if (comment == null)
			throw new IllegalArgumentException("Comment cannot be null"); //$NON-NLS-1$

		StringBuilder uri = new StringBuilder(SEGMENT_REPOS);
		uri.append('/').append(repoId);
		uri.append(SEGMENT_ISSUES).append(SEGMENT_COMMENTS);
		uri.append('/').append(comment.getId());
		return client.post(uri.toString(), comment, Comment.class);
	}

	/**
	 * Delete the issue comment with the given id
	 *
	 * @param repository
	 * @param commentId
	 * @throws IOException
	 */
	public void deleteComment(IRepositoryIdProvider repository, long commentId)
			throws IOException {
		String repoId = getId(repository);
		StringBuilder uri = new StringBuilder(SEGMENT_REPOS);
		uri.append('/').append(repoId);
		uri.append(SEGMENT_ISSUES).append(SEGMENT_COMMENTS);
		uri.append('/').append(Long.toString(commentId));
		client.delete(uri.toString());
	}

	/**
	 * Page issue events for repository
	 *
	 * @param user
	 * @param repository
	 * @return iterator over issue event pages
	 */
	public PageIterator<IssueEvent> pageEvents(String user, String repository) {
		return pageEvents(user, repository, PAGE_SIZE);
	}

	/**
	 * Page issue events for repository
	 *
	 * @param user
	 * @param repository
	 * @param size
	 * @return iterator over issue event pages
	 */
	public PageIterator<IssueEvent> pageEvents(String user, String repository,
			int size) {
		return pageEvents(user, repository, PAGE_FIRST, size);
	}

	/**
	 * Page events for issue in repository
	 *
	 * @param user
	 * @param repository
	 * @param start
	 * @param size
	 * @return iterator over issue event pages
	 */
	public PageIterator<IssueEvent> pageEvents(String user, String repository,
			int start, int size) {
		verifyRepository(user, repository);

		PagedRequest<IssueEvent> request = createPagedRequest(start, size);
		StringBuilder uri = new StringBuilder(SEGMENT_REPOS);
		uri.append('/').append(user).append('/').append(repository);
		uri.append(SEGMENT_ISSUES);
		uri.append(SEGMENT_EVENTS);
		request.setUri(uri);
		request.setType(new TypeToken<List<IssueEvent>>() {
		}.getType());
		return createPageIterator(request);
	}

	/**
	 * Page events for issue in repository
	 *
	 * @param user
	 * @param repository
	 * @param issueId
	 * @return iterator over issue event pages
	 */
	public PageIterator<IssueEvent> pageIssueEvents(String user,
			String repository, int issueId) {
		return pageIssueEvents(user, repository, issueId, PAGE_SIZE);
	}

	/**
	 * Page events for issue in repository
	 *
	 * @param user
	 * @param repository
	 * @param issueId
	 * @param size
	 * @return iterator over issue event pages
	 */
	public PageIterator<IssueEvent> pageIssueEvents(String user,
			String repository, int issueId, int size) {
		return pageIssueEvents(user, repository, issueId, PAGE_FIRST, size);
	}

	/**
	 * Page issue events for repository
	 *
	 * @param user
	 * @param repository
	 * @param issueId
	 * @param start
	 * @param size
	 * @return iterator over issue event pages
	 */
	public PageIterator<IssueEvent> pageIssueEvents(String user,
			String repository, int issueId, int start, int size) {
		verifyRepository(user, repository);

		PagedRequest<IssueEvent> request = createPagedRequest(start, size);
		StringBuilder uri = new StringBuilder(SEGMENT_REPOS);
		uri.append('/').append(user).append('/').append(repository);
		uri.append(SEGMENT_ISSUES);
		uri.append('/').append(issueId);
		uri.append(SEGMENT_EVENTS);
		request.setUri(uri);
		request.setType(new TypeToken<List<IssueEvent>>() {
		}.getType());
		return createPageIterator(request);
	}

	/**
	 * Get issue event for repository
	 *
	 * @param user
	 * @param repository
	 * @param eventId
	 * @return iterator over issue event pages
	 * @throws IOException
	 */
	public IssueEvent getIssueEvent(String user, String repository, long eventId)
			throws IOException {
		verifyRepository(user, repository);

		GitHubRequest request = createRequest();
		StringBuilder uri = new StringBuilder(SEGMENT_REPOS);
		uri.append('/').append(user).append('/').append(repository);
		uri.append(SEGMENT_ISSUES);
		uri.append(SEGMENT_EVENTS);
		uri.append('/').append(eventId);
		request.setUri(uri);
		request.setType(IssueEvent.class);
		return (IssueEvent) client.get(request).getBody();
	}

	/**
	 * Search issues in the given repository using the given query
	 *
	 * @param repository
	 * @param state
	 *            {@link #STATE_OPEN} or {@link #STATE_CLOSED}
	 * @param query
	 * @return issues matching query
	 * @throws IOException
	 */
	public List<SearchIssue> searchIssues(IRepositoryIdProvider repository,
			String state, String query) throws IOException {
		String id = getId(repository);
		if (state == null)
			throw new IllegalArgumentException("State cannot be null"); //$NON-NLS-1$
		if (state.length() == 0)
			throw new IllegalArgumentException("State cannot be empty"); //$NON-NLS-1$
		if (query == null)
			throw new IllegalArgumentException("Query cannot be null"); //$NON-NLS-1$
		if (query.length() == 0)
			throw new IllegalArgumentException("Query cannot be empty"); //$NON-NLS-1$

		StringBuilder uri = new StringBuilder(SEGMENT_LEGACY + SEGMENT_ISSUES
				+ SEGMENT_SEARCH);
		uri.append('/').append(id);
		uri.append('/').append(state);
		final String encodedQuery = URLEncoder.encode(query, CHARSET_UTF8)
				.replace("+", "%20") //$NON-NLS-1$ //$NON-NLS-2$
				.replace(".", "%2E"); //$NON-NLS-1$ //$NON-NLS-2$
		uri.append('/').append(encodedQuery);

		PagedRequest<SearchIssue> request = createPagedRequest();
		request.setUri(uri);
		request.setType(IssueContainer.class);
		return getAll(request);
	}
}
