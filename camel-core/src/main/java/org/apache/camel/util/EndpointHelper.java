begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
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
name|Arrays
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
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicLong
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
name|PatternSyntaxException
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
name|PollingConsumer
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
name|Route
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
name|BrowsableEndpoint
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
comment|/**  * Some helper methods for working with {@link Endpoint} instances  *  * @version   */
end_comment

begin_class
DECL|class|EndpointHelper
specifier|public
specifier|final
class|class
name|EndpointHelper
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|EndpointHelper
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|ENDPOINT_COUNTER
specifier|private
specifier|static
specifier|final
name|AtomicLong
name|ENDPOINT_COUNTER
init|=
operator|new
name|AtomicLong
argument_list|(
literal|0
argument_list|)
decl_stmt|;
DECL|method|EndpointHelper ()
specifier|private
name|EndpointHelper
parameter_list|()
block|{
comment|//Utility Class
block|}
comment|/**      * Creates a {@link PollingConsumer} and polls all pending messages on the endpoint      * and invokes the given {@link Processor} to process each {@link Exchange} and then closes      * down the consumer and throws any exceptions thrown.      */
DECL|method|pollEndpoint (Endpoint endpoint, Processor processor, long timeout)
specifier|public
specifier|static
name|void
name|pollEndpoint
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|long
name|timeout
parameter_list|)
throws|throws
name|Exception
block|{
name|PollingConsumer
name|consumer
init|=
name|endpoint
operator|.
name|createPollingConsumer
argument_list|()
decl_stmt|;
try|try
block|{
name|consumer
operator|.
name|start
argument_list|()
expr_stmt|;
while|while
condition|(
literal|true
condition|)
block|{
name|Exchange
name|exchange
init|=
name|consumer
operator|.
name|receive
argument_list|(
name|timeout
argument_list|)
decl_stmt|;
if|if
condition|(
name|exchange
operator|==
literal|null
condition|)
block|{
break|break;
block|}
else|else
block|{
name|processor
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
block|}
finally|finally
block|{
try|try
block|{
name|consumer
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Failed to stop PollingConsumer: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Creates a {@link PollingConsumer} and polls all pending messages on the      * endpoint and invokes the given {@link Processor} to process each      * {@link Exchange} and then closes down the consumer and throws any      * exceptions thrown.      */
DECL|method|pollEndpoint (Endpoint endpoint, Processor processor)
specifier|public
specifier|static
name|void
name|pollEndpoint
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
name|pollEndpoint
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|,
literal|1000L
argument_list|)
expr_stmt|;
block|}
comment|/**      * Matches the endpoint with the given pattern.      *<p/>      * The endpoint will first resolve property placeholders using {@link CamelContext#resolvePropertyPlaceholders(String)}.      *<p/>      * The match rules are applied in this order:      *<ul>      *<li>exact match, returns true</li>      *<li>wildcard match (pattern ends with a * and the uri starts with the pattern), returns true</li>      *<li>regular expression match, returns true</li>      *<li>otherwise returns false</li>      *</ul>      *      * @param context the Camel context, if<tt>null</tt> then property placeholder resolution is skipped.      * @param uri     the endpoint uri      * @param pattern a pattern to match      * @return<tt>true</tt> if match,<tt>false</tt> otherwise.      */
DECL|method|matchEndpoint (CamelContext context, String uri, String pattern)
specifier|public
specifier|static
name|boolean
name|matchEndpoint
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|String
name|uri
parameter_list|,
name|String
name|pattern
parameter_list|)
block|{
if|if
condition|(
name|context
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|uri
operator|=
name|context
operator|.
name|resolvePropertyPlaceholders
argument_list|(
name|uri
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
name|ResolveEndpointFailedException
argument_list|(
name|uri
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|// normalize uri so we can do endpoint hits with minor mistakes and parameters is not in the same order
try|try
block|{
name|uri
operator|=
name|URISupport
operator|.
name|normalizeUri
argument_list|(
name|uri
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
name|ResolveEndpointFailedException
argument_list|(
name|uri
argument_list|,
name|e
argument_list|)
throw|;
block|}
comment|// we need to test with and without scheme separators (//)
if|if
condition|(
name|uri
operator|.
name|indexOf
argument_list|(
literal|"://"
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
comment|// try without :// also
name|String
name|scheme
init|=
name|ObjectHelper
operator|.
name|before
argument_list|(
name|uri
argument_list|,
literal|"://"
argument_list|)
decl_stmt|;
name|String
name|path
init|=
name|ObjectHelper
operator|.
name|after
argument_list|(
name|uri
argument_list|,
literal|"://"
argument_list|)
decl_stmt|;
if|if
condition|(
name|matchPattern
argument_list|(
name|scheme
operator|+
literal|":"
operator|+
name|path
argument_list|,
name|pattern
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
else|else
block|{
comment|// try with :// also
name|String
name|scheme
init|=
name|ObjectHelper
operator|.
name|before
argument_list|(
name|uri
argument_list|,
literal|":"
argument_list|)
decl_stmt|;
name|String
name|path
init|=
name|ObjectHelper
operator|.
name|after
argument_list|(
name|uri
argument_list|,
literal|":"
argument_list|)
decl_stmt|;
if|if
condition|(
name|matchPattern
argument_list|(
name|scheme
operator|+
literal|"://"
operator|+
name|path
argument_list|,
name|pattern
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
comment|// and fallback to test with the uri as is
return|return
name|matchPattern
argument_list|(
name|uri
argument_list|,
name|pattern
argument_list|)
return|;
block|}
comment|/**      * Matches the endpoint with the given pattern.      * @see #matchEndpoint(org.apache.camel.CamelContext, String, String)      *      * @deprecated use {@link #matchEndpoint(org.apache.camel.CamelContext, String, String)} instead.      */
annotation|@
name|Deprecated
DECL|method|matchEndpoint (String uri, String pattern)
specifier|public
specifier|static
name|boolean
name|matchEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|pattern
parameter_list|)
block|{
return|return
name|matchEndpoint
argument_list|(
literal|null
argument_list|,
name|uri
argument_list|,
name|pattern
argument_list|)
return|;
block|}
comment|/**      * Matches the name with the given pattern.      *<p/>      * The match rules are applied in this order:      *<ul>      *<li>exact match, returns true</li>      *<li>wildcard match (pattern ends with a * and the name starts with the pattern), returns true</li>      *<li>regular expression match, returns true</li>      *<li>otherwise returns false</li>      *</ul>      *      * @param name    the name      * @param pattern a pattern to match      * @return<tt>true</tt> if match,<tt>false</tt> otherwise.      */
DECL|method|matchPattern (String name, String pattern)
specifier|public
specifier|static
name|boolean
name|matchPattern
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|pattern
parameter_list|)
block|{
if|if
condition|(
name|name
operator|==
literal|null
operator|||
name|pattern
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|name
operator|.
name|equals
argument_list|(
name|pattern
argument_list|)
condition|)
block|{
comment|// exact match
return|return
literal|true
return|;
block|}
if|if
condition|(
name|matchWildcard
argument_list|(
name|name
argument_list|,
name|pattern
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|matchRegex
argument_list|(
name|name
argument_list|,
name|pattern
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
comment|// no match
return|return
literal|false
return|;
block|}
comment|/**      * Matches the name with the given pattern.      *<p/>      * The match rules are applied in this order:      *<ul>      *<li>wildcard match (pattern ends with a * and the name starts with the pattern), returns true</li>      *<li>otherwise returns false</li>      *</ul>      *      * @param name    the name      * @param pattern a pattern to match      * @return<tt>true</tt> if match,<tt>false</tt> otherwise.      */
DECL|method|matchWildcard (String name, String pattern)
specifier|private
specifier|static
name|boolean
name|matchWildcard
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|pattern
parameter_list|)
block|{
comment|// we have wildcard support in that hence you can match with: file* to match any file endpoints
if|if
condition|(
name|pattern
operator|.
name|endsWith
argument_list|(
literal|"*"
argument_list|)
operator|&&
name|name
operator|.
name|startsWith
argument_list|(
name|pattern
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|pattern
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
comment|/**      * Matches the name with the given pattern.      *<p/>      * The match rules are applied in this order:      *<ul>      *<li>regular expression match, returns true</li>      *<li>otherwise returns false</li>      *</ul>      *      * @param name    the name      * @param pattern a pattern to match      * @return<tt>true</tt> if match,<tt>false</tt> otherwise.      */
DECL|method|matchRegex (String name, String pattern)
specifier|private
specifier|static
name|boolean
name|matchRegex
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|pattern
parameter_list|)
block|{
comment|// match by regular expression
try|try
block|{
if|if
condition|(
name|name
operator|.
name|matches
argument_list|(
name|pattern
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
catch|catch
parameter_list|(
name|PatternSyntaxException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
return|return
literal|false
return|;
block|}
comment|/**      * Sets the regular properties on the given bean      *      * @param context    the camel context      * @param bean       the bean      * @param parameters parameters      * @throws Exception is thrown if setting property fails      */
DECL|method|setProperties (CamelContext context, Object bean, Map<String, Object> parameters)
specifier|public
specifier|static
name|void
name|setProperties
parameter_list|(
name|CamelContext
name|context
parameter_list|,
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
name|IntrospectionSupport
operator|.
name|setProperties
argument_list|(
name|context
operator|.
name|getTypeConverter
argument_list|()
argument_list|,
name|bean
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sets the reference properties on the given bean      *<p/>      * This is convention over configuration, setting all reference parameters (using {@link #isReferenceParameter(String)}      * by looking it up in registry and setting it on the bean if possible.      *      * @param context    the camel context      * @param bean       the bean      * @param parameters parameters      * @throws Exception is thrown if setting property fails      */
DECL|method|setReferenceProperties (CamelContext context, Object bean, Map<String, Object> parameters)
specifier|public
specifier|static
name|void
name|setReferenceProperties
parameter_list|(
name|CamelContext
name|context
parameter_list|,
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
name|parameters
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
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
name|String
name|name
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|Object
name|v
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|String
name|value
init|=
name|v
operator|!=
literal|null
condition|?
name|v
operator|.
name|toString
argument_list|()
else|:
literal|null
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
operator|&&
name|isReferenceParameter
argument_list|(
name|value
argument_list|)
condition|)
block|{
name|boolean
name|hit
init|=
name|IntrospectionSupport
operator|.
name|setProperty
argument_list|(
name|context
argument_list|,
name|context
operator|.
name|getTypeConverter
argument_list|()
argument_list|,
name|bean
argument_list|,
name|name
argument_list|,
literal|null
argument_list|,
name|value
argument_list|,
literal|true
argument_list|)
decl_stmt|;
if|if
condition|(
name|hit
condition|)
block|{
comment|// must remove as its a valid option and we could configure it
name|it
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/**      * Is the given parameter a reference parameter (starting with a # char)      *      * @param parameter the parameter      * @return<tt>true</tt> if its a reference parameter      */
DECL|method|isReferenceParameter (String parameter)
specifier|public
specifier|static
name|boolean
name|isReferenceParameter
parameter_list|(
name|String
name|parameter
parameter_list|)
block|{
return|return
name|parameter
operator|!=
literal|null
operator|&&
name|parameter
operator|.
name|trim
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"#"
argument_list|)
return|;
block|}
comment|/**      * Resolves a reference parameter by making a lookup in the registry.      *      * @param<T>     type of object to lookup.      * @param context Camel context to use for lookup.      * @param value   reference parameter value.      * @param type    type of object to lookup.      * @return lookup result.      * @throws IllegalArgumentException if referenced object was not found in registry.      */
DECL|method|resolveReferenceParameter (CamelContext context, String value, Class<T> type)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|resolveReferenceParameter
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|String
name|value
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|resolveReferenceParameter
argument_list|(
name|context
argument_list|,
name|value
argument_list|,
name|type
argument_list|,
literal|true
argument_list|)
return|;
block|}
comment|/**      * Resolves a reference parameter by making a lookup in the registry.      *      * @param<T>     type of object to lookup.      * @param context Camel context to use for lookup.      * @param value   reference parameter value.      * @param type    type of object to lookup.      * @return lookup result (or<code>null</code> only if      *<code>mandatory</code> is<code>false</code>).      * @throws IllegalArgumentException if object was not found in registry and      *<code>mandatory</code> is<code>true</code>.      */
DECL|method|resolveReferenceParameter (CamelContext context, String value, Class<T> type, boolean mandatory)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|resolveReferenceParameter
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|String
name|value
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|boolean
name|mandatory
parameter_list|)
block|{
name|String
name|valueNoHash
init|=
name|value
operator|.
name|replaceAll
argument_list|(
literal|"#"
argument_list|,
literal|""
argument_list|)
decl_stmt|;
if|if
condition|(
name|mandatory
condition|)
block|{
return|return
name|CamelContextHelper
operator|.
name|mandatoryLookup
argument_list|(
name|context
argument_list|,
name|valueNoHash
argument_list|,
name|type
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|CamelContextHelper
operator|.
name|lookup
argument_list|(
name|context
argument_list|,
name|valueNoHash
argument_list|,
name|type
argument_list|)
return|;
block|}
block|}
comment|/**      * Resolves a reference list parameter by making lookups in the registry.      * The parameter value must be one of the following:      *<ul>      *<li>a comma-separated list of references to beans of type T</li>      *<li>a single reference to a bean type T</li>      *<li>a single reference to a bean of type java.util.List</li>      *</ul>      *      * @param context     Camel context to use for lookup.      * @param value       reference parameter value.      * @param elementType result list element type.      * @return list of lookup results.      * @throws IllegalArgumentException if any referenced object was not found in registry.      */
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unchecked"
block|,
literal|"rawtypes"
block|}
argument_list|)
DECL|method|resolveReferenceListParameter (CamelContext context, String value, Class<T> elementType)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|List
argument_list|<
name|T
argument_list|>
name|resolveReferenceListParameter
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|String
name|value
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|elementType
parameter_list|)
block|{
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|elements
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|value
operator|.
name|split
argument_list|(
literal|","
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|elements
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|Object
name|bean
init|=
name|resolveReferenceParameter
argument_list|(
name|context
argument_list|,
name|elements
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|trim
argument_list|()
argument_list|,
name|Object
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|bean
operator|instanceof
name|List
condition|)
block|{
comment|// The bean is a list
return|return
operator|(
name|List
operator|)
name|bean
return|;
block|}
else|else
block|{
comment|// The bean is a list element
return|return
name|Arrays
operator|.
name|asList
argument_list|(
name|elementType
operator|.
name|cast
argument_list|(
name|bean
argument_list|)
argument_list|)
return|;
block|}
block|}
else|else
block|{
comment|// more than one list element
name|List
argument_list|<
name|T
argument_list|>
name|result
init|=
operator|new
name|ArrayList
argument_list|<
name|T
argument_list|>
argument_list|(
name|elements
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|element
range|:
name|elements
control|)
block|{
name|result
operator|.
name|add
argument_list|(
name|resolveReferenceParameter
argument_list|(
name|context
argument_list|,
name|element
operator|.
name|trim
argument_list|()
argument_list|,
name|elementType
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
block|}
comment|/**      * Gets the route id for the given endpoint in which there is a consumer listening.      *      * @param endpoint  the endpoint      * @return the route id, or<tt>null</tt> if none found      */
DECL|method|getRouteIdFromEndpoint (Endpoint endpoint)
specifier|public
specifier|static
name|String
name|getRouteIdFromEndpoint
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
if|if
condition|(
name|endpoint
operator|==
literal|null
operator|||
name|endpoint
operator|.
name|getCamelContext
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|List
argument_list|<
name|Route
argument_list|>
name|routes
init|=
name|endpoint
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getRoutes
argument_list|()
decl_stmt|;
for|for
control|(
name|Route
name|route
range|:
name|routes
control|)
block|{
if|if
condition|(
name|route
operator|.
name|getEndpoint
argument_list|()
operator|.
name|equals
argument_list|(
name|endpoint
argument_list|)
operator|||
name|route
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointKey
argument_list|()
operator|.
name|equals
argument_list|(
name|endpoint
operator|.
name|getEndpointKey
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|route
operator|.
name|getId
argument_list|()
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/**      * A helper method for Endpoint implementations to create new Ids for Endpoints which also implement      * {@link org.apache.camel.spi.HasId}      */
DECL|method|createEndpointId ()
specifier|public
specifier|static
name|String
name|createEndpointId
parameter_list|()
block|{
return|return
literal|"endpoint"
operator|+
name|ENDPOINT_COUNTER
operator|.
name|incrementAndGet
argument_list|()
return|;
block|}
comment|/**      * Lookup the id the given endpoint has been enlisted with in the {@link org.apache.camel.spi.Registry}.      *      * @param endpoint  the endpoint      * @return the endpoint id, or<tt>null</tt> if not found      */
DECL|method|lookupEndpointRegistryId (Endpoint endpoint)
specifier|public
specifier|static
name|String
name|lookupEndpointRegistryId
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
if|if
condition|(
name|endpoint
operator|==
literal|null
operator|||
name|endpoint
operator|.
name|getCamelContext
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|Endpoint
argument_list|>
name|map
init|=
name|endpoint
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getRegistry
argument_list|()
operator|.
name|findByTypeWithName
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Endpoint
argument_list|>
name|entry
range|:
name|map
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|equals
argument_list|(
name|endpoint
argument_list|)
condition|)
block|{
return|return
name|entry
operator|.
name|getKey
argument_list|()
return|;
block|}
block|}
comment|// not found
return|return
literal|null
return|;
block|}
comment|/**      * Browses the {@link BrowsableEndpoint} within the given range, and returns the messages as a XML payload.      *      * @param endpoint the browsable endpoint      * @param fromIndex  from range      * @param toIndex    to range      * @param includeBody whether to include the message body in the XML payload      * @return XML payload with the messages      * @throws IllegalArgumentException if the from and to range is invalid      * @see MessageHelper#dumpAsXml(org.apache.camel.Message)      */
DECL|method|browseRangeMessagesAsXml (BrowsableEndpoint endpoint, Integer fromIndex, Integer toIndex, Boolean includeBody)
specifier|public
specifier|static
name|String
name|browseRangeMessagesAsXml
parameter_list|(
name|BrowsableEndpoint
name|endpoint
parameter_list|,
name|Integer
name|fromIndex
parameter_list|,
name|Integer
name|toIndex
parameter_list|,
name|Boolean
name|includeBody
parameter_list|)
block|{
if|if
condition|(
name|fromIndex
operator|==
literal|null
condition|)
block|{
name|fromIndex
operator|=
literal|0
expr_stmt|;
block|}
if|if
condition|(
name|toIndex
operator|==
literal|null
condition|)
block|{
name|toIndex
operator|=
name|Integer
operator|.
name|MAX_VALUE
expr_stmt|;
block|}
if|if
condition|(
name|fromIndex
operator|>
name|toIndex
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"From index cannot be larger than to index, was: "
operator|+
name|fromIndex
operator|+
literal|"> "
operator|+
name|toIndex
argument_list|)
throw|;
block|}
name|List
argument_list|<
name|Exchange
argument_list|>
name|exchanges
init|=
name|endpoint
operator|.
name|getExchanges
argument_list|()
decl_stmt|;
if|if
condition|(
name|exchanges
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
literal|null
return|;
block|}
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"<messages>"
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
name|fromIndex
init|;
name|i
operator|<
name|exchanges
operator|.
name|size
argument_list|()
operator|&&
name|i
operator|<=
name|toIndex
condition|;
name|i
operator|++
control|)
block|{
name|Exchange
name|exchange
init|=
name|exchanges
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|Message
name|msg
init|=
name|exchange
operator|.
name|hasOut
argument_list|()
condition|?
name|exchange
operator|.
name|getOut
argument_list|()
else|:
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|String
name|xml
init|=
name|MessageHelper
operator|.
name|dumpAsXml
argument_list|(
name|msg
argument_list|,
name|includeBody
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
operator|.
name|append
argument_list|(
name|xml
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
literal|"\n</messages>"
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

