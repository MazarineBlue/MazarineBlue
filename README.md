# MazarineBlue
[![Build Status][travis-image]][travis]
[![Release][release-image]][release]
[![License][license-image]][AGPL3 license]
[![Tech Debt][sonarqube-tech-debt-image]][sonarqube]
[![Coverage][sonarqube-coverage-image]][sonarqube]

<img src="https://github.com/MazarineBlue/MazarineBlue/wiki/images/MazarineBlue-logo.png" align="right" width="50%"/>

MazarineBlue is a automation framework, containing an interpreter that
reads instructions and data from a source and is extendable though
custom build modules. Several modules are included that allow, among
other things, data extraction from excel and other sources, web browser
interaction and data driven testing.

To find out more please check the [MazarineBlue website][website] and
the [MazarineBlue wiki][wiki].

**Table of Contens**  
[Example](#example)  
[Installation](#installation)  
[Documentation](#documentation)  
[Bugs and feedback](#bugs-and-feedback)  
[Contributing](#contributing)  
[Development](#development)  
[Commit messages](#commit-messages)  
[Tips and Tricks](#tips-and-tricks)  
[Authors & contributors](#authors--contributors)  
[Copyright & license](#copyright--license)

## Example
Below you can find a very simple instruction sheet and that data sheet
that goes with it. The code enters the username and password into a
webpage and the press the submit button. Please read the ['write your
first script'][write your first script] article for a more detailed
hello-world script.

**A very simple instruction sheet:**

|               |                              |        |        |
|---------------|------------------------------|--------|--------|
| Import        | org.mazarineblue.datasources |        |        |
| Import        | org.mazarineblue.webdriver   |        |        |
| Data Source   | source                       | array  | config |
| Select Source | source                       |        |        |
| Load URL      | www.example.com              |        |        |
| Type          | username-input               | smith  |        |
| Type          | password-input               | secret |        |
| Click         | submit-button                |        |        |

**The data sheet that goes with it:**

| Name           | Type  | Key
|----------------|-------|----------------------
| username-input | id    | username
| password-input | css   | #password
| submit-button  | xpath | //input[@id='submit']

## Installation
Assuming java is installed installation is a simple matter download the
latest version and unzipping it. The application can be run by double
clicking on the include bat file. Please read the [installation] article
for a more defailed installation instruction.

## Documentation
The [MazarineBlue wiki][wiki] is the home of the documentation. Here
you can find the following sections:
- Getting started
- User guide
- Programming interface
- Developers guide

## Bugs and feedback
Please submit a ticket to our [issue tracker] when you have found a bug
or want to submit a patch. Please review [guidelines for contributing]
[guidelines for contributing] before you do though.

## Contributing
This project follows the [Git flow][git flow] model by Vincent
Driessen. The git commands are included in this document, for a more
detailed description please read ['A successful git branching model']
[git flow].

Please follow the following rules, if you like to contribute:
- Create a fork if you do not have a commit bit
- Only commit directly to the master branch: typos, improvement to the
  readme and documentation
- For new features:
  - branch off from: develop
  - send a pull request to get feedback
  - merge back into develop
  - branch naming convention: anything except, master, develop, release-\* or hotfix-\*
- For hotfixes:
  - branch off from: master 
  - send a pull request to get feedback
  - merge back into: develop and master
  - branch naming convention: hotfix-\*
- For a new release:
  - branch off from: develop
  - send a pull request to get feedback
  - merge back into: develop and master
  - branch naming convention: release-\* 
- When merging use the --no-ff parameter 
- All contributions must follow the [style guide]  [style guide]

## Development
MazarineBlue is developed using NetBeans, Maven and Git.  You can grab a
copy here:
- https://netbeans.org/downloads/
- http://git-scm.com/download/win

You can get a copy of the code 
- git clone https://github.com/MazarineBlue/MazarineBlue.git
- git clone git@github.com:MazarineBlue/MazarineBlue.git
 
Please import
[etc/NetBeans-formatting-settings.zip][NetBeans formatting settings] to
NetBeans.

## Commit messages

A commit message should be helpfull for reviewing. It's when a commit
message is helpfull for reviewing that git blame, revert, rebase, log
shortlog and other subcommand come to life. It's when a commit message
helpt you to understand *what* was changed and *why* that reviewing
becomes possible and efficient. A commit message is helpfull for
reviewing when it complies with the following simple rules:

1. Separate subject from body with a blank line;
2. Limit the subject line to around 50 characters;
3. Capatalize the subject line;
4. Do not end subject with a period;
5. Use the imperative mood in the subject line;
6. Wrap the body at 72 characters;
7. Use the body to explain *what* and *why*;

The subject should give the reader a overview on what the commit does.
Therefor it should be short and use imperative mood. Imperative mood
means "spoken or written as if giving a command or instruction". A
properly format git commit subject should be able to complete the
following sentence: if applied, this commit will..."

The body must explain on the *what* was changed and *why*. A helpfull
message provides context to the changes that are in the commit. Answer
the following questions:

- Why was this change necessary?
- How does this change address the issue?
- What side effects does this change have?

This project comes with a commit template. Please activate it by running:  
```git config commit.template .gitmessage.txt```

The format looks like this:

> Summarize changes in around 50 characters or less
>
> More detailed explanatory text, if necessary. Wrap it to about 72
> characters or so. In some contexts, the first line is treated as the
> subject of the commit and the rest of the text as the body. The
> blank line separating the summary from the body is critical (unless
> you omit the body entirely); various tools like `log`, `shortlog`
> and `rebase` can get confused if you run the two together.
> 
> Explain the problem that this commit is solving. Focus on why you
> are making this change as opposed to how (the code explains that).
> Are there side effects or other unintuitive consequenses of this
> change? Here's the place to explain them.
> 
> Further paragraphs come after blank lines.
> 
>  - Bullet points are okay, too
> 
>  - Typically a hyphen or asterisk is used for the bullet, preceded
>    by a single space, with blank lines in between, but conventions
>    vary here
> 
> Include a references to the issue tracker like this:
> 
> Resolves: #123  
> See also: #456, #789

## Tips and Tricks
- You can set your default editor with
  ```git config -global core.editor vim```

- And you can add the following to your ```~/.vimrc``` to add spell
  checking and automatic wrapping at the recommended 72 columns to you
commit message:

  ```
  autocmd Filetype gitcommit setlocal spell textwidth=72
  ```

## Authors & contributors
| Person                     | Role               |
|----------------------------|--------------------|
| Alex de Kruijff (akruijff) | Lead Developer     |
| Daan Verbiest              | Graphical designer |

## Copyright & license
MazarineBlue is released under the
[GNU Affero General Public License, version 3][AGPL3 license]. The
modules MazarineBlue-Parser and MazarineBlue-EventNotifier are release under
the [BSD 2-Clause License][BSD 2-clause license].

[logo-image]: https://github.com/MazarineBlue/MazarineBlue/wiki/images/MazarineBlue.png
[website]: http://www.MazarineBlue.org/

[issue tracker]: http://github.com/MazarineBlue/MazarineBlue/issues
[guidelines for contributing]: CONTRIBUTING.md
[style guide]: STYLE.md

[wiki]: https://github.com/MazarineBlue/MazarineBlue/wiki
[write your first script]: https://github.com/MazarineBlue/MazarineBlue/wiki/Writing-your-first-script
[installation]: https://github.com/MazarineBlue/MazarineBlue/wiki/Installation

[NetBeans formatting settings]: etc/NetBeans-formatting-settings.zip

[git flow]: http://nvie.com/posts/a-successful-git-branching-model/

[travis-image]: https://travis-ci.org/MazarineBlue/MazarineBlue.svg?branch=develop
[travis]: https://travis-ci.org/MazarineBlue/MazarineBlue

[release-image]: http://img.shields.io/badge/release-0.2_SNAPSHOT-blue.svg
[release]: http://github.com/MazarineBlue/MazarineBlue/releases

[license-image]: http://img.shields.io/:license-agpl3-blue.svg
[AGPL3 license]: www.gnu.org/licenses/agpl-3.0.html
[BSD 2-clause license]: http://opensource.org/licenses/BSD-2-Clause

[sonarqube]: https://sonarqube.com/dashboard?id=org.mazarineblue%3AMazarineBlue
[sonarqube-tech-debt-image]: https://img.shields.io/sonar/http/nemo.sonarqube.org/org.mazarineblue:MazarineBlue/tech_debt.svg
[sonarqube-coverage-image]: https://img.shields.io/sonar/http/nemo.sonarqube.org/org.mazarineblue:MazarineBlue/coverage.svg
