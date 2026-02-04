#!/bin/bash
# Setup script for Library Management System database

export PATH="/Library/PostgreSQL/18/bin:$PATH"

DB_NAME="library_management"
DB_USER="${DB_USER:-$(whoami)}"

echo "Setting up Library Management System database..."
echo "Database: $DB_NAME"
echo "User: $DB_USER"
echo ""

# Create database using psql
echo "Creating database..."
psql -U "$DB_USER" -d postgres -c "CREATE DATABASE $DB_NAME;" 2>&1

if [ $? -eq 0 ]; then
    echo "✓ Database created successfully"
else
    echo "⚠ Database might already exist or there was an error"
fi

echo ""
echo "Loading schema..."
psql -U "$DB_USER" -d "$DB_NAME" -f database/schema.sql 2>&1 | grep -E "(ERROR|CREATE|ALTER)" | head -20

echo ""
echo "Loading seed data..."
psql -U "$DB_USER" -d "$DB_NAME" -f database/seed_data.sql 2>&1 | tail -5

echo ""
echo "Verifying data..."
psql -U "$DB_USER" -d "$DB_NAME" -f verify_data.sql 2>&1 | grep -A 10 "total_loans"

echo ""
echo "Setup complete!"
echo ""
echo "To run the application:"
echo "  export DB_NAME=$DB_NAME"
echo "  export DB_USER=$DB_USER"
echo "  export DB_PASSWORD='your_password'  # if needed"
echo "  mvn exec:java"
