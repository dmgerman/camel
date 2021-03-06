begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.maven.generator.swagger
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|maven
operator|.
name|generator
operator|.
name|swagger
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
name|MalformedURLException
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
name|Optional
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
name|generator
operator|.
name|swagger
operator|.
name|DestinationGenerator
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
name|IOHelper
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
name|ObjectHelper
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
name|execution
operator|.
name|MavenSession
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
name|Dependency
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
name|BuildPluginManager
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
name|twdata
operator|.
name|maven
operator|.
name|mojoexecutor
operator|.
name|MojoExecutor
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|twdata
operator|.
name|maven
operator|.
name|mojoexecutor
operator|.
name|MojoExecutor
operator|.
name|artifactId
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|twdata
operator|.
name|maven
operator|.
name|mojoexecutor
operator|.
name|MojoExecutor
operator|.
name|configuration
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|twdata
operator|.
name|maven
operator|.
name|mojoexecutor
operator|.
name|MojoExecutor
operator|.
name|executeMojo
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|twdata
operator|.
name|maven
operator|.
name|mojoexecutor
operator|.
name|MojoExecutor
operator|.
name|executionEnvironment
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|twdata
operator|.
name|maven
operator|.
name|mojoexecutor
operator|.
name|MojoExecutor
operator|.
name|goal
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|twdata
operator|.
name|maven
operator|.
name|mojoexecutor
operator|.
name|MojoExecutor
operator|.
name|groupId
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|twdata
operator|.
name|maven
operator|.
name|mojoexecutor
operator|.
name|MojoExecutor
operator|.
name|plugin
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|twdata
operator|.
name|maven
operator|.
name|mojoexecutor
operator|.
name|MojoExecutor
operator|.
name|version
import|;
end_import

