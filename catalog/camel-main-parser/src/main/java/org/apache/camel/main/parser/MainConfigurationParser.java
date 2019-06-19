begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.main.parser
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|main
operator|.
name|parser
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
name|io
operator|.
name|FileNotFoundException
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
name|List
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
name|jboss
operator|.
name|forge
operator|.
name|roaster
operator|.
name|Roaster
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|forge
operator|.
name|roaster
operator|.
name|model
operator|.
name|source
operator|.
name|FieldSource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|forge
operator|.
name|roaster
operator|.
name|model
operator|.
name|source
operator|.
name|JavaClassSource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|forge
operator|.
name|roaster
operator|.
name|model
operator|.
name|source
operator|.
name|MethodSource
import|;
end_import

begin_class
DECL|class|MainConfigurationParser
specifier|public
class|class
name|MainConfigurationParser
block|{
comment|/**      * Parses the Camel Main configuration java source file.      */
DECL|method|parseConfigurationSource (String fileName)
specifier|public
name|List
argument_list|<
name|ConfigurationModel
argument_list|>
name|parseConfigurationSource
parameter_list|(
name|String
name|fileName
parameter_list|)
throws|throws
name|FileNotFoundException
block|{
specifier|final
name|List
argument_list|<
name|ConfigurationModel
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|JavaClassSource
name|clazz
init|=
operator|(
name|JavaClassSource
operator|)
name|Roaster
operator|.
name|parse
argument_list|(
operator|new
name|File
argument_list|(
name|fileName
argument_list|)
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|FieldSource
argument_list|<
name|JavaClassSource
argument_list|>
argument_list|>
name|fields
init|=
name|clazz
operator|.
name|getFields
argument_list|()
decl_stmt|;
comment|// filter out final or static fields
name|fields
operator|=
name|fields
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|f
lambda|->
operator|!
name|f
operator|.
name|isFinal
argument_list|()
operator|&&
operator|!
name|f
operator|.
name|isStatic
argument_list|()
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
expr_stmt|;
name|fields
operator|.
name|forEach
argument_list|(
name|f
lambda|->
block|{
name|String
name|name
init|=
name|f
operator|.
name|getName
argument_list|()
decl_stmt|;
name|String
name|javaType
init|=
name|f
operator|.
name|getType
argument_list|()
operator|.
name|getQualifiedName
argument_list|()
decl_stmt|;
name|String
name|defaultValue
init|=
name|f
operator|.
name|getStringInitializer
argument_list|()
decl_stmt|;
comment|// the field must have a setter
name|String
name|setterName
init|=
literal|"set"
operator|+
name|Character
operator|.
name|toUpperCase
argument_list|(
name|name
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|+
name|name
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|MethodSource
name|setter
init|=
name|clazz
operator|.
name|getMethod
argument_list|(
name|setterName
argument_list|,
name|javaType
argument_list|)
decl_stmt|;
if|if
condition|(
name|setter
operator|!=
literal|null
condition|)
block|{
name|String
name|desc
init|=
name|setter
operator|.
name|getJavaDoc
argument_list|()
operator|.
name|getFullText
argument_list|()
decl_stmt|;
name|ConfigurationModel
name|model
init|=
operator|new
name|ConfigurationModel
argument_list|()
decl_stmt|;
name|model
operator|.
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|model
operator|.
name|setJavaType
argument_list|(
name|javaType
argument_list|)
expr_stmt|;
name|model
operator|.
name|setDefaultValue
argument_list|(
name|defaultValue
argument_list|)
expr_stmt|;
name|model
operator|.
name|setDescription
argument_list|(
name|desc
argument_list|)
expr_stmt|;
name|answer
operator|.
name|add
argument_list|(
name|model
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

