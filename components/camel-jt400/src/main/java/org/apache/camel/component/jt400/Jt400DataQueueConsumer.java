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
name|com
operator|.
name|ibm
operator|.
name|as400
operator|.
name|access
operator|.
name|BaseDataQueue
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
name|KeyedDataQueue
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
name|KeyedDataQueueEntry
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
name|impl
operator|.
name|ScheduledPollConsumer
import|;
end_import

begin_comment
comment|/**  * A scheduled {@link org.apache.camel.Consumer} that polls a data queue for data  */
end_comment

begin_class
DECL|class|Jt400DataQueueConsumer
specifier|public
class|class
name|Jt400DataQueueConsumer
extends|extends
name|ScheduledPollConsumer
block|{
comment|/**      * Performs the lifecycle logic of this consumer.      */
DECL|field|queueService
specifier|private
specifier|final
name|Jt400DataQueueService
name|queueService
decl_stmt|;
comment|/**      * Creates a new consumer instance      */
DECL|method|Jt400DataQueueConsumer (Jt400Endpoint endpoint, Processor processor)
specifier|public
name|Jt400DataQueueConsumer
parameter_list|(
name|Jt400Endpoint
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
name|queueService
operator|=
operator|new
name|Jt400DataQueueService
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|Jt400Endpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|Jt400Endpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|poll ()
specifier|protected
name|int
name|poll
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
name|receive
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getReadTimeout
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|exchange
operator|!=
literal|null
condition|)
block|{
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
return|return
literal|1
return|;
block|}
else|else
block|{
return|return
literal|0
return|;
block|}
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
name|queueService
operator|.
name|start
argument_list|()
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
name|queueService
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Deprecated
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
annotation|@
name|Deprecated
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
comment|/**      * Receives an entry from a data queue and returns an {@link Exchange} to      * send this data If the endpoint's format is set to {@link org.apache.camel.component.jt400.Jt400Configuration.Format#binary},      * the data queue entry's data will be received/sent as a      *<code>byte[]</code>. If the endpoint's format is set to      * {@link org.apache.camel.component.jt400.Jt400Configuration.Format#text}, the data queue entry's data will be received/sent as      * a<code>String</code>.      *<p/>      * The following message headers may be set by the receiver      *<ul>      *<li>SENDER_INFORMATION: The Sender Information from the Data Queue</li>      *<li>KEY: The message key if the endpoint is configured to connect to a<code>KeyedDataQueue</code></li>      *</ul>      *      * @param timeout time to wait when reading from data queue. A value of -1      *                indicates a blocking read.      */
DECL|method|receive (long timeout)
specifier|public
name|Exchange
name|receive
parameter_list|(
name|long
name|timeout
parameter_list|)
block|{
name|BaseDataQueue
name|queue
init|=
name|queueService
operator|.
name|getDataQueue
argument_list|()
decl_stmt|;
try|try
block|{
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|isKeyed
argument_list|()
condition|)
block|{
return|return
name|receive
argument_list|(
operator|(
name|KeyedDataQueue
operator|)
name|queue
argument_list|,
name|timeout
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|receive
argument_list|(
operator|(
name|DataQueue
operator|)
name|queue
argument_list|,
name|timeout
argument_list|)
return|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Unable to read from data queue: "
operator|+
name|queue
operator|.
name|getName
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|receive (DataQueue queue, long timeout)
specifier|private
name|Exchange
name|receive
parameter_list|(
name|DataQueue
name|queue
parameter_list|,
name|long
name|timeout
parameter_list|)
throws|throws
name|Exception
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
name|int
name|seconds
init|=
operator|(
name|int
operator|)
name|timeout
operator|/
literal|1000
decl_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"Reading from data queue: {} with {} seconds timeout"
argument_list|,
name|queue
operator|.
name|getName
argument_list|()
argument_list|,
name|seconds
argument_list|)
expr_stmt|;
name|entry
operator|=
name|queue
operator|.
name|read
argument_list|(
name|seconds
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Reading from data queue: {} with no timeout"
argument_list|,
name|queue
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|entry
operator|=
name|queue
operator|.
name|read
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
name|Exchange
name|exchange
init|=
name|getEndpoint
argument_list|()
operator|.
name|createExchange
argument_list|()
decl_stmt|;
if|if
condition|(
name|entry
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Jt400Endpoint
operator|.
name|SENDER_INFORMATION
argument_list|,
name|entry
operator|.
name|getSenderInformation
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|getFormat
argument_list|()
operator|==
name|Jt400Configuration
operator|.
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
return|return
literal|null
return|;
block|}
DECL|method|receive (KeyedDataQueue queue, long timeout)
specifier|private
name|Exchange
name|receive
parameter_list|(
name|KeyedDataQueue
name|queue
parameter_list|,
name|long
name|timeout
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|key
init|=
name|getEndpoint
argument_list|()
operator|.
name|getSearchKey
argument_list|()
decl_stmt|;
name|String
name|searchType
init|=
name|getEndpoint
argument_list|()
operator|.
name|getSearchType
argument_list|()
operator|.
name|name
argument_list|()
decl_stmt|;
name|KeyedDataQueueEntry
name|entry
decl_stmt|;
if|if
condition|(
name|timeout
operator|>=
literal|0
condition|)
block|{
name|int
name|seconds
init|=
operator|(
name|int
operator|)
name|timeout
operator|/
literal|1000
decl_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"Reading from data queue: {} with {} seconds timeout"
argument_list|,
name|queue
operator|.
name|getName
argument_list|()
argument_list|,
name|seconds
argument_list|)
expr_stmt|;
name|entry
operator|=
name|queue
operator|.
name|read
argument_list|(
name|key
argument_list|,
name|seconds
argument_list|,
name|searchType
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Reading from data queue: {} with no timeout"
argument_list|,
name|queue
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|entry
operator|=
name|queue
operator|.
name|read
argument_list|(
name|key
argument_list|,
operator|-
literal|1
argument_list|,
name|searchType
argument_list|)
expr_stmt|;
block|}
name|Exchange
name|exchange
init|=
name|getEndpoint
argument_list|()
operator|.
name|createExchange
argument_list|()
decl_stmt|;
if|if
condition|(
name|entry
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Jt400Endpoint
operator|.
name|SENDER_INFORMATION
argument_list|,
name|entry
operator|.
name|getSenderInformation
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|getFormat
argument_list|()
operator|==
name|Jt400Configuration
operator|.
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
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Jt400Endpoint
operator|.
name|KEY
argument_list|,
name|entry
operator|.
name|getKey
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
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Jt400Endpoint
operator|.
name|KEY
argument_list|,
name|entry
operator|.
name|getKeyString
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|exchange
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

