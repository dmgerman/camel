begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.servicenow
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|servicenow
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
name|util
operator|.
name|ArrayList
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|HttpHeaders
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Response
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
name|JavaType
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
name|JsonNode
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
name|Processor
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
name|StringHelper
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

begin_class
DECL|class|AbstractServiceNowProcessor
specifier|public
specifier|abstract
class|class
name|AbstractServiceNowProcessor
implements|implements
name|Processor
block|{
DECL|field|endpoint
specifier|protected
specifier|final
name|ServiceNowEndpoint
name|endpoint
decl_stmt|;
DECL|field|config
specifier|protected
specifier|final
name|ServiceNowConfiguration
name|config
decl_stmt|;
DECL|field|mapper
specifier|protected
specifier|final
name|ObjectMapper
name|mapper
decl_stmt|;
DECL|field|client
specifier|protected
specifier|final
name|ServiceNowClient
name|client
decl_stmt|;
comment|// Cache for JavaTypes
DECL|field|javaTypeCache
specifier|private
specifier|final
name|JavaTypeCache
name|javaTypeCache
decl_stmt|;
DECL|field|dispatchers
specifier|private
specifier|final
name|List
argument_list|<
name|ServiceNowDispatcher
argument_list|>
name|dispatchers
decl_stmt|;
DECL|method|AbstractServiceNowProcessor (ServiceNowEndpoint endpoint)
specifier|protected
name|AbstractServiceNowProcessor
parameter_list|(
name|ServiceNowEndpoint
name|endpoint
parameter_list|)
throws|throws
name|Exception
block|{
name|this
operator|.
name|javaTypeCache
operator|=
operator|new
name|JavaTypeCache
argument_list|()
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|config
operator|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
expr_stmt|;
name|this
operator|.
name|mapper
operator|=
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|config
operator|.
name|getMapper
argument_list|()
argument_list|,
literal|"mapper"
argument_list|)
expr_stmt|;
name|this
operator|.
name|client
operator|=
operator|new
name|ServiceNowClient
argument_list|(
name|config
argument_list|)
expr_stmt|;
name|this
operator|.
name|dispatchers
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
DECL|method|setBodyAndHeaders (Message message, Class<?> model, Response response)
specifier|protected
name|AbstractServiceNowProcessor
name|setBodyAndHeaders
parameter_list|(
name|Message
name|message
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|model
parameter_list|,
name|Response
name|response
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|response
operator|!=
literal|null
condition|)
block|{
name|setHeaders
argument_list|(
name|message
argument_list|,
name|model
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|setBody
argument_list|(
name|message
argument_list|,
name|model
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
specifier|final
name|ServiceNowDispatcher
name|dispatcher
init|=
name|findDispatcher
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|dispatcher
operator|!=
literal|null
condition|)
block|{
name|dispatcher
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unable to process exchange"
argument_list|)
throw|;
block|}
block|}
comment|// *********************************
comment|// Header
comment|// *********************************
DECL|method|setHeaders (Message message, Class<?> model, Response response)
specifier|protected
name|AbstractServiceNowProcessor
name|setHeaders
parameter_list|(
name|Message
name|message
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|model
parameter_list|,
name|Response
name|response
parameter_list|)
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|String
argument_list|>
name|links
init|=
name|response
operator|.
name|getStringHeaders
argument_list|()
operator|.
name|get
argument_list|(
name|HttpHeaders
operator|.
name|LINK
argument_list|)
decl_stmt|;
if|if
condition|(
name|links
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|link
range|:
name|links
control|)
block|{
name|String
index|[]
name|parts
init|=
name|link
operator|.
name|split
argument_list|(
literal|";"
argument_list|)
decl_stmt|;
if|if
condition|(
name|parts
operator|.
name|length
operator|!=
literal|2
condition|)
block|{
continue|continue;
block|}
comment|// Sanitize parts
name|String
name|uri
init|=
name|ObjectHelper
operator|.
name|between
argument_list|(
name|parts
index|[
literal|0
index|]
argument_list|,
literal|"<"
argument_list|,
literal|">"
argument_list|)
decl_stmt|;
name|String
name|rel
init|=
name|StringHelper
operator|.
name|removeQuotes
argument_list|(
name|ObjectHelper
operator|.
name|after
argument_list|(
name|parts
index|[
literal|1
index|]
argument_list|,
literal|"="
argument_list|)
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|query
init|=
name|URISupport
operator|.
name|parseQuery
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|Object
name|offset
init|=
name|query
operator|.
name|get
argument_list|(
name|ServiceNowParams
operator|.
name|SYSPARM_OFFSET
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|offset
operator|!=
literal|null
condition|)
block|{
switch|switch
condition|(
name|rel
condition|)
block|{
case|case
name|ServiceNowConstants
operator|.
name|LINK_FIRST
case|:
name|message
operator|.
name|setHeader
argument_list|(
name|ServiceNowConstants
operator|.
name|OFFSET_FIRST
argument_list|,
name|offset
argument_list|)
expr_stmt|;
break|break;
case|case
name|ServiceNowConstants
operator|.
name|LINK_LAST
case|:
name|message
operator|.
name|setHeader
argument_list|(
name|ServiceNowConstants
operator|.
name|OFFSET_LAST
argument_list|,
name|offset
argument_list|)
expr_stmt|;
break|break;
case|case
name|ServiceNowConstants
operator|.
name|LINK_NEXT
case|:
name|message
operator|.
name|setHeader
argument_list|(
name|ServiceNowConstants
operator|.
name|OFFSET_NEXT
argument_list|,
name|offset
argument_list|)
expr_stmt|;
break|break;
case|case
name|ServiceNowConstants
operator|.
name|LINK_PREV
case|:
name|message
operator|.
name|setHeader
argument_list|(
name|ServiceNowConstants
operator|.
name|OFFSET_PREV
argument_list|,
name|offset
argument_list|)
expr_stmt|;
break|break;
default|default:
break|break;
block|}
block|}
block|}
block|}
name|String
name|attachmentMeta
init|=
name|response
operator|.
name|getHeaderString
argument_list|(
name|ServiceNowConstants
operator|.
name|ATTACHMENT_META_HEADER
argument_list|)
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|attachmentMeta
argument_list|)
condition|)
block|{
name|message
operator|.
name|setHeader
argument_list|(
name|ServiceNowConstants
operator|.
name|CONTENT_META
argument_list|,
name|mapper
operator|.
name|readValue
argument_list|(
name|attachmentMeta
argument_list|,
name|Map
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|copyHeader
argument_list|(
name|response
argument_list|,
name|HttpHeaders
operator|.
name|CONTENT_TYPE
argument_list|,
name|message
argument_list|,
name|ServiceNowConstants
operator|.
name|CONTENT_TYPE
argument_list|)
expr_stmt|;
name|copyHeader
argument_list|(
name|response
argument_list|,
name|HttpHeaders
operator|.
name|CONTENT_ENCODING
argument_list|,
name|message
argument_list|,
name|ServiceNowConstants
operator|.
name|CONTENT_ENCODING
argument_list|)
expr_stmt|;
if|if
condition|(
name|model
operator|!=
literal|null
condition|)
block|{
name|message
operator|.
name|getHeaders
argument_list|()
operator|.
name|putIfAbsent
argument_list|(
name|ServiceNowConstants
operator|.
name|MODEL
argument_list|,
name|model
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
comment|// *********************************
comment|// Body
comment|// *********************************
DECL|method|setBody (Message message, Class<?> model, Response response)
specifier|protected
name|AbstractServiceNowProcessor
name|setBody
parameter_list|(
name|Message
name|message
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|model
parameter_list|,
name|Response
name|response
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|message
operator|!=
literal|null
operator|&&
name|response
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|response
operator|.
name|getHeaderString
argument_list|(
name|HttpHeaders
operator|.
name|CONTENT_TYPE
argument_list|)
argument_list|)
condition|)
block|{
name|JsonNode
name|node
init|=
name|response
operator|.
name|readEntity
argument_list|(
name|JsonNode
operator|.
name|class
argument_list|)
decl_stmt|;
name|Object
name|body
init|=
name|unwrap
argument_list|(
name|node
argument_list|,
name|model
argument_list|)
decl_stmt|;
if|if
condition|(
name|body
operator|!=
literal|null
condition|)
block|{
name|message
operator|.
name|setBody
argument_list|(
name|body
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|this
return|;
block|}
DECL|method|validateBody (Message message, Class<?> model)
specifier|protected
name|AbstractServiceNowProcessor
name|validateBody
parameter_list|(
name|Message
name|message
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|model
parameter_list|)
block|{
return|return
name|validateBody
argument_list|(
name|message
operator|.
name|getBody
argument_list|()
argument_list|,
name|model
argument_list|)
return|;
block|}
DECL|method|validateBody (Object body, Class<?> model)
specifier|protected
name|AbstractServiceNowProcessor
name|validateBody
parameter_list|(
name|Object
name|body
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|model
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|body
argument_list|,
literal|"body"
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|body
operator|.
name|getClass
argument_list|()
operator|.
name|isAssignableFrom
argument_list|(
name|model
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Body is not compatible with model (body="
operator|+
name|body
operator|.
name|getClass
argument_list|()
operator|+
literal|", model="
operator|+
name|model
argument_list|)
throw|;
block|}
return|return
name|this
return|;
block|}
DECL|method|unwrap (JsonNode answer, Class<?> model)
specifier|protected
name|Object
name|unwrap
parameter_list|(
name|JsonNode
name|answer
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|model
parameter_list|)
throws|throws
name|IOException
block|{
name|Object
name|result
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|answer
operator|!=
literal|null
condition|)
block|{
name|JsonNode
name|node
init|=
name|answer
operator|.
name|get
argument_list|(
literal|"result"
argument_list|)
decl_stmt|;
if|if
condition|(
name|node
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|node
operator|.
name|isArray
argument_list|()
condition|)
block|{
if|if
condition|(
name|model
operator|.
name|isInstance
argument_list|(
name|Map
operator|.
name|class
argument_list|)
condition|)
block|{
comment|// If the model is a Map, there's no need to use any
comment|// specific JavaType to instruct Jackson about the
comment|// expected element type
name|result
operator|=
name|mapper
operator|.
name|treeToValue
argument_list|(
name|node
argument_list|,
name|List
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|result
operator|=
name|mapper
operator|.
name|readValue
argument_list|(
name|node
operator|.
name|traverse
argument_list|()
argument_list|,
name|javaTypeCache
operator|.
name|get
argument_list|(
name|model
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|result
operator|=
name|mapper
operator|.
name|treeToValue
argument_list|(
name|node
argument_list|,
name|model
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|result
return|;
block|}
comment|// *********************************
comment|// Helpers
comment|// *********************************
DECL|method|addDispatcher (ServiceNowDispatcher dispatcher)
specifier|protected
specifier|final
name|void
name|addDispatcher
parameter_list|(
name|ServiceNowDispatcher
name|dispatcher
parameter_list|)
block|{
name|this
operator|.
name|dispatchers
operator|.
name|add
argument_list|(
name|dispatcher
argument_list|)
expr_stmt|;
block|}
DECL|method|addDispatcher (String action, Processor processor)
specifier|protected
specifier|final
name|void
name|addDispatcher
parameter_list|(
name|String
name|action
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
name|addDispatcher
argument_list|(
name|ServiceNowDispatcher
operator|.
name|on
argument_list|(
name|action
argument_list|,
literal|null
argument_list|,
name|processor
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|addDispatcher (String action, String subject, Processor processor)
specifier|protected
specifier|final
name|void
name|addDispatcher
parameter_list|(
name|String
name|action
parameter_list|,
name|String
name|subject
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
name|addDispatcher
argument_list|(
name|ServiceNowDispatcher
operator|.
name|on
argument_list|(
name|action
argument_list|,
name|subject
argument_list|,
name|processor
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|findDispatcher (Exchange exchange)
specifier|protected
specifier|final
name|ServiceNowDispatcher
name|findDispatcher
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|ServiceNowDispatcher
name|dispatcher
init|=
literal|null
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
name|dispatchers
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|dispatcher
operator|=
name|dispatchers
operator|.
name|get
argument_list|(
name|i
argument_list|)
expr_stmt|;
if|if
condition|(
name|dispatcher
operator|.
name|match
argument_list|(
name|exchange
argument_list|)
condition|)
block|{
return|return
name|dispatcher
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|getRequestParamFromHeader (ServiceNowParam sysParam, Message message)
specifier|protected
name|Object
name|getRequestParamFromHeader
parameter_list|(
name|ServiceNowParam
name|sysParam
parameter_list|,
name|Message
name|message
parameter_list|)
block|{
return|return
name|message
operator|.
name|getHeader
argument_list|(
name|sysParam
operator|.
name|getHeader
argument_list|()
argument_list|,
name|sysParam
operator|.
name|getDefaultValue
argument_list|(
name|config
argument_list|)
argument_list|,
name|sysParam
operator|.
name|getType
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getMandatoryRequestParamFromHeader (ServiceNowParam sysParam, Message message)
specifier|protected
name|Object
name|getMandatoryRequestParamFromHeader
parameter_list|(
name|ServiceNowParam
name|sysParam
parameter_list|,
name|Message
name|message
parameter_list|)
block|{
return|return
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|getRequestParamFromHeader
argument_list|(
name|sysParam
argument_list|,
name|message
argument_list|)
argument_list|,
name|sysParam
operator|.
name|getHeader
argument_list|()
argument_list|)
return|;
block|}
DECL|method|copyHeader (Response from, String fromId, Message to, String toId)
specifier|protected
name|void
name|copyHeader
parameter_list|(
name|Response
name|from
parameter_list|,
name|String
name|fromId
parameter_list|,
name|Message
name|to
parameter_list|,
name|String
name|toId
parameter_list|)
block|{
name|Object
name|fromValue
init|=
name|from
operator|.
name|getHeaders
argument_list|()
operator|.
name|getFirst
argument_list|(
name|fromId
argument_list|)
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|fromValue
argument_list|)
condition|)
block|{
name|to
operator|.
name|setHeader
argument_list|(
name|toId
argument_list|,
name|fromValue
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getModel (Message message)
specifier|protected
name|Class
argument_list|<
name|?
argument_list|>
name|getModel
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
return|return
name|getModel
argument_list|(
name|message
argument_list|,
literal|null
argument_list|)
return|;
block|}
DECL|method|getModel (Message message, String modelName)
specifier|protected
name|Class
argument_list|<
name|?
argument_list|>
name|getModel
parameter_list|(
name|Message
name|message
parameter_list|,
name|String
name|modelName
parameter_list|)
block|{
return|return
name|message
operator|.
name|getHeader
argument_list|(
name|ServiceNowConstants
operator|.
name|MODEL
argument_list|,
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|modelName
argument_list|)
condition|?
name|Map
operator|.
name|class
else|:
name|config
operator|.
name|getModel
argument_list|(
name|modelName
argument_list|,
name|Map
operator|.
name|class
argument_list|)
argument_list|,
name|Class
operator|.
name|class
argument_list|)
return|;
block|}
comment|// *************************************************************************
comment|// Use ClassValue to lazy create and cache JavaType
comment|// *************************************************************************
DECL|class|JavaTypeCache
specifier|private
class|class
name|JavaTypeCache
extends|extends
name|ClassValue
argument_list|<
name|JavaType
argument_list|>
block|{
annotation|@
name|Override
DECL|method|computeValue (Class<?> type)
specifier|protected
name|JavaType
name|computeValue
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|mapper
operator|.
name|getTypeFactory
argument_list|()
operator|.
name|constructCollectionType
argument_list|(
name|List
operator|.
name|class
argument_list|,
name|type
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

