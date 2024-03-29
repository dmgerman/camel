begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hazelcast.replicatedmap
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
name|replicatedmap
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
name|HazelcastDefaultComponent
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
name|spi
operator|.
name|UriEndpoint
import|;
end_import

begin_comment
comment|/**  * The hazelcast-replicatedmap component is used to access<a href="http://www.hazelcast.com/">Hazelcast</a> replicated map.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.16.0"
argument_list|,
name|scheme
operator|=
literal|"hazelcast-replicatedmap"
argument_list|,
name|title
operator|=
literal|"Hazelcast Replicated Map"
argument_list|,
name|syntax
operator|=
literal|"hazelcast-replicatedmap:cacheName"
argument_list|,
name|label
operator|=
literal|"cache,datagrid"
argument_list|)
DECL|class|HazelcastReplicatedmapEndpoint
specifier|public
class|class
name|HazelcastReplicatedmapEndpoint
extends|extends
name|HazelcastDefaultEndpoint
block|{
DECL|method|HazelcastReplicatedmapEndpoint (HazelcastInstance hazelcastInstance, String uri, String cacheName, HazelcastDefaultComponent component)
specifier|public
name|HazelcastReplicatedmapEndpoint
parameter_list|(
name|HazelcastInstance
name|hazelcastInstance
parameter_list|,
name|String
name|uri
parameter_list|,
name|String
name|cacheName
parameter_list|,
name|HazelcastDefaultComponent
name|component
parameter_list|)
block|{
name|super
argument_list|(
name|hazelcastInstance
argument_list|,
name|uri
argument_list|,
name|component
argument_list|,
name|cacheName
argument_list|)
expr_stmt|;
name|setCommand
argument_list|(
name|HazelcastCommand
operator|.
name|replicatedmap
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
name|HazelcastReplicatedmapConsumer
name|answer
init|=
operator|new
name|HazelcastReplicatedmapConsumer
argument_list|(
name|hazelcastInstance
argument_list|,
name|this
argument_list|,
name|processor
argument_list|,
name|cacheName
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
name|HazelcastReplicatedmapProducer
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

