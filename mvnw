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

if [ -z "$MAVEN_SKIP_RC" ] ; then

  if [ -f /usr/local/etc/mavenrc ] ; then
    . /usr/local/etc/mavenrc
  fi

  if [ -f "$HOME/.mavenrc" ] ; then
    . "$HOME/.mavenrc"
  fi

fi

cygwin=false
mingw=false
case "$(uname)" in
  CYGWIN*) cygwin=true ;;
  MINGW*) mingw=true ;;
  MSYS*) mingw=true ;;
  WIN*) mingw=true ;;
  *) ;;
esac

if [ -z "$JAVA_HOME" ] ; then
  if [ -x "/usr/libexec/java_home" ] ; then
    JAVA_HOME="$(/usr/libexec/java_home)"
  elif [ -x "/usr/lib/jvm/default-java/bin/java" ] ; then
    JAVA_HOME="/usr/lib/jvm/default-java"
  fi
fi

if [ -z "$JAVA_HOME" ] ; then
  JAVACMD="$(command -v java)"
else
  JAVACMD="$JAVA_HOME/bin/java"
fi

if [ ! -x "$JAVACMD" ] ; then
  echo "Error: JAVA_HOME is not defined correctly." >&2
  echo "  We cannot execute $JAVACMD" >&2
  exit 1
fi

if [ -z "$MAVEN_PROJECTBASEDIR" ] ; then
  PRG="$0"
  while [ -h "$PRG" ] ; do
    ls="$(ls -ld "$PRG")"
    link="$(expr "$ls" : '.*-> \(.*\)$')"
    if expr "$link" : '/.*' >/dev/null ; then
      PRG="$link"
    else
      PRG="$(dirname "$PRG")/$link"
    fi
  done
  SAVED="$(pwd)"
  cd "$(dirname "$PRG")" >/dev/null
  MAVEN_PROJECTBASEDIR="$(pwd -P)"
  cd "$SAVED" >/dev/null
fi

MAVEN_WRAPPER_PROPERTIES="${MAVEN_PROJECTBASEDIR}/.mvn/wrapper/maven-wrapper.properties"
if [ ! -f "$MAVEN_WRAPPER_PROPERTIES" ] ; then
  echo "Cannot find $MAVEN_WRAPPER_PROPERTIES" >&2
  exit 1
fi

while IFS="=" read -r key value; do
  case "$key" in
    distributionUrl) distributionUrl="$value" ;;
    wrapperUrl) wrapperUrl="$value" ;;
  esac
done < "$MAVEN_WRAPPER_PROPERTIES"

wrapperJar="${MAVEN_PROJECTBASEDIR}/.mvn/wrapper/maven-wrapper.jar"
if [ ! -f "$wrapperJar" ] ; then
  jarDir="$(dirname "$wrapperJar")"
  mkdir -p "$jarDir"
  if command -v curl >/dev/null 2>&1 ; then
    curl --silent --location "${wrapperUrl}" --output "$wrapperJar"
  elif command -v wget >/dev/null 2>&1 ; then
    wget --quiet "${wrapperUrl}" -O "$wrapperJar"
  else
    echo "Cannot download Maven wrapper jar" >&2
    exit 1
  fi
fi

if $cygwin || $mingw ; then
  MAVEN_PROJECTBASEDIR="$(cygpath --path --windows "$MAVEN_PROJECTBASEDIR")"
  wrapperJar="$(cygpath --path --windows "$wrapperJar")"
fi

exec "$JAVACMD" $MAVEN_OPTS \
  -classpath "$wrapperJar" \
  -Dmaven.multiModuleProjectDirectory="$MAVEN_PROJECTBASEDIR" \
  org.apache.maven.wrapper.MavenWrapperMain "$@"
