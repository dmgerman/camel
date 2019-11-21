begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.iota
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|iota
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
name|Ignore
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
DECL|class|IOTAProducerTest
specifier|public
class|class
name|IOTAProducerTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|SEED
specifier|private
specifier|static
specifier|final
name|String
name|SEED
init|=
literal|"IHDEENZYITYVYSPKAURUZAQKGVJEREFDJMYTANNXXGPZ9GJWTEOJJ9IPMXOGZNQLSNMFDSQOTZAEETUEA"
decl_stmt|;
DECL|field|ADDRESS
specifier|private
specifier|static
specifier|final
name|String
name|ADDRESS
init|=
literal|"LXQHWNY9CQOHPNMKFJFIJHGEPAENAOVFRDIBF99PPHDTWJDCGHLYETXT9NPUVSNKT9XDTDYNJKJCPQMZCCOZVXMTXC"
decl_stmt|;
DECL|field|IOTA_NODE_URL
specifier|private
specifier|static
specifier|final
name|String
name|IOTA_NODE_URL
init|=
literal|"https://nodes.thetangle.org:443"
decl_stmt|;
annotation|@
name|Ignore
annotation|@
name|Test
DECL|method|sendTransferTest ()
specifier|public
name|void
name|sendTransferTest
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|message
init|=
literal|"ILOVEAPACHECAMEL"
decl_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:iota-send-message-response"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMinimumMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:iota-send-message"
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|getNewAddressTest ()
specifier|public
name|void
name|getNewAddressTest
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:iota-new-address-response"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMinimumMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:iota-new-address"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|getTransfersTest ()
specifier|public
name|void
name|getTransfersTest
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:iota-get-transfers-response"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMinimumMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:iota-get-transfers"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
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
block|{
name|from
argument_list|(
literal|"direct:iota-send-message"
argument_list|)
operator|.
name|setHeader
argument_list|(
name|IOTAConstants
operator|.
name|SEED_HEADER
argument_list|,
name|constant
argument_list|(
name|SEED
argument_list|)
argument_list|)
operator|.
name|setHeader
argument_list|(
name|IOTAConstants
operator|.
name|TO_ADDRESS_HEADER
argument_list|,
name|constant
argument_list|(
name|ADDRESS
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"iota://test?url="
operator|+
name|IOTA_NODE_URL
operator|+
literal|"&securityLevel=2&tag=APACHECAMELTEST&depth=3&operation="
operator|+
name|IOTAConstants
operator|.
name|SEND_TRANSFER_OPERATION
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:iota-send-message-response"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:iota-new-address"
argument_list|)
operator|.
name|setHeader
argument_list|(
name|IOTAConstants
operator|.
name|SEED_HEADER
argument_list|,
name|constant
argument_list|(
name|SEED
argument_list|)
argument_list|)
operator|.
name|setHeader
argument_list|(
name|IOTAConstants
operator|.
name|ADDRESS_INDEX_HEADER
argument_list|,
name|constant
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"iota://test?url="
operator|+
name|IOTA_NODE_URL
operator|+
literal|"&securityLevel=1&operation="
operator|+
name|IOTAConstants
operator|.
name|GET_NEW_ADDRESS_OPERATION
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:iota-new-address-response"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:iota-get-transfers"
argument_list|)
operator|.
name|setHeader
argument_list|(
name|IOTAConstants
operator|.
name|SEED_HEADER
argument_list|,
name|constant
argument_list|(
name|SEED
argument_list|)
argument_list|)
operator|.
name|setHeader
argument_list|(
name|IOTAConstants
operator|.
name|ADDRESS_START_INDEX_HEADER
argument_list|,
name|constant
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|setHeader
argument_list|(
name|IOTAConstants
operator|.
name|ADDRESS_END_INDEX_HEADER
argument_list|,
name|constant
argument_list|(
literal|10
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"iota://test?url="
operator|+
name|IOTA_NODE_URL
operator|+
literal|"&securityLevel=1&operation="
operator|+
name|IOTAConstants
operator|.
name|GET_TRANSFERS_OPERATION
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:iota-get-transfers-response"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

