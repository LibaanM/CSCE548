# Quick MySQL Workbench Setup - 3 Simple Steps

## ✅ All-in-One Script Created!

I've created a **single SQL file** that does everything:
- Creates the database
- Creates all 5 tables
- Inserts all test data (54 loans)
- Verifies the setup

**File:** `database/create_mysql_database.sql`

---

## 🚀 How to Run (3 Steps)

### Step 1: Open MySQL Workbench
1. Launch **MySQL Workbench**
2. Connect to your MySQL server

### Step 2: Open the Script
1. Click **File → Open SQL Script**
2. Navigate to: `database/create_mysql_database.sql`
3. Click **Open**

### Step 3: Execute
1. Click the **Execute** button (⚡ lightning bolt icon)
   - OR press `Ctrl+Enter` (Windows) / `Cmd+Enter` (Mac)
2. Wait for "Query OK" messages
3. Done! ✅

---

## 📊 Verify It Worked

After running, execute this query to verify:

```sql
SELECT COUNT(*) as total_loans FROM loans;
```

Should show: **54 loans** ✓

---

## 🎨 Create ER Diagram

1. Go to **Database → Reverse Engineer...**
2. Click **Next**
3. Select your connection → **Next**
4. Select `library_management` database
5. Click **Next** → **Next** → **Execute**
6. Click **Next** → **Finish**

The ER diagram will show all 5 tables with relationships!

---

## 📸 For Submission

After creating the ER diagram:
1. **Take screenshot** of the ER diagram
2. **Take screenshot** of table structures
3. **Take screenshot** of record counts

These screenshots go in your submission PDF!

---

## ⚠️ Troubleshooting

**Error: "Table already exists"**
- The script includes `DROP TABLE IF EXISTS` - it should handle this
- If issues persist, manually run: `DROP DATABASE library_management;` first

**Error: "Foreign key constraint fails"**
- Make sure you run the **entire script** at once
- Don't run parts separately - the script is designed to run all at once

**Can't see ER diagram**
- Make sure you've completed the Reverse Engineer wizard
- Check that all 5 tables exist: `SHOW TABLES;`

---

## 📁 Files Summary

- **`database/create_mysql_database.sql`** ← **USE THIS ONE!** (Complete setup)
- `database/schema_mysql.sql` (Schema only)
- `database/seed_data_mysql.sql` (Data only)

The `create_mysql_database.sql` file has everything in one place for easy execution!

