begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jasypt
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jasypt
package|;
end_package

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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

begin_class
DECL|class|MainTest
specifier|public
class|class
name|MainTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
DECL|method|testMainShowOptions ()
specifier|public
name|void
name|testMainShowOptions
parameter_list|()
throws|throws
name|Exception
block|{
name|Main
operator|.
name|main
argument_list|(
operator|new
name|String
index|[]
block|{}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMainEncrypt ()
specifier|public
name|void
name|testMainEncrypt
parameter_list|()
throws|throws
name|Exception
block|{
name|Main
name|main
init|=
operator|new
name|Main
argument_list|()
decl_stmt|;
name|main
operator|.
name|run
argument_list|(
literal|"-c encrypt -p secret -i tiger"
operator|.
name|split
argument_list|(
literal|" "
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMainDecrypt ()
specifier|public
name|void
name|testMainDecrypt
parameter_list|()
throws|throws
name|Exception
block|{
name|Main
name|main
init|=
operator|new
name|Main
argument_list|()
decl_stmt|;
name|main
operator|.
name|run
argument_list|(
literal|"-c decrypt -p secret -i bsW9uV37gQ0QHFu7KO03Ww=="
operator|.
name|split
argument_list|(
literal|" "
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUnknownOption ()
specifier|public
name|void
name|testUnknownOption
parameter_list|()
throws|throws
name|Exception
block|{
name|Main
name|main
init|=
operator|new
name|Main
argument_list|()
decl_stmt|;
name|main
operator|.
name|run
argument_list|(
literal|"-c encrypt -xxx foo"
operator|.
name|split
argument_list|(
literal|" "
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMissingPassword ()
specifier|public
name|void
name|testMissingPassword
parameter_list|()
throws|throws
name|Exception
block|{
name|Main
name|main
init|=
operator|new
name|Main
argument_list|()
decl_stmt|;
name|main
operator|.
name|run
argument_list|(
literal|"-c encrypt -i tiger"
operator|.
name|split
argument_list|(
literal|" "
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMissingInput ()
specifier|public
name|void
name|testMissingInput
parameter_list|()
throws|throws
name|Exception
block|{
name|Main
name|main
init|=
operator|new
name|Main
argument_list|()
decl_stmt|;
name|main
operator|.
name|run
argument_list|(
literal|"-c encrypt -p secret"
operator|.
name|split
argument_list|(
literal|" "
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

