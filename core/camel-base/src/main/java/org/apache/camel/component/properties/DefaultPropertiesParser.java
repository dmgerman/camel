begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|util
operator|.
name|HashSet
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
name|StringHelper
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

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|PropertiesComponent
operator|.
name|PREFIX_TOKEN
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
name|spi
operator|.
name|PropertiesComponent
operator|.
name|SUFFIX_TOKEN
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
name|IOHelper
operator|.
name|lookupEnvironmentVariable
import|;
end_import

begin_comment
comment|/**  * A parser to parse a string which contains property placeholders.  */
end_comment

begin_class
DECL|class|DefaultPropertiesParser
specifier|public
class|class
name|DefaultPropertiesParser
implements|implements
name|PropertiesParser
block|{
DECL|field|GET_OR_ELSE_TOKEN
specifier|private
specifier|static
specifier|final
name|String
name|GET_OR_ELSE_TOKEN
init|=
literal|":"
decl_stmt|;
DECL|field|log
specifier|protected
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|propertiesComponent
specifier|private
name|PropertiesComponent
name|propertiesComponent
decl_stmt|;
DECL|method|DefaultPropertiesParser ()
specifier|public
name|DefaultPropertiesParser
parameter_list|()
block|{     }
DECL|method|DefaultPropertiesParser (PropertiesComponent propertiesComponent)
specifier|public
name|DefaultPropertiesParser
parameter_list|(
name|PropertiesComponent
name|propertiesComponent
parameter_list|)
block|{
name|this
operator|.
name|propertiesComponent
operator|=
name|propertiesComponent
expr_stmt|;
block|}
DECL|method|getPropertiesComponent ()
specifier|public
name|PropertiesComponent
name|getPropertiesComponent
parameter_list|()
block|{
return|return
name|propertiesComponent
return|;
block|}
DECL|method|setPropertiesComponent (PropertiesComponent propertiesComponent)
specifier|public
name|void
name|setPropertiesComponent
parameter_list|(
name|PropertiesComponent
name|propertiesComponent
parameter_list|)
block|{
name|this
operator|.
name|propertiesComponent
operator|=
name|propertiesComponent
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|parseUri (String text, PropertiesLookup properties, boolean defaultFallbackEnabled)
specifier|public
name|String
name|parseUri
parameter_list|(
name|String
name|text
parameter_list|,
name|PropertiesLookup
name|properties
parameter_list|,
name|boolean
name|defaultFallbackEnabled
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
name|ParsingContext
name|context
init|=
operator|new
name|ParsingContext
argument_list|(
name|properties
argument_list|,
name|defaultFallbackEnabled
argument_list|)
decl_stmt|;
return|return
name|context
operator|.
name|parse
argument_list|(
name|text
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|parseProperty (String key, String value, PropertiesLookup properties)
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
name|PropertiesLookup
name|properties
parameter_list|)
block|{
return|return
name|value
return|;
block|}
comment|/**      * This inner class helps replacing properties.      */
DECL|class|ParsingContext
specifier|private
specifier|final
class|class
name|ParsingContext
block|{
DECL|field|properties
specifier|private
specifier|final
name|PropertiesLookup
name|properties
decl_stmt|;
DECL|field|defaultFallbackEnabled
specifier|private
specifier|final
name|boolean
name|defaultFallbackEnabled
decl_stmt|;
DECL|method|ParsingContext (PropertiesLookup properties, boolean defaultFallbackEnabled)
name|ParsingContext
parameter_list|(
name|PropertiesLookup
name|properties
parameter_list|,
name|boolean
name|defaultFallbackEnabled
parameter_list|)
block|{
name|this
operator|.
name|properties
operator|=
name|properties
expr_stmt|;
name|this
operator|.
name|defaultFallbackEnabled
operator|=
name|defaultFallbackEnabled
expr_stmt|;
block|}
comment|/**          * Parses the given input string and replaces all properties          *          * @param input Input string          * @return Evaluated string          */
DECL|method|parse (String input)
specifier|public
name|String
name|parse
parameter_list|(
name|String
name|input
parameter_list|)
block|{
return|return
name|doParse
argument_list|(
name|input
argument_list|,
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
argument_list|)
return|;
block|}
comment|/**          * Recursively parses the given input string and replaces all properties          *          * @param input                Input string          * @param replacedPropertyKeys Already replaced property keys used for tracking circular references          * @return Evaluated string          */
DECL|method|doParse (String input, Set<String> replacedPropertyKeys)
specifier|private
name|String
name|doParse
parameter_list|(
name|String
name|input
parameter_list|,
name|Set
argument_list|<
name|String
argument_list|>
name|replacedPropertyKeys
parameter_list|)
block|{
if|if
condition|(
name|input
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|String
name|answer
init|=
name|input
decl_stmt|;
name|Property
name|property
decl_stmt|;
while|while
condition|(
operator|(
name|property
operator|=
name|readProperty
argument_list|(
name|answer
argument_list|)
operator|)
operator|!=
literal|null
condition|)
block|{
comment|// Check for circular references
if|if
condition|(
name|replacedPropertyKeys
operator|.
name|contains
argument_list|(
name|property
operator|.
name|getKey
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Circular reference detected with key ["
operator|+
name|property
operator|.
name|getKey
argument_list|()
operator|+
literal|"] from text: "
operator|+
name|input
argument_list|)
throw|;
block|}
name|Set
argument_list|<
name|String
argument_list|>
name|newReplaced
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|replacedPropertyKeys
argument_list|)
decl_stmt|;
name|newReplaced
operator|.
name|add
argument_list|(
name|property
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|before
init|=
name|answer
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|property
operator|.
name|getBeginIndex
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|after
init|=
name|answer
operator|.
name|substring
argument_list|(
name|property
operator|.
name|getEndIndex
argument_list|()
argument_list|)
decl_stmt|;
name|answer
operator|=
name|before
operator|+
name|doParse
argument_list|(
name|property
operator|.
name|getValue
argument_list|()
argument_list|,
name|newReplaced
argument_list|)
operator|+
name|after
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
comment|/**          * Finds a property in the given string. It returns {@code null} if there's no property defined.          *          * @param input Input string          * @return A property in the given string or {@code null} if not found          */
DECL|method|readProperty (String input)
specifier|private
name|Property
name|readProperty
parameter_list|(
name|String
name|input
parameter_list|)
block|{
comment|// Find the index of the first valid suffix token
name|int
name|suffix
init|=
name|getSuffixIndex
argument_list|(
name|input
argument_list|)
decl_stmt|;
comment|// If not found, ensure that there is no valid prefix token in the string
if|if
condition|(
name|suffix
operator|==
operator|-
literal|1
condition|)
block|{
if|if
condition|(
name|getMatchingPrefixIndex
argument_list|(
name|input
argument_list|,
name|input
operator|.
name|length
argument_list|()
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Missing %s from the text: %s"
argument_list|,
name|SUFFIX_TOKEN
argument_list|,
name|input
argument_list|)
argument_list|)
throw|;
block|}
return|return
literal|null
return|;
block|}
comment|// Find the index of the prefix token that matches the suffix token
name|int
name|prefix
init|=
name|getMatchingPrefixIndex
argument_list|(
name|input
argument_list|,
name|suffix
argument_list|)
decl_stmt|;
if|if
condition|(
name|prefix
operator|==
operator|-
literal|1
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Missing %s from the text: %s"
argument_list|,
name|PREFIX_TOKEN
argument_list|,
name|input
argument_list|)
argument_list|)
throw|;
block|}
name|String
name|key
init|=
name|input
operator|.
name|substring
argument_list|(
name|prefix
operator|+
name|PREFIX_TOKEN
operator|.
name|length
argument_list|()
argument_list|,
name|suffix
argument_list|)
decl_stmt|;
name|String
name|value
init|=
name|getPropertyValue
argument_list|(
name|key
argument_list|,
name|input
argument_list|)
decl_stmt|;
return|return
operator|new
name|Property
argument_list|(
name|prefix
argument_list|,
name|suffix
operator|+
name|SUFFIX_TOKEN
operator|.
name|length
argument_list|()
argument_list|,
name|key
argument_list|,
name|value
argument_list|)
return|;
block|}
comment|/**          * Gets the first index of the suffix token that is not surrounded by quotes          *          * @param input Input string          * @return First index of the suffix token that is not surrounded by quotes          */
DECL|method|getSuffixIndex (String input)
specifier|private
name|int
name|getSuffixIndex
parameter_list|(
name|String
name|input
parameter_list|)
block|{
name|int
name|index
init|=
operator|-
literal|1
decl_stmt|;
do|do
block|{
name|index
operator|=
name|input
operator|.
name|indexOf
argument_list|(
name|SUFFIX_TOKEN
argument_list|,
name|index
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
do|while
condition|(
name|index
operator|!=
operator|-
literal|1
operator|&&
name|isQuoted
argument_list|(
name|input
argument_list|,
name|index
argument_list|,
name|SUFFIX_TOKEN
argument_list|)
condition|)
do|;
return|return
name|index
return|;
block|}
comment|/**          * Gets the index of the prefix token that matches the suffix at the given index and that is not surrounded by quotes          *          * @param input       Input string          * @param suffixIndex Index of the suffix token          * @return Index of the prefix token that matches the suffix at the given index and that is not surrounded by quotes          */
DECL|method|getMatchingPrefixIndex (String input, int suffixIndex)
specifier|private
name|int
name|getMatchingPrefixIndex
parameter_list|(
name|String
name|input
parameter_list|,
name|int
name|suffixIndex
parameter_list|)
block|{
name|int
name|index
init|=
name|suffixIndex
decl_stmt|;
do|do
block|{
name|index
operator|=
name|input
operator|.
name|lastIndexOf
argument_list|(
name|PREFIX_TOKEN
argument_list|,
name|index
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
do|while
condition|(
name|index
operator|!=
operator|-
literal|1
operator|&&
name|isQuoted
argument_list|(
name|input
argument_list|,
name|index
argument_list|,
name|PREFIX_TOKEN
argument_list|)
condition|)
do|;
return|return
name|index
return|;
block|}
comment|/**          * Indicates whether or not the token at the given index is surrounded by single or double quotes          *          * @param input Input string          * @param index Index of the token          * @param token Token          * @return {@code true}          */
DECL|method|isQuoted (String input, int index, String token)
specifier|private
name|boolean
name|isQuoted
parameter_list|(
name|String
name|input
parameter_list|,
name|int
name|index
parameter_list|,
name|String
name|token
parameter_list|)
block|{
name|int
name|beforeIndex
init|=
name|index
operator|-
literal|1
decl_stmt|;
name|int
name|afterIndex
init|=
name|index
operator|+
name|token
operator|.
name|length
argument_list|()
decl_stmt|;
if|if
condition|(
name|beforeIndex
operator|>=
literal|0
operator|&&
name|afterIndex
operator|<
name|input
operator|.
name|length
argument_list|()
condition|)
block|{
name|char
name|before
init|=
name|input
operator|.
name|charAt
argument_list|(
name|beforeIndex
argument_list|)
decl_stmt|;
name|char
name|after
init|=
name|input
operator|.
name|charAt
argument_list|(
name|afterIndex
argument_list|)
decl_stmt|;
return|return
operator|(
name|before
operator|==
name|after
operator|)
operator|&&
operator|(
name|before
operator|==
literal|'\''
operator|||
name|before
operator|==
literal|'"'
operator|)
return|;
block|}
return|return
literal|false
return|;
block|}
comment|/**          * Gets the value of the property with given key          *          * @param key   Key of the property          * @param input Input string (used for exception message if value not found)          * @return Value of the property with the given key          */
DECL|method|getPropertyValue (String key, String input)
specifier|private
name|String
name|getPropertyValue
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|input
parameter_list|)
block|{
comment|// the key may be a function, so lets check this first
if|if
condition|(
name|propertiesComponent
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|PropertiesFunction
name|function
range|:
name|propertiesComponent
operator|.
name|getFunctions
argument_list|()
operator|.
name|values
argument_list|()
control|)
block|{
name|String
name|token
init|=
name|function
operator|.
name|getName
argument_list|()
operator|+
literal|":"
decl_stmt|;
if|if
condition|(
name|key
operator|.
name|startsWith
argument_list|(
name|token
argument_list|)
condition|)
block|{
name|String
name|remainder
init|=
name|key
operator|.
name|substring
argument_list|(
name|token
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Property with key [{}] is applied by function [{}]"
argument_list|,
name|key
argument_list|,
name|function
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|value
init|=
name|function
operator|.
name|apply
argument_list|(
name|remainder
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Property with key ["
operator|+
name|key
operator|+
literal|"] using function ["
operator|+
name|function
operator|.
name|getName
argument_list|()
operator|+
literal|"]"
operator|+
literal|" returned null value which is not allowed, from input: "
operator|+
name|input
argument_list|)
throw|;
block|}
else|else
block|{
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Property with key [{}] applied by function [{}] -> {}"
argument_list|,
name|key
argument_list|,
name|function
operator|.
name|getName
argument_list|()
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
return|return
name|value
return|;
block|}
block|}
block|}
block|}
comment|// they key may have a get or else expression
name|String
name|defaultValue
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|defaultFallbackEnabled
operator|&&
name|key
operator|.
name|contains
argument_list|(
name|GET_OR_ELSE_TOKEN
argument_list|)
condition|)
block|{
name|defaultValue
operator|=
name|StringHelper
operator|.
name|after
argument_list|(
name|key
argument_list|,
name|GET_OR_ELSE_TOKEN
argument_list|)
expr_stmt|;
name|key
operator|=
name|StringHelper
operator|.
name|before
argument_list|(
name|key
argument_list|,
name|GET_OR_ELSE_TOKEN
argument_list|)
expr_stmt|;
block|}
name|String
name|value
init|=
name|doGetPropertyValue
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|==
literal|null
operator|&&
name|defaultValue
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Property with key [{}] not found, using default value: {}"
argument_list|,
name|key
argument_list|,
name|defaultValue
argument_list|)
expr_stmt|;
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
name|StringBuilder
name|esb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|esb
operator|.
name|append
argument_list|(
literal|"Property with key ["
argument_list|)
operator|.
name|append
argument_list|(
name|key
argument_list|)
operator|.
name|append
argument_list|(
literal|"] "
argument_list|)
expr_stmt|;
name|esb
operator|.
name|append
argument_list|(
literal|"not found in properties from text: "
argument_list|)
operator|.
name|append
argument_list|(
name|input
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|esb
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
return|return
name|value
return|;
block|}
comment|/**          * Gets the property with the given key, it returns {@code null} if the property is not found          *          * @param key Key of the property          * @return Value of the property or {@code null} if not found          */
DECL|method|doGetPropertyValue (String key)
specifier|private
name|String
name|doGetPropertyValue
parameter_list|(
name|String
name|key
parameter_list|)
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|key
argument_list|)
condition|)
block|{
return|return
name|parseProperty
argument_list|(
name|key
argument_list|,
literal|null
argument_list|,
name|properties
argument_list|)
return|;
block|}
name|String
name|value
init|=
literal|null
decl_stmt|;
comment|// override is the default mode for ENV
name|int
name|envMode
init|=
name|propertiesComponent
operator|!=
literal|null
condition|?
name|propertiesComponent
operator|.
name|getEnvironmentVariableMode
argument_list|()
else|:
name|PropertiesComponent
operator|.
name|ENVIRONMENT_VARIABLES_MODE_FALLBACK
decl_stmt|;
comment|// override is the default mode for SYS
name|int
name|sysMode
init|=
name|propertiesComponent
operator|!=
literal|null
condition|?
name|propertiesComponent
operator|.
name|getSystemPropertiesMode
argument_list|()
else|:
name|PropertiesComponent
operator|.
name|SYSTEM_PROPERTIES_MODE_OVERRIDE
decl_stmt|;
if|if
condition|(
name|envMode
operator|==
name|PropertiesComponent
operator|.
name|ENVIRONMENT_VARIABLES_MODE_OVERRIDE
condition|)
block|{
name|value
operator|=
name|lookupEnvironmentVariable
argument_list|(
name|key
argument_list|)
expr_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Found an OS environment property: {} with value: {} to be used."
argument_list|,
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|value
operator|==
literal|null
operator|&&
name|sysMode
operator|==
name|PropertiesComponent
operator|.
name|SYSTEM_PROPERTIES_MODE_OVERRIDE
condition|)
block|{
name|value
operator|=
name|System
operator|.
name|getProperty
argument_list|(
name|key
argument_list|)
expr_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Found a JVM system property: {} with value: {} to be used."
argument_list|,
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|value
operator|==
literal|null
operator|&&
name|properties
operator|!=
literal|null
condition|)
block|{
name|value
operator|=
name|properties
operator|.
name|lookup
argument_list|(
name|key
argument_list|)
expr_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Found property: {} with value: {} to be used."
argument_list|,
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|value
operator|==
literal|null
operator|&&
name|envMode
operator|==
name|PropertiesComponent
operator|.
name|ENVIRONMENT_VARIABLES_MODE_FALLBACK
condition|)
block|{
name|value
operator|=
name|lookupEnvironmentVariable
argument_list|(
name|key
argument_list|)
expr_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Found an OS environment property: {} with value: {} to be used."
argument_list|,
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|value
operator|==
literal|null
operator|&&
name|sysMode
operator|==
name|PropertiesComponent
operator|.
name|SYSTEM_PROPERTIES_MODE_FALLBACK
condition|)
block|{
name|value
operator|=
name|System
operator|.
name|getProperty
argument_list|(
name|key
argument_list|)
expr_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Found a JVM system property: {} with value: {} to be used."
argument_list|,
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
return|return
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
comment|/**      * This inner class is the definition of a property used in a string      */
DECL|class|Property
specifier|private
specifier|static
specifier|final
class|class
name|Property
block|{
DECL|field|beginIndex
specifier|private
specifier|final
name|int
name|beginIndex
decl_stmt|;
DECL|field|endIndex
specifier|private
specifier|final
name|int
name|endIndex
decl_stmt|;
DECL|field|key
specifier|private
specifier|final
name|String
name|key
decl_stmt|;
DECL|field|value
specifier|private
specifier|final
name|String
name|value
decl_stmt|;
DECL|method|Property (int beginIndex, int endIndex, String key, String value)
specifier|private
name|Property
parameter_list|(
name|int
name|beginIndex
parameter_list|,
name|int
name|endIndex
parameter_list|,
name|String
name|key
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|beginIndex
operator|=
name|beginIndex
expr_stmt|;
name|this
operator|.
name|endIndex
operator|=
name|endIndex
expr_stmt|;
name|this
operator|.
name|key
operator|=
name|key
expr_stmt|;
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
block|}
comment|/**          * Gets the begin index of the property (including the prefix token).          */
DECL|method|getBeginIndex ()
specifier|public
name|int
name|getBeginIndex
parameter_list|()
block|{
return|return
name|beginIndex
return|;
block|}
comment|/**          * Gets the end index of the property (including the suffix token).          */
DECL|method|getEndIndex ()
specifier|public
name|int
name|getEndIndex
parameter_list|()
block|{
return|return
name|endIndex
return|;
block|}
comment|/**          * Gets the key of the property.          */
DECL|method|getKey ()
specifier|public
name|String
name|getKey
parameter_list|()
block|{
return|return
name|key
return|;
block|}
comment|/**          * Gets the value of the property.          */
DECL|method|getValue ()
specifier|public
name|String
name|getValue
parameter_list|()
block|{
return|return
name|value
return|;
block|}
block|}
block|}
end_class

end_unit

