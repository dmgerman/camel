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
name|assertNotNull
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

begin_class
DECL|class|XmlHelperTest
specifier|public
class|class
name|XmlHelperTest
block|{
annotation|@
name|Test
DECL|method|testBuildNamespaceAwareDocument ()
specifier|public
name|void
name|testBuildNamespaceAwareDocument
parameter_list|()
throws|throws
name|Exception
block|{
name|assertNotNull
argument_list|(
name|XmlHelper
operator|.
name|buildNamespaceAwareDocument
argument_list|(
name|ResourceUtils
operator|.
name|getResourceAsFile
argument_list|(
literal|"xmls/empty.xml"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testBuildTransformer ()
specifier|public
name|void
name|testBuildTransformer
parameter_list|()
throws|throws
name|Exception
block|{
name|assertNotNull
argument_list|(
name|XmlHelper
operator|.
name|buildTransformer
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testBuildXPath ()
specifier|public
name|void
name|testBuildXPath
parameter_list|()
throws|throws
name|Exception
block|{
name|assertNotNull
argument_list|(
name|XmlHelper
operator|.
name|buildXPath
argument_list|(
operator|new
name|CamelSpringNamespace
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testBuildXPathNullPointerExpected ()
specifier|public
name|void
name|testBuildXPathNullPointerExpected
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|XmlHelper
operator|.
name|buildXPath
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"NullPointerException expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|e
parameter_list|)
block|{
comment|// Expected.
block|}
block|}
block|}
end_class

end_unit

