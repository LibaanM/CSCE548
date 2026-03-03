#!/usr/bin/env bash
# Run the duplicate-authors fix against your PostgreSQL database.
# Uses DB_NAME (default: library_management) and current user unless you set DB_USER.

set -euo pipefail
cd "$(dirname "$0")"
DB_NAME="${DB_NAME:-library_management}"
DB_USER="${DB_USER:-$(whoami)}"

echo "Fixing duplicate authors in database: $DB_NAME (user: $DB_USER)"
echo "This will: 1) point all books to one author per duplicate group, 2) delete duplicate author rows."
echo ""
read -r -p "Continue? [y/N] " ans
case "${ans:-n}" in
  y|Y) ;;
  *) echo "Aborted."; exit 0 ;;
esac

psql -U "$DB_USER" -d "$DB_NAME" -f database/fix_duplicate_authors.sql
echo "Done. Restart your app and click 'Get all' on Authors again to see a single row per author."
