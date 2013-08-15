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
name|EndpointConfiguration
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
name|support
operator|.
name|ServiceSupport
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
comment|/**  * Default component to use for base for components implementations.  *  * @version   */
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
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|DefaultComponent
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
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
annotation|@
name|Deprecated
DECL|method|preProcessUri (String uri)
specifier|protected
name|String
name|preProcessUri
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
comment|// Give components a chance to preprocess URIs and migrate to URI syntax that discourages invalid URIs
comment|// (see CAMEL-4425)
comment|// check URI string to the unsafe URI characters
name|String
name|encodedUri
init|=
name|UnsafeUriCharactersEncoder
operator|.
name|encode
argument_list|(
name|uri
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|encodedUri
operator|.
name|equals
argument_list|(
name|uri
argument_list|)
condition|)
block|{
comment|// uri supplied is not really valid
name|LOG
operator|.
name|warn
argument_list|(
literal|"Supplied URI '{}' contains unsafe characters, please check encoding"
argument_list|,
name|uri
argument_list|)
expr_stmt|;
block|}
return|return
name|encodedUri
return|;
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
comment|// check URI string to the unsafe URI characters
name|String
name|encodedUri
init|=
name|preProcessUri
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|URI
name|u
init|=
operator|new
name|URI
argument_list|(
name|encodedUri
argument_list|)
decl_stmt|;
name|String
name|path
init|=
name|useRawUri
argument_list|()
condition|?
name|u
operator|.
name|getRawSchemeSpecificPart
argument_list|()
else|:
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
operator|-
literal|1
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
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
decl_stmt|;
if|if
condition|(
name|useRawUri
argument_list|()
condition|)
block|{
comment|// when using raw uri then the query is taking from the uri as is
name|String
name|query
decl_stmt|;
name|idx
operator|=
name|uri
operator|.
name|indexOf
argument_list|(
literal|'?'
argument_list|)
expr_stmt|;
if|if
condition|(
name|idx
operator|>
operator|-
literal|1
condition|)
block|{
name|query
operator|=
name|uri
operator|.
name|substring
argument_list|(
name|idx
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|query
operator|=
name|u
operator|.
name|getRawQuery
argument_list|()
expr_stmt|;
block|}
comment|// and use method parseQuery
name|parameters
operator|=
name|URISupport
operator|.
name|parseQuery
argument_list|(
name|query
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// however when using the encoded (default mode) uri then the query,
comment|// is taken from the URI (ensures values is URI encoded)
comment|// and use method parseParameters
name|parameters
operator|=
name|URISupport
operator|.
name|parseParameters
argument_list|(
name|u
argument_list|)
expr_stmt|;
block|}
comment|// parameters using raw syntax: RAW(value)
comment|// should have the token removed, so its only the value we have in parameters, as we are about to create
comment|// an endpoint and want to have the parameter values without the RAW tokens
name|URISupport
operator|.
name|resolveRawParameterValues
argument_list|(
name|parameters
argument_list|)
expr_stmt|;
comment|// use encoded or raw uri?
name|uri
operator|=
name|useRawUri
argument_list|()
condition|?
name|uri
else|:
name|encodedUri
expr_stmt|;
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
name|isTraceEnabled
argument_list|()
condition|)
block|{
comment|// at trace level its okay to have parameters logged, that may contain passwords
name|LOG
operator|.
name|trace
argument_list|(
literal|"Creating endpoint uri=[{}], path=[{}], parameters=[{}]"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|URISupport
operator|.
name|sanitizeUri
argument_list|(
name|uri
argument_list|)
block|,
name|URISupport
operator|.
name|sanitizePath
argument_list|(
name|path
argument_list|)
block|,
name|parameters
block|}
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
comment|// but at debug level only output sanitized uris
name|LOG
operator|.
name|debug
argument_list|(
literal|"Creating endpoint uri=[{}], path=[{}]"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|URISupport
operator|.
name|sanitizeUri
argument_list|(
name|uri
argument_list|)
block|,
name|URISupport
operator|.
name|sanitizePath
argument_list|(
name|path
argument_list|)
block|}
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
operator|!
name|parameters
operator|.
name|isEmpty
argument_list|()
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
comment|// fail if there are parameters that could not be set, then they are probably misspell or not supported at all
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
name|afterConfiguration
argument_list|(
name|uri
argument_list|,
name|path
argument_list|,
name|endpoint
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
return|return
name|endpoint
return|;
block|}
annotation|@
name|Override
DECL|method|createComponentConfiguration ()
specifier|public
name|ComponentConfiguration
name|createComponentConfiguration
parameter_list|()
block|{
return|return
operator|new
name|DefaultComponentConfiguration
argument_list|(
name|this
argument_list|)
return|;
block|}
DECL|method|createConfiguration (String uri)
specifier|public
name|EndpointConfiguration
name|createConfiguration
parameter_list|(
name|String
name|uri
parameter_list|)
throws|throws
name|Exception
block|{
name|MappedEndpointConfiguration
name|config
init|=
operator|new
name|MappedEndpointConfiguration
argument_list|(
name|getCamelContext
argument_list|()
argument_list|)
decl_stmt|;
name|config
operator|.
name|setURI
argument_list|(
operator|new
name|URI
argument_list|(
name|uri
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|config
return|;
block|}
DECL|method|useRawUri ()
specifier|public
name|boolean
name|useRawUri
parameter_list|()
block|{
comment|// should use encoded uri by default
return|return
literal|false
return|;
block|}
comment|/**      * Strategy to do post configuration logic.      *<p/>      * Can be used to construct an URI based on the remaining parameters. For example the parameters that configures      * the endpoint have been removed from the parameters which leaves only the additional parameters left.      *      * @param uri the uri      * @param remaining the remaining part of the URI without the query parameters or component prefix      * @param endpoint the created endpoint      * @param parameters the remaining parameters after the endpoint has been created and parsed the parameters      * @throws Exception can be thrown to indicate error creating the endpoint      */
DECL|method|afterConfiguration (String uri, String remaining, Endpoint endpoint, Map<String, Object> parameters)
specifier|protected
name|void
name|afterConfiguration
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Endpoint
name|endpoint
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
comment|// noop
block|}
comment|/**      * Strategy for validation of parameters, that was not able to be resolved to any endpoint options.      *      * @param uri          the uri      * @param parameters   the parameters, an empty map if no parameters given      * @param optionPrefix optional prefix to filter the parameters for validation. Use<tt>null</tt> for validate all.      * @throws ResolveEndpointFailedException should be thrown if the URI validation failed      */
DECL|method|validateParameters (String uri, Map<String, Object> parameters, String optionPrefix)
specifier|protected
name|void
name|validateParameters
parameter_list|(
name|String
name|uri
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|,
name|String
name|optionPrefix
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
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
comment|/**      * Strategy for validation of the uri when creating the endpoint.      *      * @param uri        the uri      * @param path       the path - part after the scheme      * @param parameters the parameters, an empty map if no parameters given      * @throws ResolveEndpointFailedException should be thrown if the URI validation failed      */
DECL|method|validateURI (String uri, String path, Map<String, Object> parameters)
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
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
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
comment|// if we have a trailing& then that is invalid as well
if|if
condition|(
name|uri
operator|.
name|endsWith
argument_list|(
literal|"&"
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ResolveEndpointFailedException
argument_list|(
name|uri
argument_list|,
literal|"Invalid uri syntax: Trailing& marker found. "
operator|+
literal|"Check the uri and remove the trailing& marker."
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
comment|// noop
block|}
comment|/**      * A factory method allowing derived components to create a new endpoint      * from the given URI, remaining path and optional parameters      *      * @param uri the full URI of the endpoint      * @param remaining the remaining part of the URI without the query      *                parameters or component prefix      * @param parameters the optional parameters passed in      * @return a newly created endpoint or null if the endpoint cannot be      *         created based on the inputs      * @throws Exception is thrown if error creating the endpoint      */
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
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
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Sets the bean properties on the given bean      *      * @param bean  the bean      * @param parameters  properties to set      */
DECL|method|setProperties (Object bean, Map<String, Object> parameters)
specifier|protected
name|void
name|setProperties
parameter_list|(
name|Object
name|bean
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
comment|/**      * Gets the parameter and remove it from the parameter map. This method doesn't resolve      * reference parameters in the registry.      *       * @param parameters the parameters      * @param key        the key      * @param type       the requested type to convert the value from the parameter      * @return  the converted value parameter,<tt>null</tt> if parameter does not exists.      * @see #resolveAndRemoveReferenceParameter(Map, String, Class)      */
DECL|method|getAndRemoveParameter (Map<String, Object> parameters, String key, Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|getAndRemoveParameter
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
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
comment|/**      * Gets the parameter and remove it from the parameter map. This method doesn't resolve      * reference parameters in the registry.      *      * @param parameters    the parameters      * @param key           the key      * @param type          the requested type to convert the value from the parameter      * @param defaultValue  use this default value if the parameter does not contain the key      * @return  the converted value parameter      * @see #resolveAndRemoveReferenceParameter(Map, String, Class, Object)      */
DECL|method|getAndRemoveParameter (Map<String, Object> parameters, String key, Class<T> type, T defaultValue)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|getAndRemoveParameter
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
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
comment|/**      * Resolves a reference parameter in the registry and removes it from the map.       *       * @param<T>           type of object to lookup in the registry.      * @param parameters    parameter map.      * @param key           parameter map key.      * @param type          type of object to lookup in the registry.      * @return the referenced object or<code>null</code> if the parameter map       *         doesn't contain the key.      * @throws IllegalArgumentException if a non-null reference was not found in       *         registry.      */
DECL|method|resolveAndRemoveReferenceParameter (Map<String, Object> parameters, String key, Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|resolveAndRemoveReferenceParameter
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
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
name|resolveAndRemoveReferenceParameter
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
comment|/**      * Resolves a reference parameter in the registry and removes it from the map.       *       * @param<T>           type of object to lookup in the registry.      * @param parameters    parameter map.      * @param key           parameter map key.      * @param type          type of object to lookup in the registry.      * @param defaultValue  default value to use if the parameter map doesn't       *                      contain the key.      * @return the referenced object or the default value.      * @throws IllegalArgumentException if referenced object was not found in       *         registry.      */
DECL|method|resolveAndRemoveReferenceParameter (Map<String, Object> parameters, String key, Class<T> type, T defaultValue)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|resolveAndRemoveReferenceParameter
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
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
name|String
name|value
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
name|key
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
return|return
name|defaultValue
return|;
block|}
else|else
block|{
return|return
name|EndpointHelper
operator|.
name|resolveReferenceParameter
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|value
operator|.
name|toString
argument_list|()
argument_list|,
name|type
argument_list|)
return|;
block|}
block|}
comment|/**      * Resolves a reference list parameter in the registry and removes it from      * the map.      *       * @param parameters      *            parameter map.      * @param key      *            parameter map key.      * @param elementType      *            result list element type.      * @return the list of referenced objects or an empty list if the parameter      *         map doesn't contain the key.      * @throws IllegalArgumentException if any of the referenced objects was       *         not found in registry.      * @see EndpointHelper#resolveReferenceListParameter(CamelContext, String, Class)      */
DECL|method|resolveAndRemoveReferenceListParameter (Map<String, Object> parameters, String key, Class<T> elementType)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|List
argument_list|<
name|T
argument_list|>
name|resolveAndRemoveReferenceListParameter
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|,
name|String
name|key
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|elementType
parameter_list|)
block|{
return|return
name|resolveAndRemoveReferenceListParameter
argument_list|(
name|parameters
argument_list|,
name|key
argument_list|,
name|elementType
argument_list|,
operator|new
name|ArrayList
argument_list|<
name|T
argument_list|>
argument_list|(
literal|0
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Resolves a reference list parameter in the registry and removes it from      * the map.      *       * @param parameters      *            parameter map.      * @param key      *            parameter map key.      * @param elementType      *            result list element type.      * @param defaultValue      *            default value to use if the parameter map doesn't      *            contain the key.      * @return the list of referenced objects or the default value.      * @throws IllegalArgumentException if any of the referenced objects was       *         not found in registry.      * @see EndpointHelper#resolveReferenceListParameter(CamelContext, String, Class)      */
DECL|method|resolveAndRemoveReferenceListParameter (Map<String, Object> parameters, String key, Class<T> elementType, List<T> defaultValue)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|List
argument_list|<
name|T
argument_list|>
name|resolveAndRemoveReferenceListParameter
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|,
name|String
name|key
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|elementType
parameter_list|,
name|List
argument_list|<
name|T
argument_list|>
name|defaultValue
parameter_list|)
block|{
name|String
name|value
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
name|key
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
return|return
name|defaultValue
return|;
block|}
else|else
block|{
return|return
name|EndpointHelper
operator|.
name|resolveReferenceListParameter
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|value
operator|.
name|toString
argument_list|()
argument_list|,
name|elementType
argument_list|)
return|;
block|}
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

