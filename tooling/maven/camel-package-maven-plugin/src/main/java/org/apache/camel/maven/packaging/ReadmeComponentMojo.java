begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *<p/>  * http://www.apache.org/licenses/LICENSE-2.0  *<p/>  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|FilenameFilter
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
name|StringWriter
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
name|java
operator|.
name|util
operator|.
name|TreeSet
import|;
end_import

begin_import
import|import
name|freemarker
operator|.
name|template
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|freemarker
operator|.
name|template
operator|.
name|Template
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
name|maven
operator|.
name|packaging
operator|.
name|model
operator|.
name|ComponentModel
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
name|maven
operator|.
name|packaging
operator|.
name|model
operator|.
name|ComponentOptionModel
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
name|project
operator|.
name|MavenProject
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
name|JSonSchemaHelper
operator|.
name|getValue
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

begin_comment
comment|/**  * Generate or updates the component readme.md file in the project root directort.  *  * @goal update-readme  */
end_comment

begin_class
DECL|class|ReadmeComponentMojo
specifier|public
class|class
name|ReadmeComponentMojo
extends|extends
name|AbstractMojo
block|{
comment|/**      * The maven project.      *      * @parameter property="project"      * @required      * @readonly      */
DECL|field|project
specifier|protected
name|MavenProject
name|project
decl_stmt|;
comment|/**      * The output directory for generated readme file      *      * @parameter default-value="${project.build.directory}"      */
DECL|field|buildDir
specifier|protected
name|File
name|buildDir
decl_stmt|;
comment|/**      * build context to check changed files and mark them for refresh (used for      * m2e compatibility)      *      * @component      * @readonly      */
DECL|field|buildContext
specifier|private
name|BuildContext
name|buildContext
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
comment|// find the component names
name|List
argument_list|<
name|String
argument_list|>
name|componentNames
init|=
name|findComponentNames
argument_list|()
decl_stmt|;
specifier|final
name|Set
argument_list|<
name|File
argument_list|>
name|jsonFiles
init|=
operator|new
name|TreeSet
argument_list|<
name|File
argument_list|>
argument_list|()
decl_stmt|;
name|PackageHelper
operator|.
name|findJsonFiles
argument_list|(
name|buildDir
argument_list|,
name|jsonFiles
argument_list|,
operator|new
name|PackageHelper
operator|.
name|CamelComponentsModelFilter
argument_list|()
argument_list|)
expr_stmt|;
comment|// only if there is components we should create/update the readme file
if|if
condition|(
operator|!
name|componentNames
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|"Found "
operator|+
name|componentNames
operator|.
name|size
argument_list|()
operator|+
literal|" components"
argument_list|)
expr_stmt|;
name|File
name|readmeFile
init|=
name|initReadMeFile
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|componentName
range|:
name|componentNames
control|)
block|{
name|String
name|json
init|=
name|loadComponentJson
argument_list|(
name|jsonFiles
argument_list|,
name|componentName
argument_list|)
decl_stmt|;
if|if
condition|(
name|json
operator|!=
literal|null
condition|)
block|{
name|ComponentModel
name|model
init|=
name|generateComponentModel
argument_list|(
name|componentName
argument_list|,
name|json
argument_list|)
decl_stmt|;
name|String
name|component
init|=
name|templateComponent
argument_list|(
name|model
argument_list|)
decl_stmt|;
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
name|component
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|loadComponentJson (Set<File> jsonFiles, String componentName)
specifier|private
name|String
name|loadComponentJson
parameter_list|(
name|Set
argument_list|<
name|File
argument_list|>
name|jsonFiles
parameter_list|,
name|String
name|componentName
parameter_list|)
block|{
try|try
block|{
for|for
control|(
name|File
name|file
range|:
name|jsonFiles
control|)
block|{
if|if
condition|(
name|file
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|componentName
operator|+
literal|".json"
argument_list|)
condition|)
block|{
name|String
name|json
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
name|boolean
name|isComponent
init|=
name|json
operator|.
name|contains
argument_list|(
literal|"\"kind\": \"component\""
argument_list|)
decl_stmt|;
if|if
condition|(
name|isComponent
condition|)
block|{
return|return
name|json
return|;
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
return|return
literal|null
return|;
block|}
DECL|method|generateComponentModel (String componentName, String json)
specifier|private
name|ComponentModel
name|generateComponentModel
parameter_list|(
name|String
name|componentName
parameter_list|,
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
name|rows
init|=
name|JSonSchemaHelper
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
name|ComponentModel
name|component
init|=
operator|new
name|ComponentModel
argument_list|()
decl_stmt|;
name|component
operator|.
name|setScheme
argument_list|(
name|getValue
argument_list|(
literal|"scheme"
argument_list|,
name|rows
argument_list|)
argument_list|)
expr_stmt|;
name|component
operator|.
name|setSyntax
argument_list|(
name|getValue
argument_list|(
literal|"syntax"
argument_list|,
name|rows
argument_list|)
argument_list|)
expr_stmt|;
name|component
operator|.
name|setTitle
argument_list|(
name|getValue
argument_list|(
literal|"title"
argument_list|,
name|rows
argument_list|)
argument_list|)
expr_stmt|;
name|component
operator|.
name|setDescription
argument_list|(
name|getValue
argument_list|(
literal|"description"
argument_list|,
name|rows
argument_list|)
argument_list|)
expr_stmt|;
name|component
operator|.
name|setLabel
argument_list|(
name|getValue
argument_list|(
literal|"label"
argument_list|,
name|rows
argument_list|)
argument_list|)
expr_stmt|;
name|component
operator|.
name|setDeprecated
argument_list|(
name|getValue
argument_list|(
literal|"deprecated"
argument_list|,
name|rows
argument_list|)
argument_list|)
expr_stmt|;
name|component
operator|.
name|setConsumerOnly
argument_list|(
name|getValue
argument_list|(
literal|"consumerOnly"
argument_list|,
name|rows
argument_list|)
argument_list|)
expr_stmt|;
name|component
operator|.
name|setProducerOnly
argument_list|(
name|getValue
argument_list|(
literal|"producerOnly"
argument_list|,
name|rows
argument_list|)
argument_list|)
expr_stmt|;
name|component
operator|.
name|setJavaType
argument_list|(
name|getValue
argument_list|(
literal|"javaType"
argument_list|,
name|rows
argument_list|)
argument_list|)
expr_stmt|;
name|component
operator|.
name|setGroupId
argument_list|(
name|getValue
argument_list|(
literal|"groupId"
argument_list|,
name|rows
argument_list|)
argument_list|)
expr_stmt|;
name|component
operator|.
name|setArtifactId
argument_list|(
name|getValue
argument_list|(
literal|"artifactId"
argument_list|,
name|rows
argument_list|)
argument_list|)
expr_stmt|;
name|component
operator|.
name|setVersion
argument_list|(
name|getValue
argument_list|(
literal|"version"
argument_list|,
name|rows
argument_list|)
argument_list|)
expr_stmt|;
name|rows
operator|=
name|JSonSchemaHelper
operator|.
name|parseJsonSchema
argument_list|(
literal|"componentProperties"
argument_list|,
name|json
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ComponentOptionModel
argument_list|>
name|options
init|=
operator|new
name|ArrayList
argument_list|<
name|ComponentOptionModel
argument_list|>
argument_list|()
decl_stmt|;
name|ComponentOptionModel
name|option
init|=
operator|new
name|ComponentOptionModel
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
name|row
range|:
name|rows
control|)
block|{
name|option
operator|.
name|setKey
argument_list|(
name|getValue
argument_list|(
literal|"key"
argument_list|,
name|row
argument_list|)
argument_list|)
expr_stmt|;
name|option
operator|.
name|setKind
argument_list|(
name|getValue
argument_list|(
literal|"kind"
argument_list|,
name|row
argument_list|)
argument_list|)
expr_stmt|;
name|option
operator|.
name|setType
argument_list|(
name|getValue
argument_list|(
literal|"type"
argument_list|,
name|row
argument_list|)
argument_list|)
expr_stmt|;
name|option
operator|.
name|setJavaType
argument_list|(
name|getValue
argument_list|(
literal|"javaType"
argument_list|,
name|row
argument_list|)
argument_list|)
expr_stmt|;
name|option
operator|.
name|setDeprecated
argument_list|(
name|getValue
argument_list|(
literal|"javaType"
argument_list|,
name|row
argument_list|)
argument_list|)
expr_stmt|;
name|option
operator|.
name|setDescription
argument_list|(
name|getValue
argument_list|(
literal|"description"
argument_list|,
name|row
argument_list|)
argument_list|)
expr_stmt|;
name|options
operator|.
name|add
argument_list|(
name|option
argument_list|)
expr_stmt|;
block|}
name|component
operator|.
name|setOptions
argument_list|(
name|options
argument_list|)
expr_stmt|;
return|return
name|component
return|;
block|}
DECL|method|templateComponent (ComponentModel model)
specifier|private
name|String
name|templateComponent
parameter_list|(
name|ComponentModel
name|model
parameter_list|)
throws|throws
name|MojoExecutionException
block|{
try|try
block|{
name|String
name|ftl
init|=
name|loadText
argument_list|(
name|ReadmeComponentMojo
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"component-header.ftl"
argument_list|)
argument_list|)
decl_stmt|;
name|Template
name|template
init|=
operator|new
name|Template
argument_list|(
literal|"header"
argument_list|,
name|ftl
argument_list|,
operator|new
name|Configuration
argument_list|()
argument_list|)
decl_stmt|;
name|StringWriter
name|buffer
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|template
operator|.
name|process
argument_list|(
name|model
argument_list|,
name|buffer
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|flush
argument_list|()
expr_stmt|;
return|return
name|buffer
operator|.
name|toString
argument_list|()
return|;
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
literal|"Error processing freemarker template. Readon: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|findComponentNames ()
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|findComponentNames
parameter_list|()
block|{
name|List
argument_list|<
name|String
argument_list|>
name|componentNames
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
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
literal|"META-INF/services/org/apache/camel/component"
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
comment|// skip directories as there may be a sub .resolver directory
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
name|componentNames
operator|.
name|add
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
return|return
name|componentNames
return|;
block|}
DECL|method|initReadMeFile ()
specifier|private
name|File
name|initReadMeFile
parameter_list|()
throws|throws
name|MojoExecutionException
block|{
name|File
name|readmeDir
init|=
operator|new
name|File
argument_list|(
name|buildDir
argument_list|,
literal|".."
argument_list|)
decl_stmt|;
name|File
name|readmeFile
init|=
operator|new
name|File
argument_list|(
name|readmeDir
argument_list|,
literal|"readme.md"
argument_list|)
decl_stmt|;
comment|// see if a file with name readme.md exists in any kind of case
name|String
index|[]
name|names
init|=
name|readmeDir
operator|.
name|list
argument_list|(
operator|new
name|FilenameFilter
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|accept
parameter_list|(
name|File
name|dir
parameter_list|,
name|String
name|name
parameter_list|)
block|{
return|return
literal|"readme.md"
operator|.
name|equalsIgnoreCase
argument_list|(
name|name
argument_list|)
return|;
block|}
block|}
argument_list|)
decl_stmt|;
if|if
condition|(
name|names
operator|!=
literal|null
operator|&&
name|names
operator|.
name|length
operator|==
literal|1
condition|)
block|{
name|readmeFile
operator|=
operator|new
name|File
argument_list|(
name|readmeDir
argument_list|,
name|names
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
name|boolean
name|exists
init|=
name|readmeFile
operator|.
name|exists
argument_list|()
decl_stmt|;
if|if
condition|(
name|exists
condition|)
block|{
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|"Using existing "
operator|+
name|readmeFile
operator|.
name|getName
argument_list|()
operator|+
literal|" file"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|"Creating new readme.md file"
argument_list|)
expr_stmt|;
block|}
return|return
name|readmeFile
return|;
block|}
block|}
end_class

end_unit

