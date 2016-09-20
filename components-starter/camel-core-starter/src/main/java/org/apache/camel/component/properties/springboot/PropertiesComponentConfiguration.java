begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.properties.springboot
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
operator|.
name|springboot
package|;
end_package

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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|properties
operator|.
name|PropertiesParser
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
name|properties
operator|.
name|PropertiesResolver
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|ConfigurationProperties
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|NestedConfigurationProperty
import|;
end_import

begin_comment
comment|/**  * The properties component is used for using property placeholders in endpoint  * uris.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.component.properties"
argument_list|)
DECL|class|PropertiesComponentConfiguration
specifier|public
class|class
name|PropertiesComponentConfiguration
block|{
comment|/**      * A list of locations to load properties. You can use comma to separate      * multiple locations. This option will override any default locations and      * only use the locations from this option.      */
DECL|field|locations
specifier|private
name|String
index|[]
name|locations
decl_stmt|;
comment|/**      * A list of locations to load properties. You can use comma to separate      * multiple locations. This option will override any default locations and      * only use the locations from this option.      */
DECL|field|location
specifier|private
name|String
name|location
decl_stmt|;
comment|/**      * Encoding to use when loading properties file from the file system or      * classpath. If no encoding has been set then the properties files is      * loaded using ISO-8859-1 encoding (latin-1) as documented by link      * java.util.Propertiesload(java.io.InputStream)      */
DECL|field|encoding
specifier|private
name|String
name|encoding
decl_stmt|;
comment|/**      * To use a custom PropertiesResolver      */
annotation|@
name|NestedConfigurationProperty
DECL|field|propertiesResolver
specifier|private
name|PropertiesResolver
name|propertiesResolver
decl_stmt|;
comment|/**      * To use a custom PropertiesParser      */
annotation|@
name|NestedConfigurationProperty
DECL|field|propertiesParser
specifier|private
name|PropertiesParser
name|propertiesParser
decl_stmt|;
comment|/**      * Whether or not to cache loaded properties. The default value is true.      */
DECL|field|cache
specifier|private
name|Boolean
name|cache
decl_stmt|;
comment|/**      * Optional prefix prepended to property names before resolution.      */
DECL|field|propertyPrefix
specifier|private
name|String
name|propertyPrefix
decl_stmt|;
comment|/**      * Optional suffix appended to property names before resolution.      */
DECL|field|propertySuffix
specifier|private
name|String
name|propertySuffix
decl_stmt|;
comment|/**      * If true first attempt resolution of property name augmented with      * propertyPrefix and propertySuffix before falling back the plain property      * name specified. If false only the augmented property name is searched.      */
DECL|field|fallbackToUnaugmentedProperty
specifier|private
name|Boolean
name|fallbackToUnaugmentedProperty
decl_stmt|;
comment|/**      * Whether to silently ignore if a location cannot be located such as a      * properties file not found.      */
DECL|field|ignoreMissingLocation
specifier|private
name|Boolean
name|ignoreMissingLocation
decl_stmt|;
comment|/**      * Sets the value of the prefix token used to identify properties to      * replace. Setting a value of null restores the default token (link link      * DEFAULT_PREFIX_TOKEN).      */
DECL|field|prefixToken
specifier|private
name|String
name|prefixToken
decl_stmt|;
comment|/**      * Sets the value of the suffix token used to identify properties to      * replace. Setting a value of null restores the default token (link link      * DEFAULT_SUFFIX_TOKEN).      */
DECL|field|suffixToken
specifier|private
name|String
name|suffixToken
decl_stmt|;
comment|/**      * Sets initial properties which will be used before any locations are      * resolved.      */
DECL|field|initialProperties
specifier|private
name|Properties
name|initialProperties
decl_stmt|;
comment|/**      * Sets a special list of override properties that take precedence and will      * use first if a property exist.      */
DECL|field|overrideProperties
specifier|private
name|Properties
name|overrideProperties
decl_stmt|;
comment|/**      * Sets the system property mode.      */
DECL|field|systemPropertiesMode
specifier|private
name|Integer
name|systemPropertiesMode
decl_stmt|;
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
DECL|method|getLocation ()
specifier|public
name|String
name|getLocation
parameter_list|()
block|{
return|return
name|location
return|;
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
name|this
operator|.
name|location
operator|=
name|location
expr_stmt|;
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
DECL|method|getCache ()
specifier|public
name|Boolean
name|getCache
parameter_list|()
block|{
return|return
name|cache
return|;
block|}
DECL|method|setCache (Boolean cache)
specifier|public
name|void
name|setCache
parameter_list|(
name|Boolean
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
DECL|method|getFallbackToUnaugmentedProperty ()
specifier|public
name|Boolean
name|getFallbackToUnaugmentedProperty
parameter_list|()
block|{
return|return
name|fallbackToUnaugmentedProperty
return|;
block|}
DECL|method|setFallbackToUnaugmentedProperty ( Boolean fallbackToUnaugmentedProperty)
specifier|public
name|void
name|setFallbackToUnaugmentedProperty
parameter_list|(
name|Boolean
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
DECL|method|getIgnoreMissingLocation ()
specifier|public
name|Boolean
name|getIgnoreMissingLocation
parameter_list|()
block|{
return|return
name|ignoreMissingLocation
return|;
block|}
DECL|method|setIgnoreMissingLocation (Boolean ignoreMissingLocation)
specifier|public
name|void
name|setIgnoreMissingLocation
parameter_list|(
name|Boolean
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
DECL|method|setPrefixToken (String prefixToken)
specifier|public
name|void
name|setPrefixToken
parameter_list|(
name|String
name|prefixToken
parameter_list|)
block|{
name|this
operator|.
name|prefixToken
operator|=
name|prefixToken
expr_stmt|;
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
DECL|method|setSuffixToken (String suffixToken)
specifier|public
name|void
name|setSuffixToken
parameter_list|(
name|String
name|suffixToken
parameter_list|)
block|{
name|this
operator|.
name|suffixToken
operator|=
name|suffixToken
expr_stmt|;
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
DECL|method|getSystemPropertiesMode ()
specifier|public
name|Integer
name|getSystemPropertiesMode
parameter_list|()
block|{
return|return
name|systemPropertiesMode
return|;
block|}
DECL|method|setSystemPropertiesMode (Integer systemPropertiesMode)
specifier|public
name|void
name|setSystemPropertiesMode
parameter_list|(
name|Integer
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
block|}
end_class

end_unit

