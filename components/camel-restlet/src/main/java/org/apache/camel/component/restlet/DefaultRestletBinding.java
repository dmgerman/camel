begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.restlet
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|restlet
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
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
name|PrintWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Calendar
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
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
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|dom
operator|.
name|DOMSource
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
name|Exchange
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
name|Message
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
name|NoTypeConversionAvailableException
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
name|StringSource
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
name|WrappedFile
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
name|file
operator|.
name|GenericFile
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
name|spi
operator|.
name|HeaderFilterStrategy
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
name|spi
operator|.
name|HeaderFilterStrategyAware
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
name|MessageHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|restlet
operator|.
name|Request
import|;
end_import

begin_import
import|import
name|org
operator|.
name|restlet
operator|.
name|Response
import|;
end_import

begin_import
import|import
name|org
operator|.
name|restlet
operator|.
name|data
operator|.
name|ChallengeResponse
import|;
end_import

begin_import
import|import
name|org
operator|.
name|restlet
operator|.
name|data
operator|.
name|ChallengeScheme
import|;
end_import

begin_import
import|import
name|org
operator|.
name|restlet
operator|.
name|data
operator|.
name|CharacterSet
import|;
end_import

begin_import
import|import
name|org
operator|.
name|restlet
operator|.
name|data
operator|.
name|Form
import|;
end_import

begin_import
import|import
name|org
operator|.
name|restlet
operator|.
name|data
operator|.
name|MediaType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|restlet
operator|.
name|data
operator|.
name|Method
import|;
end_import

begin_import
import|import
name|org
operator|.
name|restlet
operator|.
name|data
operator|.
name|Preference
import|;
end_import

begin_import
import|import
name|org
operator|.
name|restlet
operator|.
name|data
operator|.
name|Status
import|;
end_import

begin_import
import|import
name|org
operator|.
name|restlet
operator|.
name|engine
operator|.
name|http
operator|.
name|header
operator|.
name|HeaderConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|restlet
operator|.
name|representation
operator|.
name|FileRepresentation
import|;
end_import

begin_import
import|import
name|org
operator|.
name|restlet
operator|.
name|representation
operator|.
name|InputRepresentation
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * Default Restlet binding implementation  */
end_comment

