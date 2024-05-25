## Implement Azure Security
* Implement user authentication and authorization
  * Authenticate and authorize users by using the Microsoft Identity platform
  * Authenticate and authorize users and apps by using Microsoft Entra ID
  * Create and implement shared access signatures
  * Implement solutions that interact with Microsoft Graph


### What is Microsoft Entra ID
So far we have been working with Azure resources with our Azure Admin Account.
But in an organization, you want to have users who can access and manage resources.
Who has permission to create resources. Who has permission to access resources.
We need to create users and ble able to assign permissions.
Microsoft Entra ID - This is a cloud-based identity and access management service. This identity service can be used for Azure, Microsoft 365 and even other software as a service applciations.

Even Applications can be linked to identities and be given access accordingly.
You can define users in Microsoft Entra ID.
Authentication - Here the identity of the users are verified.
Authorization - Here the permissions are checked for the users.

Microsoft Entra ID was previousy known as Azure Active Directory. Azure AD


### What is Role-based access control
Role based access control
- We can define different roles to a user.
- There are many in-bult roles.
- You can also define your own custom roles.
- You can assign a role at the subscription level.
- You can assign a role at the resource group level.
- You can assign a role at the resource level.

* Owner Role
  * Here the user would have the complete access and be able to manage the resources. The user can also delegate access to other users.
* Contributor Role
  * Here the user would have complete access and be able to manage the resource.
* User Access Administrator Role
  * Here the user would be able to delegate access to other users.
* Reader Role
  * Here the user would be just be able to read the properties for the resources.

### Introduction to Application Objects (Identities)
We can make use of Microsoft Entra ID as the authentication and authorization provider. We delegate these tasks to Microsoft Entra ID.
In order for the Applcations to use Microsoft Entra ID, it needs to be registered in Entra ID. This is done by creating an **Application Object**
The **Application Object** is associated with a **Service Principal**. This principal is then given permissions to access resources.

Applcation -> Applciation Object -> Service Principal -> Storage Account
Users -> User Principal -> Storage Account

* LAB - Application Objects
  * create an Application Object
    * Go to Microsoft Entra ID
      * left side menu: App Registrations
      * click on "Register an Application"
        * name: blobapp
        * leave the other options with the default values and click on "REGISTER"
        * after created, left side menu click on "Certificates & Secrets" and create a New Client Secret (Copy the client secret values that it generate)
  * Go to the storage account
    * left side menu: Access Control (IAM)
      * click Add Role Assignment
        * on Members, leave the default selected
          * click + Select Memebers and search for the name "blobapp"
          * click on "Review and Assign"


### What is Microsoft Graph API
Is used to access data withim microsoft 365, to access the information of users, groups that are defined in you microsoft Azure Account.
Using Postman to execute the API requests

API stands for Application Programming Interface

1. This exposes API's that can be used to get information on let's say users and groups in your tenant/derectory.
2. Application -> Microsoft Entra ID
3. First our application needs to make a call to Microsoft Entra ID to get an Access Token
4. The Access Token will be based on the Application Object and the permissions it has.
5. The Access Token can then be used ageinst the Graph API.


* Lab Getting User Information
  * create an Application Object for your Postman Tool, named "postman"
    * left side menu click on "API permissions" inside your App Registration
    * Click on "+ Add a permission"
      * choose "Microsoft Graph"
      * choose "Application Permissions"
        * scroll down to "User" -> Select User.Read.All
      * Click on "Grant admin consent for Default Directory"
      * Add a client secret for this App Registration

1. Get the Access Token
// Line breaks are for legibility only. This is the first Request in order to GET the Access TOKEN
// MS documentation link: https://learn.microsoft.com/en-us/graph/auth-v2-service?tabs=http
POST https://login.microsoftonline.com/{tenant}/oauth2/v2.0/token HTTP/1.1
Host: login.microsoftonline.com
Content-Type: application/x-www-form-urlencoded

client_id={client-id-for-the-app-registration}
&scope=https://graph.microsoft.com/.default
&client_secret=qWgdYA....L1qKv5bPX
&grant_type=client_credentials

2. Execute a Request Against the API - List the Users
//Get the user Data
//https://learn.microsoft.com/en-us/graph/api/resources/users?view=graph-rest-1.0&preserve-view=true
//https://learn.microsoft.com/en-us/graph/api/user-list?view=graph-rest-1.0&tabs=http

curl --location 'https://graph.microsoft.com/v1.0/users' \
--header 'Authorization: Bearer {my-access-token}'

I stopped at number 211