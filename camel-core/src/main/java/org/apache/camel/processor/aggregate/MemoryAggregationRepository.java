begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.aggregate
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|aggregate
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|ConcurrentHashMap
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
name|ConcurrentMap
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
name|CamelContext
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
name|spi
operator|.
name|OptimisticLockingAggregationRepository
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
name|ServiceSupport
import|;
end_import

begin_comment
comment|/**  * A memory based {@link org.apache.camel.spi.AggregationRepository} which stores {@link Exchange}s in memory only.  *  * Supports both optimistic locking and non-optimistic locking modes. Defaults to non-optimistic locking mode.  *  * @version   */
end_comment

begin_class
DECL|class|MemoryAggregationRepository
specifier|public
class|class
name|MemoryAggregationRepository
extends|extends
name|ServiceSupport
implements|implements
name|OptimisticLockingAggregationRepository
block|{
DECL|field|cache
specifier|private
specifier|final
name|ConcurrentMap
argument_list|<
name|String
argument_list|,
name|Exchange
argument_list|>
name|cache
init|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|Exchange
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|optimisticLocking
specifier|private
specifier|final
name|boolean
name|optimisticLocking
decl_stmt|;
DECL|method|MemoryAggregationRepository ()
specifier|public
name|MemoryAggregationRepository
parameter_list|()
block|{
name|this
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|MemoryAggregationRepository (boolean optimisticLocking)
specifier|public
name|MemoryAggregationRepository
parameter_list|(
name|boolean
name|optimisticLocking
parameter_list|)
block|{
name|this
operator|.
name|optimisticLocking
operator|=
name|optimisticLocking
expr_stmt|;
block|}
DECL|method|add (CamelContext camelContext, String key, Exchange oldExchange, Exchange newExchange)
specifier|public
name|Exchange
name|add
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|key
parameter_list|,
name|Exchange
name|oldExchange
parameter_list|,
name|Exchange
name|newExchange
parameter_list|)
block|{
if|if
condition|(
operator|!
name|optimisticLocking
condition|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
if|if
condition|(
name|oldExchange
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|cache
operator|.
name|putIfAbsent
argument_list|(
name|key
argument_list|,
name|newExchange
argument_list|)
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|OptimisticLockingException
argument_list|()
throw|;
block|}
block|}
else|else
block|{
if|if
condition|(
operator|!
name|cache
operator|.
name|replace
argument_list|(
name|key
argument_list|,
name|oldExchange
argument_list|,
name|newExchange
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|OptimisticLockingException
argument_list|()
throw|;
block|}
block|}
return|return
name|oldExchange
return|;
block|}
DECL|method|add (CamelContext camelContext, String key, Exchange exchange)
specifier|public
name|Exchange
name|add
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|key
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|optimisticLocking
condition|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
return|return
name|cache
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|exchange
argument_list|)
return|;
block|}
DECL|method|get (CamelContext camelContext, String key)
specifier|public
name|Exchange
name|get
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|key
parameter_list|)
block|{
return|return
name|cache
operator|.
name|get
argument_list|(
name|key
argument_list|)
return|;
block|}
DECL|method|remove (CamelContext camelContext, String key, Exchange exchange)
specifier|public
name|void
name|remove
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|key
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|optimisticLocking
condition|)
block|{
if|if
condition|(
operator|!
name|cache
operator|.
name|remove
argument_list|(
name|key
argument_list|,
name|exchange
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|OptimisticLockingException
argument_list|()
throw|;
block|}
block|}
else|else
block|{
name|cache
operator|.
name|remove
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|confirm (CamelContext camelContext, String exchangeId)
specifier|public
name|void
name|confirm
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|exchangeId
parameter_list|)
block|{
comment|// noop
block|}
DECL|method|getKeys ()
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getKeys
parameter_list|()
block|{
comment|// do not allow edits to the set
return|return
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
name|cache
operator|.
name|keySet
argument_list|()
argument_list|)
return|;
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
block|{     }
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
name|cache
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

