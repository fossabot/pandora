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

function script_dir() {
  # No relative path
  cd "$(dirname $0)"
  SCRIPT_DIR=`pwd`
}

function project_basedir() {
  # No relative path
  cd "$(dirname $0)/../.."
  BASE_DIR=`pwd`
}

function null_variable() {
  if [[ "X$1" == "X" ]]; then
    echo -e $2
    exit 1
  fi
}

function is_maven_snapshot() {
  VERSION_SUFFIX=`awk -F "-" '{print $NF}' <<< $1`
  if [[ "${VERSION_SUFFIX}" == "SNAPSHOT" ]]; then
    echo "1"
  else
    echo "0"
  fi
}

function project_version() {
  set -o errexit

  MVN_VERSION=$(mvn -q \
    -Dexec.executable="echo" \
    -Dexec.args='${project.version}' \
    --non-recursive \
    org.codehaus.mojo:exec-maven-plugin:1.3.1:exec)

  echo ${MVN_VERSION}
}

function git_commit() {
  null_variable "${BASE_DIR}" "No variable BASE_DIR provided, call function project_basedir first."

  cd ${BASE_DIR}
  git add .
  git commit -m "$1"
}

function on_error() {
  CODE="${?}" && \
  set +x && \
  printf "[ERROR] Error(code=%s) occurred at %s:%s command: %s\n" \
      "${CODE}" "${BASH_SOURCE}" "${LINENO}" "${BASH_COMMAND}"
}

function die() {
  echo "${1}";
  exit 1;
}
