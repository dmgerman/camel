begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|FileFilter
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
name|FileReader
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
name|Arrays
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
name|Locale
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
name|SpringBootAutoConfigureOptionModel
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
name|SpringBootModel
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
name|json
operator|.
name|DeserializationException
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
name|json
operator|.
name|JsonArray
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
name|json
operator|.
name|JsonObject
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
name|json
operator|.
name|Jsoner
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
name|Component
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
name|project
operator|.
name|MavenProject
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mvel2
operator|.
name|templates
operator|.
name|TemplateRuntime
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
name|writeText
import|;
end_import

begin_comment
comment|/**  * For all the Camel components that has Spring Boot starter JAR, their documentation  * .adoc files in their component directory is updated to include spring boot auto configuration options.  */
end_comment

begin_class
annotation|@
name|Mojo
argument_list|(
name|name
operator|=
literal|"update-spring-boot-auto-configuration-readme"
argument_list|,
name|threadSafe
operator|=
literal|true
argument_list|)
DECL|class|UpdateSpringBootAutoConfigurationReadmeMojo
specifier|public
class|class
name|UpdateSpringBootAutoConfigurationReadmeMojo
extends|extends
name|AbstractMojo
block|{
comment|/**      * The maven project.      */
annotation|@
name|Parameter
argument_list|(
name|property
operator|=
literal|"project"
argument_list|,
name|required
operator|=
literal|true
argument_list|,
name|readonly
operator|=
literal|true
argument_list|)
DECL|field|project
specifier|protected
name|MavenProject
name|project
decl_stmt|;
comment|/**      * The project build directory      *      */
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"${project.build.directory}"
argument_list|)
DECL|field|buildDir
specifier|protected
name|File
name|buildDir
decl_stmt|;
comment|/**      * The documentation directory      *      */
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"${basedir}/../../../../components/"
argument_list|)
DECL|field|componentsDir
specifier|protected
name|File
name|componentsDir
decl_stmt|;
comment|/**      * Whether to fail the build fast if any Warnings was detected.      */
annotation|@
name|Parameter
DECL|field|failFast
specifier|protected
name|Boolean
name|failFast
decl_stmt|;
comment|/**      * Whether to fail if an option has no documentation.      */
annotation|@
name|Parameter
DECL|field|failOnMissingDescription
specifier|protected
name|Boolean
name|failOnMissingDescription
decl_stmt|;
comment|/**      * build context to check changed files and mark them for refresh (used for      * m2e compatibility)      */
annotation|@
name|Component
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
try|try
block|{
name|executeStarter
argument_list|(
name|project
operator|.
name|getBasedir
argument_list|()
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
literal|"Error processing spring-configuration-metadata.json"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|executeStarter (File starter)
specifier|private
name|void
name|executeStarter
parameter_list|(
name|File
name|starter
parameter_list|)
throws|throws
name|Exception
block|{
name|File
name|jsonFile
init|=
operator|new
name|File
argument_list|(
name|buildDir
argument_list|,
literal|"classes/META-INF/spring-configuration-metadata.json"
argument_list|)
decl_stmt|;
comment|// only if there is components we should update the documentation files
if|if
condition|(
name|jsonFile
operator|.
name|exists
argument_list|()
condition|)
block|{
name|getLog
argument_list|()
operator|.
name|debug
argument_list|(
literal|"Processing Spring Boot auto-configuration file: "
operator|+
name|jsonFile
argument_list|)
expr_stmt|;
name|Object
name|js
init|=
name|Jsoner
operator|.
name|deserialize
argument_list|(
operator|new
name|FileReader
argument_list|(
name|jsonFile
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|js
operator|!=
literal|null
condition|)
block|{
name|String
name|name
init|=
name|starter
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|isValidStarter
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return;
block|}
name|File
name|compDir
init|=
name|getComponentsDir
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|File
index|[]
name|docFiles
decl_stmt|;
name|File
name|docFolder
decl_stmt|;
name|String
name|componentName
decl_stmt|;
if|if
condition|(
literal|"camel-spring-boot"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
comment|// special for camel-spring-boot where we also want to auto-generate the options in the adoc file
name|componentName
operator|=
literal|"spring-boot"
expr_stmt|;
name|docFolder
operator|=
operator|new
name|File
argument_list|(
name|compDir
argument_list|,
literal|"/src/main/docs/"
argument_list|)
expr_stmt|;
name|docFiles
operator|=
name|docFolder
operator|.
name|listFiles
argument_list|(
operator|new
name|ComponentDocFilter
argument_list|(
name|componentName
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"camel-univocity-parsers-starter"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
comment|// special for univocity-parsers
name|componentName
operator|=
literal|"univocity"
expr_stmt|;
name|docFolder
operator|=
operator|new
name|File
argument_list|(
name|compDir
argument_list|,
literal|"camel-univocity-parsers/src/main/docs/"
argument_list|)
expr_stmt|;
name|docFiles
operator|=
name|docFolder
operator|.
name|listFiles
argument_list|(
operator|new
name|ComponentDocFilter
argument_list|(
name|componentName
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// skip camel-  and -starter in the end
name|componentName
operator|=
name|name
operator|.
name|substring
argument_list|(
literal|6
argument_list|,
name|name
operator|.
name|length
argument_list|()
operator|-
literal|8
argument_list|)
expr_stmt|;
name|getLog
argument_list|()
operator|.
name|debug
argument_list|(
literal|"Camel component: "
operator|+
name|componentName
argument_list|)
expr_stmt|;
name|docFolder
operator|=
operator|new
name|File
argument_list|(
name|compDir
argument_list|,
literal|"camel-"
operator|+
name|componentName
operator|+
literal|"/src/main/docs/"
argument_list|)
expr_stmt|;
name|docFiles
operator|=
name|docFolder
operator|.
name|listFiles
argument_list|(
operator|new
name|ComponentDocFilter
argument_list|(
name|componentName
argument_list|)
argument_list|)
expr_stmt|;
comment|// maybe its one of those component that has subfolders with -api and -component
if|if
condition|(
name|docFiles
operator|==
literal|null
operator|||
name|docFiles
operator|.
name|length
operator|==
literal|0
condition|)
block|{
name|docFolder
operator|=
operator|new
name|File
argument_list|(
name|compDir
argument_list|,
literal|"camel-"
operator|+
name|componentName
operator|+
literal|"/camel-"
operator|+
name|componentName
operator|+
literal|"-component/src/main/docs/"
argument_list|)
expr_stmt|;
name|docFiles
operator|=
name|docFolder
operator|.
name|listFiles
argument_list|(
operator|new
name|ComponentDocFilter
argument_list|(
name|componentName
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|docFiles
operator|!=
literal|null
operator|&&
name|docFiles
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|List
argument_list|<
name|File
argument_list|>
name|files
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|docFiles
argument_list|)
decl_stmt|;
comment|// find out if the JAR has a Camel component, dataformat, or language
name|boolean
name|hasComponentDataFormatOrLanguage
init|=
name|files
operator|.
name|stream
argument_list|()
operator|.
name|anyMatch
argument_list|(
parameter_list|(
name|f
parameter_list|)
lambda|->
name|f
operator|.
name|getName
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|"-component.adoc"
argument_list|)
operator|||
name|f
operator|.
name|getName
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|"-dataformat.adoc"
argument_list|)
operator|||
name|f
operator|.
name|getName
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|"-language.adoc"
argument_list|)
argument_list|)
decl_stmt|;
comment|// if so then skip the root adoc file as its just a introduction to the others
if|if
condition|(
name|hasComponentDataFormatOrLanguage
condition|)
block|{
name|files
operator|=
name|Arrays
operator|.
name|stream
argument_list|(
name|docFiles
argument_list|)
operator|.
name|filter
argument_list|(
parameter_list|(
name|f
parameter_list|)
lambda|->
operator|!
name|f
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|componentName
operator|+
literal|".adoc"
argument_list|)
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
block|}
if|if
condition|(
name|files
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|List
argument_list|<
name|SpringBootAutoConfigureOptionModel
argument_list|>
name|models
init|=
name|parseSpringBootAutoConfigureModels
argument_list|(
name|jsonFile
argument_list|,
literal|null
argument_list|)
decl_stmt|;
comment|// special for other kind of JARs that is not a regular Camel component,dataformat,language
name|boolean
name|onlyOther
init|=
name|files
operator|.
name|size
argument_list|()
operator|==
literal|1
operator|&&
operator|!
name|hasComponentDataFormatOrLanguage
decl_stmt|;
if|if
condition|(
name|models
operator|.
name|isEmpty
argument_list|()
operator|&&
name|onlyOther
condition|)
block|{
comment|// there are no spring-boot auto configuration for this other kind of JAR so lets just ignore this
return|return;
block|}
name|File
name|docFile
init|=
name|files
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
comment|// check for missing description on options
name|boolean
name|noDescription
init|=
literal|false
decl_stmt|;
for|for
control|(
name|SpringBootAutoConfigureOptionModel
name|o
range|:
name|models
control|)
block|{
if|if
condition|(
name|StringHelper
operator|.
name|isEmpty
argument_list|(
name|o
operator|.
name|getDescription
argument_list|()
argument_list|)
condition|)
block|{
name|noDescription
operator|=
literal|true
expr_stmt|;
name|getLog
argument_list|()
operator|.
name|warn
argument_list|(
literal|"Option "
operator|+
name|o
operator|.
name|getName
argument_list|()
operator|+
literal|" has no description"
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|noDescription
operator|&&
name|isFailOnNoDescription
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
literal|"Failed build due failOnMissingDescription=true"
argument_list|)
throw|;
block|}
name|String
name|changed
init|=
name|templateAutoConfigurationOptions
argument_list|(
name|models
argument_list|,
name|componentName
argument_list|)
decl_stmt|;
name|boolean
name|updated
init|=
name|updateAutoConfigureOptions
argument_list|(
name|docFile
argument_list|,
name|changed
argument_list|)
decl_stmt|;
if|if
condition|(
name|updated
condition|)
block|{
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|"Updated doc file: "
operator|+
name|docFile
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|getLog
argument_list|()
operator|.
name|debug
argument_list|(
literal|"No changes to doc file: "
operator|+
name|docFile
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|files
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
comment|// when we have 2 or more files we need to filter the model options accordingly
for|for
control|(
name|File
name|docFile
range|:
name|files
control|)
block|{
name|String
name|docName
init|=
name|docFile
operator|.
name|getName
argument_list|()
decl_stmt|;
name|int
name|pos
init|=
name|docName
operator|.
name|lastIndexOf
argument_list|(
literal|"-"
argument_list|)
decl_stmt|;
comment|// spring-boot use lower cased keys
name|String
name|prefix
init|=
name|pos
operator|>
literal|0
condition|?
name|docName
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|pos
argument_list|)
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
else|:
literal|null
decl_stmt|;
name|List
argument_list|<
name|SpringBootAutoConfigureOptionModel
argument_list|>
name|models
init|=
name|parseSpringBootAutoConfigureModels
argument_list|(
name|jsonFile
argument_list|,
name|prefix
argument_list|)
decl_stmt|;
comment|// check for missing description on options
name|boolean
name|noDescription
init|=
literal|false
decl_stmt|;
for|for
control|(
name|SpringBootAutoConfigureOptionModel
name|o
range|:
name|models
control|)
block|{
if|if
condition|(
name|StringHelper
operator|.
name|isEmpty
argument_list|(
name|o
operator|.
name|getDescription
argument_list|()
argument_list|)
condition|)
block|{
name|noDescription
operator|=
literal|true
expr_stmt|;
name|getLog
argument_list|()
operator|.
name|warn
argument_list|(
literal|"Option "
operator|+
name|o
operator|.
name|getName
argument_list|()
operator|+
literal|" has no description"
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|noDescription
operator|&&
name|isFailOnNoDescription
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
literal|"Failed build due failOnMissingDescription=true"
argument_list|)
throw|;
block|}
name|String
name|changed
init|=
name|templateAutoConfigurationOptions
argument_list|(
name|models
argument_list|,
name|componentName
argument_list|)
decl_stmt|;
name|boolean
name|updated
init|=
name|updateAutoConfigureOptions
argument_list|(
name|docFile
argument_list|,
name|changed
argument_list|)
decl_stmt|;
if|if
condition|(
name|updated
condition|)
block|{
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|"Updated doc file: "
operator|+
name|docFile
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|getLog
argument_list|()
operator|.
name|debug
argument_list|(
literal|"No changes to doc file: "
operator|+
name|docFile
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
else|else
block|{
name|getLog
argument_list|()
operator|.
name|warn
argument_list|(
literal|"No component docs found in folder: "
operator|+
name|docFolder
argument_list|)
expr_stmt|;
if|if
condition|(
name|isFailFast
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
literal|"Failed build due failFast=true"
argument_list|)
throw|;
block|}
block|}
block|}
block|}
block|}
DECL|method|getComponentsDir (String name)
specifier|private
name|File
name|getComponentsDir
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
literal|"camel-spring-boot"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
comment|// special for camel-spring-boot
return|return
name|project
operator|.
name|getBasedir
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|componentsDir
return|;
block|}
block|}
DECL|class|ComponentDocFilter
specifier|private
specifier|static
specifier|final
class|class
name|ComponentDocFilter
implements|implements
name|FileFilter
block|{
DECL|field|componentName
specifier|private
specifier|final
name|String
name|componentName
decl_stmt|;
DECL|method|ComponentDocFilter (String componentName)
specifier|public
name|ComponentDocFilter
parameter_list|(
name|String
name|componentName
parameter_list|)
block|{
name|this
operator|.
name|componentName
operator|=
name|asComponentName
argument_list|(
name|componentName
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|accept (File pathname)
specifier|public
name|boolean
name|accept
parameter_list|(
name|File
name|pathname
parameter_list|)
block|{
name|String
name|name
init|=
name|pathname
operator|.
name|getName
argument_list|()
decl_stmt|;
return|return
name|name
operator|.
name|startsWith
argument_list|(
name|componentName
argument_list|)
operator|&&
name|name
operator|.
name|endsWith
argument_list|(
literal|".adoc"
argument_list|)
return|;
block|}
block|}
DECL|method|asComponentName (String componentName)
specifier|private
specifier|static
name|String
name|asComponentName
parameter_list|(
name|String
name|componentName
parameter_list|)
block|{
if|if
condition|(
literal|"fastjson"
operator|.
name|equals
argument_list|(
name|componentName
argument_list|)
condition|)
block|{
return|return
literal|"json-fastjson"
return|;
block|}
elseif|else
if|if
condition|(
literal|"gson"
operator|.
name|equals
argument_list|(
name|componentName
argument_list|)
condition|)
block|{
return|return
literal|"json-gson"
return|;
block|}
elseif|else
if|if
condition|(
literal|"jackson"
operator|.
name|equals
argument_list|(
name|componentName
argument_list|)
condition|)
block|{
return|return
literal|"json-jackson"
return|;
block|}
elseif|else
if|if
condition|(
literal|"johnzon"
operator|.
name|equals
argument_list|(
name|componentName
argument_list|)
condition|)
block|{
return|return
literal|"json-johnzon"
return|;
block|}
elseif|else
if|if
condition|(
literal|"snakeyaml"
operator|.
name|equals
argument_list|(
name|componentName
argument_list|)
condition|)
block|{
return|return
literal|"yaml-snakeyaml"
return|;
block|}
elseif|else
if|if
condition|(
literal|"cassandraql"
operator|.
name|equals
argument_list|(
name|componentName
argument_list|)
condition|)
block|{
return|return
literal|"cql"
return|;
block|}
elseif|else
if|if
condition|(
literal|"josql"
operator|.
name|equals
argument_list|(
name|componentName
argument_list|)
condition|)
block|{
return|return
literal|"sql"
return|;
block|}
elseif|else
if|if
condition|(
literal|"juel"
operator|.
name|equals
argument_list|(
name|componentName
argument_list|)
condition|)
block|{
return|return
literal|"el"
return|;
block|}
elseif|else
if|if
condition|(
literal|"jsch"
operator|.
name|equals
argument_list|(
name|componentName
argument_list|)
condition|)
block|{
return|return
literal|"scp"
return|;
block|}
elseif|else
if|if
condition|(
literal|"printer"
operator|.
name|equals
argument_list|(
name|componentName
argument_list|)
condition|)
block|{
return|return
literal|"lpr"
return|;
block|}
elseif|else
if|if
condition|(
literal|"saxon"
operator|.
name|equals
argument_list|(
name|componentName
argument_list|)
condition|)
block|{
return|return
literal|"xquery"
return|;
block|}
elseif|else
if|if
condition|(
literal|"stringtemplate"
operator|.
name|equals
argument_list|(
name|componentName
argument_list|)
condition|)
block|{
return|return
literal|"string-template"
return|;
block|}
elseif|else
if|if
condition|(
literal|"tagsoup"
operator|.
name|equals
argument_list|(
name|componentName
argument_list|)
condition|)
block|{
return|return
literal|"tidyMarkup"
return|;
block|}
return|return
name|componentName
return|;
block|}
DECL|method|isValidStarter (String name)
specifier|private
specifier|static
name|boolean
name|isValidStarter
parameter_list|(
name|String
name|name
parameter_list|)
block|{
comment|// skip these
if|if
condition|(
literal|"camel-core-starter"
operator|.
name|equals
argument_list|(
name|name
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
DECL|method|parseSpringBootAutoConfigureModels (File file, String include)
specifier|private
name|List
argument_list|<
name|SpringBootAutoConfigureOptionModel
argument_list|>
name|parseSpringBootAutoConfigureModels
parameter_list|(
name|File
name|file
parameter_list|,
name|String
name|include
parameter_list|)
throws|throws
name|IOException
throws|,
name|DeserializationException
block|{
name|getLog
argument_list|()
operator|.
name|debug
argument_list|(
literal|"Parsing Spring Boot AutoConfigureModel using include: "
operator|+
name|include
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|SpringBootAutoConfigureOptionModel
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|JsonObject
name|obj
init|=
operator|(
name|JsonObject
operator|)
name|Jsoner
operator|.
name|deserialize
argument_list|(
operator|new
name|FileReader
argument_list|(
name|file
argument_list|)
argument_list|)
decl_stmt|;
name|JsonArray
name|arr
init|=
name|obj
operator|.
name|getCollection
argument_list|(
literal|"properties"
argument_list|)
decl_stmt|;
if|if
condition|(
name|arr
operator|!=
literal|null
operator|&&
operator|!
name|arr
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|arr
operator|.
name|forEach
argument_list|(
parameter_list|(
name|e
parameter_list|)
lambda|->
block|{
name|JsonObject
name|row
init|=
operator|(
name|JsonObject
operator|)
name|e
decl_stmt|;
name|String
name|name
init|=
name|row
operator|.
name|getString
argument_list|(
literal|"name"
argument_list|)
decl_stmt|;
name|String
name|javaType
init|=
name|row
operator|.
name|getString
argument_list|(
literal|"type"
argument_list|)
decl_stmt|;
name|String
name|desc
init|=
name|row
operator|.
name|getStringOrDefault
argument_list|(
literal|"description"
argument_list|,
literal|""
argument_list|)
decl_stmt|;
name|String
name|defaultValue
init|=
name|row
operator|.
name|getStringOrDefault
argument_list|(
literal|"defaultValue"
argument_list|,
literal|""
argument_list|)
decl_stmt|;
comment|// skip this special option and also if not matching the filter
name|boolean
name|skip
init|=
name|name
operator|.
name|endsWith
argument_list|(
literal|"customizer.enabled"
argument_list|)
operator|||
name|include
operator|!=
literal|null
operator|&&
operator|!
name|name
operator|.
name|contains
argument_list|(
literal|"."
operator|+
name|include
operator|+
literal|"."
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|skip
condition|)
block|{
name|SpringBootAutoConfigureOptionModel
name|model
init|=
operator|new
name|SpringBootAutoConfigureOptionModel
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
block|}
return|return
name|answer
return|;
block|}
DECL|method|updateAutoConfigureOptions (File file, String changed)
specifier|private
name|boolean
name|updateAutoConfigureOptions
parameter_list|(
name|File
name|file
parameter_list|,
name|String
name|changed
parameter_list|)
throws|throws
name|MojoExecutionException
block|{
if|if
condition|(
operator|!
name|file
operator|.
name|exists
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
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
name|String
name|existing
init|=
name|StringHelper
operator|.
name|between
argument_list|(
name|text
argument_list|,
literal|"// spring-boot-auto-configure options: START"
argument_list|,
literal|"// spring-boot-auto-configure options: END"
argument_list|)
decl_stmt|;
if|if
condition|(
name|existing
operator|!=
literal|null
condition|)
block|{
comment|// remove leading line breaks etc
name|existing
operator|=
name|existing
operator|.
name|trim
argument_list|()
expr_stmt|;
name|changed
operator|=
name|changed
operator|.
name|trim
argument_list|()
expr_stmt|;
if|if
condition|(
name|existing
operator|.
name|equals
argument_list|(
name|changed
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
else|else
block|{
name|String
name|before
init|=
name|StringHelper
operator|.
name|before
argument_list|(
name|text
argument_list|,
literal|"// spring-boot-auto-configure options: START"
argument_list|)
decl_stmt|;
name|String
name|after
init|=
name|StringHelper
operator|.
name|after
argument_list|(
name|text
argument_list|,
literal|"// spring-boot-auto-configure options: END"
argument_list|)
decl_stmt|;
name|text
operator|=
name|before
operator|+
literal|"// spring-boot-auto-configure options: START\n"
operator|+
name|changed
operator|+
literal|"\n// spring-boot-auto-configure options: END"
operator|+
name|after
expr_stmt|;
name|writeText
argument_list|(
name|file
argument_list|,
name|text
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
else|else
block|{
name|getLog
argument_list|()
operator|.
name|warn
argument_list|(
literal|"Cannot find markers in file "
operator|+
name|file
argument_list|)
expr_stmt|;
name|getLog
argument_list|()
operator|.
name|warn
argument_list|(
literal|"Add the following markers"
argument_list|)
expr_stmt|;
name|getLog
argument_list|()
operator|.
name|warn
argument_list|(
literal|"\t// spring-boot-auto-configure options: START"
argument_list|)
expr_stmt|;
name|getLog
argument_list|()
operator|.
name|warn
argument_list|(
literal|"\t// spring-boot-auto-configure options: END"
argument_list|)
expr_stmt|;
if|if
condition|(
name|isFailFast
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
literal|"Failed build due failFast=true"
argument_list|)
throw|;
block|}
return|return
literal|false
return|;
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
literal|"Error reading file "
operator|+
name|file
operator|+
literal|" Reason: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|templateAutoConfigurationOptions (List<SpringBootAutoConfigureOptionModel> options, String componentName)
specifier|private
name|String
name|templateAutoConfigurationOptions
parameter_list|(
name|List
argument_list|<
name|SpringBootAutoConfigureOptionModel
argument_list|>
name|options
parameter_list|,
name|String
name|componentName
parameter_list|)
throws|throws
name|MojoExecutionException
block|{
name|SpringBootModel
name|model
init|=
operator|new
name|SpringBootModel
argument_list|()
decl_stmt|;
name|model
operator|.
name|setGroupId
argument_list|(
name|project
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|model
operator|.
name|setArtifactId
argument_list|(
literal|"camel-"
operator|+
name|componentName
operator|+
literal|"-starter"
argument_list|)
expr_stmt|;
name|model
operator|.
name|setVersion
argument_list|(
name|project
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|model
operator|.
name|setOptions
argument_list|(
name|options
argument_list|)
expr_stmt|;
try|try
block|{
name|String
name|template
init|=
name|loadText
argument_list|(
name|UpdateSpringBootAutoConfigurationReadmeMojo
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"spring-boot-auto-configure-options.mvel"
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|out
init|=
operator|(
name|String
operator|)
name|TemplateRuntime
operator|.
name|eval
argument_list|(
name|template
argument_list|,
name|model
argument_list|)
decl_stmt|;
return|return
name|out
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
literal|"Error processing mvel template. Reason: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|isFailFast ()
specifier|private
name|boolean
name|isFailFast
parameter_list|()
block|{
return|return
name|failFast
operator|!=
literal|null
operator|&&
name|failFast
return|;
block|}
DECL|method|isFailOnNoDescription ()
specifier|private
name|boolean
name|isFailOnNoDescription
parameter_list|()
block|{
return|return
name|failOnMissingDescription
operator|!=
literal|null
operator|&&
name|failOnMissingDescription
return|;
block|}
block|}
end_class

end_unit

