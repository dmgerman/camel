begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.seda
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|seda
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|BlockingQueue
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
name|LinkedBlockingQueue
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
name|impl
operator|.
name|DefaultComponent
import|;
end_import

begin_comment
comment|/**  * An implementation of the<a href="http://camel.apache.org/seda.html">SEDA components</a>  * for asynchronous SEDA exchanges on a {@link BlockingQueue} within a CamelContext  *  * @version   */
end_comment

begin_class
DECL|class|SedaComponent
specifier|public
class|class
name|SedaComponent
extends|extends
name|DefaultComponent
block|{
DECL|field|maxConcurrentConsumers
specifier|protected
specifier|final
name|int
name|maxConcurrentConsumers
init|=
literal|500
decl_stmt|;
DECL|field|queueSize
specifier|protected
name|int
name|queueSize
decl_stmt|;
DECL|field|defaultConcurrentConsumers
specifier|protected
name|int
name|defaultConcurrentConsumers
init|=
literal|1
decl_stmt|;
DECL|field|queues
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|QueueReference
argument_list|>
name|queues
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|QueueReference
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|setQueueSize (int size)
specifier|public
name|void
name|setQueueSize
parameter_list|(
name|int
name|size
parameter_list|)
block|{
name|queueSize
operator|=
name|size
expr_stmt|;
block|}
DECL|method|getQueueSize ()
specifier|public
name|int
name|getQueueSize
parameter_list|()
block|{
return|return
name|queueSize
return|;
block|}
DECL|method|setConcurrentConsumers (int size)
specifier|public
name|void
name|setConcurrentConsumers
parameter_list|(
name|int
name|size
parameter_list|)
block|{
name|defaultConcurrentConsumers
operator|=
name|size
expr_stmt|;
block|}
DECL|method|getConcurrentConsumers ()
specifier|public
name|int
name|getConcurrentConsumers
parameter_list|()
block|{
return|return
name|defaultConcurrentConsumers
return|;
block|}
DECL|method|createQueue (String uri, Map<String, Object> parameters)
specifier|public
specifier|synchronized
name|BlockingQueue
argument_list|<
name|Exchange
argument_list|>
name|createQueue
parameter_list|(
name|String
name|uri
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
block|{
name|String
name|key
init|=
name|getQueueKey
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|QueueReference
name|ref
init|=
name|getQueues
argument_list|()
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|ref
operator|!=
literal|null
condition|)
block|{
comment|// add the reference before returning queue
name|ref
operator|.
name|addReference
argument_list|()
expr_stmt|;
return|return
name|ref
operator|.
name|getQueue
argument_list|()
return|;
block|}
comment|// create queue
name|BlockingQueue
argument_list|<
name|Exchange
argument_list|>
name|queue
decl_stmt|;
name|Integer
name|size
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"size"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|size
operator|!=
literal|null
operator|&&
name|size
operator|>
literal|0
condition|)
block|{
name|queue
operator|=
operator|new
name|LinkedBlockingQueue
argument_list|<
name|Exchange
argument_list|>
argument_list|(
name|size
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|getQueueSize
argument_list|()
operator|>
literal|0
condition|)
block|{
name|queue
operator|=
operator|new
name|LinkedBlockingQueue
argument_list|<
name|Exchange
argument_list|>
argument_list|(
name|getQueueSize
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|queue
operator|=
operator|new
name|LinkedBlockingQueue
argument_list|<
name|Exchange
argument_list|>
argument_list|()
expr_stmt|;
block|}
block|}
comment|// create and add a new reference queue
name|ref
operator|=
operator|new
name|QueueReference
argument_list|(
name|queue
argument_list|)
expr_stmt|;
name|ref
operator|.
name|addReference
argument_list|()
expr_stmt|;
name|getQueues
argument_list|()
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|ref
argument_list|)
expr_stmt|;
return|return
name|queue
return|;
block|}
DECL|method|getQueues ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|QueueReference
argument_list|>
name|getQueues
parameter_list|()
block|{
return|return
name|queues
return|;
block|}
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|int
name|consumers
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"concurrentConsumers"
argument_list|,
name|Integer
operator|.
name|class
argument_list|,
name|defaultConcurrentConsumers
argument_list|)
decl_stmt|;
name|boolean
name|limitConcurrentConsumers
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"limitConcurrentConsumers"
argument_list|,
name|Boolean
operator|.
name|class
argument_list|,
literal|true
argument_list|)
decl_stmt|;
if|if
condition|(
name|limitConcurrentConsumers
operator|&&
name|consumers
operator|>
name|maxConcurrentConsumers
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The limitConcurrentConsumers flag in set to true. ConcurrentConsumers cannot be set at a value greater than "
operator|+
name|maxConcurrentConsumers
operator|+
literal|" was "
operator|+
name|consumers
argument_list|)
throw|;
block|}
name|SedaEndpoint
name|answer
init|=
operator|new
name|SedaEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|createQueue
argument_list|(
name|uri
argument_list|,
name|parameters
argument_list|)
argument_list|,
name|consumers
argument_list|)
decl_stmt|;
name|answer
operator|.
name|configureProperties
argument_list|(
name|parameters
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|getQueueKey (String uri)
specifier|public
name|String
name|getQueueKey
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
if|if
condition|(
name|uri
operator|.
name|contains
argument_list|(
literal|"?"
argument_list|)
condition|)
block|{
comment|// strip parameters
name|uri
operator|=
name|uri
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|uri
operator|.
name|indexOf
argument_list|(
literal|'?'
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|uri
return|;
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
name|getQueues
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
comment|/**      * On shutting down the endpoint      *       * @param endpoint the endpoint      */
DECL|method|onShutdownEndpoint (SedaEndpoint endpoint)
name|void
name|onShutdownEndpoint
parameter_list|(
name|SedaEndpoint
name|endpoint
parameter_list|)
block|{
comment|// we need to remove the endpoint from the reference counter
name|String
name|key
init|=
name|getQueueKey
argument_list|(
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
decl_stmt|;
name|QueueReference
name|ref
init|=
name|getQueues
argument_list|()
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|ref
operator|!=
literal|null
condition|)
block|{
name|ref
operator|.
name|removeReference
argument_list|()
expr_stmt|;
if|if
condition|(
name|ref
operator|.
name|getCount
argument_list|()
operator|<=
literal|0
condition|)
block|{
comment|// reference no longer needed so remove from queues
name|getQueues
argument_list|()
operator|.
name|remove
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Holder for queue references.      *<p/>      * This is used to keep track of the usages of the queues, so we know when a queue is no longer      * in use, and can safely be discarded.      */
DECL|class|QueueReference
specifier|public
specifier|static
specifier|final
class|class
name|QueueReference
block|{
DECL|field|queue
specifier|private
specifier|final
name|BlockingQueue
argument_list|<
name|Exchange
argument_list|>
name|queue
decl_stmt|;
DECL|field|count
specifier|private
specifier|volatile
name|int
name|count
decl_stmt|;
DECL|method|QueueReference (BlockingQueue<Exchange> queue)
specifier|private
name|QueueReference
parameter_list|(
name|BlockingQueue
argument_list|<
name|Exchange
argument_list|>
name|queue
parameter_list|)
block|{
name|this
operator|.
name|queue
operator|=
name|queue
expr_stmt|;
block|}
DECL|method|addReference ()
name|void
name|addReference
parameter_list|()
block|{
name|count
operator|++
expr_stmt|;
block|}
DECL|method|removeReference ()
name|void
name|removeReference
parameter_list|()
block|{
name|count
operator|--
expr_stmt|;
block|}
comment|/**          * Gets the reference counter          */
DECL|method|getCount ()
specifier|public
name|int
name|getCount
parameter_list|()
block|{
return|return
name|count
return|;
block|}
comment|/**          * Gets the queue          */
DECL|method|getQueue ()
specifier|public
name|BlockingQueue
argument_list|<
name|Exchange
argument_list|>
name|getQueue
parameter_list|()
block|{
return|return
name|queue
return|;
block|}
block|}
block|}
end_class

end_unit

