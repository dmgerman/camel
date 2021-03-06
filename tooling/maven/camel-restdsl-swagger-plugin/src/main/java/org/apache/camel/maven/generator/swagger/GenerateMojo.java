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
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|io
operator|.
name|swagger
operator|.
name|models
operator|.
name|Swagger
import|;
end_import

begin_import
import|import
name|io
operator|.
name|swagger
operator|.
name|parser
operator|.
name|SwaggerParser
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
name|generator
operator|.
name|swagger
operator|.
name|RestDslGenerator
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
name|RestDslSourceCodeGenerator
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
name|SpringBootProjectSourceCodeGenerator
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

begin_class
annotation|@
name|Mojo
argument_list|(
name|name
operator|=
literal|"generate"
argument_list|,
name|inheritByDefault
operator|=
literal|false
argument_list|,
name|defaultPhase
operator|=
name|LifecyclePhase
operator|.
name|GENERATE_SOURCES
argument_list|,
name|requiresDependencyResolution
operator|=
name|ResolutionScope
operator|.
name|COMPILE
argument_list|,
name|threadSafe
operator|=
literal|true
argument_list|)
DECL|class|GenerateMojo
specifier|public
class|class
name|GenerateMojo
extends|extends
name|AbstractGenerateMojo
block|{
annotation|@
name|Parameter
DECL|field|className
specifier|private
name|String
name|className
decl_stmt|;
annotation|@
name|Parameter
DECL|field|indent
specifier|private
name|String
name|indent
decl_stmt|;
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"${project.build.directory}/generated-sources/restdsl-swagger"
argument_list|,
name|required
operator|=
literal|true
argument_list|)
DECL|field|outputDirectory
specifier|private
name|String
name|outputDirectory
decl_stmt|;
annotation|@
name|Parameter
DECL|field|packageName
specifier|private
name|String
name|packageName
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
block|{
if|if
condition|(
name|skip
condition|)
block|{
return|return;
block|}
specifier|final
name|SwaggerParser
name|swaggerParser
init|=
operator|new
name|SwaggerParser
argument_list|()
decl_stmt|;
specifier|final
name|Swagger
name|swagger
init|=
name|swaggerParser
operator|.
name|read
argument_list|(
name|specificationUri
argument_list|)
decl_stmt|;
if|if
condition|(
name|swagger
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
literal|"Unable to generate REST DSL Swagger sources from specification: "
operator|+
name|specificationUri
operator|+
literal|", make sure that the specification is available at the given URI"
argument_list|)
throw|;
block|}
specifier|final
name|RestDslSourceCodeGenerator
argument_list|<
name|Path
argument_list|>
name|generator
init|=
name|RestDslGenerator
operator|.
name|toPath
argument_list|(
name|swagger
argument_list|)
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|filterOperation
argument_list|)
condition|)
block|{
name|generator
operator|.
name|withOperationFilter
argument_list|(
name|filterOperation
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|className
argument_list|)
condition|)
block|{
name|generator
operator|.
name|withClassName
argument_list|(
name|className
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|indent
operator|!=
literal|null
condition|)
block|{
name|generator
operator|.
name|withIndent
argument_list|(
name|indent
operator|.
name|replace
argument_list|(
literal|"\\t"
argument_list|,
literal|"\t"
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|packageName
argument_list|)
condition|)
block|{
name|generator
operator|.
name|withPackageName
argument_list|(
name|packageName
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|destinationGenerator
argument_list|)
condition|)
block|{
specifier|final
name|DestinationGenerator
name|destinationGeneratorObject
init|=
name|createDestinationGenerator
argument_list|()
decl_stmt|;
name|generator
operator|.
name|withDestinationGenerator
argument_list|(
name|destinationGeneratorObject
argument_list|)
expr_stmt|;
block|}
specifier|final
name|Path
name|outputPath
init|=
operator|new
name|File
argument_list|(
name|outputDirectory
argument_list|)
operator|.
name|toPath
argument_list|()
decl_stmt|;
if|if
condition|(
name|restConfiguration
condition|)
block|{
name|String
name|comp
init|=
name|detectRestComponentFromClasspath
argument_list|()
decl_stmt|;
if|if
condition|(
name|comp
operator|!=
literal|null
condition|)
block|{
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|"Detected Camel Rest component from classpath: "
operator|+
name|comp
argument_list|)
expr_stmt|;
name|generator
operator|.
name|withRestComponent
argument_list|(
name|comp
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|comp
operator|=
literal|"servlet"
expr_stmt|;
comment|// is it spring boot?
name|String
name|aid
init|=
literal|"camel-servlet"
decl_stmt|;
if|if
condition|(
name|detectSpringBootFromClasspath
argument_list|()
condition|)
block|{
name|aid
operator|=
literal|"camel-servlet-starter"
expr_stmt|;
block|}
name|String
name|dep
init|=
literal|"\n\t\t<dependency>"
operator|+
literal|"\n\t\t\t<groupId>org.apache.camel</groupId>"
operator|+
literal|"\n\t\t\t<artifactId>"
operator|+
name|aid
operator|+
literal|"</artifactId>"
decl_stmt|;
name|String
name|ver
init|=
name|detectCamelVersionFromClasspath
argument_list|()
decl_stmt|;
if|if
condition|(
name|ver
operator|!=
literal|null
condition|)
block|{
name|dep
operator|+=
literal|"\n\t\t\t<version>"
operator|+
name|ver
operator|+
literal|"</version>"
expr_stmt|;
block|}
name|dep
operator|+=
literal|"\n\t\t</dependency>\n"
expr_stmt|;
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|"Cannot detect Rest component from classpath. Will use servlet as Rest component."
argument_list|)
expr_stmt|;
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|"Add the following dependency in the Maven pom.xml file:\n"
operator|+
name|dep
operator|+
literal|"\n"
argument_list|)
expr_stmt|;
name|generator
operator|.
name|withRestComponent
argument_list|(
literal|"servlet"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|apiContextPath
argument_list|)
condition|)
block|{
name|generator
operator|.
name|withApiContextPath
argument_list|(
name|apiContextPath
argument_list|)
expr_stmt|;
block|}
comment|// if its a spring boot project and we use servlet then we should generate additional source code
if|if
condition|(
name|detectSpringBootFromClasspath
argument_list|()
operator|&&
literal|"servlet"
operator|.
name|equals
argument_list|(
name|comp
argument_list|)
condition|)
block|{
try|try
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|packageName
argument_list|)
condition|)
block|{
comment|// if not explicit package name then try to use package where the spring boot application is located
name|String
name|pName
init|=
name|detectSpringBootMainPackage
argument_list|()
decl_stmt|;
if|if
condition|(
name|pName
operator|!=
literal|null
condition|)
block|{
name|packageName
operator|=
name|pName
expr_stmt|;
name|generator
operator|.
name|withPackageName
argument_list|(
name|packageName
argument_list|)
expr_stmt|;
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|"Detected @SpringBootApplication, and will be using its package name: "
operator|+
name|packageName
argument_list|)
expr_stmt|;
block|}
block|}
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|"Generating Camel Rest Controller source with package name "
operator|+
name|packageName
operator|+
literal|" in source directory: "
operator|+
name|outputPath
argument_list|)
expr_stmt|;
name|SpringBootProjectSourceCodeGenerator
operator|.
name|generator
argument_list|()
operator|.
name|withPackageName
argument_list|(
name|packageName
argument_list|)
operator|.
name|generate
argument_list|(
name|outputPath
argument_list|)
expr_stmt|;
comment|// the Camel Rest Controller allows to use root as context-path
name|generator
operator|.
name|withRestContextPath
argument_list|(
literal|"/"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
specifier|final
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
literal|"Unable to generate Camel Rest Controller source due "
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
if|if
condition|(
name|detectSpringBootFromClasspath
argument_list|()
condition|)
block|{
name|generator
operator|.
name|asSpringComponent
argument_list|()
expr_stmt|;
name|generator
operator|.
name|asSpringBootProject
argument_list|()
expr_stmt|;
block|}
try|try
block|{
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|"Generating Camel DSL source in directory: "
operator|+
name|outputPath
argument_list|)
expr_stmt|;
name|generator
operator|.
name|generate
argument_list|(
name|outputPath
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
specifier|final
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
literal|"Unable to generate REST DSL Swagger sources from specification: "
operator|+
name|specificationUri
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

