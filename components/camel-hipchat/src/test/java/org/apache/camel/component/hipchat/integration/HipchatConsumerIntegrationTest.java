begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hipchat.integration
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hipchat
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
name|Predicate
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
name|hipchat
operator|.
name|HipchatConstants
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
name|support
operator|.
name|processor
operator|.
name|idempotent
operator|.
name|MemoryIdempotentRepository
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
name|apache
operator|.
name|http
operator|.
name|StatusLine
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
annotation|@
name|Ignore
argument_list|(
literal|"Must be manually tested. Provide your own auth key, user,& room from https://www.hipchat.com/docs/apiv2/auth"
argument_list|)
DECL|class|HipchatConsumerIntegrationTest
specifier|public
class|class
name|HipchatConsumerIntegrationTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:result"
argument_list|)
DECL|field|result
specifier|private
name|MockEndpoint
name|result
decl_stmt|;
annotation|@
name|Test
DECL|method|sendInOnly ()
specifier|public
name|void
name|sendInOnly
parameter_list|()
throws|throws
name|Exception
block|{
name|result
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|result
operator|.
name|expectedMessagesMatches
argument_list|(
operator|new
name|Predicate
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|matches
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|StatusLine
name|status
init|=
operator|(
name|StatusLine
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|HipchatConstants
operator|.
name|FROM_USER_RESPONSE_STATUS
argument_list|)
decl_stmt|;
return|return
literal|200
operator|==
name|status
operator|.
name|getStatusCode
argument_list|()
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
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
name|String
name|hipchatEndpointUri
init|=
literal|"hipchat:http:api.hipchat.com?authToken=XXXX&consumeUsers=@ShreyasPurohit&delay=1000"
decl_stmt|;
name|from
argument_list|(
name|hipchatEndpointUri
argument_list|)
operator|.
name|idempotentConsumer
argument_list|(
name|simple
argument_list|(
literal|"${in.header.HipchatMessageDate} ${in.header.HipchatFromUser}"
argument_list|)
argument_list|,
name|MemoryIdempotentRepository
operator|.
name|memoryIdempotentRepository
argument_list|(
literal|200
argument_list|)
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

