begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hazelcast.listener
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hazelcast
operator|.
name|listener
package|;
end_package

begin_import
import|import
name|com
operator|.
name|hazelcast
operator|.
name|core
operator|.
name|EntryEvent
import|;
end_import

begin_import
import|import
name|com
operator|.
name|hazelcast
operator|.
name|core
operator|.
name|EntryListener
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
name|component
operator|.
name|hazelcast
operator|.
name|HazelcastConstants
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
name|component
operator|.
name|hazelcast
operator|.
name|HazelcastDefaultConsumer
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|CamelEntryListener
specifier|public
class|class
name|CamelEntryListener
extends|extends
name|CamelListener
implements|implements
name|EntryListener
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
block|{
DECL|method|CamelEntryListener (HazelcastDefaultConsumer consumer, String cacheName)
specifier|public
name|CamelEntryListener
parameter_list|(
name|HazelcastDefaultConsumer
name|consumer
parameter_list|,
name|String
name|cacheName
parameter_list|)
block|{
name|super
argument_list|(
name|consumer
argument_list|,
name|cacheName
argument_list|)
expr_stmt|;
block|}
DECL|method|entryAdded (EntryEvent<Object, Object> event)
specifier|public
name|void
name|entryAdded
parameter_list|(
name|EntryEvent
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|event
parameter_list|)
block|{
name|this
operator|.
name|sendExchange
argument_list|(
name|HazelcastConstants
operator|.
name|ADDED
argument_list|,
name|event
operator|.
name|getKey
argument_list|()
argument_list|,
name|event
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|entryEvicted (EntryEvent<Object, Object> event)
specifier|public
name|void
name|entryEvicted
parameter_list|(
name|EntryEvent
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|event
parameter_list|)
block|{
name|this
operator|.
name|sendExchange
argument_list|(
name|HazelcastConstants
operator|.
name|ENVICTED
argument_list|,
name|event
operator|.
name|getKey
argument_list|()
argument_list|,
name|event
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|entryRemoved (EntryEvent<Object, Object> event)
specifier|public
name|void
name|entryRemoved
parameter_list|(
name|EntryEvent
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|event
parameter_list|)
block|{
name|this
operator|.
name|sendExchange
argument_list|(
name|HazelcastConstants
operator|.
name|REMOVED
argument_list|,
name|event
operator|.
name|getKey
argument_list|()
argument_list|,
name|event
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|entryUpdated (EntryEvent<Object, Object> event)
specifier|public
name|void
name|entryUpdated
parameter_list|(
name|EntryEvent
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|event
parameter_list|)
block|{
name|this
operator|.
name|sendExchange
argument_list|(
name|HazelcastConstants
operator|.
name|UPDATED
argument_list|,
name|event
operator|.
name|getKey
argument_list|()
argument_list|,
name|event
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

