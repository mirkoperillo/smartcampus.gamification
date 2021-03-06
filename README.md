Gamification Engine is a platform that permits to define and execute score based games.

Platform is developed in Java using Apache Maven as build tool.

## Components
* [game-engine.core](game-engine.core): it contains Drools engine implementation and models
* [game-engine.web](game-engine.web): REST API, admin web interface and security layer
* [game-engine.test](game-engine.test): junit test environment to test a new game definition
* [game-engine.games](game-engine.games): collection of production games.
* [game-engine.tools](game-engine.tools): a collection of tools to manage gamification operations 
* [gamification-java-client](gamification-java-client): a java client library that wraps the gamification-engine APIs
* [run-configs](run-configs): folder used by docker


## Quickstart run

You can use docker to have a quick running system.

### Requirements

* docker > 1.18
* docker-compose > 1.25.0
   

You can find configurations into the `gamification.env` file.

### Setup simple security

Instruction to use BASIC AUTH security

1. Copy `run-configs/users.yml` where you want (ex: `/your-path/users.yml`)
2. Open `/your-path/users.yml` and modify `username` and `password`
3. In `gamification.env` modify `SPRING_PROFILE` to `sec`
4. In `gamification.env` modify `USERS_FILE` to `/app/config/users.yml`
5. In `docker-compose.yml` modify `volumes` as:
```
 volumes:
   - ./logs:/app/game-engine.web/logs
   - /your-path:/app/config
```

### Run

1. run `sudo COMPOSE_DOCKER_CLI_BUILD=1 DOCKER_BUILDKIT=1 docker-compose up` from the root folder.

**NOTE**
* gamification-engine will be up and running on host port **8010** 
* administration console is available at url **http://localhost:8010/gamification/consoleweb/**
* APIs can be invoked using the url prefix **http://localhost:8010/gamification**
* APIs are protected with the user you put in file `users.yml`
* mongodb container exposes to host port **50000** to inspect the data inside
* gamification-engine exposes a JMX connection on host port **7777**


## Quickstart build

To build a complete game engine instance you need following tools: 
* bower

Build steps:
1. move to folder `game-engine.web/src/main/resources/consoleweb-assets`
2. run command `bower install`
3. follow instructions from step 2 of [the setup chapter of wiki](https://github.com/smartcommunitylab/smartcampus.gamification/wiki/Setup#build) 

## Upgrades

### v2.1.0 -> v2.2.0

**This is old stuff. Version prior of v2.2.0 are very old and we don't think exist anymore a database based on these versions.
This section is maintained only for historical reasons.**

In version 2.2.0 we have changed the persistence model representation of playerState and pointConcept, so if you migrate from v2.1.0 or prior version to v.2.2.0 you have to run following script
to you mongo database.

```
mongo DB_NAME game-engine.core/src/main/scripts/migration-db/2.1.0-to-2.2.0/step1.js
mongo DB_NAME game-engine.core/src/main/scripts/migration-db/2.1.0-to-2.2.0/step2.js
mongo DB_NAME game-engine.core/src/main/scripts/migration-db/2.1.0-to-2.2.0/step3.js
```

## Documentation
Read the project [wiki](https://github.com/smartcommunitylab/smartcampus.gamification/wiki) for all details about the project

## Licence
Project is licensed under the Apache License Version 2.0
