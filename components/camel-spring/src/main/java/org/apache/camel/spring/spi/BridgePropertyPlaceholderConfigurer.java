begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.spi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|spi
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
name|component
operator|.
name|properties
operator|.
name|AugmentedPropertyNameAwarePropertiesParser
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
name|beans
operator|.
name|BeansException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|config
operator|.
name|ConfigurableListableBeanFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|config
operator|.
name|PropertyPlaceholderConfigurer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|core
operator|.
name|Constants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|util
operator|.
name|PropertyPlaceholderHelper
import|;
end_import

begin_comment
comment|/**  * A {@link PropertyPlaceholderConfigurer} that bridges Camel's<a href="http://camel.apache.org/using-propertyplaceholder.html">  * property placeholder</a> with the Spring property placeholder mechanism.  */
end_comment

begin_class
DECL|class|BridgePropertyPlaceholderConfigurer
specifier|public
class|class
name|BridgePropertyPlaceholderConfigurer
extends|extends
name|PropertyPlaceholderConfigurer
implements|implements
name|PropertiesResolver
implements|,
name|AugmentedPropertyNameAwarePropertiesParser
block|{
comment|// NOTE: this class must be in the spi package as if its in the root package, then Spring fails to parse the XML
comment|// files due some weird spring issue. But that is okay as having this class in the spi package is fine anyway.
DECL|field|properties
specifier|private
specifier|final
name|Properties
name|properties
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
DECL|field|resolver
specifier|private
name|PropertiesResolver
name|resolver
decl_stmt|;
DECL|field|parser
specifier|private
name|PropertiesParser
name|parser
decl_stmt|;
DECL|field|id
specifier|private
name|String
name|id
decl_stmt|;
DECL|field|helper
specifier|private
name|PropertyPlaceholderHelper
name|helper
decl_stmt|;
comment|// to support both Spring 3.0 / 3.1+ we need to keep track of these as they have private modified in Spring 3.0
DECL|field|configuredPlaceholderPrefix
specifier|private
name|String
name|configuredPlaceholderPrefix
decl_stmt|;
DECL|field|configuredPlaceholderSuffix
specifier|private
name|String
name|configuredPlaceholderSuffix
decl_stmt|;
DECL|field|configuredValueSeparator
specifier|private
name|String
name|configuredValueSeparator
decl_stmt|;
DECL|field|configuredIgnoreUnresolvablePlaceholders
specifier|private
name|Boolean
name|configuredIgnoreUnresolvablePlaceholders
decl_stmt|;
DECL|field|systemPropertiesMode
specifier|private
name|int
name|systemPropertiesMode
init|=
name|SYSTEM_PROPERTIES_MODE_FALLBACK
decl_stmt|;
DECL|field|ignoreResourceNotFound
specifier|private
name|Boolean
name|ignoreResourceNotFound
decl_stmt|;
annotation|@
name|Override
DECL|method|processProperties (ConfigurableListableBeanFactory beanFactoryToProcess, Properties props)
specifier|protected
name|void
name|processProperties
parameter_list|(
name|ConfigurableListableBeanFactory
name|beanFactoryToProcess
parameter_list|,
name|Properties
name|props
parameter_list|)
throws|throws
name|BeansException
block|{
name|super
operator|.
name|processProperties
argument_list|(
name|beanFactoryToProcess
argument_list|,
name|props
argument_list|)
expr_stmt|;
comment|// store all the spring properties so we can refer to them later
name|properties
operator|.
name|putAll
argument_list|(
name|props
argument_list|)
expr_stmt|;
comment|// create helper
name|helper
operator|=
operator|new
name|PropertyPlaceholderHelper
argument_list|(
name|configuredPlaceholderPrefix
operator|!=
literal|null
condition|?
name|configuredPlaceholderPrefix
else|:
name|DEFAULT_PLACEHOLDER_PREFIX
argument_list|,
name|configuredPlaceholderSuffix
operator|!=
literal|null
condition|?
name|configuredPlaceholderSuffix
else|:
name|DEFAULT_PLACEHOLDER_SUFFIX
argument_list|,
name|configuredValueSeparator
operator|!=
literal|null
condition|?
name|configuredValueSeparator
else|:
name|DEFAULT_VALUE_SEPARATOR
argument_list|,
name|configuredIgnoreUnresolvablePlaceholders
operator|!=
literal|null
condition|?
name|configuredIgnoreUnresolvablePlaceholders
else|:
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setBeanName (String beanName)
specifier|public
name|void
name|setBeanName
parameter_list|(
name|String
name|beanName
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|beanName
expr_stmt|;
name|super
operator|.
name|setBeanName
argument_list|(
name|beanName
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setSystemPropertiesModeName (String constantName)
specifier|public
name|void
name|setSystemPropertiesModeName
parameter_list|(
name|String
name|constantName
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
name|super
operator|.
name|setSystemPropertiesModeName
argument_list|(
name|constantName
argument_list|)
expr_stmt|;
name|Constants
name|constants
init|=
operator|new
name|Constants
argument_list|(
name|PropertyPlaceholderConfigurer
operator|.
name|class
argument_list|)
decl_stmt|;
name|this
operator|.
name|systemPropertiesMode
operator|=
name|constants
operator|.
name|asNumber
argument_list|(
name|constantName
argument_list|)
operator|.
name|intValue
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setSystemPropertiesMode (int systemPropertiesMode)
specifier|public
name|void
name|setSystemPropertiesMode
parameter_list|(
name|int
name|systemPropertiesMode
parameter_list|)
block|{
name|super
operator|.
name|setSystemPropertiesMode
argument_list|(
name|systemPropertiesMode
argument_list|)
expr_stmt|;
name|this
operator|.
name|systemPropertiesMode
operator|=
name|systemPropertiesMode
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setPlaceholderPrefix (String placeholderPrefix)
specifier|public
name|void
name|setPlaceholderPrefix
parameter_list|(
name|String
name|placeholderPrefix
parameter_list|)
block|{
name|super
operator|.
name|setPlaceholderPrefix
argument_list|(
name|placeholderPrefix
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuredPlaceholderPrefix
operator|=
name|placeholderPrefix
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setPlaceholderSuffix (String placeholderSuffix)
specifier|public
name|void
name|setPlaceholderSuffix
parameter_list|(
name|String
name|placeholderSuffix
parameter_list|)
block|{
name|super
operator|.
name|setPlaceholderSuffix
argument_list|(
name|placeholderSuffix
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuredPlaceholderSuffix
operator|=
name|placeholderSuffix
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setValueSeparator (String valueSeparator)
specifier|public
name|void
name|setValueSeparator
parameter_list|(
name|String
name|valueSeparator
parameter_list|)
block|{
name|super
operator|.
name|setValueSeparator
argument_list|(
name|valueSeparator
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuredValueSeparator
operator|=
name|valueSeparator
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setIgnoreUnresolvablePlaceholders (boolean ignoreUnresolvablePlaceholders)
specifier|public
name|void
name|setIgnoreUnresolvablePlaceholders
parameter_list|(
name|boolean
name|ignoreUnresolvablePlaceholders
parameter_list|)
block|{
name|super
operator|.
name|setIgnoreUnresolvablePlaceholders
argument_list|(
name|ignoreUnresolvablePlaceholders
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuredIgnoreUnresolvablePlaceholders
operator|=
name|ignoreUnresolvablePlaceholders
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setIgnoreResourceNotFound (boolean ignoreResourceNotFound)
specifier|public
name|void
name|setIgnoreResourceNotFound
parameter_list|(
name|boolean
name|ignoreResourceNotFound
parameter_list|)
block|{
name|super
operator|.
name|setIgnoreResourceNotFound
argument_list|(
name|ignoreResourceNotFound
argument_list|)
expr_stmt|;
name|this
operator|.
name|ignoreResourceNotFound
operator|=
name|ignoreResourceNotFound
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|resolveProperties (CamelContext context, boolean ignoreMissingLocation, String... uri)
specifier|public
name|Properties
name|resolveProperties
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|boolean
name|ignoreMissingLocation
parameter_list|,
name|String
modifier|...
name|uri
parameter_list|)
throws|throws
name|Exception
block|{
comment|// return the spring properties, if it
name|Properties
name|answer
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|u
range|:
name|uri
control|)
block|{
name|String
name|ref
init|=
literal|"ref:"
operator|+
name|id
decl_stmt|;
if|if
condition|(
name|ref
operator|.
name|equals
argument_list|(
name|u
argument_list|)
condition|)
block|{
name|answer
operator|.
name|putAll
argument_list|(
name|properties
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|resolver
operator|!=
literal|null
condition|)
block|{
name|boolean
name|flag
init|=
name|ignoreMissingLocation
decl_stmt|;
comment|// Override the setting by using ignoreResourceNotFound
if|if
condition|(
name|ignoreResourceNotFound
operator|!=
literal|null
condition|)
block|{
name|flag
operator|=
name|ignoreResourceNotFound
expr_stmt|;
block|}
name|Properties
name|p
init|=
name|resolver
operator|.
name|resolveProperties
argument_list|(
name|context
argument_list|,
name|flag
argument_list|,
name|u
argument_list|)
decl_stmt|;
if|if
condition|(
name|p
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|putAll
argument_list|(
name|p
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// must not return null
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|parseUri (String text, Properties properties, String prefixToken, String suffixToken, String propertyPrefix, String propertySuffix, boolean fallbackToUnaugmentedProperty)
specifier|public
name|String
name|parseUri
parameter_list|(
name|String
name|text
parameter_list|,
name|Properties
name|properties
parameter_list|,
name|String
name|prefixToken
parameter_list|,
name|String
name|suffixToken
parameter_list|,
name|String
name|propertyPrefix
parameter_list|,
name|String
name|propertySuffix
parameter_list|,
name|boolean
name|fallbackToUnaugmentedProperty
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
comment|// first let Camel parse the text as it may contain Camel placeholders
name|String
name|answer
decl_stmt|;
if|if
condition|(
name|parser
operator|instanceof
name|AugmentedPropertyNameAwarePropertiesParser
condition|)
block|{
name|answer
operator|=
operator|(
operator|(
name|AugmentedPropertyNameAwarePropertiesParser
operator|)
name|parser
operator|)
operator|.
name|parseUri
argument_list|(
name|text
argument_list|,
name|properties
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
expr_stmt|;
block|}
else|else
block|{
name|answer
operator|=
name|parser
operator|.
name|parseUri
argument_list|(
name|text
argument_list|,
name|properties
argument_list|,
name|prefixToken
argument_list|,
name|suffixToken
argument_list|)
expr_stmt|;
block|}
comment|// then let Spring parse it to resolve any Spring placeholders
if|if
condition|(
name|answer
operator|!=
literal|null
condition|)
block|{
name|answer
operator|=
name|springResolvePlaceholders
argument_list|(
name|answer
argument_list|,
name|properties
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|answer
operator|=
name|springResolvePlaceholders
argument_list|(
name|text
argument_list|,
name|properties
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|parseUri (String text, Properties properties, String prefixToken, String suffixToken)
specifier|public
name|String
name|parseUri
parameter_list|(
name|String
name|text
parameter_list|,
name|Properties
name|properties
parameter_list|,
name|String
name|prefixToken
parameter_list|,
name|String
name|suffixToken
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
name|String
name|answer
init|=
name|parser
operator|.
name|parseUri
argument_list|(
name|text
argument_list|,
name|properties
argument_list|,
name|prefixToken
argument_list|,
name|suffixToken
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|!=
literal|null
condition|)
block|{
name|answer
operator|=
name|springResolvePlaceholders
argument_list|(
name|answer
argument_list|,
name|properties
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|answer
operator|=
name|springResolvePlaceholders
argument_list|(
name|text
argument_list|,
name|properties
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|parseProperty (String key, String value, Properties properties)
specifier|public
name|String
name|parseProperty
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|value
parameter_list|,
name|Properties
name|properties
parameter_list|)
block|{
name|String
name|answer
init|=
name|parser
operator|.
name|parseProperty
argument_list|(
name|key
argument_list|,
name|value
argument_list|,
name|properties
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|!=
literal|null
condition|)
block|{
name|answer
operator|=
name|springResolvePlaceholders
argument_list|(
name|answer
argument_list|,
name|properties
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|answer
operator|=
name|springResolvePlaceholders
argument_list|(
name|value
argument_list|,
name|properties
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
comment|/**      * Resolves the placeholders using Spring's property placeholder functionality.      *      * @param text   the text which may contain spring placeholders      * @param properties the properties      * @return the parsed text with replaced placeholders, or the original text as is      */
DECL|method|springResolvePlaceholders (String text, Properties properties)
specifier|protected
name|String
name|springResolvePlaceholders
parameter_list|(
name|String
name|text
parameter_list|,
name|Properties
name|properties
parameter_list|)
block|{
return|return
name|helper
operator|.
name|replacePlaceholders
argument_list|(
name|text
argument_list|,
operator|new
name|BridgePropertyPlaceholderResolver
argument_list|(
name|properties
argument_list|)
argument_list|)
return|;
block|}
DECL|method|setResolver (PropertiesResolver resolver)
specifier|public
name|void
name|setResolver
parameter_list|(
name|PropertiesResolver
name|resolver
parameter_list|)
block|{
name|this
operator|.
name|resolver
operator|=
name|resolver
expr_stmt|;
block|}
DECL|method|setParser (PropertiesParser parser)
specifier|public
name|void
name|setParser
parameter_list|(
name|PropertiesParser
name|parser
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|parser
operator|!=
literal|null
condition|)
block|{
comment|// use a bridge if there is already a parser configured
name|this
operator|.
name|parser
operator|=
operator|new
name|BridgePropertiesParser
argument_list|(
name|this
operator|.
name|parser
argument_list|,
name|parser
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|parser
operator|=
name|parser
expr_stmt|;
block|}
block|}
DECL|class|BridgePropertyPlaceholderResolver
specifier|private
class|class
name|BridgePropertyPlaceholderResolver
implements|implements
name|PropertyPlaceholderHelper
operator|.
name|PlaceholderResolver
block|{
DECL|field|properties
specifier|private
specifier|final
name|Properties
name|properties
decl_stmt|;
DECL|method|BridgePropertyPlaceholderResolver (Properties properties)
specifier|public
name|BridgePropertyPlaceholderResolver
parameter_list|(
name|Properties
name|properties
parameter_list|)
block|{
name|this
operator|.
name|properties
operator|=
name|properties
expr_stmt|;
block|}
DECL|method|resolvePlaceholder (String placeholderName)
specifier|public
name|String
name|resolvePlaceholder
parameter_list|(
name|String
name|placeholderName
parameter_list|)
block|{
name|String
name|propVal
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|systemPropertiesMode
operator|==
name|SYSTEM_PROPERTIES_MODE_OVERRIDE
condition|)
block|{
name|propVal
operator|=
name|resolveSystemProperty
argument_list|(
name|placeholderName
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|propVal
operator|==
literal|null
condition|)
block|{
name|propVal
operator|=
operator|(
name|String
operator|)
name|properties
operator|.
name|get
argument_list|(
name|placeholderName
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|propVal
operator|==
literal|null
operator|&&
name|systemPropertiesMode
operator|==
name|SYSTEM_PROPERTIES_MODE_FALLBACK
condition|)
block|{
name|propVal
operator|=
name|resolveSystemProperty
argument_list|(
name|placeholderName
argument_list|)
expr_stmt|;
block|}
return|return
name|propVal
return|;
block|}
block|}
DECL|class|BridgePropertiesParser
specifier|private
class|class
name|BridgePropertiesParser
implements|implements
name|PropertiesParser
block|{
DECL|field|delegate
specifier|private
specifier|final
name|PropertiesParser
name|delegate
decl_stmt|;
DECL|field|parser
specifier|private
specifier|final
name|PropertiesParser
name|parser
decl_stmt|;
DECL|method|BridgePropertiesParser (PropertiesParser delegate, PropertiesParser parser)
specifier|private
name|BridgePropertiesParser
parameter_list|(
name|PropertiesParser
name|delegate
parameter_list|,
name|PropertiesParser
name|parser
parameter_list|)
block|{
name|this
operator|.
name|delegate
operator|=
name|delegate
expr_stmt|;
name|this
operator|.
name|parser
operator|=
name|parser
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|parseUri (String text, Properties properties, String prefixToken, String suffixToken)
specifier|public
name|String
name|parseUri
parameter_list|(
name|String
name|text
parameter_list|,
name|Properties
name|properties
parameter_list|,
name|String
name|prefixToken
parameter_list|,
name|String
name|suffixToken
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
name|String
name|answer
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|delegate
operator|!=
literal|null
condition|)
block|{
name|answer
operator|=
name|delegate
operator|.
name|parseUri
argument_list|(
name|text
argument_list|,
name|properties
argument_list|,
name|prefixToken
argument_list|,
name|suffixToken
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|answer
operator|!=
literal|null
condition|)
block|{
name|text
operator|=
name|answer
expr_stmt|;
block|}
return|return
name|parser
operator|.
name|parseUri
argument_list|(
name|text
argument_list|,
name|properties
argument_list|,
name|prefixToken
argument_list|,
name|suffixToken
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|parseProperty (String key, String value, Properties properties)
specifier|public
name|String
name|parseProperty
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|value
parameter_list|,
name|Properties
name|properties
parameter_list|)
block|{
name|String
name|answer
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|delegate
operator|!=
literal|null
condition|)
block|{
name|answer
operator|=
name|delegate
operator|.
name|parseProperty
argument_list|(
name|key
argument_list|,
name|value
argument_list|,
name|properties
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|answer
operator|!=
literal|null
condition|)
block|{
name|value
operator|=
name|answer
expr_stmt|;
block|}
return|return
name|parser
operator|.
name|parseProperty
argument_list|(
name|key
argument_list|,
name|value
argument_list|,
name|properties
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

