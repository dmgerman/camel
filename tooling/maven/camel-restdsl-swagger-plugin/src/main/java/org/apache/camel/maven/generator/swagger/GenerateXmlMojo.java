begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|FileOutputStream
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
name|CamelContext
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
name|RestDslXmlGenerator
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
name|impl
operator|.
name|DefaultCamelContext
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

begin_class
annotation|@
name|Mojo
argument_list|(
name|name
operator|=
literal|"generate-xml"
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
DECL|class|GenerateXmlMojo
specifier|public
class|class
name|GenerateXmlMojo
extends|extends
name|AbstractGenerateMojo
block|{
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
argument_list|(
name|defaultValue
operator|=
literal|"camel-rest.xml"
argument_list|,
name|required
operator|=
literal|true
argument_list|)
DECL|field|fileName
specifier|private
name|String
name|fileName
decl_stmt|;
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"false"
argument_list|)
DECL|field|blueprint
name|boolean
name|blueprint
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
name|RestDslXmlGenerator
name|generator
init|=
name|RestDslGenerator
operator|.
name|toXml
argument_list|(
name|swagger
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|blueprint
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|blueprint
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|blueprint
argument_list|)
expr_stmt|;
if|if
condition|(
name|blueprint
condition|)
block|{
name|generator
operator|.
name|withBlueprint
argument_list|()
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
try|try
block|{
name|CamelContext
name|camel
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|String
name|xml
init|=
name|generator
operator|.
name|generate
argument_list|(
name|camel
argument_list|)
decl_stmt|;
comment|// ensure output folder is created
operator|new
name|File
argument_list|(
name|outputDirectory
argument_list|)
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
name|outputDirectory
argument_list|,
name|fileName
argument_list|)
decl_stmt|;
name|FileOutputStream
name|fos
init|=
operator|new
name|FileOutputStream
argument_list|(
name|out
argument_list|)
decl_stmt|;
name|fos
operator|.
name|write
argument_list|(
name|xml
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
block|}
catch|catch
parameter_list|(
specifier|final
name|Exception
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

