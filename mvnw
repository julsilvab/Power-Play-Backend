#!/bin/sh
# ----------------------------------------------------------------------------
# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements. See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership. The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License. You may obtain a copy of the License at
#
#    https://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied. See the License for the
# specific language governing permissions and limitations
# under the License.
# ----------------------------------------------------------------------------

set -euo pipefail

# resolve links - $0 may be a symlink
PRG="$0"
while [ -h "$PRG" ]; do
  ls="$(ls -ld "$PRG")"
  link="$(expr "$ls" : '.*-> \(.*\)$')"
  if expr "$link" : '/.*' >/dev/null; then
    PRG="$link"
  else
    PRG="$(dirname "$PRG")/$link"
  fi
done
PRG_DIR="$(dirname "$PRG")"
MAVEN_PROJECTBASEDIR="$(cd "$PRG_DIR"; pwd -P)"
[ -z "$MAVEN_PROJECTBASEDIR" ] && MAVEN_PROJECTBASEDIR=.

MAVEN_WRAPPER_PROPERTIES="${MAVEN_PROJECTBASEDIR}/.mvn/wrapper/maven-wrapper.properties"
MAVEN_WRAPPER_JAR="${MAVEN_PROJECTBASEDIR}/.mvn/wrapper/maven-wrapper.jar"

if [ -z "${JAVA_HOME:-}" ]; then
  JAVA_EXE="$(command -v java || true)"
else
  JAVA_EXE="$JAVA_HOME/bin/java"
fi

if [ ! -x "$JAVA_EXE" ]; then
  echo "Error: JAVA_HOME is not defined correctly or java executable was not found." >&2
  exit 1
fi

if [ ! -f "$MAVEN_WRAPPER_PROPERTIES" ]; then
  echo "Cannot find $MAVEN_WRAPPER_PROPERTIES" >&2
  exit 1
fi

wrapperUrl=$(grep -E '^wrapperUrl=' "$MAVEN_WRAPPER_PROPERTIES" | cut -d'=' -f2-)
if [ ! -f "$MAVEN_WRAPPER_JAR" ]; then
  if command -v curl >/dev/null 2>&1; then
    curl -fsSL "$wrapperUrl" -o "$MAVEN_WRAPPER_JAR"
  elif command -v wget >/dev/null 2>&1; then
    wget -q "$wrapperUrl" -O "$MAVEN_WRAPPER_JAR"
  else
    echo "Error: Need curl or wget to download the Maven wrapper." >&2
    exit 1
  fi
fi

exec "$JAVA_EXE" \
  ${MAVEN_OPTS:-} \
  -classpath "$MAVEN_WRAPPER_JAR" \
  -Dmaven.multiModuleProjectDirectory="$MAVEN_PROJECTBASEDIR" \
  org.apache.maven.wrapper.MavenWrapperMain "$@"
