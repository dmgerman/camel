begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hazelcast.queue
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
name|queue
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
name|HazelcastInstance
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
name|Component
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
name|Consumer
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
name|Processor
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
name|Producer
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
name|HazelcastDefaultEndpoint
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|HazelcastQueueEndpoint
specifier|public
class|class
name|HazelcastQueueEndpoint
extends|extends
name|HazelcastDefaultEndpoint
block|{
DECL|method|HazelcastQueueEndpoint (HazelcastInstance hazelcastInstance, String endpointUri, Component component, String cacheName)
specifier|public
name|HazelcastQueueEndpoint
parameter_list|(
name|HazelcastInstance
name|hazelcastInstance
parameter_list|,
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|,
name|String
name|cacheName
parameter_list|)
block|{
name|super
argument_list|(
name|hazelcastInstance
argument_list|,
name|endpointUri
argument_list|,
name|component
argument_list|,
name|cacheName
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|new
name|HazelcastQueueConsumer
argument_list|(
name|hazelcastInstance
argument_list|,
name|this
argument_list|,
name|processor
argument_list|,
name|cacheName
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|HazelcastQueueProducer
argument_list|(
name|hazelcastInstance
argument_list|,
name|this
argument_list|,
name|cacheName
argument_list|)
return|;
block|}
block|}
end_class

end_unit

