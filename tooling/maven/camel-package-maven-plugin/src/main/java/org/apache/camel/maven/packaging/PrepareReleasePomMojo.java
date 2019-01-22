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
name|util
operator|.
name|HashSet
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
name|SortedSet
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
name|PackageHelper
operator|.
name|after
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
comment|/**  * Prepares the apache-camel/pom.xml and common-bin to keep the Camel artifacts up-to-date.  */
end_comment

begin_class
annotation|@
name|Mojo
argument_list|(
name|name
operator|=
literal|"prepare-release-pom"
argument_list|,
name|threadSafe
operator|=
literal|true
argument_list|)
DECL|class|PrepareReleasePomMojo
specifier|public
class|class
name|PrepareReleasePomMojo
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
comment|/**      * The apache-camel/pom      */
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"${project.build.directory}/../../../apache-camel/pom.xml"
argument_list|)
DECL|field|releasePom
specifier|protected
name|File
name|releasePom
decl_stmt|;
comment|/**      * The apache-camel/descriptors/common-bin.xml      */
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"${project.build.directory}/../../../apache-camel/src/main/descriptors/common-bin.xml"
argument_list|)
DECL|field|commonBinXml
specifier|protected
name|File
name|commonBinXml
decl_stmt|;
comment|/**      * The directory for components      */
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"${project.build.directory}/../../../components"
argument_list|)
DECL|field|componentsDir
specifier|protected
name|File
name|componentsDir
decl_stmt|;
comment|/**      * The directory for spring boot starters      */
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"${project.build.directory}/../../spring-boot/components-starter"
argument_list|)
DECL|field|startersDir
specifier|protected
name|File
name|startersDir
decl_stmt|;
comment|/**      * Maven ProjectHelper.      */
annotation|@
name|Component
DECL|field|projectHelper
specifier|private
name|MavenProjectHelper
name|projectHelper
decl_stmt|;
comment|/**      * Execute goal.      *      * @throws MojoExecutionException execution of the main class or one of the      *                                                        threads it generated failed.      * @throws MojoFailureException   something bad happened...      */
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
name|updatePomAndCommonBin
argument_list|(
name|componentsDir
argument_list|,
literal|"camel components"
argument_list|)
expr_stmt|;
name|updatePomAndCommonBin
argument_list|(
name|startersDir
argument_list|,
literal|"camel starters"
argument_list|)
expr_stmt|;
block|}
DECL|method|updatePomAndCommonBin (File dir, String token)
specifier|protected
name|void
name|updatePomAndCommonBin
parameter_list|(
name|File
name|dir
parameter_list|,
name|String
name|token
parameter_list|)
throws|throws
name|MojoExecutionException
throws|,
name|MojoFailureException
block|{
name|SortedSet
argument_list|<
name|String
argument_list|>
name|artifactIds
init|=
operator|new
name|TreeSet
argument_list|<>
argument_list|()
decl_stmt|;
try|try
block|{
name|Set
argument_list|<
name|File
argument_list|>
name|poms
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|findComponentPoms
argument_list|(
name|dir
argument_list|,
name|poms
argument_list|)
expr_stmt|;
for|for
control|(
name|File
name|pom
range|:
name|poms
control|)
block|{
name|String
name|aid
init|=
name|asArtifactId
argument_list|(
name|pom
argument_list|)
decl_stmt|;
if|if
condition|(
name|isValidArtifactId
argument_list|(
name|aid
argument_list|)
condition|)
block|{
name|artifactIds
operator|.
name|add
argument_list|(
name|aid
argument_list|)
expr_stmt|;
block|}
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
name|MojoFailureException
argument_list|(
literal|"Error due "
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
name|getLog
argument_list|()
operator|.
name|debug
argument_list|(
literal|"ArtifactIds: "
operator|+
name|artifactIds
argument_list|)
expr_stmt|;
comment|// update pom.xml
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|aid
range|:
name|artifactIds
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"<dependency>\n"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"<groupId>org.apache.camel</groupId>\n"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"<artifactId>"
operator|+
name|aid
operator|+
literal|"</artifactId>\n"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"<version>${project.version}</version>\n"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"</dependency>\n"
argument_list|)
expr_stmt|;
block|}
name|String
name|changed
init|=
name|sb
operator|.
name|toString
argument_list|()
decl_stmt|;
name|boolean
name|updated
init|=
name|updateXmlFile
argument_list|(
name|releasePom
argument_list|,
name|token
argument_list|,
name|changed
argument_list|,
literal|"    "
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
literal|"Updated apache-camel/pom.xml file"
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
literal|"No changes to apache-camel/pom.xml file"
argument_list|)
expr_stmt|;
block|}
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|"apache-camel/pom.xml contains "
operator|+
name|artifactIds
operator|.
name|size
argument_list|()
operator|+
literal|" "
operator|+
name|token
operator|+
literal|" dependencies"
argument_list|)
expr_stmt|;
comment|// update common-bin.xml
name|sb
operator|=
operator|new
name|StringBuilder
argument_list|()
expr_stmt|;
for|for
control|(
name|String
name|aid
range|:
name|artifactIds
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"<include>org.apache.camel:"
operator|+
name|aid
operator|+
literal|"</include>\n"
argument_list|)
expr_stmt|;
block|}
name|changed
operator|=
name|sb
operator|.
name|toString
argument_list|()
expr_stmt|;
name|updated
operator|=
name|updateXmlFile
argument_list|(
name|commonBinXml
argument_list|,
name|token
argument_list|,
name|changed
argument_list|,
literal|"        "
argument_list|)
expr_stmt|;
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
literal|"Updated apache-camel/src/main/descriptors/common-bin.xml file"
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
literal|"No changes to apache-camel/src/main/descriptors/common-bin.xml file"
argument_list|)
expr_stmt|;
block|}
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|"apache-camel/src/main/descriptors/common-bin.xml contains "
operator|+
name|artifactIds
operator|.
name|size
argument_list|()
operator|+
literal|" "
operator|+
name|token
operator|+
literal|" dependencies"
argument_list|)
expr_stmt|;
block|}
DECL|method|findComponentPoms (File parentDir, Set<File> components)
specifier|private
name|void
name|findComponentPoms
parameter_list|(
name|File
name|parentDir
parameter_list|,
name|Set
argument_list|<
name|File
argument_list|>
name|components
parameter_list|)
block|{
name|File
index|[]
name|files
init|=
name|parentDir
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
if|if
condition|(
name|file
operator|.
name|isDirectory
argument_list|()
operator|&&
name|file
operator|.
name|getName
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"camel-"
argument_list|)
condition|)
block|{
name|findComponentPoms
argument_list|(
name|file
argument_list|,
name|components
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|parentDir
operator|.
name|getName
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"camel-"
argument_list|)
operator|&&
name|file
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"pom.xml"
argument_list|)
condition|)
block|{
name|components
operator|.
name|add
argument_list|(
name|file
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|asArtifactId (File pom)
specifier|private
name|String
name|asArtifactId
parameter_list|(
name|File
name|pom
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|text
init|=
name|loadText
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|pom
argument_list|)
argument_list|)
decl_stmt|;
name|text
operator|=
name|after
argument_list|(
name|text
argument_list|,
literal|"</parent>"
argument_list|)
expr_stmt|;
if|if
condition|(
name|text
operator|!=
literal|null
condition|)
block|{
return|return
name|between
argument_list|(
name|text
argument_list|,
literal|"<artifactId>"
argument_list|,
literal|"</artifactId>"
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
DECL|method|isValidArtifactId (String aid)
specifier|private
name|boolean
name|isValidArtifactId
parameter_list|(
name|String
name|aid
parameter_list|)
block|{
return|return
name|aid
operator|!=
literal|null
operator|&&
operator|!
name|aid
operator|.
name|endsWith
argument_list|(
literal|"-maven-plugin"
argument_list|)
operator|&&
operator|!
name|aid
operator|.
name|endsWith
argument_list|(
literal|"-parent"
argument_list|)
return|;
block|}
DECL|method|updateXmlFile (File file, String token, String changed, String spaces)
specifier|private
name|boolean
name|updateXmlFile
parameter_list|(
name|File
name|file
parameter_list|,
name|String
name|token
parameter_list|,
name|String
name|changed
parameter_list|,
name|String
name|spaces
parameter_list|)
throws|throws
name|MojoExecutionException
block|{
name|String
name|start
init|=
literal|"<!-- "
operator|+
name|token
operator|+
literal|": START -->"
decl_stmt|;
name|String
name|end
init|=
literal|"<!-- "
operator|+
name|token
operator|+
literal|": END -->"
decl_stmt|;
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
name|between
argument_list|(
name|text
argument_list|,
name|start
argument_list|,
name|end
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
name|start
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
name|end
argument_list|)
decl_stmt|;
name|text
operator|=
name|before
operator|+
name|start
operator|+
literal|"\n"
operator|+
name|spaces
operator|+
name|changed
operator|+
literal|"\n"
operator|+
name|spaces
operator|+
name|end
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
block|}
end_class

end_unit

