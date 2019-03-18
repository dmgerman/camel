begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce.internal.processor
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
name|processor
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

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
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
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
name|fasterxml
operator|.
name|jackson
operator|.
name|core
operator|.
name|type
operator|.
name|TypeReference
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|databind
operator|.
name|ObjectMapper
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
name|AsyncCallback
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
name|component
operator|.
name|salesforce
operator|.
name|SalesforceEndpoint
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
name|SalesforceEndpointConfig
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
name|TypeReferences
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
name|AbstractDTOBase
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
name|CreateSObjectResult
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
name|GlobalObjects
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
name|Limits
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
name|RestResources
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
name|SObjectBasicInfo
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
name|SObjectDescription
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
name|approval
operator|.
name|ApprovalResult
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
name|approval
operator|.
name|Approvals
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
name|utils
operator|.
name|JsonUtils
import|;
end_import

begin_class
DECL|class|JsonRestProcessor
specifier|public
class|class
name|JsonRestProcessor
extends|extends
name|AbstractRestProcessor
block|{
DECL|field|RESPONSE_TYPE
specifier|private
specifier|static
specifier|final
name|String
name|RESPONSE_TYPE
init|=
name|JsonRestProcessor
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".responseType"
decl_stmt|;
comment|// it is ok to use a single thread safe ObjectMapper
DECL|field|objectMapper
specifier|private
specifier|final
name|ObjectMapper
name|objectMapper
decl_stmt|;
DECL|method|JsonRestProcessor (SalesforceEndpoint endpoint)
specifier|public
name|JsonRestProcessor
parameter_list|(
name|SalesforceEndpoint
name|endpoint
parameter_list|)
throws|throws
name|SalesforceException
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getObjectMapper
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|objectMapper
operator|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getObjectMapper
argument_list|()
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isSerializeNulls
argument_list|()
condition|)
block|{
name|this
operator|.
name|objectMapper
operator|=
name|JsonUtils
operator|.
name|withNullSerialization
argument_list|(
name|JsonUtils
operator|.
name|createObjectMapper
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|objectMapper
operator|=
name|JsonUtils
operator|.
name|createObjectMapper
argument_list|()
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|processRequest (Exchange exchange)
specifier|protected
name|void
name|processRequest
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|SalesforceException
block|{
switch|switch
condition|(
name|operationName
condition|)
block|{
case|case
name|GET_VERSIONS
case|:
comment|// handle in built response types
name|exchange
operator|.
name|setProperty
argument_list|(
name|RESPONSE_TYPE
argument_list|,
name|TypeReferences
operator|.
name|VERSION_LIST_TYPE
argument_list|)
expr_stmt|;
break|break;
case|case
name|GET_RESOURCES
case|:
comment|// handle in built response types
name|exchange
operator|.
name|setProperty
argument_list|(
name|RESPONSE_CLASS
argument_list|,
name|RestResources
operator|.
name|class
argument_list|)
expr_stmt|;
break|break;
case|case
name|GET_GLOBAL_OBJECTS
case|:
comment|// handle in built response types
name|exchange
operator|.
name|setProperty
argument_list|(
name|RESPONSE_CLASS
argument_list|,
name|GlobalObjects
operator|.
name|class
argument_list|)
expr_stmt|;
break|break;
case|case
name|GET_BASIC_INFO
case|:
comment|// handle in built response types
name|exchange
operator|.
name|setProperty
argument_list|(
name|RESPONSE_CLASS
argument_list|,
name|SObjectBasicInfo
operator|.
name|class
argument_list|)
expr_stmt|;
break|break;
case|case
name|GET_DESCRIPTION
case|:
comment|// handle in built response types
name|exchange
operator|.
name|setProperty
argument_list|(
name|RESPONSE_CLASS
argument_list|,
name|SObjectDescription
operator|.
name|class
argument_list|)
expr_stmt|;
break|break;
case|case
name|CREATE_SOBJECT
case|:
comment|// handle known response type
name|exchange
operator|.
name|setProperty
argument_list|(
name|RESPONSE_CLASS
argument_list|,
name|CreateSObjectResult
operator|.
name|class
argument_list|)
expr_stmt|;
break|break;
case|case
name|UPSERT_SOBJECT
case|:
comment|// handle known response type
name|exchange
operator|.
name|setProperty
argument_list|(
name|RESPONSE_CLASS
argument_list|,
name|CreateSObjectResult
operator|.
name|class
argument_list|)
expr_stmt|;
break|break;
case|case
name|SEARCH
case|:
comment|// handle known response type
name|exchange
operator|.
name|setProperty
argument_list|(
name|RESPONSE_TYPE
argument_list|,
name|TypeReferences
operator|.
name|SEARCH_RESULT_TYPE
argument_list|)
expr_stmt|;
break|break;
case|case
name|RECENT
case|:
comment|// handle known response type
name|exchange
operator|.
name|setProperty
argument_list|(
name|RESPONSE_TYPE
argument_list|,
name|TypeReferences
operator|.
name|RECENT_ITEM_LIST_TYPE
argument_list|)
expr_stmt|;
break|break;
case|case
name|LIMITS
case|:
comment|// handle known response type
name|exchange
operator|.
name|setProperty
argument_list|(
name|RESPONSE_CLASS
argument_list|,
name|Limits
operator|.
name|class
argument_list|)
expr_stmt|;
break|break;
case|case
name|APPROVAL
case|:
comment|// handle known response type
name|exchange
operator|.
name|setProperty
argument_list|(
name|RESPONSE_CLASS
argument_list|,
name|ApprovalResult
operator|.
name|class
argument_list|)
expr_stmt|;
break|break;
case|case
name|APPROVALS
case|:
comment|// handle known response type
name|exchange
operator|.
name|setProperty
argument_list|(
name|RESPONSE_CLASS
argument_list|,
name|Approvals
operator|.
name|class
argument_list|)
expr_stmt|;
break|break;
default|default:
comment|// ignore, some operations do not require response class or type
block|}
block|}
annotation|@
name|Override
DECL|method|getRequestStream (Exchange exchange)
specifier|protected
name|InputStream
name|getRequestStream
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|SalesforceException
block|{
name|InputStream
name|request
decl_stmt|;
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|request
operator|=
name|in
operator|.
name|getBody
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
expr_stmt|;
if|if
condition|(
name|request
operator|==
literal|null
condition|)
block|{
name|AbstractDTOBase
name|dto
init|=
name|in
operator|.
name|getBody
argument_list|(
name|AbstractDTOBase
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|dto
operator|!=
literal|null
condition|)
block|{
comment|// marshall the DTO
name|request
operator|=
name|getRequestStream
argument_list|(
name|in
argument_list|,
name|dto
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// if all else fails, get body as String
specifier|final
name|String
name|body
init|=
name|in
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|==
name|body
condition|)
block|{
name|String
name|msg
init|=
literal|"Unsupported request message body "
operator|+
operator|(
name|in
operator|.
name|getBody
argument_list|()
operator|==
literal|null
condition|?
literal|null
else|:
name|in
operator|.
name|getBody
argument_list|()
operator|.
name|getClass
argument_list|()
operator|)
decl_stmt|;
throw|throw
operator|new
name|SalesforceException
argument_list|(
name|msg
argument_list|,
literal|null
argument_list|)
throw|;
block|}
else|else
block|{
name|request
operator|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|body
operator|.
name|getBytes
argument_list|(
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|request
return|;
block|}
annotation|@
name|Override
DECL|method|getRequestStream (final Message in, final Object object)
specifier|protected
name|InputStream
name|getRequestStream
parameter_list|(
specifier|final
name|Message
name|in
parameter_list|,
specifier|final
name|Object
name|object
parameter_list|)
throws|throws
name|SalesforceException
block|{
specifier|final
name|ByteArrayOutputStream
name|out
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
try|try
block|{
name|prepareMapper
argument_list|(
name|in
argument_list|)
operator|.
name|writeValue
argument_list|(
name|out
argument_list|,
name|object
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
specifier|final
name|String
name|msg
init|=
literal|"Error marshaling request: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
decl_stmt|;
throw|throw
operator|new
name|SalesforceException
argument_list|(
name|msg
argument_list|,
name|e
argument_list|)
throw|;
block|}
return|return
operator|new
name|ByteArrayInputStream
argument_list|(
name|out
operator|.
name|toByteArray
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|processResponse (Exchange exchange, InputStream responseEntity, Map<String, String> headers, SalesforceException ex, AsyncCallback callback)
specifier|protected
name|void
name|processResponse
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|InputStream
name|responseEntity
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
parameter_list|,
name|SalesforceException
name|ex
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
block|{
comment|// process JSON response for TypeReference
try|try
block|{
specifier|final
name|Message
name|out
init|=
name|exchange
operator|.
name|getOut
argument_list|()
decl_stmt|;
specifier|final
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|out
operator|.
name|copyFromWithNewBody
argument_list|(
name|in
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|out
operator|.
name|getHeaders
argument_list|()
operator|.
name|putAll
argument_list|(
name|headers
argument_list|)
expr_stmt|;
if|if
condition|(
name|ex
operator|!=
literal|null
condition|)
block|{
comment|// if an exception is reported we should not loose it
if|if
condition|(
name|shouldReport
argument_list|(
name|ex
argument_list|)
condition|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|responseEntity
operator|!=
literal|null
condition|)
block|{
comment|// do we need to un-marshal a response
specifier|final
name|Object
name|response
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|responseClass
init|=
name|exchange
operator|.
name|getProperty
argument_list|(
name|RESPONSE_CLASS
argument_list|,
name|Class
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|rawPayload
operator|&&
name|responseClass
operator|!=
literal|null
condition|)
block|{
name|response
operator|=
name|prepareMapper
argument_list|(
name|in
argument_list|)
operator|.
name|readValue
argument_list|(
name|responseEntity
argument_list|,
name|responseClass
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|TypeReference
argument_list|<
name|?
argument_list|>
name|responseType
init|=
name|exchange
operator|.
name|getProperty
argument_list|(
name|RESPONSE_TYPE
argument_list|,
name|TypeReference
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|rawPayload
operator|&&
name|responseType
operator|!=
literal|null
condition|)
block|{
name|response
operator|=
name|prepareMapper
argument_list|(
name|in
argument_list|)
operator|.
name|readValue
argument_list|(
name|responseEntity
argument_list|,
name|responseType
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// return the response as a stream, for getBlobField
name|response
operator|=
name|responseEntity
expr_stmt|;
block|}
block|}
name|out
operator|.
name|setBody
argument_list|(
name|response
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|String
name|msg
init|=
literal|"Error parsing JSON response: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|setException
argument_list|(
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
finally|finally
block|{
comment|// cleanup temporary exchange headers
name|exchange
operator|.
name|removeProperty
argument_list|(
name|RESPONSE_CLASS
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|removeProperty
argument_list|(
name|RESPONSE_TYPE
argument_list|)
expr_stmt|;
comment|// consume response entity
try|try
block|{
if|if
condition|(
name|responseEntity
operator|!=
literal|null
condition|)
block|{
name|responseEntity
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|ignored
parameter_list|)
block|{             }
comment|// notify callback that exchange is done
name|callback
operator|.
name|done
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|prepareMapper (final Message in)
specifier|private
name|ObjectMapper
name|prepareMapper
parameter_list|(
specifier|final
name|Message
name|in
parameter_list|)
block|{
specifier|final
name|Object
name|serializeNulls
init|=
name|in
operator|.
name|getHeader
argument_list|(
name|SalesforceEndpointConfig
operator|.
name|SERIALIZE_NULLS
argument_list|)
decl_stmt|;
if|if
condition|(
name|Boolean
operator|.
name|TRUE
operator|.
name|equals
argument_list|(
name|serializeNulls
argument_list|)
condition|)
block|{
return|return
name|JsonUtils
operator|.
name|withNullSerialization
argument_list|(
name|objectMapper
argument_list|)
return|;
block|}
return|return
name|objectMapper
return|;
block|}
block|}
end_class

end_unit

