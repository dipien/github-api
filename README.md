# Java Connector for GitHub API

## Continuous Integration
|Branch|Status|
| ------------- | ------------- |
|Master|[![Build Status](https://travis-ci.org/maxirosson/jdroid-java-github.svg?branch=master)](https://travis-ci.org/maxirosson/jdroid-java-github)|
|Staging|[![Build Status](https://api.travis-ci.org/maxirosson/jdroid-java-github.svg?branch=staging)](https://travis-ci.org/maxirosson/jdroid-java-github)|
|Production|[![Build Status](https://api.travis-ci.org/maxirosson/jdroid-java-github.svg?branch=production)](https://travis-ci.org/maxirosson/jdroid-java-github)|

## Setup 

Add the following configuration to your `build.gradle`:

    repositories {
      mavenCentral()
    }
  
    dependencies {
      classpath 'com.jdroidframework:jdroid-java-github:X.Y.Z'
    }
    
Replace the X.Y.Z by the [latest version](https://github.com/maxirosson/jdroid-java-github/releases/latest)

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

<a href='https://pledgie.com/campaigns/30030'><img alt='Click here to lend your support to: Jdroid and make a donation at pledgie.com !' src='https://pledgie.com/campaigns/30030.png?skin_name=chrome' border='0' ></a>
