# swedish-event-planners
Event planning tool for the company Swedish Event Planners (project for Modern Methods of Software Engineering)

This tool is developed based on given business requirements. The code style and the implementation is not the most 
important thing. More emphasise is put on software engineering and project management practices. Especially this 
assignment drew our attention to some extreme programming approaches. We realised many smaller and bigger things 
which can be improved. However, we did not focus on all improvement opportunities as the time was limited.

## Content
These are the different topics which are covered in this README.
* [Login](#login)
* [Workflow](#worflow)
* [Releases](#releases)
* [Refactoring](#refactoring)
* [Planning](#planning)
* [Metaphor](#metaphor)
* [Acceptance Tests](#acceptance-tests)
* [Stand-up Meeting Reports](#stand-up-meeting-reports)
* [Project Structure](#project-structure)
* [Run Application](#run-application)
* [Deploy With Docker](#deploy-with-docker)

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

## Worflow

Add description on how a request gets generated and flow through the application

## Releases

Write about release policy with continuous deployment and milestones

## Refactoring

Sometimes, certain improvement possibilities of the code can be achieved by refactoring it. The changes can often be 
quite severe and the time needed for performing the required refactoring steps can be huge. In some situations,
developers realise immediately that the improvement through refactoring is worth it. However, it is also possible
that the amount of needed time for the refactoring is bigger than the gained improvement or the time needed would 
result in missing the set deadlines. As the refactoring is dependent on each developer and the project scope, the code
quality is very subjective and other people might have achieved the same differently. We also see improvement potential
which means that a software project can barely inf an end.

## Planning

Write about sprint, release and time planning. Also consider to write about the assigned risks and values.

## Metaphor

To convince other, non-technical people of the own programme or project, a metaphor is needed to outline the project 
and creator a greater interest of the other person. Furthermore, creating a metaphor helps to structure the own project 
clearer.

Our program can be thought as a cook preparing a meal for a group of customers. A brief explanation is given in the 
following paragraphs, as well a a table mapping metaphor elements with the actual system.

A cook needs to prepare a dish for a group of customers. She selects first a recipe from a book and proposes to her 
clients. Then, she asks for food preferences and allergies. After making the appropriate changes to the recipe, the 
cook is ready to start cooking.

First, she needs to check whether she has the required ingredients. If not, buy them. With all the ingredients ready, 
she can proceed to the actual preparation of the dish. Once the dish is cooked, she needs to check whether the customers 
are all seated. If so, she can proceed to serve the meal.

| Metaphor                           | System                                                                                                       |
| ---------------------------------- | ------------------------------------------------------------------------------------------------------------ |
| The cook                           | Swedish Event Planners — the company in charge of preparing the event.                                       |
| Recipe                             | First request by client.                                                                                     |
| Group of customers                 | SCSO — in charge of accepting the request in the first place and after some other approvals have taken place.|
| Preferences                        | Check request with Financial Manager.                                                                        |
| Allergies                          | Check request with Administration Manager.                                                                   |
| Check and possibly buy ingredients | Check whether we have sufficient staff and issue a request to HR team if not.                                |
| Actual cooking                     | Forward tasks to sub-teams.                                                                                  |
| Check if customers are seated      | Check whether we need extra budget and issue a request to FM if not.                                         |
| Serve food                         | Actual event.                                                                                                |

## Acceptance Tests

### Review Request by FinancialManager

Expected Actions:
1. After login, FinancialManager selects a New Request from list.
2. FinancialManager reviews the New Request.
3. FinancialManager clicks on `Reject request' button.

Expected Results:
1. FinancialManager sees New Request list.
2. The details for the selected New Request appear on the right half of the screen.
3. After clicking the button, FinancialManager can no longer see the New Request.

Test Result:
Successful

See the [story](https://github.com/felix-seifert/swedish-event-planners/issues/11).

### Proposal forwarded to Sub-teams by ProductionManager
Proposal forwarded to Sub-teams by Production Manager

Expected Actions:
1. After login, ProductionManager selects a Proposal from list.
2. ProductionManager reviews the Proposal.
3. ProductionManager clicks on `Forward to sub-teams' button.

Expected Results:
1. ProductionManager sees Proposal list.
2. The details for the selected Proposal appear on the right half of the screen.
3. After clicking the button, ProductionManager can still see forwarded Proposals in his/her grid 
(as long as hi/her subteam works on it), but its status has changed.

Test Result:
Successful

See the [story](https://github.com/felix-seifert/swedish-event-planners/issues/6).

## Stand-up Meeting Reports

## Project Structure

- `MainView.java` in `src/main/java` contains the navigation setup. It uses [App Layout](https://vaadin.com/components/vaadin-app-layout).
- `views` package in `src/main/java/ui` contains the server-side Java views of the application.
- `views` folder in `frontend/src/` contains the client-side JavaScript views of the application.
- `backend` folder in `src/main/java` contains the backend in Java of the application. The backend is divided into models, repositories and services.
- `security` folder in `src/main/java` contains the configured security function of Spring Security. It does not contain a proper user management and the roles are hardcoded.

## Run Application
There are two ways to run the application:  
 - To run from the command line, use `mvn` and open [http://localhost:8080](http://localhost:8080) in your browser.
 - Another way is to to run the `Application` class directly from your IDE.

## Deploy With Docker

To build the Dockerized version of the project, run

```
docker build . -t sep:latest
```

Once the Docker image is correctly built, you can test it locally using

```
docker run -p 8080:8080 sep:latest
```

...Consider to add Docker image to Docker Hub...
