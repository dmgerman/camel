begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sjms.consumer
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
name|consumer
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
name|ExecutorService
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
name|component
operator|.
name|sjms
operator|.
name|SjmsEndpoint
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
name|spi
operator|.
name|Synchronization
import|;
end_import

begin_comment
comment|/**  * An InOnly {@link AbstractMessageHandler}  */
end_comment

begin_class
DECL|class|InOnlyMessageHandler
specifier|public
class|class
name|InOnlyMessageHandler
extends|extends
name|AbstractMessageHandler
block|{
DECL|method|InOnlyMessageHandler (SjmsEndpoint endpoint, ExecutorService executor)
specifier|public
name|InOnlyMessageHandler
parameter_list|(
name|SjmsEndpoint
name|endpoint
parameter_list|,
name|ExecutorService
name|executor
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|executor
argument_list|)
expr_stmt|;
block|}
DECL|method|InOnlyMessageHandler (SjmsEndpoint endpoint, ExecutorService executor, Synchronization synchronization)
specifier|public
name|InOnlyMessageHandler
parameter_list|(
name|SjmsEndpoint
name|endpoint
parameter_list|,
name|ExecutorService
name|executor
parameter_list|,
name|Synchronization
name|synchronization
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|executor
argument_list|,
name|synchronization
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|handleMessage (final Exchange exchange)
specifier|public
name|void
name|handleMessage
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
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
literal|"Handling InOnly Message: {}"
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|exchange
operator|.
name|isFailed
argument_list|()
condition|)
block|{
name|NoOpAsyncCallback
name|callback
init|=
operator|new
name|NoOpAsyncCallback
argument_list|()
decl_stmt|;
if|if
condition|(
name|isTransacted
argument_list|()
operator|||
name|isSynchronous
argument_list|()
condition|)
block|{
comment|// must process synchronous if transacted or configured to do so
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
literal|"Synchronous processing: Message[{}], Destination[{}] "
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// process asynchronous using the async routing engine
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
literal|"Asynchronous processing: Message[{}], Destination[{}] "
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
block|{
comment|// no-op
block|}
DECL|class|NoOpAsyncCallback
specifier|protected
class|class
name|NoOpAsyncCallback
implements|implements
name|AsyncCallback
block|{
DECL|method|NoOpAsyncCallback ()
specifier|public
name|NoOpAsyncCallback
parameter_list|()
block|{         }
annotation|@
name|Override
DECL|method|done (boolean sync)
specifier|public
name|void
name|done
parameter_list|(
name|boolean
name|sync
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"NoOpAsyncCallback InOnly Exchange complete"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

