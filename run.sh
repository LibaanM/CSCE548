#!/bin/bash
# Script to run the Java application with database configuration

echo "Workout Tracker - Java Application"
echo "=================================="
echo ""

# Check if password is set
if [ -z "$DB_PASSWORD" ]; then
    echo "⚠️  Database password not set."
    echo ""
    echo "Please set the database password:"
    echo "  export DB_PASSWORD='your_password'"
    echo ""
    echo "Or run with password inline:"
    echo "  DB_PASSWORD='your_password' ./run.sh"
    echo ""
    echo "If you haven't set a password yet, you can:"
    echo "  1. Set a password for your PostgreSQL user:"
    echo "     psql -U postgres -d postgres"
    echo "     ALTER USER libaanmohamed PASSWORD 'mypassword';"
    echo ""
    echo "  2. Or configure PostgreSQL for trust authentication (no password needed)"
    echo "     See README.md for instructions"
    echo ""
    exit 1
fi

echo "Starting application..."
echo "Database: ${DB_NAME:-workout_tracker}"
echo "User: ${DB_USER:-$(whoami)}"
echo ""

mvn exec:java

