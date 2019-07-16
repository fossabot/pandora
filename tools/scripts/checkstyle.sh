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

trap on_error ERR

## Create temp file
readonly LOG_FILE=$(mktemp -t XXXcheckstyle-log)
readonly RESULT_FILE=$(mktemp -t XXXcheckstyle-result)

${BASE_DIR}/mvnw checkstyle:checkstyle-aggregate \
    -f ${BASE_DIR}/pom.xml \
    -Dcheckstyle.config.location=${BASE_DIR}/tools/checkstyle.xml \
    -Dcheckstyle.suppressions.location=${BASE_DIR}/tools/checkstyle-suppression.xml \
    -Dcheckstyle.output.format=plain \
    -Dcheckstyle.output.file=${RESULT_FILE} > ${LOG_FILE} 2>&1 || (cat ${LOG_FILE} ; exit 1)

grep "^\[ERROR\]" ${RESULT_FILE} \
    && die "CHECKSTYLE ERROR" || echo "CHECKSTYLE OK"
