begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.spring.integration.adapter
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
operator|.
name|adapter
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
name|atomic
operator|.
name|AtomicBoolean
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
name|Consumer
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
name|spring
operator|.
name|integration
operator|.
name|SpringIntegrationBinding
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
name|service
operator|.
name|ServiceHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|DisposableBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|InitializingBean
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

begin_comment
comment|/**  * A CamelContext will be injected into CameSourceAdapter which will  * let Spring Integration channel talk to the CamelContext certain endpoint  */
end_comment

begin_class
DECL|class|CamelSourceAdapter
specifier|public
class|class
name|CamelSourceAdapter
extends|extends
name|AbstractCamelAdapter
implements|implements
name|InitializingBean
implements|,
name|DisposableBean
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|CamelSourceAdapter
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|consumer
specifier|private
name|Consumer
name|consumer
decl_stmt|;
DECL|field|camelEndpoint
specifier|private
name|Endpoint
name|camelEndpoint
decl_stmt|;
DECL|field|requestChannel
specifier|private
name|MessageChannel
name|requestChannel
decl_stmt|;
DECL|field|replyChannel
specifier|private
name|DirectChannel
name|replyChannel
decl_stmt|;
DECL|field|initialized
specifier|private
specifier|final
name|AtomicBoolean
name|initialized
init|=
operator|new
name|AtomicBoolean
argument_list|()
decl_stmt|;
DECL|method|setRequestChannel (MessageChannel channel)
specifier|public
name|void
name|setRequestChannel
parameter_list|(
name|MessageChannel
name|channel
parameter_list|)
block|{
name|requestChannel
operator|=
name|channel
expr_stmt|;
block|}
DECL|method|getChannel ()
specifier|public
name|MessageChannel
name|getChannel
parameter_list|()
block|{
return|return
name|requestChannel
return|;
block|}
DECL|method|setReplyChannel (DirectChannel channel)
specifier|public
name|void
name|setReplyChannel
parameter_list|(
name|DirectChannel
name|channel
parameter_list|)
block|{
name|replyChannel
operator|=
name|channel
expr_stmt|;
block|}
DECL|class|ConsumerProcessor
specifier|protected
class|class
name|ConsumerProcessor
implements|implements
name|Processor
block|{
annotation|@
name|Override
DECL|method|process (final Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|org
operator|.
name|springframework
operator|.
name|messaging
operator|.
name|Message
argument_list|<
name|?
argument_list|>
name|request
init|=
name|SpringIntegrationBinding
operator|.
name|createSpringIntegrationMessage
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|exchange
operator|.
name|getPattern
argument_list|()
operator|.
name|isOutCapable
argument_list|()
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
operator|.
name|put
argument_list|(
name|MessageHeaders
operator|.
name|REPLY_CHANNEL
argument_list|,
name|replyChannel
argument_list|)
expr_stmt|;
comment|// we want to do in-out so the inputChannel is mandatory (used to receive reply from spring integration)
if|if
condition|(
name|replyChannel
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"ReplyChannel has not been configured on: "
operator|+
name|this
argument_list|)
throw|;
block|}
name|replyChannel
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
name|LOG
operator|.
name|debug
argument_list|(
literal|"Received {} from ReplyChannel: {}"
argument_list|,
name|message
argument_list|,
name|replyChannel
argument_list|)
expr_stmt|;
comment|//TODO set the correlationID
name|SpringIntegrationBinding
operator|.
name|storeToCamelMessage
argument_list|(
name|message
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
name|requestChannel
operator|.
name|send
argument_list|(
name|request
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|afterPropertiesSet ()
specifier|public
specifier|final
name|void
name|afterPropertiesSet
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|initialized
operator|.
name|compareAndSet
argument_list|(
literal|false
argument_list|,
literal|true
argument_list|)
condition|)
block|{
name|initialize
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|destroy ()
specifier|public
name|void
name|destroy
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|consumer
operator|!=
literal|null
condition|)
block|{
name|ServiceHelper
operator|.
name|stopAndShutdownService
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|initialize ()
specifier|protected
name|void
name|initialize
parameter_list|()
throws|throws
name|Exception
block|{
comment|// start the service here
name|camelEndpoint
operator|=
name|getCamelContext
argument_list|()
operator|.
name|getEndpoint
argument_list|(
name|getCamelEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|consumer
operator|=
name|camelEndpoint
operator|.
name|createConsumer
argument_list|(
operator|new
name|ConsumerProcessor
argument_list|()
argument_list|)
expr_stmt|;
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

