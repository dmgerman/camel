begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|netty
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
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|NettyComponentWithConfigurationTest
specifier|public
class|class
name|NettyComponentWithConfigurationTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testNettyComponentWithConfiguration ()
specifier|public
name|void
name|testNettyComponentWithConfiguration
parameter_list|()
throws|throws
name|Exception
block|{
name|NettyComponent
name|comp
init|=
name|context
operator|.
name|getComponent
argument_list|(
literal|"netty"
argument_list|,
name|NettyComponent
operator|.
name|class
argument_list|)
decl_stmt|;
name|NettyConfiguration
name|cfg
init|=
operator|new
name|NettyConfiguration
argument_list|()
decl_stmt|;
name|comp
operator|.
name|setConfiguration
argument_list|(
name|cfg
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|cfg
argument_list|,
name|comp
operator|.
name|getConfiguration
argument_list|()
argument_list|)
expr_stmt|;
name|NettyEndpoint
name|e1
init|=
operator|(
name|NettyEndpoint
operator|)
name|comp
operator|.
name|createEndpoint
argument_list|(
literal|"netty://tcp://localhost:4455"
argument_list|)
decl_stmt|;
name|NettyEndpoint
name|e2
init|=
operator|(
name|NettyEndpoint
operator|)
name|comp
operator|.
name|createEndpoint
argument_list|(
literal|"netty://tcp://localhost:5566?sync=false&needClientAuth=true"
argument_list|)
decl_stmt|;
comment|// should not be same
name|assertNotSame
argument_list|(
name|e1
argument_list|,
name|e2
argument_list|)
expr_stmt|;
name|assertNotSame
argument_list|(
name|e1
operator|.
name|getConfiguration
argument_list|()
argument_list|,
name|e2
operator|.
name|getConfiguration
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|e2
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getReceiveBufferSizePredictor
argument_list|()
argument_list|)
expr_stmt|;
name|e2
operator|.
name|getConfiguration
argument_list|()
operator|.
name|setReceiveBufferSizePredictor
argument_list|(
literal|1024
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1024
argument_list|,
name|e2
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getReceiveBufferSizePredictor
argument_list|()
argument_list|)
expr_stmt|;
name|e2
operator|.
name|getConfiguration
argument_list|()
operator|.
name|setPort
argument_list|(
literal|5566
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|e1
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isSync
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|e1
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isNeedClientAuth
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|e2
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isSync
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|e2
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isNeedClientAuth
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4455
argument_list|,
name|e1
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5566
argument_list|,
name|e2
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNettyComponentUdpWithConfiguration ()
specifier|public
name|void
name|testNettyComponentUdpWithConfiguration
parameter_list|()
throws|throws
name|Exception
block|{
name|NettyComponent
name|comp
init|=
name|context
operator|.
name|getComponent
argument_list|(
literal|"netty"
argument_list|,
name|NettyComponent
operator|.
name|class
argument_list|)
decl_stmt|;
name|NettyConfiguration
name|cfg
init|=
operator|new
name|NettyConfiguration
argument_list|()
decl_stmt|;
name|comp
operator|.
name|setConfiguration
argument_list|(
name|cfg
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|cfg
argument_list|,
name|comp
operator|.
name|getConfiguration
argument_list|()
argument_list|)
expr_stmt|;
name|NettyEndpoint
name|e1
init|=
operator|(
name|NettyEndpoint
operator|)
name|comp
operator|.
name|createEndpoint
argument_list|(
literal|"netty://udp://localhost:8601?sync=false"
argument_list|)
decl_stmt|;
name|NettyEndpoint
name|e2
init|=
operator|(
name|NettyEndpoint
operator|)
name|comp
operator|.
name|createEndpoint
argument_list|(
literal|"netty://udp://localhost:8602?sync=false&udpConnectionlessSending=true"
argument_list|)
decl_stmt|;
comment|// should not be same
name|assertNotSame
argument_list|(
name|e1
argument_list|,
name|e2
argument_list|)
expr_stmt|;
name|assertNotSame
argument_list|(
name|e1
operator|.
name|getConfiguration
argument_list|()
argument_list|,
name|e2
operator|.
name|getConfiguration
argument_list|()
argument_list|)
expr_stmt|;
comment|// both endpoints are sync=false
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|e1
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isSync
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|e2
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isSync
argument_list|()
argument_list|)
expr_stmt|;
comment|// if not set it should be false
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|e1
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isUdpConnectionlessSending
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|e2
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isUdpConnectionlessSending
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|8601
argument_list|,
name|e1
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|8602
argument_list|,
name|e2
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

