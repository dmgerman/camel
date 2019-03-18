begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hazelcast.topic
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
name|topic
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
name|MultipleConsumersSupport
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
name|HazelcastCommand
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
name|HazelcastOperation
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
name|UriEndpoint
import|;
end_import

begin_comment
comment|/**  * The hazelcast-topic component is used to access<a href="http://www.hazelcast.com/">Hazelcast</a> distributed topic.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.15.0"
argument_list|,
name|scheme
operator|=
literal|"hazelcast-topic"
argument_list|,
name|title
operator|=
literal|"Hazelcast Topic"
argument_list|,
name|syntax
operator|=
literal|"hazelcast-topic:cacheName"
argument_list|,
name|label
operator|=
literal|"cache,datagrid"
argument_list|)
DECL|class|HazelcastTopicEndpoint
specifier|public
class|class
name|HazelcastTopicEndpoint
extends|extends
name|HazelcastDefaultEndpoint
implements|implements
name|MultipleConsumersSupport
block|{
DECL|field|configuration
specifier|private
specifier|final
name|HazelcastTopicConfiguration
name|configuration
decl_stmt|;
DECL|method|HazelcastTopicEndpoint (HazelcastInstance hazelcastInstance, String endpointUri, Component component, String cacheName, final HazelcastTopicConfiguration configuration)
specifier|public
name|HazelcastTopicEndpoint
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
parameter_list|,
specifier|final
name|HazelcastTopicConfiguration
name|configuration
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
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
name|setCommand
argument_list|(
name|HazelcastCommand
operator|.
name|topic
argument_list|)
expr_stmt|;
name|setDefaultOperation
argument_list|(
name|HazelcastOperation
operator|.
name|PUBLISH
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
name|HazelcastTopicConsumer
name|answer
init|=
operator|new
name|HazelcastTopicConsumer
argument_list|(
name|hazelcastInstance
argument_list|,
name|this
argument_list|,
name|processor
argument_list|,
name|cacheName
argument_list|,
name|configuration
operator|.
name|isReliable
argument_list|()
argument_list|)
decl_stmt|;
name|configureConsumer
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
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
name|HazelcastTopicProducer
argument_list|(
name|hazelcastInstance
argument_list|,
name|this
argument_list|,
name|cacheName
argument_list|,
name|configuration
operator|.
name|isReliable
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|isMultipleConsumersSupported ()
specifier|public
name|boolean
name|isMultipleConsumersSupported
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

