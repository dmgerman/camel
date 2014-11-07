begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
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
specifier|public
specifier|final
class|class
name|JsonSchemaHelper
block|{
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
literal|"\"(.+?)\""
argument_list|)
decl_stmt|;
DECL|method|JsonSchemaHelper ()
specifier|private
name|JsonSchemaHelper
parameter_list|()
block|{     }
comment|/**      * Gets the JSon schema type.      *      * @param   type the java type      * @return  the json schema type, is never null, but returns<tt>object</tt> as the generic type      */
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
comment|/**      * Gets the JSon schema primitive type.      *      * @param   type the java type      * @return  the json schema primitive type, or<tt>null</tt> if not a primitive      */
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
comment|/**      * Extracts the description value from the blob of json with the given property name      *      * @param json the blob of json      * @param name the name of the property to extract the description      * @return the value of the description, or<tt>null</tt> if no description exists      */
DECL|method|getDescription (String json, String name)
specifier|public
specifier|static
name|String
name|getDescription
parameter_list|(
name|String
name|json
parameter_list|,
name|String
name|name
parameter_list|)
block|{
comment|// we dont have a json parser, but we know the structure, so just do this simple way
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
literal|"\""
operator|+
name|name
operator|+
literal|"\":"
argument_list|)
condition|)
block|{
comment|// grab text after description
name|String
name|value
init|=
name|ObjectHelper
operator|.
name|after
argument_list|(
name|line
argument_list|,
literal|"\"description\": \""
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|int
name|lastQuote
init|=
name|value
operator|.
name|lastIndexOf
argument_list|(
literal|'"'
argument_list|)
decl_stmt|;
name|value
operator|=
name|value
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|lastQuote
argument_list|)
expr_stmt|;
name|value
operator|=
name|StringHelper
operator|.
name|removeLeadingAndEndingQuotes
argument_list|(
name|value
argument_list|)
expr_stmt|;
return|return
name|value
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Parses the endpoint explain json      *      * @param json the json      * @return a list of all the options, where each row is a set of key value pairs with metadata      */
DECL|method|parseEndpointExplainJson (String json)
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
name|parseEndpointExplainJson
parameter_list|(
name|String
name|json
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
comment|// parse line by line
comment|// skip first 2 lines as they are leading
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
name|int
name|i
init|=
literal|2
init|;
name|i
operator|<
name|lines
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|String
name|line
init|=
name|lines
index|[
name|i
index|]
decl_stmt|;
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
argument_list|<>
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
comment|// the first key is the name of the option
name|String
name|key
init|=
literal|"name"
decl_stmt|;
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
block|}
end_class

end_unit

