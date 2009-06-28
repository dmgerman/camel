begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
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
name|concurrent
operator|.
name|ExecutorService
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
name|ScheduledExecutorService
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
name|ResolveEndpointFailedException
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
name|CamelContextHelper
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
name|URISupport
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
name|UnsafeUriCharactersEncoder
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
name|concurrent
operator|.
name|ExecutorServiceHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_comment
comment|/**  * Default component to use for base for components implementations.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|DefaultComponent
specifier|public
specifier|abstract
class|class
name|DefaultComponent
extends|extends
name|ServiceSupport
implements|implements
name|Component
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|DefaultComponent
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|DEFAULT_THREADPOOL_SIZE
specifier|private
specifier|static
specifier|final
name|int
name|DEFAULT_THREADPOOL_SIZE
init|=
literal|5
decl_stmt|;
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|executorService
specifier|private
name|ExecutorService
name|executorService
decl_stmt|;
DECL|method|DefaultComponent ()
specifier|public
name|DefaultComponent
parameter_list|()
block|{     }
DECL|method|DefaultComponent (CamelContext context)
specifier|public
name|DefaultComponent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|context
expr_stmt|;
block|}
DECL|method|createEndpoint (String uri)
specifier|public
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|)
throws|throws
name|Exception
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
literal|"camelContext"
argument_list|)
expr_stmt|;
comment|//encode URI string to the unsafe URI characters
name|URI
name|u
init|=
operator|new
name|URI
argument_list|(
name|UnsafeUriCharactersEncoder
operator|.
name|encode
argument_list|(
name|uri
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|path
init|=
name|u
operator|.
name|getSchemeSpecificPart
argument_list|()
decl_stmt|;
comment|// lets trim off any query arguments
if|if
condition|(
name|path
operator|.
name|startsWith
argument_list|(
literal|"//"
argument_list|)
condition|)
block|{
name|path
operator|=
name|path
operator|.
name|substring
argument_list|(
literal|2
argument_list|)
expr_stmt|;
block|}
name|int
name|idx
init|=
name|path
operator|.
name|indexOf
argument_list|(
literal|'?'
argument_list|)
decl_stmt|;
if|if
condition|(
name|idx
operator|>
literal|0
condition|)
block|{
name|path
operator|=
name|path
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|idx
argument_list|)
expr_stmt|;
block|}
name|Map
name|parameters
init|=
name|URISupport
operator|.
name|parseParameters
argument_list|(
name|u
argument_list|)
decl_stmt|;
name|validateURI
argument_list|(
name|uri
argument_list|,
name|path
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Creating endpoint uri=["
operator|+
name|uri
operator|+
literal|"], path=["
operator|+
name|path
operator|+
literal|"], parameters=["
operator|+
name|parameters
operator|+
literal|"]"
argument_list|)
expr_stmt|;
block|}
name|Endpoint
name|endpoint
init|=
name|createEndpoint
argument_list|(
name|uri
argument_list|,
name|path
argument_list|,
name|parameters
argument_list|)
decl_stmt|;
if|if
condition|(
name|endpoint
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|parameters
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|configureProperties
argument_list|(
name|parameters
argument_list|)
expr_stmt|;
if|if
condition|(
name|useIntrospectionOnEndpoint
argument_list|()
condition|)
block|{
name|setProperties
argument_list|(
name|endpoint
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
block|}
comment|// if endpoint is strict (not lenient) and we have unknown parameters configured then
comment|// fail if there are parameters that could not be set, then they are probably miss spelt or not supported at all
if|if
condition|(
operator|!
name|endpoint
operator|.
name|isLenientProperties
argument_list|()
condition|)
block|{
name|validateParameters
argument_list|(
name|uri
argument_list|,
name|parameters
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|endpoint
return|;
block|}
comment|/**      * Strategy for validation of parameters, that was not able to be resolved to any endpoint options.      *      * @param uri          the uri - the uri the end user provided untouched      * @param parameters   the parameters, an empty map if no parameters given      * @param optionPrefix optional prefix to filter the parameters for validation. Use<tt>null</tt> for validate all.      * @throws ResolveEndpointFailedException should be thrown if the URI validation failed      */
DECL|method|validateParameters (String uri, Map parameters, String optionPrefix)
specifier|protected
name|void
name|validateParameters
parameter_list|(
name|String
name|uri
parameter_list|,
name|Map
name|parameters
parameter_list|,
name|String
name|optionPrefix
parameter_list|)
block|{
name|Map
name|param
init|=
name|parameters
decl_stmt|;
if|if
condition|(
name|optionPrefix
operator|!=
literal|null
condition|)
block|{
name|param
operator|=
name|IntrospectionSupport
operator|.
name|extractProperties
argument_list|(
name|parameters
argument_list|,
name|optionPrefix
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|param
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
throw|throw
operator|new
name|ResolveEndpointFailedException
argument_list|(
name|uri
argument_list|,
literal|"There are "
operator|+
name|param
operator|.
name|size
argument_list|()
operator|+
literal|" parameters that couldn't be set on the endpoint."
operator|+
literal|" Check the uri if the parameters are spelt correctly and that they are properties of the endpoint."
operator|+
literal|" Unknown parameters=["
operator|+
name|param
operator|+
literal|"]"
argument_list|)
throw|;
block|}
block|}
comment|/**      * Strategy for validation of the uri when creating the endpoint.      *      * @param uri        the uri - the uri the end user provided untouched      * @param path       the path - part after the scheme      * @param parameters the parameters, an empty map if no parameters given      * @throws ResolveEndpointFailedException should be thrown if the URI validation failed      */
DECL|method|validateURI (String uri, String path, Map parameters)
specifier|protected
name|void
name|validateURI
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|path
parameter_list|,
name|Map
name|parameters
parameter_list|)
block|{
comment|// check for uri containing& but no ? marker
if|if
condition|(
name|uri
operator|.
name|contains
argument_list|(
literal|"&"
argument_list|)
operator|&&
operator|!
name|uri
operator|.
name|contains
argument_list|(
literal|"?"
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ResolveEndpointFailedException
argument_list|(
name|uri
argument_list|,
literal|"Invalid uri syntax: no ? marker however the uri "
operator|+
literal|"has& parameter separators. Check the uri if its missing a ? marker."
argument_list|)
throw|;
block|}
comment|// check for uri containing double&& markers
if|if
condition|(
name|uri
operator|.
name|contains
argument_list|(
literal|"&&"
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ResolveEndpointFailedException
argument_list|(
name|uri
argument_list|,
literal|"Invalid uri syntax: Double&& marker found. "
operator|+
literal|"Check the uri and remove the duplicate& marker."
argument_list|)
throw|;
block|}
block|}
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|camelContext
return|;
block|}
DECL|method|setCamelContext (CamelContext context)
specifier|public
name|void
name|setCamelContext
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|context
expr_stmt|;
block|}
DECL|method|getExecutorService ()
specifier|public
specifier|synchronized
name|ExecutorService
name|getExecutorService
parameter_list|()
block|{
if|if
condition|(
name|executorService
operator|==
literal|null
condition|)
block|{
name|executorService
operator|=
name|createScheduledExecutorService
argument_list|()
expr_stmt|;
block|}
return|return
name|executorService
return|;
block|}
DECL|method|setExecutorService (ExecutorService executorService)
specifier|public
specifier|synchronized
name|void
name|setExecutorService
parameter_list|(
name|ExecutorService
name|executorService
parameter_list|)
block|{
name|this
operator|.
name|executorService
operator|=
name|executorService
expr_stmt|;
block|}
DECL|method|getScheduledExecutorService ()
specifier|public
specifier|synchronized
name|ScheduledExecutorService
name|getScheduledExecutorService
parameter_list|()
block|{
name|ExecutorService
name|executor
init|=
name|getExecutorService
argument_list|()
decl_stmt|;
if|if
condition|(
name|executor
operator|instanceof
name|ScheduledExecutorService
condition|)
block|{
return|return
operator|(
name|ScheduledExecutorService
operator|)
name|executor
return|;
block|}
else|else
block|{
return|return
name|createScheduledExecutorService
argument_list|()
return|;
block|}
block|}
comment|/**      * A factory method to create a default thread pool and executor      */
DECL|method|createScheduledExecutorService ()
specifier|protected
name|ScheduledExecutorService
name|createScheduledExecutorService
parameter_list|()
block|{
name|String
name|name
init|=
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
decl_stmt|;
return|return
name|ExecutorServiceHelper
operator|.
name|newScheduledThreadPool
argument_list|(
name|DEFAULT_THREADPOOL_SIZE
argument_list|,
name|name
argument_list|,
literal|true
argument_list|)
return|;
block|}
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
literal|"camelContext"
argument_list|)
expr_stmt|;
block|}
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
name|executorService
operator|!=
literal|null
condition|)
block|{
name|executorService
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * A factory method allowing derived components to create a new endpoint      * from the given URI, remaining path and optional parameters      *      * @param uri the full URI of the endpoint      * @param remaining the remaining part of the URI without the query      *                parameters or component prefix      * @param parameters the optional parameters passed in      * @return a newly created endpoint or null if the endpoint cannot be      *         created based on the inputs      */
DECL|method|createEndpoint (String uri, String remaining, Map parameters)
specifier|protected
specifier|abstract
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
name|parameters
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Sets the bean properties on the given bean      *      * @param bean  the bean      * @param parameters  properties to set      */
DECL|method|setProperties (Object bean, Map parameters)
specifier|protected
name|void
name|setProperties
parameter_list|(
name|Object
name|bean
parameter_list|,
name|Map
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
comment|// set reference properties first as they use # syntax that fools the regular properties setter
name|EndpointHelper
operator|.
name|setReferenceProperties
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|bean
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
name|EndpointHelper
operator|.
name|setProperties
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|bean
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
block|}
comment|/**      * Derived classes may wish to overload this to prevent the default introspection of URI parameters      * on the created Endpoint instance      */
DECL|method|useIntrospectionOnEndpoint ()
specifier|protected
name|boolean
name|useIntrospectionOnEndpoint
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
comment|// Some helper methods
comment|//-------------------------------------------------------------------------
comment|/**      * Converts the given value to the requested type      */
DECL|method|convertTo (Class<T> type, Object value)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|convertTo
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
return|return
name|CamelContextHelper
operator|.
name|convertTo
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|type
argument_list|,
name|value
argument_list|)
return|;
block|}
comment|/**      * Converts the given value to the specified type throwing an {@link IllegalArgumentException}      * if the value could not be converted to a non null value      */
DECL|method|mandatoryConvertTo (Class<T> type, Object value)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|mandatoryConvertTo
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
return|return
name|CamelContextHelper
operator|.
name|mandatoryConvertTo
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|type
argument_list|,
name|value
argument_list|)
return|;
block|}
comment|/**      * Creates a new instance of the given type using the {@link org.apache.camel.spi.Injector} on the given      * {@link CamelContext}      */
DECL|method|newInstance (Class<T> beanType)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|newInstance
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|beanType
parameter_list|)
block|{
return|return
name|getCamelContext
argument_list|()
operator|.
name|getInjector
argument_list|()
operator|.
name|newInstance
argument_list|(
name|beanType
argument_list|)
return|;
block|}
comment|/**      * Look up the given named bean in the {@link org.apache.camel.spi.Registry} on the      * {@link CamelContext}      */
DECL|method|lookup (String name)
specifier|public
name|Object
name|lookup
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|getCamelContext
argument_list|()
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookup
argument_list|(
name|name
argument_list|)
return|;
block|}
comment|/**      * Look up the given named bean of the given type in the {@link org.apache.camel.spi.Registry} on the      * {@link CamelContext}      */
DECL|method|lookup (String name, Class<T> beanType)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|lookup
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|beanType
parameter_list|)
block|{
return|return
name|getCamelContext
argument_list|()
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookup
argument_list|(
name|name
argument_list|,
name|beanType
argument_list|)
return|;
block|}
comment|/**      * Look up the given named bean in the {@link org.apache.camel.spi.Registry} on the      * {@link CamelContext} or throws exception if not found.      */
DECL|method|mandatoryLookup (String name)
specifier|public
name|Object
name|mandatoryLookup
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|CamelContextHelper
operator|.
name|mandatoryLookup
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|name
argument_list|)
return|;
block|}
comment|/**      * Look up the given named bean of the given type in the {@link org.apache.camel.spi.Registry} on the      * {@link CamelContext} or throws exception if not found.      */
DECL|method|mandatoryLookup (String name, Class<T> beanType)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|mandatoryLookup
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|beanType
parameter_list|)
block|{
return|return
name|CamelContextHelper
operator|.
name|mandatoryLookup
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|name
argument_list|,
name|beanType
argument_list|)
return|;
block|}
comment|/**      * Gets the parameter and remove it from the parameter map.      *       * @param parameters the parameters      * @param key        the key      * @param type       the requested type to convert the value from the parameter      * @return  the converted value parameter,<tt>null</tt> if parameter does not exists.      */
DECL|method|getAndRemoveParameter (Map parameters, String key, Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|getAndRemoveParameter
parameter_list|(
name|Map
name|parameters
parameter_list|,
name|String
name|key
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
name|key
argument_list|,
name|type
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      * Gets the parameter and remove it from the parameter map.      *      * @param parameters    the parameters      * @param key           the key      * @param type          the requested type to convert the value from the parameter      * @param defaultValue  use this default value if the parameter does not contain the key      * @return  the converted value parameter      */
DECL|method|getAndRemoveParameter (Map parameters, String key, Class<T> type, T defaultValue)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|getAndRemoveParameter
parameter_list|(
name|Map
name|parameters
parameter_list|,
name|String
name|key
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|T
name|defaultValue
parameter_list|)
block|{
name|Object
name|value
init|=
name|parameters
operator|.
name|remove
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
name|value
operator|=
name|defaultValue
expr_stmt|;
block|}
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|convertTo
argument_list|(
name|type
argument_list|,
name|value
argument_list|)
return|;
block|}
comment|/**      * Returns the reminder of the text if it starts with the prefix.      *<p/>      * Is useable for string parameters that contains commands.      *       * @param prefix  the prefix      * @param text  the text      * @return the reminder, or null if no reminder      */
DECL|method|ifStartsWithReturnRemainder (String prefix, String text)
specifier|protected
name|String
name|ifStartsWithReturnRemainder
parameter_list|(
name|String
name|prefix
parameter_list|,
name|String
name|text
parameter_list|)
block|{
if|if
condition|(
name|text
operator|.
name|startsWith
argument_list|(
name|prefix
argument_list|)
condition|)
block|{
name|String
name|remainder
init|=
name|text
operator|.
name|substring
argument_list|(
name|prefix
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|remainder
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
return|return
name|remainder
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

