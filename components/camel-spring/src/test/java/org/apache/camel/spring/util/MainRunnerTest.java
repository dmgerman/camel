begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|util
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|TestSupport
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
comment|/**  * @version   */
end_comment

begin_class
DECL|class|MainRunnerTest
specifier|public
class|class
name|MainRunnerTest
extends|extends
name|TestSupport
block|{
annotation|@
name|Test
DECL|method|testMainRunner ()
specifier|public
name|void
name|testMainRunner
parameter_list|()
throws|throws
name|Exception
block|{
name|MainRunner
name|runner
init|=
operator|new
name|MainRunner
argument_list|()
decl_stmt|;
name|runner
operator|.
name|setDelay
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|runner
operator|.
name|setArgs
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"Hello"
block|,
literal|"World"
block|}
argument_list|)
expr_stmt|;
name|runner
operator|.
name|setMain
argument_list|(
name|MainRunnerTest
operator|.
name|class
argument_list|)
expr_stmt|;
name|runner
operator|.
name|afterPropertiesSet
argument_list|()
expr_stmt|;
name|runner
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
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
block|{
name|assertEquals
argument_list|(
literal|"Hello"
argument_list|,
name|args
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"World"
argument_list|,
name|args
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

