begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.kubernetes.fmp
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|kubernetes
operator|.
name|fmp
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|UUID
import|;
end_import

begin_import
import|import
name|com
operator|.
name|hazelcast
operator|.
name|client
operator|.
name|HazelcastClient
import|;
end_import

begin_import
import|import
name|com
operator|.
name|hazelcast
operator|.
name|client
operator|.
name|config
operator|.
name|ClientConfig
import|;
end_import

begin_import
import|import
name|com
operator|.
name|hazelcast
operator|.
name|config
operator|.
name|GroupConfig
import|;
end_import

begin_import
import|import
name|com
operator|.
name|hazelcast
operator|.
name|config
operator|.
name|SSLConfig
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
name|Exchange
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
name|builder
operator|.
name|RouteBuilder
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
name|component
operator|.
name|hazelcast
operator|.
name|topic
operator|.
name|HazelcastTopicComponent
import|;
end_import

begin_class
DECL|class|HazelcastRoute
specifier|public
class|class
name|HazelcastRoute
extends|extends
name|RouteBuilder
block|{
annotation|@
name|Override
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
comment|// setup hazelcast
name|ClientConfig
name|config
init|=
operator|new
name|ClientConfig
argument_list|()
decl_stmt|;
name|config
operator|.
name|getNetworkConfig
argument_list|()
operator|.
name|addAddress
argument_list|(
literal|"hazelcast"
argument_list|)
expr_stmt|;
name|config
operator|.
name|getNetworkConfig
argument_list|()
operator|.
name|setSSLConfig
argument_list|(
operator|new
name|SSLConfig
argument_list|()
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|config
operator|.
name|setGroupConfig
argument_list|(
operator|new
name|GroupConfig
argument_list|(
literal|"someGroup"
argument_list|)
argument_list|)
expr_stmt|;
name|HazelcastInstance
name|instance
init|=
name|HazelcastClient
operator|.
name|newHazelcastClient
argument_list|(
name|config
argument_list|)
decl_stmt|;
comment|// setup camel hazelcast
name|HazelcastTopicComponent
name|hazelcast
init|=
operator|new
name|HazelcastTopicComponent
argument_list|()
decl_stmt|;
name|hazelcast
operator|.
name|setHazelcastInstance
argument_list|(
name|instance
argument_list|)
expr_stmt|;
name|getContext
argument_list|()
operator|.
name|addComponent
argument_list|(
literal|"hazelcast-topic"
argument_list|,
name|hazelcast
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"timer:foo?period=5000"
argument_list|)
operator|.
name|log
argument_list|(
literal|"Producer side: Sending data to Hazelcast topic.."
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|HazelcastConstants
operator|.
name|OPERATION
argument_list|,
name|HazelcastOperation
operator|.
name|PUBLISH
argument_list|)
expr_stmt|;
name|String
name|payload
init|=
literal|"Test "
operator|+
name|UUID
operator|.
name|randomUUID
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|payload
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|to
argument_list|(
literal|"hazelcast-topic:foo"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"hazelcast-topic:foo"
argument_list|)
operator|.
name|log
argument_list|(
literal|"Consumer side: Detected following action: $simple{in.header.CamelHazelcastListenerAction}"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

