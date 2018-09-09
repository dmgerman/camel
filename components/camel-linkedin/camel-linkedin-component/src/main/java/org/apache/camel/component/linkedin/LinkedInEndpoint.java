begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.linkedin
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|linkedin
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
name|linkedin
operator|.
name|api
operator|.
name|CommentsResource
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
name|linkedin
operator|.
name|api
operator|.
name|CompaniesResource
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
name|linkedin
operator|.
name|api
operator|.
name|EnumQueryParamConverterProvider
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
name|linkedin
operator|.
name|api
operator|.
name|GroupsResource
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
name|linkedin
operator|.
name|api
operator|.
name|JobsResource
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
name|linkedin
operator|.
name|api
operator|.
name|LinkedInOAuthRequestFilter
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
name|linkedin
operator|.
name|api
operator|.
name|PeopleResource
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
name|linkedin
operator|.
name|api
operator|.
name|PostsResource
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
name|linkedin
operator|.
name|api
operator|.
name|SearchResource
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
name|linkedin
operator|.
name|internal
operator|.
name|LinkedInApiCollection
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
name|linkedin
operator|.
name|internal
operator|.
name|LinkedInApiName
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
name|linkedin
operator|.
name|internal
operator|.
name|LinkedInConstants
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
name|linkedin
operator|.
name|internal
operator|.
name|LinkedInPropertiesHelper
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|Bus
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|BusFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|client
operator|.
name|AbstractClient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|client
operator|.
name|JAXRSClientFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|client
operator|.
name|JAXRSClientFactoryBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|client
operator|.
name|WebClient
import|;
end_import

