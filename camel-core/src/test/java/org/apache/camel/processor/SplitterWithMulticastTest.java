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
name|builder
operator|.
name|RouteBuilder
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|SplitterWithMulticastTest
specifier|public
class|class
name|SplitterWithMulticastTest
extends|extends
name|ContextTestSupport
block|{
DECL|method|testSplitterWithMulticast ()
specifier|public
name|void
name|testSplitterWithMulticast
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
literal|"A,B,C"
argument_list|)
expr_stmt|;
comment|// should get the original input message without any headers etc
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|allMessages
argument_list|()
operator|.
name|header
argument_list|(
literal|"bar"
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|allMessages
argument_list|()
operator|.
name|header
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:split"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"A"
argument_list|,
literal|"B"
argument_list|,
literal|"C"
argument_list|)
expr_stmt|;
comment|// should have the bar header because multicast uses UseLatestAggregationStrategy by default
name|getMockEndpoint
argument_list|(
literal|"mock:split"
argument_list|)
operator|.
name|expectedHeaderReceived
argument_list|(
literal|"bar"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
comment|// should NOT have the foo header because multicast uses UseLatestAggregationStrategy by default
name|getMockEndpoint
argument_list|(
literal|"mock:split"
argument_list|)
operator|.
name|allMessages
argument_list|()
operator|.
name|header
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"A,B,C"
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
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|split
argument_list|(
name|body
argument_list|()
operator|.
name|tokenize
argument_list|(
literal|","
argument_list|)
argument_list|)
operator|.
name|multicast
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"foo"
argument_list|,
name|constant
argument_list|(
literal|"ABC"
argument_list|)
argument_list|)
operator|.
name|setHeader
argument_list|(
literal|"bar"
argument_list|,
name|constant
argument_list|(
literal|123
argument_list|)
argument_list|)
operator|.
name|end
argument_list|()
operator|.
name|to
argument_list|(
literal|"log:split?showHeaders=true"
argument_list|,
literal|"mock:split"
argument_list|)
operator|.
name|end
argument_list|()
operator|.
name|to
argument_list|(
literal|"log:result?showHeaders=true"
argument_list|,
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

