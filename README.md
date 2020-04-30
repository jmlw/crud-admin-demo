# Crud Admin Demo

Demonstrates a configurable UI based on API entities with Spring Boot and React.

## Running
Start server application with `./gradlew bootRun`. Server will be hosted on port 8080 with an H2 in-memory database. Shutting down the server will result in all persistent data to be lost. Uncomment the `datasource.url` configuration in `application.yml` to enable disk based persistence. 

Start client application in another shell with `cd client && yarn start`. Client application will be hosted on port 3000 and is run with a live development server.

## Example API Usage
See `api.http` for documentation on how to interact with the API.

See `api-demo.http` for HTTP requests that demonstrate creating entities, modifying, and relating them in the database.

## Creating a new Entity Page
1) Create a new entity in the domain package
2) Create a DTO object in the `service.dto` package and annotate appropriately for the Admin Controller to introspect. Note, this isn't strictly necessary as the annotations could live on the entity, however using a DTO and mapper ensures that there are no lazy initialisation exceptions since `open-in-view` is disabled to prevent n+1 selects from occurring during JSON rendering. 
3) Create a new controller extending `AbstractAdminController`. Override `entityClass` and`dtoClass` methods to supply superclass with class information. Optionally override `buildCriteriaQuery` and `toAutocompleteDto` to enable query searches for relation fields in the UI (required when TO_ONE or TO_MANY relations exist for UI to function properly).
4) Add configuration object to the array in AdminConfig.tsx to enable a new list view drawer button, list view columns, and the base API path for the entity section to fetch the configuration & enable CRUD functions for the form. Note: this could probably be introspected from the DTOs or entities & controllers within the API & a single configuration endpoint would serve all the required data.

---

**NOTE: This is not intended for production use. It should serve only as an example of how automated CRUD for JPA entities could be built.**
