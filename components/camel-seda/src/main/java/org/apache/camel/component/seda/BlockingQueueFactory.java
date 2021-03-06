begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|/**  * Factory of {@link java.util.concurrent.BlockingQueue}  * @param<E> Element type, usually {@link Exchange}  */
end_comment

begin_interface
DECL|interface|BlockingQueueFactory
specifier|public
interface|interface
name|BlockingQueueFactory
parameter_list|<
name|E
parameter_list|>
block|{
comment|/**      * Create a new {@link java.util.concurrent.BlockingQueue} with default capacity      * @return New {@link java.util.concurrent.BlockingQueue}      */
DECL|method|create ()
name|BlockingQueue
argument_list|<
name|E
argument_list|>
name|create
parameter_list|()
function_decl|;
comment|/**      * Create a new {@link java.util.concurrent.BlockingQueue} with given capacity      * @return New {@link java.util.concurrent.BlockingQueue}      */
DECL|method|create (int capacity)
name|BlockingQueue
argument_list|<
name|E
argument_list|>
name|create
parameter_list|(
name|int
name|capacity
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

