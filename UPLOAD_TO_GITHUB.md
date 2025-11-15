# How to Upload Digital Mall to GitHub

This guide will help you upload your Digital Mall project to GitHub.

## Prerequisites

1. **Install Git** (if not already installed)
   - Download from: https://git-scm.com/download/win
   - During installation, accept all default options
   - After installation, restart your terminal/PowerShell

2. **Create a GitHub Account** (if you don't have one)
   - Go to: https://github.com
   - Sign up for a free account

3. **Create a New Repository on GitHub**
   - Log in to GitHub
   - Click the "+" icon in the top right corner
   - Select "New repository"
   - Name it: `Digital_Mall` (or any name you prefer)
   - Choose Public or Private
   - **DO NOT** initialize with README, .gitignore, or license (we already have these)
   - Click "Create repository"

## Step-by-Step Instructions

### Step 1: Open PowerShell/Terminal
Open PowerShell in your project directory (`C:\Digital_Mall`)

### Step 2: Initialize Git Repository
```powershell
git init
```

### Step 3: Add All Files
```powershell
git add .
```

### Step 4: Create Initial Commit
```powershell
git commit -m "Initial commit: Digital Mall E-Commerce Application"
```

### Step 5: Add GitHub Remote Repository
Replace `YOUR_USERNAME` with your GitHub username and `YOUR_REPOSITORY_NAME` with your repository name:
```powershell
git remote add origin https://github.com/YOUR_USERNAME/YOUR_REPOSITORY_NAME.git
```

For example:
```powershell
git remote add origin https://github.com/johndoe/Digital_Mall.git
```

### Step 6: Push to GitHub
```powershell
git branch -M main
git push -u origin main
```

**Note:** If this is your first time, GitHub will prompt you for your username and password. Use a Personal Access Token instead of your password:
- Go to GitHub → Settings → Developer settings → Personal access tokens → Tokens (classic)
- Generate a new token with `repo` permissions
- Use the token as your password

## Quick Script (After Git is Installed)

You can also run these commands in sequence:

```powershell
git init
git add .
git commit -m "Initial commit: Digital Mall E-Commerce Application"
git branch -M main
git remote add origin https://github.com/YOUR_USERNAME/YOUR_REPOSITORY_NAME.git
git push -u origin main
```

## Troubleshooting

### If Git is not recognized:
1. Make sure Git is installed
2. Restart your terminal/PowerShell after installation
3. Verify installation: `git --version`

### If you get authentication errors:
1. Use Personal Access Token instead of password
2. Make sure your GitHub username is correct
3. Check if the repository URL is correct

### If you want to update files later:
```powershell
git add .
git commit -m "Your commit message"
git push
```

## What Files Are Excluded?

The `.gitignore` file excludes:
- Compiled Java files (`.class` files)
- Log files
- Generated bill files
- IDE configuration files
- OS-specific files

This keeps your repository clean and focused on source code.

## Next Steps

After uploading:
1. Your code will be available on GitHub
2. You can share the repository URL with others
3. You can clone it on other machines using: `git clone https://github.com/YOUR_USERNAME/YOUR_REPOSITORY_NAME.git`
4. You can continue to push updates using `git add .`, `git commit -m "message"`, and `git push`

