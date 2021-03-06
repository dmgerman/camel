begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.timer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|timer
package|;
end_package

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|SimpleDateFormat
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
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
name|ContextTestSupport
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
name|Endpoint
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
name|builder
operator|.
name|RouteBuilder
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
name|impl
operator|.
name|JndiRegistry
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
DECL|class|TimerReferenceConfigurationTest
specifier|public
class|class
name|TimerReferenceConfigurationTest
extends|extends
name|ContextTestSupport
block|{
comment|/**      * reference params      */
DECL|field|refExpectedTimeString
specifier|final
name|String
name|refExpectedTimeString
init|=
literal|"1972-12-11 19:55:00"
decl_stmt|;
DECL|field|refExpectedPattern
specifier|final
name|String
name|refExpectedPattern
init|=
literal|"yyyy-MM-dd HH:mm:ss"
decl_stmt|;
DECL|field|refExpectedPeriod
specifier|final
name|long
name|refExpectedPeriod
init|=
literal|500
decl_stmt|;
DECL|field|refExpectedDelay
specifier|final
name|long
name|refExpectedDelay
init|=
literal|100
decl_stmt|;
DECL|field|refExpectedFixedRate
specifier|final
name|boolean
name|refExpectedFixedRate
init|=
literal|true
decl_stmt|;
DECL|field|refExpectedDaemon
specifier|final
name|boolean
name|refExpectedDaemon
init|=
literal|false
decl_stmt|;
DECL|field|refExpectedRepeatCount
specifier|final
name|long
name|refExpectedRepeatCount
init|=
literal|11
decl_stmt|;
comment|/**      * value params      */
DECL|field|valExpectedTimeString
specifier|final
name|String
name|valExpectedTimeString
init|=
literal|"1970-04-17T18:07:41"
decl_stmt|;
DECL|field|valExpectedPattern
specifier|final
name|String
name|valExpectedPattern
init|=
literal|"yyyy-MM-dd'T'HH:mm:ss"
decl_stmt|;
DECL|field|valExpectedPeriod
specifier|final
name|long
name|valExpectedPeriod
init|=
literal|350
decl_stmt|;
DECL|field|valExpectedDelay
specifier|final
name|long
name|valExpectedDelay
init|=
literal|123
decl_stmt|;
DECL|field|valExpectedFixedRate
specifier|final
name|boolean
name|valExpectedFixedRate
init|=
literal|false
decl_stmt|;
DECL|field|valExpectedDaemon
specifier|final
name|boolean
name|valExpectedDaemon
init|=
literal|true
decl_stmt|;
DECL|field|valExpectedRepeatCount
specifier|final
name|long
name|valExpectedRepeatCount
init|=
literal|13
decl_stmt|;
DECL|field|refTimerUri
specifier|final
name|String
name|refTimerUri
init|=
literal|"timer://passByRefTimer?"
operator|+
literal|"time=#refExpectedTimeString"
operator|+
literal|"&pattern=#refExpectedPattern"
operator|+
literal|"&period=#refExpectedPeriod"
operator|+
literal|"&delay=#refExpectedDelay"
operator|+
literal|"&fixedRate=#refExpectedFixedRate"
operator|+
literal|"&daemon=#refExpectedDaemon"
operator|+
literal|"&repeatCount=#refExpectedRepeatCount"
decl_stmt|;
DECL|field|valueTimerUri
specifier|final
name|String
name|valueTimerUri
init|=
literal|"timer://passByValueTimer?"
operator|+
literal|"time="
operator|+
name|valExpectedTimeString
operator|+
literal|"&pattern="
operator|+
name|valExpectedPattern
operator|+
literal|"&period="
operator|+
name|valExpectedPeriod
operator|+
literal|"&delay="
operator|+
name|valExpectedDelay
operator|+
literal|"&fixedRate="
operator|+
name|valExpectedFixedRate
operator|+
literal|"&daemon="
operator|+
name|valExpectedDaemon
operator|+
literal|"&repeatCount="
operator|+
name|valExpectedRepeatCount
decl_stmt|;
DECL|field|mockEndpointUri
specifier|final
name|String
name|mockEndpointUri
init|=
literal|"mock:result"
decl_stmt|;
annotation|@
name|Override
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|reg
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|reg
operator|.
name|bind
argument_list|(
literal|"refExpectedTimeString"
argument_list|,
name|refExpectedTimeString
argument_list|)
expr_stmt|;
name|reg
operator|.
name|bind
argument_list|(
literal|"refExpectedPattern"
argument_list|,
name|refExpectedPattern
argument_list|)
expr_stmt|;
name|reg
operator|.
name|bind
argument_list|(
literal|"refExpectedPeriod"
argument_list|,
name|refExpectedPeriod
argument_list|)
expr_stmt|;
name|reg
operator|.
name|bind
argument_list|(
literal|"refExpectedDelay"
argument_list|,
name|refExpectedDelay
argument_list|)
expr_stmt|;
name|reg
operator|.
name|bind
argument_list|(
literal|"refExpectedFixedRate"
argument_list|,
name|refExpectedFixedRate
argument_list|)
expr_stmt|;
name|reg
operator|.
name|bind
argument_list|(
literal|"refExpectedDaemon"
argument_list|,
name|refExpectedDaemon
argument_list|)
expr_stmt|;
name|reg
operator|.
name|bind
argument_list|(
literal|"refExpectedRepeatCount"
argument_list|,
name|refExpectedRepeatCount
argument_list|)
expr_stmt|;
return|return
name|reg
return|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
name|refTimerUri
argument_list|)
operator|.
name|to
argument_list|(
name|mockEndpointUri
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|valueTimerUri
argument_list|)
operator|.
name|to
argument_list|(
name|mockEndpointUri
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
comment|/**      * Test that the reference configuration params are correct      */
annotation|@
name|Test
DECL|method|testReferenceConfiguration ()
specifier|public
name|void
name|testReferenceConfiguration
parameter_list|()
throws|throws
name|Exception
block|{
name|Endpoint
name|e
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
name|refTimerUri
argument_list|)
decl_stmt|;
name|TimerEndpoint
name|timer
init|=
operator|(
name|TimerEndpoint
operator|)
name|e
decl_stmt|;
specifier|final
name|Date
name|expectedTimeObject
init|=
operator|new
name|SimpleDateFormat
argument_list|(
name|refExpectedPattern
argument_list|)
operator|.
name|parse
argument_list|(
name|refExpectedTimeString
argument_list|)
decl_stmt|;
specifier|final
name|Date
name|time
init|=
name|timer
operator|.
name|getTime
argument_list|()
decl_stmt|;
specifier|final
name|long
name|period
init|=
name|timer
operator|.
name|getPeriod
argument_list|()
decl_stmt|;
specifier|final
name|long
name|delay
init|=
name|timer
operator|.
name|getDelay
argument_list|()
decl_stmt|;
specifier|final
name|boolean
name|fixedRate
init|=
name|timer
operator|.
name|isFixedRate
argument_list|()
decl_stmt|;
specifier|final
name|boolean
name|daemon
init|=
name|timer
operator|.
name|isDaemon
argument_list|()
decl_stmt|;
specifier|final
name|long
name|repeatCount
init|=
name|timer
operator|.
name|getRepeatCount
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|refExpectedDelay
argument_list|,
name|delay
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|refExpectedPeriod
argument_list|,
name|period
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedTimeObject
argument_list|,
name|time
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|refExpectedFixedRate
argument_list|,
name|fixedRate
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|refExpectedDaemon
argument_list|,
name|daemon
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|refExpectedRepeatCount
argument_list|,
name|repeatCount
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test that the 'value' configuration params are correct      */
annotation|@
name|Test
DECL|method|testValueConfiguration ()
specifier|public
name|void
name|testValueConfiguration
parameter_list|()
throws|throws
name|Exception
block|{
name|Endpoint
name|e
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
name|valueTimerUri
argument_list|)
decl_stmt|;
name|TimerEndpoint
name|timer
init|=
operator|(
name|TimerEndpoint
operator|)
name|e
decl_stmt|;
specifier|final
name|Date
name|expectedTimeObject
init|=
operator|new
name|SimpleDateFormat
argument_list|(
name|valExpectedPattern
argument_list|)
operator|.
name|parse
argument_list|(
name|valExpectedTimeString
argument_list|)
decl_stmt|;
specifier|final
name|Date
name|time
init|=
name|timer
operator|.
name|getTime
argument_list|()
decl_stmt|;
specifier|final
name|long
name|period
init|=
name|timer
operator|.
name|getPeriod
argument_list|()
decl_stmt|;
specifier|final
name|long
name|delay
init|=
name|timer
operator|.
name|getDelay
argument_list|()
decl_stmt|;
specifier|final
name|boolean
name|fixedRate
init|=
name|timer
operator|.
name|isFixedRate
argument_list|()
decl_stmt|;
specifier|final
name|boolean
name|daemon
init|=
name|timer
operator|.
name|isDaemon
argument_list|()
decl_stmt|;
specifier|final
name|long
name|repeatCount
init|=
name|timer
operator|.
name|getRepeatCount
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|valExpectedDelay
argument_list|,
name|delay
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|valExpectedPeriod
argument_list|,
name|period
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedTimeObject
argument_list|,
name|time
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|valExpectedFixedRate
argument_list|,
name|fixedRate
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|valExpectedDaemon
argument_list|,
name|daemon
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|valExpectedRepeatCount
argument_list|,
name|repeatCount
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

