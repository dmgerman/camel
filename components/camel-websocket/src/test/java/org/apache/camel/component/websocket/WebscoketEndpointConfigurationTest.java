begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|junit
operator|.
name|Test
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

begin_class
DECL|class|WebscoketEndpointConfigurationTest
specifier|public
class|class
name|WebscoketEndpointConfigurationTest
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
name|Override
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|port
operator|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|(
literal|16330
argument_list|)
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
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
name|String
name|uri
init|=
literal|"websocket://localhost:"
operator|+
name|port
operator|+
literal|"/bar?bufferSize=65000&maxIdleTime=3000"
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
literal|"Get a wrong buffersize"
argument_list|,
literal|"65000"
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
literal|"Get a worng maxIdleTime"
argument_list|,
literal|"3000"
argument_list|,
name|maxIdleTime
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

