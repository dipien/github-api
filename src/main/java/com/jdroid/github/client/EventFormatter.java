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
package com.jdroid.github.client;

import static com.google.gson.FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES;
import static com.jdroid.github.event.Event.TYPE_COMMIT_COMMENT;
import static com.jdroid.github.event.Event.TYPE_CREATE;
import static com.jdroid.github.event.Event.TYPE_DELETE;
import static com.jdroid.github.event.Event.TYPE_DOWNLOAD;
import static com.jdroid.github.event.Event.TYPE_FOLLOW;
import static com.jdroid.github.event.Event.TYPE_FORK;
import static com.jdroid.github.event.Event.TYPE_FORK_APPLY;
import static com.jdroid.github.event.Event.TYPE_GIST;
import static com.jdroid.github.event.Event.TYPE_GOLLUM;
import static com.jdroid.github.event.Event.TYPE_ISSUES;
import static com.jdroid.github.event.Event.TYPE_ISSUE_COMMENT;
import static com.jdroid.github.event.Event.TYPE_MEMBER;
import static com.jdroid.github.event.Event.TYPE_PUBLIC;
import static com.jdroid.github.event.Event.TYPE_PULL_REQUEST;
import static com.jdroid.github.event.Event.TYPE_PULL_REQUEST_REVIEW_COMMENT;
import static com.jdroid.github.event.Event.TYPE_PUSH;
import static com.jdroid.github.event.Event.TYPE_TEAM_ADD;
import static com.jdroid.github.event.Event.TYPE_WATCH;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Date;

import com.jdroid.github.event.CommitCommentPayload;
import com.jdroid.github.event.CreatePayload;
import com.jdroid.github.event.DeletePayload;
import com.jdroid.github.event.DownloadPayload;
import com.jdroid.github.event.Event;
import com.jdroid.github.event.EventPayload;
import com.jdroid.github.event.FollowPayload;
import com.jdroid.github.event.ForkApplyPayload;
import com.jdroid.github.event.ForkPayload;
import com.jdroid.github.event.GistPayload;
import com.jdroid.github.event.GollumPayload;
import com.jdroid.github.event.IssueCommentPayload;
import com.jdroid.github.event.IssuesPayload;
import com.jdroid.github.event.MemberPayload;
import com.jdroid.github.event.PublicPayload;
import com.jdroid.github.event.PullRequestPayload;
import com.jdroid.github.event.PullRequestReviewCommentPayload;
import com.jdroid.github.event.PushPayload;
import com.jdroid.github.event.TeamAddPayload;
import com.jdroid.github.event.WatchPayload;

/**
 * Formats an event's payload with the appropriate class given a certain event
 * type
 */
public class EventFormatter implements JsonDeserializer<Event> {

	private final Gson gson = new GsonBuilder()
			.registerTypeAdapter(Date.class, new DateFormatter())
			.setFieldNamingPolicy(LOWER_CASE_WITH_UNDERSCORES).create();

	public Event deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
		final Event event = gson.fromJson(json, Event.class);
		if (event == null || !json.isJsonObject())
			return event;
		final JsonElement rawPayload = json.getAsJsonObject().get("payload");
		if (rawPayload == null || !rawPayload.isJsonObject())
			return event;
		final String type = event.getType();
		if (type == null || type.length() == 0)
			return event;

		Class<? extends EventPayload> payloadClass;
		if (TYPE_COMMIT_COMMENT.equals(type))
			payloadClass = CommitCommentPayload.class;
		else if (TYPE_CREATE.equals(type))
			payloadClass = CreatePayload.class;
		else if (TYPE_DELETE.equals(type))
			payloadClass = DeletePayload.class;
		else if (TYPE_DOWNLOAD.equals(type))
			payloadClass = DownloadPayload.class;
		else if (TYPE_FOLLOW.equals(type))
			payloadClass = FollowPayload.class;
		else if (TYPE_FORK.equals(type))
			payloadClass = ForkPayload.class;
		else if (TYPE_FORK_APPLY.equals(type))
			payloadClass = ForkApplyPayload.class;
		else if (TYPE_GIST.equals(type))
			payloadClass = GistPayload.class;
		else if (TYPE_GOLLUM.equals(type))
			payloadClass = GollumPayload.class;
		else if (TYPE_ISSUE_COMMENT.equals(type))
			payloadClass = IssueCommentPayload.class;
		else if (TYPE_ISSUES.equals(type))
			payloadClass = IssuesPayload.class;
		else if (TYPE_MEMBER.equals(type))
			payloadClass = MemberPayload.class;
		else if (TYPE_PUBLIC.equals(type))
			payloadClass = PublicPayload.class;
		else if (TYPE_PULL_REQUEST.equals(type))
			payloadClass = PullRequestPayload.class;
		else if (TYPE_PULL_REQUEST_REVIEW_COMMENT.equals(type))
			payloadClass = PullRequestReviewCommentPayload.class;
		else if (TYPE_PUSH.equals(type))
			payloadClass = PushPayload.class;
		else if (TYPE_RELEASE.equals(type))
			payloadClass = ReleasePayload.class;
		else if (TYPE_TEAM_ADD.equals(type))
			payloadClass = TeamAddPayload.class;
		else if (TYPE_WATCH.equals(type))
			payloadClass = WatchPayload.class;
		else
			return event;

		try {
			EventPayload typedPayload = context.deserialize(rawPayload,
					payloadClass);
			return event.setPayload(typedPayload);
		} catch (JsonParseException jpe) {
			// Parse exception here denotes legacy payloads with differing
			// fields than built-in payload classes provide
			return event;
		}
	}
}
