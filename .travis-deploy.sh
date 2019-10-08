#!/bin/bash

if [ "$TRAVIS_REPO_SLUG" == "Blazebit/blaze-expression" ] &&
    [ "$TRAVIS_BRANCH" == "master" ] &&
    [ "$TRAVIS_PULL_REQUEST" == "false" ]; then

  echo "Starting snapshot deployment..."
  mvn -B -P blazebit-release -s .travis-settings.xml -DperformRelease -DskipTests -Dgpg.skip=true -Dquiet=true clean deploy
  echo "Snapshots deployed!"

else
  echo "Skipping snapshot deployment..."
fi
