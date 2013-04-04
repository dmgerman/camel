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
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Enumeration
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
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
name|Properties
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
name|SortedMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|StringTokenizer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeMap
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
name|NoSuchBeanException
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
name|NoSuchEndpointException
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
name|RouteStartupOrder
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
operator|.
name|isEmpty
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
operator|.
name|isNotEmpty
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
operator|.
name|notNull
import|;
end_import

begin_comment
comment|/**  * A number of helper methods  *  * @version   */
end_comment

begin_class
DECL|class|CamelContextHelper
specifier|public
specifier|final
class|class
name|CamelContextHelper
block|{
DECL|field|COMPONENT_DESCRIPTOR
specifier|public
specifier|static
specifier|final
name|String
name|COMPONENT_DESCRIPTOR
init|=
literal|"META-INF/services/org/apache/camel/component.properties"
decl_stmt|;
comment|/**      * Utility classes should not have a public constructor.      */
DECL|method|CamelContextHelper ()
specifier|private
name|CamelContextHelper
parameter_list|()
block|{     }
comment|/**      * Returns the mandatory endpoint for the given URI or the      * {@link org.apache.camel.NoSuchEndpointException} is thrown      */
DECL|method|getMandatoryEndpoint (CamelContext camelContext, String uri)
specifier|public
specifier|static
name|Endpoint
name|getMandatoryEndpoint
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|uri
parameter_list|)
throws|throws
name|NoSuchEndpointException
block|{
name|Endpoint
name|endpoint
init|=
name|camelContext
operator|.
name|getEndpoint
argument_list|(
name|uri
argument_list|)
decl_stmt|;
if|if
condition|(
name|endpoint
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NoSuchEndpointException
argument_list|(
name|uri
argument_list|)
throw|;
block|}
else|else
block|{
return|return
name|endpoint
return|;
block|}
block|}
comment|/**      * Returns the mandatory endpoint for the given URI and type or the      * {@link org.apache.camel.NoSuchEndpointException} is thrown      */
DECL|method|getMandatoryEndpoint (CamelContext camelContext, String uri, Class<T> type)
specifier|public
specifier|static
parameter_list|<
name|T
extends|extends
name|Endpoint
parameter_list|>
name|T
name|getMandatoryEndpoint
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|uri
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
name|Endpoint
name|endpoint
init|=
name|getMandatoryEndpoint
argument_list|(
name|camelContext
argument_list|,
name|uri
argument_list|)
decl_stmt|;
return|return
name|ObjectHelper
operator|.
name|cast
argument_list|(
name|type
argument_list|,
name|endpoint
argument_list|)
return|;
block|}
comment|/**      * Converts the given value to the requested type      */
DECL|method|convertTo (CamelContext context, Class<T> type, Object value)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|convertTo
parameter_list|(
name|CamelContext
name|context
parameter_list|,
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
name|notNull
argument_list|(
name|context
argument_list|,
literal|"camelContext"
argument_list|)
expr_stmt|;
return|return
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|type
argument_list|,
name|value
argument_list|)
return|;
block|}
comment|/**      * Converts the given value to the specified type throwing an {@link IllegalArgumentException}      * if the value could not be converted to a non null value      */
DECL|method|mandatoryConvertTo (CamelContext context, Class<T> type, Object value)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|mandatoryConvertTo
parameter_list|(
name|CamelContext
name|context
parameter_list|,
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
name|T
name|answer
init|=
name|convertTo
argument_list|(
name|context
argument_list|,
name|type
argument_list|,
name|value
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Value "
operator|+
name|value
operator|+
literal|" converted to "
operator|+
name|type
operator|.
name|getName
argument_list|()
operator|+
literal|" cannot be null"
argument_list|)
throw|;
block|}
return|return
name|answer
return|;
block|}
comment|/**      * Creates a new instance of the given type using the {@link org.apache.camel.spi.Injector} on the given      * {@link CamelContext}      */
DECL|method|newInstance (CamelContext context, Class<T> beanType)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|newInstance
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|beanType
parameter_list|)
block|{
return|return
name|context
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
DECL|method|lookup (CamelContext context, String name)
specifier|public
specifier|static
name|Object
name|lookup
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|String
name|name
parameter_list|)
block|{
return|return
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByName
argument_list|(
name|name
argument_list|)
return|;
block|}
comment|/**      * Look up the given named bean of the given type in the {@link org.apache.camel.spi.Registry} on the      * {@link CamelContext}      */
DECL|method|lookup (CamelContext context, String name, Class<T> beanType)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|lookup
parameter_list|(
name|CamelContext
name|context
parameter_list|,
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
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByNameAndType
argument_list|(
name|name
argument_list|,
name|beanType
argument_list|)
return|;
block|}
comment|/**      * Look up the given named bean in the {@link org.apache.camel.spi.Registry} on the      * {@link CamelContext} or throws {@link NoSuchBeanException} if not found.      */
DECL|method|mandatoryLookup (CamelContext context, String name)
specifier|public
specifier|static
name|Object
name|mandatoryLookup
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|Object
name|answer
init|=
name|lookup
argument_list|(
name|context
argument_list|,
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NoSuchBeanException
argument_list|(
name|name
argument_list|)
throw|;
block|}
return|return
name|answer
return|;
block|}
comment|/**      * Look up the given named bean of the given type in the {@link org.apache.camel.spi.Registry} on the      * {@link CamelContext} or throws NoSuchBeanException if not found.      */
DECL|method|mandatoryLookup (CamelContext context, String name, Class<T> beanType)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|mandatoryLookup
parameter_list|(
name|CamelContext
name|context
parameter_list|,
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
name|T
name|answer
init|=
name|lookup
argument_list|(
name|context
argument_list|,
name|name
argument_list|,
name|beanType
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NoSuchBeanException
argument_list|(
name|name
argument_list|,
name|beanType
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
return|return
name|answer
return|;
block|}
comment|/**      * Evaluates the @EndpointInject annotation using the given context      */
DECL|method|getEndpointInjection (CamelContext camelContext, String uri, String ref, String injectionPointName, boolean mandatory)
specifier|public
specifier|static
name|Endpoint
name|getEndpointInjection
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|uri
parameter_list|,
name|String
name|ref
parameter_list|,
name|String
name|injectionPointName
parameter_list|,
name|boolean
name|mandatory
parameter_list|)
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|uri
argument_list|)
operator|&&
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|ref
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Both uri and name is provided, only either one is allowed: uri="
operator|+
name|uri
operator|+
literal|", ref="
operator|+
name|ref
argument_list|)
throw|;
block|}
name|Endpoint
name|endpoint
decl_stmt|;
if|if
condition|(
name|isNotEmpty
argument_list|(
name|uri
argument_list|)
condition|)
block|{
name|endpoint
operator|=
name|camelContext
operator|.
name|getEndpoint
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// if a ref is given then it should be possible to lookup
comment|// otherwise we do not catch situations where there is a typo etc
if|if
condition|(
name|isNotEmpty
argument_list|(
name|ref
argument_list|)
condition|)
block|{
name|endpoint
operator|=
name|mandatoryLookup
argument_list|(
name|camelContext
argument_list|,
name|ref
argument_list|,
name|Endpoint
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|isEmpty
argument_list|(
name|ref
argument_list|)
condition|)
block|{
name|ref
operator|=
name|injectionPointName
expr_stmt|;
block|}
if|if
condition|(
name|mandatory
condition|)
block|{
name|endpoint
operator|=
name|mandatoryLookup
argument_list|(
name|camelContext
argument_list|,
name|ref
argument_list|,
name|Endpoint
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|endpoint
operator|=
name|lookup
argument_list|(
name|camelContext
argument_list|,
name|ref
argument_list|,
name|Endpoint
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|endpoint
return|;
block|}
comment|/**      * Gets the maximum cache pool size.      *<p/>      * Will use the property set on CamelContext with the key {@link Exchange#MAXIMUM_CACHE_POOL_SIZE}.      * If no property has been set, then it will fallback to return a size of 1000.      *      * @param camelContext the camel context      * @return the maximum cache size      * @throws IllegalArgumentException is thrown if the property is illegal      */
DECL|method|getMaximumCachePoolSize (CamelContext camelContext)
specifier|public
specifier|static
name|int
name|getMaximumCachePoolSize
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
if|if
condition|(
name|camelContext
operator|!=
literal|null
condition|)
block|{
name|String
name|s
init|=
name|camelContext
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|MAXIMUM_CACHE_POOL_SIZE
argument_list|)
decl_stmt|;
if|if
condition|(
name|s
operator|!=
literal|null
condition|)
block|{
try|try
block|{
comment|// we cannot use Camel type converters as they may not be ready this early
name|Integer
name|size
init|=
name|Integer
operator|.
name|valueOf
argument_list|(
name|s
argument_list|)
decl_stmt|;
if|if
condition|(
name|size
operator|==
literal|null
operator|||
name|size
operator|<=
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Property "
operator|+
name|Exchange
operator|.
name|MAXIMUM_CACHE_POOL_SIZE
operator|+
literal|" must be a positive number, was: "
operator|+
name|s
argument_list|)
throw|;
block|}
return|return
name|size
return|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Property "
operator|+
name|Exchange
operator|.
name|MAXIMUM_CACHE_POOL_SIZE
operator|+
literal|" must be a positive number, was: "
operator|+
name|s
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
comment|// 1000 is the default fallback
return|return
literal|1000
return|;
block|}
comment|/**      * Gets the maximum endpoint cache size.      *<p/>      * Will use the property set on CamelContext with the key {@link Exchange#MAXIMUM_ENDPOINT_CACHE_SIZE}.      * If no property has been set, then it will fallback to return a size of 1000.      *      * @param camelContext the camel context      * @return the maximum cache size      * @throws IllegalArgumentException is thrown if the property is illegal      */
DECL|method|getMaximumEndpointCacheSize (CamelContext camelContext)
specifier|public
specifier|static
name|int
name|getMaximumEndpointCacheSize
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
if|if
condition|(
name|camelContext
operator|!=
literal|null
condition|)
block|{
name|String
name|s
init|=
name|camelContext
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|MAXIMUM_ENDPOINT_CACHE_SIZE
argument_list|)
decl_stmt|;
if|if
condition|(
name|s
operator|!=
literal|null
condition|)
block|{
comment|// we cannot use Camel type converters as they may not be ready this early
try|try
block|{
name|Integer
name|size
init|=
name|Integer
operator|.
name|valueOf
argument_list|(
name|s
argument_list|)
decl_stmt|;
if|if
condition|(
name|size
operator|==
literal|null
operator|||
name|size
operator|<=
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Property "
operator|+
name|Exchange
operator|.
name|MAXIMUM_ENDPOINT_CACHE_SIZE
operator|+
literal|" must be a positive number, was: "
operator|+
name|s
argument_list|)
throw|;
block|}
return|return
name|size
return|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Property "
operator|+
name|Exchange
operator|.
name|MAXIMUM_ENDPOINT_CACHE_SIZE
operator|+
literal|" must be a positive number, was: "
operator|+
name|s
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
comment|// 1000 is the default fallback
return|return
literal|1000
return|;
block|}
comment|/**      * Parses the given text and handling property placeholders as well      *      * @param camelContext the camel context      * @param text  the text      * @return the parsed text, or<tt>null</tt> if the text was<tt>null</tt>      * @throws Exception is thrown if illegal argument      */
DECL|method|parseText (CamelContext camelContext, String text)
specifier|public
specifier|static
name|String
name|parseText
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|text
parameter_list|)
throws|throws
name|Exception
block|{
comment|// ensure we support property placeholders
return|return
name|camelContext
operator|.
name|resolvePropertyPlaceholders
argument_list|(
name|text
argument_list|)
return|;
block|}
comment|/**      * Parses the given text and converts it to an Integer and handling property placeholders as well      *      * @param camelContext the camel context      * @param text  the text      * @return the integer vale, or<tt>null</tt> if the text was<tt>null</tt>      * @throws Exception is thrown if illegal argument or type conversion not possible      */
DECL|method|parseInteger (CamelContext camelContext, String text)
specifier|public
specifier|static
name|Integer
name|parseInteger
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|text
parameter_list|)
throws|throws
name|Exception
block|{
comment|// ensure we support property placeholders
name|String
name|s
init|=
name|camelContext
operator|.
name|resolvePropertyPlaceholders
argument_list|(
name|text
argument_list|)
decl_stmt|;
if|if
condition|(
name|s
operator|!=
literal|null
condition|)
block|{
try|try
block|{
return|return
name|camelContext
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|mandatoryConvertTo
argument_list|(
name|Integer
operator|.
name|class
argument_list|,
name|s
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|e
parameter_list|)
block|{
if|if
condition|(
name|s
operator|.
name|equals
argument_list|(
name|text
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Error parsing ["
operator|+
name|s
operator|+
literal|"] as an Integer."
argument_list|,
name|e
argument_list|)
throw|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Error parsing ["
operator|+
name|s
operator|+
literal|"] from property "
operator|+
name|text
operator|+
literal|" as an Integer."
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Parses the given text and converts it to an Long and handling property placeholders as well      *      * @param camelContext the camel context      * @param text  the text      * @return the long vale, or<tt>null</tt> if the text was<tt>null</tt>      * @throws Exception is thrown if illegal argument or type conversion not possible      */
DECL|method|parseLong (CamelContext camelContext, String text)
specifier|public
specifier|static
name|Long
name|parseLong
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|text
parameter_list|)
throws|throws
name|Exception
block|{
comment|// ensure we support property placeholders
name|String
name|s
init|=
name|camelContext
operator|.
name|resolvePropertyPlaceholders
argument_list|(
name|text
argument_list|)
decl_stmt|;
if|if
condition|(
name|s
operator|!=
literal|null
condition|)
block|{
try|try
block|{
return|return
name|camelContext
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|mandatoryConvertTo
argument_list|(
name|Long
operator|.
name|class
argument_list|,
name|s
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|e
parameter_list|)
block|{
if|if
condition|(
name|s
operator|.
name|equals
argument_list|(
name|text
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Error parsing ["
operator|+
name|s
operator|+
literal|"] as a Long."
argument_list|,
name|e
argument_list|)
throw|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Error parsing ["
operator|+
name|s
operator|+
literal|"] from property "
operator|+
name|text
operator|+
literal|" as a Long."
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Parses the given text and converts it to a Double and handling property placeholders as well      *      * @param camelContext the camel context      * @param text  the text      * @return the double vale, or<tt>null</tt> if the text was<tt>null</tt>      * @throws Exception is thrown if illegal argument or type conversion not possible      */
DECL|method|parseDouble (CamelContext camelContext, String text)
specifier|public
specifier|static
name|Double
name|parseDouble
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|text
parameter_list|)
throws|throws
name|Exception
block|{
comment|// ensure we support property placeholders
name|String
name|s
init|=
name|camelContext
operator|.
name|resolvePropertyPlaceholders
argument_list|(
name|text
argument_list|)
decl_stmt|;
if|if
condition|(
name|s
operator|!=
literal|null
condition|)
block|{
try|try
block|{
return|return
name|camelContext
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|mandatoryConvertTo
argument_list|(
name|Double
operator|.
name|class
argument_list|,
name|s
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|e
parameter_list|)
block|{
if|if
condition|(
name|s
operator|.
name|equals
argument_list|(
name|text
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Error parsing ["
operator|+
name|s
operator|+
literal|"] as an Integer."
argument_list|,
name|e
argument_list|)
throw|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Error parsing ["
operator|+
name|s
operator|+
literal|"] from property "
operator|+
name|text
operator|+
literal|" as an Integer."
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Parses the given text and converts it to an Boolean and handling property placeholders as well      *      * @param camelContext the camel context      * @param text  the text      * @return the boolean vale, or<tt>null</tt> if the text was<tt>null</tt>      * @throws Exception is thrown if illegal argument or type conversion not possible      */
DECL|method|parseBoolean (CamelContext camelContext, String text)
specifier|public
specifier|static
name|Boolean
name|parseBoolean
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|text
parameter_list|)
throws|throws
name|Exception
block|{
comment|// ensure we support property placeholders
name|String
name|s
init|=
name|camelContext
operator|.
name|resolvePropertyPlaceholders
argument_list|(
name|text
argument_list|)
decl_stmt|;
if|if
condition|(
name|s
operator|!=
literal|null
condition|)
block|{
name|s
operator|=
name|s
operator|.
name|trim
argument_list|()
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|ENGLISH
argument_list|)
expr_stmt|;
if|if
condition|(
name|s
operator|.
name|equals
argument_list|(
literal|"true"
argument_list|)
operator|||
name|s
operator|.
name|equals
argument_list|(
literal|"false"
argument_list|)
condition|)
block|{
return|return
literal|"true"
operator|.
name|equals
argument_list|(
name|s
argument_list|)
condition|?
name|Boolean
operator|.
name|TRUE
else|:
name|Boolean
operator|.
name|FALSE
return|;
block|}
else|else
block|{
if|if
condition|(
name|s
operator|.
name|equals
argument_list|(
name|text
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Error parsing ["
operator|+
name|s
operator|+
literal|"] as a Boolean."
argument_list|)
throw|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Error parsing ["
operator|+
name|s
operator|+
literal|"] from property "
operator|+
name|text
operator|+
literal|" as a Boolean."
argument_list|)
throw|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Finds all possible Components on the classpath and Registry      */
DECL|method|findComponents (CamelContext camelContext)
specifier|public
specifier|static
name|SortedMap
argument_list|<
name|String
argument_list|,
name|Properties
argument_list|>
name|findComponents
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
throws|throws
name|LoadPropertiesException
block|{
name|SortedMap
argument_list|<
name|String
argument_list|,
name|Properties
argument_list|>
name|map
init|=
operator|new
name|TreeMap
argument_list|<
name|String
argument_list|,
name|Properties
argument_list|>
argument_list|()
decl_stmt|;
name|Enumeration
argument_list|<
name|URL
argument_list|>
name|iter
init|=
name|camelContext
operator|.
name|getClassResolver
argument_list|()
operator|.
name|loadResourcesAsURL
argument_list|(
name|COMPONENT_DESCRIPTOR
argument_list|)
decl_stmt|;
while|while
condition|(
name|iter
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|URL
name|url
init|=
name|iter
operator|.
name|nextElement
argument_list|()
decl_stmt|;
try|try
block|{
name|Properties
name|properties
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|properties
operator|.
name|load
argument_list|(
name|url
operator|.
name|openStream
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|names
init|=
name|properties
operator|.
name|getProperty
argument_list|(
literal|"components"
argument_list|)
decl_stmt|;
if|if
condition|(
name|names
operator|!=
literal|null
condition|)
block|{
name|StringTokenizer
name|tok
init|=
operator|new
name|StringTokenizer
argument_list|(
name|names
argument_list|)
decl_stmt|;
while|while
condition|(
name|tok
operator|.
name|hasMoreTokens
argument_list|()
condition|)
block|{
name|String
name|name
init|=
name|tok
operator|.
name|nextToken
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|properties
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|LoadPropertiesException
argument_list|(
name|url
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|// lets see what other components are in the registry
name|Map
argument_list|<
name|String
argument_list|,
name|Component
argument_list|>
name|beanMap
init|=
name|camelContext
operator|.
name|getRegistry
argument_list|()
operator|.
name|findByTypeWithName
argument_list|(
name|Component
operator|.
name|class
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Component
argument_list|>
argument_list|>
name|entries
init|=
name|beanMap
operator|.
name|entrySet
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Component
argument_list|>
name|entry
range|:
name|entries
control|)
block|{
name|String
name|name
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|map
operator|.
name|containsKey
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|Properties
name|properties
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|Component
name|component
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|component
operator|!=
literal|null
condition|)
block|{
name|properties
operator|.
name|put
argument_list|(
literal|"component"
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"class"
argument_list|,
name|component
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|properties
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|map
return|;
block|}
comment|/**      * Gets the route startup order for the given route id      *      * @param camelContext  the camel context      * @param routeId       the id of the route      * @return the startup order, or<tt>0</tt> if not possible to determine      */
DECL|method|getRouteStartupOrder (CamelContext camelContext, String routeId)
specifier|public
specifier|static
name|int
name|getRouteStartupOrder
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|routeId
parameter_list|)
block|{
for|for
control|(
name|RouteStartupOrder
name|order
range|:
name|camelContext
operator|.
name|getRouteStartupOrder
argument_list|()
control|)
block|{
if|if
condition|(
name|order
operator|.
name|getRoute
argument_list|()
operator|.
name|getId
argument_list|()
operator|.
name|equals
argument_list|(
name|routeId
argument_list|)
condition|)
block|{
return|return
name|order
operator|.
name|getStartupOrder
argument_list|()
return|;
block|}
block|}
return|return
literal|0
return|;
block|}
block|}
end_class

end_unit

