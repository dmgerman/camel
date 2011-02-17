begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
package|;
end_package

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_comment
comment|/**  *  @version   */
end_comment

begin_class
DECL|class|RedeliveryPolicyDelayPatternTest
specifier|public
class|class
name|RedeliveryPolicyDelayPatternTest
extends|extends
name|TestCase
block|{
DECL|field|policy
specifier|private
name|RedeliveryPolicy
name|policy
init|=
operator|new
name|RedeliveryPolicy
argument_list|()
decl_stmt|;
DECL|method|testDelayPattern ()
specifier|public
name|void
name|testDelayPattern
parameter_list|()
throws|throws
name|Exception
block|{
name|policy
operator|.
name|setDelayPattern
argument_list|(
literal|"3:1000;5:3000;10:5000;20:10000"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|policy
operator|.
name|calculateRedeliveryDelay
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|policy
operator|.
name|calculateRedeliveryDelay
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|policy
operator|.
name|calculateRedeliveryDelay
argument_list|(
literal|0
argument_list|,
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1000
argument_list|,
name|policy
operator|.
name|calculateRedeliveryDelay
argument_list|(
literal|0
argument_list|,
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1000
argument_list|,
name|policy
operator|.
name|calculateRedeliveryDelay
argument_list|(
literal|0
argument_list|,
literal|4
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3000
argument_list|,
name|policy
operator|.
name|calculateRedeliveryDelay
argument_list|(
literal|0
argument_list|,
literal|5
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3000
argument_list|,
name|policy
operator|.
name|calculateRedeliveryDelay
argument_list|(
literal|0
argument_list|,
literal|5
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3000
argument_list|,
name|policy
operator|.
name|calculateRedeliveryDelay
argument_list|(
literal|0
argument_list|,
literal|6
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3000
argument_list|,
name|policy
operator|.
name|calculateRedeliveryDelay
argument_list|(
literal|0
argument_list|,
literal|7
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3000
argument_list|,
name|policy
operator|.
name|calculateRedeliveryDelay
argument_list|(
literal|0
argument_list|,
literal|8
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3000
argument_list|,
name|policy
operator|.
name|calculateRedeliveryDelay
argument_list|(
literal|0
argument_list|,
literal|9
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5000
argument_list|,
name|policy
operator|.
name|calculateRedeliveryDelay
argument_list|(
literal|0
argument_list|,
literal|10
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5000
argument_list|,
name|policy
operator|.
name|calculateRedeliveryDelay
argument_list|(
literal|0
argument_list|,
literal|11
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5000
argument_list|,
name|policy
operator|.
name|calculateRedeliveryDelay
argument_list|(
literal|0
argument_list|,
literal|15
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5000
argument_list|,
name|policy
operator|.
name|calculateRedeliveryDelay
argument_list|(
literal|0
argument_list|,
literal|19
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|10000
argument_list|,
name|policy
operator|.
name|calculateRedeliveryDelay
argument_list|(
literal|0
argument_list|,
literal|20
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|10000
argument_list|,
name|policy
operator|.
name|calculateRedeliveryDelay
argument_list|(
literal|0
argument_list|,
literal|21
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|10000
argument_list|,
name|policy
operator|.
name|calculateRedeliveryDelay
argument_list|(
literal|0
argument_list|,
literal|25
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|10000
argument_list|,
name|policy
operator|.
name|calculateRedeliveryDelay
argument_list|(
literal|0
argument_list|,
literal|50
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|10000
argument_list|,
name|policy
operator|.
name|calculateRedeliveryDelay
argument_list|(
literal|0
argument_list|,
literal|100
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

