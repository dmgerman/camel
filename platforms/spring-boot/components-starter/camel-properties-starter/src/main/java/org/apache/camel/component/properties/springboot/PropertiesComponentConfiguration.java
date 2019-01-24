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
name|List
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Generated
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
name|PropertiesLocation
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
name|spring
operator|.
name|boot
operator|.
name|ComponentConfigurationPropertiesCommon
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

begin_comment
comment|/**  * The properties component is used for using property placeholders in endpoint  * uris.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.SpringBootAutoConfigurationMojo"
argument_list|)
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
extends|extends
name|ComponentConfigurationPropertiesCommon
block|{
comment|/**      * Whether to enable auto configuration of the properties component. This is      * enabled by default.      */
DECL|field|enabled
specifier|private
name|Boolean
name|enabled
decl_stmt|;
comment|/**      * A list of locations to load properties. This option will override any      * default locations and only use the locations from this option.      */
DECL|field|locations
specifier|private
name|List
argument_list|<
name|PropertiesLocation
argument_list|>
name|locations
decl_stmt|;
comment|/**      * A list of locations to load properties. You can use comma to separate      * multiple locations. This option will override any default locations and      * only use the locations from this option.      */
DECL|field|location
specifier|private
name|String
name|location
decl_stmt|;
comment|/**      * Encoding to use when loading properties file from the file system or      * classpath. If no encoding has been set, then the properties files is      * loaded using ISO-8859-1 encoding (latin-1) as documented by      * java.util.Properties#load(java.io.InputStream)      */
DECL|field|encoding
specifier|private
name|String
name|encoding
decl_stmt|;
comment|/**      * To use a custom PropertiesResolver. The option is a      * org.apache.camel.component.properties.PropertiesResolver type.      */
DECL|field|propertiesResolver
specifier|private
name|String
name|propertiesResolver
decl_stmt|;
comment|/**      * To use a custom PropertiesParser. The option is a      * org.apache.camel.component.properties.PropertiesParser type.      */
DECL|field|propertiesParser
specifier|private
name|String
name|propertiesParser
decl_stmt|;
comment|/**      * Whether or not to cache loaded properties. The default value is true.      */
DECL|field|cache
specifier|private
name|Boolean
name|cache
init|=
literal|true
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
comment|/**      * If true, first attempt resolution of property name augmented with      * propertyPrefix and propertySuffix before falling back the plain property      * name specified. If false, only the augmented property name is searched.      */
DECL|field|fallbackToUnaugmentedProperty
specifier|private
name|Boolean
name|fallbackToUnaugmentedProperty
init|=
literal|true
decl_stmt|;
comment|/**      * If false, the component does not attempt to find a default for the key by      * looking after the colon separator.      */
DECL|field|defaultFallbackEnabled
specifier|private
name|Boolean
name|defaultFallbackEnabled
init|=
literal|true
decl_stmt|;
comment|/**      * Whether to silently ignore if a location cannot be located, such as a      * properties file not found.      */
DECL|field|ignoreMissingLocation
specifier|private
name|Boolean
name|ignoreMissingLocation
init|=
literal|false
decl_stmt|;
comment|/**      * Sets the value of the prefix token used to identify properties to      * replace. Setting a value of null restores the default token (link      * DEFAULT_PREFIX_TOKEN).      */
DECL|field|prefixToken
specifier|private
name|String
name|prefixToken
init|=
literal|"{{"
decl_stmt|;
comment|/**      * Sets the value of the suffix token used to identify properties to      * replace. Setting a value of null restores the default token (link      * DEFAULT_SUFFIX_TOKEN).      */
DECL|field|suffixToken
specifier|private
name|String
name|suffixToken
init|=
literal|"}}"
decl_stmt|;
comment|/**      * Sets initial properties which will be used before any locations are      * resolved. The option is a java.util.Properties type.      */
DECL|field|initialProperties
specifier|private
name|String
name|initialProperties
decl_stmt|;
comment|/**      * Sets a special list of override properties that take precedence and will      * use first, if a property exist. The option is a java.util.Properties      * type.      */
DECL|field|overrideProperties
specifier|private
name|String
name|overrideProperties
decl_stmt|;
comment|/**      * Sets the system property mode.      */
DECL|field|systemPropertiesMode
specifier|private
name|Integer
name|systemPropertiesMode
init|=
literal|2
decl_stmt|;
comment|/**      * Whether the component should resolve property placeholders on itself when      * starting. Only properties which are of String type can use property      * placeholders.      */
DECL|field|resolvePropertyPlaceholders
specifier|private
name|Boolean
name|resolvePropertyPlaceholders
init|=
literal|true
decl_stmt|;
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
name|String
name|getPropertiesResolver
parameter_list|()
block|{
return|return
name|propertiesResolver
return|;
block|}
DECL|method|setPropertiesResolver (String propertiesResolver)
specifier|public
name|void
name|setPropertiesResolver
parameter_list|(
name|String
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
name|String
name|getPropertiesParser
parameter_list|()
block|{
return|return
name|propertiesParser
return|;
block|}
DECL|method|setPropertiesParser (String propertiesParser)
specifier|public
name|void
name|setPropertiesParser
parameter_list|(
name|String
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
DECL|method|getDefaultFallbackEnabled ()
specifier|public
name|Boolean
name|getDefaultFallbackEnabled
parameter_list|()
block|{
return|return
name|defaultFallbackEnabled
return|;
block|}
DECL|method|setDefaultFallbackEnabled (Boolean defaultFallbackEnabled)
specifier|public
name|void
name|setDefaultFallbackEnabled
parameter_list|(
name|Boolean
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
name|String
name|getInitialProperties
parameter_list|()
block|{
return|return
name|initialProperties
return|;
block|}
DECL|method|setInitialProperties (String initialProperties)
specifier|public
name|void
name|setInitialProperties
parameter_list|(
name|String
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
name|String
name|getOverrideProperties
parameter_list|()
block|{
return|return
name|overrideProperties
return|;
block|}
DECL|method|setOverrideProperties (String overrideProperties)
specifier|public
name|void
name|setOverrideProperties
parameter_list|(
name|String
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
DECL|method|getResolvePropertyPlaceholders ()
specifier|public
name|Boolean
name|getResolvePropertyPlaceholders
parameter_list|()
block|{
return|return
name|resolvePropertyPlaceholders
return|;
block|}
DECL|method|setResolvePropertyPlaceholders ( Boolean resolvePropertyPlaceholders)
specifier|public
name|void
name|setResolvePropertyPlaceholders
parameter_list|(
name|Boolean
name|resolvePropertyPlaceholders
parameter_list|)
block|{
name|this
operator|.
name|resolvePropertyPlaceholders
operator|=
name|resolvePropertyPlaceholders
expr_stmt|;
block|}
block|}
end_class

end_unit
