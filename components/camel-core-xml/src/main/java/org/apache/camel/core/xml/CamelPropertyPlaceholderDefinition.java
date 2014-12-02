begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_comment
comment|/**  *<code>PropertyPlaceholderDefinition</code> represents a&lt;propertyPlaceholder/&gt element.  *  * @version   */
end_comment

begin_class
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
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|location
specifier|private
name|String
name|location
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|cache
specifier|private
name|Boolean
name|cache
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|ignoreMissingLocation
specifier|private
name|Boolean
name|ignoreMissingLocation
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|propertiesResolverRef
specifier|private
name|String
name|propertiesResolverRef
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
DECL|field|propertyPrefix
specifier|private
name|String
name|propertyPrefix
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|propertySuffix
specifier|private
name|String
name|propertySuffix
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|fallbackToUnaugmentedProperty
specifier|private
name|Boolean
name|fallbackToUnaugmentedProperty
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|prefixToken
specifier|private
name|String
name|prefixToken
decl_stmt|;
annotation|@
name|XmlAttribute
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
DECL|method|isCache ()
specifier|public
name|Boolean
name|isCache
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
DECL|method|getPropertiesResolverRef ()
specifier|public
name|String
name|getPropertiesResolverRef
parameter_list|()
block|{
return|return
name|propertiesResolverRef
return|;
block|}
DECL|method|setPropertiesResolverRef (String propertiesResolverRef)
specifier|public
name|void
name|setPropertiesResolverRef
parameter_list|(
name|String
name|propertiesResolverRef
parameter_list|)
block|{
name|this
operator|.
name|propertiesResolverRef
operator|=
name|propertiesResolverRef
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
name|Boolean
name|isFallbackToUnaugmentedProperty
parameter_list|()
block|{
return|return
name|fallbackToUnaugmentedProperty
return|;
block|}
DECL|method|setFallbackToUnaugmentedProperty (Boolean fallbackToUnaugmentedProperty)
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
block|}
end_class

end_unit

