begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.infinispan
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|infinispan
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
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
DECL|class|InfinispanLocalConsumerTest
specifier|public
class|class
name|InfinispanLocalConsumerTest
extends|extends
name|InfinispanTestSupport
block|{
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:result"
argument_list|)
DECL|field|mockResult
specifier|private
name|MockEndpoint
name|mockResult
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:result2"
argument_list|)
DECL|field|mockResult2
specifier|private
name|MockEndpoint
name|mockResult2
decl_stmt|;
annotation|@
name|Test
DECL|method|consumerReceivedPreAndPostEntryCreatedEventNotifications ()
specifier|public
name|void
name|consumerReceivedPreAndPostEntryCreatedEventNotifications
parameter_list|()
throws|throws
name|Exception
block|{
name|mockResult
operator|.
name|expectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|mockResult
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|outHeader
argument_list|(
name|InfinispanConstants
operator|.
name|EVENT_TYPE
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"CACHE_ENTRY_CREATED"
argument_list|)
expr_stmt|;
name|mockResult
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|outHeader
argument_list|(
name|InfinispanConstants
operator|.
name|IS_PRE
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|mockResult
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|outHeader
argument_list|(
name|InfinispanConstants
operator|.
name|CACHE_NAME
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
name|mockResult
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|outHeader
argument_list|(
name|InfinispanConstants
operator|.
name|KEY
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|KEY_ONE
argument_list|)
expr_stmt|;
name|mockResult
operator|.
name|message
argument_list|(
literal|1
argument_list|)
operator|.
name|outHeader
argument_list|(
name|InfinispanConstants
operator|.
name|EVENT_TYPE
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"CACHE_ENTRY_CREATED"
argument_list|)
expr_stmt|;
name|mockResult
operator|.
name|message
argument_list|(
literal|1
argument_list|)
operator|.
name|outHeader
argument_list|(
name|InfinispanConstants
operator|.
name|IS_PRE
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|mockResult
operator|.
name|message
argument_list|(
literal|1
argument_list|)
operator|.
name|outHeader
argument_list|(
name|InfinispanConstants
operator|.
name|CACHE_NAME
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
name|mockResult
operator|.
name|message
argument_list|(
literal|1
argument_list|)
operator|.
name|outHeader
argument_list|(
name|InfinispanConstants
operator|.
name|KEY
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|KEY_ONE
argument_list|)
expr_stmt|;
name|currentCache
argument_list|()
operator|.
name|put
argument_list|(
name|KEY_ONE
argument_list|,
name|VALUE_ONE
argument_list|)
expr_stmt|;
name|mockResult
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|consumerReceivedExpirationEventNotification ()
specifier|public
name|void
name|consumerReceivedExpirationEventNotification
parameter_list|()
throws|throws
name|Exception
block|{
name|mockResult2
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mockResult2
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|outHeader
argument_list|(
name|InfinispanConstants
operator|.
name|EVENT_TYPE
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"CACHE_ENTRY_EXPIRED"
argument_list|)
expr_stmt|;
name|mockResult2
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|outHeader
argument_list|(
name|InfinispanConstants
operator|.
name|IS_PRE
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|mockResult2
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|outHeader
argument_list|(
name|InfinispanConstants
operator|.
name|CACHE_NAME
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
name|mockResult2
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|outHeader
argument_list|(
name|InfinispanConstants
operator|.
name|KEY
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"keyTwo"
argument_list|)
expr_stmt|;
name|injectTimeService
argument_list|()
expr_stmt|;
name|currentCache
argument_list|()
operator|.
name|put
argument_list|(
literal|"keyTwo"
argument_list|,
literal|"valueTwo"
argument_list|,
literal|1000
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
name|ts
operator|.
name|advance
argument_list|(
literal|1001
argument_list|)
expr_stmt|;
comment|//expiration events are thrown only after a get if expiration reaper thread is not enabled
name|assertNull
argument_list|(
name|currentCache
argument_list|()
operator|.
name|get
argument_list|(
literal|"keyTwo"
argument_list|)
argument_list|)
expr_stmt|;
name|mockResult2
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
literal|"infinispan://localhost?cacheContainer=#cacheContainer&eventTypes=CACHE_ENTRY_CREATED"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"infinispan://localhost?cacheContainer=#cacheContainer&eventTypes=CACHE_ENTRY_EXPIRED"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result2"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

