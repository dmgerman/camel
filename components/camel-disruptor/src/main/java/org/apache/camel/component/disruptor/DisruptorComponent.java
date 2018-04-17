begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.disruptor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|disruptor
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
name|impl
operator|.
name|UriEndpointComponent
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
name|Metadata
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
comment|/**  * An implementation of the<a href="https://github.com/sirchia/camel-disruptor">Disruptor component</a>  * for asynchronous SEDA exchanges on an  *<a href="https://github.com/LMAX-Exchange/disruptor">LMAX Disruptor</a> within a CamelContext  */
end_comment

begin_class
DECL|class|DisruptorComponent
specifier|public
class|class
name|DisruptorComponent
extends|extends
name|UriEndpointComponent
block|{
DECL|field|DEFAULT_BUFFER_SIZE
specifier|public
specifier|static
specifier|final
name|int
name|DEFAULT_BUFFER_SIZE
init|=
literal|1024
decl_stmt|;
DECL|field|MAX_CONCURRENT_CONSUMERS
specifier|public
specifier|static
specifier|final
name|int
name|MAX_CONCURRENT_CONSUMERS
init|=
literal|500
decl_stmt|;
DECL|field|LOGGER
specifier|private
specifier|static
specifier|final
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|DisruptorComponent
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|""
operator|+
name|DEFAULT_BUFFER_SIZE
argument_list|)
DECL|field|bufferSize
specifier|private
name|int
name|bufferSize
init|=
operator|-
literal|1
decl_stmt|;
comment|//for SEDA compatibility only
DECL|field|queueSize
specifier|private
name|int
name|queueSize
init|=
operator|-
literal|1
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|defaultValue
operator|=
literal|"1"
argument_list|)
DECL|field|defaultConcurrentConsumers
specifier|private
name|int
name|defaultConcurrentConsumers
init|=
literal|1
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|)
DECL|field|defaultMultipleConsumers
specifier|private
name|boolean
name|defaultMultipleConsumers
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|defaultValue
operator|=
literal|"Multi"
argument_list|)
DECL|field|defaultProducerType
specifier|private
name|DisruptorProducerType
name|defaultProducerType
init|=
name|DisruptorProducerType
operator|.
name|Multi
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|defaultValue
operator|=
literal|"Blocking"
argument_list|)
DECL|field|defaultWaitStrategy
specifier|private
name|DisruptorWaitStrategy
name|defaultWaitStrategy
init|=
name|DisruptorWaitStrategy
operator|.
name|Blocking
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|defaultBlockWhenFull
specifier|private
name|boolean
name|defaultBlockWhenFull
init|=
literal|true
decl_stmt|;
comment|//synchronized access guarded by this
DECL|field|disruptors
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|DisruptorReference
argument_list|>
name|disruptors
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|method|DisruptorComponent ()
specifier|public
name|DisruptorComponent
parameter_list|()
block|{
name|super
argument_list|(
name|DisruptorEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createEndpoint (final String uri, final String remaining, final Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
specifier|final
name|String
name|uri
parameter_list|,
specifier|final
name|String
name|remaining
parameter_list|,
specifier|final
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
specifier|final
name|int
name|concurrentConsumers
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
specifier|final
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
name|concurrentConsumers
operator|>
name|MAX_CONCURRENT_CONSUMERS
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The limitConcurrentConsumers flag in set to true. ConcurrentConsumers cannot be set at a value greater than "
operator|+
name|MAX_CONCURRENT_CONSUMERS
operator|+
literal|" was "
operator|+
name|concurrentConsumers
argument_list|)
throw|;
block|}
if|if
condition|(
name|concurrentConsumers
operator|<
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"concurrentConsumers found to be "
operator|+
name|concurrentConsumers
operator|+
literal|", must be greater than 0"
argument_list|)
throw|;
block|}
name|int
name|size
init|=
literal|0
decl_stmt|;
if|if
condition|(
name|parameters
operator|.
name|containsKey
argument_list|(
literal|"size"
argument_list|)
condition|)
block|{
name|size
operator|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"size"
argument_list|,
name|int
operator|.
name|class
argument_list|)
expr_stmt|;
if|if
condition|(
name|size
operator|<=
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"size found to be "
operator|+
name|size
operator|+
literal|", must be greater than 0"
argument_list|)
throw|;
block|}
block|}
comment|// Check if the pollTimeout argument is set (may be the case if Disruptor component is used as drop-in
comment|// replacement for the SEDA component.
if|if
condition|(
name|parameters
operator|.
name|containsKey
argument_list|(
literal|"pollTimeout"
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The 'pollTimeout' argument is not supported by the Disruptor component"
argument_list|)
throw|;
block|}
specifier|final
name|DisruptorWaitStrategy
name|waitStrategy
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"waitStrategy"
argument_list|,
name|DisruptorWaitStrategy
operator|.
name|class
argument_list|,
name|defaultWaitStrategy
argument_list|)
decl_stmt|;
specifier|final
name|DisruptorProducerType
name|producerType
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"producerType"
argument_list|,
name|DisruptorProducerType
operator|.
name|class
argument_list|,
name|defaultProducerType
argument_list|)
decl_stmt|;
specifier|final
name|boolean
name|multipleConsumers
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"multipleConsumers"
argument_list|,
name|boolean
operator|.
name|class
argument_list|,
name|defaultMultipleConsumers
argument_list|)
decl_stmt|;
specifier|final
name|boolean
name|blockWhenFull
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"blockWhenFull"
argument_list|,
name|boolean
operator|.
name|class
argument_list|,
name|defaultBlockWhenFull
argument_list|)
decl_stmt|;
specifier|final
name|DisruptorReference
name|disruptorReference
init|=
name|getOrCreateDisruptor
argument_list|(
name|uri
argument_list|,
name|remaining
argument_list|,
name|size
argument_list|,
name|producerType
argument_list|,
name|waitStrategy
argument_list|)
decl_stmt|;
specifier|final
name|DisruptorEndpoint
name|disruptorEndpoint
init|=
operator|new
name|DisruptorEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|disruptorReference
argument_list|,
name|concurrentConsumers
argument_list|,
name|multipleConsumers
argument_list|,
name|blockWhenFull
argument_list|)
decl_stmt|;
name|disruptorEndpoint
operator|.
name|setWaitStrategy
argument_list|(
name|waitStrategy
argument_list|)
expr_stmt|;
name|disruptorEndpoint
operator|.
name|setProducerType
argument_list|(
name|producerType
argument_list|)
expr_stmt|;
name|disruptorEndpoint
operator|.
name|configureProperties
argument_list|(
name|parameters
argument_list|)
expr_stmt|;
return|return
name|disruptorEndpoint
return|;
block|}
DECL|method|getOrCreateDisruptor (final String uri, final String name, final int size, final DisruptorProducerType producerType, final DisruptorWaitStrategy waitStrategy)
specifier|private
name|DisruptorReference
name|getOrCreateDisruptor
parameter_list|(
specifier|final
name|String
name|uri
parameter_list|,
specifier|final
name|String
name|name
parameter_list|,
specifier|final
name|int
name|size
parameter_list|,
specifier|final
name|DisruptorProducerType
name|producerType
parameter_list|,
specifier|final
name|DisruptorWaitStrategy
name|waitStrategy
parameter_list|)
throws|throws
name|Exception
block|{
specifier|final
name|String
name|key
init|=
name|getDisruptorKey
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|int
name|sizeToUse
decl_stmt|;
if|if
condition|(
name|size
operator|>
literal|0
condition|)
block|{
name|sizeToUse
operator|=
name|size
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|bufferSize
operator|>
literal|0
condition|)
block|{
name|sizeToUse
operator|=
name|bufferSize
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|queueSize
operator|>
literal|0
condition|)
block|{
name|sizeToUse
operator|=
name|queueSize
expr_stmt|;
block|}
else|else
block|{
name|sizeToUse
operator|=
name|DEFAULT_BUFFER_SIZE
expr_stmt|;
block|}
name|sizeToUse
operator|=
name|powerOfTwo
argument_list|(
name|sizeToUse
argument_list|)
expr_stmt|;
synchronized|synchronized
init|(
name|this
init|)
block|{
name|DisruptorReference
name|ref
init|=
name|getDisruptors
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
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Creating new disruptor for key {}"
argument_list|,
name|key
argument_list|)
expr_stmt|;
name|ref
operator|=
operator|new
name|DisruptorReference
argument_list|(
name|this
argument_list|,
name|uri
argument_list|,
name|name
argument_list|,
name|sizeToUse
argument_list|,
name|producerType
argument_list|,
name|waitStrategy
argument_list|)
expr_stmt|;
name|getDisruptors
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
else|else
block|{
comment|//if size was explicitly requested, the size to use should match the retrieved DisruptorReference
if|if
condition|(
name|size
operator|!=
literal|0
operator|&&
name|ref
operator|.
name|getBufferSize
argument_list|()
operator|!=
name|sizeToUse
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
name|ref
operator|.
name|getBufferSize
argument_list|()
operator|+
literal|" does not match given queue size "
operator|+
name|sizeToUse
argument_list|)
throw|;
block|}
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Reusing disruptor {} for key {}"
argument_list|,
name|ref
argument_list|,
name|key
argument_list|)
expr_stmt|;
block|}
return|return
name|ref
return|;
block|}
block|}
DECL|method|powerOfTwo (int size)
specifier|private
specifier|static
name|int
name|powerOfTwo
parameter_list|(
name|int
name|size
parameter_list|)
block|{
name|size
operator|--
expr_stmt|;
name|size
operator||=
name|size
operator|>>
literal|1
expr_stmt|;
name|size
operator||=
name|size
operator|>>
literal|2
expr_stmt|;
name|size
operator||=
name|size
operator|>>
literal|4
expr_stmt|;
name|size
operator||=
name|size
operator|>>
literal|8
expr_stmt|;
name|size
operator||=
name|size
operator|>>
literal|16
expr_stmt|;
name|size
operator|++
expr_stmt|;
return|return
name|size
return|;
block|}
DECL|method|getDisruptorKey (String uri)
specifier|public
specifier|static
name|String
name|getDisruptorKey
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
synchronized|synchronized
init|(
name|this
init|)
block|{
name|getDisruptors
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
DECL|method|getDisruptors ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|DisruptorReference
argument_list|>
name|getDisruptors
parameter_list|()
block|{
return|return
name|disruptors
return|;
block|}
DECL|method|getDefaultConcurrentConsumers ()
specifier|public
name|int
name|getDefaultConcurrentConsumers
parameter_list|()
block|{
return|return
name|defaultConcurrentConsumers
return|;
block|}
comment|/**      * To configure the default number of concurrent consumers      */
DECL|method|setDefaultConcurrentConsumers (final int defaultConcurrentConsumers)
specifier|public
name|void
name|setDefaultConcurrentConsumers
parameter_list|(
specifier|final
name|int
name|defaultConcurrentConsumers
parameter_list|)
block|{
name|this
operator|.
name|defaultConcurrentConsumers
operator|=
name|defaultConcurrentConsumers
expr_stmt|;
block|}
DECL|method|isDefaultMultipleConsumers ()
specifier|public
name|boolean
name|isDefaultMultipleConsumers
parameter_list|()
block|{
return|return
name|defaultMultipleConsumers
return|;
block|}
comment|/**      * To configure the default value for multiple consumers      */
DECL|method|setDefaultMultipleConsumers (final boolean defaultMultipleConsumers)
specifier|public
name|void
name|setDefaultMultipleConsumers
parameter_list|(
specifier|final
name|boolean
name|defaultMultipleConsumers
parameter_list|)
block|{
name|this
operator|.
name|defaultMultipleConsumers
operator|=
name|defaultMultipleConsumers
expr_stmt|;
block|}
DECL|method|getDefaultProducerType ()
specifier|public
name|DisruptorProducerType
name|getDefaultProducerType
parameter_list|()
block|{
return|return
name|defaultProducerType
return|;
block|}
comment|/**      * To configure the default value for DisruptorProducerType      *<p/>      * The default value is Multi.      */
DECL|method|setDefaultProducerType (final DisruptorProducerType defaultProducerType)
specifier|public
name|void
name|setDefaultProducerType
parameter_list|(
specifier|final
name|DisruptorProducerType
name|defaultProducerType
parameter_list|)
block|{
name|this
operator|.
name|defaultProducerType
operator|=
name|defaultProducerType
expr_stmt|;
block|}
DECL|method|getDefaultWaitStrategy ()
specifier|public
name|DisruptorWaitStrategy
name|getDefaultWaitStrategy
parameter_list|()
block|{
return|return
name|defaultWaitStrategy
return|;
block|}
comment|/**      * To configure the default value for DisruptorWaitStrategy      *<p/>      * The default value is Blocking.      */
DECL|method|setDefaultWaitStrategy (final DisruptorWaitStrategy defaultWaitStrategy)
specifier|public
name|void
name|setDefaultWaitStrategy
parameter_list|(
specifier|final
name|DisruptorWaitStrategy
name|defaultWaitStrategy
parameter_list|)
block|{
name|this
operator|.
name|defaultWaitStrategy
operator|=
name|defaultWaitStrategy
expr_stmt|;
block|}
DECL|method|isDefaultBlockWhenFull ()
specifier|public
name|boolean
name|isDefaultBlockWhenFull
parameter_list|()
block|{
return|return
name|defaultBlockWhenFull
return|;
block|}
comment|/**      * To configure the default value for block when full      *<p/>      * The default value is true.      */
DECL|method|setDefaultBlockWhenFull (boolean defaultBlockWhenFull)
specifier|public
name|void
name|setDefaultBlockWhenFull
parameter_list|(
name|boolean
name|defaultBlockWhenFull
parameter_list|)
block|{
name|this
operator|.
name|defaultBlockWhenFull
operator|=
name|defaultBlockWhenFull
expr_stmt|;
block|}
comment|/**      * To configure the ring buffer size      */
annotation|@
name|Deprecated
DECL|method|setQueueSize (final int size)
specifier|public
name|void
name|setQueueSize
parameter_list|(
specifier|final
name|int
name|size
parameter_list|)
block|{
name|queueSize
operator|=
name|size
expr_stmt|;
block|}
annotation|@
name|Deprecated
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
comment|/**      * To configure the ring buffer size      */
DECL|method|setBufferSize (final int size)
specifier|public
name|void
name|setBufferSize
parameter_list|(
specifier|final
name|int
name|size
parameter_list|)
block|{
name|bufferSize
operator|=
name|size
expr_stmt|;
block|}
DECL|method|getBufferSize ()
specifier|public
name|int
name|getBufferSize
parameter_list|()
block|{
return|return
name|bufferSize
return|;
block|}
DECL|method|onShutdownEndpoint (DisruptorEndpoint disruptorEndpoint)
specifier|public
name|void
name|onShutdownEndpoint
parameter_list|(
name|DisruptorEndpoint
name|disruptorEndpoint
parameter_list|)
block|{
name|String
name|disruptorKey
init|=
name|getDisruptorKey
argument_list|(
name|disruptorEndpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
decl_stmt|;
name|DisruptorReference
name|disruptorReference
init|=
name|getDisruptors
argument_list|()
operator|.
name|get
argument_list|(
name|disruptorKey
argument_list|)
decl_stmt|;
if|if
condition|(
name|disruptorReference
operator|.
name|getEndpointCount
argument_list|()
operator|==
literal|0
condition|)
block|{
comment|//the last disruptor has been removed, we can delete the disruptor
name|getDisruptors
argument_list|()
operator|.
name|remove
argument_list|(
name|disruptorKey
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

