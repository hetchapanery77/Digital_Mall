# PowerShell script to upload Digital Mall to GitHub
# Make sure Git is installed before running this script

Write-Host "=== Digital Mall GitHub Upload Script ===" -ForegroundColor Green
Write-Host ""

# Check if Git is installed
try {
    $gitVersion = git --version
    Write-Host "Git is installed: $gitVersion" -ForegroundColor Green
} catch {
    Write-Host "ERROR: Git is not installed!" -ForegroundColor Red
    Write-Host "Please install Git from: https://git-scm.com/download/win" -ForegroundColor Yellow
    Write-Host "After installation, restart PowerShell and run this script again." -ForegroundColor Yellow
    exit 1
}

Write-Host ""
Write-Host "Before proceeding, make sure you have:" -ForegroundColor Yellow
Write-Host "1. Created a GitHub repository" -ForegroundColor Yellow
Write-Host "2. Have your GitHub username and repository name ready" -ForegroundColor Yellow
Write-Host ""

# Get GitHub username
$username = Read-Host "Enter your GitHub username"
if ([string]::IsNullOrWhiteSpace($username)) {
    Write-Host "Username cannot be empty!" -ForegroundColor Red
    exit 1
}

# Get repository name
$repoName = Read-Host "Enter your GitHub repository name"
if ([string]::IsNullOrWhiteSpace($repoName)) {
    Write-Host "Repository name cannot be empty!" -ForegroundColor Red
    exit 1
}

$remoteUrl = "https://github.com/$username/$repoName.git"

Write-Host ""
Write-Host "Repository URL: $remoteUrl" -ForegroundColor Cyan
$confirm = Read-Host "Is this correct? (Y/N)"
if ($confirm -ne "Y" -and $confirm -ne "y") {
    Write-Host "Upload cancelled." -ForegroundColor Yellow
    exit 0
}

Write-Host ""
Write-Host "Initializing Git repository..." -ForegroundColor Green
git init

Write-Host "Adding files..." -ForegroundColor Green
git add .

Write-Host "Creating initial commit..." -ForegroundColor Green
git commit -m "Initial commit: Digital Mall E-Commerce Application"

Write-Host "Setting main branch..." -ForegroundColor Green
git branch -M main

Write-Host "Adding remote repository..." -ForegroundColor Green
git remote add origin $remoteUrl

Write-Host ""
Write-Host "Pushing to GitHub..." -ForegroundColor Green
Write-Host "You may be prompted for your GitHub credentials." -ForegroundColor Yellow
Write-Host "Use your GitHub username and a Personal Access Token as password." -ForegroundColor Yellow
Write-Host ""

git push -u origin main

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "SUCCESS! Your project has been uploaded to GitHub!" -ForegroundColor Green
    Write-Host "Repository URL: $remoteUrl" -ForegroundColor Cyan
} else {
    Write-Host ""
    Write-Host "ERROR: Push failed. Please check:" -ForegroundColor Red
    Write-Host "1. Your GitHub credentials" -ForegroundColor Yellow
    Write-Host "2. The repository URL is correct" -ForegroundColor Yellow
    Write-Host "3. The repository exists on GitHub" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "If you need to update the remote URL, use:" -ForegroundColor Yellow
    Write-Host "git remote set-url origin $remoteUrl" -ForegroundColor Cyan
}

