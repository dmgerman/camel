begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce.internal.client
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|salesforce
operator|.
name|internal
operator|.
name|client
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|UnsupportedEncodingException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URLEncoder
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|com
operator|.
name|thoughtworks
operator|.
name|xstream
operator|.
name|XStream
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|salesforce
operator|.
name|SalesforceHttpClient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|salesforce
operator|.
name|api
operator|.
name|SalesforceException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|salesforce
operator|.
name|api
operator|.
name|SalesforceMultipleChoicesException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|salesforce
operator|.
name|api
operator|.
name|dto
operator|.
name|RestError
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|salesforce
operator|.
name|internal
operator|.
name|PayloadFormat
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|salesforce
operator|.
name|internal
operator|.
name|SalesforceSession
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|salesforce
operator|.
name|internal
operator|.
name|dto
operator|.
name|RestChoices
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|salesforce
operator|.
name|internal
operator|.
name|dto
operator|.
name|RestErrors
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|URISupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|jackson
operator|.
name|map
operator|.
name|ObjectMapper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|jackson
operator|.
name|type
operator|.
name|TypeReference
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|client
operator|.
name|api
operator|.
name|Request
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|client
operator|.
name|api
operator|.
name|Response
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|client
operator|.
name|util
operator|.
name|InputStreamContentProvider
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|http
operator|.
name|HttpHeader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|http
operator|.
name|HttpMethod
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|http
operator|.
name|HttpStatus
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|util
operator|.
name|StringUtil
import|;
end_import

