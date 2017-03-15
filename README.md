SonarQube Reporter
==================

Generate SonarQube reports from its database.

# How to use 

## Running

Download the latest jar release and run:
 
```bash
java -jar sonarqube-reporter-x.x.x.jar
```

## Available parameters

| Parameter     | Description                         |
|---------------|-------------------------------------|
|`--host`       | MySQL host (default: localhost)     |
|`--port`       | MySQL port (default: 3306)          |
|`--database`   | MySQL database (default: sonarqube) |
|`--user`       | MySQL user (default: root)          |
|`--password`   | MySQL password (default: empty)     |
|`--output-dir` | Output directory (default: .)       |


# How to develop

## To-do

* Add support for H2 database (SonarQube default);
* Add more output adapters and allow its selection;
* Improvements on class loading (maybe use a DIC?);
* Improvements on testing.
* Exception handling

## Running

1. Clone the repository;
2. Switch to the repository folder and run gradle:

```bash
./gradlew run
```

## Running the tests

```bash
./gradlew test
```
## Generating Jar

```bash
./gradlew build
```

You will find the jar file in the `build/libs` folder.
