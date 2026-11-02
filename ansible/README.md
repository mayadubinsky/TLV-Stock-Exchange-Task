# Ansible and Docker — Hello World Hit Counter

A Docker Compose application deployed using Ansible. The application contains two services:

- **web** — serves a basic “Hello World” HTML page and increments a hit counter on each request.
- **db** — a separate Redis container that stores the counter.

Redis data is stored in a named Docker volume, `redis-data`.

## Project Structure

```text
ansible/
├── playbook.yml
├── requirements.yml
└── app/
    ├── docker-compose.yml
    └── web/
        └── Application source files and Dockerfile
```

## Prerequisites

- An Ubuntu server supported by Docker’s official APT repository.
- Ansible installed on the server.
- A user with sudo privileges.
- Internet access to download packages, Ansible collections, and container images.
- Port `8081` available on the server.

The playbook runs locally on the target server using `hosts: localhost` and `connection: local`.

The installation assumes a fresh server or an existing installation using Docker’s official packages. Conflicting packages from other installation sources may require manual removal.

## Deployment

Clone the repository and enter the Ansible directory:

```bash
git clone https://github.com/mayadubinsky/TLV-Stock-Exchange-Task.git
cd TLV-Stock-Exchange-Task/ansible
```

Install the required Ansible collection:

```bash
ansible-galaxy collection install -r requirements.yml
```

Run the playbook:

```bash
ansible-playbook playbook.yml
```

If sudo requires a password, use:

```bash
ansible-playbook playbook.yml --ask-become-pass
```

The playbook:

1. Installs the prerequisites for Docker’s APT repository.
2. Adds Docker’s repository signing key and repository configuration.
3. Installs Docker Engine, Buildx, and the Docker Compose plugin.
4. Ensures Docker is running and enabled at boot.
5. Copies the application to `/opt/hello-app`.
6. Builds and starts the application using Docker Compose.

## Access the Application

From the server:

```bash
curl http://localhost:8081
```

From a browser:

```text
http://<SERVER_PUBLIC_IP>:8081
```

Refresh the page to increment the hit counter.

## Verify Data Persistence

1. Open the application several times and note the counter value.
2. Stop and remove the containers:

   ```bash
   sudo docker compose -f /opt/hello-app/docker-compose.yml down
   ```

3. Run the playbook again from the `ansible` directory:

   ```bash
   ansible-playbook playbook.yml
   ```

4. Open the application again. The counter should continue from its previous value, accounting for new requests.

Do not add `-v` to `docker compose down` during this test: it removes the named volume and its stored data.

## Update the Application

Update the source files under `ansible/app` and run the playbook again. Ansible copies the files to the deployment directory, and Docker Compose rebuilds the web image and applies the changes.

## Stop the Application

```bash
sudo docker compose -f /opt/hello-app/docker-compose.yml down
```

This removes the application containers and network while retaining the Redis volume.
