begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util
DECL|package|org.apache.camel.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
package|;
end_package

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

begin_comment
comment|/**  * A helper class for<a href="http://json-schema.org/">JSON schema</a>.  */
end_comment

begin_class
DECL|class|JsonSchemaHelper
DECL|class|JsonSchemaHelper
specifier|public
specifier|final
class|class
name|JsonSchemaHelper
block|{
DECL|field|PATTERN
DECL|field|PATTERN
specifier|private
specifier|static
specifier|final
name|Pattern
name|PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"\"(.+?)\"|\\[(.+)\\]"
argument_list|)
decl_stmt|;
DECL|field|QUOT
DECL|field|QUOT
specifier|private
specifier|static
specifier|final
name|String
name|QUOT
init|=
literal|"&quot;"
decl_stmt|;
DECL|method|JsonSchemaHelper ()
DECL|method|JsonSchemaHelper ()
specifier|private
name|JsonSchemaHelper
parameter_list|()
block|{     }
comment|/**      * Gets the JSon schema type.      *      * @param type the java type      * @return the json schema type, is never null, but returns<tt>object</tt> as the generic type      */
DECL|method|getType (Class<?> type)
DECL|method|getType (Class<?> type)
specifier|public
specifier|static
name|String
name|getType
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
if|if
condition|(
name|type
operator|.
name|isEnum
argument_list|()
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
operator|.
name|isArray
argument_list|()
condition|)
block|{
return|return
literal|"array"
return|;
block|}
if|if
condition|(
name|type
operator|.
name|isAssignableFrom
argument_list|(
name|URI
operator|.
name|class
argument_list|)
operator|||
name|type
operator|.
name|isAssignableFrom
argument_list|(
name|URL
operator|.
name|class
argument_list|)
condition|)
block|{
return|return
literal|"sting"
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
comment|/**      * Gets the JSon schema primitive type.      *      * @param type the java type      * @return the json schema primitive type, or<tt>null</tt> if not a primitive      */
DECL|method|getPrimitiveType (Class<?> type)
DECL|method|getPrimitiveType (Class<?> type)
specifier|public
specifier|static
name|String
name|getPrimitiveType
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
name|String
name|name
init|=
name|type
operator|.
name|getCanonicalName
argument_list|()
decl_stmt|;
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
comment|/**      * Parses the json schema to split it into a list or rows, where each row contains key value pairs with the metadata      *      * @param group the group to parse from such as<tt>component</tt>,<tt>componentProperties</tt>, or<tt>properties</tt>.      * @param json  the json      * @return a list of all the rows, where each row is a set of key value pairs with metadata      */
DECL|method|parseJsonSchema (String group, String json, boolean parseProperties)
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
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
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
name|boolean
name|found
init|=
literal|false
decl_stmt|;
comment|// parse line by line
name|String
index|[]
name|lines
init|=
name|json
operator|.
name|split
argument_list|(
literal|"\n"
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|line
range|:
name|lines
control|)
block|{
comment|// we need to find the group first
if|if
condition|(
operator|!
name|found
condition|)
block|{
name|String
name|s
init|=
name|line
operator|.
name|trim
argument_list|()
decl_stmt|;
name|found
operator|=
name|s
operator|.
name|startsWith
argument_list|(
literal|"\""
operator|+
name|group
operator|+
literal|"\":"
argument_list|)
operator|&&
name|s
operator|.
name|endsWith
argument_list|(
literal|"{"
argument_list|)
expr_stmt|;
continue|continue;
block|}
comment|// we should stop when we end the group
if|if
condition|(
name|line
operator|.
name|equals
argument_list|(
literal|"  },"
argument_list|)
operator|||
name|line
operator|.
name|equals
argument_list|(
literal|"  }"
argument_list|)
condition|)
block|{
break|break;
block|}
comment|// need to safe encode \" so we can parse the line
name|line
operator|=
name|line
operator|.
name|replaceAll
argument_list|(
literal|"\"\\\\\"\""
argument_list|,
literal|'"'
operator|+
name|QUOT
operator|+
literal|'"'
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|row
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|Matcher
name|matcher
init|=
name|PATTERN
operator|.
name|matcher
argument_list|(
name|line
argument_list|)
decl_stmt|;
name|String
name|key
decl_stmt|;
if|if
condition|(
name|parseProperties
condition|)
block|{
comment|// when parsing properties the first key is given as name, so the first parsed token is the value of the name
name|key
operator|=
literal|"name"
expr_stmt|;
block|}
else|else
block|{
name|key
operator|=
literal|null
expr_stmt|;
block|}
while|while
condition|(
name|matcher
operator|.
name|find
argument_list|()
condition|)
block|{
if|if
condition|(
name|key
operator|==
literal|null
condition|)
block|{
name|key
operator|=
name|matcher
operator|.
name|group
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|String
name|value
init|=
name|matcher
operator|.
name|group
argument_list|(
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
name|value
operator|=
name|matcher
operator|.
name|group
argument_list|(
literal|2
argument_list|)
expr_stmt|;
comment|// its an enum so strip out " and trim spaces after comma
name|value
operator|=
name|value
operator|.
name|replaceAll
argument_list|(
literal|"\""
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|value
operator|=
name|value
operator|.
name|replaceAll
argument_list|(
literal|", "
argument_list|,
literal|","
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|value
operator|=
name|value
operator|.
name|trim
argument_list|()
expr_stmt|;
comment|// decode
name|value
operator|=
name|value
operator|.
name|replaceAll
argument_list|(
name|QUOT
argument_list|,
literal|"\""
argument_list|)
expr_stmt|;
name|value
operator|=
name|decodeJson
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
name|row
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
comment|// reset
name|key
operator|=
literal|null
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|row
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|answer
operator|.
name|add
argument_list|(
name|row
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
DECL|method|decodeJson (String value)
DECL|method|decodeJson (String value)
specifier|private
specifier|static
name|String
name|decodeJson
parameter_list|(
name|String
name|value
parameter_list|)
block|{
comment|// json encodes a \ as \\ so we need to decode from \\ back to \
if|if
condition|(
literal|"\\\\"
operator|.
name|equals
argument_list|(
name|value
argument_list|)
condition|)
block|{
name|value
operator|=
literal|"\\"
expr_stmt|;
block|}
return|return
name|value
return|;
block|}
comment|/**      * Is the property required      *      * @param rows the rows of properties      * @param name name of the property      * @return<tt>true</tt> if required, or<tt>false</tt> if not      */
DECL|method|isPropertyRequired (List<Map<String, String>> rows, String name)
DECL|method|isPropertyRequired (List<Map<String, String>> rows, String name)
specifier|public
specifier|static
name|boolean
name|isPropertyRequired
parameter_list|(
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|rows
parameter_list|,
name|String
name|name
parameter_list|)
block|{
for|for
control|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|row
range|:
name|rows
control|)
block|{
name|boolean
name|required
init|=
literal|false
decl_stmt|;
name|boolean
name|found
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|row
operator|.
name|containsKey
argument_list|(
literal|"name"
argument_list|)
condition|)
block|{
name|found
operator|=
name|name
operator|.
name|equals
argument_list|(
name|row
operator|.
name|get
argument_list|(
literal|"name"
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|row
operator|.
name|containsKey
argument_list|(
literal|"required"
argument_list|)
condition|)
block|{
name|required
operator|=
literal|"true"
operator|.
name|equals
argument_list|(
name|row
operator|.
name|get
argument_list|(
literal|"required"
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|found
condition|)
block|{
return|return
name|required
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
comment|/**      * Gets the default value of the property      *      * @param rows the rows of properties      * @param name name of the property      * @return the default value or<tt>null</tt> if no default value exists      */
DECL|method|getPropertyDefaultValue (List<Map<String, String>> rows, String name)
DECL|method|getPropertyDefaultValue (List<Map<String, String>> rows, String name)
specifier|public
specifier|static
name|String
name|getPropertyDefaultValue
parameter_list|(
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|rows
parameter_list|,
name|String
name|name
parameter_list|)
block|{
for|for
control|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|row
range|:
name|rows
control|)
block|{
name|String
name|defaultValue
init|=
literal|null
decl_stmt|;
name|boolean
name|found
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|row
operator|.
name|containsKey
argument_list|(
literal|"name"
argument_list|)
condition|)
block|{
name|found
operator|=
name|name
operator|.
name|equals
argument_list|(
name|row
operator|.
name|get
argument_list|(
literal|"name"
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|row
operator|.
name|containsKey
argument_list|(
literal|"defaultValue"
argument_list|)
condition|)
block|{
name|defaultValue
operator|=
name|row
operator|.
name|get
argument_list|(
literal|"defaultValue"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|found
condition|)
block|{
return|return
name|defaultValue
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

