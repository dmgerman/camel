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
name|Map
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
name|assertEquals
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
name|assertFalse
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

begin_class
DECL|class|PackageHelperTest
specifier|public
class|class
name|PackageHelperTest
block|{
annotation|@
name|Test
DECL|method|testFileToString ()
specifier|public
name|void
name|testFileToString
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"dk19i21)@+#(OR"
argument_list|,
name|PackageHelper
operator|.
name|fileToString
argument_list|(
name|ResourceUtils
operator|.
name|getResourceAsFile
argument_list|(
literal|"filecontent/a.txt"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testFindJsonFiles ()
specifier|public
name|void
name|testFindJsonFiles
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|File
argument_list|>
name|jsonFiles
init|=
name|PackageHelper
operator|.
name|findJsonFiles
argument_list|(
name|ResourceUtils
operator|.
name|getResourceAsFile
argument_list|(
literal|"json"
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Files a.json must be found"
argument_list|,
name|jsonFiles
operator|.
name|containsKey
argument_list|(
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Files b.json must be found"
argument_list|,
name|jsonFiles
operator|.
name|containsKey
argument_list|(
literal|"b"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"File c.txt must not be found"
argument_list|,
name|jsonFiles
operator|.
name|containsKey
argument_list|(
literal|"c"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

