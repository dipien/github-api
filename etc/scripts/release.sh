#!/bin/sh
set -e

# The path to a directory where the code will be checked out and the assemblies would be generated. For example: /home/user/build. Required.
BUILD_DIRECTORY=$1
GITHUB_WRITE_TOKEN=$2
GITHUB_READ_TOKEN=$3
GITHUB_EMAIL=$4

# Whether the source code and assemblies on the build directory should be cleaned or not
CLEAN=false

REPOSITORY_OWNER=maxirosson
PROJECT_NAME=jdroid-java-github

PROJECT_DIRECTORY=$BUILD_DIRECTORY/$PROJECT_NAME
SOURCE_DIRECTORY=$PROJECT_DIRECTORY/sources
ASSEMBLIES_DIRECTORY=$PROJECT_DIRECTORY/assemblies
PROJECT_HOME=$SOURCE_DIRECTORY/$PROJECT_NAME

# ************************
# Parameters validation
# ************************

if [ -z "$BUILD_DIRECTORY" ]
then
	echo "[ERROR] The BUILD_DIRECTORY parameter is required"
	exit 1;
fi

if [ ! -d "$BUILD_DIRECTORY" ]
then
	echo "[ERROR] - The BUILD_DIRECTORY directory does not exist."
	exit 1;
fi

if [ -z "$GITHUB_WRITE_TOKEN" ]
then
	echo "[ERROR] The GITHUB_WRITE_TOKEN parameter is required"
	exit 1;
fi

# ************************
# Checking out
# ************************

if [ "$CLEAN" = "true" ] || [ ! -d "$SOURCE_DIRECTORY" ]
then
	# Clean the directories
	rm -r -f $SOURCE_DIRECTORY
	mkdir -p $SOURCE_DIRECTORY

	# Checkout the project
	cd $SOURCE_DIRECTORY
	echo Cloning git@github.com:$REPOSITORY_OWNER/$PROJECT_NAME.git
	git clone git@github.com:$REPOSITORY_OWNER/$PROJECT_NAME.git $PROJECT_NAME
fi
cd $PROJECT_HOME
git config user.email $GITHUB_EMAIL

# ************************
# Synch production branch
# ************************

git add -A
git stash
git checkout production
git pull

VERSION=`./gradlew :printVersion -q --configure-on-demand -PSNAPSHOT=false`

# ************************
# Close Milestone on GitHub
# Upload Release on GitHub
# Generate Change Log
# ************************

./gradlew :closeGitHubMilestone :createGitHubRelease :generateChangelogTask --configure-on-demand --refresh-dependencies -PSNAPSHOT=false -PREPOSITORY_OWNER=$REPOSITORY_OWNER -PREPOSITORY_NAME=$PROJECT_NAME -PGITHUB_WRITE_TOKEN=$GITHUB_WRITE_TOKEN -PGITHUB_READ_TOKEN=$GITHUB_READ_TOKEN

# ************************
# Deploy to Sonatype repository
# ************************

cd $PROJECT_HOME

cmd="./gradlew clean uploadArchives -PSNAPSHOT=false -PLOCAL_UPLOAD=false"

echo "Executing the following command"
echo "${cmd}"
eval "${cmd}"
