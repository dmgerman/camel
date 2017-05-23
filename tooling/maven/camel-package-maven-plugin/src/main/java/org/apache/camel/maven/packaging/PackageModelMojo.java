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
name|FileOutputStream
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
name|OutputStream
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
name|Collections
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
name|commons
operator|.
name|io
operator|.
name|FileUtils
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
comment|/**  * Analyses the Camel EIPs in a project and generates extra descriptor information for easier auto-discovery in Camel.  *  * @goal generate-eips-list  */
end_comment

begin_class
DECL|class|PackageModelMojo
specifier|public
class|class
name|PackageModelMojo
extends|extends
name|AbstractMojo
block|{
comment|/**      * The maven project.      *      * @parameter property="project"      * @required      * @readonly      */
DECL|field|project
specifier|protected
name|MavenProject
name|project
decl_stmt|;
comment|/**      * The camel-core directory      *      * @parameter default-value="${project.build.directory}"      */
DECL|field|buildDir
specifier|protected
name|File
name|buildDir
decl_stmt|;
comment|/**      * The output directory for generated models file      *      * @parameter default-value="${project.build.directory}/generated/camel/models"      */
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
comment|/**      * build context to check changed files and mark them for refresh      * (used for m2e compatibility)      *      * @component      * @readonly      */
DECL|field|buildContext
specifier|private
name|BuildContext
name|buildContext
decl_stmt|;
comment|/**      * Execute goal.      *      * @throws org.apache.maven.plugin.MojoExecutionException execution of the main class or one of the      *                 threads it generated failed.      * @throws org.apache.maven.plugin.MojoFailureException something bad happened...      */
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
name|prepareModel
argument_list|(
name|getLog
argument_list|()
argument_list|,
name|project
argument_list|,
name|projectHelper
argument_list|,
name|outDir
argument_list|,
name|buildDir
argument_list|,
name|buildContext
argument_list|)
expr_stmt|;
block|}
DECL|method|prepareModel (Log log, MavenProject project, MavenProjectHelper projectHelper, File modelOutDir, File buildDir, BuildContext buildContext)
specifier|public
specifier|static
name|void
name|prepareModel
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
name|modelOutDir
parameter_list|,
name|File
name|buildDir
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
name|modelOutDir
argument_list|,
literal|"META-INF/services/org/apache/camel/"
argument_list|)
decl_stmt|;
name|camelMetaDir
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
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
comment|// find all json files in camel-core
if|if
condition|(
name|buildDir
operator|!=
literal|null
operator|&&
name|buildDir
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|File
name|target
init|=
operator|new
name|File
argument_list|(
name|buildDir
argument_list|,
literal|"classes/org/apache/camel/model"
argument_list|)
decl_stmt|;
name|PackageHelper
operator|.
name|findJsonFiles
argument_list|(
name|target
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
block|}
name|List
argument_list|<
name|String
argument_list|>
name|models
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
comment|// sort the names
for|for
control|(
name|File
name|file
range|:
name|jsonFiles
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
name|endsWith
argument_list|(
literal|".json"
argument_list|)
condition|)
block|{
comment|// strip out .json from the name
name|String
name|modelName
init|=
name|name
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|name
operator|.
name|length
argument_list|()
operator|-
literal|5
argument_list|)
decl_stmt|;
name|models
operator|.
name|add
argument_list|(
name|modelName
argument_list|)
expr_stmt|;
block|}
block|}
name|Collections
operator|.
name|sort
argument_list|(
name|models
argument_list|)
expr_stmt|;
name|File
name|outFile
init|=
operator|new
name|File
argument_list|(
name|camelMetaDir
argument_list|,
literal|"model.properties"
argument_list|)
decl_stmt|;
comment|// check if the existing file has the same content, and if so then leave it as is so we do not write any changes
comment|// which can cause a re-compile of all the source code
if|if
condition|(
name|outFile
operator|.
name|exists
argument_list|()
condition|)
block|{
try|try
block|{
name|List
argument_list|<
name|String
argument_list|>
name|existing
init|=
name|FileUtils
operator|.
name|readLines
argument_list|(
name|outFile
argument_list|)
decl_stmt|;
comment|// skip comment lines
name|existing
operator|=
name|existing
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|l
lambda|->
operator|!
name|l
operator|.
name|startsWith
argument_list|(
literal|"#"
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
comment|// are the content the same?
if|if
condition|(
name|models
operator|.
name|containsAll
argument_list|(
name|existing
argument_list|)
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"No model changes detected"
argument_list|)
expr_stmt|;
return|return;
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
block|}
try|try
block|{
name|OutputStream
name|os
init|=
name|buildContext
operator|.
name|newFileOutputStream
argument_list|(
name|outFile
argument_list|)
decl_stmt|;
name|FileOutputStream
name|fos
init|=
operator|new
name|FileOutputStream
argument_list|(
name|outFile
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|fos
operator|.
name|write
argument_list|(
literal|"#Generated by camel-package-maven-plugin\n"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|name
range|:
name|models
control|)
block|{
name|fos
operator|.
name|write
argument_list|(
name|name
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|fos
operator|.
name|write
argument_list|(
literal|"\n"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|fos
operator|.
name|close
argument_list|()
expr_stmt|;
name|os
operator|.
name|close
argument_list|()
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
name|models
operator|.
name|size
argument_list|()
operator|+
literal|" Camel models"
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
block|}
end_class

end_unit

