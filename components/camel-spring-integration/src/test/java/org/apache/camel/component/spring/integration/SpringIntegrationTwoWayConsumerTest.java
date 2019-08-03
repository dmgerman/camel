begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.spring.integration
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|spring
operator|.
name|integration
package|;
end_package

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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|CountDownLatch
import|;
end_import

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
name|test
operator|.
name|spring
operator|.
name|CamelSpringTestSupport
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
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|ClassPathXmlApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|integration
operator|.
name|channel
operator|.
name|DirectChannel
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|messaging
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|messaging
operator|.
name|MessageChannel
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|messaging
operator|.
name|MessageHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|messaging
operator|.
name|MessageHeaders
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|messaging
operator|.
name|support
operator|.
name|GenericMessage
import|;
end_import

begin_class
DECL|class|SpringIntegrationTwoWayConsumerTest
specifier|public
class|class
name|SpringIntegrationTwoWayConsumerTest
extends|extends
name|CamelSpringTestSupport
block|{
DECL|field|MESSAGE_BODY
specifier|private
specifier|static
specifier|final
name|String
name|MESSAGE_BODY
init|=
literal|"Request message"
decl_stmt|;
annotation|@
name|Test
DECL|method|testSendingTwoWayMessage ()
specifier|public
name|void
name|testSendingTwoWayMessage
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|MessageChannel
name|requestChannel
init|=
name|getMandatoryBean
argument_list|(
name|MessageChannel
operator|.
name|class
argument_list|,
literal|"requestChannel"
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|maps
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|maps
operator|.
name|put
argument_list|(
name|MessageHeaders
operator|.
name|REPLY_CHANNEL
argument_list|,
literal|"responseChannel"
argument_list|)
expr_stmt|;
name|Message
argument_list|<
name|String
argument_list|>
name|message
init|=
operator|new
name|GenericMessage
argument_list|<>
argument_list|(
name|MESSAGE_BODY
argument_list|,
name|maps
argument_list|)
decl_stmt|;
name|DirectChannel
name|responseChannel
init|=
name|getMandatoryBean
argument_list|(
name|DirectChannel
operator|.
name|class
argument_list|,
literal|"responseChannel"
argument_list|)
decl_stmt|;
name|responseChannel
operator|.
name|subscribe
argument_list|(
operator|new
name|MessageHandler
argument_list|()
block|{
specifier|public
name|void
name|handleMessage
parameter_list|(
name|Message
argument_list|<
name|?
argument_list|>
name|message
parameter_list|)
block|{
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get the wrong result"
argument_list|,
name|MESSAGE_BODY
operator|+
literal|" is processed"
argument_list|,
name|message
operator|.
name|getPayload
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Done"
argument_list|,
name|message
operator|.
name|getHeaders
argument_list|()
operator|.
name|get
argument_list|(
literal|"Status"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|requestChannel
operator|.
name|send
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|latch
operator|.
name|await
argument_list|(
literal|1
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createApplicationContext ()
specifier|public
name|ClassPathXmlApplicationContext
name|createApplicationContext
parameter_list|()
block|{
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/component/spring/integration/twoWayConsumer.xml"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

