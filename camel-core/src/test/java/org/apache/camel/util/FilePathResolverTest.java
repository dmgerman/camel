begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
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
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
import|;
end_import

begin_class
DECL|class|FilePathResolverTest
specifier|public
class|class
name|FilePathResolverTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
DECL|method|testFilePathResolver ()
specifier|public
name|void
name|testFilePathResolver
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"/foo/bar"
argument_list|,
name|FilePathResolver
operator|.
name|resolvePath
argument_list|(
literal|"/foo/bar"
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|tmp
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.io.tmpdir"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|tmp
operator|+
literal|"foo"
argument_list|,
name|FilePathResolver
operator|.
name|resolvePath
argument_list|(
literal|"${java.io.tmpdir}foo"
argument_list|)
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"beer"
argument_list|,
literal|"Carlsberg"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|tmp
operator|+
literal|"foo/Carlsberg"
argument_list|,
name|FilePathResolver
operator|.
name|resolvePath
argument_list|(
literal|"${java.io.tmpdir}foo/${beer}"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

