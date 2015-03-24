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
name|lang
operator|.
name|reflect
operator|.
name|Field
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Modifier
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
name|Arrays
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
name|Set
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
name|Matcher
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|CamelContext
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
name|ComponentConfiguration
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
name|Endpoint
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
name|dto
operator|.
name|AbstractQueryRecordsBase
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
name|AbstractSObjectBase
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
name|OperationName
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
name|streaming
operator|.
name|SubscriptionHelper
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
name|impl
operator|.
name|UriEndpointComponent
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
name|EndpointCompleter
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
name|ReflectionHelper
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
name|ServiceHelper
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
name|jsse
operator|.
name|SSLContextParameters
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
name|Address
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
name|HttpClient
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
name|RedirectListener
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
name|security
operator|.
name|ProxyAuthorization
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
name|ssl
operator|.
name|SslContextFactory
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
comment|/**  * Represents the component that manages {@link SalesforceEndpoint}.  */
end_comment

begin_class
DECL|class|SalesforceComponent
specifier|public
class|class
name|SalesforceComponent
extends|extends
name|UriEndpointComponent
implements|implements
name|EndpointCompleter
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
name|SalesforceComponent
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|CONNECTION_TIMEOUT
specifier|private
specifier|static
specifier|final
name|int
name|CONNECTION_TIMEOUT
init|=
literal|60000
decl_stmt|;
DECL|field|RESPONSE_TIMEOUT
specifier|private
specifier|static
specifier|final
name|long
name|RESPONSE_TIMEOUT
init|=
literal|60000
decl_stmt|;
DECL|field|SOBJECT_NAME_PATTERN
specifier|private
specifier|static
specifier|final
name|Pattern
name|SOBJECT_NAME_PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"^.*[\\?&]sObjectName=([^&,]+).*$"
argument_list|)
decl_stmt|;
DECL|field|APEX_CALL_PREFIX
specifier|private
specifier|static
specifier|final
name|String
name|APEX_CALL_PREFIX
init|=
name|OperationName
operator|.
name|APEX_CALL
operator|.
name|value
argument_list|()
operator|+
literal|"/"
decl_stmt|;
DECL|field|loginConfig
specifier|private
name|SalesforceLoginConfig
name|loginConfig
decl_stmt|;
DECL|field|config
specifier|private
name|SalesforceEndpointConfig
name|config
decl_stmt|;
comment|// HTTP client parameters, map of property-name to value
DECL|field|httpClientProperties
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|httpClientProperties
decl_stmt|;
comment|// SSL parameters
DECL|field|sslContextParameters
specifier|private
name|SSLContextParameters
name|sslContextParameters
decl_stmt|;
comment|// Proxy host and port
DECL|field|httpProxyHost
specifier|private
name|String
name|httpProxyHost
decl_stmt|;
DECL|field|httpProxyPort
specifier|private
name|Integer
name|httpProxyPort
decl_stmt|;
comment|// Proxy basic authentication
DECL|field|httpProxyUsername
specifier|private
name|String
name|httpProxyUsername
decl_stmt|;
DECL|field|httpProxyPassword
specifier|private
name|String
name|httpProxyPassword
decl_stmt|;
comment|// DTO packages to scan
DECL|field|packages
specifier|private
name|String
index|[]
name|packages
decl_stmt|;
comment|// component state
DECL|field|httpClient
specifier|private
name|HttpClient
name|httpClient
decl_stmt|;
DECL|field|session
specifier|private
name|SalesforceSession
name|session
decl_stmt|;
DECL|field|classMap
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|classMap
decl_stmt|;
comment|// Lazily created helper for consumer endpoints
DECL|field|subscriptionHelper
specifier|private
name|SubscriptionHelper
name|subscriptionHelper
decl_stmt|;
DECL|method|SalesforceComponent ()
specifier|public
name|SalesforceComponent
parameter_list|()
block|{
name|super
argument_list|(
name|SalesforceEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|SalesforceComponent (CamelContext context)
specifier|public
name|SalesforceComponent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|,
name|SalesforceEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
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
comment|// get Operation from remaining URI
name|OperationName
name|operationName
init|=
literal|null
decl_stmt|;
name|String
name|topicName
init|=
literal|null
decl_stmt|;
name|String
name|apexUrl
init|=
literal|null
decl_stmt|;
try|try
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Creating endpoint for: {}"
argument_list|,
name|remaining
argument_list|)
expr_stmt|;
if|if
condition|(
name|remaining
operator|.
name|startsWith
argument_list|(
name|APEX_CALL_PREFIX
argument_list|)
condition|)
block|{
comment|// extract APEX URL
name|apexUrl
operator|=
name|remaining
operator|.
name|substring
argument_list|(
name|APEX_CALL_PREFIX
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|remaining
operator|=
name|OperationName
operator|.
name|APEX_CALL
operator|.
name|value
argument_list|()
expr_stmt|;
block|}
name|operationName
operator|=
name|OperationName
operator|.
name|fromValue
argument_list|(
name|remaining
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|ex
parameter_list|)
block|{
comment|// if its not an operation name, treat is as topic name for consumer endpoints
name|topicName
operator|=
name|remaining
expr_stmt|;
block|}
comment|// create endpoint config
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
name|SalesforceEndpointConfig
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|config
operator|.
name|getHttpClient
argument_list|()
operator|==
literal|null
condition|)
block|{
comment|// set the component's httpClient as default
name|config
operator|.
name|setHttpClient
argument_list|(
name|httpClient
argument_list|)
expr_stmt|;
block|}
comment|// create a deep copy and map parameters
specifier|final
name|SalesforceEndpointConfig
name|copy
init|=
name|config
operator|.
name|copy
argument_list|()
decl_stmt|;
name|setProperties
argument_list|(
name|copy
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
comment|// set apexUrl in endpoint config
if|if
condition|(
name|apexUrl
operator|!=
literal|null
condition|)
block|{
name|copy
operator|.
name|setApexUrl
argument_list|(
name|apexUrl
argument_list|)
expr_stmt|;
block|}
specifier|final
name|SalesforceEndpoint
name|endpoint
init|=
operator|new
name|SalesforceEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|copy
argument_list|,
name|operationName
argument_list|,
name|topicName
argument_list|)
decl_stmt|;
comment|// map remaining parameters to endpoint (specifically, synchronous)
name|setProperties
argument_list|(
name|endpoint
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
comment|// if operation is APEX call, map remaining parameters to query params
if|if
condition|(
name|operationName
operator|==
name|OperationName
operator|.
name|APEX_CALL
operator|&&
operator|!
name|parameters
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|queryParams
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|(
name|copy
operator|.
name|getApexQueryParams
argument_list|()
argument_list|)
decl_stmt|;
comment|// override component params with endpoint params
name|queryParams
operator|.
name|putAll
argument_list|(
name|parameters
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|clear
argument_list|()
expr_stmt|;
name|copy
operator|.
name|setApexQueryParams
argument_list|(
name|queryParams
argument_list|)
expr_stmt|;
block|}
return|return
name|endpoint
return|;
block|}
DECL|method|parsePackages ()
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|parsePackages
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|result
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|classes
init|=
name|getCamelContext
argument_list|()
operator|.
name|getPackageScanClassResolver
argument_list|()
operator|.
name|findImplementations
argument_list|(
name|AbstractSObjectBase
operator|.
name|class
argument_list|,
name|packages
argument_list|)
decl_stmt|;
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|aClass
range|:
name|classes
control|)
block|{
comment|// findImplementations also returns AbstractSObjectBase for some reason!!!
if|if
condition|(
name|AbstractSObjectBase
operator|.
name|class
operator|!=
name|aClass
condition|)
block|{
name|result
operator|.
name|put
argument_list|(
name|aClass
operator|.
name|getSimpleName
argument_list|()
argument_list|,
name|aClass
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|result
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
comment|// validate properties
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|loginConfig
argument_list|,
literal|"loginConfig"
argument_list|)
expr_stmt|;
comment|// create a Jetty HttpClient if not already set
if|if
condition|(
literal|null
operator|==
name|httpClient
condition|)
block|{
if|if
condition|(
name|config
operator|!=
literal|null
operator|&&
name|config
operator|.
name|getHttpClient
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|httpClient
operator|=
name|config
operator|.
name|getHttpClient
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|httpClient
operator|=
operator|new
name|HttpClient
argument_list|()
expr_stmt|;
comment|// default settings
name|httpClient
operator|.
name|setConnectorType
argument_list|(
name|HttpClient
operator|.
name|CONNECTOR_SELECT_CHANNEL
argument_list|)
expr_stmt|;
name|httpClient
operator|.
name|setConnectTimeout
argument_list|(
name|CONNECTION_TIMEOUT
argument_list|)
expr_stmt|;
name|httpClient
operator|.
name|setTimeout
argument_list|(
name|RESPONSE_TIMEOUT
argument_list|)
expr_stmt|;
block|}
block|}
comment|// set ssl context parameters
specifier|final
name|SSLContextParameters
name|contextParameters
init|=
name|sslContextParameters
operator|!=
literal|null
condition|?
name|sslContextParameters
else|:
operator|new
name|SSLContextParameters
argument_list|()
decl_stmt|;
specifier|final
name|SslContextFactory
name|sslContextFactory
init|=
name|httpClient
operator|.
name|getSslContextFactory
argument_list|()
decl_stmt|;
name|sslContextFactory
operator|.
name|setSslContext
argument_list|(
name|contextParameters
operator|.
name|createSSLContext
argument_list|()
argument_list|)
expr_stmt|;
comment|// set HTTP client parameters
if|if
condition|(
name|httpClientProperties
operator|!=
literal|null
operator|&&
operator|!
name|httpClientProperties
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|IntrospectionSupport
operator|.
name|setProperties
argument_list|(
name|getCamelContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
argument_list|,
name|httpClient
argument_list|,
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|(
name|httpClientProperties
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// set HTTP proxy settings
if|if
condition|(
name|this
operator|.
name|httpProxyHost
operator|!=
literal|null
operator|&&
name|httpProxyPort
operator|!=
literal|null
condition|)
block|{
name|httpClient
operator|.
name|setProxy
argument_list|(
operator|new
name|Address
argument_list|(
name|this
operator|.
name|httpProxyHost
argument_list|,
name|this
operator|.
name|httpProxyPort
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|this
operator|.
name|httpProxyUsername
operator|!=
literal|null
operator|&&
name|httpProxyPassword
operator|!=
literal|null
condition|)
block|{
name|httpClient
operator|.
name|setProxyAuthentication
argument_list|(
operator|new
name|ProxyAuthorization
argument_list|(
name|this
operator|.
name|httpProxyUsername
argument_list|,
name|this
operator|.
name|httpProxyPassword
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// add redirect listener to handle Salesforce redirects
comment|// this is ok to do since the RedirectListener is in the same classloader as Jetty client
name|String
name|listenerClass
init|=
name|RedirectListener
operator|.
name|class
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|httpClient
operator|.
name|getRegisteredListeners
argument_list|()
operator|==
literal|null
operator|||
operator|!
name|httpClient
operator|.
name|getRegisteredListeners
argument_list|()
operator|.
name|contains
argument_list|(
name|listenerClass
argument_list|)
condition|)
block|{
name|httpClient
operator|.
name|registerListener
argument_list|(
name|listenerClass
argument_list|)
expr_stmt|;
block|}
comment|// SalesforceSecurityListener can't be registered the same way
comment|// since Jetty HttpClient's Class.forName() can't see it
comment|// start the Jetty client to initialize thread pool, etc.
name|httpClient
operator|.
name|start
argument_list|()
expr_stmt|;
comment|// support restarts
if|if
condition|(
literal|null
operator|==
name|this
operator|.
name|session
condition|)
block|{
name|this
operator|.
name|session
operator|=
operator|new
name|SalesforceSession
argument_list|(
name|httpClient
argument_list|,
name|loginConfig
argument_list|)
expr_stmt|;
block|}
comment|// login at startup if lazyLogin is disabled
if|if
condition|(
operator|!
name|loginConfig
operator|.
name|isLazyLogin
argument_list|()
condition|)
block|{
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|session
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|packages
operator|!=
literal|null
operator|&&
name|packages
operator|.
name|length
operator|>
literal|0
condition|)
block|{
comment|// parse the packages to create SObject name to class map
name|classMap
operator|=
name|parsePackages
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Found {} generated classes in packages: {}"
argument_list|,
name|classMap
operator|.
name|size
argument_list|()
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|packages
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// use an empty map to avoid NPEs later
name|LOG
operator|.
name|warn
argument_list|(
literal|"Missing property packages, getSObject* operations will NOT work"
argument_list|)
expr_stmt|;
name|classMap
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|subscriptionHelper
operator|!=
literal|null
condition|)
block|{
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|subscriptionHelper
argument_list|)
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
name|classMap
operator|!=
literal|null
condition|)
block|{
name|classMap
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
try|try
block|{
if|if
condition|(
name|subscriptionHelper
operator|!=
literal|null
condition|)
block|{
comment|// shutdown all streaming connections
comment|// note that this is done in the component, and not in consumer
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|subscriptionHelper
argument_list|)
expr_stmt|;
name|subscriptionHelper
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|session
operator|!=
literal|null
operator|&&
name|session
operator|.
name|getAccessToken
argument_list|()
operator|!=
literal|null
condition|)
block|{
try|try
block|{
comment|// logout of Salesforce
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|session
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SalesforceException
name|ignored
parameter_list|)
block|{                 }
block|}
block|}
finally|finally
block|{
if|if
condition|(
name|httpClient
operator|!=
literal|null
condition|)
block|{
comment|// shutdown http client connections
name|httpClient
operator|.
name|stop
argument_list|()
expr_stmt|;
comment|// destroy http client if it was created by the component
if|if
condition|(
name|config
operator|.
name|getHttpClient
argument_list|()
operator|==
literal|null
condition|)
block|{
name|httpClient
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
name|httpClient
operator|=
literal|null
expr_stmt|;
block|}
block|}
block|}
DECL|method|getSubscriptionHelper ()
specifier|public
name|SubscriptionHelper
name|getSubscriptionHelper
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|subscriptionHelper
operator|==
literal|null
condition|)
block|{
comment|// lazily create subscription helper
name|subscriptionHelper
operator|=
operator|new
name|SubscriptionHelper
argument_list|(
name|this
argument_list|)
expr_stmt|;
comment|// also start the helper to connect to Salesforce
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|subscriptionHelper
argument_list|)
expr_stmt|;
block|}
return|return
name|subscriptionHelper
return|;
block|}
annotation|@
name|Override
DECL|method|completeEndpointPath (ComponentConfiguration configuration, String completionText)
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|completeEndpointPath
parameter_list|(
name|ComponentConfiguration
name|configuration
parameter_list|,
name|String
name|completionText
parameter_list|)
block|{
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|result
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
comment|// return operations names on empty completion text
specifier|final
name|boolean
name|empty
init|=
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|completionText
argument_list|)
decl_stmt|;
if|if
condition|(
name|empty
operator|||
name|completionText
operator|.
name|indexOf
argument_list|(
literal|'?'
argument_list|)
operator|==
operator|-
literal|1
condition|)
block|{
if|if
condition|(
name|empty
condition|)
block|{
name|completionText
operator|=
literal|""
expr_stmt|;
block|}
specifier|final
name|OperationName
index|[]
name|values
init|=
name|OperationName
operator|.
name|values
argument_list|()
decl_stmt|;
for|for
control|(
name|OperationName
name|val
range|:
name|values
control|)
block|{
specifier|final
name|String
name|strValue
init|=
name|val
operator|.
name|value
argument_list|()
decl_stmt|;
if|if
condition|(
name|strValue
operator|.
name|startsWith
argument_list|(
name|completionText
argument_list|)
condition|)
block|{
name|result
operator|.
name|add
argument_list|(
name|strValue
argument_list|)
expr_stmt|;
block|}
block|}
comment|// also add place holder for user defined push topic name for empty completionText
if|if
condition|(
name|empty
condition|)
block|{
name|result
operator|.
name|add
argument_list|(
literal|"[PushTopicName]"
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// handle package parameters
if|if
condition|(
name|completionText
operator|.
name|matches
argument_list|(
literal|"^.*[\\?&]sObjectName=$"
argument_list|)
condition|)
block|{
name|result
operator|.
name|addAll
argument_list|(
name|classMap
operator|.
name|keySet
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|completionText
operator|.
name|matches
argument_list|(
literal|"^.*[\\?&]sObjectFields=$"
argument_list|)
condition|)
block|{
comment|// find sObjectName from configuration or completionText
name|String
name|sObjectName
init|=
operator|(
name|String
operator|)
name|configuration
operator|.
name|getParameter
argument_list|(
literal|"sObjectName"
argument_list|)
decl_stmt|;
if|if
condition|(
name|sObjectName
operator|==
literal|null
condition|)
block|{
specifier|final
name|Matcher
name|matcher
init|=
name|SOBJECT_NAME_PATTERN
operator|.
name|matcher
argument_list|(
name|completionText
argument_list|)
decl_stmt|;
if|if
condition|(
name|matcher
operator|.
name|matches
argument_list|()
condition|)
block|{
name|sObjectName
operator|=
name|matcher
operator|.
name|group
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
comment|// return all fields of sObject
if|if
condition|(
name|sObjectName
operator|!=
literal|null
condition|)
block|{
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|aClass
init|=
name|classMap
operator|.
name|get
argument_list|(
name|sObjectName
argument_list|)
decl_stmt|;
name|ReflectionHelper
operator|.
name|doWithFields
argument_list|(
name|aClass
argument_list|,
operator|new
name|ReflectionHelper
operator|.
name|FieldCallback
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|doWith
parameter_list|(
name|Field
name|field
parameter_list|)
throws|throws
name|IllegalArgumentException
throws|,
name|IllegalAccessException
block|{
comment|// get non-static fields
if|if
condition|(
operator|(
name|field
operator|.
name|getModifiers
argument_list|()
operator|&
name|Modifier
operator|.
name|STATIC
operator|)
operator|==
literal|0
condition|)
block|{
name|result
operator|.
name|add
argument_list|(
name|field
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|completionText
operator|.
name|matches
argument_list|(
literal|"^.*[\\?&]sObjectClass=$"
argument_list|)
condition|)
block|{
for|for
control|(
name|Class
name|c
range|:
name|classMap
operator|.
name|values
argument_list|()
control|)
block|{
name|result
operator|.
name|add
argument_list|(
name|c
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// also add Query records classes
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|classes
init|=
name|getCamelContext
argument_list|()
operator|.
name|getPackageScanClassResolver
argument_list|()
operator|.
name|findImplementations
argument_list|(
name|AbstractQueryRecordsBase
operator|.
name|class
argument_list|,
name|packages
argument_list|)
decl_stmt|;
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|aClass
range|:
name|classes
control|)
block|{
comment|// findImplementations also returns AbstractQueryRecordsBase for some reason!!!
if|if
condition|(
name|AbstractQueryRecordsBase
operator|.
name|class
operator|!=
name|aClass
condition|)
block|{
name|result
operator|.
name|add
argument_list|(
name|aClass
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
return|return
name|result
return|;
block|}
DECL|method|getLoginConfig ()
specifier|public
name|SalesforceLoginConfig
name|getLoginConfig
parameter_list|()
block|{
return|return
name|loginConfig
return|;
block|}
DECL|method|setLoginConfig (SalesforceLoginConfig loginConfig)
specifier|public
name|void
name|setLoginConfig
parameter_list|(
name|SalesforceLoginConfig
name|loginConfig
parameter_list|)
block|{
name|this
operator|.
name|loginConfig
operator|=
name|loginConfig
expr_stmt|;
block|}
DECL|method|getConfig ()
specifier|public
name|SalesforceEndpointConfig
name|getConfig
parameter_list|()
block|{
return|return
name|config
return|;
block|}
DECL|method|setConfig (SalesforceEndpointConfig config)
specifier|public
name|void
name|setConfig
parameter_list|(
name|SalesforceEndpointConfig
name|config
parameter_list|)
block|{
name|this
operator|.
name|config
operator|=
name|config
expr_stmt|;
block|}
DECL|method|getHttpClientProperties ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getHttpClientProperties
parameter_list|()
block|{
return|return
name|httpClientProperties
return|;
block|}
DECL|method|setHttpClientProperties (Map<String, Object> httpClientProperties)
specifier|public
name|void
name|setHttpClientProperties
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|httpClientProperties
parameter_list|)
block|{
name|this
operator|.
name|httpClientProperties
operator|=
name|httpClientProperties
expr_stmt|;
block|}
DECL|method|getSslContextParameters ()
specifier|public
name|SSLContextParameters
name|getSslContextParameters
parameter_list|()
block|{
return|return
name|sslContextParameters
return|;
block|}
DECL|method|setSslContextParameters (SSLContextParameters sslContextParameters)
specifier|public
name|void
name|setSslContextParameters
parameter_list|(
name|SSLContextParameters
name|sslContextParameters
parameter_list|)
block|{
name|this
operator|.
name|sslContextParameters
operator|=
name|sslContextParameters
expr_stmt|;
block|}
DECL|method|getHttpProxyHost ()
specifier|public
name|String
name|getHttpProxyHost
parameter_list|()
block|{
return|return
name|httpProxyHost
return|;
block|}
DECL|method|setHttpProxyHost (String httpProxyHost)
specifier|public
name|void
name|setHttpProxyHost
parameter_list|(
name|String
name|httpProxyHost
parameter_list|)
block|{
name|this
operator|.
name|httpProxyHost
operator|=
name|httpProxyHost
expr_stmt|;
block|}
DECL|method|getHttpProxyPort ()
specifier|public
name|Integer
name|getHttpProxyPort
parameter_list|()
block|{
return|return
name|httpProxyPort
return|;
block|}
DECL|method|setHttpProxyPort (Integer httpProxyPort)
specifier|public
name|void
name|setHttpProxyPort
parameter_list|(
name|Integer
name|httpProxyPort
parameter_list|)
block|{
name|this
operator|.
name|httpProxyPort
operator|=
name|httpProxyPort
expr_stmt|;
block|}
DECL|method|getHttpProxyUsername ()
specifier|public
name|String
name|getHttpProxyUsername
parameter_list|()
block|{
return|return
name|httpProxyUsername
return|;
block|}
DECL|method|setHttpProxyUsername (String httpProxyUsername)
specifier|public
name|void
name|setHttpProxyUsername
parameter_list|(
name|String
name|httpProxyUsername
parameter_list|)
block|{
name|this
operator|.
name|httpProxyUsername
operator|=
name|httpProxyUsername
expr_stmt|;
block|}
DECL|method|getHttpProxyPassword ()
specifier|public
name|String
name|getHttpProxyPassword
parameter_list|()
block|{
return|return
name|httpProxyPassword
return|;
block|}
DECL|method|setHttpProxyPassword (String httpProxyPassword)
specifier|public
name|void
name|setHttpProxyPassword
parameter_list|(
name|String
name|httpProxyPassword
parameter_list|)
block|{
name|this
operator|.
name|httpProxyPassword
operator|=
name|httpProxyPassword
expr_stmt|;
block|}
DECL|method|getPackages ()
specifier|public
name|String
index|[]
name|getPackages
parameter_list|()
block|{
return|return
name|packages
return|;
block|}
DECL|method|setPackages (String[] packages)
specifier|public
name|void
name|setPackages
parameter_list|(
name|String
index|[]
name|packages
parameter_list|)
block|{
name|this
operator|.
name|packages
operator|=
name|packages
expr_stmt|;
block|}
DECL|method|setPackages (String packages)
specifier|public
name|void
name|setPackages
parameter_list|(
name|String
name|packages
parameter_list|)
block|{
comment|// split using comma
if|if
condition|(
name|packages
operator|!=
literal|null
condition|)
block|{
name|setPackages
argument_list|(
name|packages
operator|.
name|split
argument_list|(
literal|","
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getSession ()
specifier|public
name|SalesforceSession
name|getSession
parameter_list|()
block|{
return|return
name|session
return|;
block|}
DECL|method|getClassMap ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|getClassMap
parameter_list|()
block|{
return|return
name|classMap
return|;
block|}
block|}
end_class

end_unit

