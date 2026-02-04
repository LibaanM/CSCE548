# MySQL Workbench Setup Guide

## Files Created

I've created MySQL-compatible versions of your database files:
- `database/schema_mysql.sql` - MySQL schema (5 tables)
- `database/seed_data_mysql.sql` - MySQL seed data (54+ loans)

## How to Import into MySQL Workbench

### Step 1: Open MySQL Workbench

1. Launch MySQL Workbench
2. Connect to your MySQL server (create a connection if needed)

### Step 2: Create Database

1. Click the **SQL+** button (or go to File → New Query Tab)
2. Run this command:
   ```sql
   CREATE DATABASE library_management;
   USE library_management;
   ```

### Step 3: Import Schema

**Option A: Copy and Paste**
1. Open `database/schema_mysql.sql` in a text editor
2. Copy all the SQL code
3. Paste it into MySQL Workbench query tab
4. Click the **Execute** button (lightning bolt icon) or press `Ctrl+Enter` (Windows) / `Cmd+Enter` (Mac)

**Option B: Import from File**
1. In MySQL Workbench, go to **File → Run SQL Script**
2. Navigate to `database/schema_mysql.sql`
3. Click **Open**
4. The script will execute automatically

### Step 4: Import Seed Data

1. Open `database/seed_data_mysql.sql` in a text editor
2. Copy all the SQL code
3. Paste it into MySQL Workbench query tab
4. Click **Execute**

### Step 5: Verify Tables Created

Run this query to see all tables:
```sql
SHOW TABLES;
```

You should see:
- `authors`
- `book_categories`
- `books`
- `loans`
- `members`

### Step 6: Verify Data Count

Run this query to verify you have 50+ loans:
```sql
SELECT COUNT(*) as total_loans FROM loans;
```

Should show **54 loans**.

## Create Database Diagram in MySQL Workbench

### Step 1: Open Database Reverse Engineering

1. Go to **Database → Reverse Engineer...**
2. Click **Next**
3. Select your connection and click **Next**
4. Select `library_management` database
5. Click **Next** → **Next** → **Execute**
6. Click **Next** → **Finish**

### Step 2: View ER Diagram

1. In the left sidebar, expand `library_management`
2. Click on **Schemas** tab
3. Right-click on `library_management` → **Database → Reverse Engineer...**
4. Or go to **Database → Reverse Engineer...** again
5. The ER diagram will show all 5 tables with relationships

### Step 3: Export Diagram

1. In the ER diagram view, go to **File → Export → Export as PNG**
2. Or take a screenshot for your submission

## Differences: PostgreSQL vs MySQL

The MySQL version has these changes:
- `SERIAL` → `INT AUTO_INCREMENT`
- `CURRENT_DATE` → `CURDATE()`
- Email regex uses MySQL `REGEXP` instead of PostgreSQL `~*`
- Some constraint syntax differences

## Quick Reference

**Files to use in MySQL Workbench:**
- `database/schema_mysql.sql` - Create tables
- `database/seed_data_mysql.sql` - Insert test data

**Files for PostgreSQL (your Java app):**
- `database/schema.sql` - PostgreSQL schema
- `database/seed_data.sql` - PostgreSQL seed data

## Troubleshooting

**Error: "Table already exists"**
- Run `DROP DATABASE library_management;` first
- Then `CREATE DATABASE library_management;`

**Error: "Foreign key constraint fails"**
- Make sure you run the schema first (creates tables)
- Then run seed data (inserts data)

**Can't see ER diagram**
- Make sure you've run the Reverse Engineer wizard
- Check that all tables are created successfully

## For Submission

After creating the database in MySQL Workbench:
1. **Take screenshot of ER diagram** - Shows all 5 tables and relationships
2. **Take screenshot of table structure** - Shows columns, data types, constraints
3. **Take screenshot of record counts** - Shows 54 loans

These screenshots can be included in your submission PDF!

