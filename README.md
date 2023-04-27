# JobSearch

- This is work in progress project

## Requirements

- `java 1.19`
- `maven 3.8.1`
- `docker 20.10.22`
- `docker-compose 1.29.2`
- `node 18.16.0`
- `npm 9.5.1`

## Installation

### Development Environment

By default, the server will run in development mode (dev profile), if you want to specify the prod mode,
you can change the application.properties or add the `-Dspring.profiles.active=dev` flag when executing.

- `docker-compose run`
- `mvn spring-boot:run`
- `npm run dev`

#### Other useful commands 

Check if the init_aws.sh script was correctly executed.

```bash
curl -s localhost:4566/_localstack/init | jq .
```

Check the available services

```bash
curl -s localhost:4566/health | jq .
```

Check the status of docker-compose

```bash
docker-compose ps
```

Enter the localstack docker container

```bash
docker-compose exec localstack bash
```

```bash
http://localhost:4566/_localstack/ses
http://localhost:4566/_aws/ses
```

### Production Environment

- wip...

## Troublesome

- The first time you try to execute docker-compose, the `/scripts/init.sql` will be applied.
This script will only run if the data directory of postgres is empty (in our case, our data directory is mounted
in the volume `pg_jobsearch_vol`). If the process of setup up the project doesn't go well and we want
to execute the script again, we have can delete the volumes with `docker-compose down --volumnes`.


## Debug

```bash
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8000"
```

Then inside intellij create a profile just for debug, with the default information, just 
add the `8000` port.

## Formatter
- Check if there is any error `mvn spotless:check`
- Format the code `mvn spotless:apply`

If you want to compile the project, it need to be checked without errors
Building the project will also verify the formatter.

## Test
- Run the test with `mvn clean test`.
- Run an specific test with `mvn clean test -Dtest=<name>Test


## Some interesting commands
- docker run -it --rm --network server_jbls_net postgres:15.1-alpine psql -h postgres_jbs -d jobsearch_db -U jobsearch_admin
