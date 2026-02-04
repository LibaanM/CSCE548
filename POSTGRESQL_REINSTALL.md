# PostgreSQL Reinstallation Guide

## Method 1: Uninstall and Reinstall with Homebrew (Recommended)

### Step 1: Uninstall Current PostgreSQL

```bash
# Run the uninstall script
./uninstall_postgresql.sh

# Or manually:
sudo launchctl stop com.edb.postgresql18.server
sudo launchctl unload /Library/LaunchDaemons/com.edb.postgresql18.server.plist
sudo rm -rf /Library/PostgreSQL/18
sudo rm -rf /Library/LaunchDaemons/com.edb.postgresql18.server.plist
```

### Step 2: Install with Homebrew

```bash
# Install Homebrew if you don't have it
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"

# Install PostgreSQL
brew install postgresql@18

# Start the service
brew services start postgresql@18
```

### Step 3: Set Password

```bash
# Add PostgreSQL to PATH
export PATH="/opt/homebrew/opt/postgresql@18/bin:$PATH"
# OR if on Intel Mac:
# export PATH="/usr/local/opt/postgresql@18/bin:$PATH"

# Set password for your user
psql -U $(whoami) -d postgres -c "ALTER USER $(whoami) WITH PASSWORD 'mypassword';"
```

### Step 4: Use with Application

```bash
export DB_NAME=library_management
export DB_USER=$(whoami)
export DB_PASSWORD='mypassword'
mvn exec:java
```

---

## Method 2: Download Official Installer

### Step 1: Uninstall Current PostgreSQL

```bash
./uninstall_postgresql.sh
```

### Step 2: Download and Install

1. Go to: https://www.postgresql.org/download/macosx/
2. Download PostgreSQL 18 installer
3. Run the installer
4. **During installation, set a password for the postgres superuser**
5. Note the port (usually 5432)

### Step 3: Set Password for Your User

```bash
export PATH="/Library/PostgreSQL/18/bin:$PATH"

# Connect as postgres (use password you set during installation)
psql -U postgres -d postgres

# In psql, set password for your user:
ALTER USER libaanmohamed PASSWORD 'mypassword';
\q
```

### Step 4: Use with Application

```bash
export DB_NAME=library_management
export DB_USER=libaanmohamed
export DB_PASSWORD='mypassword'
mvn exec:java
```

---

## Method 3: Quick Reinstall Script

```bash
# Run the automated reinstall script
./reinstall_postgresql.sh
```

This will:
- Install PostgreSQL via Homebrew
- Start the service
- Set a default password ('postgres')
- Show you how to use it

---

## Troubleshooting

### Find PostgreSQL Location

```bash
# Homebrew installation
which psql
# Usually: /opt/homebrew/opt/postgresql@18/bin/psql (Apple Silicon)
# Or: /usr/local/opt/postgresql@18/bin/psql (Intel)

# Official installer
# Usually: /Library/PostgreSQL/18/bin/psql
```

### Check if PostgreSQL is Running

```bash
# Homebrew
brew services list | grep postgresql

# Official installer
sudo launchctl list | grep postgresql
```

### Reset Password if You Forgot

```bash
# Stop PostgreSQL
brew services stop postgresql@18
# OR
sudo launchctl stop com.edb.postgresql18.server

# Start in single-user mode (Homebrew)
postgres --single -D /opt/homebrew/var/postgresql@18 postgres

# Then in psql:
ALTER USER your_username WITH PASSWORD 'newpassword';
```

---

## Recommended: Use Homebrew Method

Homebrew is easier to manage and update. The official installer is more complex but gives you a GUI installer.

