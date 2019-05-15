begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ahc.ws
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ahc
operator|.
name|ws
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Reader
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
name|support
operator|.
name|DefaultConsumer
import|;
end_import

begin_class
DECL|class|WsConsumer
specifier|public
class|class
name|WsConsumer
extends|extends
name|DefaultConsumer
block|{
DECL|method|WsConsumer (WsEndpoint endpoint, Processor processor)
specifier|public
name|WsConsumer
parameter_list|(
name|WsEndpoint
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
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|public
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
name|getEndpoint
argument_list|()
operator|.
name|connect
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|public
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|getEndpoint
argument_list|()
operator|.
name|disconnect
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|WsEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|WsEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
DECL|method|sendMessage (String message)
specifier|public
name|void
name|sendMessage
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|sendMessageInternal
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
DECL|method|sendMessage (Throwable throwable)
specifier|public
name|void
name|sendMessage
parameter_list|(
name|Throwable
name|throwable
parameter_list|)
block|{
name|sendMessageInternal
argument_list|(
name|throwable
argument_list|)
expr_stmt|;
block|}
DECL|method|sendMessage (byte[] message)
specifier|public
name|void
name|sendMessage
parameter_list|(
name|byte
index|[]
name|message
parameter_list|)
block|{
name|sendMessageInternal
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
DECL|method|sendMessage (InputStream message)
specifier|public
name|void
name|sendMessage
parameter_list|(
name|InputStream
name|message
parameter_list|)
block|{
name|sendMessageInternal
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
DECL|method|sendMessage (Reader message)
specifier|public
name|void
name|sendMessage
parameter_list|(
name|Reader
name|message
parameter_list|)
block|{
name|sendMessageInternal
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
DECL|method|sendMessageInternal (Object message)
specifier|private
name|void
name|sendMessageInternal
parameter_list|(
name|Object
name|message
parameter_list|)
block|{
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
comment|//TODO may set some headers with some meta info (e.g., socket info, unique-id for correlation purpose, etc0
comment|// set the body
if|if
condition|(
name|message
operator|instanceof
name|Throwable
condition|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
operator|(
name|Throwable
operator|)
name|message
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
comment|// send exchange using the async routing engine
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
name|exchange
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
literal|"Error processing exchange"
argument_list|,
name|exchange
argument_list|,
name|exchange
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

