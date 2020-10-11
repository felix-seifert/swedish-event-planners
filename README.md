# swedish-event-planners
Event planning tool for the company Swedish Event Planners (project for Modern Methods of Software Engineering)

## Login

The source code includes a hardcoded version of the different roles and how they are assigned to the also hardcoded 
users. One can log in with the username which is provided below and the password `password`. The implementation of the users can be seen in the 
[`SecurityConfiguration`](https://github.com/felix-seifert/swedish-event-planners/blob/main/src/main/java/com/felixseifert/swedisheventplanners/security/SecurityConfiguration.java).

The following roles exist:
* `AdministrationManager`
* `SeniorCustomerServiceOfficer`
* `CustomerServiceOfficer`
* `SeniorHRManager`
* `HRAssistant`
* `HREmployee`
* `MarketingOfficer`
* `MarketingAssistant`
* `MarketingEmployee`
* `FinancialManager`
* `Accountant`
* `ProductionManager`
* `ProductionSubTeam`
* `ServicesManager`
* `ServicesSubTeam`
* `VicePresident`
* `Secretary`
* `ClientViewer`
* `EmployeeViewer`
* `StaffViewer`
* `Client`

The following list shows all the users with their user name and which roles they have:
* `mike`: `AdministrationManager`
* `janet`: `SeniorCustomerServiceOfficer`, `ClientViewer`
* `cso`: `CustomerServiceOfficer`
* `simon`: `SeniorHRManager`, `HREmployee`, `EmployeeViewer`
* `maria`: `HRAssistant`, `HREmployee`, `EmployeeViewer`
* `david`: `MarketingOfficer`, `MarketingEmployee`, `ClientViewer`
* `emma`: `MarketingAssistant`, `MarketingEmployee`, `ClientViewer`
* `alice`: `FinancialManager`, `Accountant`, `ClientViewer`, `EmployeeViewer`
* `jack`: `ProductionManager`, `StaffViewer`
* `production`: `ProductionSubTeam`
* `natalie`: `ServicesManager`, `StaffViewer`
* `services`: `ServicesSubTeam`
* `charlie`: `VicePresident`
* `secretary`: `Secretary`, `EmployeeViewer`
* `client`: `Client`

For testing purposes, the page `Master Detail` is only visible by the user `mike`. Relevant changes for this can be 
done with an annotation over the class name of the view and the menu in 
[`MainView.java`](https://github.com/felix-seifert/swedish-event-planners/blob/main/src/main/java/com/felixseifert/swedisheventplanners/views/main/MainView.java).

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