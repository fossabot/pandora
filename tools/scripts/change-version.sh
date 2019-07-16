#!/usr/bin/env bash
#
# Copyright (c) 2019 Artistian and/or its affiliates. All rights reserved.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

set -o pipefail || true  # trace ERR through pipes
set -o errtrace || true # trace ERR through commands and functions
set -o errexit || true  # exit the script if any statement returns a non-true return value

## Include common shell function and environment variables
source $(dirname $0)/common-function.sh
project_basedir
cd ${BASE_DIR}

function project_version_commit() {
  COMMIT_MESSAGE=""
  if [[ `is_maven_snapshot $1` == "1" ]]; then
    COMMIT_MESSAGE="Prepare to next development, change project version to: $1"
  else
    COMMIT_MESSAGE="Release project, change project version to: $1"
  fi

  git_commit "${COMMIT_MESSAGE}"
}

null_variable "$1" "\033[31mUsage: sh change-version.sh [project version]\033[0m"

## Change version in pom.xml
mvn versions:set -DnewVersion=$1
find . -type f -name 'pom.xml.versionsBackup' -exec rm -rf {} \;

## Commit the change to git
if [[ "$2" != "--no-commit" ]]; then
  project_version_commit $1
fi

echo -e "\n\033[31mThe project's version was successfully changed to: [$1]\033[0m"
