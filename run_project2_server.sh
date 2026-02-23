#!/usr/bin/env bash
# Project 2: Start the Library REST API server.
# Uses PORT=7000 by default; set PORT if 7000 is in use (e.g. export PORT=7001).

cd "$(dirname "$0")"
export PORT="${PORT:-7000}"
echo "Starting Library API server on port $PORT ..."
echo "  API base URL: http://localhost:$PORT"
echo "  Stop with Ctrl+C. Then run the client in another terminal:"
echo "  BASE_URL=http://localhost:$PORT mvn exec:java -Dexec.mainClass=edu.csce548.library.client.LibraryClient"
echo ""
mvn -q exec:java -Dexec.mainClass=edu.csce548.library.api.LibraryServer
