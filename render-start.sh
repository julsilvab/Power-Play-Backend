#!/usr/bin/env bash
set -euo pipefail

JAVA_DIR="$PWD/.render-java"
JDK_URL="https://github.com/adoptium/temurin21-binaries/releases/download/jdk-21.0.4%2B7/OpenJDK21U-jdk_x64_linux_hotspot_21.0.4_7.tar.gz"
JDK_ARCHIVE="/tmp/render-jdk.tar.gz"

ensure_java() {
  if [ -x "$JAVA_DIR/bin/java" ]; then
    return
  fi
  echo "Descargando Temurin JDK 21..."
  mkdir -p "$JAVA_DIR"
  curl -fsSL "$JDK_URL" -o "$JDK_ARCHIVE"
  tar -xzf "$JDK_ARCHIVE" --strip-components=1 -C "$JAVA_DIR"
}

ensure_java
export JAVA_HOME="$JAVA_DIR"
export PATH="$JAVA_HOME/bin:$PATH"

JAR_FILE=$(ls target/*.jar | head -n 1)
if [ -z "$JAR_FILE" ]; then
  echo "No se encontró el jar generado en target/." >&2
  exit 1
fi

exec java -jar "$JAR_FILE"
