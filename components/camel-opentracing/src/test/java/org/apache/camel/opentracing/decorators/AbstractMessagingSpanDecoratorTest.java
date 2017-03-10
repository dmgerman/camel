begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.opentracing.decorators
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|opentracing
operator|.
name|decorators
package|;
end_package

begin_import
import|import
name|io
operator|.
name|opentracing
operator|.
name|mock
operator|.
name|MockSpan
import|;
end_import

begin_import
import|import
name|io
operator|.
name|opentracing
operator|.
name|mock
operator|.
name|MockTracer
import|;
end_import

begin_import
import|import
name|io
operator|.
name|opentracing
operator|.
name|tag
operator|.
name|Tags
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
name|opentracing
operator|.
name|SpanDecorator
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
name|mockito
operator|.
name|Mockito
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_class
DECL|class|AbstractMessagingSpanDecoratorTest
specifier|public
class|class
name|AbstractMessagingSpanDecoratorTest
block|{
annotation|@
name|Test
DECL|method|testOperationName ()
specifier|public
name|void
name|testOperationName
parameter_list|()
block|{
name|Endpoint
name|endpoint
init|=
name|Mockito
operator|.
name|mock
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|"jms://MyQueue?hello=world"
argument_list|)
expr_stmt|;
name|SpanDecorator
name|decorator
init|=
operator|new
name|AbstractMessagingSpanDecorator
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|String
name|getComponent
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
block|}
decl_stmt|;
name|assertEquals
argument_list|(
literal|"MyQueue"
argument_list|,
name|decorator
operator|.
name|getOperationName
argument_list|(
literal|null
argument_list|,
name|endpoint
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPreMessageBusDestination ()
specifier|public
name|void
name|testPreMessageBusDestination
parameter_list|()
block|{
name|Endpoint
name|endpoint
init|=
name|Mockito
operator|.
name|mock
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|"jms://MyQueue?hello=world"
argument_list|)
expr_stmt|;
name|SpanDecorator
name|decorator
init|=
operator|new
name|AbstractMessagingSpanDecorator
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|String
name|getComponent
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
block|}
decl_stmt|;
name|MockTracer
name|tracer
init|=
operator|new
name|MockTracer
argument_list|()
decl_stmt|;
name|MockSpan
name|span
init|=
operator|(
name|MockSpan
operator|)
name|tracer
operator|.
name|buildSpan
argument_list|(
literal|"TestSpan"
argument_list|)
operator|.
name|start
argument_list|()
decl_stmt|;
name|decorator
operator|.
name|pre
argument_list|(
name|span
argument_list|,
literal|null
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"MyQueue"
argument_list|,
name|span
operator|.
name|tags
argument_list|()
operator|.
name|get
argument_list|(
name|Tags
operator|.
name|MESSAGE_BUS_DESTINATION
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPreMessageId ()
specifier|public
name|void
name|testPreMessageId
parameter_list|()
block|{
name|String
name|messageId
init|=
literal|"abcd"
decl_stmt|;
name|Endpoint
name|endpoint
init|=
name|Mockito
operator|.
name|mock
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
name|Mockito
operator|.
name|mock
argument_list|(
name|Exchange
operator|.
name|class
argument_list|)
decl_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
name|SpanDecorator
name|decorator
init|=
operator|new
name|AbstractMessagingSpanDecorator
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|String
name|getComponent
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getMessageId
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|messageId
return|;
block|}
block|}
decl_stmt|;
name|MockTracer
name|tracer
init|=
operator|new
name|MockTracer
argument_list|()
decl_stmt|;
name|MockSpan
name|span
init|=
operator|(
name|MockSpan
operator|)
name|tracer
operator|.
name|buildSpan
argument_list|(
literal|"TestSpan"
argument_list|)
operator|.
name|start
argument_list|()
decl_stmt|;
name|decorator
operator|.
name|pre
argument_list|(
name|span
argument_list|,
name|exchange
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|messageId
argument_list|,
name|span
operator|.
name|tags
argument_list|()
operator|.
name|get
argument_list|(
name|AwsSqsSpanDecorator
operator|.
name|MESSAGE_BUS_ID
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

