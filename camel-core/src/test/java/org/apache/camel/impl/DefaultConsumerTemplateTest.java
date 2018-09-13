begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
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
name|ConsumerTemplate
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
name|RuntimeCamelException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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
name|awaitility
operator|.
name|Awaitility
operator|.
name|await
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|DefaultConsumerTemplateTest
specifier|public
class|class
name|DefaultConsumerTemplateTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|consumer
specifier|private
name|DefaultConsumerTemplate
name|consumer
decl_stmt|;
annotation|@
name|Override
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|consumer
operator|=
operator|new
name|DefaultConsumerTemplate
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|After
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|consumer
operator|.
name|stop
argument_list|()
expr_stmt|;
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConsumeReceive ()
specifier|public
name|void
name|testConsumeReceive
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:foo"
argument_list|,
literal|"Hello"
argument_list|)
expr_stmt|;
name|Exchange
name|out
init|=
name|consumer
operator|.
name|receive
argument_list|(
literal|"seda:foo"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello"
argument_list|,
name|out
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|context
argument_list|,
name|consumer
operator|.
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConsumeTwiceReceive ()
specifier|public
name|void
name|testConsumeTwiceReceive
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:foo"
argument_list|,
literal|"Hello"
argument_list|)
expr_stmt|;
name|Exchange
name|out
init|=
name|consumer
operator|.
name|receive
argument_list|(
literal|"seda:foo"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello"
argument_list|,
name|out
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:foo"
argument_list|,
literal|"Bye"
argument_list|)
expr_stmt|;
name|out
operator|=
name|consumer
operator|.
name|receive
argument_list|(
literal|"seda:foo"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Bye"
argument_list|,
name|out
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConsumeReceiveNoWait ()
specifier|public
name|void
name|testConsumeReceiveNoWait
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|out
init|=
name|consumer
operator|.
name|receiveNoWait
argument_list|(
literal|"seda:foo"
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:foo"
argument_list|,
literal|"Hello"
argument_list|)
expr_stmt|;
name|await
argument_list|()
operator|.
name|atMost
argument_list|(
literal|1
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
operator|.
name|until
argument_list|(
parameter_list|()
lambda|->
block|{
name|Exchange
name|foo
init|=
name|consumer
operator|.
name|receiveNoWait
argument_list|(
literal|"seda:foo"
argument_list|)
decl_stmt|;
if|if
condition|(
name|foo
operator|!=
literal|null
condition|)
block|{
name|assertEquals
argument_list|(
literal|"Hello"
argument_list|,
name|foo
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|foo
operator|!=
literal|null
return|;
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConsumeReceiveTimeout ()
specifier|public
name|void
name|testConsumeReceiveTimeout
parameter_list|()
throws|throws
name|Exception
block|{
name|long
name|start
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|Exchange
name|out
init|=
name|consumer
operator|.
name|receive
argument_list|(
literal|"seda:foo"
argument_list|,
literal|1000
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|long
name|delta
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|-
name|start
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Should take about 1 sec: "
operator|+
name|delta
argument_list|,
name|delta
operator|<
literal|1500
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:foo"
argument_list|,
literal|"Hello"
argument_list|)
expr_stmt|;
name|out
operator|=
name|consumer
operator|.
name|receive
argument_list|(
literal|"seda:foo"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello"
argument_list|,
name|out
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConsumeReceiveBody ()
specifier|public
name|void
name|testConsumeReceiveBody
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:foo"
argument_list|,
literal|"Hello"
argument_list|)
expr_stmt|;
name|Object
name|body
init|=
name|consumer
operator|.
name|receiveBody
argument_list|(
literal|"seda:foo"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hello"
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConsumeTwiceReceiveBody ()
specifier|public
name|void
name|testConsumeTwiceReceiveBody
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:foo"
argument_list|,
literal|"Hello"
argument_list|)
expr_stmt|;
name|Object
name|body
init|=
name|consumer
operator|.
name|receiveBody
argument_list|(
literal|"seda:foo"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hello"
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:foo"
argument_list|,
literal|"Bye"
argument_list|)
expr_stmt|;
name|body
operator|=
name|consumer
operator|.
name|receiveBody
argument_list|(
literal|"seda:foo"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Bye"
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConsumeReceiveBodyNoWait ()
specifier|public
name|void
name|testConsumeReceiveBodyNoWait
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
name|body
init|=
name|consumer
operator|.
name|receiveBodyNoWait
argument_list|(
literal|"seda:foo"
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:foo"
argument_list|,
literal|"Hello"
argument_list|)
expr_stmt|;
name|await
argument_list|()
operator|.
name|atMost
argument_list|(
literal|1
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
operator|.
name|untilAsserted
argument_list|(
parameter_list|()
lambda|->
block|{
name|Object
name|foo
init|=
name|consumer
operator|.
name|receiveBodyNoWait
argument_list|(
literal|"seda:foo"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hello"
argument_list|,
name|foo
argument_list|)
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConsumeReceiveBodyString ()
specifier|public
name|void
name|testConsumeReceiveBodyString
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:foo"
argument_list|,
literal|"Hello"
argument_list|)
expr_stmt|;
name|String
name|body
init|=
name|consumer
operator|.
name|receiveBody
argument_list|(
literal|"seda:foo"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hello"
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConsumeTwiceReceiveBodyString ()
specifier|public
name|void
name|testConsumeTwiceReceiveBodyString
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:foo"
argument_list|,
literal|"Hello"
argument_list|)
expr_stmt|;
name|String
name|body
init|=
name|consumer
operator|.
name|receiveBody
argument_list|(
literal|"seda:foo"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hello"
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:foo"
argument_list|,
literal|"Bye"
argument_list|)
expr_stmt|;
name|body
operator|=
name|consumer
operator|.
name|receiveBody
argument_list|(
literal|"seda:foo"
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Bye"
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConsumeReceiveBodyStringNoWait ()
specifier|public
name|void
name|testConsumeReceiveBodyStringNoWait
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|body
init|=
name|consumer
operator|.
name|receiveBodyNoWait
argument_list|(
literal|"seda:foo"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:foo"
argument_list|,
literal|"Hello"
argument_list|)
expr_stmt|;
name|await
argument_list|()
operator|.
name|atMost
argument_list|(
literal|1
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
operator|.
name|untilAsserted
argument_list|(
parameter_list|()
lambda|->
block|{
name|String
name|foo
init|=
name|consumer
operator|.
name|receiveBodyNoWait
argument_list|(
literal|"seda:foo"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hello"
argument_list|,
name|foo
argument_list|)
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConsumeReceiveEndpoint ()
specifier|public
name|void
name|testConsumeReceiveEndpoint
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:foo"
argument_list|,
literal|"Hello"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|consumer
operator|.
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"seda:foo"
argument_list|)
decl_stmt|;
name|Exchange
name|out
init|=
name|consumer
operator|.
name|receive
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hello"
argument_list|,
name|out
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConsumeReceiveEndpointTimeout ()
specifier|public
name|void
name|testConsumeReceiveEndpointTimeout
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:foo"
argument_list|,
literal|"Hello"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|consumer
operator|.
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"seda:foo"
argument_list|)
decl_stmt|;
name|Exchange
name|out
init|=
name|consumer
operator|.
name|receive
argument_list|(
name|endpoint
argument_list|,
literal|1000
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hello"
argument_list|,
name|out
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConsumeReceiveEndpointNoWait ()
specifier|public
name|void
name|testConsumeReceiveEndpointNoWait
parameter_list|()
throws|throws
name|Exception
block|{
name|assertNotNull
argument_list|(
name|consumer
operator|.
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"seda:foo"
argument_list|)
decl_stmt|;
name|Exchange
name|out
init|=
name|consumer
operator|.
name|receiveNoWait
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:foo"
argument_list|,
literal|"Hello"
argument_list|)
expr_stmt|;
name|await
argument_list|()
operator|.
name|atMost
argument_list|(
literal|1
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
operator|.
name|until
argument_list|(
parameter_list|()
lambda|->
block|{
name|Exchange
name|foo
init|=
name|consumer
operator|.
name|receiveNoWait
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
if|if
condition|(
name|foo
operator|!=
literal|null
condition|)
block|{
name|assertEquals
argument_list|(
literal|"Hello"
argument_list|,
name|foo
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|foo
operator|!=
literal|null
return|;
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConsumeReceiveEndpointBody ()
specifier|public
name|void
name|testConsumeReceiveEndpointBody
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:foo"
argument_list|,
literal|"Hello"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|consumer
operator|.
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"seda:foo"
argument_list|)
decl_stmt|;
name|Object
name|body
init|=
name|consumer
operator|.
name|receiveBody
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hello"
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConsumeReceiveEndpointBodyTimeout ()
specifier|public
name|void
name|testConsumeReceiveEndpointBodyTimeout
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:foo"
argument_list|,
literal|"Hello"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|consumer
operator|.
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"seda:foo"
argument_list|)
decl_stmt|;
name|Object
name|body
init|=
name|consumer
operator|.
name|receiveBody
argument_list|(
name|endpoint
argument_list|,
literal|1000
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hello"
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConsumeReceiveEndpointBodyType ()
specifier|public
name|void
name|testConsumeReceiveEndpointBodyType
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:foo"
argument_list|,
literal|"Hello"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|consumer
operator|.
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"seda:foo"
argument_list|)
decl_stmt|;
name|String
name|body
init|=
name|consumer
operator|.
name|receiveBody
argument_list|(
name|endpoint
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hello"
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConsumeReceiveEndpointBodyTimeoutType ()
specifier|public
name|void
name|testConsumeReceiveEndpointBodyTimeoutType
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:foo"
argument_list|,
literal|"Hello"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|consumer
operator|.
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"seda:foo"
argument_list|)
decl_stmt|;
name|String
name|body
init|=
name|consumer
operator|.
name|receiveBody
argument_list|(
name|endpoint
argument_list|,
literal|1000
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hello"
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConsumeReceiveBodyTimeoutType ()
specifier|public
name|void
name|testConsumeReceiveBodyTimeoutType
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:foo"
argument_list|,
literal|"Hello"
argument_list|)
expr_stmt|;
name|String
name|body
init|=
name|consumer
operator|.
name|receiveBody
argument_list|(
literal|"seda:foo"
argument_list|,
literal|1000
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hello"
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConsumeReceiveEndpointBodyTypeNoWait ()
specifier|public
name|void
name|testConsumeReceiveEndpointBodyTypeNoWait
parameter_list|()
throws|throws
name|Exception
block|{
name|assertNotNull
argument_list|(
name|consumer
operator|.
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"seda:foo"
argument_list|)
decl_stmt|;
name|String
name|out
init|=
name|consumer
operator|.
name|receiveBodyNoWait
argument_list|(
name|endpoint
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:foo"
argument_list|,
literal|"Hello"
argument_list|)
expr_stmt|;
name|await
argument_list|()
operator|.
name|atMost
argument_list|(
literal|1
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
operator|.
name|untilAsserted
argument_list|(
parameter_list|()
lambda|->
block|{
name|String
name|foo
init|=
name|consumer
operator|.
name|receiveBodyNoWait
argument_list|(
name|endpoint
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hello"
argument_list|,
name|foo
argument_list|)
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConsumeReceiveEndpointBodyNoWait ()
specifier|public
name|void
name|testConsumeReceiveEndpointBodyNoWait
parameter_list|()
throws|throws
name|Exception
block|{
name|assertNotNull
argument_list|(
name|consumer
operator|.
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"seda:foo"
argument_list|)
decl_stmt|;
name|Object
name|out
init|=
name|consumer
operator|.
name|receiveBodyNoWait
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:foo"
argument_list|,
literal|"Hello"
argument_list|)
expr_stmt|;
name|await
argument_list|()
operator|.
name|atMost
argument_list|(
literal|1
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
operator|.
name|untilAsserted
argument_list|(
parameter_list|()
lambda|->
block|{
name|Object
name|foo
init|=
name|consumer
operator|.
name|receiveBodyNoWait
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hello"
argument_list|,
name|foo
argument_list|)
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testReceiveException ()
specifier|public
name|void
name|testReceiveException
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|setException
argument_list|(
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Damn"
argument_list|)
argument_list|)
expr_stmt|;
name|Exchange
name|out
init|=
name|template
operator|.
name|send
argument_list|(
literal|"seda:foo"
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|out
operator|.
name|isFailed
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|out
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|consumer
operator|.
name|receiveBody
argument_list|(
literal|"seda:foo"
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeCamelException
name|e
parameter_list|)
block|{
name|assertIsInstanceOf
argument_list|(
name|IllegalArgumentException
operator|.
name|class
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Damn"
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testReceiveOut ()
specifier|public
name|void
name|testReceiveOut
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Bye World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|send
argument_list|(
literal|"seda:foo"
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|String
name|out
init|=
name|consumer
operator|.
name|receiveBody
argument_list|(
literal|"seda:foo"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Bye World"
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCacheConsumers ()
specifier|public
name|void
name|testCacheConsumers
parameter_list|()
throws|throws
name|Exception
block|{
name|ConsumerTemplate
name|template
init|=
operator|new
name|DefaultConsumerTemplate
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|template
operator|.
name|setMaximumCacheSize
argument_list|(
literal|500
argument_list|)
expr_stmt|;
name|template
operator|.
name|start
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Size should be 0"
argument_list|,
literal|0
argument_list|,
name|template
operator|.
name|getCurrentCacheSize
argument_list|()
argument_list|)
expr_stmt|;
comment|// test that we cache at most 500 consumers to avoid it eating to much memory
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|503
condition|;
name|i
operator|++
control|)
block|{
name|Endpoint
name|e
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"direct:queue:"
operator|+
name|i
argument_list|)
decl_stmt|;
name|template
operator|.
name|receiveNoWait
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
comment|// the eviction is async so force cleanup
name|template
operator|.
name|cleanUp
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Size should be 500"
argument_list|,
literal|500
argument_list|,
name|template
operator|.
name|getCurrentCacheSize
argument_list|()
argument_list|)
expr_stmt|;
name|template
operator|.
name|stop
argument_list|()
expr_stmt|;
comment|// should be 0
name|assertEquals
argument_list|(
literal|"Size should be 0"
argument_list|,
literal|0
argument_list|,
name|template
operator|.
name|getCurrentCacheSize
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCacheConsumersFromContext ()
specifier|public
name|void
name|testCacheConsumersFromContext
parameter_list|()
throws|throws
name|Exception
block|{
name|ConsumerTemplate
name|template
init|=
name|context
operator|.
name|createConsumerTemplate
argument_list|(
literal|500
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Size should be 0"
argument_list|,
literal|0
argument_list|,
name|template
operator|.
name|getCurrentCacheSize
argument_list|()
argument_list|)
expr_stmt|;
comment|// test that we cache at most 500 consumers to avoid it eating to much memory
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|503
condition|;
name|i
operator|++
control|)
block|{
name|Endpoint
name|e
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"direct:queue:"
operator|+
name|i
argument_list|)
decl_stmt|;
name|template
operator|.
name|receiveNoWait
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
comment|// the eviction is async so force cleanup
name|template
operator|.
name|cleanUp
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Size should be 500"
argument_list|,
literal|500
argument_list|,
name|template
operator|.
name|getCurrentCacheSize
argument_list|()
argument_list|)
expr_stmt|;
name|template
operator|.
name|stop
argument_list|()
expr_stmt|;
comment|// should be 0
name|assertEquals
argument_list|(
literal|"Size should be 0"
argument_list|,
literal|0
argument_list|,
name|template
operator|.
name|getCurrentCacheSize
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDoneUoW ()
specifier|public
name|void
name|testDoneUoW
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteDirectory
argument_list|(
literal|"target/foo"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file:target/foo"
argument_list|,
literal|"Hello World"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"hello.txt"
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|consumer
operator|.
name|receive
argument_list|(
literal|"file:target/foo?initialDelay=0&delay=10&delete=true"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
comment|// file should still exists
name|File
name|file
init|=
operator|new
name|File
argument_list|(
literal|"target/foo/hello.txt"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"File should exist "
operator|+
name|file
argument_list|,
name|file
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
comment|// done the exchange
name|consumer
operator|.
name|doneUoW
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"File should have been deleted "
operator|+
name|file
argument_list|,
name|file
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

