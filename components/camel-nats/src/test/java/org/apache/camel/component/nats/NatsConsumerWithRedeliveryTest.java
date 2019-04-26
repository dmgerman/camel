begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.nats
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|nats
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
name|io
operator|.
name|nats
operator|.
name|client
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
name|LoggingLevel
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
name|RuntimeCamelException
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
DECL|class|NatsConsumerWithRedeliveryTest
specifier|public
class|class
name|NatsConsumerWithRedeliveryTest
extends|extends
name|NatsTestSupport
block|{
DECL|field|REDELIVERY_COUNT
specifier|private
specifier|static
specifier|final
name|int
name|REDELIVERY_COUNT
init|=
literal|2
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
literal|"mock:result"
argument_list|)
DECL|field|mockResultEndpoint
specifier|protected
name|MockEndpoint
name|mockResultEndpoint
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
literal|"mock:exception"
argument_list|)
DECL|field|exception
specifier|private
name|MockEndpoint
name|exception
decl_stmt|;
annotation|@
name|Test
DECL|method|testConsumer ()
specifier|public
name|void
name|testConsumer
parameter_list|()
throws|throws
name|InterruptedException
throws|,
name|IOException
block|{
name|mockResultEndpoint
operator|.
name|setExpectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mockResultEndpoint
operator|.
name|setAssertPeriod
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:send"
argument_list|,
literal|"test"
argument_list|)
expr_stmt|;
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:send"
argument_list|,
literal|"golang"
argument_list|)
expr_stmt|;
name|exception
operator|.
name|setExpectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|exception
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|mockResultEndpoint
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
throws|throws
name|Exception
block|{
name|onException
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
operator|.
name|maximumRedeliveries
argument_list|(
name|REDELIVERY_COUNT
argument_list|)
operator|.
name|retryAttemptedLogLevel
argument_list|(
name|LoggingLevel
operator|.
name|INFO
argument_list|)
operator|.
name|retriesExhaustedLogLevel
argument_list|(
name|LoggingLevel
operator|.
name|ERROR
argument_list|)
operator|.
name|redeliveryDelay
argument_list|(
literal|10
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:exception"
argument_list|)
operator|.
name|handled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:send"
argument_list|)
operator|.
name|to
argument_list|(
literal|"nats://"
operator|+
name|getNatsUrl
argument_list|()
operator|+
literal|"?topic=test&flushConnection=true"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"nats://"
operator|+
name|getNatsUrl
argument_list|()
operator|+
literal|"?topic=test&flushConnection=true"
argument_list|)
operator|.
name|choice
argument_list|()
operator|.
name|when
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
name|Message
name|g
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|Message
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|s
init|=
operator|new
name|String
argument_list|(
name|g
operator|.
name|getData
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|s
operator|.
name|contains
argument_list|(
literal|"test"
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
block|}
argument_list|)
operator|.
name|throwException
argument_list|(
name|RuntimeCamelException
operator|.
name|class
argument_list|,
literal|"Test for this"
argument_list|)
operator|.
name|end
argument_list|()
operator|.
name|to
argument_list|(
name|mockResultEndpoint
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

