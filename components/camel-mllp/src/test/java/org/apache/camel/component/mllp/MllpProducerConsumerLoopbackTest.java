begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mllp
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mllp
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
name|CamelContext
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
name|EndpointInject
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
name|LoggingLevel
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
name|ProducerTemplate
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
name|component
operator|.
name|mock
operator|.
name|MockEndpoint
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
name|DefaultCamelContext
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
name|test
operator|.
name|AvailablePortFinder
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
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
name|test
operator|.
name|mllp
operator|.
name|Hl7TestMessageGenerator
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
name|test
operator|.
name|mllp
operator|.
name|PassthroughProcessor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|hamcrest
operator|.
name|CoreMatchers
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
name|BeforeClass
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
name|Assume
operator|.
name|assumeTrue
import|;
end_import

begin_class
DECL|class|MllpProducerConsumerLoopbackTest
specifier|public
class|class
name|MllpProducerConsumerLoopbackTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|mllpPort
name|int
name|mllpPort
init|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
decl_stmt|;
DECL|field|mllpHost
name|String
name|mllpHost
init|=
literal|"localhost"
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"direct://source"
argument_list|)
DECL|field|source
name|ProducerTemplate
name|source
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock://acknowledged"
argument_list|)
DECL|field|acknowledged
name|MockEndpoint
name|acknowledged
decl_stmt|;
annotation|@
name|BeforeClass
DECL|method|setUpClass ()
specifier|public
specifier|static
name|void
name|setUpClass
parameter_list|()
throws|throws
name|Exception
block|{
name|assumeTrue
argument_list|(
literal|"Skipping test running in CI server - Fails sometimes on CI server with address already in use"
argument_list|,
name|System
operator|.
name|getenv
argument_list|(
literal|"BUILD_ID"
argument_list|)
operator|==
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|DefaultCamelContext
name|context
init|=
operator|(
name|DefaultCamelContext
operator|)
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|setUseMDCLogging
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|context
operator|.
name|setName
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilders ()
specifier|protected
name|RouteBuilder
index|[]
name|createRouteBuilders
parameter_list|()
throws|throws
name|Exception
block|{
name|RouteBuilder
index|[]
name|builders
init|=
operator|new
name|RouteBuilder
index|[
literal|2
index|]
decl_stmt|;
name|builders
index|[
literal|0
index|]
operator|=
operator|new
name|RouteBuilder
argument_list|()
block|{
name|String
name|routeId
init|=
literal|"mllp-receiver"
decl_stmt|;
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|fromF
argument_list|(
literal|"mllp://%s:%d?autoAck=true&readTimeout=5000"
argument_list|,
name|mllpHost
argument_list|,
name|mllpPort
argument_list|)
operator|.
name|id
argument_list|(
name|routeId
argument_list|)
operator|.
name|convertBodyTo
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|acknowledged
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|PassthroughProcessor
argument_list|(
literal|"after send to result"
argument_list|)
argument_list|)
operator|.
name|log
argument_list|(
name|LoggingLevel
operator|.
name|DEBUG
argument_list|,
name|routeId
argument_list|,
literal|"Receiving: ${body}"
argument_list|)
expr_stmt|;
block|}
block|}
expr_stmt|;
name|builders
index|[
literal|1
index|]
operator|=
operator|new
name|RouteBuilder
argument_list|()
block|{
name|String
name|routeId
init|=
literal|"mllp-sender"
decl_stmt|;
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
name|source
operator|.
name|getDefaultEndpoint
argument_list|()
argument_list|)
operator|.
name|routeId
argument_list|(
name|routeId
argument_list|)
operator|.
name|log
argument_list|(
name|LoggingLevel
operator|.
name|DEBUG
argument_list|,
name|routeId
argument_list|,
literal|"Sending: ${body}"
argument_list|)
operator|.
name|toF
argument_list|(
literal|"mllp://%s:%d?readTimeout=5000"
argument_list|,
name|mllpHost
argument_list|,
name|mllpPort
argument_list|)
operator|.
name|setBody
argument_list|(
name|header
argument_list|(
name|MllpConstants
operator|.
name|MLLP_ACKNOWLEDGEMENT
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
expr_stmt|;
return|return
name|builders
return|;
block|}
annotation|@
name|Test
DECL|method|testLoopbackWithOneMessage ()
specifier|public
name|void
name|testLoopbackWithOneMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|testMessage
init|=
name|Hl7TestMessageGenerator
operator|.
name|generateMessage
argument_list|()
decl_stmt|;
name|acknowledged
operator|.
name|expectedBodiesReceived
argument_list|(
name|testMessage
argument_list|)
expr_stmt|;
name|String
name|acknowledgement
init|=
name|source
operator|.
name|requestBody
argument_list|(
operator|(
name|Object
operator|)
name|testMessage
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertThat
argument_list|(
literal|"Should be acknowledgment for message 1"
argument_list|,
name|acknowledgement
argument_list|,
name|CoreMatchers
operator|.
name|containsString
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"MSA|AA|00001"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|(
literal|60
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testLoopbackWithMultipleMessages ()
specifier|public
name|void
name|testLoopbackWithMultipleMessages
parameter_list|()
throws|throws
name|Exception
block|{
name|int
name|messageCount
init|=
literal|1000
decl_stmt|;
name|acknowledged
operator|.
name|expectedMessageCount
argument_list|(
name|messageCount
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<=
name|messageCount
condition|;
operator|++
name|i
control|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Processing message {}"
argument_list|,
name|i
argument_list|)
expr_stmt|;
name|String
name|testMessage
init|=
name|Hl7TestMessageGenerator
operator|.
name|generateMessage
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|acknowledged
operator|.
name|message
argument_list|(
name|i
operator|-
literal|1
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|isEqualTo
argument_list|(
name|testMessage
argument_list|)
expr_stmt|;
name|String
name|acknowledgement
init|=
name|source
operator|.
name|requestBody
argument_list|(
operator|(
name|Object
operator|)
name|testMessage
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertThat
argument_list|(
literal|"Should be acknowledgment for message "
operator|+
name|i
argument_list|,
name|acknowledgement
argument_list|,
name|CoreMatchers
operator|.
name|containsString
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"MSA|AA|%05d"
argument_list|,
name|i
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|assertMockEndpointsSatisfied
argument_list|(
literal|60
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

