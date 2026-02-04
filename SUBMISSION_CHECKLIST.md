# CSCE 548 Project 1 - Submission Checklist

## What to Submit

Based on the project requirements, you need to submit:

1. **Screenshots** (see list below)
2. **GitHub Repository Link**

You can attach these directly to Blackboard OR put them in a PDF document (PDF only - NOT Word, Google Docs, text, or Pages).

---

## Required Screenshots

### 1. Database Schema Screenshot
- Show your database structure (tables, relationships)
- You can use a database diagram tool or show the schema.sql file
- Should show all 5 tables: `book_categories`, `authors`, `members`, `books`, `loans`

### 2. Database Record Count Screenshot
- Run the application
- Select menu option **10** (Display database record counts)
- Screenshot showing the counts for all tables
- Should show 50+ loans (you have 54 ✓)

### 3. Application Running Screenshot
- Screenshot of the main menu when the application starts
- Shows the console menu with all 10 options

### 4. Complex Query Screenshot (JOINs)
- Run the application
- Select menu option **6** (Display loans with details - uses JOINs)
- Screenshot showing the results with joined data from multiple tables

### 5. Additional Query Screenshot
- Run the application
- Select menu option **7** (Display detailed loan information) or **8** (Display member loan summary)
- Screenshot showing detailed query results

### 6. Code Structure Screenshot
- Show your project structure (src/main/java directory)
- Should show all model classes, DAOs, and Main.java

---

## GitHub Repository Setup

### Step 1: Initialize Git (if not already done)
```bash
cd /Users/libaanmohamed/CSCE548
git init
```

### Step 2: Add All Files
```bash
git add .
```

### Step 3: Create Initial Commit
```bash
git commit -m "Initial commit: Library Management System - CSCE 548 Project 1"
```

### Step 4: Create GitHub Repository
1. Go to https://github.com
2. Click "New repository"
3. Name it: `csce548-project1-library-management` (or similar)
4. **Don't** initialize with README, .gitignore, or license
5. Click "Create repository"

### Step 5: Push to GitHub
```bash
git remote add origin https://github.com/YOUR_USERNAME/YOUR_REPO_NAME.git
git branch -M main
git push -u origin main
```

---

## Submission Format

### Option 1: Direct Attachment
- Attach screenshots directly to Blackboard
- Include GitHub repo link in submission comments

### Option 2: PDF Document
- Create a PDF with all screenshots
- Include GitHub repo link in the PDF
- **Important:** Must be PDF format only (not Word, Google Docs, etc.)

---

## Quick Screenshot Guide

### To Take Screenshots on macOS:
1. **Full Screen:** `Cmd + Shift + 3`
2. **Selected Area:** `Cmd + Shift + 4`
3. **Window:** `Cmd + Shift + 4`, then press `Spacebar`, click window

### Screenshot Locations:
1. **Database Schema:** Show `database/schema.sql` or database diagram
2. **Application Menu:** Run `mvn exec:java`, screenshot menu
3. **Record Counts:** Menu option 10
4. **JOIN Query:** Menu option 6
5. **Detailed Query:** Menu option 7 or 8
6. **Code Structure:** Show `src/main/java` directory in your IDE

---

## Verification Checklist

Before submitting, verify:

- [ ] All 5 tables created in database
- [ ] 50+ rows in loans table (you have 54 ✓)
- [ ] Application compiles: `mvn clean compile` ✓
- [ ] Application runs: `mvn exec:java` ✓
- [ ] All 10 menu options work
- [ ] Complex queries with JOINs work (menu options 6, 7, 8, 9)
- [ ] GitHub repository created and pushed
- [ ] All screenshots taken
- [ ] PDF created (if using Option 2)

---

## Example Submission Structure

```
Submission:
├── Screenshot 1: Database Schema
├── Screenshot 2: Record Counts (showing 54 loans)
├── Screenshot 3: Main Menu
├── Screenshot 4: JOIN Query Results
├── Screenshot 5: Detailed Query Results
├── Screenshot 6: Code Structure
└── GitHub Link: https://github.com/YOUR_USERNAME/YOUR_REPO
```

---

## Need Help?

If you need help with:
- **GitHub setup:** See "GitHub Repository Setup" above
- **Taking screenshots:** Use macOS screenshot shortcuts
- **Creating PDF:** Use Preview app or online PDF tools
- **Running application:** Use `./run_app.sh` or see README.md

