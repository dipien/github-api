/*******************************************************************************
 *  Copyright (c) 2011 GitHub Inc.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *    Jason Tsay (GitHub Inc.) - initial API and implementation
 *******************************************************************************/
package com.dipien.github.event;

import com.dipien.github.Comment;
import com.dipien.github.Issue;

import java.io.Serializable;

/**
 * IssueCommentEvent payload model class.
 */
public class IssueCommentPayload extends EventPayload implements Serializable {

	private static final long serialVersionUID = 2661548417314120170L;

	private String action;

	private Issue issue;

	private Comment comment;

	/**
	 * @return action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param action
	 * @return this IssueCommentPayload
	 */
	public IssueCommentPayload setAction(String action) {
		this.action = action;
		return this;
	}

	/**
	 * @return issue
	 */
	public Issue getIssue() {
		return issue;
	}

	/**
	 * @param issue
	 * @return this IssueCommentPayload
	 */
	public IssueCommentPayload setIssue(Issue issue) {
		this.issue = issue;
		return this;
	}

	/**
	 * @return comment
	 */
	public Comment getComment() {
		return comment;
	}

	/**
	 * @param comment
	 * @return this IssueCommentPayload
	 */
	public IssueCommentPayload setComment(Comment comment) {
		this.comment = comment;
		return this;
	}
}
