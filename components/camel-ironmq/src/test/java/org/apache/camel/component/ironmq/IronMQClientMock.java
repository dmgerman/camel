begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ironmq
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ironmq
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
name|io
operator|.
name|iron
operator|.
name|ironmq
operator|.
name|Client
import|;
end_import

begin_import
import|import
name|io
operator|.
name|iron
operator|.
name|ironmq
operator|.
name|Queue
import|;
end_import

begin_class
DECL|class|IronMQClientMock
specifier|public
class|class
name|IronMQClientMock
extends|extends
name|Client
block|{
DECL|field|memQueues
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Queue
argument_list|>
name|memQueues
init|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|Queue
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|IronMQClientMock (String projectId, String token)
specifier|public
name|IronMQClientMock
parameter_list|(
name|String
name|projectId
parameter_list|,
name|String
name|token
parameter_list|)
block|{
name|super
argument_list|(
name|projectId
argument_list|,
name|token
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|queue (String name)
specifier|public
name|Queue
name|queue
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|Queue
name|answer
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|memQueues
operator|.
name|containsKey
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|answer
operator|=
name|memQueues
operator|.
name|get
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|answer
operator|=
operator|new
name|MockQueue
argument_list|(
name|this
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|memQueues
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|answer
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

