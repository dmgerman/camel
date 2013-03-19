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
name|apache
operator|.
name|maven
operator|.
name|project
operator|.
name|MavenProjectHelper
import|;
end_import

begin_comment
comment|/**  * Analyses the Camel plugins in a project and generates extra descriptor information for easier auto-discovery in Camel.  *  * @goal generate-components-list  * @execute phase="generate-resources"  */
end_comment

begin_class
DECL|class|PackageMojo
specifier|public
class|class
name|PackageMojo
extends|extends
name|AbstractMojo
block|{
comment|/**      * The maven project.      *      * @parameter property="project"      * @required      * @readonly      */
DECL|field|project
specifier|protected
name|MavenProject
name|project
decl_stmt|;
comment|/**      * The output directory for generated components file      *      * @parameter default-value="${project.build.directory}/generated/camel/components"      */
DECL|field|outDir
specifier|protected
name|File
name|outDir
decl_stmt|;
comment|/**      * Maven ProjectHelper.      *      * @component      * @readonly      */
DECL|field|projectHelper
specifier|private
name|MavenProjectHelper
name|projectHelper
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
name|File
name|camelMetaDir
init|=
operator|new
name|File
argument_list|(
name|outDir
argument_list|,
literal|"META-INF/services/org/apache/camel/"
argument_list|)
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
block|}
block|}
block|}
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
literal|"components"
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
literal|"component.properties"
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
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|"Generated "
operator|+
name|outFile
operator|+
literal|" containing the Camel "
operator|+
operator|(
name|count
operator|>
literal|1
condition|?
literal|"components "
else|:
literal|"component "
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
literal|"**/component.properties"
argument_list|)
expr_stmt|;
name|projectHelper
operator|.
name|addResource
argument_list|(
name|this
operator|.
name|project
argument_list|,
name|outDir
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
name|this
operator|.
name|project
argument_list|,
literal|"properties"
argument_list|,
literal|"camelComponent"
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
name|getLog
argument_list|()
operator|.
name|debug
argument_list|(
literal|"No META-INF/services/org/apache/camel/component directory found. Are you sure you have created a Camel component?"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