begin_class
DECL|class|AbstractGenerateMojo
specifier|abstract
class|class
name|AbstractGenerateMojo
extends|extends
name|AbstractMojo
block|{
comment|// this list should be in priority order
DECL|field|DEFAULT_REST_CONSUMER_COMPONENTS
specifier|public
specifier|static
specifier|final
name|String
index|[]
name|DEFAULT_REST_CONSUMER_COMPONENTS
init|=
operator|new
name|String
index|[]
block|{
literal|"servlet"
block|,
literal|"undertow"
block|,
literal|"jetty"
block|,
literal|"netty-http"
block|,
literal|"spark-java"
block|,
literal|"coap"
block|}
decl_stmt|;
annotation|@
name|Parameter
DECL|field|apiContextPath
name|String
name|apiContextPath
decl_stmt|;
annotation|@
name|Parameter
DECL|field|destinationGenerator
name|String
name|destinationGenerator
decl_stmt|;
annotation|@
name|Parameter
DECL|field|filterOperation
name|String
name|filterOperation
decl_stmt|;
annotation|@
name|Parameter
DECL|field|modelNamePrefix
name|String
name|modelNamePrefix
decl_stmt|;
annotation|@
name|Parameter
DECL|field|modelNameSuffix
name|String
name|modelNameSuffix
decl_stmt|;
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"${project.build.directory}/generated-sources/swagger"
argument_list|)
DECL|field|modelOutput
name|String
name|modelOutput
decl_stmt|;
annotation|@
name|Parameter
DECL|field|modelPackage
name|String
name|modelPackage
decl_stmt|;
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|modelWithXml
name|String
name|modelWithXml
decl_stmt|;
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"${project}"
argument_list|)
DECL|field|project
name|MavenProject
name|project
decl_stmt|;
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|restConfiguration
name|boolean
name|restConfiguration
decl_stmt|;
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"false"
argument_list|)
DECL|field|skip
name|boolean
name|skip
decl_stmt|;
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"${project.basedir}/src/spec/swagger.json"
argument_list|,
name|required
operator|=
literal|true
argument_list|)
DECL|field|specificationUri
name|String
name|specificationUri
decl_stmt|;
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"2.3.1"
argument_list|)
DECL|field|swaggerCodegenMavenPluginVersion
name|String
name|swaggerCodegenMavenPluginVersion
decl_stmt|;
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"${project}"
argument_list|,
name|readonly
operator|=
literal|true
argument_list|)
DECL|field|mavenProject
specifier|private
name|MavenProject
name|mavenProject
decl_stmt|;
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"${session}"
argument_list|,
name|readonly
operator|=
literal|true
argument_list|)
DECL|field|mavenSession
specifier|private
name|MavenSession
name|mavenSession
decl_stmt|;
annotation|@
name|Component
DECL|field|pluginManager
specifier|private
name|BuildPluginManager
name|pluginManager
decl_stmt|;
DECL|method|createDestinationGenerator ()
name|DestinationGenerator
name|createDestinationGenerator
parameter_list|()
throws|throws
name|MojoExecutionException
block|{
specifier|final
name|Class
argument_list|<
name|DestinationGenerator
argument_list|>
name|destinationGeneratorClass
decl_stmt|;
specifier|final
name|ClassLoader
name|contextClassLoader
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
decl_stmt|;
specifier|final
name|URL
name|outputDirectory
decl_stmt|;
try|try
block|{
name|outputDirectory
operator|=
operator|new
name|File
argument_list|(
name|project
operator|.
name|getBuild
argument_list|()
operator|.
name|getOutputDirectory
argument_list|()
argument_list|)
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
specifier|final
name|MalformedURLException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
name|e
argument_list|)
throw|;
block|}
specifier|final
name|URL
index|[]
name|withOutput
init|=
operator|new
name|URL
index|[]
block|{
name|outputDirectory
block|}
decl_stmt|;
try|try
init|(
name|URLClassLoader
name|classLoader
init|=
operator|new
name|URLClassLoader
argument_list|(
name|withOutput
argument_list|,
name|contextClassLoader
argument_list|)
init|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unchecked"
block|,
literal|"rawtypes"
block|}
argument_list|)
specifier|final
name|Class
argument_list|<
name|DestinationGenerator
argument_list|>
name|tmp
init|=
operator|(
name|Class
operator|)
name|classLoader
operator|.
name|loadClass
argument_list|(
name|destinationGenerator
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|DestinationGenerator
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|tmp
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
literal|"The given destinationGenerator class ("
operator|+
name|destinationGenerator
operator|+
literal|") does not implement "
operator|+
name|DestinationGenerator
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|" interface."
argument_list|)
throw|;
block|}
name|destinationGeneratorClass
operator|=
name|tmp
expr_stmt|;
block|}
catch|catch
parameter_list|(
specifier|final
name|ClassNotFoundException
decl||
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
literal|"The given destinationGenerator class ("
operator|+
name|destinationGenerator
operator|+
literal|") cannot be loaded, make sure that it is present in the COMPILE classpath scope of the project"
argument_list|,
name|e
argument_list|)
throw|;
block|}
specifier|final
name|DestinationGenerator
name|destinationGeneratorObject
decl_stmt|;
try|try
block|{
name|destinationGeneratorObject
operator|=
name|destinationGeneratorClass
operator|.
name|newInstance
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InstantiationException
decl||
name|IllegalAccessException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
literal|"The given destinationGenerator class ("
operator|+
name|destinationGenerator
operator|+
literal|") cannot be instantiated, make sure that it is declared as public and that all dependencies are present on the COMPILE classpath scope of the project"
argument_list|,
name|e
argument_list|)
throw|;
block|}
return|return
name|destinationGeneratorObject
return|;
block|}
DECL|method|generateDto (final String language)
name|void
name|generateDto
parameter_list|(
specifier|final
name|String
name|language
parameter_list|)
throws|throws
name|MojoExecutionException
block|{
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|"Generating DTO classes using io.swagger:swagger-codegen-maven-plugin:"
operator|+
name|swaggerCodegenMavenPluginVersion
argument_list|)
expr_stmt|;
comment|// swagger-codegen-maven-plugin documentation and its supported options
comment|// https://github.com/swagger-api/swagger-codegen/tree/master/modules/swagger-codegen-maven-plugin
specifier|final
name|List
argument_list|<
name|MojoExecutor
operator|.
name|Element
argument_list|>
name|elements
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|elements
operator|.
name|add
argument_list|(
operator|new
name|MojoExecutor
operator|.
name|Element
argument_list|(
literal|"inputSpec"
argument_list|,
name|specificationUri
argument_list|)
argument_list|)
expr_stmt|;
name|elements
operator|.
name|add
argument_list|(
operator|new
name|MojoExecutor
operator|.
name|Element
argument_list|(
literal|"language"
argument_list|,
name|language
argument_list|)
argument_list|)
expr_stmt|;
name|elements
operator|.
name|add
argument_list|(
operator|new
name|MojoExecutor
operator|.
name|Element
argument_list|(
literal|"generateApis"
argument_list|,
literal|"false"
argument_list|)
argument_list|)
expr_stmt|;
name|elements
operator|.
name|add
argument_list|(
operator|new
name|MojoExecutor
operator|.
name|Element
argument_list|(
literal|"generateModelTests"
argument_list|,
literal|"false"
argument_list|)
argument_list|)
expr_stmt|;
name|elements
operator|.
name|add
argument_list|(
operator|new
name|MojoExecutor
operator|.
name|Element
argument_list|(
literal|"generateModelDocumentation"
argument_list|,
literal|"false"
argument_list|)
argument_list|)
expr_stmt|;
name|elements
operator|.
name|add
argument_list|(
operator|new
name|MojoExecutor
operator|.
name|Element
argument_list|(
literal|"generateSupportingFiles"
argument_list|,
literal|"false"
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|modelOutput
operator|!=
literal|null
condition|)
block|{
name|elements
operator|.
name|add
argument_list|(
operator|new
name|MojoExecutor
operator|.
name|Element
argument_list|(
literal|"output"
argument_list|,
name|modelOutput
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|modelPackage
operator|!=
literal|null
condition|)
block|{
name|elements
operator|.
name|add
argument_list|(
operator|new
name|MojoExecutor
operator|.
name|Element
argument_list|(
literal|"modelPackage"
argument_list|,
name|modelPackage
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|modelNamePrefix
operator|!=
literal|null
condition|)
block|{
name|elements
operator|.
name|add
argument_list|(
operator|new
name|MojoExecutor
operator|.
name|Element
argument_list|(
literal|"modelNamePrefix"
argument_list|,
name|modelNamePrefix
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|modelNameSuffix
operator|!=
literal|null
condition|)
block|{
name|elements
operator|.
name|add
argument_list|(
operator|new
name|MojoExecutor
operator|.
name|Element
argument_list|(
literal|"modelNameSuffix"
argument_list|,
name|modelNameSuffix
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|modelWithXml
operator|!=
literal|null
condition|)
block|{
name|elements
operator|.
name|add
argument_list|(
operator|new
name|MojoExecutor
operator|.
name|Element
argument_list|(
literal|"withXml"
argument_list|,
name|modelPackage
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|executeMojo
argument_list|(
name|plugin
argument_list|(
name|groupId
argument_list|(
literal|"io.swagger"
argument_list|)
argument_list|,
name|artifactId
argument_list|(
literal|"swagger-codegen-maven-plugin"
argument_list|)
argument_list|,
name|version
argument_list|(
name|swaggerCodegenMavenPluginVersion
argument_list|)
argument_list|)
argument_list|,
name|goal
argument_list|(
literal|"generate"
argument_list|)
argument_list|,
name|configuration
argument_list|(
name|elements
operator|.
name|toArray
argument_list|(
operator|new
name|MojoExecutor
operator|.
name|Element
index|[
name|elements
operator|.
name|size
argument_list|()
index|]
argument_list|)
argument_list|)
argument_list|,
name|executionEnvironment
argument_list|(
name|mavenProject
argument_list|,
name|mavenSession
argument_list|,
name|pluginManager
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|detectCamelVersionFromClasspath ()
specifier|protected
name|String
name|detectCamelVersionFromClasspath
parameter_list|()
block|{
return|return
name|mavenProject
operator|.
name|getDependencies
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|d
lambda|->
literal|"org.apache.camel"
operator|.
name|equals
argument_list|(
name|d
operator|.
name|getGroupId
argument_list|()
argument_list|)
operator|&&
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|d
operator|.
name|getVersion
argument_list|()
argument_list|)
argument_list|)
operator|.
name|findFirst
argument_list|()
operator|.
name|map
argument_list|(
name|Dependency
operator|::
name|getVersion
argument_list|)
operator|.
name|orElse
argument_list|(
literal|null
argument_list|)
return|;
block|}
DECL|method|detectRestComponentFromClasspath ()
specifier|protected
name|String
name|detectRestComponentFromClasspath
parameter_list|()
block|{
for|for
control|(
specifier|final
name|Dependency
name|dep
range|:
name|mavenProject
operator|.
name|getDependencies
argument_list|()
control|)
block|{
if|if
condition|(
literal|"org.apache.camel"
operator|.
name|equals
argument_list|(
name|dep
operator|.
name|getGroupId
argument_list|()
argument_list|)
condition|)
block|{
specifier|final
name|String
name|aid
init|=
name|dep
operator|.
name|getArtifactId
argument_list|()
decl_stmt|;
specifier|final
name|Optional
argument_list|<
name|String
argument_list|>
name|comp
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|DEFAULT_REST_CONSUMER_COMPONENTS
argument_list|)
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|c
lambda|->
name|aid
operator|.
name|startsWith
argument_list|(
literal|"camel-"
operator|+
name|c
argument_list|)
argument_list|)
operator|.
name|findFirst
argument_list|()
decl_stmt|;
if|if
condition|(
name|comp
operator|.
name|isPresent
argument_list|()
condition|)
block|{
return|return
name|comp
operator|.
name|get
argument_list|()
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|detectSpringBootFromClasspath ()
specifier|protected
name|boolean
name|detectSpringBootFromClasspath
parameter_list|()
block|{
return|return
name|mavenProject
operator|.
name|getDependencies
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|anyMatch
argument_list|(
name|d
lambda|->
literal|"org.springframework.boot"
operator|.
name|equals
argument_list|(
name|d
operator|.
name|getGroupId
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
DECL|method|detectSpringBootMainPackage ()
specifier|protected
name|String
name|detectSpringBootMainPackage
parameter_list|()
throws|throws
name|IOException
block|{
for|for
control|(
specifier|final
name|String
name|src
range|:
name|mavenProject
operator|.
name|getCompileSourceRoots
argument_list|()
control|)
block|{
specifier|final
name|String
name|d
init|=
name|findSpringSpringBootPackage
argument_list|(
operator|new
name|File
argument_list|(
name|src
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|d
operator|!=
literal|null
condition|)
block|{
return|return
name|d
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|findSpringSpringBootPackage (final File dir)
specifier|protected
name|String
name|findSpringSpringBootPackage
parameter_list|(
specifier|final
name|File
name|dir
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
name|File
index|[]
name|files
init|=
name|dir
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
specifier|final
name|File
name|file
range|:
name|files
control|)
block|{
if|if
condition|(
name|file
operator|.
name|getName
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|".java"
argument_list|)
condition|)
block|{
try|try
init|(
name|InputStream
name|stream
init|=
operator|new
name|FileInputStream
argument_list|(
name|file
argument_list|)
init|)
block|{
specifier|final
name|String
name|content
init|=
name|IOHelper
operator|.
name|loadText
argument_list|(
name|stream
argument_list|)
decl_stmt|;
if|if
condition|(
name|content
operator|.
name|contains
argument_list|(
literal|"@SpringBootApplication"
argument_list|)
condition|)
block|{
return|return
name|grabPackageName
argument_list|(
name|content
argument_list|)
return|;
block|}
block|}
block|}
elseif|else
if|if
condition|(
name|file
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
specifier|final
name|String
name|packageName
init|=
name|findSpringSpringBootPackage
argument_list|(
name|file
argument_list|)
decl_stmt|;
if|if
condition|(
name|packageName
operator|!=
literal|null
condition|)
block|{
return|return
name|packageName
return|;
block|}
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|grabPackageName (final String content)
specifier|protected
specifier|static
name|String
name|grabPackageName
parameter_list|(
specifier|final
name|String
name|content
parameter_list|)
block|{
specifier|final
name|String
index|[]
name|lines
init|=
name|content
operator|.
name|split
argument_list|(
literal|"\\n"
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
literal|"package "
argument_list|)
condition|)
block|{
name|line
operator|=
name|line
operator|.
name|substring
argument_list|(
literal|8
argument_list|)
expr_stmt|;
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
name|endsWith
argument_list|(
literal|";"
argument_list|)
condition|)
block|{
name|line
operator|=
name|line
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|line
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
return|return
name|line
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

