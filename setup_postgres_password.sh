#!/bin/bash
# Script to set PostgreSQL password after installation

export PATH="/Library/PostgreSQL/18/bin:$PATH"

echo "PostgreSQL Password Setup"
echo "========================"
echo ""

# Check if PostgreSQL is running
if ! pg_isready -U postgres > /dev/null 2>&1; then
    echo "⚠️  PostgreSQL doesn't seem to be running."
    echo "Starting PostgreSQL..."
    sudo launchctl start com.edb.postgresql18.server
    sleep 2
fi

echo "Attempting to set password..."
echo ""

# Try method 1: Connect as postgres user (might work with peer auth)
echo "Method 1: Try setting password as postgres user..."
psql -U postgres -d postgres -c "ALTER USER $(whoami) WITH PASSWORD 'postgres';" 2>&1

if [ $? -eq 0 ]; then
    echo "✓ Password set successfully!"
    echo ""
    echo "Password for user '$(whoami)': postgres"
    echo ""
    echo "To use with your application:"
    echo "  export DB_USER=$(whoami)"
    echo "  export DB_PASSWORD='postgres'"
    echo "  mvn exec:java"
    exit 0
fi

# Try method 2: Create user if it doesn't exist
echo ""
echo "Method 2: Creating user if needed..."
psql -U postgres -d postgres -c "CREATE USER $(whoami) WITH PASSWORD 'postgres';" 2>&1

# Try method 3: Configure trust authentication
echo ""
echo "Method 3: Configuring trust authentication (no password needed)..."
PG_HBA="/Library/PostgreSQL/18/data/pg_hba.conf"

if [ -f "$PG_HBA" ]; then
    echo "Found pg_hba.conf at: $PG_HBA"
    echo ""
    echo "To enable trust authentication (no password), edit this file:"
    echo "  sudo nano $PG_HBA"
    echo ""
    echo "Change these lines:"
    echo "  FROM: local   all   all   scram-sha-256"
    echo "  TO:   local   all   all   trust"
    echo ""
    echo "  FROM: host    all   all   127.0.0.1/32   scram-sha-256"
    echo "  TO:   host    all   all   127.0.0.1/32   trust"
    echo ""
    echo "Then restart PostgreSQL:"
    echo "  sudo launchctl stop com.edb.postgresql18.server"
    echo "  sudo launchctl start com.edb.postgresql18.server"
else
    echo "Could not find pg_hba.conf. It might be at:"
    echo "  /Library/PostgreSQL/18/data/pg_hba.conf"
fi

echo ""
echo "Alternative: Try connecting interactively:"
echo "  psql -U postgres -d postgres"
echo "  Then run: ALTER USER $(whoami) WITH PASSWORD 'postgres';"

