begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.properties
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|properties
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
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
name|impl
operator|.
name|DefaultComponent
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
name|FilePathResolver
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
name|LRUSoftCache
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
comment|/**  * The<a href="http://camel.apache.org/properties">properties</a> component.  *  * @version   */
end_comment

begin_class
DECL|class|PropertiesComponent
specifier|public
class|class
name|PropertiesComponent
extends|extends
name|DefaultComponent
block|{
comment|/**      * The default prefix token.      */
DECL|field|DEFAULT_PREFIX_TOKEN
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_PREFIX_TOKEN
init|=
literal|"{{"
decl_stmt|;
comment|/**      * The default suffix token.      */
DECL|field|DEFAULT_SUFFIX_TOKEN
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_SUFFIX_TOKEN
init|=
literal|"}}"
decl_stmt|;
comment|/**      * The default prefix token.      * @deprecated Use {@link #DEFAULT_PREFIX_TOKEN} instead.      */
annotation|@
name|Deprecated
DECL|field|PREFIX_TOKEN
specifier|public
specifier|static
specifier|final
name|String
name|PREFIX_TOKEN
init|=
name|DEFAULT_PREFIX_TOKEN
decl_stmt|;
comment|/**      * The default suffix token.      * @deprecated Use {@link #DEFAULT_SUFFIX_TOKEN} instead.      */
annotation|@
name|Deprecated
DECL|field|SUFFIX_TOKEN
specifier|public
specifier|static
specifier|final
name|String
name|SUFFIX_TOKEN
init|=
name|DEFAULT_SUFFIX_TOKEN
decl_stmt|;
comment|/**      * Key for stores special override properties that containers such as OSGi can store      * in the OSGi service registry      */
DECL|field|OVERRIDE_PROPERTIES
specifier|public
specifier|static
specifier|final
name|String
name|OVERRIDE_PROPERTIES
init|=
name|PropertiesComponent
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".OverrideProperties"
decl_stmt|;
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
name|PropertiesComponent
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|cacheMap
specifier|private
specifier|final
name|Map
argument_list|<
name|CacheKey
argument_list|,
name|Properties
argument_list|>
name|cacheMap
init|=
operator|new
name|LRUSoftCache
argument_list|<
name|CacheKey
argument_list|,
name|Properties
argument_list|>
argument_list|(
literal|1000
argument_list|)
decl_stmt|;
DECL|field|propertiesResolver
specifier|private
name|PropertiesResolver
name|propertiesResolver
init|=
operator|new
name|DefaultPropertiesResolver
argument_list|()
decl_stmt|;
DECL|field|propertiesParser
specifier|private
name|PropertiesParser
name|propertiesParser
init|=
operator|new
name|DefaultPropertiesParser
argument_list|()
decl_stmt|;
DECL|field|locations
specifier|private
name|String
index|[]
name|locations
decl_stmt|;
DECL|field|ignoreMissingLocation
specifier|private
name|boolean
name|ignoreMissingLocation
decl_stmt|;
DECL|field|cache
specifier|private
name|boolean
name|cache
init|=
literal|true
decl_stmt|;
DECL|field|propertyPrefix
specifier|private
name|String
name|propertyPrefix
decl_stmt|;
DECL|field|propertySuffix
specifier|private
name|String
name|propertySuffix
decl_stmt|;
DECL|field|fallbackToUnaugmentedProperty
specifier|private
name|boolean
name|fallbackToUnaugmentedProperty
init|=
literal|true
decl_stmt|;
DECL|field|prefixToken
specifier|private
name|String
name|prefixToken
init|=
name|DEFAULT_PREFIX_TOKEN
decl_stmt|;
DECL|field|suffixToken
specifier|private
name|String
name|suffixToken
init|=
name|DEFAULT_SUFFIX_TOKEN
decl_stmt|;
DECL|field|overrideProperties
specifier|private
name|Properties
name|overrideProperties
decl_stmt|;
DECL|method|PropertiesComponent ()
specifier|public
name|PropertiesComponent
parameter_list|()
block|{     }
DECL|method|PropertiesComponent (String location)
specifier|public
name|PropertiesComponent
parameter_list|(
name|String
name|location
parameter_list|)
block|{
name|setLocation
argument_list|(
name|location
argument_list|)
expr_stmt|;
block|}
DECL|method|PropertiesComponent (String... locations)
specifier|public
name|PropertiesComponent
parameter_list|(
name|String
modifier|...
name|locations
parameter_list|)
block|{
name|setLocations
argument_list|(
name|locations
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
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
name|String
index|[]
name|paths
init|=
name|locations
decl_stmt|;
comment|// override default locations
name|String
name|locations
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"locations"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|Boolean
name|ignoreMissingLocationLoc
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"ignoreMissingLocation"
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|locations
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Overriding default locations with location: {}"
argument_list|,
name|locations
argument_list|)
expr_stmt|;
name|paths
operator|=
name|locations
operator|.
name|split
argument_list|(
literal|","
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ignoreMissingLocationLoc
operator|!=
literal|null
condition|)
block|{
name|ignoreMissingLocation
operator|=
name|ignoreMissingLocationLoc
expr_stmt|;
block|}
name|String
name|endpointUri
init|=
name|parseUri
argument_list|(
name|remaining
argument_list|,
name|paths
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Endpoint uri parsed as: {}"
argument_list|,
name|endpointUri
argument_list|)
expr_stmt|;
return|return
name|getCamelContext
argument_list|()
operator|.
name|getEndpoint
argument_list|(
name|endpointUri
argument_list|)
return|;
block|}
DECL|method|parseUri (String uri)
specifier|public
name|String
name|parseUri
parameter_list|(
name|String
name|uri
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|parseUri
argument_list|(
name|uri
argument_list|,
name|locations
argument_list|)
return|;
block|}
DECL|method|parseUri (String uri, String... paths)
specifier|public
name|String
name|parseUri
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
modifier|...
name|paths
parameter_list|)
throws|throws
name|Exception
block|{
name|Properties
name|prop
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|paths
operator|!=
literal|null
condition|)
block|{
comment|// location may contain JVM system property or OS environment variables
comment|// so we need to parse those
name|String
index|[]
name|locations
init|=
name|parseLocations
argument_list|(
name|paths
argument_list|)
decl_stmt|;
comment|// check cache first
name|CacheKey
name|key
init|=
operator|new
name|CacheKey
argument_list|(
name|locations
argument_list|)
decl_stmt|;
name|prop
operator|=
name|cache
condition|?
name|cacheMap
operator|.
name|get
argument_list|(
name|key
argument_list|)
else|:
literal|null
expr_stmt|;
if|if
condition|(
name|prop
operator|==
literal|null
condition|)
block|{
name|prop
operator|=
name|propertiesResolver
operator|.
name|resolveProperties
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|ignoreMissingLocation
argument_list|,
name|locations
argument_list|)
expr_stmt|;
if|if
condition|(
name|cache
condition|)
block|{
name|cacheMap
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|prop
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// use override properties
if|if
condition|(
name|prop
operator|!=
literal|null
operator|&&
name|overrideProperties
operator|!=
literal|null
condition|)
block|{
comment|// make a copy to avoid affecting the original properties
name|Properties
name|override
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|override
operator|.
name|putAll
argument_list|(
name|prop
argument_list|)
expr_stmt|;
name|override
operator|.
name|putAll
argument_list|(
name|overrideProperties
argument_list|)
expr_stmt|;
name|prop
operator|=
name|override
expr_stmt|;
block|}
comment|// enclose tokens if missing
if|if
condition|(
operator|!
name|uri
operator|.
name|contains
argument_list|(
name|prefixToken
argument_list|)
operator|&&
operator|!
name|uri
operator|.
name|startsWith
argument_list|(
name|prefixToken
argument_list|)
condition|)
block|{
name|uri
operator|=
name|prefixToken
operator|+
name|uri
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|uri
operator|.
name|contains
argument_list|(
name|suffixToken
argument_list|)
operator|&&
operator|!
name|uri
operator|.
name|endsWith
argument_list|(
name|suffixToken
argument_list|)
condition|)
block|{
name|uri
operator|=
name|uri
operator|+
name|suffixToken
expr_stmt|;
block|}
name|LOG
operator|.
name|trace
argument_list|(
literal|"Parsing uri {} with properties: {}"
argument_list|,
name|uri
argument_list|,
name|prop
argument_list|)
expr_stmt|;
if|if
condition|(
name|propertiesParser
operator|instanceof
name|AugmentedPropertyNameAwarePropertiesParser
condition|)
block|{
return|return
operator|(
operator|(
name|AugmentedPropertyNameAwarePropertiesParser
operator|)
name|propertiesParser
operator|)
operator|.
name|parseUri
argument_list|(
name|uri
argument_list|,
name|prop
argument_list|,
name|prefixToken
argument_list|,
name|suffixToken
argument_list|,
name|propertyPrefix
argument_list|,
name|propertySuffix
argument_list|,
name|fallbackToUnaugmentedProperty
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|propertiesParser
operator|.
name|parseUri
argument_list|(
name|uri
argument_list|,
name|prop
argument_list|,
name|prefixToken
argument_list|,
name|suffixToken
argument_list|)
return|;
block|}
block|}
DECL|method|getLocations ()
specifier|public
name|String
index|[]
name|getLocations
parameter_list|()
block|{
return|return
name|locations
return|;
block|}
DECL|method|setLocations (String[] locations)
specifier|public
name|void
name|setLocations
parameter_list|(
name|String
index|[]
name|locations
parameter_list|)
block|{
name|this
operator|.
name|locations
operator|=
name|locations
expr_stmt|;
block|}
DECL|method|setLocation (String location)
specifier|public
name|void
name|setLocation
parameter_list|(
name|String
name|location
parameter_list|)
block|{
name|setLocations
argument_list|(
name|location
operator|.
name|split
argument_list|(
literal|","
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|getPropertiesResolver ()
specifier|public
name|PropertiesResolver
name|getPropertiesResolver
parameter_list|()
block|{
return|return
name|propertiesResolver
return|;
block|}
DECL|method|setPropertiesResolver (PropertiesResolver propertiesResolver)
specifier|public
name|void
name|setPropertiesResolver
parameter_list|(
name|PropertiesResolver
name|propertiesResolver
parameter_list|)
block|{
name|this
operator|.
name|propertiesResolver
operator|=
name|propertiesResolver
expr_stmt|;
block|}
DECL|method|getPropertiesParser ()
specifier|public
name|PropertiesParser
name|getPropertiesParser
parameter_list|()
block|{
return|return
name|propertiesParser
return|;
block|}
DECL|method|setPropertiesParser (PropertiesParser propertiesParser)
specifier|public
name|void
name|setPropertiesParser
parameter_list|(
name|PropertiesParser
name|propertiesParser
parameter_list|)
block|{
name|this
operator|.
name|propertiesParser
operator|=
name|propertiesParser
expr_stmt|;
block|}
DECL|method|isCache ()
specifier|public
name|boolean
name|isCache
parameter_list|()
block|{
return|return
name|cache
return|;
block|}
DECL|method|setCache (boolean cache)
specifier|public
name|void
name|setCache
parameter_list|(
name|boolean
name|cache
parameter_list|)
block|{
name|this
operator|.
name|cache
operator|=
name|cache
expr_stmt|;
block|}
DECL|method|getPropertyPrefix ()
specifier|public
name|String
name|getPropertyPrefix
parameter_list|()
block|{
return|return
name|propertyPrefix
return|;
block|}
DECL|method|setPropertyPrefix (String propertyPrefix)
specifier|public
name|void
name|setPropertyPrefix
parameter_list|(
name|String
name|propertyPrefix
parameter_list|)
block|{
name|this
operator|.
name|propertyPrefix
operator|=
name|propertyPrefix
expr_stmt|;
block|}
DECL|method|getPropertySuffix ()
specifier|public
name|String
name|getPropertySuffix
parameter_list|()
block|{
return|return
name|propertySuffix
return|;
block|}
DECL|method|setPropertySuffix (String propertySuffix)
specifier|public
name|void
name|setPropertySuffix
parameter_list|(
name|String
name|propertySuffix
parameter_list|)
block|{
name|this
operator|.
name|propertySuffix
operator|=
name|propertySuffix
expr_stmt|;
block|}
DECL|method|isFallbackToUnaugmentedProperty ()
specifier|public
name|boolean
name|isFallbackToUnaugmentedProperty
parameter_list|()
block|{
return|return
name|fallbackToUnaugmentedProperty
return|;
block|}
DECL|method|setFallbackToUnaugmentedProperty (boolean fallbackToUnaugmentedProperty)
specifier|public
name|void
name|setFallbackToUnaugmentedProperty
parameter_list|(
name|boolean
name|fallbackToUnaugmentedProperty
parameter_list|)
block|{
name|this
operator|.
name|fallbackToUnaugmentedProperty
operator|=
name|fallbackToUnaugmentedProperty
expr_stmt|;
block|}
DECL|method|isIgnoreMissingLocation ()
specifier|public
name|boolean
name|isIgnoreMissingLocation
parameter_list|()
block|{
return|return
name|ignoreMissingLocation
return|;
block|}
DECL|method|setIgnoreMissingLocation (boolean ignoreMissingLocation)
specifier|public
name|void
name|setIgnoreMissingLocation
parameter_list|(
name|boolean
name|ignoreMissingLocation
parameter_list|)
block|{
name|this
operator|.
name|ignoreMissingLocation
operator|=
name|ignoreMissingLocation
expr_stmt|;
block|}
DECL|method|getPrefixToken ()
specifier|public
name|String
name|getPrefixToken
parameter_list|()
block|{
return|return
name|prefixToken
return|;
block|}
comment|/**      * Sets the value of the prefix token used to identify properties to replace.  Setting a value of      * {@code null} restores the default token (@link {@link #DEFAULT_PREFIX_TOKEN}).      */
DECL|method|setPrefixToken (String prefixToken)
specifier|public
name|void
name|setPrefixToken
parameter_list|(
name|String
name|prefixToken
parameter_list|)
block|{
if|if
condition|(
name|prefixToken
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|prefixToken
operator|=
name|DEFAULT_PREFIX_TOKEN
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|prefixToken
operator|=
name|prefixToken
expr_stmt|;
block|}
block|}
DECL|method|getSuffixToken ()
specifier|public
name|String
name|getSuffixToken
parameter_list|()
block|{
return|return
name|suffixToken
return|;
block|}
comment|/**      * Sets the value of the suffix token used to identify properties to replace.  Setting a value of      * {@code null} restores the default token (@link {@link #DEFAULT_SUFFIX_TOKEN}).      */
DECL|method|setSuffixToken (String suffixToken)
specifier|public
name|void
name|setSuffixToken
parameter_list|(
name|String
name|suffixToken
parameter_list|)
block|{
if|if
condition|(
name|suffixToken
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|suffixToken
operator|=
name|DEFAULT_SUFFIX_TOKEN
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|suffixToken
operator|=
name|suffixToken
expr_stmt|;
block|}
block|}
DECL|method|getOverrideProperties ()
specifier|public
name|Properties
name|getOverrideProperties
parameter_list|()
block|{
return|return
name|overrideProperties
return|;
block|}
comment|/**      * Sets a special list of override properties that take precedence      * and will use first, if a property exist.      *      * @param overrideProperties properties that is used first      */
DECL|method|setOverrideProperties (Properties overrideProperties)
specifier|public
name|void
name|setOverrideProperties
parameter_list|(
name|Properties
name|overrideProperties
parameter_list|)
block|{
name|this
operator|.
name|overrideProperties
operator|=
name|overrideProperties
expr_stmt|;
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
name|cacheMap
operator|.
name|clear
argument_list|()
expr_stmt|;
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
DECL|method|parseLocations (String[] locations)
specifier|private
name|String
index|[]
name|parseLocations
parameter_list|(
name|String
index|[]
name|locations
parameter_list|)
block|{
name|String
index|[]
name|answer
init|=
operator|new
name|String
index|[
name|locations
operator|.
name|length
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|locations
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|String
name|location
init|=
name|locations
index|[
name|i
index|]
decl_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Parsing location: {} "
argument_list|,
name|location
argument_list|)
expr_stmt|;
name|location
operator|=
name|FilePathResolver
operator|.
name|resolvePath
argument_list|(
name|location
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Parsed location: {} "
argument_list|,
name|location
argument_list|)
expr_stmt|;
name|answer
index|[
name|i
index|]
operator|=
name|location
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
comment|/**      * Key used in the locations cache      */
DECL|class|CacheKey
specifier|private
specifier|static
specifier|final
class|class
name|CacheKey
implements|implements
name|Serializable
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
DECL|field|locations
specifier|private
specifier|final
name|String
index|[]
name|locations
decl_stmt|;
DECL|method|CacheKey (String[] locations)
specifier|private
name|CacheKey
parameter_list|(
name|String
index|[]
name|locations
parameter_list|)
block|{
name|this
operator|.
name|locations
operator|=
name|locations
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|equals (Object o)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|o
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|o
operator|==
literal|null
operator|||
name|getClass
argument_list|()
operator|!=
name|o
operator|.
name|getClass
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|CacheKey
name|that
init|=
operator|(
name|CacheKey
operator|)
name|o
decl_stmt|;
if|if
condition|(
operator|!
name|Arrays
operator|.
name|equals
argument_list|(
name|locations
argument_list|,
name|that
operator|.
name|locations
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|locations
operator|!=
literal|null
condition|?
name|Arrays
operator|.
name|hashCode
argument_list|(
name|locations
argument_list|)
else|:
literal|0
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"LocationKey["
operator|+
name|Arrays
operator|.
name|asList
argument_list|(
name|locations
argument_list|)
operator|.
name|toString
argument_list|()
operator|+
literal|"]"
return|;
block|}
block|}
block|}
end_class

end_unit

