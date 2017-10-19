begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce
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
name|util
operator|.
name|Collections
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
name|Optional
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
name|CompletableFuture
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
name|ExecutionException
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
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|module
operator|.
name|jsonSchema
operator|.
name|JsonSchema
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
name|utils
operator|.
name|JsonUtils
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
name|client
operator|.
name|RestClient
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
name|client
operator|.
name|RestClient
operator|.
name|ResponseCallback
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

begin_class
DECL|class|SalesforceMetaDataExtension
specifier|public
class|class
name|SalesforceMetaDataExtension
extends|extends
name|AbstractMetaDataExtension
block|{
annotation|@
name|FunctionalInterface
DECL|interface|SchemaMapper
interface|interface
name|SchemaMapper
block|{
DECL|method|map (InputStream stream)
name|JsonSchema
name|map
parameter_list|(
name|InputStream
name|stream
parameter_list|)
throws|throws
name|IOException
function_decl|;
block|}
DECL|field|MAPPER
specifier|private
specifier|static
specifier|final
name|ObjectMapper
name|MAPPER
init|=
name|JsonUtils
operator|.
name|createObjectMapper
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|meta (final Map<String, Object> parameters)
specifier|public
name|Optional
argument_list|<
name|MetaData
argument_list|>
name|meta
parameter_list|(
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
block|{
specifier|final
name|JsonSchema
name|schema
init|=
name|schemaFor
argument_list|(
name|parameters
argument_list|)
decl_stmt|;
specifier|final
name|MetaData
name|metaData
init|=
name|MetaDataBuilder
operator|.
name|on
argument_list|(
name|getCamelContext
argument_list|()
argument_list|)
comment|//
operator|.
name|withAttribute
argument_list|(
name|MetaData
operator|.
name|CONTENT_TYPE
argument_list|,
literal|"application/schema+json"
argument_list|)
comment|//
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
comment|//
operator|.
name|withPayload
argument_list|(
name|schema
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
return|return
name|Optional
operator|.
name|ofNullable
argument_list|(
name|metaData
argument_list|)
return|;
block|}
DECL|method|allObjectsSchema (final Map<String, Object> parameters)
name|JsonSchema
name|allObjectsSchema
parameter_list|(
specifier|final
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
return|return
name|SalesforceClientTemplate
operator|.
name|invoke
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|parameters
argument_list|,
name|client
lambda|->
name|fetchAllObjectsSchema
argument_list|(
name|client
argument_list|)
argument_list|)
return|;
block|}
DECL|method|schemaFor (final Map<String, Object> parameters)
name|JsonSchema
name|schemaFor
parameter_list|(
specifier|final
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
if|if
condition|(
name|parameters
operator|.
name|containsKey
argument_list|(
name|SalesforceEndpointConfig
operator|.
name|SOBJECT_NAME
argument_list|)
condition|)
block|{
return|return
name|singleObjectSchema
argument_list|(
name|parameters
argument_list|)
return|;
block|}
return|return
name|allObjectsSchema
argument_list|(
name|parameters
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
specifier|final
name|Exception
name|e
parameter_list|)
block|{
throw|throw
name|ObjectHelper
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|singleObjectSchema (final Map<String, Object> parameters)
name|JsonSchema
name|singleObjectSchema
parameter_list|(
specifier|final
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
return|return
name|SalesforceClientTemplate
operator|.
name|invoke
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|parameters
argument_list|,
name|client
lambda|->
name|fetchSingleObjectSchema
argument_list|(
name|client
argument_list|,
operator|(
name|String
operator|)
name|parameters
operator|.
name|get
argument_list|(
name|SalesforceEndpointConfig
operator|.
name|SOBJECT_NAME
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
DECL|method|fetch (final Consumer<ResponseCallback> restMethod, final SchemaMapper callback)
specifier|static
name|JsonSchema
name|fetch
parameter_list|(
specifier|final
name|Consumer
argument_list|<
name|ResponseCallback
argument_list|>
name|restMethod
parameter_list|,
specifier|final
name|SchemaMapper
name|callback
parameter_list|)
block|{
specifier|final
name|CompletableFuture
argument_list|<
name|JsonSchema
argument_list|>
name|ret
init|=
operator|new
name|CompletableFuture
argument_list|<>
argument_list|()
decl_stmt|;
name|restMethod
operator|.
name|accept
argument_list|(
parameter_list|(
name|response
parameter_list|,
name|headers
parameter_list|,
name|exception
parameter_list|)
lambda|->
block|{
if|if
condition|(
name|exception
operator|!=
literal|null
condition|)
block|{
name|ret
operator|.
name|completeExceptionally
argument_list|(
name|exception
argument_list|)
expr_stmt|;
block|}
else|else
block|{
try|try
init|(
specifier|final
name|InputStream
name|is
init|=
name|response
init|)
block|{
name|ret
operator|.
name|complete
argument_list|(
name|callback
operator|.
name|map
argument_list|(
name|is
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
specifier|final
name|IOException
name|e
parameter_list|)
block|{
name|ret
operator|.
name|completeExceptionally
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
try|try
block|{
return|return
name|ret
operator|.
name|get
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
decl||
name|ExecutionException
name|e
parameter_list|)
block|{
throw|throw
name|ObjectHelper
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|fetchAllObjectsSchema (final RestClient client)
specifier|static
name|JsonSchema
name|fetchAllObjectsSchema
parameter_list|(
specifier|final
name|RestClient
name|client
parameter_list|)
block|{
return|return
name|fetch
argument_list|(
name|callback
lambda|->
name|client
operator|.
name|getGlobalObjects
argument_list|(
name|Collections
operator|.
name|emptyMap
argument_list|()
argument_list|,
name|callback
argument_list|)
argument_list|,
name|SalesforceMetaDataExtension
operator|::
name|mapAllObjectsSchema
argument_list|)
return|;
block|}
DECL|method|fetchSingleObjectSchema (final RestClient client, final String objectName)
specifier|static
name|JsonSchema
name|fetchSingleObjectSchema
parameter_list|(
specifier|final
name|RestClient
name|client
parameter_list|,
specifier|final
name|String
name|objectName
parameter_list|)
block|{
return|return
name|fetch
argument_list|(
name|callback
lambda|->
name|client
operator|.
name|getDescription
argument_list|(
name|objectName
argument_list|,
name|Collections
operator|.
name|emptyMap
argument_list|()
argument_list|,
name|callback
argument_list|)
argument_list|,
name|SalesforceMetaDataExtension
operator|::
name|mapSingleObjectSchema
argument_list|)
return|;
block|}
DECL|method|mapAllObjectsSchema (final InputStream stream)
specifier|static
name|JsonSchema
name|mapAllObjectsSchema
parameter_list|(
specifier|final
name|InputStream
name|stream
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
name|GlobalObjects
name|globalObjects
init|=
name|MAPPER
operator|.
name|readerFor
argument_list|(
name|GlobalObjects
operator|.
name|class
argument_list|)
operator|.
name|readValue
argument_list|(
name|stream
argument_list|)
decl_stmt|;
return|return
name|JsonUtils
operator|.
name|getGlobalObjectsJsonSchemaAsSchema
argument_list|(
name|globalObjects
argument_list|)
return|;
block|}
DECL|method|mapSingleObjectSchema (final InputStream stream)
specifier|static
name|JsonSchema
name|mapSingleObjectSchema
parameter_list|(
specifier|final
name|InputStream
name|stream
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
name|SObjectDescription
name|description
init|=
name|MAPPER
operator|.
name|readerFor
argument_list|(
name|SObjectDescription
operator|.
name|class
argument_list|)
operator|.
name|readValue
argument_list|(
name|stream
argument_list|)
decl_stmt|;
return|return
name|JsonUtils
operator|.
name|getSObjectJsonSchemaAsSchema
argument_list|(
name|description
argument_list|,
literal|true
argument_list|)
return|;
block|}
block|}
end_class

end_unit

