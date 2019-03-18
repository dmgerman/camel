begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl.health
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|health
package|;
end_package

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|Duration
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
name|apache
operator|.
name|camel
operator|.
name|health
operator|.
name|HealthCheck
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
name|health
operator|.
name|HealthCheckResultBuilder
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
DECL|class|HealthCheckTest
specifier|public
class|class
name|HealthCheckTest
block|{
annotation|@
name|Test
DECL|method|testCheck ()
specifier|public
name|void
name|testCheck
parameter_list|()
throws|throws
name|Exception
block|{
name|MyHealthCheck
name|check
init|=
operator|new
name|MyHealthCheck
argument_list|()
decl_stmt|;
name|check
operator|.
name|setState
argument_list|(
name|HealthCheck
operator|.
name|State
operator|.
name|UP
argument_list|)
expr_stmt|;
name|HealthCheck
operator|.
name|Result
name|result
decl_stmt|;
name|result
operator|=
name|check
operator|.
name|call
argument_list|()
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|HealthCheck
operator|.
name|State
operator|.
name|UNKNOWN
argument_list|,
name|result
operator|.
name|getState
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|result
operator|.
name|getMessage
argument_list|()
operator|.
name|isPresent
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"Disabled"
argument_list|,
name|result
operator|.
name|getMessage
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|result
operator|.
name|getDetails
argument_list|()
operator|.
name|get
argument_list|(
name|AbstractHealthCheck
operator|.
name|CHECK_ENABLED
argument_list|)
argument_list|)
expr_stmt|;
name|check
operator|.
name|getConfiguration
argument_list|()
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|result
operator|=
name|check
operator|.
name|call
argument_list|()
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|HealthCheck
operator|.
name|State
operator|.
name|UP
argument_list|,
name|result
operator|.
name|getState
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|result
operator|.
name|getMessage
argument_list|()
operator|.
name|isPresent
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|result
operator|.
name|getDetails
argument_list|()
operator|.
name|containsKey
argument_list|(
name|AbstractHealthCheck
operator|.
name|CHECK_ENABLED
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInterval ()
specifier|public
name|void
name|testInterval
parameter_list|()
throws|throws
name|Exception
block|{
name|MyHealthCheck
name|check
init|=
operator|new
name|MyHealthCheck
argument_list|()
decl_stmt|;
name|check
operator|.
name|setState
argument_list|(
name|HealthCheck
operator|.
name|State
operator|.
name|UP
argument_list|)
expr_stmt|;
name|check
operator|.
name|getConfiguration
argument_list|()
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|check
operator|.
name|getConfiguration
argument_list|()
operator|.
name|setInterval
argument_list|(
name|Duration
operator|.
name|ofMillis
argument_list|(
literal|1000
argument_list|)
argument_list|)
expr_stmt|;
name|HealthCheck
operator|.
name|Result
name|result1
init|=
name|check
operator|.
name|call
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|HealthCheck
operator|.
name|State
operator|.
name|UP
argument_list|,
name|result1
operator|.
name|getState
argument_list|()
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|100
argument_list|)
expr_stmt|;
name|HealthCheck
operator|.
name|Result
name|result2
init|=
name|check
operator|.
name|call
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|HealthCheck
operator|.
name|State
operator|.
name|UP
argument_list|,
name|result2
operator|.
name|getState
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|result1
operator|.
name|getDetails
argument_list|()
operator|.
name|get
argument_list|(
name|AbstractHealthCheck
operator|.
name|INVOCATION_TIME
argument_list|)
argument_list|,
name|result2
operator|.
name|getDetails
argument_list|()
operator|.
name|get
argument_list|(
name|AbstractHealthCheck
operator|.
name|INVOCATION_TIME
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|result1
operator|.
name|getDetails
argument_list|()
operator|.
name|get
argument_list|(
name|AbstractHealthCheck
operator|.
name|INVOCATION_COUNT
argument_list|)
argument_list|,
name|result2
operator|.
name|getDetails
argument_list|()
operator|.
name|get
argument_list|(
name|AbstractHealthCheck
operator|.
name|INVOCATION_COUNT
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNotEquals
argument_list|(
name|check
operator|.
name|getMetaData
argument_list|()
operator|.
name|get
argument_list|(
name|AbstractHealthCheck
operator|.
name|INVOCATION_ATTEMPT_TIME
argument_list|)
argument_list|,
name|result2
operator|.
name|getDetails
argument_list|()
operator|.
name|get
argument_list|(
name|AbstractHealthCheck
operator|.
name|INVOCATION_TIME
argument_list|)
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|1250
argument_list|)
expr_stmt|;
name|HealthCheck
operator|.
name|Result
name|result3
init|=
name|check
operator|.
name|call
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|HealthCheck
operator|.
name|State
operator|.
name|UP
argument_list|,
name|result3
operator|.
name|getState
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNotEquals
argument_list|(
name|result2
operator|.
name|getDetails
argument_list|()
operator|.
name|get
argument_list|(
name|AbstractHealthCheck
operator|.
name|INVOCATION_TIME
argument_list|)
argument_list|,
name|result3
operator|.
name|getDetails
argument_list|()
operator|.
name|get
argument_list|(
name|AbstractHealthCheck
operator|.
name|INVOCATION_TIME
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNotEquals
argument_list|(
name|result2
operator|.
name|getDetails
argument_list|()
operator|.
name|get
argument_list|(
name|AbstractHealthCheck
operator|.
name|INVOCATION_COUNT
argument_list|)
argument_list|,
name|result3
operator|.
name|getDetails
argument_list|()
operator|.
name|get
argument_list|(
name|AbstractHealthCheck
operator|.
name|INVOCATION_COUNT
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|check
operator|.
name|getMetaData
argument_list|()
operator|.
name|get
argument_list|(
name|AbstractHealthCheck
operator|.
name|INVOCATION_ATTEMPT_TIME
argument_list|)
argument_list|,
name|result3
operator|.
name|getDetails
argument_list|()
operator|.
name|get
argument_list|(
name|AbstractHealthCheck
operator|.
name|INVOCATION_TIME
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testThreshold ()
specifier|public
name|void
name|testThreshold
parameter_list|()
throws|throws
name|Exception
block|{
name|MyHealthCheck
name|check
init|=
operator|new
name|MyHealthCheck
argument_list|()
decl_stmt|;
name|check
operator|.
name|setState
argument_list|(
name|HealthCheck
operator|.
name|State
operator|.
name|DOWN
argument_list|)
expr_stmt|;
name|check
operator|.
name|getConfiguration
argument_list|()
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|check
operator|.
name|getConfiguration
argument_list|()
operator|.
name|setFailureThreshold
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|HealthCheck
operator|.
name|Result
name|result
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|check
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getFailureThreshold
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|result
operator|=
name|check
operator|.
name|call
argument_list|()
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|HealthCheck
operator|.
name|State
operator|.
name|UP
argument_list|,
name|result
operator|.
name|getState
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|i
operator|+
literal|1
argument_list|,
name|result
operator|.
name|getDetails
argument_list|()
operator|.
name|get
argument_list|(
name|AbstractHealthCheck
operator|.
name|INVOCATION_COUNT
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|i
operator|+
literal|1
argument_list|,
name|result
operator|.
name|getDetails
argument_list|()
operator|.
name|get
argument_list|(
name|AbstractHealthCheck
operator|.
name|FAILURE_COUNT
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|Assert
operator|.
name|assertEquals
argument_list|(
name|HealthCheck
operator|.
name|State
operator|.
name|DOWN
argument_list|,
name|check
operator|.
name|call
argument_list|()
operator|.
name|getState
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testIntervalThreshold ()
specifier|public
name|void
name|testIntervalThreshold
parameter_list|()
throws|throws
name|Exception
block|{
name|MyHealthCheck
name|check
init|=
operator|new
name|MyHealthCheck
argument_list|()
decl_stmt|;
name|check
operator|.
name|setState
argument_list|(
name|HealthCheck
operator|.
name|State
operator|.
name|DOWN
argument_list|)
expr_stmt|;
name|check
operator|.
name|getConfiguration
argument_list|()
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|check
operator|.
name|getConfiguration
argument_list|()
operator|.
name|setInterval
argument_list|(
name|Duration
operator|.
name|ofMillis
argument_list|(
literal|500
argument_list|)
argument_list|)
expr_stmt|;
name|check
operator|.
name|getConfiguration
argument_list|()
operator|.
name|setFailureThreshold
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|HealthCheck
operator|.
name|Result
name|result
decl_stmt|;
name|int
name|icount
decl_stmt|;
name|int
name|fcount
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|check
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getFailureThreshold
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|result
operator|=
name|check
operator|.
name|call
argument_list|()
expr_stmt|;
name|icount
operator|=
operator|(
name|int
operator|)
name|result
operator|.
name|getDetails
argument_list|()
operator|.
name|get
argument_list|(
name|AbstractHealthCheck
operator|.
name|INVOCATION_COUNT
argument_list|)
expr_stmt|;
name|fcount
operator|=
operator|(
name|int
operator|)
name|result
operator|.
name|getDetails
argument_list|()
operator|.
name|get
argument_list|(
name|AbstractHealthCheck
operator|.
name|FAILURE_COUNT
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|HealthCheck
operator|.
name|State
operator|.
name|UP
argument_list|,
name|result
operator|.
name|getState
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|i
operator|+
literal|1
argument_list|,
name|icount
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|i
operator|+
literal|1
argument_list|,
name|fcount
argument_list|)
expr_stmt|;
name|result
operator|=
name|check
operator|.
name|call
argument_list|()
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|HealthCheck
operator|.
name|State
operator|.
name|UP
argument_list|,
name|result
operator|.
name|getState
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|icount
argument_list|,
name|result
operator|.
name|getDetails
argument_list|()
operator|.
name|get
argument_list|(
name|AbstractHealthCheck
operator|.
name|INVOCATION_COUNT
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|fcount
argument_list|,
name|result
operator|.
name|getDetails
argument_list|()
operator|.
name|get
argument_list|(
name|AbstractHealthCheck
operator|.
name|FAILURE_COUNT
argument_list|)
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|550
argument_list|)
expr_stmt|;
block|}
name|Assert
operator|.
name|assertEquals
argument_list|(
name|HealthCheck
operator|.
name|State
operator|.
name|DOWN
argument_list|,
name|check
operator|.
name|call
argument_list|()
operator|.
name|getState
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// ********************************
comment|//
comment|// ********************************
DECL|class|MyHealthCheck
specifier|private
class|class
name|MyHealthCheck
extends|extends
name|AbstractHealthCheck
block|{
DECL|field|state
specifier|private
name|HealthCheck
operator|.
name|State
name|state
decl_stmt|;
DECL|method|MyHealthCheck ()
name|MyHealthCheck
parameter_list|()
block|{
name|super
argument_list|(
literal|"my"
argument_list|)
expr_stmt|;
name|this
operator|.
name|state
operator|=
name|HealthCheck
operator|.
name|State
operator|.
name|UP
expr_stmt|;
block|}
DECL|method|setState (State state)
specifier|public
name|void
name|setState
parameter_list|(
name|State
name|state
parameter_list|)
block|{
name|this
operator|.
name|state
operator|=
name|state
expr_stmt|;
block|}
DECL|method|getState ()
specifier|public
name|State
name|getState
parameter_list|()
block|{
return|return
name|state
return|;
block|}
annotation|@
name|Override
DECL|method|doCall (HealthCheckResultBuilder builder, Map<String, Object> options)
specifier|public
name|void
name|doCall
parameter_list|(
name|HealthCheckResultBuilder
name|builder
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|options
parameter_list|)
block|{
name|builder
operator|.
name|state
argument_list|(
name|state
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

