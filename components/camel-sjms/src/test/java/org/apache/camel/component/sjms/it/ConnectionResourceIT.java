begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sjms.it
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|sjms
operator|.
name|it
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
name|javax
operator|.
name|jms
operator|.
name|Connection
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Session
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|activemq
operator|.
name|pool
operator|.
name|PooledConnectionFactory
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
name|component
operator|.
name|sjms
operator|.
name|SjmsComponent
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
name|sjms
operator|.
name|jms
operator|.
name|ConnectionResource
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
name|sjms
operator|.
name|support
operator|.
name|JmsTestSupport
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
name|util
operator|.
name|StopWatch
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
comment|/**  * Integration test that verifies we can replace the internal  * ConnectionFactoryResource with another provider.  */
end_comment

begin_class
DECL|class|ConnectionResourceIT
specifier|public
class|class
name|ConnectionResourceIT
extends|extends
name|JmsTestSupport
block|{
comment|/**      * Test method for      * {@link org.apache.commons.pool.ObjectPool#returnObject(java.lang.Object)}      * .      *       * @throws Exception      */
annotation|@
name|Test
DECL|method|testCreateConnections ()
specifier|public
name|void
name|testCreateConnections
parameter_list|()
throws|throws
name|Exception
block|{
name|ConnectionResource
name|pool
init|=
operator|new
name|AMQConnectionResource
argument_list|(
literal|"tcp://localhost:33333"
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|pool
argument_list|)
expr_stmt|;
name|Connection
name|connection
init|=
name|pool
operator|.
name|borrowConnection
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|connection
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|connection
operator|.
name|createSession
argument_list|(
literal|false
argument_list|,
name|Session
operator|.
name|AUTO_ACKNOWLEDGE
argument_list|)
argument_list|)
expr_stmt|;
name|pool
operator|.
name|returnConnection
argument_list|(
name|connection
argument_list|)
expr_stmt|;
name|Connection
name|connection2
init|=
name|pool
operator|.
name|borrowConnection
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|connection2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConnectionResourceRouter ()
specifier|public
name|void
name|testConnectionResourceRouter
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|100
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectsNoDuplicates
argument_list|(
name|body
argument_list|()
argument_list|)
expr_stmt|;
name|StopWatch
name|watch
init|=
operator|new
name|StopWatch
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
literal|100
condition|;
name|i
operator|++
control|)
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:start"
argument_list|,
literal|""
operator|+
name|i
argument_list|)
expr_stmt|;
block|}
comment|// just in case we run on slow boxes
name|assertMockEndpointsSatisfied
argument_list|(
literal|20
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Took "
operator|+
name|watch
operator|.
name|taken
argument_list|()
operator|+
literal|" ms. to process 100 messages request/reply over JMS"
argument_list|)
expr_stmt|;
block|}
comment|/*      * @see org.apache.camel.test.junit4.CamelTestSupport#createCamelContext()      * @return      * @throws Exception      */
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
name|CamelContext
name|camelContext
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|AMQConnectionResource
name|pool
init|=
operator|new
name|AMQConnectionResource
argument_list|(
literal|"tcp://localhost:33333"
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|SjmsComponent
name|component
init|=
operator|new
name|SjmsComponent
argument_list|()
decl_stmt|;
name|component
operator|.
name|setConnectionResource
argument_list|(
name|pool
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|addComponent
argument_list|(
literal|"sjms"
argument_list|,
name|component
argument_list|)
expr_stmt|;
return|return
name|camelContext
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
literal|"seda:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"sjms:queue:in.foo?namedReplyTo=out.bar&exchangePattern=InOut&producerCount=5"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"sjms:queue:in.foo?exchangePattern=InOut&consumerCount=20"
argument_list|)
operator|.
name|log
argument_list|(
literal|"Using ${threadName} to process ${body}"
argument_list|)
operator|.
name|transform
argument_list|(
name|body
argument_list|()
operator|.
name|prepend
argument_list|(
literal|"Bye "
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|AMQConnectionResource
specifier|public
class|class
name|AMQConnectionResource
implements|implements
name|ConnectionResource
block|{
DECL|field|pcf
specifier|private
name|PooledConnectionFactory
name|pcf
decl_stmt|;
DECL|method|AMQConnectionResource (String connectString, int maxConnections)
specifier|public
name|AMQConnectionResource
parameter_list|(
name|String
name|connectString
parameter_list|,
name|int
name|maxConnections
parameter_list|)
block|{
name|pcf
operator|=
operator|new
name|PooledConnectionFactory
argument_list|(
name|connectString
argument_list|)
expr_stmt|;
name|pcf
operator|.
name|setMaxConnections
argument_list|(
name|maxConnections
argument_list|)
expr_stmt|;
name|pcf
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
block|{
name|pcf
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|borrowConnection ()
specifier|public
name|Connection
name|borrowConnection
parameter_list|()
throws|throws
name|Exception
block|{
name|Connection
name|answer
init|=
name|pcf
operator|.
name|createConnection
argument_list|()
decl_stmt|;
name|answer
operator|.
name|start
argument_list|()
expr_stmt|;
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|returnConnection (Connection connection)
specifier|public
name|void
name|returnConnection
parameter_list|(
name|Connection
name|connection
parameter_list|)
throws|throws
name|Exception
block|{
comment|// Do nothing in this case since the PooledConnectionFactory takes
comment|// care of this for us
name|log
operator|.
name|info
argument_list|(
literal|"Connection returned"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

