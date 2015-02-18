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
name|FileFilter
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
name|between
import|;
end_import

begin_comment
comment|/**  * Creates the Maven catalog for the Camel archetypes  *  * @goal generate-archetype-catalog  * @execute phase="process-resources"  */
end_comment

begin_class
DECL|class|PackageArchetypeCatalogMojo
specifier|public
class|class
name|PackageArchetypeCatalogMojo
extends|extends
name|AbstractMojo
block|{
comment|/**      * The maven project.      *      * @parameter property="project"      * @required      * @readonly      */
DECL|field|project
specifier|protected
name|MavenProject
name|project
decl_stmt|;
comment|/**      * The output directory for generated components file      *      * @parameter default-value="${project.build.directory}/classes/"      */
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
try|try
block|{
name|generateArchetypeCatalog
argument_list|(
name|getLog
argument_list|()
argument_list|,
name|project
argument_list|,
name|projectHelper
argument_list|,
name|outDir
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
name|MojoFailureException
argument_list|(
literal|"Error generating archetype catalog due "
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
DECL|method|generateArchetypeCatalog (Log log, MavenProject project, MavenProjectHelper projectHelper, File outDir)
specifier|public
specifier|static
name|void
name|generateArchetypeCatalog
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
name|outDir
parameter_list|)
throws|throws
name|MojoExecutionException
throws|,
name|IOException
block|{
comment|// find all archetypes
name|File
index|[]
name|dirs
init|=
operator|new
name|File
argument_list|(
literal|"."
argument_list|)
operator|.
name|listFiles
argument_list|(
operator|new
name|FileFilter
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|accept
parameter_list|(
name|File
name|pathname
parameter_list|)
block|{
comment|// skip web console as its deprecated
if|if
condition|(
literal|"camel-archetype-webconsole"
operator|.
name|equals
argument_list|(
name|pathname
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|pathname
operator|.
name|getName
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"camel-archetype"
argument_list|)
operator|&&
name|pathname
operator|.
name|isDirectory
argument_list|()
return|;
block|}
block|}
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|ArchetypeModel
argument_list|>
name|models
init|=
operator|new
name|ArrayList
argument_list|<
name|ArchetypeModel
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|File
name|dir
range|:
name|dirs
control|)
block|{
name|File
name|pom
init|=
operator|new
name|File
argument_list|(
name|dir
argument_list|,
literal|"pom.xml"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|pom
operator|.
name|exists
argument_list|()
operator|&&
operator|!
name|pom
operator|.
name|isFile
argument_list|()
condition|)
block|{
continue|continue;
block|}
name|boolean
name|parent
init|=
literal|false
decl_stmt|;
name|ArchetypeModel
name|model
init|=
operator|new
name|ArchetypeModel
argument_list|()
decl_stmt|;
comment|// just use a simple line by line text parser (no need for DOM) just to grab 4 lines of data
for|for
control|(
name|Object
name|o
range|:
name|FileUtils
operator|.
name|readLines
argument_list|(
name|pom
argument_list|)
control|)
block|{
name|String
name|line
init|=
name|o
operator|.
name|toString
argument_list|()
decl_stmt|;
comment|// we only want to read version from parent
if|if
condition|(
name|line
operator|.
name|contains
argument_list|(
literal|"<parent>"
argument_list|)
condition|)
block|{
name|parent
operator|=
literal|true
expr_stmt|;
continue|continue;
block|}
if|if
condition|(
name|line
operator|.
name|contains
argument_list|(
literal|"</parent>"
argument_list|)
condition|)
block|{
name|parent
operator|=
literal|false
expr_stmt|;
continue|continue;
block|}
if|if
condition|(
name|parent
condition|)
block|{
comment|// grab version from parent
name|String
name|version
init|=
name|between
argument_list|(
name|line
argument_list|,
literal|"<version>"
argument_list|,
literal|"</version>"
argument_list|)
decl_stmt|;
if|if
condition|(
name|version
operator|!=
literal|null
condition|)
block|{
name|model
operator|.
name|setVersion
argument_list|(
name|version
argument_list|)
expr_stmt|;
block|}
continue|continue;
block|}
name|String
name|groupId
init|=
name|between
argument_list|(
name|line
argument_list|,
literal|"<groupId>"
argument_list|,
literal|"</groupId>"
argument_list|)
decl_stmt|;
name|String
name|artifactId
init|=
name|between
argument_list|(
name|line
argument_list|,
literal|"<artifactId>"
argument_list|,
literal|"</artifactId>"
argument_list|)
decl_stmt|;
name|String
name|description
init|=
name|between
argument_list|(
name|line
argument_list|,
literal|"<description>"
argument_list|,
literal|"</description>"
argument_list|)
decl_stmt|;
if|if
condition|(
name|groupId
operator|!=
literal|null
operator|&&
name|model
operator|.
name|getGroupId
argument_list|()
operator|==
literal|null
condition|)
block|{
name|model
operator|.
name|setGroupId
argument_list|(
name|groupId
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|artifactId
operator|!=
literal|null
operator|&&
name|model
operator|.
name|getArtifactId
argument_list|()
operator|==
literal|null
condition|)
block|{
name|model
operator|.
name|setArtifactId
argument_list|(
name|artifactId
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|description
operator|!=
literal|null
operator|&&
name|model
operator|.
name|getDescription
argument_list|()
operator|==
literal|null
condition|)
block|{
name|model
operator|.
name|setDescription
argument_list|(
name|description
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|model
operator|.
name|getGroupId
argument_list|()
operator|!=
literal|null
operator|&&
name|model
operator|.
name|getArtifactId
argument_list|()
operator|!=
literal|null
operator|&&
name|model
operator|.
name|getVersion
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|models
operator|.
name|add
argument_list|(
name|model
argument_list|)
expr_stmt|;
block|}
block|}
name|log
operator|.
name|info
argument_list|(
literal|"Found "
operator|+
name|models
operator|.
name|size
argument_list|()
operator|+
literal|" archetypes"
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|models
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// make sure there is a dir
name|outDir
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
name|outDir
argument_list|,
literal|"archetype-catalog.xml"
argument_list|)
decl_stmt|;
name|FileOutputStream
name|fos
init|=
operator|new
name|FileOutputStream
argument_list|(
name|out
argument_list|,
literal|false
argument_list|)
decl_stmt|;
comment|// write top
name|String
name|top
init|=
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<archetype-catalog>\n<archetypes>"
decl_stmt|;
name|fos
operator|.
name|write
argument_list|(
name|top
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
comment|// write each archetype
for|for
control|(
name|ArchetypeModel
name|model
range|:
name|models
control|)
block|{
name|fos
operator|.
name|write
argument_list|(
literal|"\n<archetype>"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|fos
operator|.
name|write
argument_list|(
operator|(
literal|"\n<groupId>"
operator|+
name|model
operator|.
name|getGroupId
argument_list|()
operator|+
literal|"</groupId>"
operator|)
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|fos
operator|.
name|write
argument_list|(
operator|(
literal|"\n<artifactId>"
operator|+
name|model
operator|.
name|getArtifactId
argument_list|()
operator|+
literal|"</artifactId>"
operator|)
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|fos
operator|.
name|write
argument_list|(
operator|(
literal|"\n<version>"
operator|+
name|model
operator|.
name|getVersion
argument_list|()
operator|+
literal|"</version>"
operator|)
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|model
operator|.
name|getDescription
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|fos
operator|.
name|write
argument_list|(
operator|(
literal|"\n<description>"
operator|+
name|model
operator|.
name|getDescription
argument_list|()
operator|+
literal|"</description>"
operator|)
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|fos
operator|.
name|write
argument_list|(
literal|"\n</archetype>"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// write bottom
name|String
name|bottom
init|=
literal|"\n</archetypes>\n</archetype-catalog>\n"
decl_stmt|;
name|fos
operator|.
name|write
argument_list|(
name|bottom
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
name|log
operator|.
name|info
argument_list|(
literal|"Saved archetype catalog to file "
operator|+
name|out
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|ArchetypeModel
specifier|private
specifier|static
class|class
name|ArchetypeModel
block|{
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
DECL|field|description
specifier|private
name|String
name|description
decl_stmt|;
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
block|}
block|}
end_class

end_unit

