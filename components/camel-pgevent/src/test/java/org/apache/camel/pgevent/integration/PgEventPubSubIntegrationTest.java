begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.pgevent.integration
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|pgevent
operator|.
name|integration
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
name|RoutesBuilder
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

begin_class
DECL|class|PgEventPubSubIntegrationTest
specifier|public
class|class
name|PgEventPubSubIntegrationTest
extends|extends
name|AbstractPgEventIntegrationTest
block|{
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"pgevent://{{host}}:{{port}}/{{database}}/testchannel?user={{userName}}&pass={{password}}"
argument_list|)
DECL|field|subscribeEndpoint
specifier|private
name|Endpoint
name|subscribeEndpoint
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"pgevent://{{host}}:{{port}}/{{database}}/testchannel?user={{userName}}&pass={{password}}"
argument_list|)
DECL|field|notifyEndpoint
specifier|private
name|Endpoint
name|notifyEndpoint
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"timer://test?repeatCount=1&period=1"
argument_list|)
DECL|field|timerEndpoint
specifier|private
name|Endpoint
name|timerEndpoint
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:result"
argument_list|)
DECL|field|mockEndpoint
specifier|private
name|MockEndpoint
name|mockEndpoint
decl_stmt|;
annotation|@
name|Test
DECL|method|testPgEventPublishSubscribe ()
specifier|public
name|void
name|testPgEventPublishSubscribe
parameter_list|()
throws|throws
name|Exception
block|{
name|mockEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
name|TEST_MESSAGE_BODY
argument_list|)
expr_stmt|;
name|mockEndpoint
operator|.
name|assertIsSatisfied
argument_list|(
literal|5000
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RoutesBuilder
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
name|timerEndpoint
argument_list|)
operator|.
name|setBody
argument_list|(
name|constant
argument_list|(
name|TEST_MESSAGE_BODY
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
name|notifyEndpoint
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|subscribeEndpoint
argument_list|)
operator|.
name|to
argument_list|(
name|mockEndpoint
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

