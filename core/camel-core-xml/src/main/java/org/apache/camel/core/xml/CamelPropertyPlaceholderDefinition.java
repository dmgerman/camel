begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.core.xml
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|core
operator|.
name|xml
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
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessorType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAttribute
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
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
name|model
operator|.
name|IdentifiedType
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

begin_comment
comment|/**  * Properties placeholder  */
end_comment

begin_class
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"spring,configuration"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"propertyPlaceholder"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|CamelPropertyPlaceholderDefinition
specifier|public
class|class
name|CamelPropertyPlaceholderDefinition
extends|extends
name|IdentifiedType
block|{
annotation|@
name|XmlAttribute
DECL|field|location
specifier|private
name|String
name|location
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|encoding
specifier|private
name|String
name|encoding
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"false"
argument_list|)
DECL|field|ignoreMissingLocation
specifier|private
name|Boolean
name|ignoreMissingLocation
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|propertiesParserRef
specifier|private
name|String
name|propertiesParserRef
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|defaultFallbackEnabled
specifier|private
name|Boolean
name|defaultFallbackEnabled
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"{{"
argument_list|)
DECL|field|prefixToken
specifier|private
name|String
name|prefixToken
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"}}"
argument_list|)
DECL|field|suffixToken
specifier|private
name|String
name|suffixToken
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"propertiesFunction"
argument_list|)
DECL|field|functions
specifier|private
name|List
argument_list|<
name|CamelPropertyPlaceholderFunctionDefinition
argument_list|>
name|functions
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"propertiesLocation"
argument_list|)
DECL|field|locations
specifier|private
name|List
argument_list|<
name|CamelPropertyPlaceholderLocationDefinition
argument_list|>
name|locations
decl_stmt|;
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
DECL|method|getPropertiesParserRef ()
specifier|public
name|String
name|getPropertiesParserRef
parameter_list|()
block|{
return|return
name|propertiesParserRef
return|;
block|}
comment|/**      * Reference to a custom PropertiesParser to be used      */
DECL|method|setPropertiesParserRef (String propertiesParserRef)
specifier|public
name|void
name|setPropertiesParserRef
parameter_list|(
name|String
name|propertiesParserRef
parameter_list|)
block|{
name|this
operator|.
name|propertiesParserRef
operator|=
name|propertiesParserRef
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
comment|/**      * If false, the component does not attempt to find a default for the key by looking after the colon separator.      */
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
DECL|method|isIgnoreMissingLocation ()
specifier|public
name|Boolean
name|isIgnoreMissingLocation
parameter_list|()
block|{
return|return
name|ignoreMissingLocation
return|;
block|}
comment|/**      * Whether to silently ignore if a location cannot be located, such as a properties file not found.      */
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
comment|/**      * Sets the value of the prefix token used to identify properties to replace.  Setting a value of      * {@code null} restores the default token {{      */
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
comment|/**      * Sets the value of the suffix token used to identify properties to replace.  Setting a value of      * {@code null} restores the default token }}      */
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
DECL|method|getFunctions ()
specifier|public
name|List
argument_list|<
name|CamelPropertyPlaceholderFunctionDefinition
argument_list|>
name|getFunctions
parameter_list|()
block|{
return|return
name|functions
return|;
block|}
comment|/**      * List of custom properties function to use.      */
DECL|method|setFunctions (List<CamelPropertyPlaceholderFunctionDefinition> functions)
specifier|public
name|void
name|setFunctions
parameter_list|(
name|List
argument_list|<
name|CamelPropertyPlaceholderFunctionDefinition
argument_list|>
name|functions
parameter_list|)
block|{
name|this
operator|.
name|functions
operator|=
name|functions
expr_stmt|;
block|}
DECL|method|getLocations ()
specifier|public
name|List
argument_list|<
name|CamelPropertyPlaceholderLocationDefinition
argument_list|>
name|getLocations
parameter_list|()
block|{
return|return
name|locations
return|;
block|}
comment|/**      * List of property locations to use.      */
DECL|method|setLocations (List<CamelPropertyPlaceholderLocationDefinition> locations)
specifier|public
name|void
name|setLocations
parameter_list|(
name|List
argument_list|<
name|CamelPropertyPlaceholderLocationDefinition
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
block|}
end_class

end_unit

