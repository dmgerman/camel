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
name|java
operator|.
name|util
operator|.
name|Map
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
name|CamelContext
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
name|annotations
operator|.
name|Component
import|;
end_import

begin_class
annotation|@
name|Component
argument_list|(
literal|"hazelcast-replicatedmap"
argument_list|)
DECL|class|HazelcastReplicatedmapComponent
specifier|public
class|class
name|HazelcastReplicatedmapComponent
extends|extends
name|HazelcastDefaultComponent
block|{
DECL|method|HazelcastReplicatedmapComponent ()
specifier|public
name|HazelcastReplicatedmapComponent
parameter_list|()
block|{     }
DECL|method|HazelcastReplicatedmapComponent (final CamelContext context)
specifier|public
name|HazelcastReplicatedmapComponent
parameter_list|(
specifier|final
name|CamelContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doCreateEndpoint (String uri, String remaining, Map<String, Object> parameters, HazelcastInstance hzInstance)
specifier|protected
name|HazelcastDefaultEndpoint
name|doCreateEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|,
name|HazelcastInstance
name|hzInstance
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|new
name|HazelcastReplicatedmapEndpoint
argument_list|(
name|hzInstance
argument_list|,
name|uri
argument_list|,
name|remaining
argument_list|,
name|this
argument_list|)
return|;
block|}
block|}
end_class

end_unit

