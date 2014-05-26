begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|IOException
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
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|artifact
operator|.
name|DependencyResolutionRequiredException
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
name|Model
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
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  * Tests {@link JavadocApiMethodGeneratorMojo}  */
end_comment

begin_class
DECL|class|JavadocApiMethodGeneratorMojoTest
specifier|public
class|class
name|JavadocApiMethodGeneratorMojoTest
extends|extends
name|AbstractGeneratorMojoTest
block|{
annotation|@
name|Test
DECL|method|testExecute ()
specifier|public
name|void
name|testExecute
parameter_list|()
throws|throws
name|IOException
throws|,
name|MojoFailureException
throws|,
name|MojoExecutionException
block|{
comment|// delete target file to begin
specifier|final
name|File
name|outFile
init|=
operator|new
name|File
argument_list|(
name|OUT_DIR
argument_list|,
name|AbstractGeneratorMojo
operator|.
name|OUT_PACKAGE
operator|.
name|replaceAll
argument_list|(
literal|"\\."
argument_list|,
literal|"/"
argument_list|)
operator|+
literal|"/VelocityEngineApiMethod.java"
argument_list|)
decl_stmt|;
if|if
condition|(
name|outFile
operator|.
name|exists
argument_list|()
condition|)
block|{
name|outFile
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
specifier|final
name|JavadocApiMethodGeneratorMojo
name|mojo
init|=
operator|new
name|JavadocApiMethodGeneratorMojo
argument_list|()
decl_stmt|;
name|mojo
operator|.
name|outDir
operator|=
operator|new
name|File
argument_list|(
name|OUT_DIR
argument_list|)
expr_stmt|;
name|mojo
operator|.
name|outPackage
operator|=
name|AbstractGeneratorMojo
operator|.
name|OUT_PACKAGE
expr_stmt|;
comment|// use VelocityEngine javadoc
name|mojo
operator|.
name|proxyClass
operator|=
name|VelocityEngine
operator|.
name|class
operator|.
name|getCanonicalName
argument_list|()
expr_stmt|;
name|mojo
operator|.
name|project
operator|=
operator|new
name|MavenProject
argument_list|(
operator|(
name|Model
operator|)
literal|null
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|List
name|getRuntimeClasspathElements
parameter_list|()
throws|throws
name|DependencyResolutionRequiredException
block|{
return|return
name|Collections
operator|.
name|EMPTY_LIST
return|;
block|}
block|}
expr_stmt|;
name|mojo
operator|.
name|excludePackages
operator|=
name|JavadocApiMethodGeneratorMojo
operator|.
name|DEFAULT_EXCLUDE_PACKAGES
expr_stmt|;
name|mojo
operator|.
name|execute
argument_list|()
expr_stmt|;
comment|// check target file was generated
name|assertExists
argument_list|(
name|outFile
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

