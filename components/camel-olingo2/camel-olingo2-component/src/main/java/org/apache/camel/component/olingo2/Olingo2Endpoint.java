begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.olingo2
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|olingo2
package|;
end_package

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
name|HashSet
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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|Consumer
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
name|Producer
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
name|olingo2
operator|.
name|internal
operator|.
name|Olingo2ApiCollection
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
name|olingo2
operator|.
name|internal
operator|.
name|Olingo2ApiName
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
name|olingo2
operator|.
name|internal
operator|.
name|Olingo2Constants
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
name|olingo2
operator|.
name|internal
operator|.
name|Olingo2PropertiesHelper
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
name|UriEndpoint
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
name|UriParam
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
name|component
operator|.
name|AbstractApiEndpoint
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
name|component
operator|.
name|ApiMethod
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
name|component
operator|.
name|ApiMethodPropertiesHelper
import|;
end_import

begin_comment
comment|/**  * Communicates with OData 2.0 services using Apache Olingo.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.14.0"
argument_list|,
name|scheme
operator|=
literal|"olingo2"
argument_list|,
name|title
operator|=
literal|"Olingo2"
argument_list|,
name|syntax
operator|=
literal|"olingo2:apiName/methodName"
argument_list|,
name|consumerClass
operator|=
name|Olingo2Consumer
operator|.
name|class
argument_list|,
name|label
operator|=
literal|"cloud"
argument_list|)
DECL|class|Olingo2Endpoint
specifier|public
class|class
name|Olingo2Endpoint
extends|extends
name|AbstractApiEndpoint
argument_list|<
name|Olingo2ApiName
argument_list|,
name|Olingo2Configuration
argument_list|>
block|{
DECL|field|RESOURCE_PATH_PROPERTY
specifier|protected
specifier|static
specifier|final
name|String
name|RESOURCE_PATH_PROPERTY
init|=
literal|"resourcePath"
decl_stmt|;
DECL|field|RESPONSE_HANDLER_PROPERTY
specifier|protected
specifier|static
specifier|final
name|String
name|RESPONSE_HANDLER_PROPERTY
init|=
literal|"responseHandler"
decl_stmt|;
DECL|field|KEY_PREDICATE_PROPERTY
specifier|private
specifier|static
specifier|final
name|String
name|KEY_PREDICATE_PROPERTY
init|=
literal|"keyPredicate"
decl_stmt|;
DECL|field|QUERY_PARAMS_PROPERTY
specifier|private
specifier|static
specifier|final
name|String
name|QUERY_PARAMS_PROPERTY
init|=
literal|"queryParams"
decl_stmt|;
DECL|field|READ_METHOD
specifier|private
specifier|static
specifier|final
name|String
name|READ_METHOD
init|=
literal|"read"
decl_stmt|;
DECL|field|EDM_PROPERTY
specifier|private
specifier|static
specifier|final
name|String
name|EDM_PROPERTY
init|=
literal|"edm"
decl_stmt|;
DECL|field|DATA_PROPERTY
specifier|private
specifier|static
specifier|final
name|String
name|DATA_PROPERTY
init|=
literal|"data"
decl_stmt|;
DECL|field|DELETE_METHOD
specifier|private
specifier|static
specifier|final
name|String
name|DELETE_METHOD
init|=
literal|"delete"
decl_stmt|;
comment|// unparsed variants
DECL|field|UREAD_METHOD
specifier|private
specifier|static
specifier|final
name|String
name|UREAD_METHOD
init|=
literal|"uread"
decl_stmt|;
DECL|field|endpointPropertyNames
specifier|private
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|endpointPropertyNames
decl_stmt|;
annotation|@
name|UriParam
DECL|field|configuration
specifier|private
name|Olingo2Configuration
name|configuration
decl_stmt|;
DECL|field|apiProxy
specifier|private
name|Olingo2AppWrapper
name|apiProxy
decl_stmt|;
DECL|method|Olingo2Endpoint (String uri, Olingo2Component component, Olingo2ApiName apiName, String methodName, Olingo2Configuration endpointConfiguration)
specifier|public
name|Olingo2Endpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|Olingo2Component
name|component
parameter_list|,
name|Olingo2ApiName
name|apiName
parameter_list|,
name|String
name|methodName
parameter_list|,
name|Olingo2Configuration
name|endpointConfiguration
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|,
name|apiName
argument_list|,
name|methodName
argument_list|,
name|Olingo2ApiCollection
operator|.
name|getCollection
argument_list|()
operator|.
name|getHelper
argument_list|(
name|apiName
argument_list|)
argument_list|,
name|endpointConfiguration
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|endpointConfiguration
expr_stmt|;
comment|// get all endpoint property names
name|endpointPropertyNames
operator|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|(
name|getPropertiesHelper
argument_list|()
operator|.
name|getValidEndpointProperties
argument_list|(
name|configuration
argument_list|)
argument_list|)
expr_stmt|;
comment|// avoid adding edm as queryParam
name|endpointPropertyNames
operator|.
name|add
argument_list|(
name|EDM_PROPERTY
argument_list|)
expr_stmt|;
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|Olingo2Producer
argument_list|(
name|this
argument_list|)
return|;
block|}
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
comment|// make sure inBody is not set for consumers
if|if
condition|(
name|inBody
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Option inBody is not supported for consumer endpoint"
argument_list|)
throw|;
block|}
comment|// only read method is supported
if|if
condition|(
operator|!
name|READ_METHOD
operator|.
name|equals
argument_list|(
name|methodName
argument_list|)
operator|&&
operator|!
name|UREAD_METHOD
operator|.
name|equals
argument_list|(
name|methodName
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Only read method is supported for consumer endpoints"
argument_list|)
throw|;
block|}
specifier|final
name|Olingo2Consumer
name|consumer
init|=
operator|new
name|Olingo2Consumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
decl_stmt|;
comment|// also set consumer.* properties
name|configureConsumer
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
return|return
name|consumer
return|;
block|}
annotation|@
name|Override
DECL|method|getPropertiesHelper ()
specifier|protected
name|ApiMethodPropertiesHelper
argument_list|<
name|Olingo2Configuration
argument_list|>
name|getPropertiesHelper
parameter_list|()
block|{
return|return
name|Olingo2PropertiesHelper
operator|.
name|getHelper
argument_list|()
return|;
block|}
DECL|method|getThreadProfileName ()
specifier|protected
name|String
name|getThreadProfileName
parameter_list|()
block|{
return|return
name|Olingo2Constants
operator|.
name|THREAD_PROFILE_NAME
return|;
block|}
annotation|@
name|Override
DECL|method|configureProperties (Map<String, Object> options)
specifier|public
name|void
name|configureProperties
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|options
parameter_list|)
block|{
comment|// handle individual query params
name|parseQueryParams
argument_list|(
name|options
argument_list|)
expr_stmt|;
name|super
operator|.
name|configureProperties
argument_list|(
name|options
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|afterConfigureProperties ()
specifier|protected
name|void
name|afterConfigureProperties
parameter_list|()
block|{
comment|// set default inBody
if|if
condition|(
operator|!
operator|(
name|READ_METHOD
operator|.
name|equals
argument_list|(
name|methodName
argument_list|)
operator|||
name|DELETE_METHOD
operator|.
name|equals
argument_list|(
name|methodName
argument_list|)
operator|||
name|UREAD_METHOD
operator|.
name|equals
argument_list|(
name|methodName
argument_list|)
operator|)
operator|&&
name|inBody
operator|==
literal|null
condition|)
block|{
name|inBody
operator|=
name|DATA_PROPERTY
expr_stmt|;
block|}
name|createProxy
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getApiProxy (ApiMethod method, Map<String, Object> args)
specifier|public
specifier|synchronized
name|Object
name|getApiProxy
parameter_list|(
name|ApiMethod
name|method
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|args
parameter_list|)
block|{
return|return
name|apiProxy
operator|.
name|getOlingo2App
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getComponent ()
specifier|public
name|Olingo2Component
name|getComponent
parameter_list|()
block|{
return|return
operator|(
name|Olingo2Component
operator|)
name|super
operator|.
name|getComponent
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|apiProxy
operator|==
literal|null
condition|)
block|{
name|createProxy
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|apiProxy
operator|!=
literal|null
condition|)
block|{
comment|// close the apiProxy
name|getComponent
argument_list|()
operator|.
name|closeApiProxy
argument_list|(
name|apiProxy
argument_list|)
expr_stmt|;
name|apiProxy
operator|=
literal|null
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|interceptPropertyNames (Set<String> propertyNames)
specifier|public
name|void
name|interceptPropertyNames
parameter_list|(
name|Set
argument_list|<
name|String
argument_list|>
name|propertyNames
parameter_list|)
block|{
comment|// add edm, and responseHandler property names
comment|// edm is computed on first call to getApiProxy(), and responseHandler is provided by consumer and producer
if|if
condition|(
operator|!
name|DELETE_METHOD
operator|.
name|equals
argument_list|(
name|methodName
argument_list|)
condition|)
block|{
name|propertyNames
operator|.
name|add
argument_list|(
name|EDM_PROPERTY
argument_list|)
expr_stmt|;
block|}
name|propertyNames
operator|.
name|add
argument_list|(
name|RESPONSE_HANDLER_PROPERTY
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|interceptProperties (Map<String, Object> properties)
specifier|public
name|void
name|interceptProperties
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
parameter_list|)
block|{
comment|// read Edm if not set yet
name|properties
operator|.
name|put
argument_list|(
name|EDM_PROPERTY
argument_list|,
name|apiProxy
operator|.
name|getEdm
argument_list|()
argument_list|)
expr_stmt|;
comment|// handle keyPredicate
specifier|final
name|String
name|keyPredicate
init|=
operator|(
name|String
operator|)
name|properties
operator|.
name|get
argument_list|(
name|KEY_PREDICATE_PROPERTY
argument_list|)
decl_stmt|;
if|if
condition|(
name|keyPredicate
operator|!=
literal|null
condition|)
block|{
comment|// make sure a resource path is provided
specifier|final
name|String
name|resourcePath
init|=
operator|(
name|String
operator|)
name|properties
operator|.
name|get
argument_list|(
name|RESOURCE_PATH_PROPERTY
argument_list|)
decl_stmt|;
if|if
condition|(
name|resourcePath
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Resource path must be provided in endpoint URI, or URI parameter '"
operator|+
name|RESOURCE_PATH_PROPERTY
operator|+
literal|"', or exchange header '"
operator|+
name|Olingo2Constants
operator|.
name|PROPERTY_PREFIX
operator|+
name|RESOURCE_PATH_PROPERTY
operator|+
literal|"'"
argument_list|)
throw|;
block|}
comment|// append keyPredicate to dynamically create resource path
name|properties
operator|.
name|put
argument_list|(
name|RESOURCE_PATH_PROPERTY
argument_list|,
name|resourcePath
operator|+
literal|'('
operator|+
name|keyPredicate
operator|+
literal|')'
argument_list|)
expr_stmt|;
block|}
comment|// handle individual queryParams
name|parseQueryParams
argument_list|(
name|properties
argument_list|)
expr_stmt|;
block|}
DECL|method|createProxy ()
specifier|private
name|void
name|createProxy
parameter_list|()
block|{
name|apiProxy
operator|=
name|getComponent
argument_list|()
operator|.
name|createApiProxy
argument_list|(
name|getConfiguration
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|parseQueryParams (Map<String, Object> options)
specifier|private
name|void
name|parseQueryParams
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|options
parameter_list|)
block|{
comment|// extract non-endpoint properties as query params
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|queryParams
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|it
init|=
name|options
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
specifier|final
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
specifier|final
name|String
name|paramName
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|endpointPropertyNames
operator|.
name|contains
argument_list|(
name|paramName
argument_list|)
condition|)
block|{
comment|// add to query params
specifier|final
name|Object
name|value
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Null value for query parameter "
operator|+
name|paramName
argument_list|)
throw|;
block|}
name|queryParams
operator|.
name|put
argument_list|(
name|paramName
argument_list|,
name|value
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
comment|// remove entry from supplied options
name|it
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|queryParams
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|oldParams
init|=
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
operator|)
name|options
operator|.
name|get
argument_list|(
name|QUERY_PARAMS_PROPERTY
argument_list|)
decl_stmt|;
if|if
condition|(
name|oldParams
operator|==
literal|null
condition|)
block|{
comment|// set queryParams property
name|options
operator|.
name|put
argument_list|(
name|QUERY_PARAMS_PROPERTY
argument_list|,
name|queryParams
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// overwrite old params in supplied map
name|oldParams
operator|.
name|putAll
argument_list|(
name|queryParams
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

