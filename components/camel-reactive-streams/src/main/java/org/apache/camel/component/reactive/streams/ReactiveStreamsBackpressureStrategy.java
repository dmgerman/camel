begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.reactive.streams
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|reactive
operator|.
name|streams
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

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
name|Deque
import|;
end_import

begin_comment
comment|/**  * A list of possible backpressure strategy to use when the emission of upstream items cannot respect backpressure.  */
end_comment

begin_enum
DECL|enum|ReactiveStreamsBackpressureStrategy
specifier|public
enum|enum
name|ReactiveStreamsBackpressureStrategy
block|{
comment|/**      * Buffers<em>all</em> onNext values until the downstream consumes it.      */
DECL|enumConstant|BUFFER
name|BUFFER
block|{
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|Collection
argument_list|<
name|T
argument_list|>
name|update
parameter_list|(
name|Deque
argument_list|<
name|T
argument_list|>
name|buffer
parameter_list|,
name|T
name|newItem
parameter_list|)
block|{
comment|// always buffer
name|buffer
operator|.
name|addLast
argument_list|(
name|newItem
argument_list|)
expr_stmt|;
comment|// never discard
return|return
name|Collections
operator|.
name|emptySet
argument_list|()
return|;
block|}
block|}
block|,
comment|/**      * Keeps only the oldest onNext value, discarding any future value      * until it's consumed by the downstream subscriber.      */
DECL|enumConstant|OLDEST
name|OLDEST
block|{
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|Collection
argument_list|<
name|T
argument_list|>
name|update
parameter_list|(
name|Deque
argument_list|<
name|T
argument_list|>
name|buffer
parameter_list|,
name|T
name|newItem
parameter_list|)
block|{
if|if
condition|(
name|buffer
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
comment|// the buffer has another item, so discarding the incoming one
return|return
name|Collections
operator|.
name|singletonList
argument_list|(
name|newItem
argument_list|)
return|;
block|}
else|else
block|{
comment|// add the new item to the buffer, since it was empty
name|buffer
operator|.
name|addLast
argument_list|(
name|newItem
argument_list|)
expr_stmt|;
comment|// nothing is discarded
return|return
name|Collections
operator|.
name|emptySet
argument_list|()
return|;
block|}
block|}
block|}
block|,
comment|/**      * Keeps only the latest onNext value, overwriting any previous value if the      * downstream can't keep up.      */
DECL|enumConstant|LATEST
name|LATEST
block|{
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|Collection
argument_list|<
name|T
argument_list|>
name|update
parameter_list|(
name|Deque
argument_list|<
name|T
argument_list|>
name|buffer
parameter_list|,
name|T
name|newItem
parameter_list|)
block|{
name|Collection
argument_list|<
name|T
argument_list|>
name|discarded
init|=
name|Collections
operator|.
name|emptySet
argument_list|()
decl_stmt|;
if|if
condition|(
name|buffer
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
comment|// there should be an item in the buffer,
comment|// so removing it to overwrite
name|discarded
operator|=
name|Collections
operator|.
name|singletonList
argument_list|(
name|buffer
operator|.
name|removeFirst
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// add the new item to the buffer
comment|// (it should be the only item in the buffer now)
name|buffer
operator|.
name|addLast
argument_list|(
name|newItem
argument_list|)
expr_stmt|;
return|return
name|discarded
return|;
block|}
block|}
block|;
comment|/**      * Updates the buffer and returns a list of discarded elements (if any).      *      * @param buffer the buffer to update      * @param newItem the elment that should possibly be inserted      * @param<T> the generic type of the element      * @return the list of discarded elements      */
DECL|method|update (Deque<T> buffer, T newItem)
specifier|public
specifier|abstract
parameter_list|<
name|T
parameter_list|>
name|Collection
argument_list|<
name|T
argument_list|>
name|update
parameter_list|(
name|Deque
argument_list|<
name|T
argument_list|>
name|buffer
parameter_list|,
name|T
name|newItem
parameter_list|)
function_decl|;
block|}
end_enum

end_unit

