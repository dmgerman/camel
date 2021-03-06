begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.tools.apt.helper
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|tools
operator|.
name|apt
operator|.
name|helper
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
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
name|Set
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
name|util
operator|.
name|json
operator|.
name|JsonObject
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
name|json
operator|.
name|Jsoner
import|;
end_import

begin_comment
comment|/**  * A helper class for<a href="http://json-schema.org/">JSON schema</a>.  */
end_comment

begin_class
DECL|class|JsonSchemaHelper
specifier|public
specifier|final
class|class
name|JsonSchemaHelper
block|{
DECL|field|VALID_CHARS
specifier|private
specifier|static
specifier|final
name|String
name|VALID_CHARS
init|=
literal|".,-='/\\!&%():;#${}"
decl_stmt|;
DECL|method|JsonSchemaHelper ()
specifier|private
name|JsonSchemaHelper
parameter_list|()
block|{     }
DECL|method|toJson (String name, String displayName, String kind, Boolean required, String type, String defaultValue, String description, Boolean deprecated, String deprecationNote, Boolean secret, String group, String label, boolean enumType, Set<String> enums, boolean oneOfType, Set<String> oneOffTypes, boolean asPredicate, String optionalPrefix, String prefix, boolean multiValue, String configurationClass, String configurationField)
specifier|public
specifier|static
name|String
name|toJson
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|displayName
parameter_list|,
name|String
name|kind
parameter_list|,
name|Boolean
name|required
parameter_list|,
name|String
name|type
parameter_list|,
name|String
name|defaultValue
parameter_list|,
name|String
name|description
parameter_list|,
name|Boolean
name|deprecated
parameter_list|,
name|String
name|deprecationNote
parameter_list|,
name|Boolean
name|secret
parameter_list|,
name|String
name|group
parameter_list|,
name|String
name|label
parameter_list|,
name|boolean
name|enumType
parameter_list|,
name|Set
argument_list|<
name|String
argument_list|>
name|enums
parameter_list|,
name|boolean
name|oneOfType
parameter_list|,
name|Set
argument_list|<
name|String
argument_list|>
name|oneOffTypes
parameter_list|,
name|boolean
name|asPredicate
parameter_list|,
name|String
name|optionalPrefix
parameter_list|,
name|String
name|prefix
parameter_list|,
name|boolean
name|multiValue
parameter_list|,
name|String
name|configurationClass
parameter_list|,
name|String
name|configurationField
parameter_list|)
block|{
name|String
name|typeName
init|=
name|JsonSchemaHelper
operator|.
name|getType
argument_list|(
name|type
argument_list|,
name|enumType
argument_list|)
decl_stmt|;
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
name|Strings
operator|.
name|doubleQuote
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|": { \"kind\": "
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|Strings
operator|.
name|doubleQuote
argument_list|(
name|kind
argument_list|)
argument_list|)
expr_stmt|;
comment|// compute a display name if we don't have anything
if|if
condition|(
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|displayName
argument_list|)
condition|)
block|{
name|displayName
operator|=
name|Strings
operator|.
name|asTitle
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
comment|// we want display name early so its easier to spot
name|sb
operator|.
name|append
argument_list|(
literal|", \"displayName\": "
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|Strings
operator|.
name|doubleQuote
argument_list|(
name|displayName
argument_list|)
argument_list|)
expr_stmt|;
comment|// we want group early so its easier to spot
if|if
condition|(
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|group
argument_list|)
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|", \"group\": "
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|Strings
operator|.
name|doubleQuote
argument_list|(
name|group
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// we want label early so its easier to spot
if|if
condition|(
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|label
argument_list|)
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|", \"label\": "
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|Strings
operator|.
name|doubleQuote
argument_list|(
name|label
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|required
operator|!=
literal|null
condition|)
block|{
comment|// boolean type
name|sb
operator|.
name|append
argument_list|(
literal|", \"required\": "
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|required
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
literal|", \"type\": "
argument_list|)
expr_stmt|;
if|if
condition|(
literal|"enum"
operator|.
name|equals
argument_list|(
name|typeName
argument_list|)
condition|)
block|{
name|String
name|actualType
init|=
name|JsonSchemaHelper
operator|.
name|getType
argument_list|(
name|type
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|Strings
operator|.
name|doubleQuote
argument_list|(
name|actualType
argument_list|)
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", \"javaType\": \""
argument_list|)
operator|.
name|append
argument_list|(
name|type
argument_list|)
operator|.
name|append
argument_list|(
literal|"\""
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", \"enum\": [ "
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|enums
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|Strings
operator|::
name|doubleQuote
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|joining
argument_list|(
literal|", "
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|" ]"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|oneOfType
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|Strings
operator|.
name|doubleQuote
argument_list|(
name|typeName
argument_list|)
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", \"javaType\": \""
argument_list|)
operator|.
name|append
argument_list|(
name|type
argument_list|)
operator|.
name|append
argument_list|(
literal|"\""
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", \"oneOf\": [ "
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|oneOffTypes
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|Strings
operator|::
name|doubleQuote
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|joining
argument_list|(
literal|", "
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|" ]"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"array"
operator|.
name|equals
argument_list|(
name|typeName
argument_list|)
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|Strings
operator|.
name|doubleQuote
argument_list|(
literal|"array"
argument_list|)
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", \"javaType\": \""
argument_list|)
operator|.
name|append
argument_list|(
name|type
argument_list|)
operator|.
name|append
argument_list|(
literal|"\""
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|sb
operator|.
name|append
argument_list|(
name|Strings
operator|.
name|doubleQuote
argument_list|(
name|typeName
argument_list|)
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", \"javaType\": \""
argument_list|)
operator|.
name|append
argument_list|(
name|type
argument_list|)
operator|.
name|append
argument_list|(
literal|"\""
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|optionalPrefix
argument_list|)
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|", \"optionalPrefix\": "
argument_list|)
expr_stmt|;
name|String
name|text
init|=
name|safeDefaultValue
argument_list|(
name|optionalPrefix
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|Strings
operator|.
name|doubleQuote
argument_list|(
name|text
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|prefix
argument_list|)
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|", \"prefix\": "
argument_list|)
expr_stmt|;
name|String
name|text
init|=
name|safeDefaultValue
argument_list|(
name|prefix
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|Strings
operator|.
name|doubleQuote
argument_list|(
name|text
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|multiValue
condition|)
block|{
comment|// boolean value
name|sb
operator|.
name|append
argument_list|(
literal|", \"multiValue\": true"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|deprecated
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|", \"deprecated\": "
argument_list|)
expr_stmt|;
comment|// boolean value
name|sb
operator|.
name|append
argument_list|(
name|deprecated
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|deprecationNote
argument_list|)
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|", \"deprecationNote\": "
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|Strings
operator|.
name|doubleQuote
argument_list|(
name|deprecationNote
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|secret
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|", \"secret\": "
argument_list|)
expr_stmt|;
comment|// boolean value
name|sb
operator|.
name|append
argument_list|(
name|secret
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|defaultValue
argument_list|)
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|", \"defaultValue\": "
argument_list|)
expr_stmt|;
name|String
name|text
init|=
name|safeDefaultValue
argument_list|(
name|defaultValue
argument_list|)
decl_stmt|;
comment|// the type can either be boolean, integer, number or text based
if|if
condition|(
literal|"boolean"
operator|.
name|equals
argument_list|(
name|typeName
argument_list|)
operator|||
literal|"integer"
operator|.
name|equals
argument_list|(
name|typeName
argument_list|)
operator|||
literal|"number"
operator|.
name|equals
argument_list|(
name|typeName
argument_list|)
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|text
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// text should be quoted
name|sb
operator|.
name|append
argument_list|(
name|Strings
operator|.
name|doubleQuote
argument_list|(
name|text
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|// for expressions we want to know if it must be used as predicate or not
name|boolean
name|predicate
init|=
literal|"expression"
operator|.
name|equals
argument_list|(
name|kind
argument_list|)
operator|||
name|asPredicate
decl_stmt|;
if|if
condition|(
name|predicate
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|", \"asPredicate\": "
argument_list|)
expr_stmt|;
if|if
condition|(
name|asPredicate
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"true"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"false"
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|configurationClass
argument_list|)
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|", \"configurationClass\": "
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|Strings
operator|.
name|doubleQuote
argument_list|(
name|configurationClass
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|configurationField
argument_list|)
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|", \"configurationField\": "
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|Strings
operator|.
name|doubleQuote
argument_list|(
name|configurationField
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|description
argument_list|)
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|", \"description\": "
argument_list|)
expr_stmt|;
name|String
name|text
init|=
name|sanitizeDescription
argument_list|(
name|description
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|Strings
operator|.
name|doubleQuote
argument_list|(
name|text
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
literal|" }"
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * Gets the JSon schema type.      *      * @param   type the java type      * @return  the json schema type, is never null, but returns<tt>object</tt> as the generic type      */
DECL|method|getType (String type, boolean enumType)
specifier|public
specifier|static
name|String
name|getType
parameter_list|(
name|String
name|type
parameter_list|,
name|boolean
name|enumType
parameter_list|)
block|{
if|if
condition|(
name|enumType
condition|)
block|{
return|return
literal|"enum"
return|;
block|}
elseif|else
if|if
condition|(
name|type
operator|==
literal|null
condition|)
block|{
comment|// return generic type for unknown type
return|return
literal|"object"
return|;
block|}
elseif|else
if|if
condition|(
name|type
operator|.
name|equals
argument_list|(
name|URI
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
operator|||
name|type
operator|.
name|equals
argument_list|(
name|URL
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|"string"
return|;
block|}
elseif|else
if|if
condition|(
name|type
operator|.
name|equals
argument_list|(
name|File
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|"string"
return|;
block|}
elseif|else
if|if
condition|(
name|type
operator|.
name|equals
argument_list|(
name|Date
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|"string"
return|;
block|}
elseif|else
if|if
condition|(
name|type
operator|.
name|startsWith
argument_list|(
literal|"java.lang.Class"
argument_list|)
condition|)
block|{
return|return
literal|"string"
return|;
block|}
elseif|else
if|if
condition|(
name|type
operator|.
name|startsWith
argument_list|(
literal|"java.util.List"
argument_list|)
operator|||
name|type
operator|.
name|startsWith
argument_list|(
literal|"java.util.Collection"
argument_list|)
condition|)
block|{
return|return
literal|"array"
return|;
block|}
name|String
name|primitive
init|=
name|getPrimitiveType
argument_list|(
name|type
argument_list|)
decl_stmt|;
if|if
condition|(
name|primitive
operator|!=
literal|null
condition|)
block|{
return|return
name|primitive
return|;
block|}
return|return
literal|"object"
return|;
block|}
comment|/**      * Gets the JSon schema primitive type.      *      * @param   name the java type      * @return  the json schema primitive type, or<tt>null</tt> if not a primitive      */
DECL|method|getPrimitiveType (String name)
specifier|public
specifier|static
name|String
name|getPrimitiveType
parameter_list|(
name|String
name|name
parameter_list|)
block|{
comment|// special for byte[] or Object[] as its common to use
if|if
condition|(
literal|"java.lang.byte[]"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"byte[]"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
literal|"string"
return|;
block|}
elseif|else
if|if
condition|(
literal|"java.lang.Byte[]"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"Byte[]"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
literal|"array"
return|;
block|}
elseif|else
if|if
condition|(
literal|"java.lang.Object[]"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"Object[]"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
literal|"array"
return|;
block|}
elseif|else
if|if
condition|(
literal|"java.lang.String[]"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"String[]"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
literal|"array"
return|;
block|}
elseif|else
if|if
condition|(
literal|"java.lang.Character"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"Character"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"char"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
literal|"string"
return|;
block|}
elseif|else
if|if
condition|(
literal|"java.lang.String"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"String"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
literal|"string"
return|;
block|}
elseif|else
if|if
condition|(
literal|"java.lang.Boolean"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"Boolean"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"boolean"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
literal|"boolean"
return|;
block|}
elseif|else
if|if
condition|(
literal|"java.lang.Integer"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"Integer"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"int"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
literal|"integer"
return|;
block|}
elseif|else
if|if
condition|(
literal|"java.lang.Long"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"Long"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"long"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
literal|"integer"
return|;
block|}
elseif|else
if|if
condition|(
literal|"java.lang.Short"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"Short"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"short"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
literal|"integer"
return|;
block|}
elseif|else
if|if
condition|(
literal|"java.lang.Byte"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"Byte"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"byte"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
literal|"integer"
return|;
block|}
elseif|else
if|if
condition|(
literal|"java.lang.Float"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"Float"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"float"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
literal|"number"
return|;
block|}
elseif|else
if|if
condition|(
literal|"java.lang.Double"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"Double"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"double"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
literal|"number"
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Sanitizes the javadoc to removed invalid characters so it can be used as json description      *      * @param javadoc  the javadoc      * @return the text that is valid as json      */
DECL|method|sanitizeDescription (String javadoc, boolean summary)
specifier|public
specifier|static
name|String
name|sanitizeDescription
parameter_list|(
name|String
name|javadoc
parameter_list|,
name|boolean
name|summary
parameter_list|)
block|{
if|if
condition|(
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|javadoc
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
comment|// lets just use what java accepts as identifiers
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
comment|// split into lines
name|String
index|[]
name|lines
init|=
name|javadoc
operator|.
name|split
argument_list|(
literal|"\n"
argument_list|)
decl_stmt|;
name|boolean
name|first
init|=
literal|true
decl_stmt|;
for|for
control|(
name|String
name|line
range|:
name|lines
control|)
block|{
name|line
operator|=
name|line
operator|.
name|trim
argument_list|()
expr_stmt|;
if|if
condition|(
name|line
operator|.
name|startsWith
argument_list|(
literal|"**"
argument_list|)
condition|)
block|{
continue|continue;
block|}
comment|// remove leading javadoc *
if|if
condition|(
name|line
operator|.
name|startsWith
argument_list|(
literal|"*"
argument_list|)
condition|)
block|{
name|line
operator|=
name|line
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|line
operator|=
name|line
operator|.
name|trim
argument_list|()
expr_stmt|;
block|}
comment|// terminate if we reach @param, @return or @deprecated as we only want the javadoc summary
if|if
condition|(
name|line
operator|.
name|startsWith
argument_list|(
literal|"@param"
argument_list|)
operator|||
name|line
operator|.
name|startsWith
argument_list|(
literal|"@return"
argument_list|)
operator|||
name|line
operator|.
name|startsWith
argument_list|(
literal|"@deprecated"
argument_list|)
condition|)
block|{
break|break;
block|}
comment|// skip lines that are javadoc references
if|if
condition|(
name|line
operator|.
name|startsWith
argument_list|(
literal|"@"
argument_list|)
condition|)
block|{
continue|continue;
block|}
comment|// remove all XML tags
name|line
operator|=
name|line
operator|.
name|replaceAll
argument_list|(
literal|"<.*?>"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
comment|// remove all inlined javadoc links, eg such as {@link org.apache.camel.spi.Registry}
comment|// use #? to remove leading # in case its a local reference
name|line
operator|=
name|line
operator|.
name|replaceAll
argument_list|(
literal|"\\{\\@\\w+\\s#?([\\w.#(\\d,)]+)\\}"
argument_list|,
literal|"$1"
argument_list|)
expr_stmt|;
comment|// we are starting from a new line, so add a whitespace
if|if
condition|(
operator|!
name|first
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
block|}
comment|// create a new line
name|StringBuilder
name|cb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|char
name|c
range|:
name|line
operator|.
name|toCharArray
argument_list|()
control|)
block|{
if|if
condition|(
name|Character
operator|.
name|isJavaIdentifierPart
argument_list|(
name|c
argument_list|)
operator|||
name|VALID_CHARS
operator|.
name|indexOf
argument_list|(
name|c
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|cb
operator|.
name|append
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|Character
operator|.
name|isWhitespace
argument_list|(
name|c
argument_list|)
condition|)
block|{
comment|// always use space as whitespace, also for line feeds etc
name|cb
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
block|}
block|}
comment|// append data
name|String
name|s
init|=
name|cb
operator|.
name|toString
argument_list|()
operator|.
name|trim
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|s
argument_list|)
expr_stmt|;
name|boolean
name|empty
init|=
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|s
argument_list|)
decl_stmt|;
name|boolean
name|endWithDot
init|=
name|s
operator|.
name|endsWith
argument_list|(
literal|"."
argument_list|)
decl_stmt|;
name|boolean
name|haveText
init|=
name|sb
operator|.
name|length
argument_list|()
operator|>
literal|0
decl_stmt|;
if|if
condition|(
name|haveText
operator|&&
name|summary
operator|&&
operator|(
name|empty
operator|||
name|endWithDot
operator|)
condition|)
block|{
comment|// if we only want a summary, then skip at first empty line we encounter, or if the sentence ends with a dot
break|break;
block|}
name|first
operator|=
literal|false
expr_stmt|;
block|}
name|String
name|s
init|=
name|sb
operator|.
name|toString
argument_list|()
decl_stmt|;
comment|// remove double whitespaces, and trim
name|s
operator|=
name|s
operator|.
name|replaceAll
argument_list|(
literal|"\\s+"
argument_list|,
literal|" "
argument_list|)
expr_stmt|;
comment|// unescape http links
name|s
operator|=
name|s
operator|.
name|replaceAll
argument_list|(
literal|"\\\\(http:|https:)"
argument_list|,
literal|"$1"
argument_list|)
expr_stmt|;
return|return
name|s
operator|.
name|trim
argument_list|()
return|;
block|}
comment|/**      * Parses the json schema to split it into a list or rows, where each row contains key value pairs with the metadata      *      * @param group the group to parse from such as<tt>component</tt>,<tt>componentProperties</tt>, or<tt>properties</tt>.      * @param json the json      * @return a list of all the rows, where each row is a set of key value pairs with metadata      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|parseJsonSchema (String group, String json, boolean parseProperties)
specifier|public
specifier|static
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|parseJsonSchema
parameter_list|(
name|String
name|group
parameter_list|,
name|String
name|json
parameter_list|,
name|boolean
name|parseProperties
parameter_list|)
block|{
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|json
operator|==
literal|null
condition|)
block|{
return|return
name|answer
return|;
block|}
comment|// convert into a List<Map<String, String>> structure which is expected as output from this parser
try|try
block|{
name|JsonObject
name|output
init|=
operator|(
name|JsonObject
operator|)
name|Jsoner
operator|.
name|deserialize
argument_list|(
name|json
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|key
range|:
name|output
operator|.
name|keySet
argument_list|()
control|)
block|{
name|Map
name|row
init|=
name|output
operator|.
name|getMap
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|key
operator|.
name|equals
argument_list|(
name|group
argument_list|)
condition|)
block|{
if|if
condition|(
name|parseProperties
condition|)
block|{
comment|// flattern each entry in the row with name as they key, and its value as the content (its a map also)
for|for
control|(
name|Object
name|obj
range|:
name|row
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|Map
operator|.
name|Entry
name|entry
init|=
operator|(
name|Map
operator|.
name|Entry
operator|)
name|obj
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|newRow
init|=
operator|new
name|LinkedHashMap
argument_list|()
decl_stmt|;
name|newRow
operator|.
name|put
argument_list|(
literal|"name"
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|Map
name|newData
init|=
name|transformMap
argument_list|(
operator|(
name|Map
operator|)
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
decl_stmt|;
name|newRow
operator|.
name|putAll
argument_list|(
name|newData
argument_list|)
expr_stmt|;
name|answer
operator|.
name|add
argument_list|(
name|newRow
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// flattern each entry in the row as a list of single Map<key, value> elements
name|Map
name|newData
init|=
name|transformMap
argument_list|(
name|row
argument_list|)
decl_stmt|;
for|for
control|(
name|Object
name|obj
range|:
name|newData
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|Map
operator|.
name|Entry
name|entry
init|=
operator|(
name|Map
operator|.
name|Entry
operator|)
name|obj
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|newRow
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|newRow
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|answer
operator|.
name|add
argument_list|(
name|newRow
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// wrap parsing exceptions as runtime
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Cannot parse json"
argument_list|,
name|e
argument_list|)
throw|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|transformMap (Map jsonMap)
specifier|private
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|transformMap
parameter_list|(
name|Map
name|jsonMap
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|answer
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|rowObj
range|:
name|jsonMap
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|Map
operator|.
name|Entry
name|rowEntry
init|=
operator|(
name|Map
operator|.
name|Entry
operator|)
name|rowObj
decl_stmt|;
comment|// if its a list type then its an enum, and we need to parse it as a single line separated with comma
comment|// to be backwards compatible
name|Object
name|newValue
init|=
name|rowEntry
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|newValue
operator|instanceof
name|List
condition|)
block|{
name|List
argument_list|<
name|Object
argument_list|>
name|list
init|=
operator|(
name|List
operator|)
name|newValue
decl_stmt|;
name|newValue
operator|=
name|list
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|String
operator|::
name|valueOf
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|joining
argument_list|(
literal|","
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// ensure value is escaped
name|String
name|value
init|=
name|escapeJson
argument_list|(
name|newValue
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|answer
operator|.
name|put
argument_list|(
name|rowEntry
operator|.
name|getKey
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|escapeJson (String value)
specifier|private
specifier|static
name|String
name|escapeJson
parameter_list|(
name|String
name|value
parameter_list|)
block|{
comment|// need to safe encode \r as \\r so its escaped
comment|// need to safe encode \n as \\n so its escaped
comment|// need to safe encode \t as \\t so its escaped
return|return
name|value
operator|.
name|replaceAll
argument_list|(
literal|"\\\\r"
argument_list|,
literal|"\\\\\\r"
argument_list|)
operator|.
name|replaceAll
argument_list|(
literal|"\\\\n"
argument_list|,
literal|"\\\\\\n"
argument_list|)
operator|.
name|replaceAll
argument_list|(
literal|"\\\\t"
argument_list|,
literal|"\\\\\\t"
argument_list|)
return|;
block|}
comment|/**      * The default value may need to be escaped to be safe for json      */
DECL|method|safeDefaultValue (String value)
specifier|private
specifier|static
name|String
name|safeDefaultValue
parameter_list|(
name|String
name|value
parameter_list|)
block|{
if|if
condition|(
literal|"\""
operator|.
name|equals
argument_list|(
name|value
argument_list|)
condition|)
block|{
return|return
literal|"\\\""
return|;
block|}
elseif|else
if|if
condition|(
literal|"\\"
operator|.
name|equals
argument_list|(
name|value
argument_list|)
condition|)
block|{
return|return
literal|"\\\\"
return|;
block|}
else|else
block|{
return|return
name|value
return|;
block|}
block|}
block|}
end_class

end_unit

