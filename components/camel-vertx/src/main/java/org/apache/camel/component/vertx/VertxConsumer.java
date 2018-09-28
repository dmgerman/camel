begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.vertx
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|vertx
package|;
end_package

begin_import
import|import
name|io
operator|.
name|vertx
operator|.
name|core
operator|.
name|Handler
import|;
end_import

begin_import
import|import
name|io
operator|.
name|vertx
operator|.
name|core
operator|.
name|eventbus
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|io
operator|.
name|vertx
operator|.
name|core
operator|.
name|eventbus
operator|.
name|MessageConsumer
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
name|AsyncCallback
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
name|ExchangePattern
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
name|impl
operator|.
name|DefaultConsumer
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|vertx
operator|.
name|VertxHelper
operator|.
name|getVertxBody
import|;
end_import

begin_class
DECL|class|VertxConsumer
specifier|public
class|class
name|VertxConsumer
extends|extends
name|DefaultConsumer
block|{
DECL|field|endpoint
specifier|private
specifier|final
name|VertxEndpoint
name|endpoint
decl_stmt|;
DECL|field|messageConsumer
specifier|private
specifier|transient
name|MessageConsumer
name|messageConsumer
decl_stmt|;
DECL|field|handler
specifier|private
name|Handler
argument_list|<
name|Message
argument_list|<
name|Object
argument_list|>
argument_list|>
name|handler
init|=
operator|new
name|Handler
argument_list|<
name|Message
argument_list|<
name|Object
argument_list|>
argument_list|>
argument_list|()
block|{
specifier|public
name|void
name|handle
parameter_list|(
name|Message
name|event
parameter_list|)
block|{
name|onEventBusEvent
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
DECL|method|VertxConsumer (VertxEndpoint endpoint, Processor processor)
specifier|public
name|VertxConsumer
parameter_list|(
name|VertxEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
block|}
DECL|method|onEventBusEvent (final Message event)
specifier|protected
name|void
name|onEventBusEvent
parameter_list|(
specifier|final
name|Message
name|event
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"onEvent {}"
argument_list|,
name|event
argument_list|)
expr_stmt|;
specifier|final
name|boolean
name|reply
init|=
name|event
operator|.
name|replyAddress
argument_list|()
operator|!=
literal|null
decl_stmt|;
specifier|final
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|(
name|reply
condition|?
name|ExchangePattern
operator|.
name|InOut
else|:
name|ExchangePattern
operator|.
name|InOnly
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|event
operator|.
name|body
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|getAsyncProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
operator|new
name|AsyncCallback
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|done
parameter_list|(
name|boolean
name|doneSync
parameter_list|)
block|{
if|if
condition|(
name|reply
condition|)
block|{
name|Object
name|body
init|=
name|getVertxBody
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|body
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Sending reply to: {} with body: {}"
argument_list|,
name|event
operator|.
name|replyAddress
argument_list|()
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|event
operator|.
name|reply
argument_list|(
name|body
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
literal|"Error processing Vertx event: "
operator|+
name|event
argument_list|,
name|exchange
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Registering EventBus handler on address {}"
argument_list|,
name|endpoint
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|endpoint
operator|.
name|getEventBus
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|messageConsumer
operator|=
name|endpoint
operator|.
name|getEventBus
argument_list|()
operator|.
name|consumer
argument_list|(
name|endpoint
operator|.
name|getAddress
argument_list|()
argument_list|,
name|handler
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
block|}
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Unregistering EventBus handler on address {}"
argument_list|,
name|endpoint
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
block|}
try|try
block|{
if|if
condition|(
name|messageConsumer
operator|!=
literal|null
operator|&&
name|messageConsumer
operator|.
name|isRegistered
argument_list|()
condition|)
block|{
name|messageConsumer
operator|.
name|unregister
argument_list|()
expr_stmt|;
name|messageConsumer
operator|=
literal|null
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IllegalStateException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"EventBus already stopped on address {}"
argument_list|,
name|endpoint
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
comment|// ignore if already stopped as vertx throws this exception if its already stopped etc.
comment|// unfortunately it does not provide an nicer api to know its state
block|}
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

