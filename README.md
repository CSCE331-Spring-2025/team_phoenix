# GitHub Workflow
pull -> add -> commit -> push -> merge -> pull

Basic git commands:
- I like to think of 'commit' as a shipping box or package
  - *git add filename* = putting items in the box
  - *git commit -m "bla bla bla"* = attaching a label to the box
  - *git push* = the box is sent to the destination
- *git checkout* is used to switch branches (doesn't fit in the analogy just useful to know)

General things to note:
- Each new branch should represent a 'feature' that you want to implement
  - This keeps the repository clean and lets everyone work on things separately
  - I think you guys are already doing this pretty well
- Main branch ideally always has stable/working code
  - unfinished features should stay in their respective branches
  - if you're a bozo and push bad code to main for whatever reason, document it and tell everyone
- Repository (repo) refers to the entire project, including all files and branches
  - Remote Repository = what you see on github.com
  - Local Repository = what you have saved on your computer
- Git tracks changes per file, not the whole repository or branch (Miles was asking about this kind of scenario)
  - Even if your branch has an older version of some file, Git recognizes that you didn’t modify it and will keep the updated version in main.
- I wrote these steps based on using git through the command line, you can do everything using the graphical interface if you prefer

(lmk if you have any other questions)

## 1. Create a new branch
- Always pull from main branch before starting so your files are up-to-date:
```
git checkout main
git pull origin main
```
- Create the new feature branch on your local repo:
```
git checkout -b branch-name
```

## 2. Add and commit changes
- Ideally, you should add and commit pretty often
  - ex. If you're writing multiple new methods, you should add and commit as you finish each one
- For the purposes of this project, its fine to just commit everything together
```
git add .
git commit -m "overview of changes"
```

## 3. Push your branch to GitHub
- This is sending the new branch from local to remote (GitHub)
- You can do this whenever, I like to push at the end of every session
```
git push origin branch-name
```

## 4. Open a pull request (PR) on GitHub
- Go to this repository on GitHub and create a PR
- Always resolve conflicts before merging!!
  - Ask someone to review the PR if you're unsure
- No Conflicts: Merge commit (don't mess with Squash or Rebase, they're more complicated)

## 5. After merging, update the repos
- update your *local* 'main' branch (with the changes you just merged to the *remote* 'main' branch)
```
git checkout main
git pull origin main
```

## 6. Clean up (Optional)
- When the feature is 100% complete, you can delete the branch.
  - Some of our big files won't really be complete until the project is done

delete on local repository:
```
git branch -d branch-name
```
delete on remote repository:
```
git push origin --delete branch-name
```

## Note: If `main` updates while you're working
```
git checkout main
git pull origin main
git checkout feature-branch-name
git merge main
```
