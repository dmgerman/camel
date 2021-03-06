begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.iec60870.server
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|iec60870
operator|.
name|server
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
name|CompletableFuture
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
name|CompletionStage
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
name|iec60870
operator|.
name|ObjectAddress
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
name|DefaultConsumer
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
name|DefaultMessage
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|neoscada
operator|.
name|protocol
operator|.
name|iec60870
operator|.
name|server
operator|.
name|data
operator|.
name|model
operator|.
name|WriteModel
operator|.
name|Request
import|;
end_import

begin_class
DECL|class|ServerConsumer
specifier|public
class|class
name|ServerConsumer
extends|extends
name|DefaultConsumer
block|{
DECL|field|server
specifier|private
specifier|final
name|ServerInstance
name|server
decl_stmt|;
DECL|field|endpoint
specifier|private
specifier|final
name|ServerEndpoint
name|endpoint
decl_stmt|;
DECL|method|ServerConsumer (final ServerEndpoint endpoint, final Processor processor, final ServerInstance server)
specifier|public
name|ServerConsumer
parameter_list|(
specifier|final
name|ServerEndpoint
name|endpoint
parameter_list|,
specifier|final
name|Processor
name|processor
parameter_list|,
specifier|final
name|ServerInstance
name|server
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
name|this
operator|.
name|server
operator|=
name|server
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
name|this
operator|.
name|server
operator|.
name|setListener
argument_list|(
name|this
operator|.
name|endpoint
operator|.
name|getAddress
argument_list|()
argument_list|,
name|this
operator|::
name|updateValue
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|this
operator|.
name|server
operator|.
name|setListener
argument_list|(
name|this
operator|.
name|endpoint
operator|.
name|getAddress
argument_list|()
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
DECL|method|updateValue (final Request<?> value)
specifier|private
name|CompletionStage
argument_list|<
name|Void
argument_list|>
name|updateValue
parameter_list|(
specifier|final
name|Request
argument_list|<
name|?
argument_list|>
name|value
parameter_list|)
block|{
try|try
block|{
comment|// create exchange
specifier|final
name|Exchange
name|exchange
init|=
name|getEndpoint
argument_list|()
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|setIn
argument_list|(
name|mapMessage
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
comment|// create new future
specifier|final
name|CompletableFuture
argument_list|<
name|Void
argument_list|>
name|result
init|=
operator|new
name|CompletableFuture
argument_list|<>
argument_list|()
decl_stmt|;
comment|// process and map async callback to our future
name|getAsyncProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
name|doneSync
lambda|->
name|result
operator|.
name|complete
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
comment|// return future
return|return
name|result
return|;
block|}
catch|catch
parameter_list|(
specifier|final
name|Exception
name|e
parameter_list|)
block|{
comment|// we failed triggering the process
name|log
operator|.
name|debug
argument_list|(
literal|"Failed to process message"
argument_list|,
name|e
argument_list|)
expr_stmt|;
comment|// create a future
specifier|final
name|CompletableFuture
argument_list|<
name|Void
argument_list|>
name|result
init|=
operator|new
name|CompletableFuture
argument_list|<>
argument_list|()
decl_stmt|;
comment|// complete it right away
name|result
operator|.
name|completeExceptionally
argument_list|(
name|e
argument_list|)
expr_stmt|;
comment|// return it
return|return
name|result
return|;
block|}
block|}
DECL|method|mapMessage (final Request<?> request)
specifier|private
name|Message
name|mapMessage
parameter_list|(
specifier|final
name|Request
argument_list|<
name|?
argument_list|>
name|request
parameter_list|)
block|{
specifier|final
name|DefaultMessage
name|message
init|=
operator|new
name|DefaultMessage
argument_list|(
name|this
operator|.
name|endpoint
operator|.
name|getCamelContext
argument_list|()
argument_list|)
decl_stmt|;
name|message
operator|.
name|setBody
argument_list|(
name|request
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
literal|"address"
argument_list|,
name|ObjectAddress
operator|.
name|valueOf
argument_list|(
name|request
operator|.
name|getHeader
argument_list|()
operator|.
name|getAsduAddress
argument_list|()
argument_list|,
name|request
operator|.
name|getAddress
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
literal|"value"
argument_list|,
name|request
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
literal|"informationObjectAddress"
argument_list|,
name|request
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
literal|"asduHeader"
argument_list|,
name|request
operator|.
name|getHeader
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
literal|"type"
argument_list|,
name|request
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
literal|"execute"
argument_list|,
name|request
operator|.
name|isExecute
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|message
return|;
block|}
block|}
end_class

end_unit