begin_comment
comment|/**  * The linkedin component is used for retrieving LinkedIn user profiles, connections, companies, groups, posts, etc.  */
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
literal|"linkedin"
argument_list|,
name|title
operator|=
literal|"Linkedin"
argument_list|,
name|syntax
operator|=
literal|"linkedin:apiName/methodName"
argument_list|,
name|label
operator|=
literal|"api,cloud,social"
argument_list|,
name|consumerClass
operator|=
name|LinkedInConsumer
operator|.
name|class
argument_list|,
name|lenientProperties
operator|=
literal|true
argument_list|)
DECL|class|LinkedInEndpoint
specifier|public
class|class
name|LinkedInEndpoint
extends|extends
name|AbstractApiEndpoint
argument_list|<
name|LinkedInApiName
argument_list|,
name|LinkedInConfiguration
argument_list|>
block|{
DECL|field|FIELDS_OPTION
specifier|protected
specifier|static
specifier|final
name|String
name|FIELDS_OPTION
init|=
literal|"fields"
decl_stmt|;
DECL|field|DEFAULT_FIELDS_SELECTOR
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_FIELDS_SELECTOR
init|=
literal|""
decl_stmt|;
annotation|@
name|UriParam
DECL|field|configuration
specifier|private
specifier|final
name|LinkedInConfiguration
name|configuration
decl_stmt|;
comment|// OAuth request filter
DECL|field|requestFilter
specifier|private
name|LinkedInOAuthRequestFilter
name|requestFilter
decl_stmt|;
comment|// Resource API proxy
DECL|field|resourceProxy
specifier|private
name|Object
name|resourceProxy
decl_stmt|;
DECL|method|LinkedInEndpoint (String uri, LinkedInComponent component, LinkedInApiName apiName, String methodName, LinkedInConfiguration endpointConfiguration)
specifier|public
name|LinkedInEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|LinkedInComponent
name|component
parameter_list|,
name|LinkedInApiName
name|apiName
parameter_list|,
name|String
name|methodName
parameter_list|,
name|LinkedInConfiguration
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
name|LinkedInApiCollection
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
name|LinkedInProducer
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
specifier|final
name|LinkedInConsumer
name|consumer
init|=
operator|new
name|LinkedInConsumer
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
name|LinkedInConfiguration
argument_list|>
name|getPropertiesHelper
parameter_list|()
block|{
return|return
name|LinkedInPropertiesHelper
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
name|LinkedInConstants
operator|.
name|THREAD_PROFILE_NAME
return|;
block|}
annotation|@
name|Override
DECL|method|afterConfigureProperties ()
specifier|protected
name|void
name|afterConfigureProperties
parameter_list|()
block|{
name|createProxy
argument_list|()
expr_stmt|;
block|}
comment|// create API proxy, set connection properties, etc.
DECL|method|createProxy ()
specifier|private
name|void
name|createProxy
parameter_list|()
block|{
comment|// create endpoint filter or get shared filter if configuration values are same as component
name|requestFilter
operator|=
name|getComponent
argument_list|()
operator|.
name|getRequestFilter
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|proxyClass
decl_stmt|;
switch|switch
condition|(
name|apiName
condition|)
block|{
case|case
name|COMMENTS
case|:
name|proxyClass
operator|=
name|CommentsResource
operator|.
name|class
expr_stmt|;
break|break;
case|case
name|COMPANIES
case|:
name|proxyClass
operator|=
name|CompaniesResource
operator|.
name|class
expr_stmt|;
break|break;
case|case
name|GROUPS
case|:
name|proxyClass
operator|=
name|GroupsResource
operator|.
name|class
expr_stmt|;
break|break;
case|case
name|JOBS
case|:
name|proxyClass
operator|=
name|JobsResource
operator|.
name|class
expr_stmt|;
break|break;
case|case
name|PEOPLE
case|:
name|proxyClass
operator|=
name|PeopleResource
operator|.
name|class
expr_stmt|;
break|break;
case|case
name|POSTS
case|:
name|proxyClass
operator|=
name|PostsResource
operator|.
name|class
expr_stmt|;
break|break;
case|case
name|SEARCH
case|:
name|proxyClass
operator|=
name|SearchResource
operator|.
name|class
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid API name "
operator|+
name|apiName
argument_list|)
throw|;
block|}
comment|// create endpoint proxy
name|Bus
name|bus
init|=
name|BusFactory
operator|.
name|getThreadDefaultBus
argument_list|()
decl_stmt|;
name|bus
operator|.
name|setProperty
argument_list|(
literal|"allow.empty.path.template.value"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|resourceProxy
operator|=
name|JAXRSClientFactory
operator|.
name|create
argument_list|(
name|LinkedInOAuthRequestFilter
operator|.
name|BASE_ADDRESS
argument_list|,
name|proxyClass
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|Object
index|[]
block|{
name|requestFilter
block|,
operator|new
name|EnumQueryParamConverterProvider
argument_list|()
block|}
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getApiProxy (ApiMethod method, Map<String, Object> args)
specifier|public
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
name|resourceProxy
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
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
if|if
condition|(
name|resourceProxy
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
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
if|if
condition|(
name|resourceProxy
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|WebClient
operator|.
name|client
argument_list|(
name|resourceProxy
argument_list|)
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Error closing LinkedIn REST proxy: {}"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
name|resourceProxy
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|requestFilter
operator|!=
literal|null
condition|)
block|{
name|getComponent
argument_list|()
operator|.
name|closeRequestFilter
argument_list|(
name|requestFilter
argument_list|)
expr_stmt|;
name|requestFilter
operator|=
literal|null
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|getComponent ()
specifier|public
name|LinkedInComponent
name|getComponent
parameter_list|()
block|{
return|return
operator|(
name|LinkedInComponent
operator|)
name|super
operator|.
name|getComponent
argument_list|()
return|;
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
if|if
condition|(
operator|!
name|properties
operator|.
name|containsKey
argument_list|(
name|FIELDS_OPTION
argument_list|)
condition|)
block|{
name|properties
operator|.
name|put
argument_list|(
name|FIELDS_OPTION
argument_list|,
name|DEFAULT_FIELDS_SELECTOR
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

