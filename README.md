# JobSearch

- This is a project for my portfolio.

## Requirements

- `java 19` (tested with 1.19)
- `maven` (tested with 3.8.1)
- `docker` (tested with 20.10.22)
- `docker-compose` (tested with 1.29.2)

## Execute

Development Environment

By default, the server will run in development mode (dev profile), if you want to specify the prod mode,
you can change the application.properties or add the `-Dspring.profiles.active=dev` flag when executing.

Check if the init_aws.sh script was correctly executed.
```bash
curl -s localhost:4566/_localstack/init | jq .
```
Check the available services
```bash
curl -s localhost:4566/health | jq .
```

Check the messages of ses
```bash
http://localhost:4566/_localstack/ses
``

**Server**
- `docker-compose run`
- `mvn spring-boot:run`

**Client**
- `npm run dev`

Production Environment

You have to pass the flag for production profile.
For now, store the AWS credentials in a `~/.aws/credentials`

**Server**
- `java -jar -Dspring.profiles.active=prod XXX.jar`

**Client**

## Troublesome
- The first time you try to execute docker-compose, the `/scripts/init.sql` will be applied.
This script will only run if the data directory of postgres is empty (in our case, our data directory is mounted
in the volume `pg_jobsearch_vol`). If the process of setup up the project doesn't go well and we want
to execute the script again, we have can delete the volumes with `docker-compose down --volumnes`.


Access directly into the localstack container
```bash
docker exec -t <container_id> /bin/bash
```
## TODO
- [ ] Should I use Docker Swarm instead of docker-compose?
