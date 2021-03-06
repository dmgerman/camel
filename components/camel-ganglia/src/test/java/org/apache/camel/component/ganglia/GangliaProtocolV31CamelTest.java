begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ganglia
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ganglia
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

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
name|Map
import|;
end_import

begin_import
import|import
name|info
operator|.
name|ganglia
operator|.
name|gmetric4j
operator|.
name|xdr
operator|.
name|v31x
operator|.
name|Ganglia_metadata_msg
import|;
end_import

begin_import
import|import
name|info
operator|.
name|ganglia
operator|.
name|gmetric4j
operator|.
name|xdr
operator|.
name|v31x
operator|.
name|Ganglia_value_msg
import|;
end_import

begin_import
import|import
name|io
operator|.
name|netty
operator|.
name|channel
operator|.
name|ChannelHandler
operator|.
name|Sharable
import|;
end_import

begin_import
import|import
name|io
operator|.
name|netty
operator|.
name|channel
operator|.
name|ChannelHandlerContext
import|;
end_import

begin_import
import|import
name|io
operator|.
name|netty
operator|.
name|channel
operator|.
name|socket
operator|.
name|DatagramPacket
import|;
end_import

begin_import
import|import
name|io
operator|.
name|netty
operator|.
name|handler
operator|.
name|codec
operator|.
name|MessageToMessageDecoder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|acplt
operator|.
name|oncrpc
operator|.
name|OncRpcException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|acplt
operator|.
name|oncrpc
operator|.
name|XdrAble
import|;
end_import

begin_import
import|import
name|org
operator|.
name|acplt
operator|.
name|oncrpc
operator|.
name|XdrBufferDecodingStream
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
name|BindToRegistry
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
name|CamelExecutionException
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
name|ResolveEndpointFailedException
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
name|junit
operator|.
name|Rule
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
name|rules
operator|.
name|ExpectedException
import|;
end_import

begin_import
import|import static
name|info
operator|.
name|ganglia
operator|.
name|gmetric4j
operator|.
name|gmetric
operator|.
name|GMetricSlope
operator|.
name|NEGATIVE
import|;
end_import

begin_import
import|import static
name|info
operator|.
name|ganglia
operator|.
name|gmetric4j
operator|.
name|gmetric
operator|.
name|GMetricType
operator|.
name|FLOAT
import|;
end_import

begin_import
import|import static
name|info
operator|.
name|ganglia
operator|.
name|gmetric4j
operator|.
name|xdr
operator|.
name|v31x
operator|.
name|Ganglia_msg_formats
operator|.
name|gmetadata_full
import|;
end_import

begin_import
import|import static
name|info
operator|.
name|ganglia
operator|.
name|gmetric4j
operator|.
name|xdr
operator|.
name|v31x
operator|.
name|Ganglia_msg_formats
operator|.
name|gmetric_string
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ganglia
operator|.
name|GangliaConfiguration
operator|.
name|DEFAULT_DMAX
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ganglia
operator|.
name|GangliaConfiguration
operator|.
name|DEFAULT_METRIC_NAME
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ganglia
operator|.
name|GangliaConfiguration
operator|.
name|DEFAULT_SLOPE
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ganglia
operator|.
name|GangliaConfiguration
operator|.
name|DEFAULT_TMAX
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ganglia
operator|.
name|GangliaConfiguration
operator|.
name|DEFAULT_TYPE
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ganglia
operator|.
name|GangliaConfiguration
operator|.
name|DEFAULT_UNITS
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ganglia
operator|.
name|GangliaConstants
operator|.
name|GROUP_NAME
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ganglia
operator|.
name|GangliaConstants
operator|.
name|METRIC_DMAX
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ganglia
operator|.
name|GangliaConstants
operator|.
name|METRIC_NAME
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ganglia
operator|.
name|GangliaConstants
operator|.
name|METRIC_SLOPE
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ganglia
operator|.
name|GangliaConstants
operator|.
name|METRIC_TMAX
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ganglia
operator|.
name|GangliaConstants
operator|.
name|METRIC_TYPE
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ganglia
operator|.
name|GangliaConstants
operator|.
name|METRIC_UNITS
import|;
end_import

begin_comment
comment|/**  * {@code GangliaProtocolV31CamelTest} is not shipped with an embedded gmond  * agent. The gmond agent is mocked with the help of camel-netty codecs and a  * mock endpoint. As underlying UDP packets are not guaranteed to be delivered,  * loose assertions are performed.  */
end_comment

