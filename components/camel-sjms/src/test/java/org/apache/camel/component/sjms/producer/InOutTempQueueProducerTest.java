begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sjms.producer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|sjms
operator|.
name|producer
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|UUID
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|JMSException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|MessageConsumer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|MessageListener
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|MessageProducer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|TextMessage
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
name|component
operator|.
name|sjms
operator|.
name|support
operator|.
name|JmsTestSupport
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
DECL|class|InOutTempQueueProducerTest
specifier|public
class|class
name|InOutTempQueueProducerTest
extends|extends
name|JmsTestSupport
block|{
annotation|@
name|Override
DECL|method|useJmx ()
specifier|protected
name|boolean
name|useJmx
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Test
DECL|method|testInOutQueueProducer ()
specifier|public
name|void
name|testInOutQueueProducer
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|queueName
init|=
literal|"in.out.queue.producer.test.request"
decl_stmt|;
name|MessageConsumer
name|mc
init|=
name|createQueueConsumer
argument_list|(
name|queueName
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|mc
argument_list|)
expr_stmt|;
specifier|final
name|String
name|requestText
init|=
literal|"Hello World!"
decl_stmt|;
specifier|final
name|String
name|responseText
init|=
literal|"How are you"
decl_stmt|;
name|mc
operator|.
name|setMessageListener
argument_list|(
operator|new
name|MyMessageListener
argument_list|(
name|requestText
argument_list|,
name|responseText
argument_list|)
argument_list|)
expr_stmt|;
name|Object
name|responseObject
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"sjms:queue:"
operator|+
name|queueName
operator|+
literal|"?exchangePattern=InOut"
argument_list|,
name|requestText
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|responseObject
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|responseObject
operator|instanceof
name|String
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|responseText
argument_list|,
name|responseObject
argument_list|)
expr_stmt|;
name|mc
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInOutQueueProducerWithCorrelationId ()
specifier|public
name|void
name|testInOutQueueProducerWithCorrelationId
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|queueName
init|=
literal|"in.out.queue.producer.test.request"
decl_stmt|;
name|MessageConsumer
name|mc
init|=
name|createQueueConsumer
argument_list|(
name|queueName
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|mc
argument_list|)
expr_stmt|;
specifier|final
name|String
name|requestText
init|=
literal|"Hello World!"
decl_stmt|;
specifier|final
name|String
name|responseText
init|=
literal|"How are you"
decl_stmt|;
name|mc
operator|.
name|setMessageListener
argument_list|(
operator|new
name|MyMessageListener
argument_list|(
name|requestText
argument_list|,
name|responseText
argument_list|)
argument_list|)
expr_stmt|;
specifier|final
name|String
name|correlationId
init|=
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
operator|.
name|replace
argument_list|(
literal|"-"
argument_list|,
literal|""
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
name|template
operator|.
name|request
argument_list|(
literal|"sjms:queue:"
operator|+
name|queueName
operator|+
literal|"?exchangePattern=InOut"
argument_list|,
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
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|requestText
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"JMSCorrelationID"
argument_list|,
name|correlationId
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
operator|instanceof
name|String
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|responseText
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|correlationId
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"JMSCorrelationID"
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|mc
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
DECL|class|MyMessageListener
specifier|protected
class|class
name|MyMessageListener
implements|implements
name|MessageListener
block|{
DECL|field|requestText
specifier|private
name|String
name|requestText
decl_stmt|;
DECL|field|responseText
specifier|private
name|String
name|responseText
decl_stmt|;
DECL|method|MyMessageListener (String request, String response)
specifier|public
name|MyMessageListener
parameter_list|(
name|String
name|request
parameter_list|,
name|String
name|response
parameter_list|)
block|{
name|this
operator|.
name|requestText
operator|=
name|request
expr_stmt|;
name|this
operator|.
name|responseText
operator|=
name|response
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onMessage (Message message)
specifier|public
name|void
name|onMessage
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
try|try
block|{
name|TextMessage
name|request
init|=
operator|(
name|TextMessage
operator|)
name|message
decl_stmt|;
name|assertNotNull
argument_list|(
name|request
argument_list|)
expr_stmt|;
name|String
name|text
init|=
name|request
operator|.
name|getText
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|requestText
argument_list|,
name|text
argument_list|)
expr_stmt|;
name|TextMessage
name|response
init|=
name|getSession
argument_list|()
operator|.
name|createTextMessage
argument_list|()
decl_stmt|;
name|response
operator|.
name|setText
argument_list|(
name|responseText
argument_list|)
expr_stmt|;
name|response
operator|.
name|setJMSCorrelationID
argument_list|(
name|request
operator|.
name|getJMSCorrelationID
argument_list|()
argument_list|)
expr_stmt|;
name|MessageProducer
name|mp
init|=
name|getSession
argument_list|()
operator|.
name|createProducer
argument_list|(
name|message
operator|.
name|getJMSReplyTo
argument_list|()
argument_list|)
decl_stmt|;
name|mp
operator|.
name|send
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|mp
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JMSException
name|e
parameter_list|)
block|{
name|fail
argument_list|(
name|e
operator|.
name|getLocalizedMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

