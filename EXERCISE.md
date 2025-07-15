# Development Exercise

This exercise should take between 4 to 8 hours, depending on the chosen "options".

## Create an application that allows you to:

- List insurance policies
- Create an insurance policy
- Read an insurance policy
- Edit an insurance policy

## Business Object: Insurance Policy

| Name                | Type                                            | Editable | Validation                         |
|---------------------|-------------------------------------------------|----------|------------------------------------|
| Policy Identifier   | Integer                                         | No       | Auto-generated, Required, Unique   |
| Policy Name         | String                                          | Yes      | Not null, Not empty                |
| Policy Status       | Enum: <ul><li>ACTIVE</li><li>INACTIVE</li></ul> | Yes      | Not null                           |
| Coverage Start Date | Date                                            | Yes      | Not null, Required                 |
| Coverage End Date   | Date                                            | Yes      | Not null, Required                 |
| Creation Date       | Date                                            | No       | Auto-generated, Required, Not null |
| Update Date         | Date                                            | No       | Auto-generated, Required, Not null |

## Language and Storage

The development language for this application is Java.  
Data storage is handled by a relational database.

## Optional Features

- This application uses Spring Data JPA.
- This application includes a Dockerfile for containerization.
- A React SPA consumes your API to list, create, read, and edit insurance policies.

## Sharing

Share your project files with an email address to be provided.
