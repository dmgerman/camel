begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.tools.apt
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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_comment
comment|/**  * A helper class for<a href="http://json-schema.org/">JSON schema</a>.  */
end_comment

begin_class
DECL|class|JsonSchemaHelper
specifier|final
class|class
name|JsonSchemaHelper
block|{
DECL|method|JsonSchemaHelper ()
specifier|private
name|JsonSchemaHelper
parameter_list|()
block|{     }
DECL|method|toJson (String name, String type, String defaultValue, String description, boolean enumType, Set<String> enums)
specifier|public
specifier|static
name|String
name|toJson
parameter_list|(
name|String
name|name
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
name|boolean
name|enumType
parameter_list|,
name|Set
argument_list|<
name|String
argument_list|>
name|enums
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
literal|": { \"type\": "
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
name|sb
operator|.
name|append
argument_list|(
name|Strings
operator|.
name|doubleQuote
argument_list|(
literal|"string"
argument_list|)
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", \"javaType\": \""
operator|+
name|type
operator|+
literal|"\""
argument_list|)
expr_stmt|;
name|CollectionStringBuffer
name|enumValues
init|=
operator|new
name|CollectionStringBuffer
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|value
range|:
name|enums
control|)
block|{
name|enumValues
operator|.
name|append
argument_list|(
name|Strings
operator|.
name|doubleQuote
argument_list|(
name|value
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
name|enumValues
operator|.
name|toString
argument_list|()
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
operator|+
name|type
operator|+
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
operator|+
name|type
operator|+
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
name|sb
operator|.
name|append
argument_list|(
name|Strings
operator|.
name|doubleQuote
argument_list|(
name|defaultValue
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
comment|// and these is common as well
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
condition|)
block|{
return|return
literal|"boolean"
return|;
block|}
elseif|else
if|if
condition|(
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
condition|)
block|{
return|return
literal|"integer"
return|;
block|}
elseif|else
if|if
condition|(
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
condition|)
block|{
return|return
literal|"integer"
return|;
block|}
elseif|else
if|if
condition|(
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
condition|)
block|{
return|return
literal|"integer"
return|;
block|}
elseif|else
if|if
condition|(
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
condition|)
block|{
return|return
literal|"integer"
return|;
block|}
elseif|else
if|if
condition|(
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
condition|)
block|{
return|return
literal|"number"
return|;
block|}
elseif|else
if|if
condition|(
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
condition|)
block|{
return|return
literal|"number"
return|;
block|}
elseif|else
if|if
condition|(
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
DECL|method|sanitizeDescription (String javadoc)
specifier|public
specifier|static
name|String
name|sanitizeDescription
parameter_list|(
name|String
name|javadoc
parameter_list|)
block|{
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
comment|// remove all HTML tags
name|line
operator|=
name|line
operator|.
name|replaceAll
argument_list|(
literal|"\\</?\\w+\\/?>"
argument_list|,
literal|""
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
literal|'.'
operator|==
name|c
condition|)
block|{
name|sb
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
name|sb
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
block|}
block|}
name|first
operator|=
literal|false
expr_stmt|;
block|}
comment|// remove double whitespaces, and trim
name|String
name|s
init|=
name|sb
operator|.
name|toString
argument_list|()
decl_stmt|;
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
return|return
name|s
operator|.
name|trim
argument_list|()
return|;
block|}
block|}
end_class

end_unit

