begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.commands
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|commands
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintStream
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
name|PatternSyntaxException
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
name|catalog
operator|.
name|CatalogHelper
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
name|commands
operator|.
name|internal
operator|.
name|RegexUtil
import|;
end_import

begin_comment
comment|/**  * Shows properties of a component from Catalog  */
end_comment

begin_class
DECL|class|CatalogComponentInfoCommand
specifier|public
class|class
name|CatalogComponentInfoCommand
extends|extends
name|AbstractCamelCommand
block|{
DECL|field|COMPONENT_PROPERTIES
specifier|private
specifier|static
specifier|final
name|String
index|[]
index|[]
name|COMPONENT_PROPERTIES
init|=
operator|new
name|String
index|[]
index|[]
block|{
block|{
literal|"Property"
block|,
literal|"Description"
block|}
block|,
block|{
literal|"key"
block|,
literal|"description"
block|}
block|}
decl_stmt|;
DECL|field|COMPONENT_PROPERTIES_VERBOSE
specifier|private
specifier|static
specifier|final
name|String
index|[]
index|[]
name|COMPONENT_PROPERTIES_VERBOSE
init|=
operator|new
name|String
index|[]
index|[]
block|{
block|{
literal|"Property"
block|,
literal|"Description"
block|}
block|,
block|{
literal|"key"
block|,
literal|"description"
block|}
block|}
decl_stmt|;
DECL|field|PROPERTIES
specifier|private
specifier|static
specifier|final
name|String
index|[]
index|[]
name|PROPERTIES
init|=
operator|new
name|String
index|[]
index|[]
block|{
block|{
literal|"Property"
block|,
literal|"Description"
block|}
block|,
block|{
literal|"key"
block|,
literal|"description"
block|}
block|}
decl_stmt|;
DECL|field|PROPERTIES_VERBOSE
specifier|private
specifier|static
specifier|final
name|String
index|[]
index|[]
name|PROPERTIES_VERBOSE
init|=
operator|new
name|String
index|[]
index|[]
block|{
block|{
literal|"Property"
block|,
literal|"Group"
block|,
literal|"Default Value"
block|,
literal|"Description"
block|}
block|,
block|{
literal|"key"
block|,
literal|"group"
block|,
literal|"defaultValue"
block|,
literal|"description"
block|}
block|}
decl_stmt|;
DECL|field|name
specifier|private
specifier|final
name|String
name|name
decl_stmt|;
DECL|field|verbose
specifier|private
specifier|final
name|boolean
name|verbose
decl_stmt|;
DECL|field|labelFilter
specifier|private
specifier|final
name|String
name|labelFilter
decl_stmt|;
DECL|method|CatalogComponentInfoCommand (String name, boolean verbose, String labelFilter)
specifier|public
name|CatalogComponentInfoCommand
parameter_list|(
name|String
name|name
parameter_list|,
name|boolean
name|verbose
parameter_list|,
name|String
name|labelFilter
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|verbose
operator|=
name|verbose
expr_stmt|;
name|this
operator|.
name|labelFilter
operator|=
name|labelFilter
operator|!=
literal|null
condition|?
name|RegexUtil
operator|.
name|wildcardAsRegex
argument_list|(
name|labelFilter
argument_list|)
else|:
literal|null
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|execute (CamelController camelController, PrintStream out, PrintStream err)
specifier|public
name|Object
name|execute
parameter_list|(
name|CamelController
name|camelController
parameter_list|,
name|PrintStream
name|out
parameter_list|,
name|PrintStream
name|err
parameter_list|)
throws|throws
name|Exception
block|{
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|info
init|=
name|camelController
operator|.
name|componentInfo
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|String
name|component
init|=
name|name
operator|.
name|toUpperCase
argument_list|()
decl_stmt|;
if|if
condition|(
name|info
operator|.
name|containsKey
argument_list|(
literal|"description"
argument_list|)
condition|)
block|{
name|component
operator|+=
literal|" :: "
operator|+
name|info
operator|.
name|get
argument_list|(
literal|"description"
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|println
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
name|component
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
name|buildSeparatorString
argument_list|(
name|component
operator|.
name|length
argument_list|()
argument_list|,
literal|'-'
argument_list|)
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|""
argument_list|)
expr_stmt|;
if|if
condition|(
name|info
operator|.
name|containsKey
argument_list|(
literal|"label"
argument_list|)
condition|)
block|{
name|out
operator|.
name|printf
argument_list|(
literal|"label: %s\n"
argument_list|,
name|info
operator|.
name|get
argument_list|(
literal|"label"
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|info
operator|.
name|containsKey
argument_list|(
literal|"groupId"
argument_list|)
operator|&&
name|info
operator|.
name|containsKey
argument_list|(
literal|"artifactId"
argument_list|)
operator|&&
name|info
operator|.
name|containsKey
argument_list|(
literal|"version"
argument_list|)
condition|)
block|{
name|out
operator|.
name|printf
argument_list|(
literal|"maven: %s\n"
argument_list|,
name|info
operator|.
name|get
argument_list|(
literal|"groupId"
argument_list|)
operator|+
literal|"/"
operator|+
name|info
operator|.
name|get
argument_list|(
literal|"artifactId"
argument_list|)
operator|+
literal|"/"
operator|+
name|info
operator|.
name|get
argument_list|(
literal|"version"
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|dumpProperties
argument_list|(
literal|"componentProperties"
argument_list|,
name|info
argument_list|,
name|verbose
condition|?
name|COMPONENT_PROPERTIES_VERBOSE
else|:
name|COMPONENT_PROPERTIES
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|dumpProperties
argument_list|(
literal|"properties"
argument_list|,
name|info
argument_list|,
name|verbose
condition|?
name|PROPERTIES_VERBOSE
else|:
name|PROPERTIES
argument_list|,
name|out
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
DECL|method|dumpProperties (String groupName, Map<String, Object> info, String[][] tableStruct, PrintStream out)
specifier|private
name|void
name|dumpProperties
parameter_list|(
name|String
name|groupName
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|info
parameter_list|,
name|String
index|[]
index|[]
name|tableStruct
parameter_list|,
name|PrintStream
name|out
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|group
init|=
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
operator|)
name|info
operator|.
name|get
argument_list|(
name|groupName
argument_list|)
decl_stmt|;
if|if
condition|(
name|group
operator|!=
literal|null
condition|)
block|{
name|CatalogComponentHelper
operator|.
name|Table
name|table
init|=
operator|new
name|CatalogComponentHelper
operator|.
name|Table
argument_list|(
name|tableStruct
index|[
literal|0
index|]
argument_list|,
name|tableStruct
index|[
literal|1
index|]
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|group
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|entry
operator|.
name|getValue
argument_list|()
operator|instanceof
name|Map
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|data
init|=
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
operator|)
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|matchLabel
argument_list|(
name|data
argument_list|)
condition|)
block|{
name|table
operator|.
name|addRow
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|data
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
operator|!
name|table
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|out
operator|.
name|println
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
name|groupName
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|table
operator|.
name|print
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|buildSeparatorString (int len, char pad)
specifier|private
specifier|static
name|String
name|buildSeparatorString
parameter_list|(
name|int
name|len
parameter_list|,
name|char
name|pad
parameter_list|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
name|len
argument_list|)
decl_stmt|;
while|while
condition|(
name|sb
operator|.
name|length
argument_list|()
operator|<
name|len
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|pad
argument_list|)
expr_stmt|;
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|matchLabel (Map<String, Object> properties)
specifier|private
name|boolean
name|matchLabel
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
parameter_list|)
block|{
if|if
condition|(
name|labelFilter
operator|==
literal|null
condition|)
block|{
return|return
literal|true
return|;
block|}
specifier|final
name|String
name|label
init|=
operator|(
name|String
operator|)
name|properties
operator|.
name|get
argument_list|(
literal|"label"
argument_list|)
decl_stmt|;
if|if
condition|(
name|label
operator|!=
literal|null
condition|)
block|{
name|String
index|[]
name|parts
init|=
name|label
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|part
range|:
name|parts
control|)
block|{
try|try
block|{
if|if
condition|(
name|part
operator|.
name|equalsIgnoreCase
argument_list|(
name|labelFilter
argument_list|)
operator|||
name|CatalogHelper
operator|.
name|matchWildcard
argument_list|(
name|part
argument_list|,
name|labelFilter
argument_list|)
operator|||
name|part
operator|.
name|matches
argument_list|(
name|labelFilter
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
catch|catch
parameter_list|(
name|PatternSyntaxException
name|e
parameter_list|)
block|{
comment|// ignore as filter is maybe not a pattern
block|}
block|}
block|}
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

