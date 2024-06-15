# Implement Azure Security - Authentication and Authorization

## Authentication and Authorization

Authentication -> This is the process where you prove that you are who you say you are  
Authorization -> This is the process of granting access to perform an action or resource  

Microsoft Entra ID -> Is an Identity Provider  
Role-based access control  

Here users and applications can be authorized to use API's and Azure Resources.  

1. Old era of authentication
   1. Database for users names and passwords
   2. Problems:
      1. You have to maintain the database of user names and passwords
      2. You need to maintain the security of the database
      3. You need to implement newer methods of authentication - Multi-Factor Authentication
      4. The application itself is responsible for authenticating the user
2. New Era
   1. Sign in with Apple ID, Google ID, Linkedln, Microsoft
   2. Applications might now offload the authentication to other providers
   3. In a similar way, Microsoft Entra ID is an Identity Provider


#### APIs And Authorization
* API -> Application programming Interface
* APIs Examples:
  * Get Information of customers
  * Get Information of Orders
  * Get Information of Products


### Microsoft Authentication Library (MSAL)
The Microsoft Authentication Library (MSAL) enables developers to acquire security tokens from the Microsoft identity platform to authenticate users and access secured web APIs. It can be used to provide secure access to Microsoft Graph, other Microsoft APIs, third-party web APIs, or your own web API. MSAL supports many different applications architectures and platforms including .NET, JavaScript, Java, Phyton, Android, and IOS.  
https://learn.microsoft.com/en-us/entra/identity-platform/msal-overview  
https://learn.microsoft.com/en-us/entra/identity-platform/index-web-app  

#### Oauth 2.0
Very nice site about oauth
https://oauth.net

Industry-standard protocol for authorization, a set of standards on how clients can authorize thenselves to access a resource.  
A web application on behalf of a user wants to access a resource that is in the Azure Storage Account, we can use the Authorization Code Flow.  


##### Oauth Grant Types
1. Client Credentials (https://www.oauth.com/oauth2-servers/access-tokens/client-credentials/)
   1. grant_type = client_credentials
      1. First get the Access Token using an client_id and a client_secret, scope (optional). (You can pass this info in the body x-www-form-urlencoded format)
      2. After that you can call the API using the Access Token -> Header - Authorization "Bearer <access-token>"
   2. This is a type when it comes to Oauth. This is used by applications that need to get an access token outside the context of a user.
   3. The Client Credentials grant is used when applications request an access token to access their own resources, not on behalf of a user.
2. Authorization Code (https://developer.okta.com/blog/2018/04/10/oauth-authorization-code-grant-type)
   1. The Authorization Code grant type is used by confidential and public clients to exchange an authorization code for an access token.
   2. The first step in the flow is to get an authorization code
      1. The applciation will open up the browser and the user will be directed to the Authorization Server. The request will have the scope of what resources are beign requested by the user.
   3. A code will be sent in the response back. There needs to be a redirect URI in place, so that the Authorization Server can send the code back to the web application.
   4. The web application now formulates a backend POST request to exchange the code for an Access Token.
   5. Here Microsoft Entra ID will send the access token which will have the required permissions.
   6. Here we have an entire 2 step proccess which is more secure.
   7. The Application can now use the Access Token to request access to the storage API for the user.


###### Authorization Code Workflow
1. User -> Resource Owner - This is the user who has access to the protected resource.
2. Web Application -> Client - This is the applcation requesting access to the protected resource.
3. Azure Storage Account -> Resource Server - In Azure, this can be a Web API that will allow access onto the Azure Resource.
4. Authorization Server -> Microsoft Identity Platform is the authorization server. It manages the end-users information, their access and also issues security tokens.

##### So how does Authorization Code Flow Work?
* Step 1
  * The application makes a call to the Authorization Server
  * The Authorization Server responds with a Redirect URI http://cloudportalhub.com/callback
* Step 2
  * The authorization server sends the authorization code to the application
  * The authorization code is just the initial step in the proccess. The application can't do much with this code
  * The application then needs to use the authorization code to get an access token
  * The authorization code is viewable in the browser
  * But the later on proccess of getting the access token with the use of the authorization code is done by the application in the backend.
* Step 3
  * The application requests for an access token. The access token will have the permissions of the user.
* Step 4
  * The web application will now ask the resource server for access to the resource


#### OpenID Connect on the Microsoft identity platform
OpenID Connect (OIDC) extends the Oauth 2.0 authorization protocol for use as an additional protocol. You can use OIDC to enable single sign-on (SSO) between your Oauth-enabled applications by using a security token called an **ID Token**.  


### Lab: Asp Net Application - Adding Authentication
* Create a new App Net web application
* Go to Azure and create a new Application Object
  * Microsoft Entra ID -> Default Directory -> left side menu -> App Registration
    * Click +New Registration
      * Name: AuthApp
      * Click Register

### Lab -> Getting a blob content using the postman tools and oauth 2.0 Client Credentials Flow
* Have a storage account created and a postman application object in place
* Go to the storage account
  * left side menu -> Access Control (IAM)
    * click Add Role Assignment, select the READER role and add the assignment to the postman application object
    * click Add Role Assignment, select the STORAGE BLOB READER role and add the assignment to the postman application object
* Open postman in your computer
  * Set up the following headers:
    * grant_type = client_credentials
    * client_id = <my-client-id>
    * client_secret = <my-client-secret>
    * scope = https://storage.azure.com/.default
  * POST request with the following URL: https://login.microsoftonline.com/<account-id>/oauth/v2.0/token
* Go to the storage account and copy the blob URL
  * Execute a GET request to the URL that was copied, send the request and it will answer with a 404 which is expected.
  * Add the following headers:
    * Authorization = Bearer <the-token-that-was-generated-before>
    * x-ms-version = 2017-11-09
  * Execute the request again and now it will answer with a 200 OK and the blob content.

### Lab -> Creating a protected Rest API