# swedish-event-planners
Event planning tool for the company Swedish Event Planners (project for Modern Methods of Software Engineering)

This tool is developed based on given business requirements. The code style and the implementation is not the most 
important thing. More emphasise is put on software engineering and project management practices. Especially this 
assignment drew our attention to some extreme programming approaches. We realised many smaller and bigger things 
which can be improved. However, we did not focus on all improvement opportunities as the time was limited.

## Content
These are the different topics which are covered in this README.
* [Login](#login)
* [Event Flow](#event-flow)
* [Releases](#releases)
* [Planning](#planning)
* [Refactoring](#refactoring)
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

## Event Flow

When a client approaches Swedish Event Planners, either the `CustomerServiceOfficer` or the `SeniorCustomerServiceOfficer` takes 
the details for a requested event and creates a `NewRequest`. This request is first reviewed by the `SeniorCustomerServiceOfficer` 
and then by the `FinancialManager` and the `AdministrationManager` in this order. Every actor can either reject the proposal or forward
the decision to the next actor. Once all the approvals for the `NewRequest` have been issued, the `SeniorCustomerServiceOfficer` 
notifies the client and sets up a meeting (the meeting happens outside the scope of our program).

After the meeting, either the `SeniorCustomerServiceOfficer` or the `Client` itself creates a `Proposal` for the event, including the
details to be taken into account by the production and services teams. When a `Proposal` is created, two simultaneous workflows start, 
both of them happening in a similar fashion: the `ProductionManager` reviews the `Proposal` and decides to forward it to its sub-teams 
or to request extra staff (in the example implementation, only one sub-team exists). If extra staff is requested, the workflow stops 
until the request is approved by the Human Resources department. The `ProductionSubteam` reviews the request and returns it to the 
`ProductionManager`. If needed, the sub-team can add an extra budget notification. The `ProductionManager` can then either mark the 
`Proposal` as ready or request extra budget from the `FinancialManager` (only if the sub-team has requested so). Once the 
`FinancialManager` approves the request, the `ProductionManager` marks the `Proposal` as ready.
The exact same workflow happens with the `ServicesManager` and the `ServicesSubteam`. Once both workflows are marked as ready, the 
event can take place.

## Releases

The implemented system should be released each time a set of stories is readily implemented, tested and merged to the 
production branch (continuous delivery). This can be for example implemented with the GitHub Actions. Like this, stakeholders 
are able to immediately test the system and provide feedback to the developers who develop their system. 

This sets of stories where visualised in form of milestones to stories should be implemented. We did not follow the 
estimated risk or value of a story but the actual event flow at the application domain. Even though the general event 
flow was known at the beginning, we assigned the stories to the different milestones gradually because not all the 
implementation details were clear at the beginning. This also helps in accommodating changes which occur over time 
and might not be known at the beginning.

The milestones and therefore planned releases [can be seen in the GitHub repository](https://github.com/felix-seifert/swedish-event-planners/milestones).

## Planning

In the actual implementation, we followed roughly the Kanban approach where each of us works on implementing a story,
informs the other one about the result who then reviewed and tested the implementation. The story which we assigned 
were selected based on the previously described milestones.

The decision about the value of each story is based on the assumed importance of each story in the application domain. 
As no real customer was present, the risk assessment happened mainly based on programming experiences. They became 
slightly more exact over time.

We started to estimate the duration of the stories for the actual milestone. Even though our estimations became more 
exact over time, the estimations were generally to low. Especially the later described approach of pair programming 
seemed to be slightly more time-consuming. Anyway, the time estimates and the actual used implementation time are not 
visible anymore because somehow the used tool let the timings disappear if you end the subscription.

## Refactoring

Sometimes, certain improvement possibilities of the code can be achieved by refactoring it. The changes can often be 
quite severe and the time needed for performing the required refactoring steps can be huge. In some situations,
developers realise immediately that the improvement through refactoring is worth it. However, it is also possible
that the amount of needed time for the refactoring is bigger than the gained improvement or the time needed would 
result in missing the set deadlines. As the refactoring is dependent on each developer and the project scope, the code
quality is very subjective and other people might have achieved the same differently. We also see improvement potential
which means that a software project can barely inf an end. Some refactoring ideas can be seen on the [page of our 
issues](https://github.com/felix-seifert/swedish-event-planners/issues).

## Pair Programming

The approach of developing in pairs helps a lot too deal with huge difference concerning knowledge; as a developer 
you can benefit greatly by being thought the development skills and knowledge of the other developer. Also, the 
whole process is improved by combining different ways of thinking. As it might be more time-consuming because two 
developers have to work at the same time, so much less communication is needed after the implementation that it can be 
considered as better.

## Metaphor

To convince other non-technical people of the own programme or project, a metaphor is needed to outline the project 
and create a greater interest of the other person. Furthermore, creating a metaphor helps to make the structure of the 
own project clearer.

Our program can be thought as a cook preparing a meal for a group of customers. A brief explanation is given in the 
following paragraphs. In addition, a table maps metaphor elements to the actual system.

A cook needs to prepare a dish for a group of customers. Firstly she selects a recipe from a book and proposes it to her 
clients. Then, she asks for food preferences and allergies. After making the appropriate changes to the recipe, the 
cook is ready to start cooking.

At the beginning of her cooking session, she needs to check whether she has the required ingredients. If not, she 
has to buy them. With all the ingredients ready, she can then proceed to the actual preparation of the dish. Once 
the dish is cooked, she needs to check whether the customers are all seated. If so, she can proceed to serve the meal.

| Metaphor                           | System                                                                                                       |
| ---------------------------------- | ------------------------------------------------------------------------------------------------------------ |
| The cook                           | Swedish Event Planners — the company in charge of preparing the event                                        |
| Recipe                             | First request by client                                                                                      |
| Group of customers                 | SCSO — in charge of accepting the request in the first place and after some other approvals have taken place |
| Preferences                        | Check request with Financial Manager                                                                         |
| Allergies                          | Check request with Administration Manager                                                                    |
| Check and possibly buy ingredients | Check whether we have sufficient staff and issue a request to HR team if not                                 |
| Actual cooking                     | Forward tasks to sub-teams                                                                                   |
| Check if customers are seated      | Check whether we need extra budget and issue a request to FM if not                                          |
| Serve food                         | Actual event                                                                                                 |

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

See this [story](https://github.com/felix-seifert/swedish-event-planners/issues/11).

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

See this [story](https://github.com/felix-seifert/swedish-event-planners/issues/6).

## Stand-up Meeting Reports

| 2020-10-05                  | Stand-up Meeting Report                                                                                                         |
| --------------------------- | ------------------------------------------------------------------------------------------------------------------------------- |
| Participants                | Felix Seifert, Àlex Costa Sánchez                                                                                               |
| Summary of Activities       | Project kick-off, no previous activities.                                                                                       |
| Expected Actions for Today  | 1. Write a complete set of stories.<br>2. Select relevant stories for the first release.                                        |
| Problems                    | None.                                                                                                                           |

| 2020-10-12                  | Stand-up Meeting Report                                                                                                         |
| --------------------------- | ------------------------------------------------------------------------------------------------------------------------------- |
| Participants                | Felix Seifert, Àlex Costa Sánchez                                                                                               |
| Summary of Activities       | 1. Successfully completed the first milestone.                                                                                  |
| Expected Actions for Today  | 1. Write the metaphor<br>2. Start the set of stories for the second milestone.                                                  |
| Problems                    | None.                                                                                                                           |

| 2020-10-19                  | Stand-up Meeting Report                                                                                                         |
| --------------------------- | ------------------------------------------------------------------------------------------------------------------------------- |
| Participants                | Felix Seifert, Àlex Costa Sánchez                                                                                               |
| Summary of Activities       | 1. Successfully completed the second milestone.<br>2. Metaphor written down.<br>3. Third milestone started.                     |
| Expected Actions for Today  | 1. Start writing final documentation.<br>2. Continue working on the third milestone.<br>3. Special focus on finishing workflows.|
| Problems                    | Time constrains will be challenging.                                                                                            |

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

In addition, the code of the main branch is available as a docker image online and can be pulled and run using

```
docker run -p 8080:8080 seifertfelix/sep:latest
```
