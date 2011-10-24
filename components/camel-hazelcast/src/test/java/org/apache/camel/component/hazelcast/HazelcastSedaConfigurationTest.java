begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hazelcast
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
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|RuntimeCamelException
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
name|seda
operator|.
name|HazelcastSedaEndpoint
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|HazelcastSedaConfigurationTest
specifier|public
class|class
name|HazelcastSedaConfigurationTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|createEndpointWithTransferExchange ()
specifier|public
name|void
name|createEndpointWithTransferExchange
parameter_list|()
throws|throws
name|Exception
block|{
name|HazelcastComponent
name|hzlqComponent
init|=
operator|new
name|HazelcastComponent
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|hzlqComponent
operator|.
name|start
argument_list|()
expr_stmt|;
name|HazelcastSedaEndpoint
name|hzlqEndpoint
init|=
operator|(
name|HazelcastSedaEndpoint
operator|)
name|hzlqComponent
operator|.
name|createEndpoint
argument_list|(
literal|"hazelcast:seda:foo?transferExchange=true"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Invalid queue name"
argument_list|,
literal|"foo"
argument_list|,
name|hzlqEndpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getQueueName
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Default value of concurrent consumers is invalid"
argument_list|,
name|hzlqEndpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isTransferExchange
argument_list|()
argument_list|)
expr_stmt|;
name|hzlqEndpoint
operator|=
operator|(
name|HazelcastSedaEndpoint
operator|)
name|hzlqComponent
operator|.
name|createEndpoint
argument_list|(
literal|"hazelcast:seda:foo?transferExchange=false"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Invalid queue name"
argument_list|,
literal|"foo"
argument_list|,
name|hzlqEndpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getQueueName
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Default value of concurrent consumers is invalid"
argument_list|,
name|hzlqEndpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isTransferExchange
argument_list|()
argument_list|)
expr_stmt|;
name|hzlqComponent
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createEndpointWithNoParams ()
specifier|public
name|void
name|createEndpointWithNoParams
parameter_list|()
throws|throws
name|Exception
block|{
name|HazelcastComponent
name|hzlqComponent
init|=
operator|new
name|HazelcastComponent
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|hzlqComponent
operator|.
name|start
argument_list|()
expr_stmt|;
name|HazelcastSedaEndpoint
name|hzlqEndpoint
init|=
operator|(
name|HazelcastSedaEndpoint
operator|)
name|hzlqComponent
operator|.
name|createEndpoint
argument_list|(
literal|"hazelcast:seda:foo"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Invalid queue name"
argument_list|,
literal|"foo"
argument_list|,
name|hzlqEndpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getQueueName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Default value of concurrent consumers is invalid"
argument_list|,
literal|1
argument_list|,
name|hzlqEndpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getConcurrentConsumers
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Default value of pool interval is invalid"
argument_list|,
literal|1000
argument_list|,
name|hzlqEndpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getPollInterval
argument_list|()
argument_list|)
expr_stmt|;
name|hzlqComponent
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createEndpointWithConcurrentConsumersParam ()
specifier|public
name|void
name|createEndpointWithConcurrentConsumersParam
parameter_list|()
throws|throws
name|Exception
block|{
name|HazelcastComponent
name|hzlqComponent
init|=
operator|new
name|HazelcastComponent
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|hzlqComponent
operator|.
name|start
argument_list|()
expr_stmt|;
name|HazelcastSedaEndpoint
name|hzlqEndpoint
init|=
operator|(
name|HazelcastSedaEndpoint
operator|)
name|hzlqComponent
operator|.
name|createEndpoint
argument_list|(
literal|"hazelcast:seda:foo?concurrentConsumers=4"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Invalid queue name"
argument_list|,
literal|"foo"
argument_list|,
name|hzlqEndpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getQueueName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Value of concurrent consumers is invalid"
argument_list|,
literal|4
argument_list|,
name|hzlqEndpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getConcurrentConsumers
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Default value of pool interval is invalid"
argument_list|,
literal|1000
argument_list|,
name|hzlqEndpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getPollInterval
argument_list|()
argument_list|)
expr_stmt|;
name|hzlqComponent
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createEndpointWithPoolIntevalParam ()
specifier|public
name|void
name|createEndpointWithPoolIntevalParam
parameter_list|()
throws|throws
name|Exception
block|{
name|HazelcastComponent
name|hzlqComponent
init|=
operator|new
name|HazelcastComponent
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|hzlqComponent
operator|.
name|start
argument_list|()
expr_stmt|;
name|HazelcastSedaEndpoint
name|hzlqEndpoint
init|=
operator|(
name|HazelcastSedaEndpoint
operator|)
name|hzlqComponent
operator|.
name|createEndpoint
argument_list|(
literal|"hazelcast:seda:foo?pollInterval=4000"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Invalid queue name"
argument_list|,
literal|"foo"
argument_list|,
name|hzlqEndpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getQueueName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Default value of concurrent consumers is invalid"
argument_list|,
literal|1
argument_list|,
name|hzlqEndpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getConcurrentConsumers
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Invalid pool interval"
argument_list|,
literal|4000
argument_list|,
name|hzlqEndpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getPollInterval
argument_list|()
argument_list|)
expr_stmt|;
name|hzlqComponent
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|IllegalArgumentException
operator|.
name|class
argument_list|)
DECL|method|createEndpointWithoutEmptyName ()
specifier|public
name|void
name|createEndpointWithoutEmptyName
parameter_list|()
throws|throws
name|Exception
block|{
name|HazelcastComponent
name|hzlqComponent
init|=
operator|new
name|HazelcastComponent
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|hzlqComponent
operator|.
name|start
argument_list|()
expr_stmt|;
name|hzlqComponent
operator|.
name|createEndpoint
argument_list|(
literal|"hazelcast:seda: ?concurrentConsumers=4"
argument_list|)
expr_stmt|;
name|hzlqComponent
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

