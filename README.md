# Java Connector for GitHub API

## Continuous Integration
|Branch|Status|Workflows|Insights|
| ------------- | ------------- | ------------- | ------------- |
|master|[![CircleCI](https://circleci.com/gh/maxirosson/jdroid-java-github/tree/master.svg?style=svg)](https://circleci.com/gh/maxirosson/jdroid-java-github/tree/master)|[Workflows](https://circleci.com/gh/maxirosson/workflows/jdroid-java-github/tree/master)|[Insights](https://circleci.com/build-insights/gh/maxirosson/jdroid-java-github/master)|
|production|[![CircleCI](https://circleci.com/gh/maxirosson/jdroid-java-github/tree/production.svg?style=svg)](https://circleci.com/gh/maxirosson/jdroid-java-github/tree/production)|[Workflows](https://circleci.com/gh/maxirosson/workflows/jdroid-java-github/tree/production)|[Insights](https://circleci.com/build-insights/gh/maxirosson/jdroid-java-github/production)|

## Setup 

Add the following configuration to your `build.gradle`, replacing X.Y.Z by the [latest version](https://github.com/maxirosson/jdroid-java-github/releases/latest)

    repositories {
      jcenter()
    }
  
    dependencies {
      implementation 'com.jdroidtools:jdroid-java-github:X.Y.Z'
    }

## Usage

### Create a Release

    GitHubClient client = new GitHubClient();
    client.setOAuth2Token("GITHUB_OATH_TOKEN");
    IRepositoryIdProvider repositoryIdProvider = RepositoryId.create("REPOSITORY_OWNER", "REPOSITORY_NAME");
    
	Release release = new Release();
	release.setBody("RELEASE_NOTES");
	release.setDraft(false);
	release.setName("RELEASE_NAME");
	release.setTagName("RELEASE_TAG_NAME");
	release.setPrerelease(false);
	release.setTargetCommitish("BRANCH_NAME");

	ReleaseService releaseService = new ReleaseService(client);
	releaseService.createRelease(repositoryIdProvider, release);
    
## Donations
Help us to continue with this project:

[![Donate](https://www.paypalobjects.com/en_US/i/btn/btn_donate_LG.gif)](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=2UEBTRTSCYA9L)
