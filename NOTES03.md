## Azure API Management

### API Management Policy - IP Restrinction
APLIES TO: All API Management Tiers  
In Azure API Management, API publishers can change the API behaviour through configuration using **policies**. Policies are a collection of statements that are run sequentially on the request or response of an API. API Management provides more than 50 policies out of the box that you can configure to address common API scenarios such as suthentication, rate limits, caching and transformation of requests and responses. For a complete list, see API Management policy reference.  
* Polular policies include:
  * Format conversion from XML to JSON
  * Call rate limiting to restrict the number of incoming call from a developer
  * Filtering requests that come from certain IP addresses

Policies are applied inside the gateway between the API consumer and the managed API. While the gateway receives requests and forwards them, unaltered, to the underlying API, a policy can apply changes to both the inboud requet and outboud response.  

Policy definitions are simple XML documents that describe a sequence of statements to apply to requests and responses.  
The policy XML configuration is divided into **inboud**, **backend**, **outbound**, and **on-error** sections. This series of specified policy statements is executed in order for a request and a response.  

* A Simple policy structure example:
```
<policies>
    <inboud>
      <!-- Statements to be applied to the requst go here -->
    <inbound>
    <backend>
      <!-- Statements to be applied before the request if forwarded to the backend service go here  -->
    </backend>
    <outbound>
      <!-- Statements to be applied to the response go here -->
    </outbound>
    <on-error>
      <!-- statements to be applied if there is an error condition go here -->
    </on-error>
</policies>
```

* LAB restrict API by IP address, using policy
  * Navigate to the APIM service, select your API -> Course API
    * For ALL operations or a single operation you can ADD Policies, to the inbound, backend or outbound processing
      * Click to add a Policy to the INBOUND Processing
      * Get your public IP address and add the policy to deny the requests from your IP
      * After adding the policy try to execute a request using POSTMAN and you should get a 403 Forbidden error

* Simple example of policy to restric an API by IP Address:
```
<policies>
    <!-- Throttle, authorize, validate, cache, or transform the requests -->
    <inboud>
      <base />
      <ip-filter action="forbid">
        <address>91.73.226.155</address>
      </ip-filter>
    <inbound>
    <!-- Control if and how the requests are forwarded to services -->
    <backend>
      <base />
    </backend>
    <!-- Customize the responses -->
    <outbound>
      <base />
    </outbound>
    <!-- Handle exceptions and customize error responses -->
    <on-error>
      <base />
    </on-error>
</policies>
```


### API Management Policy - Rewrite URL
* LAB Rewrite URL using policy
  * Navigate to the APIM service, select your API -> Course API
    * For ALL operations or a single operation you can ADD Policies, to the inbound, backend or outbound processing
      * Click to add a Policy to the INBOUND Processing
      * This policy will transform the request from a Query String, for example: "/api/course?id=5" to a "/api/course/5" style
      * Using POSTMAN try a request using query string  "/api/course?id=5" and it should work after the policy was added


* Simple example of policy to Rewrite URL:
```
<policies>
    <!-- Throttle, authorize, validate, cache, or transform the requests -->
    <inboud>
      <base />
      <set-variable name="id" value="@(context.Request.Url.Query.GetValueOfDefault("id"))" />
      <rewrite-uri template="@{return "/api/course/" + context.Variables.GetValueOrDefault<string>("id");}" />
    <inbound>
    <!-- Control if and how the requests are forwarded to services -->
    <backend>
      <base />
    </backend>
    <!-- Customize the responses -->
    <outbound>
      <base />
    </outbound>
    <!-- Handle exceptions and customize error responses -->
    <on-error>
      <base />
    </on-error>
</policies>
```


### API Management Policy - Return Response
* LAB Return Response using policy
  * Navigate to the APIM service, select your API -> Course API
    * For ALL operations or a single operation you can ADD Policies, to the inbound, backend or outbound processing
      * Click to add a Policy to the GET Courses Operation OUTBOUND Processing
      * The policy example bellow will add a custom header called "Response-reason" and execute a minor transformation on the response Body

* Simple example of policy changing the return response 
```
<policies>
    <!-- Throttle, authorize, validate, cache, or transform the requests -->
    <inboud>
      <base />
    <inbound>
    <!-- Control if and how the requests are forwarded to services -->
    <backend>
      <base />
    </backend>
    <!-- Customize the responses -->
    <outbound>
      <base />
      <choose>
        <when condition="@(context.Response.StatusCode == 200)">
          <return-response>
            <set-status code="200" reason="OK" />
            <set-header name="Response-reason" exists-action="override">
              <value>"Returned course list"</value>
            </set-header>
            <set-body>
              @{
                string text = context.Response.Body.As<string>(preserveContent: true);
                return text;
              }
            </set-body>
          </return-response>
        </when>
      </choose>
    </outbound>
    <!-- Handle exceptions and customize error responses -->
    <on-error>
      <base />
    </on-error>
</policies>
```


### API Management Policy - Cache
* LAB Cache using policy
  * Navigate to the APIM service, select your API -> Course API
    * For ALL operations or a single operation you can ADD Policies, to the inbound, backend or outbound processing
      * Click to add a Policy to the GET Courses Operation INBOUND Processing
      * The policy example bellow will cache the response for 60 seconds

* Simple example of policy with caching
```
<policies>
    <!-- Throttle, authorize, validate, cache, or transform the requests -->
    <inboud>
      <base />
      <cache-lookup vary-by-developer="false" vary-by-developer-groups="false" downstream-caching-type="none" must-revalidate="true" caching-type="internal" />
    <inbound>
    <!-- Control if and how the requests are forwarded to services -->
    <backend>
      <base />
    </backend>
    <!-- Customize the responses -->
    <outbound>
      <cache-store duration="60" />
      <base />
    </outbound>
    <!-- Handle exceptions and customize error responses -->
    <on-error>
      <base />
    </on-error>
</policies>
```

### API Management - Virtual Network
Deploy your Azure API Management instance to a virtual network - External mode  
APPLIES TO: Developer | Premium  
Azure API Management can be deployed (injected) inside an Azure virtual network (VNet) to access backend services within the network.  
This way the APIs will not be exposed to the internet.

* LAB
  * Deploy a new Azure Virutal Machine, with an web server with a web app running on port 80
    * After deploying you web app and testing it. The virtual Machine should not have a public IP adress (no access from the internet). So remove the link to the public IP address from the VM
    * Add a DNS name to the Public IP Address, (Go to the public IP, left side menu under settings -> Click on "Configuration" "api1000.northeurope.cloudapp.azure.com")
  * Go to the VNet
    * left side menu "Subnets", click to Add a new Subnet (This will be for the APIM service)
      * name = apisubnet
      * Network Security Group to allow port 80
      * Click ADD
  * Go to the APIM instance, left side menu search for "Network" and click on it
    * choose Virtual Network = "External" option
      * Select the Virtual Network the same from the Virtual Machine and the Subnet created, and the Public IP Address that has the DNS name
      * Click Apply and after Save (It will take some time, 15 to 45 minutes)
    * Define a new API on APIM (Course API)
      * **The Web Server URL** will be the private IP address from the Azure Virtual Machine
        * click CREATE
        * click Add Operation (Get Courses) GE /api/course
      * Test the API call and it should work (Now this time, the API is hosted on an Azure Virtual Machine)