begin_class
DECL|class|DefaultRestletBinding
specifier|public
class|class
name|DefaultRestletBinding
implements|implements
name|RestletBinding
implements|,
name|HeaderFilterStrategyAware
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|DefaultRestletBinding
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|headerFilterStrategy
specifier|private
name|HeaderFilterStrategy
name|headerFilterStrategy
decl_stmt|;
DECL|method|populateExchangeFromRestletRequest (Request request, Response response, Exchange exchange)
specifier|public
name|void
name|populateExchangeFromRestletRequest
parameter_list|(
name|Request
name|request
parameter_list|,
name|Response
name|response
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|Message
name|inMessage
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|inMessage
operator|.
name|setHeader
argument_list|(
name|RestletConstants
operator|.
name|RESTLET_REQUEST
argument_list|,
name|request
argument_list|)
expr_stmt|;
name|inMessage
operator|.
name|setHeader
argument_list|(
name|RestletConstants
operator|.
name|RESTLET_RESPONSE
argument_list|,
name|response
argument_list|)
expr_stmt|;
comment|// extract headers from restlet
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|request
operator|.
name|getAttributes
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|headerFilterStrategy
operator|.
name|applyFilterToExternalHeaders
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|,
name|exchange
argument_list|)
condition|)
block|{
name|inMessage
operator|.
name|setHeader
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Populate exchange from Restlet request header: {} value: {}"
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|// copy query string to header
name|String
name|query
init|=
name|request
operator|.
name|getResourceRef
argument_list|()
operator|.
name|getQuery
argument_list|()
decl_stmt|;
if|if
condition|(
name|query
operator|!=
literal|null
condition|)
block|{
name|inMessage
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_QUERY
argument_list|,
name|query
argument_list|)
expr_stmt|;
block|}
comment|// copy URI to header
name|inMessage
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_URI
argument_list|,
name|request
operator|.
name|getResourceRef
argument_list|()
operator|.
name|getIdentifier
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
comment|// copy HTTP method to header
name|inMessage
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_METHOD
argument_list|,
name|request
operator|.
name|getMethod
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|request
operator|.
name|isEntityAvailable
argument_list|()
condition|)
block|{
return|return;
block|}
comment|// only deal with the form if the content type is "application/x-www-form-urlencoded"
if|if
condition|(
name|request
operator|.
name|getEntity
argument_list|()
operator|.
name|getMediaType
argument_list|()
operator|!=
literal|null
operator|&&
name|request
operator|.
name|getEntity
argument_list|()
operator|.
name|getMediaType
argument_list|()
operator|.
name|equals
argument_list|(
name|MediaType
operator|.
name|APPLICATION_WWW_FORM
argument_list|)
condition|)
block|{
name|Form
name|form
init|=
operator|new
name|Form
argument_list|(
name|request
operator|.
name|getEntity
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|form
operator|.
name|getValuesMap
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|entry
operator|.
name|getValue
argument_list|()
operator|==
literal|null
condition|)
block|{
name|inMessage
operator|.
name|setBody
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Populate exchange from Restlet request body: {}"
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
operator|!
name|headerFilterStrategy
operator|.
name|applyFilterToExternalHeaders
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|,
name|exchange
argument_list|)
condition|)
block|{
name|inMessage
operator|.
name|setHeader
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Populate exchange from Restlet request user header: {} value: {}"
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
else|else
block|{
name|inMessage
operator|.
name|setBody
argument_list|(
name|request
operator|.
name|getEntity
argument_list|()
operator|.
name|getStream
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|populateRestletRequestFromExchange (Request request, Exchange exchange)
specifier|public
name|void
name|populateRestletRequestFromExchange
parameter_list|(
name|Request
name|request
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|request
operator|.
name|setReferrerRef
argument_list|(
literal|"camel-restlet"
argument_list|)
expr_stmt|;
name|String
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|Form
name|form
init|=
operator|new
name|Form
argument_list|()
decl_stmt|;
comment|// add the body as the key in the form with null value
name|form
operator|.
name|add
argument_list|(
name|body
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|MediaType
name|mediaType
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
name|MediaType
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|mediaType
operator|==
literal|null
condition|)
block|{
name|mediaType
operator|=
name|MediaType
operator|.
name|APPLICATION_WWW_FORM
expr_stmt|;
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"Populate Restlet request from exchange body: {} using media type {}"
argument_list|,
name|body
argument_list|,
name|mediaType
argument_list|)
expr_stmt|;
comment|// login and password are filtered by header filter strategy
name|String
name|login
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|RestletConstants
operator|.
name|RESTLET_LOGIN
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|password
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|RestletConstants
operator|.
name|RESTLET_PASSWORD
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|login
operator|!=
literal|null
operator|&&
name|password
operator|!=
literal|null
condition|)
block|{
name|ChallengeResponse
name|authentication
init|=
operator|new
name|ChallengeResponse
argument_list|(
name|ChallengeScheme
operator|.
name|HTTP_BASIC
argument_list|,
name|login
argument_list|,
name|password
argument_list|)
decl_stmt|;
name|request
operator|.
name|setChallengeResponse
argument_list|(
name|authentication
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Basic HTTP Authentication has been applied"
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|headerFilterStrategy
operator|.
name|applyFilterToCamelHeaders
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|,
name|exchange
argument_list|)
condition|)
block|{
comment|// Use forms only for GET and POST/x-www-form-urlencoded
if|if
condition|(
name|request
operator|.
name|getMethod
argument_list|()
operator|==
name|Method
operator|.
name|GET
operator|||
operator|(
name|request
operator|.
name|getMethod
argument_list|()
operator|==
name|Method
operator|.
name|POST
operator|&&
name|mediaType
operator|==
name|MediaType
operator|.
name|APPLICATION_WWW_FORM
operator|)
condition|)
block|{
if|if
condition|(
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"org.restlet."
argument_list|)
condition|)
block|{
comment|// put the org.restlet headers in attributes
name|request
operator|.
name|getAttributes
argument_list|()
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// put the user stuff in the form
name|form
operator|.
name|add
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// For non-form post put all the headers in attributes
name|request
operator|.
name|getAttributes
argument_list|()
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"Populate Restlet request from exchange header: {} value: {}"
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"Using Content Type: {} for POST data: {}"
argument_list|,
name|mediaType
argument_list|,
name|body
argument_list|)
expr_stmt|;
comment|// Only URL Encode for GET and form POST
if|if
condition|(
name|request
operator|.
name|getMethod
argument_list|()
operator|==
name|Method
operator|.
name|GET
operator|||
operator|(
name|request
operator|.
name|getMethod
argument_list|()
operator|==
name|Method
operator|.
name|POST
operator|&&
name|mediaType
operator|==
name|MediaType
operator|.
name|APPLICATION_WWW_FORM
operator|)
condition|)
block|{
name|request
operator|.
name|setEntity
argument_list|(
name|form
operator|.
name|getWebRepresentation
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|request
operator|.
name|setEntity
argument_list|(
name|body
argument_list|,
name|mediaType
argument_list|)
expr_stmt|;
block|}
name|MediaType
name|acceptedMediaType
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|ACCEPT_CONTENT_TYPE
argument_list|,
name|MediaType
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|acceptedMediaType
operator|!=
literal|null
condition|)
block|{
name|request
operator|.
name|getClientInfo
argument_list|()
operator|.
name|getAcceptedMediaTypes
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|Preference
argument_list|<
name|MediaType
argument_list|>
argument_list|(
name|acceptedMediaType
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|populateRestletResponseFromExchange (Exchange exchange, Response response)
specifier|public
name|void
name|populateRestletResponseFromExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Response
name|response
parameter_list|)
throws|throws
name|Exception
block|{
name|Message
name|out
decl_stmt|;
if|if
condition|(
name|exchange
operator|.
name|isFailed
argument_list|()
condition|)
block|{
comment|// 500 for internal server error which can be overridden by response code in header
name|response
operator|.
name|setStatus
argument_list|(
name|Status
operator|.
name|valueOf
argument_list|(
literal|500
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|exchange
operator|.
name|hasOut
argument_list|()
operator|&&
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|isFault
argument_list|()
condition|)
block|{
name|out
operator|=
name|exchange
operator|.
name|getOut
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// print exception as message and stacktrace
name|Exception
name|t
init|=
name|exchange
operator|.
name|getException
argument_list|()
decl_stmt|;
name|StringWriter
name|sw
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|PrintWriter
name|pw
init|=
operator|new
name|PrintWriter
argument_list|(
name|sw
argument_list|)
decl_stmt|;
name|t
operator|.
name|printStackTrace
argument_list|(
name|pw
argument_list|)
expr_stmt|;
name|response
operator|.
name|setEntity
argument_list|(
name|sw
operator|.
name|toString
argument_list|()
argument_list|,
name|MediaType
operator|.
name|TEXT_PLAIN
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
else|else
block|{
name|out
operator|=
name|exchange
operator|.
name|getOut
argument_list|()
expr_stmt|;
block|}
comment|// get content type
name|MediaType
name|mediaType
init|=
name|out
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
name|MediaType
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|mediaType
operator|==
literal|null
condition|)
block|{
name|Object
name|body
init|=
name|out
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|mediaType
operator|=
name|MediaType
operator|.
name|TEXT_PLAIN
expr_stmt|;
if|if
condition|(
name|body
operator|instanceof
name|String
condition|)
block|{
name|mediaType
operator|=
name|MediaType
operator|.
name|TEXT_PLAIN
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|body
operator|instanceof
name|StringSource
operator|||
name|body
operator|instanceof
name|DOMSource
condition|)
block|{
name|mediaType
operator|=
name|MediaType
operator|.
name|TEXT_XML
expr_stmt|;
block|}
block|}
comment|// get response code
name|Integer
name|responseCode
init|=
name|out
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_RESPONSE_CODE
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|responseCode
operator|!=
literal|null
condition|)
block|{
name|response
operator|.
name|setStatus
argument_list|(
name|Status
operator|.
name|valueOf
argument_list|(
name|responseCode
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// set response body according to the message body
name|Object
name|body
init|=
name|out
operator|.
name|getBody
argument_list|()
decl_stmt|;
if|if
condition|(
name|body
operator|instanceof
name|WrappedFile
condition|)
block|{
comment|// grab body from generic file holder
name|GenericFile
argument_list|<
name|?
argument_list|>
name|gf
init|=
operator|(
name|GenericFile
argument_list|<
name|?
argument_list|>
operator|)
name|body
decl_stmt|;
name|body
operator|=
name|gf
operator|.
name|getBody
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|body
operator|==
literal|null
condition|)
block|{
comment|// empty response
name|response
operator|.
name|setEntity
argument_list|(
literal|""
argument_list|,
name|MediaType
operator|.
name|TEXT_PLAIN
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|body
operator|instanceof
name|Response
condition|)
block|{
comment|// its already a restlet response, so dont do anything
name|LOG
operator|.
name|debug
argument_list|(
literal|"Using existing Restlet Response from exchange body: {}"
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|body
operator|instanceof
name|InputStream
condition|)
block|{
name|response
operator|.
name|setEntity
argument_list|(
operator|new
name|InputRepresentation
argument_list|(
name|out
operator|.
name|getBody
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
argument_list|,
name|mediaType
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|body
operator|instanceof
name|File
condition|)
block|{
name|response
operator|.
name|setEntity
argument_list|(
operator|new
name|FileRepresentation
argument_list|(
name|out
operator|.
name|getBody
argument_list|(
name|File
operator|.
name|class
argument_list|)
argument_list|,
name|mediaType
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// fallback and use string
name|String
name|text
init|=
name|out
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|response
operator|.
name|setEntity
argument_list|(
name|text
argument_list|,
name|mediaType
argument_list|)
expr_stmt|;
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"Populate Restlet response from exchange body: {}"
argument_list|,
name|body
argument_list|)
expr_stmt|;
if|if
condition|(
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|CHARSET_NAME
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|CharacterSet
name|cs
init|=
name|CharacterSet
operator|.
name|valueOf
argument_list|(
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|CHARSET_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|response
operator|.
name|getEntity
argument_list|()
operator|.
name|setCharacterSet
argument_list|(
name|cs
argument_list|)
expr_stmt|;
block|}
comment|// set headers at the end, as the entity must be set first
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|out
operator|.
name|getHeaders
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|headerFilterStrategy
operator|.
name|applyFilterToCamelHeaders
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|,
name|exchange
argument_list|)
condition|)
block|{
name|setResponseHeader
argument_list|(
name|exchange
argument_list|,
name|response
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Populate Restlet HTTP header in response from exchange header: {} value: {}"
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|populateExchangeFromRestletResponse (Exchange exchange, Response response)
specifier|public
name|void
name|populateExchangeFromRestletResponse
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Response
name|response
parameter_list|)
throws|throws
name|Exception
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|response
operator|.
name|getAttributes
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|headerFilterStrategy
operator|.
name|applyFilterToExternalHeaders
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|,
name|exchange
argument_list|)
condition|)
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Populate exchange from Restlet response header: {} value: {}"
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|// set response code
name|int
name|responseCode
init|=
name|response
operator|.
name|getStatus
argument_list|()
operator|.
name|getCode
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_RESPONSE_CODE
argument_list|,
name|responseCode
argument_list|)
expr_stmt|;
comment|// set restlet response as header so end user have access to it if needed
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|RestletConstants
operator|.
name|RESTLET_RESPONSE
argument_list|,
name|response
argument_list|)
expr_stmt|;
if|if
condition|(
name|response
operator|.
name|getEntity
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// get content type
name|MediaType
name|mediaType
init|=
name|response
operator|.
name|getEntity
argument_list|()
operator|.
name|getMediaType
argument_list|()
decl_stmt|;
if|if
condition|(
name|mediaType
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
name|mediaType
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// get content text
name|String
name|text
init|=
name|response
operator|.
name|getEntity
argument_list|()
operator|.
name|getText
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Populate exchange from Restlet response: {}"
argument_list|,
name|text
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|text
argument_list|)
expr_stmt|;
block|}
comment|// preserve headers from in by copying any non existing headers
comment|// to avoid overriding existing headers with old values
name|MessageHelper
operator|.
name|copyHeaders
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|setResponseHeader (Exchange exchange, org.restlet.Message message, String header, Object value)
specifier|protected
name|void
name|setResponseHeader
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|org
operator|.
name|restlet
operator|.
name|Message
name|message
parameter_list|,
name|String
name|header
parameter_list|,
name|Object
name|value
parameter_list|)
throws|throws
name|NoTypeConversionAvailableException
block|{
comment|// put the header first
name|message
operator|.
name|getAttributes
argument_list|()
operator|.
name|put
argument_list|(
name|header
argument_list|,
name|value
argument_list|)
expr_stmt|;
comment|// special for certain headers
if|if
condition|(
name|message
operator|.
name|getEntity
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|header
operator|.
name|equalsIgnoreCase
argument_list|(
name|HeaderConstants
operator|.
name|HEADER_EXPIRES
argument_list|)
condition|)
block|{
if|if
condition|(
name|value
operator|instanceof
name|Calendar
condition|)
block|{
name|message
operator|.
name|getEntity
argument_list|()
operator|.
name|setExpirationDate
argument_list|(
operator|(
operator|(
name|Calendar
operator|)
name|value
operator|)
operator|.
name|getTime
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|value
operator|instanceof
name|Date
condition|)
block|{
name|message
operator|.
name|getEntity
argument_list|()
operator|.
name|setExpirationDate
argument_list|(
operator|(
name|Date
operator|)
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Date
name|date
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|mandatoryConvertTo
argument_list|(
name|Date
operator|.
name|class
argument_list|,
name|value
argument_list|)
decl_stmt|;
name|message
operator|.
name|getEntity
argument_list|()
operator|.
name|setExpirationDate
argument_list|(
name|date
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|header
operator|.
name|equalsIgnoreCase
argument_list|(
name|HeaderConstants
operator|.
name|HEADER_LAST_MODIFIED
argument_list|)
condition|)
block|{
if|if
condition|(
name|value
operator|instanceof
name|Calendar
condition|)
block|{
name|message
operator|.
name|getEntity
argument_list|()
operator|.
name|setModificationDate
argument_list|(
operator|(
operator|(
name|Calendar
operator|)
name|value
operator|)
operator|.
name|getTime
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|value
operator|instanceof
name|Date
condition|)
block|{
name|message
operator|.
name|getEntity
argument_list|()
operator|.
name|setModificationDate
argument_list|(
operator|(
name|Date
operator|)
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Date
name|date
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|mandatoryConvertTo
argument_list|(
name|Date
operator|.
name|class
argument_list|,
name|value
argument_list|)
decl_stmt|;
name|message
operator|.
name|getEntity
argument_list|()
operator|.
name|setModificationDate
argument_list|(
name|date
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|header
operator|.
name|equalsIgnoreCase
argument_list|(
name|HeaderConstants
operator|.
name|HEADER_CONTENT_LENGTH
argument_list|)
condition|)
block|{
if|if
condition|(
name|value
operator|instanceof
name|Long
condition|)
block|{
name|message
operator|.
name|getEntity
argument_list|()
operator|.
name|setSize
argument_list|(
operator|(
name|Long
operator|)
name|value
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|value
operator|instanceof
name|Integer
condition|)
block|{
name|message
operator|.
name|getEntity
argument_list|()
operator|.
name|setSize
argument_list|(
operator|(
name|Integer
operator|)
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Long
name|num
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|mandatoryConvertTo
argument_list|(
name|Long
operator|.
name|class
argument_list|,
name|value
argument_list|)
decl_stmt|;
name|message
operator|.
name|getEntity
argument_list|()
operator|.
name|setSize
argument_list|(
name|num
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|header
operator|.
name|equalsIgnoreCase
argument_list|(
name|HeaderConstants
operator|.
name|HEADER_CONTENT_TYPE
argument_list|)
condition|)
block|{
if|if
condition|(
name|value
operator|instanceof
name|MediaType
condition|)
block|{
name|message
operator|.
name|getEntity
argument_list|()
operator|.
name|setMediaType
argument_list|(
operator|(
name|MediaType
operator|)
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|String
name|type
init|=
name|value
operator|.
name|toString
argument_list|()
decl_stmt|;
name|MediaType
name|media
init|=
name|MediaType
operator|.
name|valueOf
argument_list|(
name|type
argument_list|)
decl_stmt|;
if|if
condition|(
name|media
operator|!=
literal|null
condition|)
block|{
name|message
operator|.
name|getEntity
argument_list|()
operator|.
name|setMediaType
argument_list|(
name|media
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Value {} cannot be converted as a MediaType. The value will be ignored."
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
DECL|method|getHeaderFilterStrategy ()
specifier|public
name|HeaderFilterStrategy
name|getHeaderFilterStrategy
parameter_list|()
block|{
return|return
name|headerFilterStrategy
return|;
block|}
DECL|method|setHeaderFilterStrategy (HeaderFilterStrategy strategy)
specifier|public
name|void
name|setHeaderFilterStrategy
parameter_list|(
name|HeaderFilterStrategy
name|strategy
parameter_list|)
block|{
name|headerFilterStrategy
operator|=
name|strategy
expr_stmt|;
block|}
block|}
end_class

end_unit

