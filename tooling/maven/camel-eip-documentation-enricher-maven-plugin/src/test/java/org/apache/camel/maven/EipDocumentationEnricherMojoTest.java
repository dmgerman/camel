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
name|junit
operator|.
name|Before
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

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|Mock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|junit
operator|.
name|MockitoJUnitRunner
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|fail
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|when
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|MockitoJUnitRunner
operator|.
name|class
argument_list|)
DECL|class|EipDocumentationEnricherMojoTest
specifier|public
class|class
name|EipDocumentationEnricherMojoTest
block|{
DECL|field|eipDocumentationEnricherMojo
specifier|private
name|EipDocumentationEnricherMojo
name|eipDocumentationEnricherMojo
init|=
operator|new
name|EipDocumentationEnricherMojo
argument_list|()
decl_stmt|;
annotation|@
name|Mock
DECL|field|mockCamelCore
specifier|private
name|File
name|mockCamelCore
decl_stmt|;
annotation|@
name|Mock
DECL|field|mockInputSchema
specifier|private
name|File
name|mockInputSchema
decl_stmt|;
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|eipDocumentationEnricherMojo
operator|.
name|camelCoreDir
operator|=
name|mockCamelCore
expr_stmt|;
name|eipDocumentationEnricherMojo
operator|.
name|inputCamelSchemaFile
operator|=
name|mockInputSchema
expr_stmt|;
name|eipDocumentationEnricherMojo
operator|.
name|pathToModelDir
operator|=
literal|"sub/path"
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testExecuteCamelCoreDoesNotExist ()
specifier|public
name|void
name|testExecuteCamelCoreDoesNotExist
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|mockCamelCore
operator|.
name|exists
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|mockInputSchema
operator|.
name|exists
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|mockInputSchema
operator|.
name|isFile
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|true
argument_list|)
expr_stmt|;
try|try
block|{
name|eipDocumentationEnricherMojo
operator|.
name|execute
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Expected MojoExecutionException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MojoExecutionException
name|e
parameter_list|)
block|{
comment|// Expected.
block|}
block|}
annotation|@
name|Test
DECL|method|testExecuteCamelCoreIsNull ()
specifier|public
name|void
name|testExecuteCamelCoreIsNull
parameter_list|()
throws|throws
name|Exception
block|{
name|eipDocumentationEnricherMojo
operator|.
name|camelCoreDir
operator|=
literal|null
expr_stmt|;
name|when
argument_list|(
name|mockInputSchema
operator|.
name|exists
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|mockInputSchema
operator|.
name|isFile
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|true
argument_list|)
expr_stmt|;
try|try
block|{
name|eipDocumentationEnricherMojo
operator|.
name|execute
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Expected MojoExecutionException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MojoExecutionException
name|e
parameter_list|)
block|{
comment|// Expected.
block|}
block|}
annotation|@
name|Test
DECL|method|testExecuteCamelCoreIsNotADirectory ()
specifier|public
name|void
name|testExecuteCamelCoreIsNotADirectory
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|mockCamelCore
operator|.
name|exists
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|mockInputSchema
operator|.
name|exists
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|mockInputSchema
operator|.
name|isFile
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|true
argument_list|)
expr_stmt|;
try|try
block|{
name|eipDocumentationEnricherMojo
operator|.
name|execute
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Expected MojoExecutionException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MojoExecutionException
name|e
parameter_list|)
block|{
comment|// Expected.
block|}
block|}
annotation|@
name|Test
DECL|method|testExecuteInputCamelSchemaDoesNotExist ()
specifier|public
name|void
name|testExecuteInputCamelSchemaDoesNotExist
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|mockInputSchema
operator|.
name|exists
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|false
argument_list|)
expr_stmt|;
try|try
block|{
name|eipDocumentationEnricherMojo
operator|.
name|execute
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Expected MojoExecutionException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MojoExecutionException
name|e
parameter_list|)
block|{
comment|// Expected.
block|}
block|}
annotation|@
name|Test
DECL|method|testExecuteInputCamelSchemaIsNull ()
specifier|public
name|void
name|testExecuteInputCamelSchemaIsNull
parameter_list|()
throws|throws
name|Exception
block|{
name|eipDocumentationEnricherMojo
operator|.
name|inputCamelSchemaFile
operator|=
literal|null
expr_stmt|;
try|try
block|{
name|eipDocumentationEnricherMojo
operator|.
name|execute
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Expected MojoExecutionException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MojoExecutionException
name|e
parameter_list|)
block|{
comment|// Expected.
block|}
block|}
annotation|@
name|Test
DECL|method|testExecuteInputCamelSchemaIsNotAFile ()
specifier|public
name|void
name|testExecuteInputCamelSchemaIsNotAFile
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|mockInputSchema
operator|.
name|exists
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|mockInputSchema
operator|.
name|isFile
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|false
argument_list|)
expr_stmt|;
try|try
block|{
name|eipDocumentationEnricherMojo
operator|.
name|execute
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Expected MojoExecutionException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MojoExecutionException
name|e
parameter_list|)
block|{
comment|// Expected.
block|}
block|}
annotation|@
name|Test
DECL|method|testExecutePathToModelDirIsNull ()
specifier|public
name|void
name|testExecutePathToModelDirIsNull
parameter_list|()
throws|throws
name|Exception
block|{
name|eipDocumentationEnricherMojo
operator|.
name|pathToModelDir
operator|=
literal|null
expr_stmt|;
try|try
block|{
name|eipDocumentationEnricherMojo
operator|.
name|execute
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Expected MojoExecutionException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MojoExecutionException
name|e
parameter_list|)
block|{
comment|// Expected.
block|}
block|}
block|}
end_class

end_unit