begin_class
DECL|class|GangliaProtocolV31CamelTest
specifier|public
class|class
name|GangliaProtocolV31CamelTest
extends|extends
name|CamelGangliaTestSupport
block|{
annotation|@
name|Rule
DECL|field|thrown
specifier|public
name|ExpectedException
name|thrown
init|=
name|ExpectedException
operator|.
name|none
argument_list|()
decl_stmt|;
annotation|@
name|BindToRegistry
argument_list|(
literal|"protocolV31Decoder"
argument_list|)
DECL|field|protocolV31Decoder
specifier|private
name|ProtocolV31Decoder
name|protocolV31Decoder
init|=
operator|new
name|ProtocolV31Decoder
argument_list|()
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
literal|"mock:gmond"
argument_list|)
DECL|field|mockGmond
specifier|private
name|MockEndpoint
name|mockGmond
decl_stmt|;
DECL|method|getTestUri ()
specifier|private
name|String
name|getTestUri
parameter_list|()
block|{
return|return
literal|"ganglia:localhost:"
operator|+
name|getTestPort
argument_list|()
operator|+
literal|"?mode=UNICAST"
return|;
block|}
annotation|@
name|Test
DECL|method|sendDefaultConfigurationShouldSucceed ()
specifier|public
name|void
name|sendDefaultConfigurationShouldSucceed
parameter_list|()
throws|throws
name|Exception
block|{
name|mockGmond
operator|.
name|setMinimumExpectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|mockGmond
operator|.
name|setAssertPeriod
argument_list|(
literal|100L
argument_list|)
expr_stmt|;
name|mockGmond
operator|.
name|whenAnyExchangeReceived
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|Ganglia_metadata_msg
name|metadataMessage
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|Ganglia_metadata_msg
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|metadataMessage
operator|!=
literal|null
condition|)
block|{
name|assertEquals
argument_list|(
name|DEFAULT_METRIC_NAME
argument_list|,
name|metadataMessage
operator|.
name|gfull
operator|.
name|metric
operator|.
name|name
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|DEFAULT_TYPE
operator|.
name|getGangliaType
argument_list|()
argument_list|,
name|metadataMessage
operator|.
name|gfull
operator|.
name|metric
operator|.
name|type
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|DEFAULT_SLOPE
operator|.
name|getGangliaSlope
argument_list|()
argument_list|,
name|metadataMessage
operator|.
name|gfull
operator|.
name|metric
operator|.
name|slope
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|DEFAULT_UNITS
argument_list|,
name|metadataMessage
operator|.
name|gfull
operator|.
name|metric
operator|.
name|units
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|DEFAULT_TMAX
argument_list|,
name|metadataMessage
operator|.
name|gfull
operator|.
name|metric
operator|.
name|tmax
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|DEFAULT_DMAX
argument_list|,
name|metadataMessage
operator|.
name|gfull
operator|.
name|metric
operator|.
name|dmax
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Ganglia_value_msg
name|valueMessage
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|Ganglia_value_msg
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|valueMessage
operator|!=
literal|null
condition|)
block|{
name|assertEquals
argument_list|(
literal|"28.0"
argument_list|,
name|valueMessage
operator|.
name|gstr
operator|.
name|str
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"%s"
argument_list|,
name|valueMessage
operator|.
name|gstr
operator|.
name|fmt
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|fail
argument_list|(
literal|"The gmond mock should only receive non-null metadata or value messages"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
name|getTestUri
argument_list|()
argument_list|,
literal|"28.0"
argument_list|)
expr_stmt|;
name|mockGmond
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|sendMessageHeadersShouldOverrideDefaultConfiguration ()
specifier|public
name|void
name|sendMessageHeadersShouldOverrideDefaultConfiguration
parameter_list|()
throws|throws
name|Exception
block|{
name|mockGmond
operator|.
name|setMinimumExpectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|mockGmond
operator|.
name|setAssertPeriod
argument_list|(
literal|100L
argument_list|)
expr_stmt|;
name|mockGmond
operator|.
name|whenAnyExchangeReceived
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|Ganglia_metadata_msg
name|metadataMessage
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|Ganglia_metadata_msg
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|metadataMessage
operator|!=
literal|null
condition|)
block|{
name|assertEquals
argument_list|(
literal|"depth"
argument_list|,
name|metadataMessage
operator|.
name|gfull
operator|.
name|metric
operator|.
name|name
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|FLOAT
operator|.
name|getGangliaType
argument_list|()
argument_list|,
name|metadataMessage
operator|.
name|gfull
operator|.
name|metric
operator|.
name|type
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|NEGATIVE
operator|.
name|getGangliaSlope
argument_list|()
argument_list|,
name|metadataMessage
operator|.
name|gfull
operator|.
name|metric
operator|.
name|slope
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"cm"
argument_list|,
name|metadataMessage
operator|.
name|gfull
operator|.
name|metric
operator|.
name|units
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|100
argument_list|,
name|metadataMessage
operator|.
name|gfull
operator|.
name|metric
operator|.
name|tmax
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|metadataMessage
operator|.
name|gfull
operator|.
name|metric
operator|.
name|dmax
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Ganglia_value_msg
name|valueMessage
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|Ganglia_value_msg
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|valueMessage
operator|!=
literal|null
condition|)
block|{
name|assertEquals
argument_list|(
literal|"-3.0"
argument_list|,
name|valueMessage
operator|.
name|gstr
operator|.
name|str
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"%s"
argument_list|,
name|valueMessage
operator|.
name|gstr
operator|.
name|fmt
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|fail
argument_list|(
literal|"The gmond mock should only receive non-null metadata or value messages"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|GROUP_NAME
argument_list|,
literal|"sea-mesure"
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|METRIC_NAME
argument_list|,
literal|"depth"
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|METRIC_TYPE
argument_list|,
name|FLOAT
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|METRIC_SLOPE
argument_list|,
name|NEGATIVE
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|METRIC_UNITS
argument_list|,
literal|"cm"
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|METRIC_TMAX
argument_list|,
literal|100
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|METRIC_DMAX
argument_list|,
literal|10
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeaders
argument_list|(
name|getTestUri
argument_list|()
argument_list|,
operator|-
literal|3.0f
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|mockGmond
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|sendWrongMetricTypeShouldThrow ()
specifier|public
name|void
name|sendWrongMetricTypeShouldThrow
parameter_list|()
throws|throws
name|Exception
block|{
name|thrown
operator|.
name|expect
argument_list|(
name|CamelExecutionException
operator|.
name|class
argument_list|)
expr_stmt|;
name|mockGmond
operator|.
name|setExpectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|mockGmond
operator|.
name|setAssertPeriod
argument_list|(
literal|100L
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|getTestUri
argument_list|()
argument_list|,
literal|"28.0"
argument_list|,
name|METRIC_TYPE
argument_list|,
literal|"NotAGMetricType"
argument_list|)
expr_stmt|;
name|mockGmond
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|sendWrongMetricSlopeShouldThrow ()
specifier|public
name|void
name|sendWrongMetricSlopeShouldThrow
parameter_list|()
throws|throws
name|Exception
block|{
name|thrown
operator|.
name|expect
argument_list|(
name|CamelExecutionException
operator|.
name|class
argument_list|)
expr_stmt|;
name|mockGmond
operator|.
name|setExpectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|mockGmond
operator|.
name|setAssertPeriod
argument_list|(
literal|100L
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|getTestUri
argument_list|()
argument_list|,
literal|"28.0"
argument_list|,
name|METRIC_SLOPE
argument_list|,
literal|"NotAGMetricSlope"
argument_list|)
expr_stmt|;
name|mockGmond
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|sendWrongMetricTMaxShouldThrow ()
specifier|public
name|void
name|sendWrongMetricTMaxShouldThrow
parameter_list|()
throws|throws
name|Exception
block|{
name|thrown
operator|.
name|expect
argument_list|(
name|CamelExecutionException
operator|.
name|class
argument_list|)
expr_stmt|;
name|mockGmond
operator|.
name|setExpectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|mockGmond
operator|.
name|setAssertPeriod
argument_list|(
literal|100L
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|getTestUri
argument_list|()
argument_list|,
literal|"28.0"
argument_list|,
name|METRIC_TMAX
argument_list|,
operator|new
name|Object
argument_list|()
argument_list|)
expr_stmt|;
name|mockGmond
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|sendWrongMetricDMaxShouldThrow ()
specifier|public
name|void
name|sendWrongMetricDMaxShouldThrow
parameter_list|()
throws|throws
name|Exception
block|{
name|thrown
operator|.
name|expect
argument_list|(
name|CamelExecutionException
operator|.
name|class
argument_list|)
expr_stmt|;
name|mockGmond
operator|.
name|setExpectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|mockGmond
operator|.
name|setAssertPeriod
argument_list|(
literal|100L
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|getTestUri
argument_list|()
argument_list|,
literal|"28.0"
argument_list|,
name|METRIC_DMAX
argument_list|,
operator|new
name|Object
argument_list|()
argument_list|)
expr_stmt|;
name|mockGmond
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|sendWithWrongTypeShouldThrow ()
specifier|public
name|void
name|sendWithWrongTypeShouldThrow
parameter_list|()
throws|throws
name|Exception
block|{
name|thrown
operator|.
name|expect
argument_list|(
name|ResolveEndpointFailedException
operator|.
name|class
argument_list|)
expr_stmt|;
name|mockGmond
operator|.
name|setExpectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|mockGmond
operator|.
name|setAssertPeriod
argument_list|(
literal|100L
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
name|getTestUri
argument_list|()
operator|+
literal|"&type=wrong"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|mockGmond
operator|.
name|assertIsSatisfied
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
name|from
argument_list|(
literal|"netty:udp://localhost:"
operator|+
name|getTestPort
argument_list|()
operator|+
literal|"/?decoders=#protocolV31Decoder"
argument_list|)
operator|.
name|to
argument_list|(
name|mockGmond
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Sharable
DECL|class|ProtocolV31Decoder
specifier|public
specifier|static
class|class
name|ProtocolV31Decoder
extends|extends
name|MessageToMessageDecoder
argument_list|<
name|DatagramPacket
argument_list|>
block|{
annotation|@
name|Override
DECL|method|decode (ChannelHandlerContext ctx, DatagramPacket packet, List<Object> out)
specifier|protected
name|void
name|decode
parameter_list|(
name|ChannelHandlerContext
name|ctx
parameter_list|,
name|DatagramPacket
name|packet
parameter_list|,
name|List
argument_list|<
name|Object
argument_list|>
name|out
parameter_list|)
throws|throws
name|OncRpcException
throws|,
name|IOException
block|{
name|byte
index|[]
name|bytes
init|=
operator|new
name|byte
index|[
name|packet
operator|.
name|content
argument_list|()
operator|.
name|readableBytes
argument_list|()
index|]
decl_stmt|;
name|packet
operator|.
name|content
argument_list|()
operator|.
name|readBytes
argument_list|(
name|bytes
argument_list|)
expr_stmt|;
comment|// Determine what kind of object the datagram is handling
name|XdrBufferDecodingStream
name|xbds
init|=
operator|new
name|XdrBufferDecodingStream
argument_list|(
name|bytes
argument_list|)
decl_stmt|;
name|xbds
operator|.
name|beginDecoding
argument_list|()
expr_stmt|;
name|int
name|id
init|=
name|xbds
operator|.
name|xdrDecodeInt
argument_list|()
operator|&
literal|0xbf
decl_stmt|;
name|xbds
operator|.
name|endDecoding
argument_list|()
expr_stmt|;
name|XdrAble
name|outMsg
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|id
operator|==
name|gmetadata_full
condition|)
block|{
name|outMsg
operator|=
operator|new
name|Ganglia_metadata_msg
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|id
operator|==
name|gmetric_string
condition|)
block|{
name|outMsg
operator|=
operator|new
name|Ganglia_value_msg
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|fail
argument_list|(
literal|"During those tests, the gmond mock should only receive metadata or string messages"
argument_list|)
expr_stmt|;
block|}
comment|// Unmarshall the incoming datagram
name|xbds
operator|=
operator|new
name|XdrBufferDecodingStream
argument_list|(
name|bytes
argument_list|)
expr_stmt|;
name|xbds
operator|.
name|beginDecoding
argument_list|()
expr_stmt|;
name|outMsg
operator|.
name|xdrDecode
argument_list|(
name|xbds
argument_list|)
expr_stmt|;
name|xbds
operator|.
name|endDecoding
argument_list|()
expr_stmt|;
name|out
operator|.
name|add
argument_list|(
name|outMsg
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

