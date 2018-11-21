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
name|PackageComponentMojo
operator|.
name|prepareComponent
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
name|PackageDataFormatMojo
operator|.
name|prepareDataFormat
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
name|PackageLanguageMojo
operator|.
name|prepareLanguage
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
name|PackageOtherMojo
operator|.
name|prepareOthers
import|;
end_import

begin_comment
comment|/**  * Prepares a Camel component analyzing if the maven module contains Camel  *<ul>  *<li>components</li>  *<li>dataformats</li>  *<li>languages</li>  *<li>others</li>  *</ul>  * And for each of those generates extra descriptors and schema files for easier auto-discovery in Camel and tooling.  *  * @goal prepare-components  * @threadSafe  */
end_comment

begin_class
DECL|class|PrepareComponentMojo
specifier|public
class|class
name|PrepareComponentMojo
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
DECL|field|componentOutDir
specifier|protected
name|File
name|componentOutDir
decl_stmt|;
comment|/**      * The output directory for generated dataformats file      *      * @parameter default-value="${project.build.directory}/generated/camel/dataformats"      */
DECL|field|dataFormatOutDir
specifier|protected
name|File
name|dataFormatOutDir
decl_stmt|;
comment|/**      * The output directory for generated languages file      *      * @parameter default-value="${project.build.directory}/generated/camel/languages"      */
DECL|field|languageOutDir
specifier|protected
name|File
name|languageOutDir
decl_stmt|;
comment|/**      * The output directory for generated others file      *      * @parameter default-value="${project.build.directory}/generated/camel/others"      */
DECL|field|otherOutDir
specifier|protected
name|File
name|otherOutDir
decl_stmt|;
comment|/**      * The output directory for generated schema file      *      * @parameter default-value="${project.build.directory}/classes"      */
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
comment|/**      * build context to check changed files and mark them for refresh      * (used for m2e compatibility)      *       * @component      * @readonly      */
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
name|prepareComponent
argument_list|(
name|getLog
argument_list|()
argument_list|,
name|project
argument_list|,
name|projectHelper
argument_list|,
name|componentOutDir
argument_list|,
name|buildContext
argument_list|)
expr_stmt|;
name|prepareDataFormat
argument_list|(
name|getLog
argument_list|()
argument_list|,
name|project
argument_list|,
name|projectHelper
argument_list|,
name|dataFormatOutDir
argument_list|,
name|schemaOutDir
argument_list|,
name|buildContext
argument_list|)
expr_stmt|;
name|prepareLanguage
argument_list|(
name|getLog
argument_list|()
argument_list|,
name|project
argument_list|,
name|projectHelper
argument_list|,
name|languageOutDir
argument_list|,
name|schemaOutDir
argument_list|,
name|buildContext
argument_list|)
expr_stmt|;
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
block|}
end_class

end_unit

