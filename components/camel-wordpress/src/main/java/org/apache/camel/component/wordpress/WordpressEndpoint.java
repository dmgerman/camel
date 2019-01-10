begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.wordpress
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|wordpress
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|stream
operator|.
name|Collectors
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
name|wordpress
operator|.
name|api
operator|.
name|WordpressAPIConfiguration
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
name|wordpress
operator|.
name|api
operator|.
name|WordpressServiceProvider
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
name|wordpress
operator|.
name|api
operator|.
name|auth
operator|.
name|WordpressBasicAuthentication
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
name|wordpress
operator|.
name|api
operator|.
name|model
operator|.
name|SearchCriteria
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
name|wordpress
operator|.
name|consumer
operator|.
name|WordpressPostConsumer
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
name|wordpress
operator|.
name|consumer
operator|.
name|WordpressUserConsumer
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
name|wordpress
operator|.
name|producer
operator|.
name|WordpressPostProducer
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
name|wordpress
operator|.
name|producer
operator|.
name|WordpressUserProducer
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
name|wordpress
operator|.
name|proxy
operator|.
name|WordpressOperationType
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
name|support
operator|.
name|DefaultEndpoint
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
name|Metadata
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
name|spi
operator|.
name|UriPath
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
name|support
operator|.
name|EndpointHelper
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
name|support
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

