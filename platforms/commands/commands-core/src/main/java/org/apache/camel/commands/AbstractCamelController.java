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
name|HashMap
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
name|LinkedHashSet
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|catalog
operator|.
name|CamelCatalog
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
name|DefaultCamelCatalog
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
name|JsonSchemaHelper
import|;
end_import

begin_comment
comment|/**  * Abstract {@link org.apache.camel.commands.CamelController} that implementators should extend.  */
end_comment

begin_class
DECL|class|AbstractCamelController
specifier|public
specifier|abstract
class|class
name|AbstractCamelController
implements|implements
name|CamelController
block|{
DECL|field|catalog
specifier|private
name|CamelCatalog
name|catalog
init|=
operator|new
name|DefaultCamelCatalog
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|listEipsCatalog (String filter)
specifier|public
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|listEipsCatalog
parameter_list|(
name|String
name|filter
parameter_list|)
throws|throws
name|Exception
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
name|filter
operator|!=
literal|null
condition|)
block|{
name|filter
operator|=
name|RegexUtil
operator|.
name|wildcardAsRegex
argument_list|(
name|filter
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|names
init|=
name|filter
operator|!=
literal|null
condition|?
name|catalog
operator|.
name|findModelNames
argument_list|(
name|filter
argument_list|)
else|:
name|catalog
operator|.
name|findModelNames
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|name
range|:
name|names
control|)
block|{
comment|// load models json data, and parse it to gather the model meta-data
name|String
name|json
init|=
name|catalog
operator|.
name|modelJSonSchema
argument_list|(
name|name
argument_list|)
decl_stmt|;
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
init|=
name|JsonSchemaHelper
operator|.
name|parseJsonSchema
argument_list|(
literal|"model"
argument_list|,
name|json
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|String
name|description
init|=
literal|null
decl_stmt|;
name|String
name|label
init|=
literal|null
decl_stmt|;
name|String
name|type
init|=
literal|null
decl_stmt|;
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
if|if
condition|(
name|row
operator|.
name|containsKey
argument_list|(
literal|"description"
argument_list|)
condition|)
block|{
name|description
operator|=
name|row
operator|.
name|get
argument_list|(
literal|"description"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|row
operator|.
name|containsKey
argument_list|(
literal|"label"
argument_list|)
condition|)
block|{
name|label
operator|=
name|row
operator|.
name|get
argument_list|(
literal|"label"
argument_list|)
expr_stmt|;
block|}
elseif|else
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
name|type
operator|=
name|row
operator|.
name|get
argument_list|(
literal|"javaType"
argument_list|)
expr_stmt|;
block|}
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|row
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|row
operator|.
name|put
argument_list|(
literal|"name"
argument_list|,
name|name
argument_list|)
expr_stmt|;
if|if
condition|(
name|description
operator|!=
literal|null
condition|)
block|{
name|row
operator|.
name|put
argument_list|(
literal|"description"
argument_list|,
name|description
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|label
operator|!=
literal|null
condition|)
block|{
name|row
operator|.
name|put
argument_list|(
literal|"label"
argument_list|,
name|label
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
name|row
operator|.
name|put
argument_list|(
literal|"type"
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
name|answer
operator|.
name|add
argument_list|(
name|row
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|listComponentsCatalog (String filter)
specifier|public
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|listComponentsCatalog
parameter_list|(
name|String
name|filter
parameter_list|)
throws|throws
name|Exception
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
name|filter
operator|!=
literal|null
condition|)
block|{
name|filter
operator|=
name|RegexUtil
operator|.
name|wildcardAsRegex
argument_list|(
name|filter
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|names
init|=
name|filter
operator|!=
literal|null
condition|?
name|catalog
operator|.
name|findComponentNames
argument_list|(
name|filter
argument_list|)
else|:
name|catalog
operator|.
name|findComponentNames
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|name
range|:
name|names
control|)
block|{
comment|// load component json data, and parse it to gather the component meta-data
name|String
name|json
init|=
name|catalog
operator|.
name|componentJSonSchema
argument_list|(
name|name
argument_list|)
decl_stmt|;
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
init|=
name|JsonSchemaHelper
operator|.
name|parseJsonSchema
argument_list|(
literal|"component"
argument_list|,
name|json
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|String
name|description
init|=
literal|null
decl_stmt|;
name|String
name|label
init|=
literal|null
decl_stmt|;
comment|// the status can be:
comment|// - loaded = in use
comment|// - classpath = on the classpath
comment|// - release = available from the Apache Camel release
name|String
name|status
init|=
literal|"release"
decl_stmt|;
name|String
name|type
init|=
literal|null
decl_stmt|;
name|String
name|groupId
init|=
literal|null
decl_stmt|;
name|String
name|artifactId
init|=
literal|null
decl_stmt|;
name|String
name|version
init|=
literal|null
decl_stmt|;
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
if|if
condition|(
name|row
operator|.
name|containsKey
argument_list|(
literal|"description"
argument_list|)
condition|)
block|{
name|description
operator|=
name|row
operator|.
name|get
argument_list|(
literal|"description"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|row
operator|.
name|containsKey
argument_list|(
literal|"label"
argument_list|)
condition|)
block|{
name|label
operator|=
name|row
operator|.
name|get
argument_list|(
literal|"label"
argument_list|)
expr_stmt|;
block|}
elseif|else
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
name|type
operator|=
name|row
operator|.
name|get
argument_list|(
literal|"javaType"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|row
operator|.
name|containsKey
argument_list|(
literal|"groupId"
argument_list|)
condition|)
block|{
name|groupId
operator|=
name|row
operator|.
name|get
argument_list|(
literal|"groupId"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|row
operator|.
name|containsKey
argument_list|(
literal|"artifactId"
argument_list|)
condition|)
block|{
name|artifactId
operator|=
name|row
operator|.
name|get
argument_list|(
literal|"artifactId"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|row
operator|.
name|containsKey
argument_list|(
literal|"version"
argument_list|)
condition|)
block|{
name|version
operator|=
name|row
operator|.
name|get
argument_list|(
literal|"version"
argument_list|)
expr_stmt|;
block|}
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|row
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|row
operator|.
name|put
argument_list|(
literal|"name"
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|row
operator|.
name|put
argument_list|(
literal|"status"
argument_list|,
name|status
argument_list|)
expr_stmt|;
if|if
condition|(
name|description
operator|!=
literal|null
condition|)
block|{
name|row
operator|.
name|put
argument_list|(
literal|"description"
argument_list|,
name|description
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|label
operator|!=
literal|null
condition|)
block|{
name|row
operator|.
name|put
argument_list|(
literal|"label"
argument_list|,
name|label
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
name|row
operator|.
name|put
argument_list|(
literal|"type"
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|groupId
operator|!=
literal|null
condition|)
block|{
name|row
operator|.
name|put
argument_list|(
literal|"groupId"
argument_list|,
name|groupId
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|artifactId
operator|!=
literal|null
condition|)
block|{
name|row
operator|.
name|put
argument_list|(
literal|"artifactId"
argument_list|,
name|artifactId
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|version
operator|!=
literal|null
condition|)
block|{
name|row
operator|.
name|put
argument_list|(
literal|"version"
argument_list|,
name|version
argument_list|)
expr_stmt|;
block|}
name|answer
operator|.
name|add
argument_list|(
name|row
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|listDataFormatsCatalog (String filter)
specifier|public
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|listDataFormatsCatalog
parameter_list|(
name|String
name|filter
parameter_list|)
throws|throws
name|Exception
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
name|filter
operator|!=
literal|null
condition|)
block|{
name|filter
operator|=
name|RegexUtil
operator|.
name|wildcardAsRegex
argument_list|(
name|filter
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|names
init|=
name|filter
operator|!=
literal|null
condition|?
name|catalog
operator|.
name|findDataFormatNames
argument_list|(
name|filter
argument_list|)
else|:
name|catalog
operator|.
name|findDataFormatNames
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|name
range|:
name|names
control|)
block|{
comment|// load dataformat json data, and parse it to gather the dataformat meta-data
name|String
name|json
init|=
name|catalog
operator|.
name|dataFormatJSonSchema
argument_list|(
name|name
argument_list|)
decl_stmt|;
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
init|=
name|JsonSchemaHelper
operator|.
name|parseJsonSchema
argument_list|(
literal|"dataformat"
argument_list|,
name|json
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|String
name|description
init|=
literal|null
decl_stmt|;
name|String
name|label
init|=
literal|null
decl_stmt|;
name|String
name|modelName
init|=
name|name
decl_stmt|;
comment|// the status can be:
comment|// - loaded = in use
comment|// - classpath = on the classpath
comment|// - release = available from the Apache Camel release
name|String
name|status
init|=
literal|"release"
decl_stmt|;
name|String
name|type
init|=
literal|null
decl_stmt|;
name|String
name|modelJavaType
init|=
literal|null
decl_stmt|;
name|String
name|groupId
init|=
literal|null
decl_stmt|;
name|String
name|artifactId
init|=
literal|null
decl_stmt|;
name|String
name|version
init|=
literal|null
decl_stmt|;
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
if|if
condition|(
name|row
operator|.
name|containsKey
argument_list|(
literal|"modelName"
argument_list|)
condition|)
block|{
name|modelName
operator|=
name|row
operator|.
name|get
argument_list|(
literal|"modelName"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|row
operator|.
name|containsKey
argument_list|(
literal|"description"
argument_list|)
condition|)
block|{
name|description
operator|=
name|row
operator|.
name|get
argument_list|(
literal|"description"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|row
operator|.
name|containsKey
argument_list|(
literal|"label"
argument_list|)
condition|)
block|{
name|label
operator|=
name|row
operator|.
name|get
argument_list|(
literal|"label"
argument_list|)
expr_stmt|;
block|}
elseif|else
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
name|type
operator|=
name|row
operator|.
name|get
argument_list|(
literal|"javaType"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|row
operator|.
name|containsKey
argument_list|(
literal|"modelJavaType"
argument_list|)
condition|)
block|{
name|modelJavaType
operator|=
name|row
operator|.
name|get
argument_list|(
literal|"modelJavaType"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|row
operator|.
name|containsKey
argument_list|(
literal|"groupId"
argument_list|)
condition|)
block|{
name|groupId
operator|=
name|row
operator|.
name|get
argument_list|(
literal|"groupId"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|row
operator|.
name|containsKey
argument_list|(
literal|"artifactId"
argument_list|)
condition|)
block|{
name|artifactId
operator|=
name|row
operator|.
name|get
argument_list|(
literal|"artifactId"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|row
operator|.
name|containsKey
argument_list|(
literal|"version"
argument_list|)
condition|)
block|{
name|version
operator|=
name|row
operator|.
name|get
argument_list|(
literal|"version"
argument_list|)
expr_stmt|;
block|}
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|row
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|row
operator|.
name|put
argument_list|(
literal|"name"
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|row
operator|.
name|put
argument_list|(
literal|"modelName"
argument_list|,
name|modelName
argument_list|)
expr_stmt|;
name|row
operator|.
name|put
argument_list|(
literal|"status"
argument_list|,
name|status
argument_list|)
expr_stmt|;
if|if
condition|(
name|description
operator|!=
literal|null
condition|)
block|{
name|row
operator|.
name|put
argument_list|(
literal|"description"
argument_list|,
name|description
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|label
operator|!=
literal|null
condition|)
block|{
name|row
operator|.
name|put
argument_list|(
literal|"label"
argument_list|,
name|label
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
name|row
operator|.
name|put
argument_list|(
literal|"type"
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|modelJavaType
operator|!=
literal|null
condition|)
block|{
name|row
operator|.
name|put
argument_list|(
literal|"modelJavaType"
argument_list|,
name|modelJavaType
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|groupId
operator|!=
literal|null
condition|)
block|{
name|row
operator|.
name|put
argument_list|(
literal|"groupId"
argument_list|,
name|groupId
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|artifactId
operator|!=
literal|null
condition|)
block|{
name|row
operator|.
name|put
argument_list|(
literal|"artifactId"
argument_list|,
name|artifactId
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|version
operator|!=
literal|null
condition|)
block|{
name|row
operator|.
name|put
argument_list|(
literal|"version"
argument_list|,
name|version
argument_list|)
expr_stmt|;
block|}
name|answer
operator|.
name|add
argument_list|(
name|row
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|listLanguagesCatalog (String filter)
specifier|public
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|listLanguagesCatalog
parameter_list|(
name|String
name|filter
parameter_list|)
throws|throws
name|Exception
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
name|filter
operator|!=
literal|null
condition|)
block|{
name|filter
operator|=
name|RegexUtil
operator|.
name|wildcardAsRegex
argument_list|(
name|filter
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|names
init|=
name|filter
operator|!=
literal|null
condition|?
name|catalog
operator|.
name|findLanguageNames
argument_list|(
name|filter
argument_list|)
else|:
name|catalog
operator|.
name|findLanguageNames
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|name
range|:
name|names
control|)
block|{
comment|// load language json data, and parse it to gather the language meta-data
name|String
name|json
init|=
name|catalog
operator|.
name|languageJSonSchema
argument_list|(
name|name
argument_list|)
decl_stmt|;
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
init|=
name|JsonSchemaHelper
operator|.
name|parseJsonSchema
argument_list|(
literal|"language"
argument_list|,
name|json
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|String
name|description
init|=
literal|null
decl_stmt|;
name|String
name|label
init|=
literal|null
decl_stmt|;
name|String
name|modelName
init|=
name|name
decl_stmt|;
comment|// the status can be:
comment|// - loaded = in use
comment|// - classpath = on the classpath
comment|// - release = available from the Apache Camel release
name|String
name|status
init|=
literal|"release"
decl_stmt|;
name|String
name|type
init|=
literal|null
decl_stmt|;
name|String
name|modelJavaType
init|=
literal|null
decl_stmt|;
name|String
name|groupId
init|=
literal|null
decl_stmt|;
name|String
name|artifactId
init|=
literal|null
decl_stmt|;
name|String
name|version
init|=
literal|null
decl_stmt|;
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
if|if
condition|(
name|row
operator|.
name|containsKey
argument_list|(
literal|"modelName"
argument_list|)
condition|)
block|{
name|modelName
operator|=
name|row
operator|.
name|get
argument_list|(
literal|"modelName"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|row
operator|.
name|containsKey
argument_list|(
literal|"description"
argument_list|)
condition|)
block|{
name|description
operator|=
name|row
operator|.
name|get
argument_list|(
literal|"description"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|row
operator|.
name|containsKey
argument_list|(
literal|"label"
argument_list|)
condition|)
block|{
name|label
operator|=
name|row
operator|.
name|get
argument_list|(
literal|"label"
argument_list|)
expr_stmt|;
block|}
elseif|else
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
name|type
operator|=
name|row
operator|.
name|get
argument_list|(
literal|"javaType"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|row
operator|.
name|containsKey
argument_list|(
literal|"modelJavaType"
argument_list|)
condition|)
block|{
name|modelJavaType
operator|=
name|row
operator|.
name|get
argument_list|(
literal|"modelJavaType"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|row
operator|.
name|containsKey
argument_list|(
literal|"groupId"
argument_list|)
condition|)
block|{
name|groupId
operator|=
name|row
operator|.
name|get
argument_list|(
literal|"groupId"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|row
operator|.
name|containsKey
argument_list|(
literal|"artifactId"
argument_list|)
condition|)
block|{
name|artifactId
operator|=
name|row
operator|.
name|get
argument_list|(
literal|"artifactId"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|row
operator|.
name|containsKey
argument_list|(
literal|"version"
argument_list|)
condition|)
block|{
name|version
operator|=
name|row
operator|.
name|get
argument_list|(
literal|"version"
argument_list|)
expr_stmt|;
block|}
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|row
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|row
operator|.
name|put
argument_list|(
literal|"name"
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|row
operator|.
name|put
argument_list|(
literal|"modelName"
argument_list|,
name|modelName
argument_list|)
expr_stmt|;
name|row
operator|.
name|put
argument_list|(
literal|"status"
argument_list|,
name|status
argument_list|)
expr_stmt|;
if|if
condition|(
name|description
operator|!=
literal|null
condition|)
block|{
name|row
operator|.
name|put
argument_list|(
literal|"description"
argument_list|,
name|description
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|label
operator|!=
literal|null
condition|)
block|{
name|row
operator|.
name|put
argument_list|(
literal|"label"
argument_list|,
name|label
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
name|row
operator|.
name|put
argument_list|(
literal|"type"
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|modelJavaType
operator|!=
literal|null
condition|)
block|{
name|row
operator|.
name|put
argument_list|(
literal|"modelJavaType"
argument_list|,
name|modelJavaType
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|groupId
operator|!=
literal|null
condition|)
block|{
name|row
operator|.
name|put
argument_list|(
literal|"groupId"
argument_list|,
name|groupId
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|artifactId
operator|!=
literal|null
condition|)
block|{
name|row
operator|.
name|put
argument_list|(
literal|"artifactId"
argument_list|,
name|artifactId
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|version
operator|!=
literal|null
condition|)
block|{
name|row
operator|.
name|put
argument_list|(
literal|"version"
argument_list|,
name|version
argument_list|)
expr_stmt|;
block|}
name|answer
operator|.
name|add
argument_list|(
name|row
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|listEipsLabelCatalog ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|listEipsLabelCatalog
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|answer
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|labels
init|=
name|catalog
operator|.
name|findModelLabels
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|label
range|:
name|labels
control|)
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
name|models
init|=
name|listEipsCatalog
argument_list|(
name|label
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|models
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|names
init|=
operator|new
name|LinkedHashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|info
range|:
name|models
control|)
block|{
name|String
name|name
init|=
name|info
operator|.
name|get
argument_list|(
literal|"name"
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
name|names
operator|.
name|add
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
block|}
name|answer
operator|.
name|put
argument_list|(
name|label
argument_list|,
name|names
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|listComponentsLabelCatalog ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|listComponentsLabelCatalog
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|answer
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|labels
init|=
name|catalog
operator|.
name|findComponentLabels
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|label
range|:
name|labels
control|)
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
name|components
init|=
name|listComponentsCatalog
argument_list|(
name|label
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|components
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|names
init|=
operator|new
name|LinkedHashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|info
range|:
name|components
control|)
block|{
name|String
name|name
init|=
name|info
operator|.
name|get
argument_list|(
literal|"name"
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
name|names
operator|.
name|add
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
block|}
name|answer
operator|.
name|put
argument_list|(
name|label
argument_list|,
name|names
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|listDataFormatsLabelCatalog ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|listDataFormatsLabelCatalog
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|answer
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|labels
init|=
name|catalog
operator|.
name|findDataFormatLabels
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|label
range|:
name|labels
control|)
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
name|dataFormats
init|=
name|listDataFormatsCatalog
argument_list|(
name|label
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|dataFormats
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|names
init|=
operator|new
name|LinkedHashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|info
range|:
name|dataFormats
control|)
block|{
name|String
name|name
init|=
name|info
operator|.
name|get
argument_list|(
literal|"name"
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
name|names
operator|.
name|add
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
block|}
name|answer
operator|.
name|put
argument_list|(
name|label
argument_list|,
name|names
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|listLanguagesLabelCatalog ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|listLanguagesLabelCatalog
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|answer
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|labels
init|=
name|catalog
operator|.
name|findLanguageLabels
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|label
range|:
name|labels
control|)
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
name|languages
init|=
name|listLanguagesCatalog
argument_list|(
name|label
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|languages
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|names
init|=
operator|new
name|LinkedHashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|info
range|:
name|languages
control|)
block|{
name|String
name|name
init|=
name|info
operator|.
name|get
argument_list|(
literal|"name"
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
name|names
operator|.
name|add
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
block|}
name|answer
operator|.
name|put
argument_list|(
name|label
argument_list|,
name|names
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

