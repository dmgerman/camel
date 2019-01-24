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
name|Collection
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
name|Properties
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
name|annotations
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
name|support
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
name|support
operator|.
name|LRUCacheFactory
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
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * The<a href="http://camel.apache.org/properties">Properties Component</a> allows you to use property placeholders when defining Endpoint URIs  */
end_comment

begin_class
annotation|@
name|Component
argument_list|(
literal|"properties"
argument_list|)
DECL|class|PropertiesComponent
specifier|public
class|class
name|PropertiesComponent
extends|extends
name|DefaultComponent
implements|implements
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|PropertiesComponent
block|{
comment|/**      *  Never check system properties.      */
DECL|field|SYSTEM_PROPERTIES_MODE_NEVER
specifier|public
specifier|static
specifier|final
name|int
name|SYSTEM_PROPERTIES_MODE_NEVER
init|=
literal|0
decl_stmt|;
comment|/**      * Check system properties if not resolvable in the specified properties.      */
DECL|field|SYSTEM_PROPERTIES_MODE_FALLBACK
specifier|public
specifier|static
specifier|final
name|int
name|SYSTEM_PROPERTIES_MODE_FALLBACK
init|=
literal|1
decl_stmt|;
comment|/**      * Check system properties first, before trying the specified properties.      * This allows system properties to override any other property source.      *<p/>      * This is the default.      */
DECL|field|SYSTEM_PROPERTIES_MODE_OVERRIDE
specifier|public
specifier|static
specifier|final
name|int
name|SYSTEM_PROPERTIES_MODE_OVERRIDE
init|=
literal|2
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
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
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
name|LRUCacheFactory
operator|.
name|newLRUSoftCache
argument_list|(
literal|1000
argument_list|)
decl_stmt|;
DECL|field|functions
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|PropertiesFunction
argument_list|>
name|functions
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|propertiesResolver
specifier|private
name|PropertiesResolver
name|propertiesResolver
init|=
operator|new
name|DefaultPropertiesResolver
argument_list|(
name|this
argument_list|)
decl_stmt|;
DECL|field|propertiesParser
specifier|private
name|PropertiesParser
name|propertiesParser
init|=
operator|new
name|DefaultPropertiesParser
argument_list|(
name|this
argument_list|)
decl_stmt|;
DECL|field|locations
specifier|private
name|List
argument_list|<
name|PropertiesLocation
argument_list|>
name|locations
init|=
name|Collections
operator|.
name|emptyList
argument_list|()
decl_stmt|;
DECL|field|ignoreMissingLocation
specifier|private
name|boolean
name|ignoreMissingLocation
decl_stmt|;
DECL|field|encoding
specifier|private
name|String
name|encoding
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|cache
specifier|private
name|boolean
name|cache
init|=
literal|true
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|propertyPrefix
specifier|private
name|String
name|propertyPrefix
decl_stmt|;
DECL|field|propertyPrefixResolved
specifier|private
specifier|transient
name|String
name|propertyPrefixResolved
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|propertySuffix
specifier|private
name|String
name|propertySuffix
decl_stmt|;
DECL|field|propertySuffixResolved
specifier|private
specifier|transient
name|String
name|propertySuffixResolved
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|fallbackToUnaugmentedProperty
specifier|private
name|boolean
name|fallbackToUnaugmentedProperty
init|=
literal|true
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|defaultFallbackEnabled
specifier|private
name|boolean
name|defaultFallbackEnabled
init|=
literal|true
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|defaultValue
operator|=
name|DEFAULT_PREFIX_TOKEN
argument_list|)
DECL|field|prefixToken
specifier|private
name|String
name|prefixToken
init|=
name|DEFAULT_PREFIX_TOKEN
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|defaultValue
operator|=
name|DEFAULT_SUFFIX_TOKEN
argument_list|)
DECL|field|suffixToken
specifier|private
name|String
name|suffixToken
init|=
name|DEFAULT_SUFFIX_TOKEN
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|initialProperties
specifier|private
name|Properties
name|initialProperties
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|overrideProperties
specifier|private
name|Properties
name|overrideProperties
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|""
operator|+
name|SYSTEM_PROPERTIES_MODE_OVERRIDE
argument_list|,
name|enums
operator|=
literal|"0,1,2"
argument_list|)
DECL|field|systemPropertiesMode
specifier|private
name|int
name|systemPropertiesMode
init|=
name|SYSTEM_PROPERTIES_MODE_OVERRIDE
decl_stmt|;
DECL|method|PropertiesComponent ()
specifier|public
name|PropertiesComponent
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
comment|// include out of the box functions
name|addFunction
argument_list|(
operator|new
name|EnvPropertiesFunction
argument_list|()
argument_list|)
expr_stmt|;
name|addFunction
argument_list|(
operator|new
name|SysPropertiesFunction
argument_list|()
argument_list|)
expr_stmt|;
name|addFunction
argument_list|(
operator|new
name|ServicePropertiesFunction
argument_list|()
argument_list|)
expr_stmt|;
name|addFunction
argument_list|(
operator|new
name|ServiceHostPropertiesFunction
argument_list|()
argument_list|)
expr_stmt|;
name|addFunction
argument_list|(
operator|new
name|ServicePortPropertiesFunction
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|PropertiesComponent (String location)
specifier|public
name|PropertiesComponent
parameter_list|(
name|String
name|location
parameter_list|)
block|{
name|this
argument_list|()
expr_stmt|;
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
name|this
argument_list|()
expr_stmt|;
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
name|List
argument_list|<
name|PropertiesLocation
argument_list|>
name|paths
init|=
name|locations
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
if|if
condition|(
name|locations
operator|!=
literal|null
condition|)
block|{
name|log
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
name|Arrays
operator|.
name|stream
argument_list|(
name|locations
operator|.
name|split
argument_list|(
literal|","
argument_list|)
argument_list|)
operator|.
name|map
argument_list|(
name|PropertiesLocation
operator|::
operator|new
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
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
name|log
operator|.
name|debug
argument_list|(
literal|"Endpoint uri parsed as: {}"
argument_list|,
name|endpointUri
argument_list|)
expr_stmt|;
name|Endpoint
name|delegate
init|=
name|getCamelContext
argument_list|()
operator|.
name|getEndpoint
argument_list|(
name|endpointUri
argument_list|)
decl_stmt|;
name|PropertiesEndpoint
name|answer
init|=
operator|new
name|PropertiesEndpoint
argument_list|(
name|uri
argument_list|,
name|delegate
argument_list|,
name|this
argument_list|)
decl_stmt|;
name|setProperties
argument_list|(
name|answer
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
return|return
name|answer
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
DECL|method|parseUri (String uri, String... uris)
specifier|public
name|String
name|parseUri
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
modifier|...
name|uris
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|parseUri
argument_list|(
name|uri
argument_list|,
name|uris
operator|!=
literal|null
condition|?
name|Arrays
operator|.
name|stream
argument_list|(
name|uris
argument_list|)
operator|.
name|map
argument_list|(
name|PropertiesLocation
operator|::
operator|new
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
else|:
name|Collections
operator|.
name|emptyList
argument_list|()
argument_list|)
return|;
block|}
DECL|method|parseUri (String uri, List<PropertiesLocation> paths)
specifier|public
name|String
name|parseUri
parameter_list|(
name|String
name|uri
parameter_list|,
name|List
argument_list|<
name|PropertiesLocation
argument_list|>
name|paths
parameter_list|)
throws|throws
name|Exception
block|{
name|Properties
name|prop
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
comment|// use initial properties
if|if
condition|(
name|initialProperties
operator|!=
literal|null
condition|)
block|{
name|prop
operator|.
name|putAll
argument_list|(
name|initialProperties
argument_list|)
expr_stmt|;
block|}
comment|// use locations
if|if
condition|(
name|paths
operator|!=
literal|null
condition|)
block|{
comment|// location may contain JVM system property or OS environment variables
comment|// so we need to parse those
name|List
argument_list|<
name|PropertiesLocation
argument_list|>
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
name|Properties
name|locationsProp
init|=
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
decl_stmt|;
if|if
condition|(
name|locationsProp
operator|==
literal|null
condition|)
block|{
name|locationsProp
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
name|locationsProp
argument_list|)
expr_stmt|;
block|}
block|}
name|prop
operator|.
name|putAll
argument_list|(
name|locationsProp
argument_list|)
expr_stmt|;
block|}
comment|// use override properties
if|if
condition|(
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
name|log
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
name|propertyPrefixResolved
argument_list|,
name|propertySuffixResolved
argument_list|,
name|fallbackToUnaugmentedProperty
argument_list|,
name|defaultFallbackEnabled
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
name|List
argument_list|<
name|PropertiesLocation
argument_list|>
name|getLocations
parameter_list|()
block|{
return|return
name|locations
return|;
block|}
comment|/**      * A list of locations to load properties.      * This option will override any default locations and only use the locations from this option.      */
DECL|method|setLocations (List<PropertiesLocation> locations)
specifier|public
name|void
name|setLocations
parameter_list|(
name|List
argument_list|<
name|PropertiesLocation
argument_list|>
name|locations
parameter_list|)
block|{
name|this
operator|.
name|locations
operator|=
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|locations
argument_list|)
expr_stmt|;
block|}
comment|/**      * A list of locations to load properties.      * This option will override any default locations and only use the locations from this option.      */
DECL|method|setLocations (String[] locationStrings)
specifier|public
name|void
name|setLocations
parameter_list|(
name|String
index|[]
name|locationStrings
parameter_list|)
block|{
name|List
argument_list|<
name|PropertiesLocation
argument_list|>
name|locations
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|locationStrings
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|locationString
range|:
name|locationStrings
control|)
block|{
name|locations
operator|.
name|add
argument_list|(
operator|new
name|PropertiesLocation
argument_list|(
name|locationString
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|setLocations
argument_list|(
name|locations
argument_list|)
expr_stmt|;
block|}
comment|/**      * A list of locations to load properties.      * This option will override any default locations and only use the locations from this option.      */
DECL|method|setLocations (Collection<String> locationStrings)
specifier|public
name|void
name|setLocations
parameter_list|(
name|Collection
argument_list|<
name|String
argument_list|>
name|locationStrings
parameter_list|)
block|{
name|List
argument_list|<
name|PropertiesLocation
argument_list|>
name|locations
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|locationStrings
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|locationString
range|:
name|locationStrings
control|)
block|{
name|locations
operator|.
name|add
argument_list|(
operator|new
name|PropertiesLocation
argument_list|(
name|locationString
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|setLocations
argument_list|(
name|locations
argument_list|)
expr_stmt|;
block|}
comment|/**      * A list of locations to load properties. You can use comma to separate multiple locations.      * This option will override any default locations and only use the locations from this option.      */
DECL|method|setLocation (String location)
specifier|public
name|void
name|setLocation
parameter_list|(
name|String
name|location
parameter_list|)
block|{
if|if
condition|(
name|location
operator|!=
literal|null
condition|)
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
block|}
DECL|method|getEncoding ()
specifier|public
name|String
name|getEncoding
parameter_list|()
block|{
return|return
name|encoding
return|;
block|}
comment|/**      * Encoding to use when loading properties file from the file system or classpath.      *<p/>      * If no encoding has been set, then the properties files is loaded using ISO-8859-1 encoding (latin-1)      * as documented by {@link java.util.Properties#load(java.io.InputStream)}      */
DECL|method|setEncoding (String encoding)
specifier|public
name|void
name|setEncoding
parameter_list|(
name|String
name|encoding
parameter_list|)
block|{
name|this
operator|.
name|encoding
operator|=
name|encoding
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
comment|/**      * To use a custom PropertiesResolver      */
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
comment|/**      * To use a custom PropertiesParser      */
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
comment|/**      * Whether or not to cache loaded properties. The default value is true.      */
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
comment|/**      * Optional prefix prepended to property names before resolution.      */
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
name|this
operator|.
name|propertyPrefixResolved
operator|=
name|propertyPrefix
expr_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|this
operator|.
name|propertyPrefix
argument_list|)
condition|)
block|{
name|this
operator|.
name|propertyPrefixResolved
operator|=
name|FilePathResolver
operator|.
name|resolvePath
argument_list|(
name|this
operator|.
name|propertyPrefix
argument_list|)
expr_stmt|;
block|}
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
comment|/**      * Optional suffix appended to property names before resolution.      */
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
name|this
operator|.
name|propertySuffixResolved
operator|=
name|propertySuffix
expr_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|this
operator|.
name|propertySuffix
argument_list|)
condition|)
block|{
name|this
operator|.
name|propertySuffixResolved
operator|=
name|FilePathResolver
operator|.
name|resolvePath
argument_list|(
name|this
operator|.
name|propertySuffix
argument_list|)
expr_stmt|;
block|}
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
comment|/**      * If true, first attempt resolution of property name augmented with propertyPrefix and propertySuffix      * before falling back the plain property name specified. If false, only the augmented property name is searched.      */
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
DECL|method|isDefaultFallbackEnabled ()
specifier|public
name|boolean
name|isDefaultFallbackEnabled
parameter_list|()
block|{
return|return
name|defaultFallbackEnabled
return|;
block|}
comment|/**      * If false, the component does not attempt to find a default for the key by looking after the colon separator.      */
DECL|method|setDefaultFallbackEnabled (boolean defaultFallbackEnabled)
specifier|public
name|void
name|setDefaultFallbackEnabled
parameter_list|(
name|boolean
name|defaultFallbackEnabled
parameter_list|)
block|{
name|this
operator|.
name|defaultFallbackEnabled
operator|=
name|defaultFallbackEnabled
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
comment|/**      * Whether to silently ignore if a location cannot be located, such as a properties file not found.      */
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
DECL|method|getInitialProperties ()
specifier|public
name|Properties
name|getInitialProperties
parameter_list|()
block|{
return|return
name|initialProperties
return|;
block|}
comment|/**      * Sets initial properties which will be used before any locations are resolved.      *      * @param initialProperties properties that are added first      */
DECL|method|setInitialProperties (Properties initialProperties)
specifier|public
name|void
name|setInitialProperties
parameter_list|(
name|Properties
name|initialProperties
parameter_list|)
block|{
name|this
operator|.
name|initialProperties
operator|=
name|initialProperties
expr_stmt|;
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
comment|/**      * Gets the functions registered in this properties component.      */
DECL|method|getFunctions ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|PropertiesFunction
argument_list|>
name|getFunctions
parameter_list|()
block|{
return|return
name|functions
return|;
block|}
comment|/**      * Registers the {@link org.apache.camel.component.properties.PropertiesFunction} as a function to this component.      */
DECL|method|addFunction (PropertiesFunction function)
specifier|public
name|void
name|addFunction
parameter_list|(
name|PropertiesFunction
name|function
parameter_list|)
block|{
name|this
operator|.
name|functions
operator|.
name|put
argument_list|(
name|function
operator|.
name|getName
argument_list|()
argument_list|,
name|function
argument_list|)
expr_stmt|;
block|}
comment|/**      * Is there a {@link org.apache.camel.component.properties.PropertiesFunction} with the given name?      */
DECL|method|hasFunction (String name)
specifier|public
name|boolean
name|hasFunction
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|functions
operator|.
name|containsKey
argument_list|(
name|name
argument_list|)
return|;
block|}
DECL|method|getSystemPropertiesMode ()
specifier|public
name|int
name|getSystemPropertiesMode
parameter_list|()
block|{
return|return
name|systemPropertiesMode
return|;
block|}
comment|/**      * Sets the system property mode.      *      * @see #SYSTEM_PROPERTIES_MODE_NEVER      * @see #SYSTEM_PROPERTIES_MODE_FALLBACK      * @see #SYSTEM_PROPERTIES_MODE_OVERRIDE      */
DECL|method|setSystemPropertiesMode (int systemPropertiesMode)
specifier|public
name|void
name|setSystemPropertiesMode
parameter_list|(
name|int
name|systemPropertiesMode
parameter_list|)
block|{
name|this
operator|.
name|systemPropertiesMode
operator|=
name|systemPropertiesMode
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|isResolvePropertyPlaceholders ()
specifier|public
name|boolean
name|isResolvePropertyPlaceholders
parameter_list|()
block|{
comment|// its chicken and egg, we cannot resolve placeholders on ourselves
return|return
literal|false
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
name|systemPropertiesMode
operator|!=
name|SYSTEM_PROPERTIES_MODE_NEVER
operator|&&
name|systemPropertiesMode
operator|!=
name|SYSTEM_PROPERTIES_MODE_FALLBACK
operator|&&
name|systemPropertiesMode
operator|!=
name|SYSTEM_PROPERTIES_MODE_OVERRIDE
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Option systemPropertiesMode has invalid value: "
operator|+
name|systemPropertiesMode
argument_list|)
throw|;
block|}
comment|// inject the component to the parser
if|if
condition|(
name|propertiesParser
operator|instanceof
name|DefaultPropertiesParser
condition|)
block|{
operator|(
operator|(
name|DefaultPropertiesParser
operator|)
name|propertiesParser
operator|)
operator|.
name|setPropertiesComponent
argument_list|(
name|this
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
DECL|method|parseLocations (List<PropertiesLocation> locations)
specifier|private
name|List
argument_list|<
name|PropertiesLocation
argument_list|>
name|parseLocations
parameter_list|(
name|List
argument_list|<
name|PropertiesLocation
argument_list|>
name|locations
parameter_list|)
block|{
name|List
argument_list|<
name|PropertiesLocation
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|PropertiesLocation
name|location
range|:
name|locations
control|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Parsing location: {}"
argument_list|,
name|location
argument_list|)
expr_stmt|;
try|try
block|{
name|String
name|path
init|=
name|FilePathResolver
operator|.
name|resolvePath
argument_list|(
name|location
operator|.
name|getPath
argument_list|()
argument_list|)
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Parsed location: {}"
argument_list|,
name|path
argument_list|)
expr_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|path
argument_list|)
condition|)
block|{
name|answer
operator|.
name|add
argument_list|(
operator|new
name|PropertiesLocation
argument_list|(
name|location
operator|.
name|getResolver
argument_list|()
argument_list|,
name|path
argument_list|,
name|location
operator|.
name|isOptional
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
if|if
condition|(
operator|!
name|ignoreMissingLocation
operator|&&
operator|!
name|location
operator|.
name|isOptional
argument_list|()
condition|)
block|{
throw|throw
name|e
throw|;
block|}
else|else
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Ignored missing location: {}"
argument_list|,
name|location
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// must return a not-null answer
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
name|List
argument_list|<
name|PropertiesLocation
argument_list|>
name|locations
decl_stmt|;
DECL|method|CacheKey (List<PropertiesLocation> locations)
specifier|private
name|CacheKey
parameter_list|(
name|List
argument_list|<
name|PropertiesLocation
argument_list|>
name|locations
parameter_list|)
block|{
name|this
operator|.
name|locations
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|locations
argument_list|)
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
return|return
name|locations
operator|.
name|equals
argument_list|(
name|that
operator|.
name|locations
argument_list|)
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
operator|.
name|hashCode
argument_list|()
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
name|locations
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
