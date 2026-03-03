#!/usr/bin/env bash
# Run the Library API server (serves the Project 3 web client at http://localhost:7000)
# Prerequisite: PostgreSQL running, DB_* env vars set (e.g. export DB_PASSWORD=postgres)
set -e
cd "$(dirname "$0")"
echo "Starting Library API server with web client..."
echo "Open http://localhost:7000 in your browser when the server is ready."
echo ""
mvn exec:java -Dexec.mainClass=edu.csce548.library.api.LibraryServer
