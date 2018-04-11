begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.xray
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|aws
operator|.
name|xray
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
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
name|NotifyBuilder
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
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|equalTo
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|is
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
name|assertThat
import|;
end_import

begin_class
DECL|class|Route2ConcurrentTest
specifier|public
class|class
name|Route2ConcurrentTest
extends|extends
name|CamelAwsXRayTestSupport
block|{
DECL|method|Route2ConcurrentTest ()
specifier|public
name|Route2ConcurrentTest
parameter_list|()
block|{
name|super
argument_list|(
name|TestDataBuilder
operator|.
name|createTrace
argument_list|()
operator|.
name|inRandomOrder
argument_list|()
operator|.
name|withSegment
argument_list|(
name|TestDataBuilder
operator|.
name|createSegment
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
operator|.
name|withSegment
argument_list|(
name|TestDataBuilder
operator|.
name|createSegment
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
argument_list|,
name|TestDataBuilder
operator|.
name|createTrace
argument_list|()
operator|.
name|inRandomOrder
argument_list|()
operator|.
name|withSegment
argument_list|(
name|TestDataBuilder
operator|.
name|createSegment
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
operator|.
name|withSegment
argument_list|(
name|TestDataBuilder
operator|.
name|createSegment
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
argument_list|,
name|TestDataBuilder
operator|.
name|createTrace
argument_list|()
operator|.
name|inRandomOrder
argument_list|()
operator|.
name|withSegment
argument_list|(
name|TestDataBuilder
operator|.
name|createSegment
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
operator|.
name|withSegment
argument_list|(
name|TestDataBuilder
operator|.
name|createSegment
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
argument_list|,
name|TestDataBuilder
operator|.
name|createTrace
argument_list|()
operator|.
name|inRandomOrder
argument_list|()
operator|.
name|withSegment
argument_list|(
name|TestDataBuilder
operator|.
name|createSegment
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
operator|.
name|withSegment
argument_list|(
name|TestDataBuilder
operator|.
name|createSegment
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
argument_list|,
name|TestDataBuilder
operator|.
name|createTrace
argument_list|()
operator|.
name|inRandomOrder
argument_list|()
operator|.
name|withSegment
argument_list|(
name|TestDataBuilder
operator|.
name|createSegment
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
operator|.
name|withSegment
argument_list|(
name|TestDataBuilder
operator|.
name|createSegment
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConcurrentInvocationsOfRoute ()
specifier|public
name|void
name|testConcurrentInvocationsOfRoute
parameter_list|()
throws|throws
name|Exception
block|{
name|NotifyBuilder
name|notify
init|=
operator|new
name|NotifyBuilder
argument_list|(
name|context
argument_list|)
operator|.
name|whenDone
argument_list|(
literal|10
argument_list|)
operator|.
name|create
argument_list|()
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
literal|5
condition|;
name|i
operator|++
control|)
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:foo"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
block|}
name|assertThat
argument_list|(
literal|"Not all exchanges were fully processed"
argument_list|,
name|notify
operator|.
name|matches
argument_list|(
literal|10
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
argument_list|,
name|is
argument_list|(
name|equalTo
argument_list|(
literal|true
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|verify
argument_list|()
expr_stmt|;
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
literal|"seda:foo?concurrentConsumers=5"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|log
argument_list|(
literal|"routing at ${routeId}"
argument_list|)
operator|.
name|delay
argument_list|(
name|simple
argument_list|(
literal|"${random(1000,2000)}"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"seda:bar"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"seda:bar?concurrentConsumers=5"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"bar"
argument_list|)
operator|.
name|log
argument_list|(
literal|"routing at ${routeId}"
argument_list|)
operator|.
name|delay
argument_list|(
name|simple
argument_list|(
literal|"${random(0,500)}"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

