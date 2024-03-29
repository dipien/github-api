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

import com.dipien.github.Issue;

import java.io.Serializable;

/**
 * IssuesEvent payload model class.
 */
public class IssuesPayload extends EventPayload implements Serializable {

	private static final long serialVersionUID = 3210795492806809443L;

	private String action;

	private Issue issue;

	/**
	 * @return action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param action
	 * @return this IssuesPayload
	 */
	public IssuesPayload setAction(String action) {
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
	 * @return this IssuesPayload
	 */
	public IssuesPayload setIssue(Issue issue) {
		this.issue = issue;
		return this;
	}
}
