#!/bin/bash
set -ev
if [ "${TRAVIS_PULL_REQUEST}" = "false" ]; then
    mvn --settings travis/settings.xml sonar:sonar -Dsonar.branch=`git name-rev --name-only HEAD`
else
    mvn --settings travis/settings.xml sonar:sonar -Dsonar.branch=`git name-rev --name-only HEAD` -Dsonar.analysis.mode=preview -Dsonar.issuesReport.console.enable=true
fi
