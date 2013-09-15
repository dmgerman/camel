begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.infinispan
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|infinispan
package|;
end_package

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
name|org
operator|.
name|infinispan
operator|.
name|notifications
operator|.
name|Listener
import|;
end_import

begin_class
annotation|@
name|Listener
argument_list|(
name|sync
operator|=
literal|false
argument_list|)
DECL|class|InfinispanAsyncEventListener
specifier|public
class|class
name|InfinispanAsyncEventListener
extends|extends
name|InfinispanSyncEventListener
block|{
DECL|method|InfinispanAsyncEventListener (InfinispanConsumer consumer, Set<String> eventTypes)
specifier|public
name|InfinispanAsyncEventListener
parameter_list|(
name|InfinispanConsumer
name|consumer
parameter_list|,
name|Set
argument_list|<
name|String
argument_list|>
name|eventTypes
parameter_list|)
block|{
name|super
argument_list|(
name|consumer
argument_list|,
name|eventTypes
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

