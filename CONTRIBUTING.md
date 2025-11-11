<p align="center">
  <img src="shared/resources/src/commonMain/composeResources/drawable/app_icon.png" 
       alt="Gondi Banner" 
       width="180" />
</p>

<h1 align="center">Contributing Guidelines</h1

Thank you for your interest in contributing! This document outlines how to submit pull requests, write commits, interact with the CodeRabbit bot, and follow testing and review conventions.

---

## 🧩 General Principles

* Respect existing code style, architecture, and conventions.
* Write clean, self-explanatory code with meaningful naming.
* Small, focused commits and PRs are preferred over large, sweeping ones.
* Be descriptive and professional in all discussions and commit messages.

---

## ⚙️ Pull Requests (PRs)

### ✅ Do

* Create a dedicated branch from `main` or the appropriate feature branch.
* Use clear naming: `feat/...`, `fix/...`, `chore/...`, `refactor/...`, `test/...`.
* Include a concise title and a detailed description explaining the change and its motivation.
* Reference related issues using keywords like `Closes #123` or `Fixes #456`.
* Include screenshots or logs when applicable.

### ❌ Don’t

* Submit unrelated changes in a single PR.
* Commit generated files or local environment configurations.
* Merge without an approved review (except for hotfixes pre-approved by maintainers).

Please refer to the [PR template](.github/PULL_REQUEST_TEMPLATE.md) for how to draft one.

## 💬 Code Reviews (via CodeRabbit)

### What to Expect

* CodeRabbit performs automated code analysis and adds inline comments.
* Maintain a professional tone when clarifying or resolving review comments.

### Contributor Guidelines

* If CodeRabbit flags a valid issue, fix it before re-requesting review.
* If a comment is unclear or incorrect, politely ask for clarification.
* Human reviewers have the final word; CodeRabbit is advisory.

---

## 🧪 Tests

### When Adding Features

* Add new unit or integration tests to cover new logic.
* Use existing test patterns as a reference for structure and naming.

### When Modifying Code

* Amend affected tests to maintain coverage.
* Document rationale if a test is removed or changed.
* Be prepared to clarify logic changes when reviewers request it.

### When Unsure

* Request clarification via a PR comment before modifying complex tests.

---

## 🧠 Commit Conventions

Follow the [Conventional Commits](https://www.conventionalcommits.org) standard:

```
<type>(<scope>): <description>
```

**Types:** `feat`, `fix`, `chore`, `refactor`, `test`, `docs`, `ci`, `style`.

**Examples:**

* `feat(auth): add password reset via email`
* `fix(api): correct null pointer on response mapping`
* `test(core): add coverage for game state validation`

**Do:**

* Write imperative, lowercase messages.
* Keep messages concise (under 72 chars).

**Don’t:**

* Use vague messages like `update code` or `fix stuff`.
* Commit temporary debugging changes.

---

## ⚖️ Do’s and Don’ts

### ✅ Do

* Follow project formatting and linting rules before pushing.
* Include documentation updates for new features.
* Request review early for large or architectural changes.
* Use `draft` PRs for work in progress.

### ❌ Don’t

* Force-push to shared branches without notifying others.
* Bypass CI failures or suppress test warnings without cause.
* Commit sensitive data or credentials.

---

## 📜 Summary

Contributions are welcome and appreciated. Keep PRs focused, tested, and well-documented. Respect feedback, and when in doubt, ask. Collaboration is the priority — code quality and clarity come first.
