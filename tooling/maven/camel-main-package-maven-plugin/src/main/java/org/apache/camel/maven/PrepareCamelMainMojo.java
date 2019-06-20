begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.maven
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|maven
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
name|FileOutputStream
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|main
operator|.
name|parser
operator|.
name|ConfigurationModel
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
name|main
operator|.
name|parser
operator|.
name|MainConfigurationParser
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|plugin
operator|.
name|AbstractMojo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|plugin
operator|.
name|MojoExecutionException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|plugin
operator|.
name|MojoFailureException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|plugins
operator|.
name|annotations
operator|.
name|LifecyclePhase
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|plugins
operator|.
name|annotations
operator|.
name|Mojo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|plugins
operator|.
name|annotations
operator|.
name|Parameter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|plugins
operator|.
name|annotations
operator|.
name|ResolutionScope
import|;
end_import

begin_comment
comment|/**  * Prepares camel-main by generating Camel Main configuration metadata for tooling support.  */
end_comment

begin_class
annotation|@
name|Mojo
argument_list|(
name|name
operator|=
literal|"prepare-main"
argument_list|,
name|defaultPhase
operator|=
name|LifecyclePhase
operator|.
name|PROCESS_CLASSES
argument_list|,
name|threadSafe
operator|=
literal|true
argument_list|,
name|requiresDependencyResolution
operator|=
name|ResolutionScope
operator|.
name|COMPILE
argument_list|)
DECL|class|PrepareCamelMainMojo
specifier|public
class|class
name|PrepareCamelMainMojo
extends|extends
name|AbstractMojo
block|{
comment|/**      * The output directory for generated spring boot tooling file      */
annotation|@
name|Parameter
argument_list|(
name|readonly
operator|=
literal|true
argument_list|,
name|defaultValue
operator|=
literal|"${project.build.directory}/../src/main/resources/META-INF/"
argument_list|)
DECL|field|outFolder
specifier|protected
name|File
name|outFolder
decl_stmt|;
comment|/**      * The build directory      */
annotation|@
name|Parameter
argument_list|(
name|readonly
operator|=
literal|true
argument_list|,
name|defaultValue
operator|=
literal|"${project.build.directory}/"
argument_list|)
DECL|field|buildDir
specifier|protected
name|File
name|buildDir
decl_stmt|;
annotation|@
name|Override
DECL|method|execute ()
specifier|public
name|void
name|execute
parameter_list|()
throws|throws
name|MojoExecutionException
throws|,
name|MojoFailureException
block|{
specifier|final
name|List
argument_list|<
name|ConfigurationModel
argument_list|>
name|data
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|MainConfigurationParser
name|parser
init|=
operator|new
name|MainConfigurationParser
argument_list|()
decl_stmt|;
comment|// scan for configuration files
name|File
index|[]
name|files
init|=
operator|new
name|File
argument_list|(
name|buildDir
argument_list|,
literal|"../src/main/java/org/apache/camel/main"
argument_list|)
operator|.
name|listFiles
argument_list|(
name|f
lambda|->
name|f
operator|.
name|isFile
argument_list|()
operator|&&
name|f
operator|.
name|getName
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|"Properties.java"
argument_list|)
argument_list|)
decl_stmt|;
for|for
control|(
name|File
name|file
range|:
name|files
control|)
block|{
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|"Parsing Camel Main configuration file: "
operator|+
name|file
argument_list|)
expr_stmt|;
try|try
block|{
name|List
argument_list|<
name|ConfigurationModel
argument_list|>
name|model
init|=
name|parser
operator|.
name|parseConfigurationSource
argument_list|(
name|file
argument_list|)
decl_stmt|;
comment|// compute prefix for name
name|String
name|prefix
init|=
literal|"camel.main."
decl_stmt|;
if|if
condition|(
name|file
operator|.
name|getName
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Hystrix"
argument_list|)
condition|)
block|{
name|prefix
operator|+=
literal|"hystrix."
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|file
operator|.
name|getName
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Rest"
argument_list|)
condition|)
block|{
name|prefix
operator|+=
literal|"rest."
expr_stmt|;
block|}
specifier|final
name|String
name|namePrefix
init|=
name|prefix
decl_stmt|;
name|model
operator|.
name|stream
argument_list|()
operator|.
name|forEach
argument_list|(
name|m
lambda|->
name|m
operator|.
name|setName
argument_list|(
name|namePrefix
operator|+
name|m
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|data
operator|.
name|addAll
argument_list|(
name|model
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MojoFailureException
argument_list|(
literal|"Error parsing file "
operator|+
name|file
operator|+
literal|" due "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|// lets sort so they are always ordered
name|data
operator|.
name|sort
argument_list|(
parameter_list|(
name|o1
parameter_list|,
name|o2
parameter_list|)
lambda|->
name|o1
operator|.
name|getName
argument_list|()
operator|.
name|compareToIgnoreCase
argument_list|(
name|o2
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|data
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
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
literal|"{\n"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"  \"properties\": [\n"
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|data
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|ConfigurationModel
name|row
init|=
name|data
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|String
name|name
init|=
name|camelCaseToDash
argument_list|(
name|row
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|javaType
init|=
name|springBootJavaType
argument_list|(
name|row
operator|.
name|getJavaType
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|desc
init|=
name|row
operator|.
name|getDescription
argument_list|()
decl_stmt|;
name|String
name|defaultValue
init|=
name|row
operator|.
name|getDefaultValue
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"    {\n"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"      \"name\": \""
operator|+
name|name
operator|+
literal|"\",\n"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"      \"type\": \""
operator|+
name|javaType
operator|+
literal|"\",\n"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"      \"description\": \""
operator|+
name|desc
operator|+
literal|"\""
argument_list|)
expr_stmt|;
if|if
condition|(
name|defaultValue
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|",\n"
argument_list|)
expr_stmt|;
if|if
condition|(
name|springBootDefaultValueQuotes
argument_list|(
name|javaType
argument_list|)
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"      \"defaultValue\": \""
operator|+
name|defaultValue
operator|+
literal|"\"\n"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"      \"defaultValue\": "
operator|+
name|defaultValue
operator|+
literal|"\n"
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|i
operator|<
name|data
operator|.
name|size
argument_list|()
operator|-
literal|1
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"    },\n"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"    }\n"
argument_list|)
expr_stmt|;
block|}
block|}
name|sb
operator|.
name|append
argument_list|(
literal|"  ]\n"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"}\n"
argument_list|)
expr_stmt|;
name|outFolder
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|outFolder
argument_list|,
literal|"camel-main-configuration-metadata.json"
argument_list|)
decl_stmt|;
try|try
block|{
name|FileOutputStream
name|fos
init|=
operator|new
name|FileOutputStream
argument_list|(
name|file
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|fos
operator|.
name|write
argument_list|(
name|sb
operator|.
name|toString
argument_list|()
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|fos
operator|.
name|close
argument_list|()
expr_stmt|;
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|"Created file: "
operator|+
name|file
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MojoFailureException
argument_list|(
literal|"Cannot write to file "
operator|+
name|file
operator|+
literal|" due "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
DECL|method|camelCaseToDash (String name)
specifier|private
specifier|static
name|String
name|camelCaseToDash
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|name
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|char
name|ch
init|=
name|name
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|Character
operator|.
name|isUpperCase
argument_list|(
name|ch
argument_list|)
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"-"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|Character
operator|.
name|toLowerCase
argument_list|(
name|ch
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|sb
operator|.
name|append
argument_list|(
name|ch
argument_list|)
expr_stmt|;
block|}
block|}
comment|// somme words we dont want ID -> i-d, but keep it as id
name|String
name|answer
init|=
name|sb
operator|.
name|toString
argument_list|()
decl_stmt|;
name|answer
operator|=
name|answer
operator|.
name|replaceAll
argument_list|(
literal|"-i-d-"
argument_list|,
literal|"-id-"
argument_list|)
expr_stmt|;
name|answer
operator|=
name|answer
operator|.
name|replaceAll
argument_list|(
literal|"-u-r-i-"
argument_list|,
literal|"-uri-"
argument_list|)
expr_stmt|;
name|answer
operator|=
name|answer
operator|.
name|replaceAll
argument_list|(
literal|"-u-r-l-"
argument_list|,
literal|"-url-"
argument_list|)
expr_stmt|;
name|answer
operator|=
name|answer
operator|.
name|replaceAll
argument_list|(
literal|"-j-m-s-"
argument_list|,
literal|"-jms-"
argument_list|)
expr_stmt|;
name|answer
operator|=
name|answer
operator|.
name|replaceAll
argument_list|(
literal|"-j-m-x-"
argument_list|,
literal|"-jmx-"
argument_list|)
expr_stmt|;
name|answer
operator|=
name|answer
operator|.
name|replaceFirst
argument_list|(
literal|"-i-d$"
argument_list|,
literal|"-id"
argument_list|)
expr_stmt|;
name|answer
operator|=
name|answer
operator|.
name|replaceFirst
argument_list|(
literal|"-u-r-i$"
argument_list|,
literal|"-uri"
argument_list|)
expr_stmt|;
name|answer
operator|=
name|answer
operator|.
name|replaceFirst
argument_list|(
literal|"-u-r-l$"
argument_list|,
literal|"-url"
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|springBootJavaType (String javaType)
specifier|private
specifier|static
name|String
name|springBootJavaType
parameter_list|(
name|String
name|javaType
parameter_list|)
block|{
if|if
condition|(
literal|"boolean"
operator|.
name|equalsIgnoreCase
argument_list|(
name|javaType
argument_list|)
condition|)
block|{
return|return
literal|"java.lang.Boolean"
return|;
block|}
elseif|else
if|if
condition|(
literal|"int"
operator|.
name|equalsIgnoreCase
argument_list|(
name|javaType
argument_list|)
condition|)
block|{
return|return
literal|"java.lang.Integer"
return|;
block|}
elseif|else
if|if
condition|(
literal|"long"
operator|.
name|equalsIgnoreCase
argument_list|(
name|javaType
argument_list|)
condition|)
block|{
return|return
literal|"java.lang.Long"
return|;
block|}
elseif|else
if|if
condition|(
literal|"string"
operator|.
name|equalsIgnoreCase
argument_list|(
name|javaType
argument_list|)
condition|)
block|{
return|return
literal|"java.lang.String"
return|;
block|}
return|return
name|javaType
return|;
block|}
DECL|method|springBootDefaultValueQuotes (String javaType)
specifier|private
specifier|static
name|boolean
name|springBootDefaultValueQuotes
parameter_list|(
name|String
name|javaType
parameter_list|)
block|{
if|if
condition|(
literal|"java.lang.Boolean"
operator|.
name|equalsIgnoreCase
argument_list|(
name|javaType
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
elseif|else
if|if
condition|(
literal|"java.lang.Integer"
operator|.
name|equalsIgnoreCase
argument_list|(
name|javaType
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
elseif|else
if|if
condition|(
literal|"java.lang.Long"
operator|.
name|equalsIgnoreCase
argument_list|(
name|javaType
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

