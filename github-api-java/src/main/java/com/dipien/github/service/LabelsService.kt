package com.dipien.github.service

import com.jdroid.github.IRepositoryIdProvider
import com.jdroid.github.client.GitHubClient
import com.jdroid.github.client.IGitHubConstants

// https://developer.github.com/v3/issues/labels/
class LabelsService : GitHubService {

    constructor() : super()

    constructor(client: GitHubClient) : super(client)

    fun addLabelsToIssue(repository: IRepositoryIdProvider, issueNumber: Int, labels: List<String>) {
        val repoId = getId(repository)
        val uri = StringBuilder(IGitHubConstants.SEGMENT_REPOS)
        uri.append("/").append(repoId)
        uri.append(IGitHubConstants.SEGMENT_ISSUES)
        uri.append("/").append(issueNumber)
        uri.append("/labels")
        val params = mutableMapOf<String, Any>()
        params["labels"] = labels
        client.post<Any>(uri.toString(), params, null)
    }
}
