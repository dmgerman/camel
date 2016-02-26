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
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Random
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ExecutorService
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
name|Channel
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
name|Exchange
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
name|Message
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
name|Processor
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
name|Route
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
name|EventDrivenConsumerRoute
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
name|util
operator|.
name|ServiceHelper
import|;
end_import

begin_class
DECL|class|StreamResequencerTest
specifier|public
class|class
name|StreamResequencerTest
extends|extends
name|ContextTestSupport
block|{
DECL|method|sendBodyAndHeader (String endpointUri, final Object body, final String headerName, final Object headerValue)
specifier|protected
name|void
name|sendBodyAndHeader
parameter_list|(
name|String
name|endpointUri
parameter_list|,
specifier|final
name|Object
name|body
parameter_list|,
specifier|final
name|String
name|headerName
parameter_list|,
specifier|final
name|Object
name|headerValue
parameter_list|)
block|{
name|template
operator|.
name|send
argument_list|(
name|endpointUri
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|in
operator|.
name|setBody
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|in
operator|.
name|setHeader
argument_list|(
name|headerName
argument_list|,
name|headerValue
argument_list|)
expr_stmt|;
name|in
operator|.
name|setHeader
argument_list|(
literal|"testCase"
argument_list|,
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|testSendMessagesInWrongOrderButReceiveThemInCorrectOrder ()
specifier|public
name|void
name|testSendMessagesInWrongOrderButReceiveThemInCorrectOrder
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"msg1"
argument_list|,
literal|"msg2"
argument_list|,
literal|"msg3"
argument_list|,
literal|"msg4"
argument_list|)
expr_stmt|;
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"msg4"
argument_list|,
literal|"seqnum"
argument_list|,
literal|4L
argument_list|)
expr_stmt|;
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"msg1"
argument_list|,
literal|"seqnum"
argument_list|,
literal|1L
argument_list|)
expr_stmt|;
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"msg3"
argument_list|,
literal|"seqnum"
argument_list|,
literal|3L
argument_list|)
expr_stmt|;
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"msg2"
argument_list|,
literal|"seqnum"
argument_list|,
literal|2L
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|testMultithreaded ()
specifier|public
name|void
name|testMultithreaded
parameter_list|()
throws|throws
name|Exception
block|{
name|int
name|numMessages
init|=
literal|100
decl_stmt|;
name|Object
index|[]
name|bodies
init|=
operator|new
name|Object
index|[
name|numMessages
index|]
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
name|numMessages
condition|;
name|i
operator|++
control|)
block|{
name|bodies
index|[
name|i
index|]
operator|=
literal|"msg"
operator|+
name|i
expr_stmt|;
block|}
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
name|bodies
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|setResultWaitTime
argument_list|(
literal|20000
argument_list|)
expr_stmt|;
name|ProducerTemplate
name|producerTemplate
init|=
name|context
operator|.
name|createProducerTemplate
argument_list|()
decl_stmt|;
name|ProducerTemplate
name|producerTemplate2
init|=
name|context
operator|.
name|createProducerTemplate
argument_list|()
decl_stmt|;
name|ExecutorService
name|service
init|=
name|context
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|newFixedThreadPool
argument_list|(
name|this
argument_list|,
name|getName
argument_list|()
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|service
operator|.
name|execute
argument_list|(
operator|new
name|Sender
argument_list|(
name|producerTemplate
argument_list|,
literal|0
argument_list|,
name|numMessages
argument_list|,
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|service
operator|.
name|execute
argument_list|(
operator|new
name|Sender
argument_list|(
name|producerTemplate2
argument_list|,
literal|1
argument_list|,
name|numMessages
argument_list|,
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|ServiceHelper
operator|.
name|stopServices
argument_list|(
name|producerTemplate
argument_list|,
name|producerTemplate2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|useJmx ()
specifier|protected
name|boolean
name|useJmx
parameter_list|()
block|{
name|boolean
name|enable
init|=
literal|"testStreamResequencerTypeWithJmx"
operator|.
name|equals
argument_list|(
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Going to {} JMX for the test {}"
argument_list|,
name|enable
condition|?
literal|"enable"
else|:
literal|"disable"
argument_list|,
name|getName
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|enable
return|;
block|}
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
comment|// START SNIPPET: example
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|resequence
argument_list|(
name|header
argument_list|(
literal|"seqnum"
argument_list|)
argument_list|)
operator|.
name|stream
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: example
block|}
block|}
return|;
block|}
DECL|method|testStreamResequencerTypeWithJmx ()
specifier|public
name|void
name|testStreamResequencerTypeWithJmx
parameter_list|()
throws|throws
name|Exception
block|{
name|doTestStreamResequencerType
argument_list|()
expr_stmt|;
block|}
DECL|method|testStreamResequencerTypeWithoutJmx ()
specifier|public
name|void
name|testStreamResequencerTypeWithoutJmx
parameter_list|()
throws|throws
name|Exception
block|{
name|doTestStreamResequencerType
argument_list|()
expr_stmt|;
block|}
DECL|method|doTestStreamResequencerType ()
specifier|protected
name|void
name|doTestStreamResequencerType
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Route
argument_list|>
name|list
init|=
name|getRouteList
argument_list|(
name|createRouteBuilder
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Number of routes created: "
operator|+
name|list
argument_list|,
literal|1
argument_list|,
name|list
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Route
name|route
init|=
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|EventDrivenConsumerRoute
name|consumerRoute
init|=
name|assertIsInstanceOf
argument_list|(
name|EventDrivenConsumerRoute
operator|.
name|class
argument_list|,
name|route
argument_list|)
decl_stmt|;
name|Channel
name|channel
init|=
name|unwrapChannel
argument_list|(
name|consumerRoute
operator|.
name|getProcessor
argument_list|()
argument_list|)
decl_stmt|;
name|assertIsInstanceOf
argument_list|(
name|DefaultErrorHandler
operator|.
name|class
argument_list|,
name|channel
operator|.
name|getErrorHandler
argument_list|()
argument_list|)
expr_stmt|;
name|assertIsInstanceOf
argument_list|(
name|StreamResequencer
operator|.
name|class
argument_list|,
name|channel
operator|.
name|getNextProcessor
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|class|Sender
specifier|private
specifier|static
class|class
name|Sender
implements|implements
name|Runnable
block|{
DECL|field|template
specifier|private
specifier|final
name|ProducerTemplate
name|template
decl_stmt|;
DECL|field|start
specifier|private
specifier|final
name|int
name|start
decl_stmt|;
DECL|field|end
specifier|private
specifier|final
name|int
name|end
decl_stmt|;
DECL|field|increment
specifier|private
specifier|final
name|int
name|increment
decl_stmt|;
DECL|field|random
specifier|private
specifier|final
name|Random
name|random
decl_stmt|;
DECL|method|Sender (ProducerTemplate template, int start, int end, int increment)
name|Sender
parameter_list|(
name|ProducerTemplate
name|template
parameter_list|,
name|int
name|start
parameter_list|,
name|int
name|end
parameter_list|,
name|int
name|increment
parameter_list|)
block|{
name|this
operator|.
name|template
operator|=
name|template
expr_stmt|;
name|this
operator|.
name|start
operator|=
name|start
expr_stmt|;
name|this
operator|.
name|end
operator|=
name|end
expr_stmt|;
name|this
operator|.
name|increment
operator|=
name|increment
expr_stmt|;
name|random
operator|=
operator|new
name|Random
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
for|for
control|(
name|long
name|i
init|=
name|start
init|;
name|i
operator|<
name|end
condition|;
name|i
operator|+=
name|increment
control|)
block|{
try|try
block|{
comment|// let's sleep randomly
name|Thread
operator|.
name|sleep
argument_list|(
name|random
operator|.
name|nextInt
argument_list|(
literal|20
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|interrupt
argument_list|()
expr_stmt|;
block|}
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"msg"
operator|+
name|i
argument_list|,
literal|"seqnum"
argument_list|,
name|i
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

