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
name|UriEndpointComponent
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

begin_comment
comment|/**  * An implementation of the<a href="http://camel.apache.org/seda.html">SEDA components</a>  * for asynchronous SEDA exchanges on a {@link BlockingQueue} within a CamelContext  *  * @version   */
end_comment

begin_class
DECL|class|SedaComponent
specifier|public
class|class
name|SedaComponent
extends|extends
name|UriEndpointComponent
block|{
DECL|field|log
specifier|protected
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
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
DECL|field|defaultQueueFactory
specifier|private
name|BlockingQueueFactory
argument_list|<
name|Exchange
argument_list|>
name|defaultQueueFactory
init|=
operator|new
name|LinkedBlockingQueueFactory
argument_list|<
name|Exchange
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|SedaComponent ()
specifier|public
name|SedaComponent
parameter_list|()
block|{
name|super
argument_list|(
name|SedaEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
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
DECL|method|getDefaultQueueFactory ()
specifier|public
name|BlockingQueueFactory
argument_list|<
name|Exchange
argument_list|>
name|getDefaultQueueFactory
parameter_list|()
block|{
return|return
name|defaultQueueFactory
return|;
block|}
DECL|method|setDefaultQueueFactory (BlockingQueueFactory<Exchange> defaultQueueFactory)
specifier|public
name|void
name|setDefaultQueueFactory
parameter_list|(
name|BlockingQueueFactory
argument_list|<
name|Exchange
argument_list|>
name|defaultQueueFactory
parameter_list|)
block|{
name|this
operator|.
name|defaultQueueFactory
operator|=
name|defaultQueueFactory
expr_stmt|;
block|}
comment|/**      * @deprecated use      */
annotation|@
name|Deprecated
DECL|method|getOrCreateQueue (SedaEndpoint endpoint, Integer size)
specifier|public
specifier|synchronized
name|QueueReference
name|getOrCreateQueue
parameter_list|(
name|SedaEndpoint
name|endpoint
parameter_list|,
name|Integer
name|size
parameter_list|)
block|{
return|return
name|getOrCreateQueue
argument_list|(
name|endpoint
argument_list|,
name|size
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      * @deprecated use {@link #getOrCreateQueue(SedaEndpoint, Integer, Boolean, BlockingQueueFactory)}      */
DECL|method|getOrCreateQueue (SedaEndpoint endpoint, Integer size, Boolean multipleConsumers)
specifier|public
specifier|synchronized
name|QueueReference
name|getOrCreateQueue
parameter_list|(
name|SedaEndpoint
name|endpoint
parameter_list|,
name|Integer
name|size
parameter_list|,
name|Boolean
name|multipleConsumers
parameter_list|)
block|{
return|return
name|getOrCreateQueue
argument_list|(
name|endpoint
argument_list|,
name|size
argument_list|,
name|multipleConsumers
argument_list|,
literal|null
argument_list|)
return|;
block|}
DECL|method|getOrCreateQueue (SedaEndpoint endpoint, Integer size, Boolean multipleConsumers, BlockingQueueFactory<Exchange> customQueueFactory)
specifier|public
specifier|synchronized
name|QueueReference
name|getOrCreateQueue
parameter_list|(
name|SedaEndpoint
name|endpoint
parameter_list|,
name|Integer
name|size
parameter_list|,
name|Boolean
name|multipleConsumers
parameter_list|,
name|BlockingQueueFactory
argument_list|<
name|Exchange
argument_list|>
name|customQueueFactory
parameter_list|)
block|{
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
comment|// if the given size is not provided, we just use the existing queue as is
if|if
condition|(
name|size
operator|!=
literal|null
operator|&&
operator|!
name|size
operator|.
name|equals
argument_list|(
name|ref
operator|.
name|getSize
argument_list|()
argument_list|)
condition|)
block|{
comment|// there is already a queue, so make sure the size matches
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot use existing queue "
operator|+
name|key
operator|+
literal|" as the existing queue size "
operator|+
operator|(
name|ref
operator|.
name|getSize
argument_list|()
operator|!=
literal|null
condition|?
name|ref
operator|.
name|getSize
argument_list|()
else|:
name|Integer
operator|.
name|MAX_VALUE
operator|)
operator|+
literal|" does not match given queue size "
operator|+
name|size
argument_list|)
throw|;
block|}
comment|// add the reference before returning queue
name|ref
operator|.
name|addReference
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
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
literal|"Reusing existing queue {} with size {} and reference count {}"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|key
block|,
name|size
block|,
name|ref
operator|.
name|getCount
argument_list|()
block|}
argument_list|)
expr_stmt|;
block|}
return|return
name|ref
return|;
block|}
comment|// create queue
name|BlockingQueue
argument_list|<
name|Exchange
argument_list|>
name|queue
decl_stmt|;
name|BlockingQueueFactory
argument_list|<
name|Exchange
argument_list|>
name|queueFactory
init|=
name|customQueueFactory
operator|==
literal|null
condition|?
name|defaultQueueFactory
else|:
name|customQueueFactory
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
name|queueFactory
operator|.
name|create
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
name|size
operator|=
name|getQueueSize
argument_list|()
expr_stmt|;
name|queue
operator|=
name|queueFactory
operator|.
name|create
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
name|queueFactory
operator|.
name|create
argument_list|()
expr_stmt|;
block|}
block|}
name|log
operator|.
name|debug
argument_list|(
literal|"Created queue {} with size {}"
argument_list|,
name|key
argument_list|,
name|size
argument_list|)
expr_stmt|;
comment|// create and add a new reference queue
name|ref
operator|=
operator|new
name|QueueReference
argument_list|(
name|queue
argument_list|,
name|size
argument_list|,
name|multipleConsumers
argument_list|)
expr_stmt|;
name|ref
operator|.
name|addReference
argument_list|(
name|endpoint
argument_list|)
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
name|ref
return|;
block|}
DECL|method|registerQueue (SedaEndpoint endpoint, BlockingQueue<Exchange> queue)
specifier|public
specifier|synchronized
name|QueueReference
name|registerQueue
parameter_list|(
name|SedaEndpoint
name|endpoint
parameter_list|,
name|BlockingQueue
argument_list|<
name|Exchange
argument_list|>
name|queue
parameter_list|)
block|{
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
operator|==
literal|null
condition|)
block|{
name|ref
operator|=
operator|new
name|QueueReference
argument_list|(
name|queue
argument_list|,
name|endpoint
operator|.
name|getSize
argument_list|()
argument_list|,
name|endpoint
operator|.
name|isMultipleConsumers
argument_list|()
argument_list|)
expr_stmt|;
name|ref
operator|.
name|addReference
argument_list|(
name|endpoint
argument_list|)
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
block|}
return|return
name|ref
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
DECL|method|getQueueReference (String key)
specifier|public
name|QueueReference
name|getQueueReference
parameter_list|(
name|String
name|key
parameter_list|)
block|{
return|return
name|queues
operator|.
name|get
argument_list|(
name|key
argument_list|)
return|;
block|}
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
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
comment|// Resolve queue reference
name|BlockingQueue
argument_list|<
name|Exchange
argument_list|>
name|queue
init|=
name|resolveAndRemoveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"queue"
argument_list|,
name|BlockingQueue
operator|.
name|class
argument_list|)
decl_stmt|;
name|SedaEndpoint
name|answer
decl_stmt|;
comment|// Resolve queue factory when no queue specified
if|if
condition|(
name|queue
operator|==
literal|null
condition|)
block|{
name|BlockingQueueFactory
argument_list|<
name|Exchange
argument_list|>
name|queueFactory
init|=
name|resolveAndRemoveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"queueFactory"
argument_list|,
name|BlockingQueueFactory
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// defer creating queue till endpoint is started, so we pass the
comment|// queue factory
name|answer
operator|=
operator|new
name|SedaEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|queueFactory
argument_list|,
name|consumers
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|answer
operator|=
operator|new
name|SedaEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|queue
argument_list|,
name|consumers
argument_list|)
expr_stmt|;
block|}
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
operator|&&
name|endpoint
operator|.
name|getConsumers
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
comment|// only remove the endpoint when the consumers are removed
name|ref
operator|.
name|removeReference
argument_list|(
name|endpoint
argument_list|)
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
block|}
end_class

end_unit

