package com.jdroid.github;

import java.io.Serializable;

public class Release implements Serializable {

	private String tagName;
	private String targetCommitish;
	private String name;
	private String body;
	private Boolean draft;
	private Boolean prerelease;

	public String getTagName() {
		return tagName;
	}

	public Release setTagName(String tagName) {
		this.tagName = tagName;
		return this;
	}

	public String getTargetCommitish() {
		return targetCommitish;
	}

	public Release setTargetCommitish(String targetCommitish) {
		this.targetCommitish = targetCommitish;
		return this;
	}

	public String getName() {
		return name;
	}

	public Release setName(String name) {
		this.name = name;
		return this;
	}

	public String getBody() {
		return body;
	}

	public Release setBody(String body) {
		this.body = body;
		return this;
	}

	public Boolean getDraft() {
		return draft;
	}

	public Release setDraft(Boolean draft) {
		this.draft = draft;
		return this;
	}

	public Boolean getPrerelease() {
		return prerelease;
	}

	public Release setPrerelease(Boolean prerelease) {
		this.prerelease = prerelease;
		return this;
	}
}
