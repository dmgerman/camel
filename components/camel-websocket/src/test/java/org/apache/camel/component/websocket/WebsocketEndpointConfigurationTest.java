begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.websocket
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|websocket
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
name|Consumer
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
name|component
operator|.
name|websocket
operator|.
name|WebsocketComponent
operator|.
name|ConnectorRef
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
name|eclipse
operator|.
name|jetty
operator|.
name|servlet
operator|.
name|ServletContextHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|util
operator|.
name|thread
operator|.
name|QueuedThreadPool
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|websocket
operator|.
name|servlet
operator|.
name|WebSocketServletFactory
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
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|Mock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|junit
operator|.
name|MockitoJUnitRunner
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
annotation|@
name|RunWith
argument_list|(
name|MockitoJUnitRunner
operator|.
name|class
argument_list|)
DECL|class|WebsocketEndpointConfigurationTest
specifier|public
class|class
name|WebsocketEndpointConfigurationTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|port
specifier|private
name|int
name|port
decl_stmt|;
annotation|@
name|Mock
DECL|field|processor
specifier|private
name|Processor
name|processor
decl_stmt|;
annotation|@
name|Test
DECL|method|testSetServletInitalparameters ()
specifier|public
name|void
name|testSetServletInitalparameters
parameter_list|()
throws|throws
name|Exception
block|{
name|port
operator|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
expr_stmt|;
name|String
name|uri
init|=
literal|"websocket://localhost:"
operator|+
name|port
operator|+
literal|"/bar?bufferSize=25000&maxIdleTime=3000&maxTextMessageSize=500&maxBinaryMessageSize=550"
decl_stmt|;
name|WebsocketEndpoint
name|websocketEndpoint
init|=
operator|(
name|WebsocketEndpoint
operator|)
name|context
operator|.
name|getEndpoint
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|WebsocketComponent
name|component
init|=
name|websocketEndpoint
operator|.
name|getComponent
argument_list|()
decl_stmt|;
name|component
operator|.
name|setMinThreads
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|component
operator|.
name|setMaxThreads
argument_list|(
literal|25
argument_list|)
expr_stmt|;
name|Consumer
name|consumer
init|=
name|websocketEndpoint
operator|.
name|createConsumer
argument_list|(
name|processor
argument_list|)
decl_stmt|;
name|component
operator|.
name|connect
argument_list|(
operator|(
name|WebsocketProducerConsumer
operator|)
name|consumer
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|WebsocketConsumer
operator|.
name|class
argument_list|,
name|consumer
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
comment|// just check the servlet initial parameters
name|ConnectorRef
name|conector
init|=
name|WebsocketComponent
operator|.
name|getConnectors
argument_list|()
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|ServletContextHandler
name|context
init|=
operator|(
name|ServletContextHandler
operator|)
name|conector
operator|.
name|server
operator|.
name|getHandler
argument_list|()
decl_stmt|;
name|String
name|buffersize
init|=
name|context
operator|.
name|getInitParameter
argument_list|(
literal|"bufferSize"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Got a wrong buffersize"
argument_list|,
literal|"25000"
argument_list|,
name|buffersize
argument_list|)
expr_stmt|;
name|String
name|maxIdleTime
init|=
name|context
operator|.
name|getInitParameter
argument_list|(
literal|"maxIdleTime"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Got a wrong maxIdleTime"
argument_list|,
literal|"3000"
argument_list|,
name|maxIdleTime
argument_list|)
expr_stmt|;
name|String
name|maxTextMessageSize
init|=
name|context
operator|.
name|getInitParameter
argument_list|(
literal|"maxTextMessageSize"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Got a wrong maxTextMessageSize"
argument_list|,
literal|"500"
argument_list|,
name|maxTextMessageSize
argument_list|)
expr_stmt|;
name|String
name|maxBinaryMessageSize
init|=
name|context
operator|.
name|getInitParameter
argument_list|(
literal|"maxBinaryMessageSize"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Got a wrong maxBinaryMessageSize"
argument_list|,
literal|"550"
argument_list|,
name|maxBinaryMessageSize
argument_list|)
expr_stmt|;
name|WebSocketServletFactory
name|factory
init|=
operator|(
name|WebSocketServletFactory
operator|)
name|context
operator|.
name|getServletContext
argument_list|()
operator|.
name|getAttribute
argument_list|(
name|WebSocketServletFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|int
name|factoryBufferSize
init|=
name|factory
operator|.
name|getPolicy
argument_list|()
operator|.
name|getInputBufferSize
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Got a wrong buffersize"
argument_list|,
literal|25000
argument_list|,
name|factoryBufferSize
argument_list|)
expr_stmt|;
name|long
name|factoryMaxIdleTime
init|=
name|factory
operator|.
name|getPolicy
argument_list|()
operator|.
name|getIdleTimeout
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Got a wrong maxIdleTime"
argument_list|,
literal|3000
argument_list|,
name|factoryMaxIdleTime
argument_list|)
expr_stmt|;
name|int
name|factoryMaxTextMessageSize
init|=
name|factory
operator|.
name|getPolicy
argument_list|()
operator|.
name|getMaxTextMessageSize
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Got a wrong maxTextMessageSize"
argument_list|,
literal|500
argument_list|,
name|factoryMaxTextMessageSize
argument_list|)
expr_stmt|;
name|int
name|factoryMaxBinaryMessageSize
init|=
name|factory
operator|.
name|getPolicy
argument_list|()
operator|.
name|getMaxBinaryMessageSize
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Got a wrong maxBinaryMessageSize"
argument_list|,
literal|550
argument_list|,
name|factoryMaxBinaryMessageSize
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSetServletNoMinThreadsNoMaxThreadsNoThreadPool ()
specifier|public
name|void
name|testSetServletNoMinThreadsNoMaxThreadsNoThreadPool
parameter_list|()
throws|throws
name|Exception
block|{
name|assumeTrue
argument_list|(
literal|"At lease 18 CPUs available"
argument_list|,
literal|1
operator|+
name|Runtime
operator|.
name|getRuntime
argument_list|()
operator|.
name|availableProcessors
argument_list|()
operator|*
literal|2
operator|>=
literal|19
argument_list|)
expr_stmt|;
name|port
operator|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
expr_stmt|;
name|String
name|uri
init|=
literal|"websocket://localhost:"
operator|+
name|port
operator|+
literal|"/bar?bufferSize=25000&maxIdleTime=3000"
decl_stmt|;
name|WebsocketEndpoint
name|websocketEndpoint
init|=
operator|(
name|WebsocketEndpoint
operator|)
name|context
operator|.
name|getEndpoint
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|WebsocketComponent
name|component
init|=
name|websocketEndpoint
operator|.
name|getComponent
argument_list|()
decl_stmt|;
name|Consumer
name|consumer
init|=
name|websocketEndpoint
operator|.
name|createConsumer
argument_list|(
name|processor
argument_list|)
decl_stmt|;
name|component
operator|.
name|connect
argument_list|(
operator|(
name|WebsocketProducerConsumer
operator|)
name|consumer
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|WebsocketConsumer
operator|.
name|class
argument_list|,
name|consumer
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
comment|// just check the servlet initial parameters
name|ConnectorRef
name|conector
init|=
name|WebsocketComponent
operator|.
name|getConnectors
argument_list|()
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|ServletContextHandler
name|context
init|=
operator|(
name|ServletContextHandler
operator|)
name|conector
operator|.
name|server
operator|.
name|getHandler
argument_list|()
decl_stmt|;
name|String
name|buffersize
init|=
name|context
operator|.
name|getInitParameter
argument_list|(
literal|"bufferSize"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Got a wrong buffersize"
argument_list|,
literal|"25000"
argument_list|,
name|buffersize
argument_list|)
expr_stmt|;
name|String
name|maxIdleTime
init|=
name|context
operator|.
name|getInitParameter
argument_list|(
literal|"maxIdleTime"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Got a wrong maxIdleTime"
argument_list|,
literal|"3000"
argument_list|,
name|maxIdleTime
argument_list|)
expr_stmt|;
name|WebSocketServletFactory
name|factory
init|=
operator|(
name|WebSocketServletFactory
operator|)
name|context
operator|.
name|getServletContext
argument_list|()
operator|.
name|getAttribute
argument_list|(
name|WebSocketServletFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|int
name|factoryBufferSize
init|=
name|factory
operator|.
name|getPolicy
argument_list|()
operator|.
name|getInputBufferSize
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Got a wrong buffersize"
argument_list|,
literal|25000
argument_list|,
name|factoryBufferSize
argument_list|)
expr_stmt|;
name|long
name|factoryMaxIdleTime
init|=
name|factory
operator|.
name|getPolicy
argument_list|()
operator|.
name|getIdleTimeout
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Got a wrong maxIdleTime"
argument_list|,
literal|3000
argument_list|,
name|factoryMaxIdleTime
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSetServletThreadPool ()
specifier|public
name|void
name|testSetServletThreadPool
parameter_list|()
throws|throws
name|Exception
block|{
name|port
operator|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
expr_stmt|;
name|String
name|uri
init|=
literal|"websocket://localhost:"
operator|+
name|port
operator|+
literal|"/bar?bufferSize=25000&maxIdleTime=3000"
decl_stmt|;
name|WebsocketEndpoint
name|websocketEndpoint
init|=
operator|(
name|WebsocketEndpoint
operator|)
name|context
operator|.
name|getEndpoint
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|WebsocketComponent
name|component
init|=
name|websocketEndpoint
operator|.
name|getComponent
argument_list|()
decl_stmt|;
name|QueuedThreadPool
name|qtp
init|=
operator|new
name|QueuedThreadPool
argument_list|(
literal|25
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|component
operator|.
name|setThreadPool
argument_list|(
name|qtp
argument_list|)
expr_stmt|;
name|Consumer
name|consumer
init|=
name|websocketEndpoint
operator|.
name|createConsumer
argument_list|(
name|processor
argument_list|)
decl_stmt|;
name|component
operator|.
name|connect
argument_list|(
operator|(
name|WebsocketProducerConsumer
operator|)
name|consumer
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|WebsocketConsumer
operator|.
name|class
argument_list|,
name|consumer
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
comment|// just check the servlet initial parameters
name|ConnectorRef
name|conector
init|=
name|WebsocketComponent
operator|.
name|getConnectors
argument_list|()
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|ServletContextHandler
name|context
init|=
operator|(
name|ServletContextHandler
operator|)
name|conector
operator|.
name|server
operator|.
name|getHandler
argument_list|()
decl_stmt|;
name|String
name|buffersize
init|=
name|context
operator|.
name|getInitParameter
argument_list|(
literal|"bufferSize"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Got a wrong buffersize"
argument_list|,
literal|"25000"
argument_list|,
name|buffersize
argument_list|)
expr_stmt|;
name|String
name|maxIdleTime
init|=
name|context
operator|.
name|getInitParameter
argument_list|(
literal|"maxIdleTime"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Got a wrong maxIdleTime"
argument_list|,
literal|"3000"
argument_list|,
name|maxIdleTime
argument_list|)
expr_stmt|;
name|WebSocketServletFactory
name|factory
init|=
operator|(
name|WebSocketServletFactory
operator|)
name|context
operator|.
name|getServletContext
argument_list|()
operator|.
name|getAttribute
argument_list|(
name|WebSocketServletFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|int
name|factoryBufferSize
init|=
name|factory
operator|.
name|getPolicy
argument_list|()
operator|.
name|getInputBufferSize
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Got a wrong buffersize"
argument_list|,
literal|25000
argument_list|,
name|factoryBufferSize
argument_list|)
expr_stmt|;
name|long
name|factoryMaxIdleTime
init|=
name|factory
operator|.
name|getPolicy
argument_list|()
operator|.
name|getIdleTimeout
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Got a wrong maxIdleTime"
argument_list|,
literal|3000
argument_list|,
name|factoryMaxIdleTime
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