begin_comment
comment|/**  * Integrates Camel with Wordpress.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.21.0"
argument_list|,
name|scheme
operator|=
literal|"wordpress"
argument_list|,
name|title
operator|=
literal|"Wordpress"
argument_list|,
name|syntax
operator|=
literal|"wordpress:operationDetail"
argument_list|,
name|label
operator|=
literal|"cms"
argument_list|)
DECL|class|WordpressEndpoint
specifier|public
class|class
name|WordpressEndpoint
extends|extends
name|DefaultEndpoint
block|{
DECL|field|ENDPOINT_SERVICE_POST
specifier|public
specifier|static
specifier|final
name|String
name|ENDPOINT_SERVICE_POST
init|=
literal|"post, user"
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|description
operator|=
literal|"The endpoint operation."
argument_list|,
name|enums
operator|=
name|ENDPOINT_SERVICE_POST
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|operation
specifier|private
name|String
name|operation
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|description
operator|=
literal|"The second part of an endpoint operation. Needed only when endpoint semantic is not enough, like wordpress:post:delete"
argument_list|,
name|enums
operator|=
literal|"delete"
argument_list|)
DECL|field|operationDetail
specifier|private
name|String
name|operationDetail
decl_stmt|;
annotation|@
name|UriParam
DECL|field|config
specifier|private
name|WordpressComponentConfiguration
name|config
decl_stmt|;
DECL|method|WordpressEndpoint (String uri, WordpressComponent component, WordpressComponentConfiguration configuration)
specifier|public
name|WordpressEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|WordpressComponent
name|component
parameter_list|,
name|WordpressComponentConfiguration
name|configuration
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|config
operator|=
name|configuration
expr_stmt|;
block|}
DECL|method|getConfig ()
specifier|public
name|WordpressComponentConfiguration
name|getConfig
parameter_list|()
block|{
return|return
name|config
return|;
block|}
DECL|method|getOperation ()
specifier|public
name|String
name|getOperation
parameter_list|()
block|{
return|return
name|operation
return|;
block|}
DECL|method|setOperation (String operation)
specifier|public
name|void
name|setOperation
parameter_list|(
name|String
name|operation
parameter_list|)
block|{
name|this
operator|.
name|operation
operator|=
name|operation
expr_stmt|;
block|}
DECL|method|getOperationDetail ()
specifier|public
name|String
name|getOperationDetail
parameter_list|()
block|{
return|return
name|operationDetail
return|;
block|}
DECL|method|setOperationDetail (String operationDetail)
specifier|public
name|void
name|setOperationDetail
parameter_list|(
name|String
name|operationDetail
parameter_list|)
block|{
name|this
operator|.
name|operationDetail
operator|=
name|operationDetail
expr_stmt|;
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
switch|switch
condition|(
name|WordpressOperationType
operator|.
name|valueOf
argument_list|(
name|operation
argument_list|)
condition|)
block|{
case|case
name|post
case|:
return|return
operator|new
name|WordpressPostProducer
argument_list|(
name|this
argument_list|)
return|;
case|case
name|user
case|:
return|return
operator|new
name|WordpressUserProducer
argument_list|(
name|this
argument_list|)
return|;
default|default:
break|break;
block|}
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Operation '%s' not supported."
argument_list|,
name|operation
argument_list|)
argument_list|)
throw|;
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
switch|switch
condition|(
name|WordpressOperationType
operator|.
name|valueOf
argument_list|(
name|operation
argument_list|)
condition|)
block|{
case|case
name|post
case|:
return|return
operator|new
name|WordpressPostConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
return|;
case|case
name|user
case|:
return|return
operator|new
name|WordpressUserConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
return|;
default|default:
break|break;
block|}
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Operation '%s' not supported."
argument_list|,
name|operation
argument_list|)
argument_list|)
throw|;
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
name|super
operator|.
name|configureProperties
argument_list|(
name|options
argument_list|)
expr_stmt|;
comment|// set configuration properties first
try|try
block|{
if|if
condition|(
name|config
operator|==
literal|null
condition|)
block|{
name|config
operator|=
operator|new
name|WordpressComponentConfiguration
argument_list|()
expr_stmt|;
block|}
name|EndpointHelper
operator|.
name|setReferenceProperties
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|config
argument_list|,
name|options
argument_list|)
expr_stmt|;
name|EndpointHelper
operator|.
name|setProperties
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|config
argument_list|,
name|options
argument_list|)
expr_stmt|;
if|if
condition|(
name|config
operator|.
name|getSearchCriteria
argument_list|()
operator|==
literal|null
condition|)
block|{
specifier|final
name|SearchCriteria
name|searchCriteria
init|=
name|WordpressOperationType
operator|.
name|valueOf
argument_list|(
name|operation
argument_list|)
operator|.
name|getCriteriaType
argument_list|()
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|criteriaOptions
init|=
name|IntrospectionSupport
operator|.
name|extractProperties
argument_list|(
name|options
argument_list|,
literal|"criteria."
argument_list|)
decl_stmt|;
comment|// any property that has a "," should be a List
name|criteriaOptions
operator|=
name|criteriaOptions
operator|.
name|entrySet
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toMap
argument_list|(
name|Map
operator|.
name|Entry
operator|::
name|getKey
argument_list|,
name|e
lambda|->
block|{
if|if
condition|(
name|e
operator|!=
literal|null
operator|&&
name|e
operator|.
name|toString
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|","
argument_list|)
operator|>
operator|-
literal|1
condition|)
block|{
return|return
name|Arrays
operator|.
name|asList
argument_list|(
name|e
operator|.
name|toString
argument_list|()
operator|.
name|split
argument_list|(
literal|","
argument_list|)
argument_list|)
return|;
block|}
return|return
name|e
operator|.
name|getValue
argument_list|()
return|;
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|IntrospectionSupport
operator|.
name|setProperties
argument_list|(
name|searchCriteria
argument_list|,
name|criteriaOptions
argument_list|)
expr_stmt|;
name|config
operator|.
name|setSearchCriteria
argument_list|(
name|searchCriteria
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
comment|// validate configuration
name|config
operator|.
name|validate
argument_list|()
expr_stmt|;
name|this
operator|.
name|initServiceProvider
argument_list|()
expr_stmt|;
block|}
DECL|method|initServiceProvider ()
specifier|private
name|void
name|initServiceProvider
parameter_list|()
block|{
specifier|final
name|WordpressAPIConfiguration
name|apiConfiguration
init|=
operator|new
name|WordpressAPIConfiguration
argument_list|(
name|config
operator|.
name|getUrl
argument_list|()
argument_list|,
name|config
operator|.
name|getApiVersion
argument_list|()
argument_list|)
decl_stmt|;
comment|// basic auth
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|config
operator|.
name|getUser
argument_list|()
argument_list|)
condition|)
block|{
name|apiConfiguration
operator|.
name|setAuthentication
argument_list|(
operator|new
name|WordpressBasicAuthentication
argument_list|(
name|config
operator|.
name|getUser
argument_list|()
argument_list|,
name|config
operator|.
name|getPassword
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|WordpressServiceProvider
operator|.
name|getInstance
argument_list|()
operator|.
name|init
argument_list|(
name|apiConfiguration
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

