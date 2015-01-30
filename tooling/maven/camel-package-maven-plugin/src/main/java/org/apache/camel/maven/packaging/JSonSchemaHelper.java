begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.maven.packaging
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

begin_class
DECL|class|JSonSchemaHelper
DECL|class|JSonSchemaHelper
specifier|public
specifier|final
class|class
name|JSonSchemaHelper
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
DECL|method|JSonSchemaHelper ()
DECL|method|JSonSchemaHelper ()
specifier|private
name|JSonSchemaHelper
parameter_list|()
block|{     }
comment|/**      * Parses the json schema to split it into a list or rows, where each row contains key value pairs with the metadata      *      * @param group the group to parse from such as<tt>component</tt>,<tt>componentProperties</tt>, or<tt>properties</tt>.      * @param json the json      * @return a list of all the rows, where each row is a set of key value pairs with metadata      */
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
block|}
end_class

end_unit

