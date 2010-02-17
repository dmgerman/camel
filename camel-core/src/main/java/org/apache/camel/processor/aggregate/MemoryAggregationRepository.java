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
name|ConcurrentHashMap
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
name|ServiceSupport
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
name|AggregationRepository
import|;
end_import

begin_comment
comment|/**  * A memory based {@link org.apache.camel.spi.AggregationRepository} which stores in memory only.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|MemoryAggregationRepository
specifier|public
class|class
name|MemoryAggregationRepository
extends|extends
name|ServiceSupport
implements|implements
name|AggregationRepository
argument_list|<
name|Object
argument_list|>
block|{
DECL|field|cache
specifier|private
specifier|final
name|Map
argument_list|<
name|Object
argument_list|,
name|Exchange
argument_list|>
name|cache
init|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|Object
argument_list|,
name|Exchange
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|add (Object key, Exchange exchange)
specifier|public
name|Exchange
name|add
parameter_list|(
name|Object
name|key
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
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
DECL|method|get (Object key)
specifier|public
name|Exchange
name|get
parameter_list|(
name|Object
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
DECL|method|remove (Object key)
specifier|public
name|void
name|remove
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
name|cache
operator|.
name|remove
argument_list|(
name|key
argument_list|)
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

