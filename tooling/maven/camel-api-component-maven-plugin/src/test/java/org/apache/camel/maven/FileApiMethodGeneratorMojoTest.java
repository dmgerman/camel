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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|test
operator|.
name|TestProxy
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
name|junit
operator|.
name|Test
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
name|assertTrue
import|;
end_import

begin_comment
comment|/**  * Tests {@link FileApiMethodGeneratorMojo}  */
end_comment

begin_class
DECL|class|FileApiMethodGeneratorMojoTest
specifier|public
class|class
name|FileApiMethodGeneratorMojoTest
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
name|PACKAGE_PATH
operator|+
literal|"TestProxyApiMethod.java"
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
name|FileApiMethodGeneratorMojo
name|mojo
init|=
operator|new
name|FileApiMethodGeneratorMojo
argument_list|()
decl_stmt|;
name|mojo
operator|.
name|substitutions
operator|=
operator|new
name|Substitution
index|[
literal|2
index|]
expr_stmt|;
name|mojo
operator|.
name|substitutions
index|[
literal|0
index|]
operator|=
operator|new
name|Substitution
argument_list|(
literal|".+"
argument_list|,
literal|"(.+)"
argument_list|,
literal|"java.util.List"
argument_list|,
literal|"$1List"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|mojo
operator|.
name|substitutions
index|[
literal|1
index|]
operator|=
operator|new
name|Substitution
argument_list|(
literal|".+"
argument_list|,
literal|"(.+)"
argument_list|,
literal|".*?(\\w++)\\[\\]"
argument_list|,
literal|"$1Array"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|configureSourceGeneratorMojo
argument_list|(
name|mojo
argument_list|)
expr_stmt|;
name|mojo
operator|.
name|proxyClass
operator|=
name|TestProxy
operator|.
name|class
operator|.
name|getCanonicalName
argument_list|()
expr_stmt|;
name|mojo
operator|.
name|signatureFile
operator|=
operator|new
name|File
argument_list|(
literal|"src/test/resources/test-proxy-signatures.txt"
argument_list|)
expr_stmt|;
comment|// exclude name2, and int times
name|mojo
operator|.
name|excludeConfigNames
operator|=
literal|"name2"
expr_stmt|;
name|mojo
operator|.
name|excludeConfigTypes
operator|=
literal|"int"
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

