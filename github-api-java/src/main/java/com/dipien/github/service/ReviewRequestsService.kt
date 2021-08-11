package com.dipien.github.service

import com.jdroid.github.IRepositoryIdProvider
import com.jdroid.github.client.GitHubClient
import com.jdroid.github.client.IGitHubConstants

// https://developer.github.com/v3/pulls/review_requests/
class ReviewRequestsService : GitHubService {

    constructor() : super()

    constructor(client: GitHubClient) : super(client)

    fun createReviewRequest(repository: IRepositoryIdProvider, pullNumber: Int, reviewers: List<String>? = emptyList(), teamReviewers: List<String>? = emptyList()) {
        val repoId = getId(repository)
        val uri = StringBuilder(IGitHubConstants.SEGMENT_REPOS)
        uri.append("/").append(repoId)
        uri.append(IGitHubConstants.SEGMENT_PULLS)
        uri.append("/").append(pullNumber)
        uri.append("/requested_reviewers")
        val params = mutableMapOf<String, Any>()
        params["reviewers"] = reviewers.orEmpty()
        params["team_reviewers"] = teamReviewers.orEmpty()
        client.post<Any>(uri.toString(), params, null)
    }
}
