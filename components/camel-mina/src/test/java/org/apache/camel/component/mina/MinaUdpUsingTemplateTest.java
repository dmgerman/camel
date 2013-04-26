begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mina
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mina
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
name|Test
import|;
end_import

begin_comment
comment|/**  * @version  */
end_comment

begin_class
DECL|class|MinaUdpUsingTemplateTest
specifier|public
class|class
name|MinaUdpUsingTemplateTest
extends|extends
name|BaseMinaTest
block|{
DECL|field|messageCount
specifier|private
name|int
name|messageCount
init|=
literal|3
decl_stmt|;
annotation|@
name|Test
DECL|method|testMinaRoute ()
specifier|public
name|void
name|testMinaRoute
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|endpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello Message: 0"
argument_list|,
literal|"Hello Message: 1"
argument_list|,
literal|"Hello Message: 2"
argument_list|)
expr_stmt|;
name|sendUdpMessages
argument_list|()
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|sendUdpMessages ()
specifier|protected
name|void
name|sendUdpMessages
parameter_list|()
throws|throws
name|Exception
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|messageCount
condition|;
name|i
operator|++
control|)
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"mina:udp://127.0.0.1:{{port}}?sync=false"
argument_list|,
literal|"Hello Message: "
operator|+
name|i
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testSendingByteMessages ()
specifier|public
name|void
name|testSendingByteMessages
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|endpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|byte
index|[]
name|in
init|=
literal|"Hello from bytes"
operator|.
name|getBytes
argument_list|()
decl_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"mina:udp://127.0.0.1:{{port}}?sync=false"
argument_list|,
name|in
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Exchange
argument_list|>
name|list
init|=
name|endpoint
operator|.
name|getReceivedExchanges
argument_list|()
decl_stmt|;
name|byte
index|[]
name|out
init|=
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|)
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
name|in
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|assertEquals
argument_list|(
literal|"Thew bytes should be the same"
argument_list|,
name|in
index|[
name|i
index|]
argument_list|,
name|out
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testSendingRawByteMessage ()
specifier|public
name|void
name|testSendingRawByteMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|endpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|String
name|toSend
init|=
literal|"ef3e00559f5faf0262f5ff0962d9008daa91001cd46b0fa9330ef0f3030fff250e46f72444d1cc501678c351e04b8004c"
operator|+
literal|"4000002080000fe850bbe011030000008031b031bfe9251305441593830354720020800050440ff"
decl_stmt|;
name|byte
index|[]
name|in
init|=
name|fromHexString
argument_list|(
name|toSend
argument_list|)
decl_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"mina:udp://127.0.0.1:{{port}}?sync=false"
argument_list|,
name|in
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Exchange
argument_list|>
name|list
init|=
name|endpoint
operator|.
name|getReceivedExchanges
argument_list|()
decl_stmt|;
name|byte
index|[]
name|out
init|=
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|)
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
name|in
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|assertEquals
argument_list|(
literal|"The bytes should be the same"
argument_list|,
name|in
index|[
name|i
index|]
argument_list|,
name|out
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|"The strings should be the same"
argument_list|,
name|toSend
argument_list|,
name|byteArrayToHex
argument_list|(
name|out
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|byteArrayToHex (byte[] bytes)
specifier|private
name|String
name|byteArrayToHex
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|byte
name|b
range|:
name|bytes
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"%02x"
argument_list|,
name|b
operator|&
literal|0xff
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|fromHexString (String hexstr)
specifier|private
name|byte
index|[]
name|fromHexString
parameter_list|(
name|String
name|hexstr
parameter_list|)
block|{
name|byte
name|data
index|[]
init|=
operator|new
name|byte
index|[
name|hexstr
operator|.
name|length
argument_list|()
operator|/
literal|2
index|]
decl_stmt|;
name|int
name|i
init|=
literal|0
decl_stmt|;
for|for
control|(
name|int
name|n
init|=
name|hexstr
operator|.
name|length
argument_list|()
init|;
name|i
operator|<
name|n
condition|;
name|i
operator|+=
literal|2
control|)
block|{
name|data
index|[
name|i
operator|/
literal|2
index|]
operator|=
operator|(
name|Integer
operator|.
name|decode
argument_list|(
literal|"0x"
operator|+
name|hexstr
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
operator|+
name|hexstr
operator|.
name|charAt
argument_list|(
name|i
operator|+
literal|1
argument_list|)
argument_list|)
operator|)
operator|.
name|byteValue
argument_list|()
expr_stmt|;
block|}
return|return
name|data
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
name|from
argument_list|(
literal|"mina:udp://127.0.0.1:{{port}}?sync=false&minaLogger=true"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

