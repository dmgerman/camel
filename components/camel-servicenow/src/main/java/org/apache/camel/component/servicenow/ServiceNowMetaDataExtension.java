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
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|java
operator|.
name|util
operator|.
name|Objects
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Optional
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Stack
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentHashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Consumer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Stream
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
name|HttpMethod
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
name|MediaType
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
name|core
operator|.
name|JsonProcessingException
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
name|node
operator|.
name|ArrayNode
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
name|node
operator|.
name|ObjectNode
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
name|RuntimeCamelException
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
name|extension
operator|.
name|MetaDataExtension
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
name|extension
operator|.
name|metadata
operator|.
name|AbstractMetaDataExtension
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
name|extension
operator|.
name|metadata
operator|.
name|MetaDataBuilder
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
name|servicenow
operator|.
name|model
operator|.
name|DictionaryEntry
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
name|IntrospectionSupport
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

begin_class
DECL|class|ServiceNowMetaDataExtension
specifier|final
class|class
name|ServiceNowMetaDataExtension
extends|extends
name|AbstractMetaDataExtension
block|{
DECL|field|LOGGER
specifier|private
specifier|static
specifier|final
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ServiceNowMetaDataExtension
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|properties
specifier|private
specifier|final
name|ConcurrentMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|properties
decl_stmt|;
DECL|method|ServiceNowMetaDataExtension ()
name|ServiceNowMetaDataExtension
parameter_list|()
block|{
name|this
operator|.
name|properties
operator|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|meta (Map<String, Object> parameters)
specifier|public
name|Optional
argument_list|<
name|MetaDataExtension
operator|.
name|MetaData
argument_list|>
name|meta
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
block|{
try|try
block|{
specifier|final
name|MetaContext
name|context
init|=
operator|new
name|MetaContext
argument_list|(
name|parameters
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|ObjectHelper
operator|.
name|equalIgnoreCase
argument_list|(
name|context
operator|.
name|getObjectType
argument_list|()
argument_list|,
literal|"table"
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Unsupported object type<"
operator|+
name|context
operator|.
name|getObjectType
argument_list|()
operator|+
literal|">"
argument_list|)
throw|;
block|}
return|return
name|tableMeta
argument_list|(
name|context
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|e
parameter_list|)
block|{
throw|throw
name|e
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|tableMeta (MetaContext context)
specifier|private
name|Optional
argument_list|<
name|MetaDataExtension
operator|.
name|MetaData
argument_list|>
name|tableMeta
parameter_list|(
name|MetaContext
name|context
parameter_list|)
block|{
try|try
block|{
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|names
init|=
name|getObjectHierarchy
argument_list|(
name|context
argument_list|)
decl_stmt|;
specifier|final
name|ObjectNode
name|root
init|=
name|context
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getMapper
argument_list|()
operator|.
name|createObjectNode
argument_list|()
decl_stmt|;
if|if
condition|(
name|names
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|Optional
operator|.
name|empty
argument_list|()
return|;
block|}
name|loadProperties
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|registerDefinitions
argument_list|(
name|context
argument_list|,
name|root
argument_list|)
expr_stmt|;
name|root
operator|.
name|putObject
argument_list|(
literal|"properties"
argument_list|)
expr_stmt|;
name|root
operator|.
name|put
argument_list|(
literal|"$schema"
argument_list|,
literal|"http://json-schema.org/schema#"
argument_list|)
expr_stmt|;
name|root
operator|.
name|put
argument_list|(
literal|"id"
argument_list|,
literal|"http://camel.apache.org/schemas/servicenow/"
operator|+
name|context
operator|.
name|getObjectName
argument_list|()
operator|+
literal|".json"
argument_list|)
expr_stmt|;
name|root
operator|.
name|put
argument_list|(
literal|"type"
argument_list|,
literal|"object"
argument_list|)
expr_stmt|;
name|root
operator|.
name|put
argument_list|(
literal|"additionalProperties"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|root
operator|.
name|putArray
argument_list|(
literal|"required"
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|name
range|:
name|names
control|)
block|{
name|context
operator|.
name|getStack
argument_list|()
operator|.
name|push
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Load dictionary<{}>"
argument_list|,
name|context
operator|.
name|getStack
argument_list|()
argument_list|)
expr_stmt|;
name|loadDictionary
argument_list|(
name|context
argument_list|,
name|name
argument_list|,
name|root
argument_list|)
expr_stmt|;
name|context
operator|.
name|getStack
argument_list|()
operator|.
name|pop
argument_list|()
expr_stmt|;
block|}
return|return
name|Optional
operator|.
name|of
argument_list|(
name|MetaDataBuilder
operator|.
name|on
argument_list|(
name|getCamelContext
argument_list|()
argument_list|)
operator|.
name|withAttribute
argument_list|(
name|MetaData
operator|.
name|CONTENT_TYPE
argument_list|,
literal|"application/schema+json"
argument_list|)
operator|.
name|withAttribute
argument_list|(
name|MetaData
operator|.
name|JAVA_TYPE
argument_list|,
name|JsonNode
operator|.
name|class
argument_list|)
operator|.
name|withPayload
argument_list|(
name|root
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
comment|// ********************************
comment|// Properties
comment|// ********************************
DECL|method|loadProperties (MetaContext context)
specifier|private
specifier|synchronized
name|void
name|loadProperties
parameter_list|(
name|MetaContext
name|context
parameter_list|)
block|{
if|if
condition|(
operator|!
name|properties
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
try|try
block|{
name|String
name|offset
init|=
literal|"0"
decl_stmt|;
while|while
condition|(
literal|true
condition|)
block|{
name|Response
name|response
init|=
name|context
operator|.
name|getClient
argument_list|()
operator|.
name|reset
argument_list|()
operator|.
name|types
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON_TYPE
argument_list|)
operator|.
name|path
argument_list|(
literal|"now"
argument_list|)
operator|.
name|path
argument_list|(
name|context
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getApiVersion
argument_list|()
argument_list|)
operator|.
name|path
argument_list|(
literal|"table"
argument_list|)
operator|.
name|path
argument_list|(
literal|"sys_properties"
argument_list|)
operator|.
name|query
argument_list|(
literal|"sysparm_exclude_reference_link"
argument_list|,
literal|"true"
argument_list|)
operator|.
name|query
argument_list|(
literal|"sysparm_fields"
argument_list|,
literal|"name%2Cvalue"
argument_list|)
operator|.
name|query
argument_list|(
literal|"sysparm_offset"
argument_list|,
name|offset
argument_list|)
operator|.
name|query
argument_list|(
literal|"sysparm_query"
argument_list|,
literal|"name=glide.sys.date_format^ORname=glide.sys.time_format"
argument_list|)
operator|.
name|invoke
argument_list|(
name|HttpMethod
operator|.
name|GET
argument_list|)
decl_stmt|;
name|findResultNode
argument_list|(
name|response
argument_list|)
operator|.
name|ifPresent
argument_list|(
name|node
lambda|->
name|processResult
argument_list|(
name|node
argument_list|,
name|n
lambda|->
block|{
block|if (n.hasNonNull("name"
argument_list|)
operator|&&
name|n
operator|.
name|hasNonNull
argument_list|(
literal|"value"
argument_list|)
block|)
block|{
name|properties
operator|.
name|put
argument_list|(
name|n
operator|.
name|findValue
argument_list|(
literal|"name"
argument_list|)
operator|.
name|asText
argument_list|()
argument_list|,
name|n
operator|.
name|findValue
argument_list|(
literal|"value"
argument_list|)
operator|.
name|asText
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|)
block|)
class|;
end_class

begin_decl_stmt
name|Optional
argument_list|<
name|String
argument_list|>
name|next
init|=
name|ServiceNowHelper
operator|.
name|findOffset
argument_list|(
name|response
argument_list|,
name|ServiceNowConstants
operator|.
name|LINK_NEXT
argument_list|)
decl_stmt|;
end_decl_stmt

begin_if
if|if
condition|(
name|next
operator|.
name|isPresent
argument_list|()
condition|)
block|{
name|offset
operator|=
name|next
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
else|else
block|{
break|break;
block|}
end_if

begin_catch
unit|}         }
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
end_catch

begin_comment
unit|}
comment|// ********************************
end_comment

begin_comment
comment|// Definitions
end_comment

begin_comment
comment|// ********************************
end_comment

begin_function
DECL|method|registerDefinitions (MetaContext context, ObjectNode root)
unit|private
name|void
name|registerDefinitions
parameter_list|(
name|MetaContext
name|context
parameter_list|,
name|ObjectNode
name|root
parameter_list|)
block|{
specifier|final
name|ObjectNode
name|definitions
init|=
name|root
operator|.
name|putObject
argument_list|(
literal|"definitions"
argument_list|)
decl_stmt|;
comment|// Global Unique ID
name|definitions
operator|.
name|putObject
argument_list|(
literal|"guid"
argument_list|)
operator|.
name|put
argument_list|(
literal|"type"
argument_list|,
literal|"string"
argument_list|)
operator|.
name|put
argument_list|(
literal|"pattern"
argument_list|,
literal|"^[a-fA-F0-9]{32}"
argument_list|)
expr_stmt|;
comment|// Date/Time
name|String
name|dateFormat
init|=
name|properties
operator|.
name|getOrDefault
argument_list|(
literal|"glide.sys.date_format"
argument_list|,
literal|"yyyy-MM-dd"
argument_list|)
decl_stmt|;
name|String
name|timeFormat
init|=
name|properties
operator|.
name|getOrDefault
argument_list|(
literal|"glide.sys.time_format"
argument_list|,
literal|"HH:mm:ss"
argument_list|)
decl_stmt|;
name|definitions
operator|.
name|putObject
argument_list|(
literal|"date"
argument_list|)
operator|.
name|put
argument_list|(
literal|"type"
argument_list|,
literal|"string"
argument_list|)
operator|.
name|put
argument_list|(
literal|"format"
argument_list|,
name|dateFormat
argument_list|)
expr_stmt|;
name|definitions
operator|.
name|putObject
argument_list|(
literal|"time"
argument_list|)
operator|.
name|put
argument_list|(
literal|"type"
argument_list|,
literal|"string"
argument_list|)
operator|.
name|put
argument_list|(
literal|"format"
argument_list|,
name|timeFormat
argument_list|)
expr_stmt|;
name|definitions
operator|.
name|putObject
argument_list|(
literal|"date-time"
argument_list|)
operator|.
name|put
argument_list|(
literal|"type"
argument_list|,
literal|"string"
argument_list|)
operator|.
name|put
argument_list|(
literal|"format"
argument_list|,
name|dateFormat
operator|+
literal|" "
operator|+
name|timeFormat
argument_list|)
expr_stmt|;
block|}
end_function

begin_comment
comment|// ********************************
end_comment

begin_comment
comment|// Dictionary
end_comment

begin_comment
comment|// ********************************
end_comment

begin_function
DECL|method|loadDictionary (MetaContext context, String name, ObjectNode root)
specifier|private
name|void
name|loadDictionary
parameter_list|(
name|MetaContext
name|context
parameter_list|,
name|String
name|name
parameter_list|,
name|ObjectNode
name|root
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|offset
init|=
literal|"0"
decl_stmt|;
while|while
condition|(
literal|true
condition|)
block|{
name|Response
name|response
init|=
name|context
operator|.
name|getClient
argument_list|()
operator|.
name|reset
argument_list|()
operator|.
name|types
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON_TYPE
argument_list|)
operator|.
name|path
argument_list|(
literal|"now"
argument_list|)
operator|.
name|path
argument_list|(
name|context
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getApiVersion
argument_list|()
argument_list|)
operator|.
name|path
argument_list|(
literal|"table"
argument_list|)
operator|.
name|path
argument_list|(
literal|"sys_dictionary"
argument_list|)
operator|.
name|query
argument_list|(
literal|"sysparm_display_value"
argument_list|,
literal|"false"
argument_list|)
operator|.
name|queryF
argument_list|(
literal|"sysparm_query"
argument_list|,
literal|"name=%s"
argument_list|,
name|name
argument_list|)
operator|.
name|query
argument_list|(
literal|"sysparm_offset"
argument_list|,
name|offset
argument_list|)
operator|.
name|invoke
argument_list|(
name|HttpMethod
operator|.
name|GET
argument_list|)
decl_stmt|;
name|findResultNode
argument_list|(
name|response
argument_list|)
operator|.
name|ifPresent
argument_list|(
name|node
lambda|->
name|processResult
argument_list|(
name|node
argument_list|,
name|n
lambda|->
block|{
name|processDictionaryNode
argument_list|(
name|context
argument_list|,
name|root
argument_list|,
name|n
argument_list|)
argument_list|;
block|}
block|)
end_function

begin_empty_stmt
unit|)
empty_stmt|;
end_empty_stmt

begin_decl_stmt
name|Optional
argument_list|<
name|String
argument_list|>
name|next
init|=
name|ServiceNowHelper
operator|.
name|findOffset
argument_list|(
name|response
argument_list|,
name|ServiceNowConstants
operator|.
name|LINK_NEXT
argument_list|)
decl_stmt|;
end_decl_stmt

begin_if
if|if
condition|(
name|next
operator|.
name|isPresent
argument_list|()
condition|)
block|{
name|offset
operator|=
name|next
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
else|else
block|{
break|break;
block|}
end_if

begin_function
unit|}     }
DECL|method|processDictionaryNode (MetaContext context, ObjectNode root, JsonNode node)
specifier|private
name|void
name|processDictionaryNode
parameter_list|(
name|MetaContext
name|context
parameter_list|,
name|ObjectNode
name|root
parameter_list|,
name|JsonNode
name|node
parameter_list|)
block|{
if|if
condition|(
name|node
operator|.
name|hasNonNull
argument_list|(
literal|"element"
argument_list|)
condition|)
block|{
specifier|final
name|String
name|id
init|=
name|node
operator|.
name|get
argument_list|(
literal|"element"
argument_list|)
operator|.
name|asText
argument_list|()
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|id
argument_list|)
condition|)
block|{
name|String
name|parent
init|=
name|context
operator|.
name|getStack
argument_list|()
operator|.
name|peek
argument_list|()
decl_stmt|;
name|String
name|includeKey
init|=
literal|"object."
operator|+
name|parent
operator|+
literal|".fields"
decl_stmt|;
name|String
name|fields
init|=
operator|(
name|String
operator|)
name|context
operator|.
name|getParameters
argument_list|()
operator|.
name|get
argument_list|(
name|includeKey
argument_list|)
decl_stmt|;
name|boolean
name|included
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|fields
argument_list|)
condition|)
block|{
if|if
condition|(
name|Stream
operator|.
name|of
argument_list|(
name|fields
operator|.
name|split
argument_list|(
literal|","
argument_list|)
argument_list|)
operator|.
name|map
argument_list|(
name|StringHelper
operator|::
name|trimToNull
argument_list|)
operator|.
name|filter
argument_list|(
name|Objects
operator|::
name|nonNull
argument_list|)
operator|.
name|map
argument_list|(
name|Pattern
operator|::
name|compile
argument_list|)
operator|.
name|anyMatch
argument_list|(
name|p
lambda|->
name|p
operator|.
name|matcher
argument_list|(
name|id
argument_list|)
operator|.
name|matches
argument_list|()
argument_list|)
condition|)
block|{
name|included
operator|=
literal|true
block|;                     }
if|if
condition|(
name|Stream
operator|.
name|of
argument_list|(
name|fields
operator|.
name|split
argument_list|(
literal|","
argument_list|)
argument_list|)
operator|.
name|map
argument_list|(
name|StringHelper
operator|::
name|trimToNull
argument_list|)
operator|.
name|filter
argument_list|(
name|Objects
operator|::
name|nonNull
argument_list|)
operator|.
name|anyMatch
argument_list|(
name|id
operator|::
name|equalsIgnoreCase
argument_list|)
condition|)
block|{
name|included
operator|=
literal|true
expr_stmt|;
block|}
block|}
else|else
block|{
name|included
operator|=
operator|!
name|context
operator|.
name|getParameters
argument_list|()
operator|.
name|containsKey
argument_list|(
name|includeKey
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|included
condition|)
block|{
return|return;
block|}
name|context
operator|.
name|getStack
argument_list|()
operator|.
name|push
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Load dictionary element<{}>"
argument_list|,
name|context
operator|.
name|getStack
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
specifier|final
name|DictionaryEntry
name|entry
init|=
name|context
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getMapper
argument_list|()
operator|.
name|treeToValue
argument_list|(
name|node
argument_list|,
name|DictionaryEntry
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|ObjectNode
name|property
init|=
operator|(
operator|(
name|ObjectNode
operator|)
name|root
operator|.
name|get
argument_list|(
literal|"properties"
argument_list|)
operator|)
operator|.
name|putObject
argument_list|(
name|id
argument_list|)
decl_stmt|;
comment|// Add custom fields for code generation, json schema
comment|// validators are not supposed to use this extensions.
specifier|final
name|ObjectNode
name|servicenow
init|=
name|property
operator|.
name|putObject
argument_list|(
literal|"servicenow"
argument_list|)
decl_stmt|;
comment|// the internal type
name|servicenow
operator|.
name|put
argument_list|(
literal|"internal_type"
argument_list|,
name|entry
operator|.
name|getInternalType
argument_list|()
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
switch|switch
condition|(
name|entry
operator|.
name|getInternalType
argument_list|()
operator|.
name|getValue
argument_list|()
condition|)
block|{
case|case
literal|"integer"
case|:
name|property
operator|.
name|put
argument_list|(
literal|"type"
argument_list|,
literal|"number"
argument_list|)
expr_stmt|;
break|break;
case|case
literal|"boolean"
case|:
name|property
operator|.
name|put
argument_list|(
literal|"type"
argument_list|,
literal|"boolean"
argument_list|)
expr_stmt|;
break|break;
case|case
literal|"guid"
case|:
case|case
literal|"GUID"
case|:
name|property
operator|.
name|put
argument_list|(
literal|"$ref"
argument_list|,
literal|"#/definitions/guid"
argument_list|)
expr_stmt|;
break|break;
case|case
literal|"glide_date"
case|:
name|property
operator|.
name|put
argument_list|(
literal|"$ref"
argument_list|,
literal|"#/definitions/date"
argument_list|)
expr_stmt|;
break|break;
case|case
literal|"due_date"
case|:
case|case
literal|"glide_date_time"
case|:
case|case
literal|"glide_time"
case|:
case|case
literal|"glide_duration"
case|:
name|property
operator|.
name|put
argument_list|(
literal|"$ref"
argument_list|,
literal|"#/definitions/date-time"
argument_list|)
expr_stmt|;
break|break;
case|case
literal|"reference"
case|:
name|property
operator|.
name|put
argument_list|(
literal|"$ref"
argument_list|,
literal|"#/definitions/guid"
argument_list|)
expr_stmt|;
if|if
condition|(
name|entry
operator|.
name|getReference
argument_list|()
operator|.
name|getValue
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// the referenced object type
name|servicenow
operator|.
name|put
argument_list|(
literal|"sys_db_object"
argument_list|,
name|entry
operator|.
name|getReference
argument_list|()
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
break|break;
default|default:
name|property
operator|.
name|put
argument_list|(
literal|"type"
argument_list|,
literal|"string"
argument_list|)
expr_stmt|;
if|if
condition|(
name|entry
operator|.
name|getMaxLength
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|property
operator|.
name|put
argument_list|(
literal|"maxLength"
argument_list|,
name|entry
operator|.
name|getMaxLength
argument_list|()
argument_list|)
expr_stmt|;
block|}
break|break;
block|}
if|if
condition|(
name|entry
operator|.
name|isMandatory
argument_list|()
condition|)
block|{
name|ArrayNode
name|required
init|=
operator|(
name|ArrayNode
operator|)
name|root
operator|.
name|get
argument_list|(
literal|"required"
argument_list|)
decl_stmt|;
if|if
condition|(
name|required
operator|==
literal|null
condition|)
block|{
name|required
operator|=
name|root
operator|.
name|putArray
argument_list|(
literal|"required"
argument_list|)
expr_stmt|;
block|}
name|required
operator|.
name|add
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|JsonProcessingException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
name|context
operator|.
name|getStack
argument_list|()
operator|.
name|pop
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
end_function

begin_comment
comment|// *************************************
end_comment

begin_comment
comment|// Helpers
end_comment

begin_comment
comment|// *************************************
end_comment

begin_function
DECL|method|getObjectHierarchy (MetaContext context)
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|getObjectHierarchy
parameter_list|(
name|MetaContext
name|context
parameter_list|)
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|String
argument_list|>
name|hierarchy
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|String
name|query
init|=
name|String
operator|.
name|format
argument_list|(
literal|"name=%s"
argument_list|,
name|context
operator|.
name|getObjectName
argument_list|()
argument_list|)
decl_stmt|;
while|while
condition|(
literal|true
condition|)
block|{
name|Optional
argument_list|<
name|JsonNode
argument_list|>
name|response
init|=
name|context
operator|.
name|getClient
argument_list|()
operator|.
name|reset
argument_list|()
operator|.
name|types
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON_TYPE
argument_list|)
operator|.
name|path
argument_list|(
literal|"now"
argument_list|)
operator|.
name|path
argument_list|(
name|context
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getApiVersion
argument_list|()
argument_list|)
operator|.
name|path
argument_list|(
literal|"table"
argument_list|)
operator|.
name|path
argument_list|(
literal|"sys_db_object"
argument_list|)
operator|.
name|query
argument_list|(
literal|"sysparm_exclude_reference_link"
argument_list|,
literal|"true"
argument_list|)
operator|.
name|query
argument_list|(
literal|"sysparm_fields"
argument_list|,
literal|"name%2Csuper_class"
argument_list|)
operator|.
name|query
argument_list|(
literal|"sysparm_query"
argument_list|,
name|query
argument_list|)
operator|.
name|trasform
argument_list|(
name|HttpMethod
operator|.
name|GET
argument_list|,
name|this
operator|::
name|findResultNode
argument_list|)
decl_stmt|;
if|if
condition|(
name|response
operator|.
name|isPresent
argument_list|()
condition|)
block|{
name|JsonNode
name|node
init|=
name|response
operator|.
name|get
argument_list|()
decl_stmt|;
name|JsonNode
name|nameNode
init|=
name|node
operator|.
name|findValue
argument_list|(
literal|"name"
argument_list|)
decl_stmt|;
name|JsonNode
name|classNode
init|=
name|node
operator|.
name|findValue
argument_list|(
literal|"super_class"
argument_list|)
decl_stmt|;
if|if
condition|(
name|nameNode
operator|!=
literal|null
operator|&&
name|classNode
operator|!=
literal|null
condition|)
block|{
name|query
operator|=
name|String
operator|.
name|format
argument_list|(
literal|"sys_id=%s"
argument_list|,
name|classNode
operator|.
name|textValue
argument_list|()
argument_list|)
expr_stmt|;
name|hierarchy
operator|.
name|add
argument_list|(
literal|0
argument_list|,
name|nameNode
operator|.
name|textValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
break|break;
block|}
block|}
else|else
block|{
break|break;
block|}
block|}
return|return
name|hierarchy
return|;
block|}
end_function

begin_function
DECL|method|processResult (JsonNode node, Consumer<JsonNode> consumer)
specifier|private
name|void
name|processResult
parameter_list|(
name|JsonNode
name|node
parameter_list|,
name|Consumer
argument_list|<
name|JsonNode
argument_list|>
name|consumer
parameter_list|)
block|{
if|if
condition|(
name|node
operator|.
name|isArray
argument_list|()
condition|)
block|{
name|Iterator
argument_list|<
name|JsonNode
argument_list|>
name|it
init|=
name|node
operator|.
name|elements
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|consumer
operator|.
name|accept
argument_list|(
name|it
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|consumer
operator|.
name|accept
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
block|}
end_function

begin_function
DECL|method|findResultNode (Response response)
specifier|private
name|Optional
argument_list|<
name|JsonNode
argument_list|>
name|findResultNode
parameter_list|(
name|Response
name|response
parameter_list|)
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
name|root
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
if|if
condition|(
name|root
operator|!=
literal|null
condition|)
block|{
name|Iterator
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|JsonNode
argument_list|>
argument_list|>
name|fields
init|=
name|root
operator|.
name|fields
argument_list|()
decl_stmt|;
while|while
condition|(
name|fields
operator|.
name|hasNext
argument_list|()
condition|)
block|{
specifier|final
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|JsonNode
argument_list|>
name|entry
init|=
name|fields
operator|.
name|next
argument_list|()
decl_stmt|;
specifier|final
name|String
name|key
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
specifier|final
name|JsonNode
name|node
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
literal|"result"
argument_list|,
name|key
argument_list|,
literal|true
argument_list|)
condition|)
block|{
return|return
name|Optional
operator|.
name|of
argument_list|(
name|node
argument_list|)
return|;
block|}
block|}
block|}
block|}
return|return
name|Optional
operator|.
name|empty
argument_list|()
return|;
block|}
end_function

begin_comment
comment|// *********************************
end_comment

begin_comment
comment|// Context class
end_comment

begin_comment
comment|// *********************************
end_comment

begin_class
DECL|class|MetaContext
specifier|private
specifier|final
class|class
name|MetaContext
block|{
DECL|field|parameters
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
decl_stmt|;
DECL|field|configuration
specifier|private
specifier|final
name|ServiceNowConfiguration
name|configuration
decl_stmt|;
DECL|field|client
specifier|private
specifier|final
name|ServiceNowClient
name|client
decl_stmt|;
DECL|field|instanceName
specifier|private
specifier|final
name|String
name|instanceName
decl_stmt|;
DECL|field|objectName
specifier|private
specifier|final
name|String
name|objectName
decl_stmt|;
DECL|field|objectType
specifier|private
specifier|final
name|String
name|objectType
decl_stmt|;
DECL|field|stack
specifier|private
specifier|final
name|Stack
argument_list|<
name|String
argument_list|>
name|stack
decl_stmt|;
DECL|method|MetaContext (Map<String, Object> parameters)
name|MetaContext
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|this
operator|.
name|parameters
operator|=
name|parameters
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|getComponent
argument_list|(
name|ServiceNowComponent
operator|.
name|class
argument_list|)
operator|.
name|getConfiguration
argument_list|()
operator|.
name|copy
argument_list|()
expr_stmt|;
name|this
operator|.
name|stack
operator|=
operator|new
name|Stack
argument_list|<>
argument_list|()
expr_stmt|;
name|IntrospectionSupport
operator|.
name|setProperties
argument_list|(
name|configuration
argument_list|,
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|parameters
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|instanceName
operator|=
operator|(
name|String
operator|)
name|parameters
operator|.
name|getOrDefault
argument_list|(
literal|"instanceName"
argument_list|,
name|getComponent
argument_list|(
name|ServiceNowComponent
operator|.
name|class
argument_list|)
operator|.
name|getInstanceName
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|objectType
operator|=
operator|(
name|String
operator|)
name|parameters
operator|.
name|getOrDefault
argument_list|(
literal|"objectType"
argument_list|,
literal|"table"
argument_list|)
expr_stmt|;
name|this
operator|.
name|objectName
operator|=
operator|(
name|String
operator|)
name|parameters
operator|.
name|getOrDefault
argument_list|(
literal|"objectName"
argument_list|,
name|configuration
operator|.
name|getTable
argument_list|()
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|instanceName
argument_list|,
literal|"instanceName"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|objectName
argument_list|,
literal|"objectName"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|objectType
argument_list|,
literal|"objectType"
argument_list|)
expr_stmt|;
comment|// Configure Api and OAuthToken ULRs using instanceName
if|if
condition|(
operator|!
name|configuration
operator|.
name|hasApiUrl
argument_list|()
condition|)
block|{
name|configuration
operator|.
name|setApiUrl
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"https://%s.service-now.com/api"
argument_list|,
name|instanceName
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|configuration
operator|.
name|hasOauthTokenUrl
argument_list|()
condition|)
block|{
name|configuration
operator|.
name|setOauthTokenUrl
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"https://%s.service-now.com/oauth_token.do"
argument_list|,
name|instanceName
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|client
operator|=
operator|new
name|ServiceNowClient
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
block|}
DECL|method|getParameters ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getParameters
parameter_list|()
block|{
return|return
name|parameters
return|;
block|}
DECL|method|getConfiguration ()
specifier|public
name|ServiceNowConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|getClient ()
specifier|public
name|ServiceNowClient
name|getClient
parameter_list|()
block|{
return|return
name|client
return|;
block|}
DECL|method|getInstanceName ()
specifier|public
name|String
name|getInstanceName
parameter_list|()
block|{
return|return
name|instanceName
return|;
block|}
DECL|method|getObjectType ()
specifier|public
name|String
name|getObjectType
parameter_list|()
block|{
return|return
name|objectType
return|;
block|}
DECL|method|getObjectName ()
specifier|public
name|String
name|getObjectName
parameter_list|()
block|{
return|return
name|objectName
return|;
block|}
DECL|method|getStack ()
specifier|public
name|Stack
argument_list|<
name|String
argument_list|>
name|getStack
parameter_list|()
block|{
return|return
name|stack
return|;
block|}
block|}
end_class

unit|}
end_unit

