#!/bin/bash
# Script to uninstall PostgreSQL 18 on macOS

echo "⚠️  WARNING: This will completely remove PostgreSQL 18 and all databases!"
echo "Press Ctrl+C to cancel, or Enter to continue..."
read

echo ""
echo "Stopping PostgreSQL service..."
sudo launchctl stop com.edb.postgresql18.server
sudo launchctl unload /Library/LaunchDaemons/com.edb.postgresql18.server.plist 2>/dev/null

echo ""
echo "Removing PostgreSQL files..."
sudo rm -rf /Library/PostgreSQL/18
sudo rm -rf /Library/LaunchDaemons/com.edb.postgresql18.server.plist
sudo rm -rf ~/Library/Preferences/com.edb.postgresql18.*
sudo rm -rf ~/Library/Application\ Support/PostgreSQL\ 18

echo ""
echo "Removing PostgreSQL user (if exists)..."
sudo dscl . -delete /Users/postgres 2>/dev/null

echo ""
echo "✓ PostgreSQL 18 uninstalled!"
echo ""
echo "To reinstall, download from:"
echo "  https://www.postgresql.org/download/macosx/"
echo ""
echo "Or use Homebrew:"
echo "  brew install postgresql@18"

