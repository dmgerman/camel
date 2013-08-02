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
name|LinkedBlockingQueue
import|;
end_import

begin_comment
comment|/**  * Implementation of {@link BlockingQueueFactory} producing {@link java.util.concurrent.LinkedBlockingQueue}  */
end_comment

begin_class
DECL|class|LinkedBlockingQueueFactory
specifier|public
class|class
name|LinkedBlockingQueueFactory
parameter_list|<
name|E
parameter_list|>
implements|implements
name|BlockingQueueFactory
argument_list|<
name|E
argument_list|>
block|{
annotation|@
name|Override
DECL|method|create ()
specifier|public
name|LinkedBlockingQueue
argument_list|<
name|E
argument_list|>
name|create
parameter_list|()
block|{
return|return
operator|new
name|LinkedBlockingQueue
argument_list|<
name|E
argument_list|>
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|create (int capacity)
specifier|public
name|LinkedBlockingQueue
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
operator|new
name|LinkedBlockingQueue
argument_list|<
name|E
argument_list|>
argument_list|(
name|capacity
argument_list|)
return|;
block|}
block|}
end_class

end_unit

