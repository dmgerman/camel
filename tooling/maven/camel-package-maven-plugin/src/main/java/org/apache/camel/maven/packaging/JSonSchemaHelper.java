begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.maven.packaging
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|maven
operator|.
name|packaging
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
name|org
operator|.
name|json
operator|.
name|simple
operator|.
name|JsonObject
import|;
end_import

begin_import
import|import
name|org
operator|.
name|json
operator|.
name|simple
operator|.
name|Jsoner
import|;
end_import

begin_class
DECL|class|JSonSchemaHelper
specifier|public
specifier|final
class|class
name|JSonSchemaHelper
block|{
DECL|method|JSonSchemaHelper ()
specifier|private
name|JSonSchemaHelper
parameter_list|()
block|{     }
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
name|JsonObject
name|output
init|=
name|Jsoner
operator|.
name|deserialize
argument_list|(
name|json
argument_list|,
operator|new
name|JsonObject
argument_list|()
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
name|list
init|=
operator|(
name|List
operator|)
name|newValue
decl_stmt|;
name|CollectionStringBuffer
name|csb
init|=
operator|new
name|CollectionStringBuffer
argument_list|(
literal|","
argument_list|)
decl_stmt|;
for|for
control|(
name|Object
name|line
range|:
name|list
control|)
block|{
name|csb
operator|.
name|append
argument_list|(
name|line
argument_list|)
expr_stmt|;
block|}
name|newValue
operator|=
name|csb
operator|.
name|toString
argument_list|()
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
comment|/**      * Gets the value with the key in a safe way, eg returning an empty string if there was no value for the key.      */
DECL|method|getSafeValue (String key, List<Map<String, String>> rows)
specifier|public
specifier|static
name|String
name|getSafeValue
parameter_list|(
name|String
name|key
parameter_list|,
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
name|value
init|=
name|row
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
return|return
name|value
return|;
block|}
block|}
return|return
literal|""
return|;
block|}
comment|/**      * Gets the value with the key in a safe way, eg returning an empty string if there was no value for the key.      */
DECL|method|getSafeValue (String key, Map<String, String> rows)
specifier|public
specifier|static
name|String
name|getSafeValue
parameter_list|(
name|String
name|key
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|rows
parameter_list|)
block|{
name|String
name|value
init|=
name|rows
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
return|return
name|value
return|;
block|}
return|return
literal|""
return|;
block|}
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
DECL|method|getPropertyJavaType (List<Map<String, String>> rows, String name)
specifier|public
specifier|static
name|String
name|getPropertyJavaType
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
name|javaType
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
literal|"javaType"
argument_list|)
condition|)
block|{
name|javaType
operator|=
name|row
operator|.
name|get
argument_list|(
literal|"javaType"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|found
condition|)
block|{
return|return
name|javaType
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|getPropertyType (List<Map<String, String>> rows, String name)
specifier|public
specifier|static
name|String
name|getPropertyType
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
name|type
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
literal|"type"
argument_list|)
condition|)
block|{
name|type
operator|=
name|row
operator|.
name|get
argument_list|(
literal|"type"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|found
condition|)
block|{
return|return
name|type
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

