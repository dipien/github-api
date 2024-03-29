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

import com.dipien.github.CommitComment;

import java.io.Serializable;

/**
 * CommitCommentEvent payload model class.
 */
public class CommitCommentPayload extends EventPayload implements
		Serializable {

	private static final long serialVersionUID = -2606554911096551099L;

	private CommitComment comment;

	/**
	 * @return comment
	 */
	public CommitComment getComment() {
		return comment;
	}

	/**
	 * @param comment
	 * @return this CommitCommentPayload
	 */
	public CommitCommentPayload setComment(CommitComment comment) {
		this.comment = comment;
		return this;
	}
}
