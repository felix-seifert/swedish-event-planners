# swedish-event-planners
Event planning tool for the company Swedish Event Planners (project for Modern Methods of Software Engineering)

## Login

Currently, one hardcoded user exists. Use the credentials `user` and `password` to log in. Users cannot register.

## Project structure

- `MainView.java` in `src/main/java` contains the navigation setup. It uses [App Layout](https://vaadin.com/components/vaadin-app-layout).
- `views` package in `src/main/java` contains the server-side Java views of your application.
- `views` folder in `frontend/src/` contains the client-side JavaScript views of your application.

## Running the Application
There are two ways to run the application:  
 - To run from the command line, use `mvn` and open [http://localhost:8080](http://localhost:8080) in your browser.
 - Another way is to to run the `Application` class directly from your IDE.

## Deploying using Docker

To build the Dockerized version of the project, run

```
docker build . -t sep:latest
```

Once the Docker image is correctly built, you can test it locally using

```
docker run -p 8080:8080 sep:latest
```