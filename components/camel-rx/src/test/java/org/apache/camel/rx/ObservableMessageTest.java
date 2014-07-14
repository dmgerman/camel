begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.rx
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|rx
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
name|Produce
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
name|Test
import|;
end_import

begin_import
import|import
name|rx
operator|.
name|Observable
import|;
end_import

begin_import
import|import
name|rx
operator|.
name|functions
operator|.
name|Action1
import|;
end_import

begin_import
import|import
name|rx
operator|.
name|functions
operator|.
name|Func1
import|;
end_import

begin_class
DECL|class|ObservableMessageTest
specifier|public
class|class
name|ObservableMessageTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|observableMessage
specifier|protected
name|MyObservableMessage
name|observableMessage
init|=
operator|new
name|MyObservableMessage
argument_list|()
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:result"
argument_list|)
DECL|field|resultEndpoint
specifier|protected
name|MockEndpoint
name|resultEndpoint
decl_stmt|;
annotation|@
name|Produce
argument_list|(
name|uri
operator|=
literal|"direct:start"
argument_list|)
DECL|field|template
specifier|protected
name|ProducerTemplate
name|template
decl_stmt|;
annotation|@
name|Test
DECL|method|testUseObservableInRoute ()
specifier|public
name|void
name|testUseObservableInRoute
parameter_list|()
throws|throws
name|Exception
block|{
name|resultEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello James"
argument_list|,
literal|"Hello Claus"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"James"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"Claus"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|class|MyObservableMessage
specifier|public
class|class
name|MyObservableMessage
extends|extends
name|ObservableMessage
block|{
annotation|@
name|Override
DECL|method|configure (Observable<Message> observable)
specifier|protected
name|void
name|configure
parameter_list|(
name|Observable
argument_list|<
name|Message
argument_list|>
name|observable
parameter_list|)
block|{
comment|// lets process the messages using the RX API
name|observable
operator|.
name|map
argument_list|(
operator|new
name|Func1
argument_list|<
name|Message
argument_list|,
name|String
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|String
name|call
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
return|return
literal|"Hello "
operator|+
name|message
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
return|;
block|}
block|}
argument_list|)
operator|.
name|subscribe
argument_list|(
operator|new
name|Action1
argument_list|<
name|String
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|call
parameter_list|(
name|String
name|body
parameter_list|)
block|{
name|template
operator|.
name|sendBody
argument_list|(
name|resultEndpoint
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
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
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|process
argument_list|(
name|observableMessage
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

