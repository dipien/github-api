[![Dipien](https://raw.githubusercontent.com/dipien/dipien-component-builder/master/.github/dipien_logo.png)](http://www.dipien.com)

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

Donations are greatly appreciated. You can help us to pay for our domain and this project development.

* [Donate cryptocurrency](http://coinbase.dipien.com/)
* [Donate with PayPal](http://paypal.dipien.com/)
* [Donate on Patreon](http://patreon.dipien.com/)

## Follow us
* [Twitter](http://twitter.dipien.com)
* [Medium](http://medium.dipien.com)
* [Instagram](http://instagram.dipien.com)
* [Pinterest](http://pinterest.dipien.com)
* [GitHub](http://github.dipien.com)
* [Blog](http://blog.dipien.com)