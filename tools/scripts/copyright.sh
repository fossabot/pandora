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

source $(dirname $0)/common-function.sh
project_basedir
cd ${BASE_DIR}

trap on_error ERR

readonly RESULT_FILE=$(mktemp -t XXXcopyright-result)

${BASE_DIR}/mvnw -q org.glassfish.copyright:glassfish-copyright-maven-plugin:copyright \
        -f ${BASE_DIR}/pom.xml \
        -Dcopyright.exclude=${BASE_DIR}/tools/copyright-exclude.txt \
        -Dcopyright.template=${BASE_DIR}/tools/copyright.txt \
        -Dcopyright.scm=git > ${RESULT_FILE} || die "Error running the Maven command"

grep -i "copyright" ${RESULT_FILE} && die "COPYRIGHT ERROR" || echo "COPYRIGHT OK"
