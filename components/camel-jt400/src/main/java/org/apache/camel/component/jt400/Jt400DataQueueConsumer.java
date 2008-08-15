begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jt400
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jt400
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
name|com
operator|.
name|ibm
operator|.
name|as400
operator|.
name|access
operator|.
name|AS400
import|;
end_import

begin_import
import|import
name|com
operator|.
name|ibm
operator|.
name|as400
operator|.
name|access
operator|.
name|AS400SecurityException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|ibm
operator|.
name|as400
operator|.
name|access
operator|.
name|DataQueue
import|;
end_import

begin_import
import|import
name|com
operator|.
name|ibm
operator|.
name|as400
operator|.
name|access
operator|.
name|DataQueueEntry
import|;
end_import

begin_import
import|import
name|com
operator|.
name|ibm
operator|.
name|as400
operator|.
name|access
operator|.
name|ErrorCompletingRequestException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|ibm
operator|.
name|as400
operator|.
name|access
operator|.
name|IllegalObjectTypeException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|ibm
operator|.
name|as400
operator|.
name|access
operator|.
name|ObjectDoesNotExistException
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
name|PollingConsumer
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
name|component
operator|.
name|jt400
operator|.
name|Jt400DataQueueEndpoint
operator|.
name|Format
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
name|DefaultExchange
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
name|PollingConsumerSupport
import|;
end_import

begin_comment
comment|/**  * {@link PollingConsumer} that polls a data queue for data  */
end_comment

begin_class
DECL|class|Jt400DataQueueConsumer
specifier|public
class|class
name|Jt400DataQueueConsumer
extends|extends
name|PollingConsumerSupport
argument_list|<
name|Exchange
argument_list|>
block|{
DECL|field|endpoint
specifier|private
specifier|final
name|Jt400DataQueueEndpoint
name|endpoint
decl_stmt|;
comment|/**      * Creates a new consumer instance      */
DECL|method|Jt400DataQueueConsumer (Jt400DataQueueEndpoint endpoint)
specifier|protected
name|Jt400DataQueueConsumer
parameter_list|(
name|Jt400DataQueueEndpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
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
if|if
condition|(
operator|!
name|endpoint
operator|.
name|getSystem
argument_list|()
operator|.
name|isConnected
argument_list|()
condition|)
block|{
name|endpoint
operator|.
name|getSystem
argument_list|()
operator|.
name|connectService
argument_list|(
name|AS400
operator|.
name|DATAQUEUE
argument_list|)
expr_stmt|;
block|}
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
if|if
condition|(
name|endpoint
operator|.
name|getSystem
argument_list|()
operator|.
name|isConnected
argument_list|()
condition|)
block|{
name|endpoint
operator|.
name|getSystem
argument_list|()
operator|.
name|disconnectAllServices
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * {@link Jt400DataQueueConsumer#receive(long)}      */
DECL|method|receive ()
specifier|public
name|Exchange
name|receive
parameter_list|()
block|{
comment|// -1 to indicate a blocking read from data queue
return|return
name|receive
argument_list|(
operator|-
literal|1
argument_list|)
return|;
block|}
comment|/**      * {@link Jt400DataQueueConsumer#receive(long)}      */
DECL|method|receiveNoWait ()
specifier|public
name|Exchange
name|receiveNoWait
parameter_list|()
block|{
return|return
name|receive
argument_list|(
literal|0
argument_list|)
return|;
block|}
comment|/**      * Receives an entry from a data queue and returns an {@link Exchange} to      * send this data If the endpoint's format is set to {@link Format#binary},      * the data queue entry's data will be received/sent as a      *<code>byte[]</code>. If the endpoint's format is set to      * {@link Format#text}, the data queue entry's data will be received/sent as      * a<code>String</code>.      *      * @param timeout time to wait when reading from data queue. A value of -1      *            indicates a blocking read.      */
DECL|method|receive (long timeout)
specifier|public
name|Exchange
name|receive
parameter_list|(
name|long
name|timeout
parameter_list|)
block|{
name|DataQueue
name|queue
init|=
name|endpoint
operator|.
name|getDataQueue
argument_list|()
decl_stmt|;
try|try
block|{
name|DataQueueEntry
name|entry
decl_stmt|;
if|if
condition|(
name|timeout
operator|>=
literal|0
condition|)
block|{
name|entry
operator|=
name|queue
operator|.
name|read
argument_list|(
operator|(
name|int
operator|)
name|timeout
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|entry
operator|=
name|queue
operator|.
name|read
argument_list|()
expr_stmt|;
block|}
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|endpoint
operator|.
name|getCamelContext
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|entry
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|endpoint
operator|.
name|getFormat
argument_list|()
operator|==
name|Format
operator|.
name|binary
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|entry
operator|.
name|getData
argument_list|()
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
name|entry
operator|.
name|getString
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|exchange
return|;
block|}
block|}
catch|catch
parameter_list|(
name|AS400SecurityException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Unable to read from data queue: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|ErrorCompletingRequestException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Unable to read from data queue: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Unable to read from data queue: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|IllegalObjectTypeException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Unable to read from data queue: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Unable to read from data queue: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|ObjectDoesNotExistException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Unable to read from data queue: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

