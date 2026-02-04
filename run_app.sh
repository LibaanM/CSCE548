#!/bin/bash
# Quick script to run the Library Management System application

export PATH="/Library/PostgreSQL/18/bin:$PATH"
export DB_NAME=library_management
export DB_USER=libaanmohamed
export DB_PASSWORD='postgres'

echo "Starting Library Management System..."
echo "Database: $DB_NAME"
echo "User: $DB_USER"
echo ""

mvn exec:java
