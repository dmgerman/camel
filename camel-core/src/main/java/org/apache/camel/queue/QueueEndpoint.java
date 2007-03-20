begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.queue
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|queue
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Queue
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
name|CamelContainer
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
name|DefaultEndpoint
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

begin_comment
comment|/**  * Represents a queue endpoint that uses a {@link Queue}  * object to process inbound exchanges.  *  * @org.apache.xbean.XBean  * @version $Revision: 519973 $  */
end_comment

begin_class
DECL|class|QueueEndpoint
specifier|public
class|class
name|QueueEndpoint
parameter_list|<
name|E
parameter_list|>
extends|extends
name|DefaultEndpoint
argument_list|<
name|E
argument_list|>
block|{
DECL|field|queue
specifier|private
name|Queue
argument_list|<
name|E
argument_list|>
name|queue
decl_stmt|;
DECL|method|QueueEndpoint (String uri, CamelContainer container, Queue<E> queue)
specifier|public
name|QueueEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|CamelContainer
name|container
parameter_list|,
name|Queue
argument_list|<
name|E
argument_list|>
name|queue
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|container
argument_list|)
expr_stmt|;
name|this
operator|.
name|queue
operator|=
name|queue
expr_stmt|;
block|}
DECL|method|send (E exchange)
specifier|public
name|void
name|send
parameter_list|(
name|E
name|exchange
parameter_list|)
block|{
name|queue
operator|.
name|add
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
DECL|method|createExchange ()
specifier|public
name|E
name|createExchange
parameter_list|()
block|{
comment|// How can we create a specific Exchange if we are generic??
comment|// perhaps it would be better if we did not implement this.
return|return
operator|(
name|E
operator|)
operator|new
name|DefaultExchange
argument_list|()
return|;
block|}
DECL|method|getQueue ()
specifier|public
name|Queue
argument_list|<
name|E
argument_list|>
name|getQueue
parameter_list|()
block|{
return|return
name|queue
return|;
block|}
block|}
end_class

end_unit

