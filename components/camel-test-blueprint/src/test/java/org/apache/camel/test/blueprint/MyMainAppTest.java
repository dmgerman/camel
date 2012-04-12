begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.test.blueprint
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|blueprint
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

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|MyMainAppTest
specifier|public
class|class
name|MyMainAppTest
block|{
DECL|method|main (String[] args)
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|Exception
block|{
name|MyMainAppTest
name|me
init|=
operator|new
name|MyMainAppTest
argument_list|()
decl_stmt|;
name|me
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMyMain ()
specifier|public
name|void
name|testMyMain
parameter_list|()
throws|throws
name|Exception
block|{
name|run
argument_list|()
expr_stmt|;
block|}
DECL|method|run ()
specifier|public
name|void
name|run
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
name|setBundleName
argument_list|(
literal|"MyMainBundle"
argument_list|)
expr_stmt|;
comment|// we support *.xml to find any blueprint xml files
name|main
operator|.
name|setDescriptors
argument_list|(
literal|"org/apache/camel/test/blueprint/xpath/*.xml"
argument_list|)
expr_stmt|;
name|main
operator|.
name|enableHangupSupport
argument_list|()
expr_stmt|;
comment|// run for 1 second and then stop automatic
name|main
operator|.
name|setDuration
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|main
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