begin_class
DECL|class|DefaultRestClient
specifier|public
class|class
name|DefaultRestClient
extends|extends
name|AbstractClientBase
implements|implements
name|RestClient
block|{
DECL|field|SERVICES_DATA
specifier|private
specifier|static
specifier|final
name|String
name|SERVICES_DATA
init|=
literal|"/services/data/"
decl_stmt|;
DECL|field|TOKEN_HEADER
specifier|private
specifier|static
specifier|final
name|String
name|TOKEN_HEADER
init|=
literal|"Authorization"
decl_stmt|;
DECL|field|TOKEN_PREFIX
specifier|private
specifier|static
specifier|final
name|String
name|TOKEN_PREFIX
init|=
literal|"Bearer "
decl_stmt|;
DECL|field|SERVICES_APEXREST
specifier|private
specifier|static
specifier|final
name|String
name|SERVICES_APEXREST
init|=
literal|"/services/apexrest/"
decl_stmt|;
DECL|field|format
specifier|protected
name|PayloadFormat
name|format
decl_stmt|;
DECL|field|objectMapper
specifier|private
name|ObjectMapper
name|objectMapper
decl_stmt|;
DECL|field|xStream
specifier|private
name|XStream
name|xStream
decl_stmt|;
DECL|method|DefaultRestClient (SalesforceHttpClient httpClient, String version, PayloadFormat format, SalesforceSession session)
specifier|public
name|DefaultRestClient
parameter_list|(
name|SalesforceHttpClient
name|httpClient
parameter_list|,
name|String
name|version
parameter_list|,
name|PayloadFormat
name|format
parameter_list|,
name|SalesforceSession
name|session
parameter_list|)
throws|throws
name|SalesforceException
block|{
name|super
argument_list|(
name|version
argument_list|,
name|session
argument_list|,
name|httpClient
argument_list|)
expr_stmt|;
name|this
operator|.
name|format
operator|=
name|format
expr_stmt|;
comment|// initialize error parsers for JSON and XML
name|this
operator|.
name|objectMapper
operator|=
operator|new
name|ObjectMapper
argument_list|()
expr_stmt|;
name|this
operator|.
name|xStream
operator|=
operator|new
name|XStream
argument_list|()
expr_stmt|;
name|xStream
operator|.
name|processAnnotations
argument_list|(
name|RestErrors
operator|.
name|class
argument_list|)
expr_stmt|;
name|xStream
operator|.
name|processAnnotations
argument_list|(
name|RestChoices
operator|.
name|class
argument_list|)
expr_stmt|;
name|XStreamUtils
operator|.
name|addDefaultPermissions
argument_list|(
name|xStream
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doHttpRequest (Request request, ClientResponseCallback callback)
specifier|protected
name|void
name|doHttpRequest
parameter_list|(
name|Request
name|request
parameter_list|,
name|ClientResponseCallback
name|callback
parameter_list|)
block|{
comment|// set standard headers for all requests
specifier|final
name|String
name|contentType
init|=
name|PayloadFormat
operator|.
name|JSON
operator|.
name|equals
argument_list|(
name|format
argument_list|)
condition|?
name|APPLICATION_JSON_UTF8
else|:
name|APPLICATION_XML_UTF8
decl_stmt|;
name|request
operator|.
name|header
argument_list|(
name|HttpHeader
operator|.
name|ACCEPT
argument_list|,
name|contentType
argument_list|)
expr_stmt|;
name|request
operator|.
name|header
argument_list|(
name|HttpHeader
operator|.
name|ACCEPT_CHARSET
argument_list|,
name|StringUtil
operator|.
name|__UTF8
argument_list|)
expr_stmt|;
comment|// request content type and charset is set by the request entity
name|super
operator|.
name|doHttpRequest
argument_list|(
name|request
argument_list|,
name|callback
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRestException (Response response, InputStream responseContent)
specifier|protected
name|SalesforceException
name|createRestException
parameter_list|(
name|Response
name|response
parameter_list|,
name|InputStream
name|responseContent
parameter_list|)
block|{
comment|// get status code and reason phrase
specifier|final
name|int
name|statusCode
init|=
name|response
operator|.
name|getStatus
argument_list|()
decl_stmt|;
name|String
name|reason
init|=
name|response
operator|.
name|getReason
argument_list|()
decl_stmt|;
if|if
condition|(
name|reason
operator|==
literal|null
operator|||
name|reason
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|reason
operator|=
name|HttpStatus
operator|.
name|getMessage
argument_list|(
name|statusCode
argument_list|)
expr_stmt|;
block|}
comment|// try parsing response according to format
try|try
block|{
if|if
condition|(
name|responseContent
operator|!=
literal|null
operator|&&
name|responseContent
operator|.
name|available
argument_list|()
operator|>
literal|0
condition|)
block|{
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|choices
decl_stmt|;
comment|// return list of choices as error message for 300
if|if
condition|(
name|statusCode
operator|==
name|HttpStatus
operator|.
name|MULTIPLE_CHOICES_300
condition|)
block|{
if|if
condition|(
name|PayloadFormat
operator|.
name|JSON
operator|.
name|equals
argument_list|(
name|format
argument_list|)
condition|)
block|{
name|choices
operator|=
name|objectMapper
operator|.
name|readValue
argument_list|(
name|responseContent
argument_list|,
operator|new
name|TypeReference
argument_list|<
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|()
block|{}
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|RestChoices
name|restChoices
init|=
operator|new
name|RestChoices
argument_list|()
decl_stmt|;
name|xStream
operator|.
name|fromXML
argument_list|(
name|responseContent
argument_list|,
name|restChoices
argument_list|)
expr_stmt|;
name|choices
operator|=
name|restChoices
operator|.
name|getUrls
argument_list|()
expr_stmt|;
block|}
return|return
operator|new
name|SalesforceMultipleChoicesException
argument_list|(
name|reason
argument_list|,
name|statusCode
argument_list|,
name|choices
argument_list|)
return|;
block|}
else|else
block|{
specifier|final
name|List
argument_list|<
name|RestError
argument_list|>
name|restErrors
decl_stmt|;
if|if
condition|(
name|PayloadFormat
operator|.
name|JSON
operator|.
name|equals
argument_list|(
name|format
argument_list|)
condition|)
block|{
name|restErrors
operator|=
name|objectMapper
operator|.
name|readValue
argument_list|(
name|responseContent
argument_list|,
operator|new
name|TypeReference
argument_list|<
name|List
argument_list|<
name|RestError
argument_list|>
argument_list|>
argument_list|()
block|{                             }
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|RestErrors
name|errors
init|=
operator|new
name|RestErrors
argument_list|()
decl_stmt|;
name|xStream
operator|.
name|fromXML
argument_list|(
name|responseContent
argument_list|,
name|errors
argument_list|)
expr_stmt|;
name|restErrors
operator|=
name|errors
operator|.
name|getErrors
argument_list|()
expr_stmt|;
block|}
return|return
operator|new
name|SalesforceException
argument_list|(
name|restErrors
argument_list|,
name|statusCode
argument_list|)
return|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// log and ignore
name|String
name|msg
init|=
literal|"Unexpected Error parsing "
operator|+
name|format
operator|+
literal|" error response body + ["
operator|+
name|responseContent
operator|+
literal|"] : "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
decl_stmt|;
name|log
operator|.
name|warn
argument_list|(
name|msg
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
comment|// log and ignore
name|String
name|msg
init|=
literal|"Unexpected Error parsing "
operator|+
name|format
operator|+
literal|" error response body + ["
operator|+
name|responseContent
operator|+
literal|"] : "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
decl_stmt|;
name|log
operator|.
name|warn
argument_list|(
name|msg
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
comment|// just report HTTP status info
return|return
operator|new
name|SalesforceException
argument_list|(
literal|"Unexpected error: "
operator|+
name|reason
operator|+
literal|", with content: "
operator|+
name|responseContent
argument_list|,
name|statusCode
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getVersions (final ResponseCallback callback)
specifier|public
name|void
name|getVersions
parameter_list|(
specifier|final
name|ResponseCallback
name|callback
parameter_list|)
block|{
name|Request
name|get
init|=
name|getRequest
argument_list|(
name|HttpMethod
operator|.
name|GET
argument_list|,
name|servicesDataUrl
argument_list|()
argument_list|)
decl_stmt|;
comment|// does not require authorization token
name|doHttpRequest
argument_list|(
name|get
argument_list|,
operator|new
name|DelegatingClientCallback
argument_list|(
name|callback
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getResources (ResponseCallback callback)
specifier|public
name|void
name|getResources
parameter_list|(
name|ResponseCallback
name|callback
parameter_list|)
block|{
name|Request
name|get
init|=
name|getRequest
argument_list|(
name|HttpMethod
operator|.
name|GET
argument_list|,
name|versionUrl
argument_list|()
argument_list|)
decl_stmt|;
comment|// requires authorization token
name|setAccessToken
argument_list|(
name|get
argument_list|)
expr_stmt|;
name|doHttpRequest
argument_list|(
name|get
argument_list|,
operator|new
name|DelegatingClientCallback
argument_list|(
name|callback
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getGlobalObjects (ResponseCallback callback)
specifier|public
name|void
name|getGlobalObjects
parameter_list|(
name|ResponseCallback
name|callback
parameter_list|)
block|{
name|Request
name|get
init|=
name|getRequest
argument_list|(
name|HttpMethod
operator|.
name|GET
argument_list|,
name|sobjectsUrl
argument_list|(
literal|""
argument_list|)
argument_list|)
decl_stmt|;
comment|// requires authorization token
name|setAccessToken
argument_list|(
name|get
argument_list|)
expr_stmt|;
name|doHttpRequest
argument_list|(
name|get
argument_list|,
operator|new
name|DelegatingClientCallback
argument_list|(
name|callback
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getBasicInfo (String sObjectName, ResponseCallback callback)
specifier|public
name|void
name|getBasicInfo
parameter_list|(
name|String
name|sObjectName
parameter_list|,
name|ResponseCallback
name|callback
parameter_list|)
block|{
name|Request
name|get
init|=
name|getRequest
argument_list|(
name|HttpMethod
operator|.
name|GET
argument_list|,
name|sobjectsUrl
argument_list|(
name|sObjectName
operator|+
literal|"/"
argument_list|)
argument_list|)
decl_stmt|;
comment|// requires authorization token
name|setAccessToken
argument_list|(
name|get
argument_list|)
expr_stmt|;
name|doHttpRequest
argument_list|(
name|get
argument_list|,
operator|new
name|DelegatingClientCallback
argument_list|(
name|callback
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getDescription (String sObjectName, ResponseCallback callback)
specifier|public
name|void
name|getDescription
parameter_list|(
name|String
name|sObjectName
parameter_list|,
name|ResponseCallback
name|callback
parameter_list|)
block|{
name|Request
name|get
init|=
name|getRequest
argument_list|(
name|HttpMethod
operator|.
name|GET
argument_list|,
name|sobjectsUrl
argument_list|(
name|sObjectName
operator|+
literal|"/describe/"
argument_list|)
argument_list|)
decl_stmt|;
comment|// requires authorization token
name|setAccessToken
argument_list|(
name|get
argument_list|)
expr_stmt|;
name|doHttpRequest
argument_list|(
name|get
argument_list|,
operator|new
name|DelegatingClientCallback
argument_list|(
name|callback
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getSObject (String sObjectName, String id, String[] fields, ResponseCallback callback)
specifier|public
name|void
name|getSObject
parameter_list|(
name|String
name|sObjectName
parameter_list|,
name|String
name|id
parameter_list|,
name|String
index|[]
name|fields
parameter_list|,
name|ResponseCallback
name|callback
parameter_list|)
block|{
comment|// parse fields if set
name|String
name|params
init|=
literal|""
decl_stmt|;
if|if
condition|(
name|fields
operator|!=
literal|null
operator|&&
name|fields
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|StringBuilder
name|fieldsValue
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"?fields="
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|fields
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|fieldsValue
operator|.
name|append
argument_list|(
name|fields
index|[
name|i
index|]
argument_list|)
expr_stmt|;
if|if
condition|(
name|i
operator|<
operator|(
name|fields
operator|.
name|length
operator|-
literal|1
operator|)
condition|)
block|{
name|fieldsValue
operator|.
name|append
argument_list|(
literal|','
argument_list|)
expr_stmt|;
block|}
block|}
name|params
operator|=
name|fieldsValue
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
name|Request
name|get
init|=
name|getRequest
argument_list|(
name|HttpMethod
operator|.
name|GET
argument_list|,
name|sobjectsUrl
argument_list|(
name|sObjectName
operator|+
literal|"/"
operator|+
name|id
operator|+
name|params
argument_list|)
argument_list|)
decl_stmt|;
comment|// requires authorization token
name|setAccessToken
argument_list|(
name|get
argument_list|)
expr_stmt|;
name|doHttpRequest
argument_list|(
name|get
argument_list|,
operator|new
name|DelegatingClientCallback
argument_list|(
name|callback
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createSObject (String sObjectName, InputStream sObject, ResponseCallback callback)
specifier|public
name|void
name|createSObject
parameter_list|(
name|String
name|sObjectName
parameter_list|,
name|InputStream
name|sObject
parameter_list|,
name|ResponseCallback
name|callback
parameter_list|)
block|{
comment|// post the sObject
specifier|final
name|Request
name|post
init|=
name|getRequest
argument_list|(
name|HttpMethod
operator|.
name|POST
argument_list|,
name|sobjectsUrl
argument_list|(
name|sObjectName
argument_list|)
argument_list|)
decl_stmt|;
comment|// authorization
name|setAccessToken
argument_list|(
name|post
argument_list|)
expr_stmt|;
comment|// input stream as entity content
name|post
operator|.
name|content
argument_list|(
operator|new
name|InputStreamContentProvider
argument_list|(
name|sObject
argument_list|)
argument_list|)
expr_stmt|;
name|post
operator|.
name|header
argument_list|(
name|HttpHeader
operator|.
name|CONTENT_TYPE
argument_list|,
name|PayloadFormat
operator|.
name|JSON
operator|.
name|equals
argument_list|(
name|format
argument_list|)
condition|?
name|APPLICATION_JSON_UTF8
else|:
name|APPLICATION_XML_UTF8
argument_list|)
expr_stmt|;
name|doHttpRequest
argument_list|(
name|post
argument_list|,
operator|new
name|DelegatingClientCallback
argument_list|(
name|callback
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|updateSObject (String sObjectName, String id, InputStream sObject, ResponseCallback callback)
specifier|public
name|void
name|updateSObject
parameter_list|(
name|String
name|sObjectName
parameter_list|,
name|String
name|id
parameter_list|,
name|InputStream
name|sObject
parameter_list|,
name|ResponseCallback
name|callback
parameter_list|)
block|{
specifier|final
name|Request
name|patch
init|=
name|getRequest
argument_list|(
literal|"PATCH"
argument_list|,
name|sobjectsUrl
argument_list|(
name|sObjectName
operator|+
literal|"/"
operator|+
name|id
argument_list|)
argument_list|)
decl_stmt|;
comment|// requires authorization token
name|setAccessToken
argument_list|(
name|patch
argument_list|)
expr_stmt|;
comment|// input stream as entity content
name|patch
operator|.
name|content
argument_list|(
operator|new
name|InputStreamContentProvider
argument_list|(
name|sObject
argument_list|)
argument_list|)
expr_stmt|;
name|patch
operator|.
name|header
argument_list|(
name|HttpHeader
operator|.
name|CONTENT_TYPE
argument_list|,
name|PayloadFormat
operator|.
name|JSON
operator|.
name|equals
argument_list|(
name|format
argument_list|)
condition|?
name|APPLICATION_JSON_UTF8
else|:
name|APPLICATION_XML_UTF8
argument_list|)
expr_stmt|;
name|doHttpRequest
argument_list|(
name|patch
argument_list|,
operator|new
name|DelegatingClientCallback
argument_list|(
name|callback
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|deleteSObject (String sObjectName, String id, ResponseCallback callback)
specifier|public
name|void
name|deleteSObject
parameter_list|(
name|String
name|sObjectName
parameter_list|,
name|String
name|id
parameter_list|,
name|ResponseCallback
name|callback
parameter_list|)
block|{
specifier|final
name|Request
name|delete
init|=
name|getRequest
argument_list|(
name|HttpMethod
operator|.
name|DELETE
argument_list|,
name|sobjectsUrl
argument_list|(
name|sObjectName
operator|+
literal|"/"
operator|+
name|id
argument_list|)
argument_list|)
decl_stmt|;
comment|// requires authorization token
name|setAccessToken
argument_list|(
name|delete
argument_list|)
expr_stmt|;
name|doHttpRequest
argument_list|(
name|delete
argument_list|,
operator|new
name|DelegatingClientCallback
argument_list|(
name|callback
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getSObjectWithId (String sObjectName, String fieldName, String fieldValue, ResponseCallback callback)
specifier|public
name|void
name|getSObjectWithId
parameter_list|(
name|String
name|sObjectName
parameter_list|,
name|String
name|fieldName
parameter_list|,
name|String
name|fieldValue
parameter_list|,
name|ResponseCallback
name|callback
parameter_list|)
block|{
specifier|final
name|Request
name|get
init|=
name|getRequest
argument_list|(
name|HttpMethod
operator|.
name|GET
argument_list|,
name|sobjectsExternalIdUrl
argument_list|(
name|sObjectName
argument_list|,
name|fieldName
argument_list|,
name|fieldValue
argument_list|)
argument_list|)
decl_stmt|;
comment|// requires authorization token
name|setAccessToken
argument_list|(
name|get
argument_list|)
expr_stmt|;
name|doHttpRequest
argument_list|(
name|get
argument_list|,
operator|new
name|DelegatingClientCallback
argument_list|(
name|callback
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|upsertSObject (String sObjectName, String fieldName, String fieldValue, InputStream sObject, ResponseCallback callback)
specifier|public
name|void
name|upsertSObject
parameter_list|(
name|String
name|sObjectName
parameter_list|,
name|String
name|fieldName
parameter_list|,
name|String
name|fieldValue
parameter_list|,
name|InputStream
name|sObject
parameter_list|,
name|ResponseCallback
name|callback
parameter_list|)
block|{
specifier|final
name|Request
name|patch
init|=
name|getRequest
argument_list|(
literal|"PATCH"
argument_list|,
name|sobjectsExternalIdUrl
argument_list|(
name|sObjectName
argument_list|,
name|fieldName
argument_list|,
name|fieldValue
argument_list|)
argument_list|)
decl_stmt|;
comment|// requires authorization token
name|setAccessToken
argument_list|(
name|patch
argument_list|)
expr_stmt|;
comment|// input stream as entity content
name|patch
operator|.
name|content
argument_list|(
operator|new
name|InputStreamContentProvider
argument_list|(
name|sObject
argument_list|)
argument_list|)
expr_stmt|;
comment|// TODO will the encoding always be UTF-8??
name|patch
operator|.
name|header
argument_list|(
name|HttpHeader
operator|.
name|CONTENT_TYPE
argument_list|,
name|PayloadFormat
operator|.
name|JSON
operator|.
name|equals
argument_list|(
name|format
argument_list|)
condition|?
name|APPLICATION_JSON_UTF8
else|:
name|APPLICATION_XML_UTF8
argument_list|)
expr_stmt|;
name|doHttpRequest
argument_list|(
name|patch
argument_list|,
operator|new
name|DelegatingClientCallback
argument_list|(
name|callback
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|deleteSObjectWithId (String sObjectName, String fieldName, String fieldValue, ResponseCallback callback)
specifier|public
name|void
name|deleteSObjectWithId
parameter_list|(
name|String
name|sObjectName
parameter_list|,
name|String
name|fieldName
parameter_list|,
name|String
name|fieldValue
parameter_list|,
name|ResponseCallback
name|callback
parameter_list|)
block|{
specifier|final
name|Request
name|delete
init|=
name|getRequest
argument_list|(
name|HttpMethod
operator|.
name|DELETE
argument_list|,
name|sobjectsExternalIdUrl
argument_list|(
name|sObjectName
argument_list|,
name|fieldName
argument_list|,
name|fieldValue
argument_list|)
argument_list|)
decl_stmt|;
comment|// requires authorization token
name|setAccessToken
argument_list|(
name|delete
argument_list|)
expr_stmt|;
name|doHttpRequest
argument_list|(
name|delete
argument_list|,
operator|new
name|DelegatingClientCallback
argument_list|(
name|callback
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getBlobField (String sObjectName, String id, String blobFieldName, ResponseCallback callback)
specifier|public
name|void
name|getBlobField
parameter_list|(
name|String
name|sObjectName
parameter_list|,
name|String
name|id
parameter_list|,
name|String
name|blobFieldName
parameter_list|,
name|ResponseCallback
name|callback
parameter_list|)
block|{
specifier|final
name|Request
name|get
init|=
name|getRequest
argument_list|(
name|HttpMethod
operator|.
name|GET
argument_list|,
name|sobjectsUrl
argument_list|(
name|sObjectName
operator|+
literal|"/"
operator|+
name|id
operator|+
literal|"/"
operator|+
name|blobFieldName
argument_list|)
argument_list|)
decl_stmt|;
comment|// TODO this doesn't seem to be required, the response is always the content binary stream
comment|//get.header(HttpHeader.ACCEPT_ENCODING, "base64");
comment|// requires authorization token
name|setAccessToken
argument_list|(
name|get
argument_list|)
expr_stmt|;
name|doHttpRequest
argument_list|(
name|get
argument_list|,
operator|new
name|DelegatingClientCallback
argument_list|(
name|callback
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|query (String soqlQuery, ResponseCallback callback)
specifier|public
name|void
name|query
parameter_list|(
name|String
name|soqlQuery
parameter_list|,
name|ResponseCallback
name|callback
parameter_list|)
block|{
try|try
block|{
name|String
name|encodedQuery
init|=
name|urlEncode
argument_list|(
name|soqlQuery
argument_list|)
decl_stmt|;
specifier|final
name|Request
name|get
init|=
name|getRequest
argument_list|(
name|HttpMethod
operator|.
name|GET
argument_list|,
name|versionUrl
argument_list|()
operator|+
literal|"query/?q="
operator|+
name|encodedQuery
argument_list|)
decl_stmt|;
comment|// requires authorization token
name|setAccessToken
argument_list|(
name|get
argument_list|)
expr_stmt|;
name|doHttpRequest
argument_list|(
name|get
argument_list|,
operator|new
name|DelegatingClientCallback
argument_list|(
name|callback
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|e
parameter_list|)
block|{
name|String
name|msg
init|=
literal|"Unexpected error: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
decl_stmt|;
name|callback
operator|.
name|onResponse
argument_list|(
literal|null
argument_list|,
operator|new
name|SalesforceException
argument_list|(
name|msg
argument_list|,
name|e
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|queryMore (String nextRecordsUrl, ResponseCallback callback)
specifier|public
name|void
name|queryMore
parameter_list|(
name|String
name|nextRecordsUrl
parameter_list|,
name|ResponseCallback
name|callback
parameter_list|)
block|{
specifier|final
name|Request
name|get
init|=
name|getRequest
argument_list|(
name|HttpMethod
operator|.
name|GET
argument_list|,
name|instanceUrl
operator|+
name|nextRecordsUrl
argument_list|)
decl_stmt|;
comment|// requires authorization token
name|setAccessToken
argument_list|(
name|get
argument_list|)
expr_stmt|;
name|doHttpRequest
argument_list|(
name|get
argument_list|,
operator|new
name|DelegatingClientCallback
argument_list|(
name|callback
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|search (String soslQuery, ResponseCallback callback)
specifier|public
name|void
name|search
parameter_list|(
name|String
name|soslQuery
parameter_list|,
name|ResponseCallback
name|callback
parameter_list|)
block|{
try|try
block|{
name|String
name|encodedQuery
init|=
name|urlEncode
argument_list|(
name|soslQuery
argument_list|)
decl_stmt|;
specifier|final
name|Request
name|get
init|=
name|getRequest
argument_list|(
name|HttpMethod
operator|.
name|GET
argument_list|,
name|versionUrl
argument_list|()
operator|+
literal|"search/?q="
operator|+
name|encodedQuery
argument_list|)
decl_stmt|;
comment|// requires authorization token
name|setAccessToken
argument_list|(
name|get
argument_list|)
expr_stmt|;
name|doHttpRequest
argument_list|(
name|get
argument_list|,
operator|new
name|DelegatingClientCallback
argument_list|(
name|callback
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|e
parameter_list|)
block|{
name|String
name|msg
init|=
literal|"Unexpected error: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
decl_stmt|;
name|callback
operator|.
name|onResponse
argument_list|(
literal|null
argument_list|,
operator|new
name|SalesforceException
argument_list|(
name|msg
argument_list|,
name|e
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|apexCall (String httpMethod, String apexUrl, Map<String, Object> queryParams, InputStream requestDto, ResponseCallback callback)
specifier|public
name|void
name|apexCall
parameter_list|(
name|String
name|httpMethod
parameter_list|,
name|String
name|apexUrl
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|queryParams
parameter_list|,
name|InputStream
name|requestDto
parameter_list|,
name|ResponseCallback
name|callback
parameter_list|)
block|{
comment|// create APEX call request
specifier|final
name|Request
name|request
decl_stmt|;
try|try
block|{
name|request
operator|=
name|getRequest
argument_list|(
name|httpMethod
argument_list|,
name|apexCallUrl
argument_list|(
name|apexUrl
argument_list|,
name|queryParams
argument_list|)
argument_list|)
expr_stmt|;
comment|// set request SObject and content type
if|if
condition|(
name|requestDto
operator|!=
literal|null
condition|)
block|{
name|request
operator|.
name|content
argument_list|(
operator|new
name|InputStreamContentProvider
argument_list|(
name|requestDto
argument_list|)
argument_list|)
expr_stmt|;
name|request
operator|.
name|header
argument_list|(
name|HttpHeader
operator|.
name|CONTENT_TYPE
argument_list|,
name|PayloadFormat
operator|.
name|JSON
operator|.
name|equals
argument_list|(
name|format
argument_list|)
condition|?
name|APPLICATION_JSON_UTF8
else|:
name|APPLICATION_XML_UTF8
argument_list|)
expr_stmt|;
block|}
comment|// requires authorization token
name|setAccessToken
argument_list|(
name|request
argument_list|)
expr_stmt|;
name|doHttpRequest
argument_list|(
name|request
argument_list|,
operator|new
name|DelegatingClientCallback
argument_list|(
name|callback
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|e
parameter_list|)
block|{
name|String
name|msg
init|=
literal|"Unexpected error: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
decl_stmt|;
name|callback
operator|.
name|onResponse
argument_list|(
literal|null
argument_list|,
operator|new
name|SalesforceException
argument_list|(
name|msg
argument_list|,
name|e
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
name|String
name|msg
init|=
literal|"Unexpected error: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
decl_stmt|;
name|callback
operator|.
name|onResponse
argument_list|(
literal|null
argument_list|,
operator|new
name|SalesforceException
argument_list|(
name|msg
argument_list|,
name|e
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|apexCallUrl (String apexUrl, Map<String, Object> queryParams)
specifier|private
name|String
name|apexCallUrl
parameter_list|(
name|String
name|apexUrl
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|queryParams
parameter_list|)
throws|throws
name|UnsupportedEncodingException
throws|,
name|URISyntaxException
block|{
if|if
condition|(
name|queryParams
operator|!=
literal|null
operator|&&
operator|!
name|queryParams
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|apexUrl
operator|=
name|URISupport
operator|.
name|appendParametersToURI
argument_list|(
name|apexUrl
argument_list|,
name|queryParams
argument_list|)
expr_stmt|;
block|}
return|return
name|instanceUrl
operator|+
name|SERVICES_APEXREST
operator|+
name|apexUrl
return|;
block|}
DECL|method|servicesDataUrl ()
specifier|private
name|String
name|servicesDataUrl
parameter_list|()
block|{
return|return
name|instanceUrl
operator|+
name|SERVICES_DATA
return|;
block|}
DECL|method|versionUrl ()
specifier|private
name|String
name|versionUrl
parameter_list|()
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|version
argument_list|,
literal|"version"
argument_list|)
expr_stmt|;
return|return
name|servicesDataUrl
argument_list|()
operator|+
literal|"v"
operator|+
name|version
operator|+
literal|"/"
return|;
block|}
DECL|method|sobjectsUrl (String sObjectName)
specifier|private
name|String
name|sobjectsUrl
parameter_list|(
name|String
name|sObjectName
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|sObjectName
argument_list|,
literal|"sObjectName"
argument_list|)
expr_stmt|;
return|return
name|versionUrl
argument_list|()
operator|+
literal|"sobjects/"
operator|+
name|sObjectName
return|;
block|}
DECL|method|sobjectsExternalIdUrl (String sObjectName, String fieldName, String fieldValue)
specifier|private
name|String
name|sobjectsExternalIdUrl
parameter_list|(
name|String
name|sObjectName
parameter_list|,
name|String
name|fieldName
parameter_list|,
name|String
name|fieldValue
parameter_list|)
block|{
if|if
condition|(
name|fieldName
operator|==
literal|null
operator|||
name|fieldValue
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"External field name and value cannot be NULL"
argument_list|)
throw|;
block|}
try|try
block|{
name|String
name|encodedValue
init|=
name|urlEncode
argument_list|(
name|fieldValue
argument_list|)
decl_stmt|;
return|return
name|sobjectsUrl
argument_list|(
name|sObjectName
operator|+
literal|"/"
operator|+
name|fieldName
operator|+
literal|"/"
operator|+
name|encodedValue
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|e
parameter_list|)
block|{
name|String
name|msg
init|=
literal|"Unexpected error: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
decl_stmt|;
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|msg
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|setAccessToken (Request request)
specifier|protected
name|void
name|setAccessToken
parameter_list|(
name|Request
name|request
parameter_list|)
block|{
comment|// replace old token
name|request
operator|.
name|getHeaders
argument_list|()
operator|.
name|put
argument_list|(
name|TOKEN_HEADER
argument_list|,
name|TOKEN_PREFIX
operator|+
name|accessToken
argument_list|)
expr_stmt|;
block|}
DECL|method|urlEncode (String query)
specifier|private
name|String
name|urlEncode
parameter_list|(
name|String
name|query
parameter_list|)
throws|throws
name|UnsupportedEncodingException
block|{
name|String
name|encodedQuery
init|=
name|URLEncoder
operator|.
name|encode
argument_list|(
name|query
argument_list|,
name|StringUtil
operator|.
name|__UTF8
argument_list|)
decl_stmt|;
comment|// URLEncoder likes to use '+' for spaces
name|encodedQuery
operator|=
name|encodedQuery
operator|.
name|replace
argument_list|(
literal|"+"
argument_list|,
literal|"%20"
argument_list|)
expr_stmt|;
return|return
name|encodedQuery
return|;
block|}
DECL|class|DelegatingClientCallback
specifier|private
specifier|static
class|class
name|DelegatingClientCallback
implements|implements
name|ClientResponseCallback
block|{
DECL|field|callback
specifier|private
specifier|final
name|ResponseCallback
name|callback
decl_stmt|;
DECL|method|DelegatingClientCallback (ResponseCallback callback)
specifier|public
name|DelegatingClientCallback
parameter_list|(
name|ResponseCallback
name|callback
parameter_list|)
block|{
name|this
operator|.
name|callback
operator|=
name|callback
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onResponse (InputStream response, SalesforceException ex)
specifier|public
name|void
name|onResponse
parameter_list|(
name|InputStream
name|response
parameter_list|,
name|SalesforceException
name|ex
parameter_list|)
block|{
name|callback
operator|.
name|onResponse
argument_list|(
name|response
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

