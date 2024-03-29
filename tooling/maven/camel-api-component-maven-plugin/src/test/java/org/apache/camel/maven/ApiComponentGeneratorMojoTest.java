begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|velocity
operator|.
name|VelocityContext
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
comment|/**  * Tests {@link ApiComponentGeneratorMojo}  */
end_comment

begin_class
DECL|class|ApiComponentGeneratorMojoTest
specifier|public
class|class
name|ApiComponentGeneratorMojoTest
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
name|Exception
block|{
specifier|final
name|File
name|collectionFile
init|=
operator|new
name|File
argument_list|(
name|OUT_DIR
argument_list|,
name|PACKAGE_PATH
operator|+
name|COMPONENT_NAME
operator|+
literal|"ApiCollection.java"
argument_list|)
decl_stmt|;
comment|// delete target files to begin
name|collectionFile
operator|.
name|delete
argument_list|()
expr_stmt|;
specifier|final
name|ApiComponentGeneratorMojo
name|mojo
init|=
name|createGeneratorMojo
argument_list|()
decl_stmt|;
name|mojo
operator|.
name|execute
argument_list|()
expr_stmt|;
comment|// check target file was generated
name|assertExists
argument_list|(
name|collectionFile
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createGeneratorMojo ()
specifier|protected
name|ApiComponentGeneratorMojo
name|createGeneratorMojo
parameter_list|()
block|{
specifier|final
name|ApiComponentGeneratorMojo
name|mojo
init|=
operator|new
name|ApiComponentGeneratorMojo
argument_list|()
decl_stmt|;
name|configureSourceGeneratorMojo
argument_list|(
name|mojo
argument_list|)
expr_stmt|;
name|mojo
operator|.
name|apis
operator|=
operator|new
name|ApiProxy
index|[
literal|2
index|]
expr_stmt|;
name|mojo
operator|.
name|apis
index|[
literal|0
index|]
operator|=
operator|new
name|ApiProxy
argument_list|()
expr_stmt|;
name|mojo
operator|.
name|apis
index|[
literal|0
index|]
operator|.
name|setApiName
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
name|mojo
operator|.
name|apis
index|[
literal|0
index|]
operator|.
name|setProxyClass
argument_list|(
name|TestProxy
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|mojo
operator|.
name|apis
index|[
literal|0
index|]
operator|.
name|setFromSignatureFile
argument_list|(
operator|new
name|File
argument_list|(
literal|"src/test/resources/test-proxy-signatures.txt"
argument_list|)
argument_list|)
expr_stmt|;
name|Substitution
index|[]
name|substitutions
init|=
operator|new
name|Substitution
index|[
literal|2
index|]
decl_stmt|;
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
name|mojo
operator|.
name|apis
index|[
literal|0
index|]
operator|.
name|setSubstitutions
argument_list|(
name|substitutions
argument_list|)
expr_stmt|;
comment|// exclude name2, and int times
name|mojo
operator|.
name|apis
index|[
literal|0
index|]
operator|.
name|setExcludeConfigNames
argument_list|(
literal|"name2"
argument_list|)
expr_stmt|;
name|mojo
operator|.
name|apis
index|[
literal|0
index|]
operator|.
name|setExcludeConfigTypes
argument_list|(
literal|"int"
argument_list|)
expr_stmt|;
name|mojo
operator|.
name|apis
index|[
literal|0
index|]
operator|.
name|setNullableOptions
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"namesList"
block|}
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ApiMethodAlias
argument_list|>
name|aliases
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|aliases
operator|.
name|add
argument_list|(
operator|new
name|ApiMethodAlias
argument_list|(
literal|"get(.+)"
argument_list|,
literal|"$1"
argument_list|)
argument_list|)
expr_stmt|;
name|aliases
operator|.
name|add
argument_list|(
operator|new
name|ApiMethodAlias
argument_list|(
literal|"set(.+)"
argument_list|,
literal|"$1"
argument_list|)
argument_list|)
expr_stmt|;
name|mojo
operator|.
name|apis
index|[
literal|1
index|]
operator|=
operator|new
name|ApiProxy
argument_list|()
expr_stmt|;
name|mojo
operator|.
name|apis
index|[
literal|1
index|]
operator|.
name|setApiName
argument_list|(
literal|"velocity"
argument_list|)
expr_stmt|;
name|mojo
operator|.
name|apis
index|[
literal|1
index|]
operator|.
name|setProxyClass
argument_list|(
name|VelocityContext
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|mojo
operator|.
name|apis
index|[
literal|1
index|]
operator|.
name|setAliases
argument_list|(
name|aliases
argument_list|)
expr_stmt|;
name|Substitution
name|substitution
init|=
operator|new
name|Substitution
argument_list|(
literal|".*"
argument_list|,
literal|"key"
argument_list|,
literal|"java.lang.Object"
argument_list|,
literal|"applicationKey"
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|mojo
operator|.
name|apis
index|[
literal|1
index|]
operator|.
name|setSubstitutions
argument_list|(
operator|new
name|Substitution
index|[]
block|{
name|substitution
block|}
argument_list|)
expr_stmt|;
name|mojo
operator|.
name|apis
index|[
literal|1
index|]
operator|.
name|setExtraOptions
argument_list|(
operator|new
name|ExtraOption
index|[]
block|{
operator|new
name|ExtraOption
argument_list|(
literal|"java.util.Map<String, String>"
argument_list|,
literal|"extraMap"
argument_list|)
block|}
argument_list|)
expr_stmt|;
name|mojo
operator|.
name|extraOptions
operator|=
operator|new
name|ExtraOption
index|[
literal|1
index|]
expr_stmt|;
name|mojo
operator|.
name|extraOptions
index|[
literal|0
index|]
operator|=
operator|new
name|ExtraOption
argument_list|(
literal|"java.util.List<String>"
argument_list|,
literal|"extraStrings"
argument_list|)
expr_stmt|;
specifier|final
name|FromJavadoc
name|fromJavadoc
init|=
operator|new
name|FromJavadoc
argument_list|()
decl_stmt|;
name|fromJavadoc
operator|.
name|setExcludePackages
argument_list|(
name|JavadocApiMethodGeneratorMojo
operator|.
name|DEFAULT_EXCLUDE_PACKAGES
argument_list|)
expr_stmt|;
name|fromJavadoc
operator|.
name|setExcludeMethods
argument_list|(
literal|"clone|Current|internal|icache"
argument_list|)
expr_stmt|;
name|mojo
operator|.
name|apis
index|[
literal|1
index|]
operator|.
name|setFromJavadoc
argument_list|(
name|fromJavadoc
argument_list|)
expr_stmt|;
return|return
name|mojo
return|;
block|}
block|}
end_class

end_unit

