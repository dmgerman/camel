begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util.component
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|component
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
name|Collections
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
name|HashSet
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
name|ExecutorService
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
name|Component
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
name|ExecutorServiceManager
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
name|ThreadPoolProfile
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
name|util
operator|.
name|ObjectHelper
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
comment|/**  * Abstract base class for API Component Endpoints.  */
end_comment

begin_class
DECL|class|AbstractApiEndpoint
specifier|public
specifier|abstract
class|class
name|AbstractApiEndpoint
parameter_list|<
name|E
extends|extends
name|ApiName
parameter_list|,
name|T
parameter_list|>
extends|extends
name|DefaultEndpoint
implements|implements
name|PropertyNamesInterceptor
implements|,
name|PropertiesInterceptor
block|{
comment|// thread pool executor with Endpoint Class name as keys
DECL|field|executorServiceMap
specifier|private
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|ExecutorService
argument_list|>
name|executorServiceMap
init|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|ExecutorService
argument_list|>
argument_list|()
decl_stmt|;
comment|// logger
DECL|field|log
specifier|protected
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
comment|// API name
DECL|field|apiName
specifier|protected
specifier|final
name|E
name|apiName
decl_stmt|;
comment|// API method name
DECL|field|methodName
specifier|protected
specifier|final
name|String
name|methodName
decl_stmt|;
comment|// API method helper
DECL|field|methodHelper
specifier|protected
specifier|final
name|ApiMethodHelper
argument_list|<
name|?
extends|extends
name|ApiMethod
argument_list|>
name|methodHelper
decl_stmt|;
comment|// endpoint configuration
DECL|field|configuration
specifier|protected
specifier|final
name|T
name|configuration
decl_stmt|;
comment|// property name for Exchange 'In' message body
annotation|@
name|UriParam
argument_list|(
name|description
operator|=
literal|"Sets the name of a parameter to be passed in the exchange In Body"
argument_list|)
DECL|field|inBody
specifier|protected
name|String
name|inBody
decl_stmt|;
comment|// candidate methods based on method name and endpoint configuration
DECL|field|candidates
specifier|private
name|List
argument_list|<
name|ApiMethod
argument_list|>
name|candidates
decl_stmt|;
comment|// cached Executor service
DECL|field|executorService
specifier|private
name|ExecutorService
name|executorService
decl_stmt|;
comment|// cached property names and values
DECL|field|endpointPropertyNames
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|endpointPropertyNames
decl_stmt|;
DECL|field|endpointProperties
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|endpointProperties
decl_stmt|;
DECL|method|AbstractApiEndpoint (String endpointUri, Component component, E apiName, String methodName, ApiMethodHelper<? extends ApiMethod> methodHelper, T endpointConfiguration)
specifier|public
name|AbstractApiEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|,
name|E
name|apiName
parameter_list|,
name|String
name|methodName
parameter_list|,
name|ApiMethodHelper
argument_list|<
name|?
extends|extends
name|ApiMethod
argument_list|>
name|methodHelper
parameter_list|,
name|T
name|endpointConfiguration
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|apiName
operator|=
name|apiName
expr_stmt|;
name|this
operator|.
name|methodName
operator|=
name|methodName
expr_stmt|;
name|this
operator|.
name|methodHelper
operator|=
name|methodHelper
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|endpointConfiguration
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
comment|/**      * Returns generated helper that extends {@link ApiMethodPropertiesHelper} to work with API properties.      * @return properties helper.      */
DECL|method|getPropertiesHelper ()
specifier|protected
specifier|abstract
name|ApiMethodPropertiesHelper
argument_list|<
name|T
argument_list|>
name|getPropertiesHelper
parameter_list|()
function_decl|;
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
name|T
name|configuration
init|=
name|getConfiguration
argument_list|()
decl_stmt|;
name|EndpointHelper
operator|.
name|setReferenceProperties
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|configuration
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
name|configuration
argument_list|,
name|options
argument_list|)
expr_stmt|;
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
argument_list|)
throw|;
block|}
comment|// validate and initialize state
name|initState
argument_list|()
expr_stmt|;
name|afterConfigureProperties
argument_list|()
expr_stmt|;
block|}
comment|/**      * Initialize proxies, create server connections, etc. after endpoint properties have been configured.      */
DECL|method|afterConfigureProperties ()
specifier|protected
specifier|abstract
name|void
name|afterConfigureProperties
parameter_list|()
function_decl|;
comment|/**      * Initialize endpoint state, including endpoint arguments, find candidate methods, etc.      */
DECL|method|initState ()
specifier|private
name|void
name|initState
parameter_list|()
block|{
comment|// compute endpoint property names and values
name|this
operator|.
name|endpointPropertyNames
operator|=
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
name|getPropertiesHelper
argument_list|()
operator|.
name|getEndpointPropertyNames
argument_list|(
name|configuration
argument_list|)
argument_list|)
expr_stmt|;
specifier|final
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|getPropertiesHelper
argument_list|()
operator|.
name|getEndpointProperties
argument_list|(
name|configuration
argument_list|,
name|properties
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpointProperties
operator|=
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|properties
argument_list|)
expr_stmt|;
comment|// get endpoint property names
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|arguments
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|arguments
operator|.
name|addAll
argument_list|(
name|endpointPropertyNames
argument_list|)
expr_stmt|;
comment|// add inBody argument for producers
if|if
condition|(
name|inBody
operator|!=
literal|null
condition|)
block|{
name|arguments
operator|.
name|add
argument_list|(
name|inBody
argument_list|)
expr_stmt|;
block|}
name|interceptPropertyNames
argument_list|(
name|arguments
argument_list|)
expr_stmt|;
specifier|final
name|String
index|[]
name|argNames
init|=
name|arguments
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|arguments
operator|.
name|size
argument_list|()
index|]
argument_list|)
decl_stmt|;
comment|// create a list of candidate methods
name|candidates
operator|=
operator|new
name|ArrayList
argument_list|<
name|ApiMethod
argument_list|>
argument_list|()
expr_stmt|;
name|candidates
operator|.
name|addAll
argument_list|(
name|methodHelper
operator|.
name|getCandidateMethods
argument_list|(
name|methodName
argument_list|,
name|argNames
argument_list|)
argument_list|)
expr_stmt|;
name|candidates
operator|=
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|candidates
argument_list|)
expr_stmt|;
comment|// error if there are no candidates
if|if
condition|(
name|candidates
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"No matching method for %s/%s, with arguments %s"
argument_list|,
name|apiName
operator|.
name|getName
argument_list|()
argument_list|,
name|methodName
argument_list|,
name|arguments
argument_list|)
argument_list|)
throw|;
block|}
comment|// log missing/extra properties for debugging
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|missing
init|=
name|methodHelper
operator|.
name|getMissingProperties
argument_list|(
name|methodName
argument_list|,
name|arguments
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|missing
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Method {} could use one or more properties from {}"
argument_list|,
name|methodName
argument_list|,
name|missing
argument_list|)
expr_stmt|;
block|}
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
comment|// do nothing by default
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
comment|// do nothing by default
block|}
comment|/**      * Returns endpoint configuration object.      * One of the generated *EndpointConfiguration classes that extends component configuration class.      *      * @return endpoint configuration object      */
DECL|method|getConfiguration ()
specifier|public
specifier|final
name|T
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
comment|/**      * Returns API name.      * @return apiName property.      */
DECL|method|getApiName ()
specifier|public
specifier|final
name|E
name|getApiName
parameter_list|()
block|{
return|return
name|apiName
return|;
block|}
comment|/**      * Returns method name.      * @return methodName property.      */
DECL|method|getMethodName ()
specifier|public
specifier|final
name|String
name|getMethodName
parameter_list|()
block|{
return|return
name|methodName
return|;
block|}
comment|/**      * Returns method helper.      * @return methodHelper property.      */
DECL|method|getMethodHelper ()
specifier|public
specifier|final
name|ApiMethodHelper
argument_list|<
name|?
extends|extends
name|ApiMethod
argument_list|>
name|getMethodHelper
parameter_list|()
block|{
return|return
name|methodHelper
return|;
block|}
comment|/**      * Returns candidate methods for this endpoint.      * @return list of candidate methods.      */
DECL|method|getCandidates ()
specifier|public
specifier|final
name|List
argument_list|<
name|ApiMethod
argument_list|>
name|getCandidates
parameter_list|()
block|{
return|return
name|candidates
return|;
block|}
comment|/**      * Returns name of parameter passed in the exchange In Body.      * @return inBody property.      */
DECL|method|getInBody ()
specifier|public
specifier|final
name|String
name|getInBody
parameter_list|()
block|{
return|return
name|inBody
return|;
block|}
comment|/**      * Sets the name of a parameter to be passed in the exchange In Body.      * @param inBody parameter name      * @throws IllegalArgumentException for invalid parameter name.      */
DECL|method|setInBody (String inBody)
specifier|public
specifier|final
name|void
name|setInBody
parameter_list|(
name|String
name|inBody
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
comment|// validate property name
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|inBody
argument_list|,
literal|"inBody"
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|getPropertiesHelper
argument_list|()
operator|.
name|getValidEndpointProperties
argument_list|(
name|getConfiguration
argument_list|()
argument_list|)
operator|.
name|contains
argument_list|(
name|inBody
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unknown property "
operator|+
name|inBody
argument_list|)
throw|;
block|}
name|this
operator|.
name|inBody
operator|=
name|inBody
expr_stmt|;
block|}
DECL|method|getEndpointPropertyNames ()
specifier|public
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|getEndpointPropertyNames
parameter_list|()
block|{
return|return
name|endpointPropertyNames
return|;
block|}
DECL|method|getEndpointProperties ()
specifier|public
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getEndpointProperties
parameter_list|()
block|{
return|return
name|endpointProperties
return|;
block|}
comment|/**      * Returns an instance of an API Proxy based on apiName, method and args.      * Called by {@link AbstractApiConsumer} or {@link AbstractApiProducer}.      *      * @param method method about to be invoked      * @param args method arguments      * @return a Java object that implements the method to be invoked.      * @see AbstractApiProducer      * @see AbstractApiConsumer      */
DECL|method|getApiProxy (ApiMethod method, Map<String, Object> args)
specifier|public
specifier|abstract
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
function_decl|;
DECL|method|getExecutorService ( Class<? extends AbstractApiEndpoint> endpointClass, CamelContext context, String threadProfileName)
specifier|private
specifier|static
name|ExecutorService
name|getExecutorService
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|AbstractApiEndpoint
argument_list|>
name|endpointClass
parameter_list|,
name|CamelContext
name|context
parameter_list|,
name|String
name|threadProfileName
parameter_list|)
block|{
comment|// lookup executorService for extending class name
specifier|final
name|String
name|endpointClassName
init|=
name|endpointClass
operator|.
name|getName
argument_list|()
decl_stmt|;
name|ExecutorService
name|executorService
init|=
name|executorServiceMap
operator|.
name|get
argument_list|(
name|endpointClassName
argument_list|)
decl_stmt|;
comment|// CamelContext will shutdown thread pool when it shutdown so we can
comment|// lazy create it on demand
comment|// but in case of hot-deploy or the likes we need to be able to
comment|// re-create it (its a shared static instance)
if|if
condition|(
name|executorService
operator|==
literal|null
operator|||
name|executorService
operator|.
name|isTerminated
argument_list|()
operator|||
name|executorService
operator|.
name|isShutdown
argument_list|()
condition|)
block|{
specifier|final
name|ExecutorServiceManager
name|manager
init|=
name|context
operator|.
name|getExecutorServiceManager
argument_list|()
decl_stmt|;
comment|// try to lookup a pool first based on profile
name|ThreadPoolProfile
name|poolProfile
init|=
name|manager
operator|.
name|getThreadPoolProfile
argument_list|(
name|threadProfileName
argument_list|)
decl_stmt|;
if|if
condition|(
name|poolProfile
operator|==
literal|null
condition|)
block|{
name|poolProfile
operator|=
name|manager
operator|.
name|getDefaultThreadPoolProfile
argument_list|()
expr_stmt|;
block|}
comment|// create a new pool using the custom or default profile
name|executorService
operator|=
name|manager
operator|.
name|newScheduledThreadPool
argument_list|(
name|endpointClass
argument_list|,
name|threadProfileName
argument_list|,
name|poolProfile
argument_list|)
expr_stmt|;
name|executorServiceMap
operator|.
name|put
argument_list|(
name|endpointClassName
argument_list|,
name|executorService
argument_list|)
expr_stmt|;
block|}
return|return
name|executorService
return|;
block|}
DECL|method|getExecutorService ()
specifier|public
specifier|final
name|ExecutorService
name|getExecutorService
parameter_list|()
block|{
if|if
condition|(
name|this
operator|.
name|executorService
operator|==
literal|null
condition|)
block|{
comment|// synchronize on class to avoid creating duplicate class level executors
synchronized|synchronized
init|(
name|getClass
argument_list|()
init|)
block|{
name|this
operator|.
name|executorService
operator|=
name|getExecutorService
argument_list|(
name|getClass
argument_list|()
argument_list|,
name|getCamelContext
argument_list|()
argument_list|,
name|getThreadProfileName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|this
operator|.
name|executorService
return|;
block|}
comment|/**      * Returns Thread profile name. Generated as a constant THREAD_PROFILE_NAME in *Constants.      * @return thread profile name to use.      */
DECL|method|getThreadProfileName ()
specifier|protected
specifier|abstract
name|String
name|getThreadProfileName
parameter_list|()
function_decl|;
block|}
end_class

end_unit

