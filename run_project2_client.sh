#!/usr/bin/env bash
# Project 2: Run the console client to test the API (CRUD).
# Start the server first in another terminal: ./run_project2_server.sh
# If the server uses a different port: BASE_URL=http://localhost:7001 ./run_project2_client.sh

cd "$(dirname "$0")"
BASE_URL="${BASE_URL:-http://localhost:7000}"
echo "Using API at: $BASE_URL"
echo ""
mvn -q exec:java -Dexec.mainClass=edu.csce548.library.client.LibraryClient
