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
name|InputStream
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
name|nio
operator|.
name|file
operator|.
name|Paths
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|io
operator|.
name|IOUtils
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
comment|/**  * Analyses the Camel plugins in a project and generates legal files.  */
end_comment

begin_class
annotation|@
name|Mojo
argument_list|(
name|name
operator|=
literal|"generate-legal"
argument_list|,
name|threadSafe
operator|=
literal|true
argument_list|,
name|defaultPhase
operator|=
name|LifecyclePhase
operator|.
name|PROCESS_CLASSES
argument_list|)
DECL|class|PackageLegalMojo
specifier|public
class|class
name|PackageLegalMojo
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
comment|/**      * The output directory for generated components file      */
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"${project.build.directory}/generated/camel/legal"
argument_list|)
DECL|field|legalOutDir
specifier|protected
name|File
name|legalOutDir
decl_stmt|;
comment|/**      * Maven ProjectHelper.      */
annotation|@
name|Component
DECL|field|projectHelper
specifier|private
name|MavenProjectHelper
name|projectHelper
decl_stmt|;
comment|/**      * build context to check changed files and mark them for refresh (used for      * m2e compatibility)      */
annotation|@
name|Component
DECL|field|buildContext
specifier|private
name|BuildContext
name|buildContext
decl_stmt|;
comment|/**      * Execute goal.      *      * @throws MojoExecutionException execution of the main class or one of the      *                 threads it generated failed.      * @throws MojoFailureException something bad happened...      */
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
name|processLegal
argument_list|(
name|legalOutDir
operator|.
name|toPath
argument_list|()
argument_list|)
expr_stmt|;
name|projectHelper
operator|.
name|addResource
argument_list|(
name|project
argument_list|,
name|legalOutDir
operator|.
name|getPath
argument_list|()
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"**/*"
argument_list|)
argument_list|,
name|Collections
operator|.
name|emptyList
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|processLegal (Path legalOutDir)
specifier|public
name|void
name|processLegal
parameter_list|(
name|Path
name|legalOutDir
parameter_list|)
throws|throws
name|MojoExecutionException
block|{
comment|// Only take care about camel legal stuff
if|if
condition|(
operator|!
literal|"org.apache.camel"
operator|.
name|equals
argument_list|(
name|project
operator|.
name|getGroupId
argument_list|()
argument_list|)
condition|)
block|{
return|return;
block|}
name|boolean
name|hasLicense
init|=
name|project
operator|.
name|getResources
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|Resource
operator|::
name|getDirectory
argument_list|)
operator|.
name|map
argument_list|(
name|Paths
operator|::
name|get
argument_list|)
operator|.
name|map
argument_list|(
name|p
lambda|->
name|p
operator|.
name|resolve
argument_list|(
literal|"META-INF"
argument_list|)
operator|.
name|resolve
argument_list|(
literal|"LICENSE.txt"
argument_list|)
argument_list|)
operator|.
name|anyMatch
argument_list|(
name|Files
operator|::
name|isRegularFile
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|hasLicense
condition|)
block|{
try|try
init|(
name|InputStream
name|isLicense
init|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/camel-LICENSE.txt"
argument_list|)
init|)
block|{
name|String
name|license
init|=
name|IOUtils
operator|.
name|toString
argument_list|(
name|isLicense
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
decl_stmt|;
name|updateResource
argument_list|(
name|legalOutDir
operator|.
name|resolve
argument_list|(
literal|"META-INF"
argument_list|)
operator|.
name|resolve
argument_list|(
literal|"LICENSE.txt"
argument_list|)
argument_list|,
name|license
argument_list|)
expr_stmt|;
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
literal|"Failed to write legal files. Reason: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
name|boolean
name|hasNotice
init|=
name|project
operator|.
name|getResources
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|Resource
operator|::
name|getDirectory
argument_list|)
operator|.
name|map
argument_list|(
name|Paths
operator|::
name|get
argument_list|)
operator|.
name|map
argument_list|(
name|p
lambda|->
name|p
operator|.
name|resolve
argument_list|(
literal|"META-INF"
argument_list|)
operator|.
name|resolve
argument_list|(
literal|"NOTICE.txt"
argument_list|)
argument_list|)
operator|.
name|anyMatch
argument_list|(
name|Files
operator|::
name|isRegularFile
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|hasNotice
condition|)
block|{
try|try
init|(
name|InputStream
name|isNotice
init|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/camel-NOTICE.txt"
argument_list|)
init|)
block|{
name|String
name|notice
init|=
name|IOUtils
operator|.
name|toString
argument_list|(
name|isNotice
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
decl_stmt|;
name|updateResource
argument_list|(
name|legalOutDir
operator|.
name|resolve
argument_list|(
literal|"META-INF"
argument_list|)
operator|.
name|resolve
argument_list|(
literal|"NOTICE.txt"
argument_list|)
argument_list|,
name|notice
argument_list|)
expr_stmt|;
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
literal|"Failed to write legal files. Reason: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
DECL|method|updateResource (Path out, String data)
specifier|protected
name|void
name|updateResource
parameter_list|(
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
DECL|method|refresh (Path file)
specifier|protected
name|void
name|refresh
parameter_list|(
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

