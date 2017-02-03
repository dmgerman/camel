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
name|Collections
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
name|StringHelper
operator|.
name|camelDashToTitle
import|;
end_import

begin_comment
comment|/**  * Analyses the Camel plugins in a project and generates extra descriptor information for easier auto-discovery in Camel.  *  * @goal generate-others-list  */
end_comment

begin_class
DECL|class|PackageOtherMojo
specifier|public
class|class
name|PackageOtherMojo
extends|extends
name|AbstractMojo
block|{
comment|/**      * The maven project.      *      * @parameter property="project"      * @required      * @readonly      */
DECL|field|project
specifier|protected
name|MavenProject
name|project
decl_stmt|;
comment|/**      * The output directory for generated components file      *      * @parameter default-value="${project.build.directory}/generated/camel/others"      */
DECL|field|otherOutDir
specifier|protected
name|File
name|otherOutDir
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
comment|/**      * build context to check changed files and mark them for refresh (used for      * m2e compatibility)      *       * @component      * @readonly      */
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
name|prepareOthers
argument_list|(
name|getLog
argument_list|()
argument_list|,
name|project
argument_list|,
name|projectHelper
argument_list|,
name|otherOutDir
argument_list|,
name|schemaOutDir
argument_list|,
name|buildContext
argument_list|)
expr_stmt|;
block|}
DECL|method|prepareOthers (Log log, MavenProject project, MavenProjectHelper projectHelper, File otherOutDir, File schemaOutDir, BuildContext buildContext)
specifier|public
specifier|static
name|void
name|prepareOthers
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
name|otherOutDir
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
comment|// are there any components, data formats or languages?
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
name|File
name|comp
init|=
operator|new
name|File
argument_list|(
name|f
argument_list|,
literal|"META-INF/services/org/apache/camel/component"
argument_list|)
decl_stmt|;
if|if
condition|(
name|comp
operator|.
name|exists
argument_list|()
operator|&&
name|comp
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
return|return;
block|}
name|File
name|df
init|=
operator|new
name|File
argument_list|(
name|f
argument_list|,
literal|"META-INF/services/org/apache/camel/dataformat"
argument_list|)
decl_stmt|;
if|if
condition|(
name|df
operator|.
name|exists
argument_list|()
operator|&&
name|df
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
return|return;
block|}
name|File
name|lan
init|=
operator|new
name|File
argument_list|(
name|f
argument_list|,
literal|"META-INF/services/org/apache/camel/language"
argument_list|)
decl_stmt|;
if|if
condition|(
name|lan
operator|.
name|exists
argument_list|()
operator|&&
name|lan
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
return|return;
block|}
block|}
comment|// okay none of those then this is a other kind of artifact
comment|// first we need to setup the output directory because the next check
comment|// can stop the build before the end and eclipse always needs to know about that directory
if|if
condition|(
name|projectHelper
operator|!=
literal|null
condition|)
block|{
name|projectHelper
operator|.
name|addResource
argument_list|(
name|project
argument_list|,
name|otherOutDir
operator|.
name|getPath
argument_list|()
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"**/other.properties"
argument_list|)
argument_list|,
name|Collections
operator|.
name|emptyList
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|PackageHelper
operator|.
name|haveResourcesChanged
argument_list|(
name|log
argument_list|,
name|project
argument_list|,
name|buildContext
argument_list|,
literal|"META-INF/services/org/apache/camel/component"
argument_list|)
operator|&&
operator|!
name|PackageHelper
operator|.
name|haveResourcesChanged
argument_list|(
name|log
argument_list|,
name|project
argument_list|,
name|buildContext
argument_list|,
literal|"META-INF/services/org/apache/camel/dataformat"
argument_list|)
operator|&&
operator|!
name|PackageHelper
operator|.
name|haveResourcesChanged
argument_list|(
name|log
argument_list|,
name|project
argument_list|,
name|buildContext
argument_list|,
literal|"META-INF/services/org/apache/camel/language"
argument_list|)
condition|)
block|{
return|return;
block|}
name|String
name|name
init|=
name|project
operator|.
name|getArtifactId
argument_list|()
decl_stmt|;
comment|// strip leading camel-
if|if
condition|(
name|name
operator|.
name|startsWith
argument_list|(
literal|"camel-"
argument_list|)
condition|)
block|{
name|name
operator|=
name|name
operator|.
name|substring
argument_list|(
literal|6
argument_list|)
expr_stmt|;
block|}
try|try
block|{
comment|// create json model
name|OtherModel
name|otherModel
init|=
operator|new
name|OtherModel
argument_list|()
decl_stmt|;
name|otherModel
operator|.
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|otherModel
operator|.
name|setGroupId
argument_list|(
name|project
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|otherModel
operator|.
name|setArtifactId
argument_list|(
name|project
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|otherModel
operator|.
name|setVersion
argument_list|(
name|project
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|otherModel
operator|.
name|setDescription
argument_list|(
name|project
operator|.
name|getDescription
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|project
operator|.
name|getName
argument_list|()
operator|!=
literal|null
operator|&&
name|project
operator|.
name|getName
argument_list|()
operator|.
name|contains
argument_list|(
literal|"(deprecated)"
argument_list|)
condition|)
block|{
name|otherModel
operator|.
name|setDeprecated
argument_list|(
literal|"true"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|otherModel
operator|.
name|setDeprecated
argument_list|(
literal|"false"
argument_list|)
expr_stmt|;
block|}
name|otherModel
operator|.
name|setFirstVersion
argument_list|(
name|project
operator|.
name|getProperties
argument_list|()
operator|.
name|getProperty
argument_list|(
literal|"firstVersion"
argument_list|)
argument_list|)
expr_stmt|;
name|otherModel
operator|.
name|setLabel
argument_list|(
name|project
operator|.
name|getProperties
argument_list|()
operator|.
name|getProperty
argument_list|(
literal|"label"
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|title
init|=
name|project
operator|.
name|getProperties
argument_list|()
operator|.
name|getProperty
argument_list|(
literal|"title"
argument_list|)
decl_stmt|;
if|if
condition|(
name|title
operator|==
literal|null
condition|)
block|{
name|title
operator|=
name|camelDashToTitle
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
name|otherModel
operator|.
name|setTitle
argument_list|(
name|title
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Model "
operator|+
name|otherModel
argument_list|)
expr_stmt|;
comment|// write this to the directory
name|File
name|dir
init|=
name|schemaOutDir
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
name|OutputStream
name|fos
init|=
name|buildContext
operator|.
name|newFileOutputStream
argument_list|(
name|out
argument_list|)
decl_stmt|;
name|String
name|json
init|=
name|createJsonSchema
argument_list|(
name|otherModel
argument_list|)
decl_stmt|;
name|fos
operator|.
name|write
argument_list|(
name|json
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
literal|" other"
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
literal|"Error loading language model from camel-core. Reason: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
throw|;
block|}
comment|// now create properties file
name|File
name|camelMetaDir
init|=
operator|new
name|File
argument_list|(
name|otherOutDir
argument_list|,
literal|"META-INF/services/org/apache/camel/"
argument_list|)
decl_stmt|;
name|Properties
name|properties
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"name"
argument_list|,
name|name
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
literal|"other.properties"
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
name|Properties
name|existing
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|InputStream
name|is
init|=
operator|new
name|FileInputStream
argument_list|(
name|outFile
argument_list|)
decl_stmt|;
name|existing
operator|.
name|load
argument_list|(
name|is
argument_list|)
expr_stmt|;
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
comment|// are the content the same?
if|if
condition|(
name|existing
operator|.
name|equals
argument_list|(
name|properties
argument_list|)
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"No changes detected"
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
name|properties
operator|.
name|store
argument_list|(
name|os
argument_list|,
literal|"Generated by camel-package-maven-plugin"
argument_list|)
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
DECL|method|createJsonSchema (OtherModel otherModel)
specifier|private
specifier|static
name|String
name|createJsonSchema
parameter_list|(
name|OtherModel
name|otherModel
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
comment|// language model
name|buffer
operator|.
name|append
argument_list|(
literal|"\n \"other\": {"
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
name|otherModel
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
literal|"other"
argument_list|)
operator|.
name|append
argument_list|(
literal|"\","
argument_list|)
expr_stmt|;
if|if
condition|(
name|otherModel
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
name|otherModel
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
name|otherModel
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
name|otherModel
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
literal|"\n    \"deprecated\": \""
argument_list|)
operator|.
name|append
argument_list|(
name|otherModel
operator|.
name|getDeprecated
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
name|otherModel
operator|.
name|getFirstVersion
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|"\n    \"firstVersion\": \""
argument_list|)
operator|.
name|append
argument_list|(
name|otherModel
operator|.
name|getFirstVersion
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
name|otherModel
operator|.
name|getLabel
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|"\n    \"label\": \""
argument_list|)
operator|.
name|append
argument_list|(
name|otherModel
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
name|otherModel
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
name|otherModel
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
name|otherModel
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
literal|"\n  }"
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|"\n}"
argument_list|)
expr_stmt|;
return|return
name|buffer
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|class|OtherModel
specifier|private
specifier|static
class|class
name|OtherModel
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
DECL|field|description
specifier|private
name|String
name|description
decl_stmt|;
DECL|field|deprecated
specifier|private
name|String
name|deprecated
decl_stmt|;
DECL|field|firstVersion
specifier|private
name|String
name|firstVersion
decl_stmt|;
DECL|field|label
specifier|private
name|String
name|label
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
DECL|method|getDeprecated ()
specifier|public
name|String
name|getDeprecated
parameter_list|()
block|{
return|return
name|deprecated
return|;
block|}
DECL|method|setDeprecated (String deprecated)
specifier|public
name|void
name|setDeprecated
parameter_list|(
name|String
name|deprecated
parameter_list|)
block|{
name|this
operator|.
name|deprecated
operator|=
name|deprecated
expr_stmt|;
block|}
DECL|method|getFirstVersion ()
specifier|public
name|String
name|getFirstVersion
parameter_list|()
block|{
return|return
name|firstVersion
return|;
block|}
DECL|method|setFirstVersion (String firstVersion)
specifier|public
name|void
name|setFirstVersion
parameter_list|(
name|String
name|firstVersion
parameter_list|)
block|{
name|this
operator|.
name|firstVersion
operator|=
name|firstVersion
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
literal|"OtherModel["
operator|+
literal|"name='"
operator|+
name|name
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

