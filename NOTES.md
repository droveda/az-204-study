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
We need to create users and be able to assign permissions.  
Microsoft Entra ID - This is a cloud-based identity and access management service. This identity service can be used for Azure, Microsoft 365 and even other software as a service applciations.  
  
Even Applications can be linked to identities and be given access accordingly.  
You can define users in Microsoft Entra ID.  
Authentication - Here the identity of the users are verified.  
Authorization - Here the permissions are checked for the users.  

Microsoft Entra ID was previousy known as Azure Active Directory. Azure AD


### What is Role-based access control
Role based access control:
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
  * Here the user would just be able to read the properties for the resources.

### Introduction to Application Objects (Identities)
We can make use of Microsoft Entra ID as the authentication and authorization provider. We delegate these tasks to Microsoft Entra ID.  
In order for the Applications to use Microsoft Entra ID, it needs to be registered in Entra ID. This is done by creating an **Application Object**  
The **Application Object** is associated with a **Service Principal**. This principal is then given permissions to access resources.  

- Application -> Application Object -> Service Principal -> Storage Account  
- Users -> User Principal -> Storage Account  

---  

* LAB - Application Objects
  * create an Application Object
    * Go to Microsoft Entra ID
      * left side menu: App Registrations
      * click on "Register an Application"
        * name: blobapp
        * leave the other options with the default values and click on "REGISTER"
        * after created, left side menu click on "Certificates & Secrets" and create a New Client Secret (Copy the client secret value that was generated)
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

1. This exposes API's that can be used to get information on let's say users and groups in your tenant/directory.
2. Application -> Microsoft Entra ID
3. First our application needs to make a call to Microsoft Entra ID to get an Access Token
4. The Access Token will be based on the Application Object and the permissions it has.
5. The Access Token can then be used against the Graph API.


* Lab Getting User Information
  * create an Application Object for your Postman Tool, named "postman"
    * left side menu click on "API permissions" inside your App Registration
    * Click on "+ Add a permission"
      * choose "Microsoft Graph"
      * choose "Application Permissions"
        * scroll down to "User" -> Select User.Read.All
      * Click on "Grant admin consent for Default Directory"
      * Add a client secret for this App Registration (Copy the secret)

1. Get the Access Token
```  
// Line breaks are for legibility only. This is the first Request in order to GET the Access TOKEN
// MS documentation link: https://learn.microsoft.com/en-us/graph/auth-v2-service?tabs=http
POST https://login.microsoftonline.com/{tenant}/oauth2/v2.0/token HTTP/1.1
Host: login.microsoftonline.com
Content-Type: application/x-www-form-urlencoded

client_id={client-id-for-the-app-registration}
&scope=https://graph.microsoft.com/.default
&client_secret=qWgdYA....L1qKv5bPX
&grant_type=client_credentials
```

2. Execute a Request Against the API - List the Users  
```
//Get the user Data
//https://learn.microsoft.com/en-us/graph/api/resources/users?view=graph-rest-1.0&preserve-view=true
//https://learn.microsoft.com/en-us/graph/api/user-list?view=graph-rest-1.0&tabs=http

curl --location 'https://graph.microsoft.com/v1.0/users' \
--header 'Authorization: Bearer {my-access-token}'
```


## Azure Key Vault
The Azure Key Vault is an managed service that can be used to store secrets, encryption keys and certificates.

- Database credetials
- Encryption of data
- Use certificates for HTTPS

* Lab:
  * Create a new Key-Vault using the default values
  * Give the Role "Vault Administrator" to the user admin account for that key-vault
  * Create a new Encryption key in the key vault (RSA 2048, other vaules keep the defaults)
  * Create a new Application Object/"App Registration" or use one already created 
  * Give access to the Applciation Object to access the Key-vault (Key Vault Crypto User Role)
  * Create a Simple Java Program to use the encryption key KeyClient class
    * https://learn.microsoft.com/en-us/java/api/com.azure.security.keyvault.keys.keyclient?view=azure-java-stable
    * https://learn.microsoft.com/en-us/java/api/overview/azure/security-keyvault-keys-readme?view=azure-java-stable#encrypt
  * Create a Simple Secret in the Key-vault
    * Give access to the Applciation Object to access the Key-vault (Key Vault Secrets User Role)
    * Create a Simple Java Program to get the secret value


## Managed Identities
You can make use of Managed Identities. This gives a way for applications to authenticate to Azure Resources without the need of embedding credentials.

- Your application cloud be hosted on a service that supports managed identities.
- The managed identity for the resource can be registered in Microsoft Entra ID. This would create a service principal for that resource.
- You can then provide RBAC access for that service principal onto the resource. And in your code you don't embed any sort of credentials.
- We will look into an example of having application hosted on a Virtual Machine that is acessing the blob service.

* LAB:
  * Create a new Virtual Machine
  * Go to the machine left side menu, under security click on "Identity"
  * Enable a System assigned Managed Identity, turn ON and cLick on SAVE
  * Go to the Storage Account (Access Control IAM)
    * click on Add Role Assignment
    * select the role: Storage Blob Data Contributor
    * choose **Managed Identity**
    * Click Select Members
      * Select the Virtual Machine, click select
      * Review and Assign
  * Create a Java Program to test the use of Managed Identity .credential(new DefaultAzureCredentialBuilder().build())    

You can Have two types of Managed Identityes
1. System Assigned Managed Identities
2. User Assigned Managed Identities


### Managed Identity - Getting the Access Token
* https://learn.microsoft.com/en-us/entra/identity/managed-identities-azure-resources/how-to-use-vm-token
* https://learn.microsoft.com/en-us/entra/identity/managed-identities-azure-resources/how-to-use-vm-token#get-a-token-using-java
* curl 'http://169.254.169.254/metadata/identity/oauth2/token?api-version=2018-02-01&resource=https://storage.azure.com/' -H Metadata:true -s

### Managed Identity - Using the Access Token
* After getting the access token we can use it in the blob storage for example
* add header "Authorization" with value "Bearer <token-value>"
* add header: x-ms-version = 2024-05-04
* http url -> blobUri = https://{storageAccountName}.blob.core.windows.net/{containerName}/{fileName}


### Power Shell Enable Managed Identity


### Note on User Assigned Managed Identities
* Create a new User Assigned Managed Identity
  * Inform: Subscription, Resource Group, Region and Name
  * After if was created you can assign it to a Virtual Machine for example
  * You can see it as a standalone resource
* The key difference of an User Assigned Managed Identity is that it has a separated lifecycle of the resource, a System Assigned Managed Identity if you delete the VM for example it will also delete the System Managed Identity
* With **User Assigned Managed Identity** you can assign the same identity to a Virtual Machine and an Azure Function or any other resource for instance. 

