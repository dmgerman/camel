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
name|concurrent
operator|.
name|ArrayBlockingQueue
import|;
end_import

begin_comment
comment|/**  * Implementation of {@link BlockingQueueFactory} producing {@link java.util.concurrent.ArrayBlockingQueue}  */
end_comment

begin_class
DECL|class|ArrayBlockingQueueFactory
specifier|public
class|class
name|ArrayBlockingQueueFactory
parameter_list|<
name|E
parameter_list|>
implements|implements
name|BlockingQueueFactory
argument_list|<
name|E
argument_list|>
block|{
comment|/**      * Capacity used when none provided      */
DECL|field|defaultCapacity
specifier|private
name|int
name|defaultCapacity
init|=
literal|50
decl_stmt|;
comment|/**      * Lock fairness. null means default fairness      */
DECL|field|fair
specifier|private
name|Boolean
name|fair
decl_stmt|;
comment|/**      * @return Default array capacity      */
DECL|method|getDefaultCapacity ()
specifier|public
name|int
name|getDefaultCapacity
parameter_list|()
block|{
return|return
name|defaultCapacity
return|;
block|}
comment|/**      * @param defaultCapacity Default array capacity      */
DECL|method|setDefaultCapacity (int defaultCapacity)
specifier|public
name|void
name|setDefaultCapacity
parameter_list|(
name|int
name|defaultCapacity
parameter_list|)
block|{
name|this
operator|.
name|defaultCapacity
operator|=
name|defaultCapacity
expr_stmt|;
block|}
comment|/**      * @return Lock fairness      */
DECL|method|isFair ()
specifier|public
name|boolean
name|isFair
parameter_list|()
block|{
return|return
name|fair
return|;
block|}
comment|/**      * @param fair Lock fairness      */
DECL|method|setFair (boolean fair)
specifier|public
name|void
name|setFair
parameter_list|(
name|boolean
name|fair
parameter_list|)
block|{
name|this
operator|.
name|fair
operator|=
name|fair
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|create ()
specifier|public
name|ArrayBlockingQueue
argument_list|<
name|E
argument_list|>
name|create
parameter_list|()
block|{
return|return
name|create
argument_list|(
name|defaultCapacity
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|create (int capacity)
specifier|public
name|ArrayBlockingQueue
argument_list|<
name|E
argument_list|>
name|create
parameter_list|(
name|int
name|capacity
parameter_list|)
block|{
return|return
name|fair
operator|==
literal|null
condition|?
operator|new
name|ArrayBlockingQueue
argument_list|<
name|E
argument_list|>
argument_list|(
name|defaultCapacity
argument_list|)
else|:
operator|new
name|ArrayBlockingQueue
argument_list|<
name|E
argument_list|>
argument_list|(
name|defaultCapacity
argument_list|,
name|fair
argument_list|)
return|;
block|}
block|}
end_class

end_unit

