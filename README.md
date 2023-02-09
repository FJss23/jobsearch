# JobSearch
- This is a project for my portfolio.

## Requirements
- `java 19` (tested with 1.19)
- `maven` (tested with 3.8.1)
- `docker` (tested with 20.10.22)
- `docker-compose` (tested with 1.29.2)

## Execute
### Server
- `docker-compose run`
- `mvn spring-boot:run`
### Server
- `npm run dev`

## Troublesome
- The first time you try to execute docker-compose, the `/scripts/init.sql` will be applied.
This script will only run if the data directory of postgres is empty (in our case, our data directory is mounted
in the volume `pg_jobsearch_vol`). If the process of setup up the project doesn't go well and we want
to execute the script again, we have can delete the volumes with `docker-compose down --volumnes`.


## TODO
- [ ] Check why the maven-compiler-plugin plugin is not working.
