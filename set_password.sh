#!/bin/bash
# Script to set PostgreSQL password for your user

export PATH="/Library/PostgreSQL/18/bin:$PATH"

echo "Setting PostgreSQL password for user: $(whoami)"
echo ""
echo "You'll need to connect as the postgres superuser first."
echo ""
echo "Option 1: If you know the postgres password:"
echo "  psql -U postgres -d postgres"
echo "  Then run: ALTER USER $(whoami) PASSWORD 'mypassword';"
echo ""
echo "Option 2: Try connecting without password (if trust is enabled for postgres):"
echo "  psql -U postgres -d postgres -c \"ALTER USER $(whoami) PASSWORD 'mypassword';\""
echo ""
echo "After setting the password, run:"
echo "  export DB_PASSWORD='mypassword'"
echo "  mvn exec:java"

