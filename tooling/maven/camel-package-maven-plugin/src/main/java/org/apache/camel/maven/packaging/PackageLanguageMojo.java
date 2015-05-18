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
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|maven
operator|.
name|packaging
operator|.
name|PackageHelper
operator|.
name|after
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|maven
operator|.
name|packaging
operator|.
name|PackageHelper
operator|.
name|loadText
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|maven
operator|.
name|packaging
operator|.
name|PackageHelper
operator|.
name|parseAsMap
import|;
end_import

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
name|FileInputStream
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
name|io
operator|.
name|FileWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
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
name|net
operator|.
name|URLClassLoader
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
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|Properties
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
name|artifact
operator|.
name|Artifact
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
name|model
operator|.
name|Resource
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
name|plugin
operator|.
name|logging
operator|.
name|Log
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
name|project
operator|.
name|MavenProject
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
name|project
operator|.
name|MavenProjectHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|sonatype
operator|.
name|plexus
operator|.
name|build
operator|.
name|incremental
operator|.
name|BuildContext
import|;
end_import

begin_comment
comment|/**  * Analyses the Camel plugins in a project and generates extra descriptor information for easier auto-discovery in Camel.  *  * @goal generate-languages-list  */
end_comment

begin_class
DECL|class|PackageLanguageMojo
specifier|public
class|class
name|PackageLanguageMojo
extends|extends
name|AbstractMojo
block|{
comment|/**      * The maven project.      *      * @parameter property="project"      * @required      * @readonly      */
DECL|field|project
specifier|protected
name|MavenProject
name|project
decl_stmt|;
comment|/**      * The output directory for generated languages file      *      * @parameter default-value="${project.build.directory}/generated/camel/languages"      */
DECL|field|languageOutDir
specifier|protected
name|File
name|languageOutDir
decl_stmt|;
comment|/**      * The output directory for generated languages file      *      * @parameter default-value="${project.build.directory}/classes"      */
DECL|field|schemaOutDir
specifier|protected
name|File
name|schemaOutDir
decl_stmt|;
comment|/**      * Maven ProjectHelper.      *      * @component      * @readonly      */
DECL|field|projectHelper
specifier|private
name|MavenProjectHelper
name|projectHelper
decl_stmt|;
comment|/**      * build context to check changed files and mark them for refresh      * (used for m2e compatibility)      *       * @component      * @readonly      */
DECL|field|buildContext
specifier|private
name|BuildContext
name|buildContext
decl_stmt|;
comment|/**      * Execute goal.      *      * @throws org.apache.maven.plugin.MojoExecutionException execution of the main class or one of the      *                                                        threads it generated failed.      * @throws org.apache.maven.plugin.MojoFailureException   something bad happened...      */
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
name|prepareLanguage
argument_list|(
name|getLog
argument_list|()
argument_list|,
name|project
argument_list|,
name|projectHelper
argument_list|,
name|languageOutDir
argument_list|,
name|schemaOutDir
argument_list|,
name|buildContext
argument_list|)
expr_stmt|;
block|}
DECL|method|prepareLanguage (Log log, MavenProject project, MavenProjectHelper projectHelper, File languageOutDir, File schemaOutDir, BuildContext buildContext)
specifier|public
specifier|static
name|void
name|prepareLanguage
parameter_list|(
name|Log
name|log
parameter_list|,
name|MavenProject
name|project
parameter_list|,
name|MavenProjectHelper
name|projectHelper
parameter_list|,
name|File
name|languageOutDir
parameter_list|,
name|File
name|schemaOutDir
parameter_list|,
name|BuildContext
name|buildContext
parameter_list|)
throws|throws
name|MojoExecutionException
block|{
name|File
name|camelMetaDir
init|=
operator|new
name|File
argument_list|(
name|languageOutDir
argument_list|,
literal|"META-INF/services/org/apache/camel/"
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|javaTypes
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
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|int
name|count
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Resource
name|r
range|:
name|project
operator|.
name|getBuild
argument_list|()
operator|.
name|getResources
argument_list|()
control|)
block|{
name|File
name|f
init|=
operator|new
name|File
argument_list|(
name|r
operator|.
name|getDirectory
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|f
operator|.
name|exists
argument_list|()
condition|)
block|{
name|f
operator|=
operator|new
name|File
argument_list|(
name|project
operator|.
name|getBasedir
argument_list|()
argument_list|,
name|r
operator|.
name|getDirectory
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|f
operator|=
operator|new
name|File
argument_list|(
name|f
argument_list|,
literal|"META-INF/services/org/apache/camel/language"
argument_list|)
expr_stmt|;
if|if
condition|(
name|f
operator|.
name|exists
argument_list|()
operator|&&
name|f
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|File
index|[]
name|files
init|=
name|f
operator|.
name|listFiles
argument_list|()
decl_stmt|;
if|if
condition|(
name|files
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|File
name|file
range|:
name|files
control|)
block|{
comment|// skip directories as there may be a sub .resolver directory such as in camel-script
if|if
condition|(
name|file
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
continue|continue;
block|}
name|String
name|name
init|=
name|file
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
operator|!=
literal|'.'
condition|)
block|{
name|count
operator|++
expr_stmt|;
if|if
condition|(
name|buffer
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
comment|// find out the javaType for each data format
try|try
block|{
name|String
name|text
init|=
name|loadText
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|file
argument_list|)
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
name|parseAsMap
argument_list|(
name|text
argument_list|)
decl_stmt|;
name|String
name|javaType
init|=
name|map
operator|.
name|get
argument_list|(
literal|"class"
argument_list|)
decl_stmt|;
if|if
condition|(
name|javaType
operator|!=
literal|null
condition|)
block|{
name|javaTypes
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|javaType
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
literal|"Failed to read file "
operator|+
name|file
operator|+
literal|". Reason: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
block|}
block|}
comment|// find camel-core and grab the data format model from there, and enrich this model with information from this artifact
comment|// and create json schema model file for this data format
try|try
block|{
if|if
condition|(
name|count
operator|>
literal|0
condition|)
block|{
name|Artifact
name|camelCore
init|=
name|findCamelCoreArtifact
argument_list|(
name|project
argument_list|)
decl_stmt|;
if|if
condition|(
name|camelCore
operator|!=
literal|null
condition|)
block|{
name|File
name|core
init|=
name|camelCore
operator|.
name|getFile
argument_list|()
decl_stmt|;
if|if
condition|(
name|core
operator|!=
literal|null
condition|)
block|{
name|URL
name|url
init|=
operator|new
name|URL
argument_list|(
literal|"file"
argument_list|,
literal|null
argument_list|,
name|core
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
decl_stmt|;
name|URLClassLoader
name|loader
init|=
operator|new
name|URLClassLoader
argument_list|(
operator|new
name|URL
index|[]
block|{
name|url
block|}
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
name|String
argument_list|>
name|entry
range|:
name|javaTypes
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|name
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|String
name|javaType
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|String
name|modelName
init|=
name|asModelName
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|InputStream
name|is
init|=
name|loader
operator|.
name|getResourceAsStream
argument_list|(
literal|"org/apache/camel/model/language/"
operator|+
name|modelName
operator|+
literal|".json"
argument_list|)
decl_stmt|;
if|if
condition|(
name|is
operator|==
literal|null
condition|)
block|{
comment|// use file input stream if we build camel-core itself, and thus do not have a JAR which can be loaded by URLClassLoader
name|is
operator|=
operator|new
name|FileInputStream
argument_list|(
operator|new
name|File
argument_list|(
name|core
argument_list|,
literal|"org/apache/camel/model/language/"
operator|+
name|modelName
operator|+
literal|".json"
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|String
name|json
init|=
name|loadText
argument_list|(
name|is
argument_list|)
decl_stmt|;
if|if
condition|(
name|json
operator|!=
literal|null
condition|)
block|{
name|LanguageModel
name|languageModel
init|=
operator|new
name|LanguageModel
argument_list|()
decl_stmt|;
name|languageModel
operator|.
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|languageModel
operator|.
name|setTitle
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|languageModel
operator|.
name|setModelName
argument_list|(
name|modelName
argument_list|)
expr_stmt|;
name|languageModel
operator|.
name|setLabel
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|languageModel
operator|.
name|setDescription
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|languageModel
operator|.
name|setJavaType
argument_list|(
name|javaType
argument_list|)
expr_stmt|;
name|languageModel
operator|.
name|setGroupId
argument_list|(
name|project
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|languageModel
operator|.
name|setArtifactId
argument_list|(
name|project
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|languageModel
operator|.
name|setVersion
argument_list|(
name|project
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
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
name|JSonSchemaHelper
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
literal|"title"
argument_list|)
condition|)
block|{
name|languageModel
operator|.
name|setTitle
argument_list|(
name|row
operator|.
name|get
argument_list|(
literal|"title"
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
literal|"description"
argument_list|)
condition|)
block|{
name|languageModel
operator|.
name|setDescription
argument_list|(
name|row
operator|.
name|get
argument_list|(
literal|"description"
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
literal|"label"
argument_list|)
condition|)
block|{
name|languageModel
operator|.
name|setLabel
argument_list|(
name|row
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
name|row
operator|.
name|containsKey
argument_list|(
literal|"javaType"
argument_list|)
condition|)
block|{
name|languageModel
operator|.
name|setModelJavaType
argument_list|(
name|row
operator|.
name|get
argument_list|(
literal|"javaType"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|log
operator|.
name|debug
argument_list|(
literal|"Model "
operator|+
name|languageModel
argument_list|)
expr_stmt|;
comment|// build json schema for the data format
name|String
name|properties
init|=
name|after
argument_list|(
name|json
argument_list|,
literal|"  \"properties\": {"
argument_list|)
decl_stmt|;
name|String
name|schema
init|=
name|createParameterJsonSchema
argument_list|(
name|languageModel
argument_list|,
name|properties
argument_list|)
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"JSon schema\n"
operator|+
name|schema
argument_list|)
expr_stmt|;
comment|// write this to the directory
name|File
name|dir
init|=
operator|new
name|File
argument_list|(
name|schemaOutDir
argument_list|,
name|schemaSubDirectory
argument_list|(
name|languageModel
operator|.
name|getJavaType
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|dir
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|File
name|out
init|=
operator|new
name|File
argument_list|(
name|dir
argument_list|,
name|name
operator|+
literal|".json"
argument_list|)
decl_stmt|;
name|FileOutputStream
name|fos
init|=
operator|new
name|FileOutputStream
argument_list|(
name|out
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|fos
operator|.
name|write
argument_list|(
name|schema
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
name|buildContext
operator|.
name|refresh
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Generated "
operator|+
name|out
operator|+
literal|" containing JSon schema for "
operator|+
name|name
operator|+
literal|" language"
argument_list|)
expr_stmt|;
block|}
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
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
literal|"Error loading language model from camel-core. Reason: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
throw|;
block|}
if|if
condition|(
name|count
operator|>
literal|0
condition|)
block|{
name|Properties
name|properties
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|String
name|names
init|=
name|buffer
operator|.
name|toString
argument_list|()
decl_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"languages"
argument_list|,
name|names
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"groupId"
argument_list|,
name|project
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"artifactId"
argument_list|,
name|project
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"version"
argument_list|,
name|project
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"projectName"
argument_list|,
name|project
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|project
operator|.
name|getDescription
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|properties
operator|.
name|put
argument_list|(
literal|"projectDescription"
argument_list|,
name|project
operator|.
name|getDescription
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|camelMetaDir
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|File
name|outFile
init|=
operator|new
name|File
argument_list|(
name|camelMetaDir
argument_list|,
literal|"language.properties"
argument_list|)
decl_stmt|;
try|try
block|{
name|properties
operator|.
name|store
argument_list|(
operator|new
name|FileWriter
argument_list|(
name|outFile
argument_list|)
argument_list|,
literal|"Generated by camel-package-maven-plugin"
argument_list|)
expr_stmt|;
name|buildContext
operator|.
name|refresh
argument_list|(
name|outFile
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Generated "
operator|+
name|outFile
operator|+
literal|" containing "
operator|+
name|count
operator|+
literal|" Camel "
operator|+
operator|(
name|count
operator|>
literal|1
condition|?
literal|"languages: "
else|:
literal|"language: "
operator|)
operator|+
name|names
argument_list|)
expr_stmt|;
if|if
condition|(
name|projectHelper
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|includes
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|includes
operator|.
name|add
argument_list|(
literal|"**/language.properties"
argument_list|)
expr_stmt|;
name|projectHelper
operator|.
name|addResource
argument_list|(
name|project
argument_list|,
name|languageOutDir
operator|.
name|getPath
argument_list|()
argument_list|,
name|includes
argument_list|,
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
name|projectHelper
operator|.
name|attachArtifact
argument_list|(
name|project
argument_list|,
literal|"properties"
argument_list|,
literal|"camelLanguage"
argument_list|,
name|outFile
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
literal|"Failed to write properties to "
operator|+
name|outFile
operator|+
literal|". Reason: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
else|else
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"No META-INF/services/org/apache/camel/language directory found. Are you sure you have created a Camel language?"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|asModelName (String name)
specifier|private
specifier|static
name|String
name|asModelName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
comment|// special for some languages
if|if
condition|(
literal|"bean"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
literal|"method"
return|;
block|}
elseif|else
if|if
condition|(
literal|"file"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
literal|"simple"
return|;
block|}
return|return
name|name
return|;
block|}
DECL|method|findCamelCoreArtifact (MavenProject project)
specifier|private
specifier|static
name|Artifact
name|findCamelCoreArtifact
parameter_list|(
name|MavenProject
name|project
parameter_list|)
block|{
comment|// maybe this project is camel-core itself
name|Artifact
name|artifact
init|=
name|project
operator|.
name|getArtifact
argument_list|()
decl_stmt|;
if|if
condition|(
name|artifact
operator|.
name|getGroupId
argument_list|()
operator|.
name|equals
argument_list|(
literal|"org.apache.camel"
argument_list|)
operator|&&
name|artifact
operator|.
name|getArtifactId
argument_list|()
operator|.
name|equals
argument_list|(
literal|"camel-core"
argument_list|)
condition|)
block|{
return|return
name|artifact
return|;
block|}
comment|// or its a component which has a dependency to camel-core
name|Iterator
name|it
init|=
name|project
operator|.
name|getDependencyArtifacts
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|artifact
operator|=
operator|(
name|Artifact
operator|)
name|it
operator|.
name|next
argument_list|()
expr_stmt|;
if|if
condition|(
name|artifact
operator|.
name|getGroupId
argument_list|()
operator|.
name|equals
argument_list|(
literal|"org.apache.camel"
argument_list|)
operator|&&
name|artifact
operator|.
name|getArtifactId
argument_list|()
operator|.
name|equals
argument_list|(
literal|"camel-core"
argument_list|)
condition|)
block|{
return|return
name|artifact
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|schemaSubDirectory (String javaType)
specifier|private
specifier|static
name|String
name|schemaSubDirectory
parameter_list|(
name|String
name|javaType
parameter_list|)
block|{
name|int
name|idx
init|=
name|javaType
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
decl_stmt|;
name|String
name|pckName
init|=
name|javaType
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|idx
argument_list|)
decl_stmt|;
return|return
name|pckName
operator|.
name|replace
argument_list|(
literal|'.'
argument_list|,
literal|'/'
argument_list|)
return|;
block|}
DECL|method|createParameterJsonSchema (LanguageModel languageModel, String schema)
specifier|private
specifier|static
name|String
name|createParameterJsonSchema
parameter_list|(
name|LanguageModel
name|languageModel
parameter_list|,
name|String
name|schema
parameter_list|)
block|{
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"{"
argument_list|)
decl_stmt|;
comment|// component model
name|buffer
operator|.
name|append
argument_list|(
literal|"\n \"language\": {"
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|"\n    \"name\": \""
argument_list|)
operator|.
name|append
argument_list|(
name|languageModel
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"\","
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|"\n    \"kind\": \""
argument_list|)
operator|.
name|append
argument_list|(
literal|"language"
argument_list|)
operator|.
name|append
argument_list|(
literal|"\","
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|"\n    \"modelName\": \""
argument_list|)
operator|.
name|append
argument_list|(
name|languageModel
operator|.
name|getModelName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"\","
argument_list|)
expr_stmt|;
if|if
condition|(
name|languageModel
operator|.
name|getTitle
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|"\n    \"title\": \""
argument_list|)
operator|.
name|append
argument_list|(
name|languageModel
operator|.
name|getTitle
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"\","
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|languageModel
operator|.
name|getDescription
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|"\n    \"description\": \""
argument_list|)
operator|.
name|append
argument_list|(
name|languageModel
operator|.
name|getDescription
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"\","
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
literal|"\n    \"label\": \""
argument_list|)
operator|.
name|append
argument_list|(
name|languageModel
operator|.
name|getLabel
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"\","
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|"\n    \"javaType\": \""
argument_list|)
operator|.
name|append
argument_list|(
name|languageModel
operator|.
name|getJavaType
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"\","
argument_list|)
expr_stmt|;
if|if
condition|(
name|languageModel
operator|.
name|getModelJavaType
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|"\n    \"modelJavaType\": \""
argument_list|)
operator|.
name|append
argument_list|(
name|languageModel
operator|.
name|getModelJavaType
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"\","
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
literal|"\n    \"groupId\": \""
argument_list|)
operator|.
name|append
argument_list|(
name|languageModel
operator|.
name|getGroupId
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"\","
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|"\n    \"artifactId\": \""
argument_list|)
operator|.
name|append
argument_list|(
name|languageModel
operator|.
name|getArtifactId
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"\","
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|"\n    \"version\": \""
argument_list|)
operator|.
name|append
argument_list|(
name|languageModel
operator|.
name|getVersion
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"\""
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|"\n  },"
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|"\n  \"properties\": {"
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|schema
argument_list|)
expr_stmt|;
return|return
name|buffer
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|class|LanguageModel
specifier|private
specifier|static
class|class
name|LanguageModel
block|{
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
DECL|field|title
specifier|private
name|String
name|title
decl_stmt|;
DECL|field|modelName
specifier|private
name|String
name|modelName
decl_stmt|;
DECL|field|description
specifier|private
name|String
name|description
decl_stmt|;
DECL|field|label
specifier|private
name|String
name|label
decl_stmt|;
DECL|field|javaType
specifier|private
name|String
name|javaType
decl_stmt|;
DECL|field|modelJavaType
specifier|private
name|String
name|modelJavaType
decl_stmt|;
DECL|field|groupId
specifier|private
name|String
name|groupId
decl_stmt|;
DECL|field|artifactId
specifier|private
name|String
name|artifactId
decl_stmt|;
DECL|field|version
specifier|private
name|String
name|version
decl_stmt|;
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
DECL|method|setName (String name)
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
DECL|method|getTitle ()
specifier|public
name|String
name|getTitle
parameter_list|()
block|{
return|return
name|title
return|;
block|}
DECL|method|setTitle (String title)
specifier|public
name|void
name|setTitle
parameter_list|(
name|String
name|title
parameter_list|)
block|{
name|this
operator|.
name|title
operator|=
name|title
expr_stmt|;
block|}
DECL|method|getModelName ()
specifier|public
name|String
name|getModelName
parameter_list|()
block|{
return|return
name|modelName
return|;
block|}
DECL|method|setModelName (String modelName)
specifier|public
name|void
name|setModelName
parameter_list|(
name|String
name|modelName
parameter_list|)
block|{
name|this
operator|.
name|modelName
operator|=
name|modelName
expr_stmt|;
block|}
DECL|method|getModelJavaType ()
specifier|public
name|String
name|getModelJavaType
parameter_list|()
block|{
return|return
name|modelJavaType
return|;
block|}
DECL|method|setModelJavaType (String modelJavaType)
specifier|public
name|void
name|setModelJavaType
parameter_list|(
name|String
name|modelJavaType
parameter_list|)
block|{
name|this
operator|.
name|modelJavaType
operator|=
name|modelJavaType
expr_stmt|;
block|}
DECL|method|getDescription ()
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|description
return|;
block|}
DECL|method|setDescription (String description)
specifier|public
name|void
name|setDescription
parameter_list|(
name|String
name|description
parameter_list|)
block|{
name|this
operator|.
name|description
operator|=
name|description
expr_stmt|;
block|}
DECL|method|getLabel ()
specifier|public
name|String
name|getLabel
parameter_list|()
block|{
return|return
name|label
return|;
block|}
DECL|method|setLabel (String label)
specifier|public
name|void
name|setLabel
parameter_list|(
name|String
name|label
parameter_list|)
block|{
name|this
operator|.
name|label
operator|=
name|label
expr_stmt|;
block|}
DECL|method|getJavaType ()
specifier|public
name|String
name|getJavaType
parameter_list|()
block|{
return|return
name|javaType
return|;
block|}
DECL|method|setJavaType (String javaType)
specifier|public
name|void
name|setJavaType
parameter_list|(
name|String
name|javaType
parameter_list|)
block|{
name|this
operator|.
name|javaType
operator|=
name|javaType
expr_stmt|;
block|}
DECL|method|getGroupId ()
specifier|public
name|String
name|getGroupId
parameter_list|()
block|{
return|return
name|groupId
return|;
block|}
DECL|method|setGroupId (String groupId)
specifier|public
name|void
name|setGroupId
parameter_list|(
name|String
name|groupId
parameter_list|)
block|{
name|this
operator|.
name|groupId
operator|=
name|groupId
expr_stmt|;
block|}
DECL|method|getArtifactId ()
specifier|public
name|String
name|getArtifactId
parameter_list|()
block|{
return|return
name|artifactId
return|;
block|}
DECL|method|setArtifactId (String artifactId)
specifier|public
name|void
name|setArtifactId
parameter_list|(
name|String
name|artifactId
parameter_list|)
block|{
name|this
operator|.
name|artifactId
operator|=
name|artifactId
expr_stmt|;
block|}
DECL|method|getVersion ()
specifier|public
name|String
name|getVersion
parameter_list|()
block|{
return|return
name|version
return|;
block|}
DECL|method|setVersion (String version)
specifier|public
name|void
name|setVersion
parameter_list|(
name|String
name|version
parameter_list|)
block|{
name|this
operator|.
name|version
operator|=
name|version
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"LanguageModel["
operator|+
literal|"name='"
operator|+
name|name
operator|+
literal|'\''
operator|+
literal|", modelName='"
operator|+
name|modelName
operator|+
literal|'\''
operator|+
literal|", title='"
operator|+
name|title
operator|+
literal|'\''
operator|+
literal|", description='"
operator|+
name|description
operator|+
literal|'\''
operator|+
literal|", label='"
operator|+
name|label
operator|+
literal|'\''
operator|+
literal|", javaType='"
operator|+
name|javaType
operator|+
literal|'\''
operator|+
literal|", modelJavaType='"
operator|+
name|modelJavaType
operator|+
literal|'\''
operator|+
literal|", groupId='"
operator|+
name|groupId
operator|+
literal|'\''
operator|+
literal|", artifactId='"
operator|+
name|artifactId
operator|+
literal|'\''
operator|+
literal|", version='"
operator|+
name|version
operator|+
literal|'\''
operator|+
literal|']'
return|;
block|}
block|}
block|}
end_class

end_unit

