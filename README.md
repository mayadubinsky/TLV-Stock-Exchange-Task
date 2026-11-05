# TLV Stock Exchange Task

## Jenkins Pipeline

Triggered by GitHub pushes, the pipeline prepares SCM metadata using the `prepareEnv` Shared Library function, builds a source artifact, packages it as `.tar.gz`, and pushes it to the repository’s `artifacts/` directory.

**Setup:** Configure a Jenkins Shared Library named `prepareEnv` pointing to this repository, GitHub credentials with ID `github-creds`, and a GitHub webhook. The pipeline uses the GitHub and SCM Skip plugins and requires a Linux agent with Git, rsync, and tar.

## Ansible & Docker Compose

Deploys a “Hello World” web application with a hit counter stored in a separate Redis container. Redis data is retained in a named Docker volume.

See [deployment instructions](ansible/README.md).

## Bash — extract

Extracts gzip, bzip2, ZIP, and compress archives using content detection via `file`, regardless of filename or extension. Supports multiple inputs, recursive traversal, verbose output, and help.

```bash
bash Bash/extract -h
bash Bash/extract -rv path/to/directory
```

See [usage and examples](Bash/README.md).
