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
name|IOError
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
name|io
operator|.
name|Writer
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
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
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
name|Files
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
name|java
operator|.
name|util
operator|.
name|Date
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
name|Objects
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
name|apache
operator|.
name|velocity
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
name|velocity
operator|.
name|VelocityContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|velocity
operator|.
name|app
operator|.
name|VelocityEngine
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|velocity
operator|.
name|exception
operator|.
name|VelocityException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|velocity
operator|.
name|runtime
operator|.
name|RuntimeConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|velocity
operator|.
name|runtime
operator|.
name|resource
operator|.
name|loader
operator|.
name|ClasspathResourceLoader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
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
comment|/**  * Base class for API based generation MOJOs.  */
end_comment

begin_class
DECL|class|AbstractGeneratorMojo
specifier|public
specifier|abstract
class|class
name|AbstractGeneratorMojo
extends|extends
name|AbstractMojo
block|{
DECL|field|PREFIX
specifier|protected
specifier|static
specifier|final
name|String
name|PREFIX
init|=
literal|"org.apache.camel."
decl_stmt|;
DECL|field|OUT_PACKAGE
specifier|protected
specifier|static
specifier|final
name|String
name|OUT_PACKAGE
init|=
name|PREFIX
operator|+
literal|"component.internal"
decl_stmt|;
DECL|field|COMPONENT_PACKAGE
specifier|protected
specifier|static
specifier|final
name|String
name|COMPONENT_PACKAGE
init|=
name|PREFIX
operator|+
literal|"component"
decl_stmt|;
DECL|field|projectClassLoader
specifier|private
specifier|static
name|ClassLoader
name|projectClassLoader
decl_stmt|;
DECL|field|sharedProjectState
specifier|private
specifier|static
name|boolean
name|sharedProjectState
decl_stmt|;
comment|// used for velocity logging, to avoid creating velocity.log
DECL|field|log
specifier|protected
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
name|OUT_PACKAGE
argument_list|)
DECL|field|outPackage
specifier|protected
name|String
name|outPackage
decl_stmt|;
annotation|@
name|Parameter
argument_list|(
name|required
operator|=
literal|true
argument_list|,
name|property
operator|=
name|PREFIX
operator|+
literal|"scheme"
argument_list|)
DECL|field|scheme
specifier|protected
name|String
name|scheme
decl_stmt|;
annotation|@
name|Parameter
argument_list|(
name|required
operator|=
literal|true
argument_list|,
name|property
operator|=
name|PREFIX
operator|+
literal|"componentName"
argument_list|)
DECL|field|componentName
specifier|protected
name|String
name|componentName
decl_stmt|;
annotation|@
name|Parameter
argument_list|(
name|required
operator|=
literal|true
argument_list|,
name|defaultValue
operator|=
name|COMPONENT_PACKAGE
argument_list|)
DECL|field|componentPackage
specifier|protected
name|String
name|componentPackage
decl_stmt|;
annotation|@
name|Parameter
argument_list|(
name|required
operator|=
literal|true
argument_list|,
name|defaultValue
operator|=
literal|"${project}"
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
DECL|method|AbstractGeneratorMojo ()
specifier|protected
name|AbstractGeneratorMojo
parameter_list|()
block|{
name|clearSharedProjectState
argument_list|()
expr_stmt|;
block|}
DECL|method|setSharedProjectState (boolean sharedProjectState)
specifier|public
specifier|static
name|void
name|setSharedProjectState
parameter_list|(
name|boolean
name|sharedProjectState
parameter_list|)
block|{
name|AbstractGeneratorMojo
operator|.
name|sharedProjectState
operator|=
name|sharedProjectState
expr_stmt|;
block|}
DECL|method|clearSharedProjectState ()
specifier|protected
specifier|static
name|void
name|clearSharedProjectState
parameter_list|()
block|{
if|if
condition|(
operator|!
name|sharedProjectState
condition|)
block|{
name|projectClassLoader
operator|=
literal|null
expr_stmt|;
block|}
block|}
comment|// Thread-safe deferred-construction singleton via nested static class
DECL|class|VelocityEngineHolder
specifier|private
specifier|static
class|class
name|VelocityEngineHolder
block|{
DECL|field|ENGINE
specifier|private
specifier|static
specifier|final
name|VelocityEngine
name|ENGINE
decl_stmt|;
static|static
block|{
comment|// initialize velocity to load resources from class loader and use Log4J
name|Properties
name|velocityProperties
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|velocityProperties
operator|.
name|setProperty
argument_list|(
name|RuntimeConstants
operator|.
name|RESOURCE_LOADER
argument_list|,
literal|"cloader"
argument_list|)
expr_stmt|;
name|velocityProperties
operator|.
name|setProperty
argument_list|(
literal|"cloader.resource.loader.class"
argument_list|,
name|ClasspathResourceLoader
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|Logger
name|velocityLogger
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
literal|"org.apache.camel.maven.Velocity"
argument_list|)
decl_stmt|;
name|velocityProperties
operator|.
name|setProperty
argument_list|(
name|RuntimeConstants
operator|.
name|RUNTIME_LOG_NAME
argument_list|,
name|velocityLogger
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|ENGINE
operator|=
operator|new
name|VelocityEngine
argument_list|(
name|velocityProperties
argument_list|)
expr_stmt|;
name|ENGINE
operator|.
name|init
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|getEngine ()
specifier|protected
specifier|static
name|VelocityEngine
name|getEngine
parameter_list|()
throws|throws
name|MojoExecutionException
block|{
try|try
block|{
return|return
name|VelocityEngineHolder
operator|.
name|ENGINE
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
DECL|method|getProjectClassLoader ()
specifier|protected
name|ClassLoader
name|getProjectClassLoader
parameter_list|()
throws|throws
name|MojoExecutionException
block|{
if|if
condition|(
name|projectClassLoader
operator|==
literal|null
condition|)
block|{
specifier|final
name|List
name|classpathElements
decl_stmt|;
try|try
block|{
name|classpathElements
operator|=
name|project
operator|.
name|getTestClasspathElements
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|artifact
operator|.
name|DependencyResolutionRequiredException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
specifier|final
name|URL
index|[]
name|urls
init|=
operator|new
name|URL
index|[
name|classpathElements
operator|.
name|size
argument_list|()
index|]
decl_stmt|;
name|int
name|i
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Iterator
name|it
init|=
name|classpathElements
operator|.
name|iterator
argument_list|()
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
try|try
block|{
name|urls
index|[
name|i
index|]
operator|=
operator|new
name|File
argument_list|(
operator|(
name|String
operator|)
name|it
operator|.
name|next
argument_list|()
argument_list|)
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Adding project path "
operator|+
name|urls
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
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
specifier|final
name|ClassLoader
name|tccl
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
decl_stmt|;
name|projectClassLoader
operator|=
operator|new
name|URLClassLoader
argument_list|(
name|urls
argument_list|,
name|tccl
operator|!=
literal|null
condition|?
name|tccl
else|:
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|projectClassLoader
return|;
block|}
DECL|method|mergeTemplate (VelocityContext context, File outFile, String templateName)
specifier|protected
name|void
name|mergeTemplate
parameter_list|(
name|VelocityContext
name|context
parameter_list|,
name|File
name|outFile
parameter_list|,
name|String
name|templateName
parameter_list|)
throws|throws
name|MojoExecutionException
block|{
comment|// ensure parent directories exist
specifier|final
name|File
name|outDir
init|=
name|outFile
operator|.
name|getParentFile
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|outDir
operator|.
name|isDirectory
argument_list|()
operator|&&
operator|!
name|outDir
operator|.
name|mkdirs
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
literal|"Error creating directory "
operator|+
name|outDir
argument_list|)
throw|;
block|}
comment|// add generated date
name|context
operator|.
name|put
argument_list|(
literal|"generatedDate"
argument_list|,
operator|new
name|Date
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
comment|// add output package
name|context
operator|.
name|put
argument_list|(
literal|"packageName"
argument_list|,
name|outPackage
argument_list|)
expr_stmt|;
name|context
operator|.
name|put
argument_list|(
literal|"newLine"
argument_list|,
literal|"\n"
argument_list|)
expr_stmt|;
comment|// load velocity template
name|Template
name|template
decl_stmt|;
try|try
block|{
name|template
operator|=
name|getEngine
argument_list|()
operator|.
name|getTemplate
argument_list|(
name|templateName
argument_list|,
literal|"UTF-8"
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
name|MojoExecutionException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
comment|// generate file
try|try
block|{
name|StringWriter
name|writer
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|template
operator|.
name|merge
argument_list|(
name|context
argument_list|,
name|writer
argument_list|)
expr_stmt|;
name|updateResource
argument_list|(
literal|null
argument_list|,
name|outFile
operator|.
name|toPath
argument_list|()
argument_list|,
name|writer
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|VelocityException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
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
DECL|method|getCanonicalName (Class<?> type)
specifier|public
specifier|static
name|String
name|getCanonicalName
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
comment|// remove java.lang prefix for default Java package
name|String
name|canonicalName
init|=
name|type
operator|.
name|getCanonicalName
argument_list|()
decl_stmt|;
specifier|final
name|int
name|pkgEnd
init|=
name|canonicalName
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
decl_stmt|;
if|if
condition|(
name|pkgEnd
operator|>
literal|0
operator|&&
name|canonicalName
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|pkgEnd
argument_list|)
operator|.
name|equals
argument_list|(
literal|"java.lang"
argument_list|)
condition|)
block|{
name|canonicalName
operator|=
name|canonicalName
operator|.
name|substring
argument_list|(
name|pkgEnd
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
return|return
name|canonicalName
return|;
block|}
DECL|method|updateResource (BuildContext buildContext, Path out, String data)
specifier|public
specifier|static
name|void
name|updateResource
parameter_list|(
name|BuildContext
name|buildContext
parameter_list|,
name|Path
name|out
parameter_list|,
name|String
name|data
parameter_list|)
block|{
try|try
block|{
if|if
condition|(
name|data
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|Files
operator|.
name|isRegularFile
argument_list|(
name|out
argument_list|)
condition|)
block|{
name|Files
operator|.
name|delete
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|refresh
argument_list|(
name|buildContext
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|Files
operator|.
name|isRegularFile
argument_list|(
name|out
argument_list|)
operator|&&
name|Files
operator|.
name|isReadable
argument_list|(
name|out
argument_list|)
condition|)
block|{
name|String
name|content
init|=
operator|new
name|String
argument_list|(
name|Files
operator|.
name|readAllBytes
argument_list|(
name|out
argument_list|)
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
decl_stmt|;
if|if
condition|(
name|Objects
operator|.
name|equals
argument_list|(
name|content
argument_list|,
name|data
argument_list|)
condition|)
block|{
return|return;
block|}
block|}
name|Files
operator|.
name|createDirectories
argument_list|(
name|out
operator|.
name|getParent
argument_list|()
argument_list|)
expr_stmt|;
try|try
init|(
name|Writer
name|w
init|=
name|Files
operator|.
name|newBufferedWriter
argument_list|(
name|out
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
init|)
block|{
name|w
operator|.
name|append
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
name|refresh
argument_list|(
name|buildContext
argument_list|,
name|out
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
name|IOError
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|refresh (BuildContext buildContext, Path file)
specifier|public
specifier|static
name|void
name|refresh
parameter_list|(
name|BuildContext
name|buildContext
parameter_list|,
name|Path
name|file
parameter_list|)
block|{
if|if
condition|(
name|buildContext
operator|!=
literal|null
condition|)
block|{
name|buildContext
operator|.
name|refresh
argument_list|(
name|file
operator|.
name|toFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

