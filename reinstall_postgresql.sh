#!/bin/bash
# Script to reinstall PostgreSQL using Homebrew (easier method)

echo "Installing PostgreSQL using Homebrew..."
echo ""

# Check if Homebrew is installed
if ! command -v brew &> /dev/null; then
    echo "❌ Homebrew not found. Installing Homebrew first..."
    /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
fi

echo "Installing PostgreSQL..."
brew install postgresql@18

echo ""
echo "Starting PostgreSQL service..."
brew services start postgresql@18

echo ""
echo "Waiting for PostgreSQL to start..."
sleep 3

echo ""
echo "Setting up PostgreSQL..."
echo ""

# Add PostgreSQL to PATH
export PATH="/opt/homebrew/opt/postgresql@18/bin:$PATH"
export PATH="/usr/local/opt/postgresql@18/bin:$PATH"

# Create a password for your user
echo "Setting password for user: $(whoami)"
echo "You'll be prompted to enter a password. Remember this password!"
echo ""

# Try to set password (may need to create user first)
psql -U $(whoami) -d postgres -c "ALTER USER $(whoami) WITH PASSWORD 'postgres';" 2>/dev/null || \
psql -U $(whoami) -d postgres -c "CREATE USER $(whoami) WITH PASSWORD 'postgres';" 2>/dev/null || \
echo "Note: You may need to set the password manually"

echo ""
echo "✓ PostgreSQL installed!"
echo ""
echo "Default password set to: postgres"
echo "You can change it later with:"
echo "  psql -U $(whoami) -d postgres -c \"ALTER USER $(whoami) PASSWORD 'newpassword';\""
echo ""
echo "To use with the Java application:"
echo "  export DB_PASSWORD='postgres'"
echo "  mvn exec:java"

