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
name|LinkedList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|Exchange
import|;
end_import

begin_comment
comment|/**  * Holder for queue references.  *<p/>  * This is used to keep track of the usages of the queues, so we know when a queue is no longer  * in use, and can safely be discarded.  */
end_comment

begin_class
DECL|class|QueueReference
specifier|public
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
DECL|field|size
specifier|private
name|Integer
name|size
decl_stmt|;
DECL|field|multipleConsumers
specifier|private
name|Boolean
name|multipleConsumers
decl_stmt|;
DECL|field|endpoints
specifier|private
name|List
argument_list|<
name|SedaEndpoint
argument_list|>
name|endpoints
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|method|QueueReference (BlockingQueue<Exchange> queue, Integer size, Boolean multipleConsumers)
name|QueueReference
parameter_list|(
name|BlockingQueue
argument_list|<
name|Exchange
argument_list|>
name|queue
parameter_list|,
name|Integer
name|size
parameter_list|,
name|Boolean
name|multipleConsumers
parameter_list|)
block|{
name|this
operator|.
name|queue
operator|=
name|queue
expr_stmt|;
name|this
operator|.
name|size
operator|=
name|size
expr_stmt|;
name|this
operator|.
name|multipleConsumers
operator|=
name|multipleConsumers
expr_stmt|;
block|}
DECL|method|addReference (SedaEndpoint endpoint)
specifier|synchronized
name|void
name|addReference
parameter_list|(
name|SedaEndpoint
name|endpoint
parameter_list|)
block|{
if|if
condition|(
operator|!
name|endpoints
operator|.
name|contains
argument_list|(
name|endpoint
argument_list|)
condition|)
block|{
name|endpoints
operator|.
name|add
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
comment|// update the multipleConsumers setting if need
if|if
condition|(
name|endpoint
operator|.
name|isMultipleConsumers
argument_list|()
condition|)
block|{
name|multipleConsumers
operator|=
literal|true
expr_stmt|;
block|}
block|}
block|}
DECL|method|removeReference (SedaEndpoint endpoint)
specifier|synchronized
name|void
name|removeReference
parameter_list|(
name|SedaEndpoint
name|endpoint
parameter_list|)
block|{
if|if
condition|(
name|endpoints
operator|.
name|contains
argument_list|(
name|endpoint
argument_list|)
condition|)
block|{
name|endpoints
operator|.
name|remove
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Gets the reference counter      */
DECL|method|getCount ()
specifier|public
specifier|synchronized
name|int
name|getCount
parameter_list|()
block|{
return|return
name|endpoints
operator|.
name|size
argument_list|()
return|;
block|}
comment|/**      * Gets the queue size      *      * @return<tt>null</tt> if unbounded      */
DECL|method|getSize ()
specifier|public
name|Integer
name|getSize
parameter_list|()
block|{
return|return
name|size
return|;
block|}
DECL|method|getMultipleConsumers ()
specifier|public
name|Boolean
name|getMultipleConsumers
parameter_list|()
block|{
return|return
name|multipleConsumers
return|;
block|}
comment|/**      * Gets the queue      */
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
DECL|method|hasConsumers ()
specifier|public
specifier|synchronized
name|boolean
name|hasConsumers
parameter_list|()
block|{
for|for
control|(
name|SedaEndpoint
name|endpoint
range|:
name|endpoints
control|)
block|{
if|if
condition|(
name|endpoint
operator|.
name|getConsumers
argument_list|()
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

