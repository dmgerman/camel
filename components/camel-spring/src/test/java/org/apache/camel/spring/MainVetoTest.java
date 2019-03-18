begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
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
DECL|class|MainVetoTest
specifier|public
class|class
name|MainVetoTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
DECL|method|testMain ()
specifier|public
name|void
name|testMain
parameter_list|()
throws|throws
name|Exception
block|{
comment|// lets make a simple route
name|Main
name|main
init|=
operator|new
name|Main
argument_list|()
decl_stmt|;
name|main
operator|.
name|setDuration
argument_list|(
literal|30
argument_list|)
expr_stmt|;
name|main
operator|.
name|setDurationHitExitCode
argument_list|(
literal|99
argument_list|)
expr_stmt|;
name|main
operator|.
name|setApplicationContextUri
argument_list|(
literal|"org/apache/camel/spring/MainVetoTest.xml"
argument_list|)
expr_stmt|;
comment|// should not hang as we veto fail
name|main
operator|.
name|run
argument_list|()
expr_stmt|;
comment|// should complete normally due veto
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|main
operator|.
name|getExitCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

