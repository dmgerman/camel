begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|StringHelper
operator|.
name|indentCollection
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
name|ValidateHelper
operator|.
name|asName
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
name|ValidateHelper
operator|.
name|validate
import|;
end_import

begin_comment
comment|/**  * Validate a Camel component analyzing if the meta-data files for  *<ul>  *<li>components</li>  *<li>dataformats</li>  *<li>languages</li>  *</ul>  * all contains the needed meta-data such as assigned labels, documentation for each option  */
end_comment

begin_class
annotation|@
name|Mojo
argument_list|(
name|name
operator|=
literal|"validate-components"
argument_list|,
name|threadSafe
operator|=
literal|true
argument_list|)
DECL|class|ValidateComponentMojo
specifier|public
class|class
name|ValidateComponentMojo
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
comment|/**      * Whether to validate if the components, data formats, and languages are properly documented and have all the needed details.      *      */
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|validate
specifier|protected
name|Boolean
name|validate
decl_stmt|;
comment|/**      * The output directory for generated components file      *      */
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"${project.build.directory}/classes/"
argument_list|)
DECL|field|outDir
specifier|protected
name|File
name|outDir
decl_stmt|;
comment|/**      * Maven ProjectHelper.      */
annotation|@
name|Component
DECL|field|projectHelper
specifier|private
name|MavenProjectHelper
name|projectHelper
decl_stmt|;
comment|/**      * build context to check changed files and mark them for refresh      * (used for m2e compatibility)      */
annotation|@
name|Component
DECL|field|buildContext
specifier|private
name|BuildContext
name|buildContext
decl_stmt|;
comment|/**      * Execute goal.      *      * @throws org.apache.maven.plugin.MojoExecutionException execution of the main class or one of the      *                                                        threads it generated failed.      * @throws org.apache.maven.plugin.MojoFailureException   something bad happened...      */
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
operator|!
name|validate
condition|)
block|{
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|"Validation disabled"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
specifier|final
name|Set
argument_list|<
name|File
argument_list|>
name|jsonFiles
init|=
operator|new
name|TreeSet
argument_list|<>
argument_list|()
decl_stmt|;
name|PackageHelper
operator|.
name|findJsonFiles
argument_list|(
name|outDir
argument_list|,
name|jsonFiles
argument_list|,
operator|new
name|CamelComponentsFileFilter
argument_list|()
argument_list|)
expr_stmt|;
name|boolean
name|failed
init|=
literal|false
decl_stmt|;
for|for
control|(
name|File
name|file
range|:
name|jsonFiles
control|)
block|{
specifier|final
name|String
name|name
init|=
name|asName
argument_list|(
name|file
argument_list|)
decl_stmt|;
specifier|final
name|ErrorDetail
name|detail
init|=
operator|new
name|ErrorDetail
argument_list|()
decl_stmt|;
name|getLog
argument_list|()
operator|.
name|debug
argument_list|(
literal|"Validating file "
operator|+
name|file
argument_list|)
expr_stmt|;
name|validate
argument_list|(
name|file
argument_list|,
name|detail
argument_list|)
expr_stmt|;
if|if
condition|(
name|detail
operator|.
name|hasErrors
argument_list|()
condition|)
block|{
name|failed
operator|=
literal|true
expr_stmt|;
name|getLog
argument_list|()
operator|.
name|warn
argument_list|(
literal|"The "
operator|+
name|detail
operator|.
name|getKind
argument_list|()
operator|+
literal|": "
operator|+
name|name
operator|+
literal|" has validation errors"
argument_list|)
expr_stmt|;
if|if
condition|(
name|detail
operator|.
name|isMissingDescription
argument_list|()
condition|)
block|{
name|getLog
argument_list|()
operator|.
name|warn
argument_list|(
literal|"Missing description on: "
operator|+
name|detail
operator|.
name|getKind
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|detail
operator|.
name|isMissingLabel
argument_list|()
condition|)
block|{
name|getLog
argument_list|()
operator|.
name|warn
argument_list|(
literal|"Missing label on: "
operator|+
name|detail
operator|.
name|getKind
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|detail
operator|.
name|isMissingSyntax
argument_list|()
condition|)
block|{
name|getLog
argument_list|()
operator|.
name|warn
argument_list|(
literal|"Missing syntax on endpoint"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|detail
operator|.
name|isMissingUriPath
argument_list|()
condition|)
block|{
name|getLog
argument_list|()
operator|.
name|warn
argument_list|(
literal|"Missing @UriPath on endpoint"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|detail
operator|.
name|getMissingComponentDocumentation
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|getLog
argument_list|()
operator|.
name|warn
argument_list|(
literal|"Missing component documentation for the following options:"
operator|+
name|indentCollection
argument_list|(
literal|"\n\t"
argument_list|,
name|detail
operator|.
name|getMissingComponentDocumentation
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|detail
operator|.
name|getMissingEndpointDocumentation
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|getLog
argument_list|()
operator|.
name|warn
argument_list|(
literal|"Missing endpoint documentation for the following options:"
operator|+
name|indentCollection
argument_list|(
literal|"\n\t"
argument_list|,
name|detail
operator|.
name|getMissingEndpointDocumentation
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|failed
condition|)
block|{
throw|throw
operator|new
name|MojoFailureException
argument_list|(
literal|"Validating failed, see errors above!"
argument_list|)
throw|;
block|}
else|else
block|{
name|getLog
argument_list|()
operator|.
name|info
argument_list|(
literal|"Validation complete"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|class|CamelComponentsFileFilter
specifier|private
class|class
name|CamelComponentsFileFilter
implements|implements
name|FileFilter
block|{
annotation|@
name|Override
DECL|method|accept (File pathname)
specifier|public
name|boolean
name|accept
parameter_list|(
name|File
name|pathname
parameter_list|)
block|{
if|if
condition|(
name|pathname
operator|.
name|isDirectory
argument_list|()
operator|&&
name|pathname
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"model"
argument_list|)
condition|)
block|{
comment|// do not check the camel-core model packages as there is no components there
return|return
literal|false
return|;
block|}
if|if
condition|(
name|pathname
operator|.
name|isFile
argument_list|()
operator|&&
name|pathname
operator|.
name|getName
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|".json"
argument_list|)
condition|)
block|{
comment|// must be a components json file
try|try
block|{
name|String
name|json
init|=
name|loadText
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|pathname
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|json
operator|!=
literal|null
operator|&&
name|json
operator|.
name|contains
argument_list|(
literal|"\"kind\": \"component\""
argument_list|)
return|;
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
return|return
name|pathname
operator|.
name|isDirectory
argument_list|()
operator|||
operator|(
name|pathname
operator|.
name|isFile
argument_list|()
operator|&&
name|pathname
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"component.properties"
argument_list|)
operator|)
return|;
block|}
block|}
block|}
end_class

end_unit

