# demo_up
## components
* java app
* mysql (start with cd database/ && docker compose up )
## external components used

* copy .jar to /libs/ directory
* right click on jar
* add as library

## notes
  You can delete docker volume data this way:

>> docker run --rm -v "${PWD}/mariadb:/var/lib/mysql" busybox rm -rf /var/lib/mysql/
## To install dependencies in IDEA:

* right click on project root
* maven
* Download Sources and Documentation