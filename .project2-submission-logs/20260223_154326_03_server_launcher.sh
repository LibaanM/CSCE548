#!/usr/bin/env bash
cd "/Users/libaanmohamed/CSCE548"
export PORT=7002
export DB_PASSWORD="postgres"
echo "Library API server starting on http://localhost:$PORT ..."
exec mvn -q exec:java -Dexec.mainClass=edu.csce548.library.api.LibraryServer
