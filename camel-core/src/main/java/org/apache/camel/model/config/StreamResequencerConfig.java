begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model.config
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|config
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessorType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAttribute
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlTransient
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
name|processor
operator|.
name|resequencer
operator|.
name|DefaultExchangeComparator
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
name|processor
operator|.
name|resequencer
operator|.
name|ExpressionResultComparator
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
name|Label
import|;
end_import

begin_comment
comment|/**  * Configures stream-processing resequence eip.  */
end_comment

begin_class
annotation|@
name|Label
argument_list|(
literal|"configuration"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"stream-config"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|StreamResequencerConfig
specifier|public
class|class
name|StreamResequencerConfig
extends|extends
name|ResequencerConfig
block|{
annotation|@
name|XmlAttribute
DECL|field|capacity
specifier|private
name|Integer
name|capacity
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|timeout
specifier|private
name|Long
name|timeout
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|ignoreInvalidExchanges
specifier|private
name|Boolean
name|ignoreInvalidExchanges
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|comparator
specifier|private
name|ExpressionResultComparator
name|comparator
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|comparatorRef
specifier|private
name|String
name|comparatorRef
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|rejectOld
specifier|private
name|Boolean
name|rejectOld
decl_stmt|;
comment|/**      * Creates a new {@link StreamResequencerConfig} instance using default      * values for<code>capacity</code> (1000) and<code>timeout</code>      * (1000L). Elements of the sequence are compared using the      * {@link DefaultExchangeComparator}.      */
DECL|method|StreamResequencerConfig ()
specifier|public
name|StreamResequencerConfig
parameter_list|()
block|{
name|this
argument_list|(
literal|1000
argument_list|,
literal|1000L
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a new {@link StreamResequencerConfig} instance using the given      * values for<code>capacity</code> and<code>timeout</code>. Elements      * of the sequence are compared using the {@link DefaultExchangeComparator}.      *       * @param capacity   capacity of the resequencer's inbound queue.      * @param timeout    minimum time to wait for missing elements (messages).      */
DECL|method|StreamResequencerConfig (int capacity, long timeout)
specifier|public
name|StreamResequencerConfig
parameter_list|(
name|int
name|capacity
parameter_list|,
name|long
name|timeout
parameter_list|)
block|{
name|this
argument_list|(
name|capacity
argument_list|,
name|timeout
argument_list|,
operator|new
name|DefaultExchangeComparator
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a new {@link StreamResequencerConfig} instance using the given      * values for<code>capacity</code> and<code>timeout</code>. Elements      * of the sequence are compared with the given      * {@link ExpressionResultComparator}.      *       * @param capacity   capacity of the resequencer's inbound queue.      * @param timeout    minimum time to wait for missing elements (messages).      * @param comparator comparator for sequence comparision      */
DECL|method|StreamResequencerConfig (int capacity, long timeout, ExpressionResultComparator comparator)
specifier|public
name|StreamResequencerConfig
parameter_list|(
name|int
name|capacity
parameter_list|,
name|long
name|timeout
parameter_list|,
name|ExpressionResultComparator
name|comparator
parameter_list|)
block|{
name|this
operator|.
name|capacity
operator|=
name|capacity
expr_stmt|;
name|this
operator|.
name|timeout
operator|=
name|timeout
expr_stmt|;
name|this
operator|.
name|comparator
operator|=
name|comparator
expr_stmt|;
block|}
comment|/**      * Creates a new {@link StreamResequencerConfig} instance using the given      * values for<code>capacity</code> and<code>timeout</code>. Elements      * of the sequence are compared using the {@link DefaultExchangeComparator}.      *      * @param capacity   capacity of the resequencer's inbound queue.      * @param timeout    minimum time to wait for missing elements (messages).      * @param rejectOld  if true, throws an exception when messages older than the last delivered message are processed      */
DECL|method|StreamResequencerConfig (int capacity, long timeout, Boolean rejectOld)
specifier|public
name|StreamResequencerConfig
parameter_list|(
name|int
name|capacity
parameter_list|,
name|long
name|timeout
parameter_list|,
name|Boolean
name|rejectOld
parameter_list|)
block|{
name|this
argument_list|(
name|capacity
argument_list|,
name|timeout
argument_list|,
name|rejectOld
argument_list|,
operator|new
name|DefaultExchangeComparator
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a new {@link StreamResequencerConfig} instance using the given      * values for<code>capacity</code> and<code>timeout</code>. Elements      * of the sequence are compared with the given {@link ExpressionResultComparator}.      *      * @param capacity   capacity of the resequencer's inbound queue.      * @param timeout    minimum time to wait for missing elements (messages).      * @param rejectOld  if true, throws an exception when messages older than the last delivered message are processed      * @param comparator comparator for sequence comparision      */
DECL|method|StreamResequencerConfig (int capacity, long timeout, Boolean rejectOld, ExpressionResultComparator comparator)
specifier|public
name|StreamResequencerConfig
parameter_list|(
name|int
name|capacity
parameter_list|,
name|long
name|timeout
parameter_list|,
name|Boolean
name|rejectOld
parameter_list|,
name|ExpressionResultComparator
name|comparator
parameter_list|)
block|{
name|this
operator|.
name|capacity
operator|=
name|capacity
expr_stmt|;
name|this
operator|.
name|timeout
operator|=
name|timeout
expr_stmt|;
name|this
operator|.
name|rejectOld
operator|=
name|rejectOld
expr_stmt|;
name|this
operator|.
name|comparator
operator|=
name|comparator
expr_stmt|;
block|}
comment|/**      * Returns a new {@link StreamResequencerConfig} instance using default      * values for<code>capacity</code> (1000) and<code>timeout</code>      * (1000L). Elements of the sequence are compared using the      * {@link DefaultExchangeComparator}.      *       * @return a default {@link StreamResequencerConfig}.      */
DECL|method|getDefault ()
specifier|public
specifier|static
name|StreamResequencerConfig
name|getDefault
parameter_list|()
block|{
return|return
operator|new
name|StreamResequencerConfig
argument_list|()
return|;
block|}
DECL|method|getCapacity ()
specifier|public
name|int
name|getCapacity
parameter_list|()
block|{
return|return
name|capacity
return|;
block|}
comment|/**      * Sets the capacity of the resequencer's inbound queue.      */
DECL|method|setCapacity (int capacity)
specifier|public
name|void
name|setCapacity
parameter_list|(
name|int
name|capacity
parameter_list|)
block|{
name|this
operator|.
name|capacity
operator|=
name|capacity
expr_stmt|;
block|}
DECL|method|getTimeout ()
specifier|public
name|long
name|getTimeout
parameter_list|()
block|{
return|return
name|timeout
return|;
block|}
comment|/**      * Sets minimum time to wait for missing elements (messages).      */
DECL|method|setTimeout (long timeout)
specifier|public
name|void
name|setTimeout
parameter_list|(
name|long
name|timeout
parameter_list|)
block|{
name|this
operator|.
name|timeout
operator|=
name|timeout
expr_stmt|;
block|}
DECL|method|getIgnoreInvalidExchanges ()
specifier|public
name|Boolean
name|getIgnoreInvalidExchanges
parameter_list|()
block|{
return|return
name|ignoreInvalidExchanges
return|;
block|}
comment|/**      * Whether to ignore invalid exchanges      */
DECL|method|setIgnoreInvalidExchanges (Boolean ignoreInvalidExchanges)
specifier|public
name|void
name|setIgnoreInvalidExchanges
parameter_list|(
name|Boolean
name|ignoreInvalidExchanges
parameter_list|)
block|{
name|this
operator|.
name|ignoreInvalidExchanges
operator|=
name|ignoreInvalidExchanges
expr_stmt|;
block|}
DECL|method|getComparator ()
specifier|public
name|ExpressionResultComparator
name|getComparator
parameter_list|()
block|{
return|return
name|comparator
return|;
block|}
comment|/**      * To use a custom comparator      */
DECL|method|setComparator (ExpressionResultComparator comparator)
specifier|public
name|void
name|setComparator
parameter_list|(
name|ExpressionResultComparator
name|comparator
parameter_list|)
block|{
name|this
operator|.
name|comparator
operator|=
name|comparator
expr_stmt|;
block|}
DECL|method|getComparatorRef ()
specifier|public
name|String
name|getComparatorRef
parameter_list|()
block|{
return|return
name|comparatorRef
return|;
block|}
comment|/**      * To use a custom comparator      */
DECL|method|setComparatorRef (String comparatorRef)
specifier|public
name|void
name|setComparatorRef
parameter_list|(
name|String
name|comparatorRef
parameter_list|)
block|{
name|this
operator|.
name|comparatorRef
operator|=
name|comparatorRef
expr_stmt|;
block|}
comment|/**      * If true, throws an exception when messages older than the last delivered message are processed      */
DECL|method|setRejectOld (boolean value)
specifier|public
name|void
name|setRejectOld
parameter_list|(
name|boolean
name|value
parameter_list|)
block|{
name|this
operator|.
name|rejectOld
operator|=
name|value
expr_stmt|;
block|}
DECL|method|getRejectOld ()
specifier|public
name|Boolean
name|getRejectOld
parameter_list|()
block|{
return|return
name|rejectOld
return|;
block|}
block|}
end_class

end_unit

