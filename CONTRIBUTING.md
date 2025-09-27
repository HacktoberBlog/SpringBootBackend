# Contributing to This Project

Thank you for considering contributing! This guide will help you make successful contributions to the project.

---

## 🛠️ How to Contribute

Follow these steps to set up your environment and submit a pull request (PR):

### Getting an issue
When you find the issue that you want to work on, comment on it so that the maintainers can assign the issues.

### 1. 🍴 Fork the repository  
Click the **"Fork"** button in the top right of the repo page to create your own copy.

### 2. 📥 Clone your fork  
```bash
git clone https://github.com/<your-username>/SpringBootBackend.git
cd SpringBootBackend
```

### 3. 🌱 Create a new branch
```
git checkout -b fix/fix-typo
# or
git checkout -b feat/add-user-api
```

### 4. 💾 Make your changes

Make any necessary code, documentation, or test updates.

### 5. ⚙️ Environment Setup

Before running the project or tests, set the environment variables that match those used in `application.properties`:
- `spring.mail.username` → Your Gmail account  
- `spring.mail.password` → Your 16-char Gmail App Key  
- `redis.password` → Your Redis database password  

⚠️ Do not commit secrets to GitHub. Use environment variables or a `.env` file (gitignored).


### 6. 🔨 Build the project
```
mvn clean install
```

Make sure the build passes and tests run successfully.

### 7. ⬆️ Push your branch
```
git push origin <your-branch-name>
```

### 8. 📬 Open a Pull Request

Go to your fork on GitHub and click “Compare & Pull Request”.

- Fill in the PR description template (Summary, Changes, Why).  
- Link any related issues (use `Closes #issue` syntax).  
- Choose main (or the appropriate base branch) as the target.
- Mention if this contribution is part of Hacktoberfest or GSSoC.  



### 9. 🔁 Resolve conflicts (if any)

If your PR has merge conflicts:

- Add the main repo as an upstream remote (do this once):

```
git remote add upstream https://github.com/HacktoberBlog/SpringBootBackend.git
```

- Fetch the latest changes from upstream:
```
git fetch upstream
```

- Switch to your local master branch and sync with upstream:
```
git checkout master
git rebase upstream/master
```

- Switch back to your feature branch and rebase on updated master:
```
git checkout <your-branch-name>
git rebase master
```

### 📝 Request a Review

Once your PR is ready:

- Assign it to a maintainer or request a review using GitHub’s review feature.



