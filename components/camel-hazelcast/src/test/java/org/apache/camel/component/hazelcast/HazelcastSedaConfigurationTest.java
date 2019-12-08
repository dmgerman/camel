begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|PropertyBindingException
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
name|ResolveEndpointFailedException
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
name|HazelcastSedaEndpoint
name|hzlqEndpoint
init|=
operator|(
name|HazelcastSedaEndpoint
operator|)
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"hazelcast-seda:foo?transferExchange=true"
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
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"hazelcast-seda:foo?transferExchange=false"
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
name|HazelcastSedaEndpoint
name|hzlqEndpoint
init|=
operator|(
name|HazelcastSedaEndpoint
operator|)
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"hazelcast-seda:foo"
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
literal|"Default value of pool timeout is invalid"
argument_list|,
literal|1000
argument_list|,
name|hzlqEndpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getPollTimeout
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Default value of on error delay is invalid"
argument_list|,
literal|1000
argument_list|,
name|hzlqEndpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getOnErrorDelay
argument_list|()
argument_list|)
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
name|HazelcastSedaEndpoint
name|hzlqEndpoint
init|=
operator|(
name|HazelcastSedaEndpoint
operator|)
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"hazelcast-seda:foo?concurrentConsumers=4"
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
literal|"Default value of pool timeout is invalid"
argument_list|,
literal|1000
argument_list|,
name|hzlqEndpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getPollTimeout
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Default value of on error delay is invalid"
argument_list|,
literal|1000
argument_list|,
name|hzlqEndpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getOnErrorDelay
argument_list|()
argument_list|)
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
name|HazelcastSedaEndpoint
name|hzlqEndpoint
init|=
operator|(
name|HazelcastSedaEndpoint
operator|)
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"hazelcast-seda:foo?pollTimeout=4000"
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
literal|"Invalid pool timeout"
argument_list|,
literal|4000
argument_list|,
name|hzlqEndpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getPollTimeout
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Default value of on error delay is invalid"
argument_list|,
literal|1000
argument_list|,
name|hzlqEndpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getOnErrorDelay
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createEndpointWithOnErrorDelayParam ()
specifier|public
name|void
name|createEndpointWithOnErrorDelayParam
parameter_list|()
throws|throws
name|Exception
block|{
name|HazelcastSedaEndpoint
name|hzlqEndpoint
init|=
operator|(
name|HazelcastSedaEndpoint
operator|)
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"hazelcast-seda:foo?onErrorDelay=5000"
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
literal|"Default value of pool timeout is invalid"
argument_list|,
literal|1000
argument_list|,
name|hzlqEndpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getPollTimeout
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Value of on error delay is invalid"
argument_list|,
literal|5000
argument_list|,
name|hzlqEndpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getOnErrorDelay
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createEndpointWithIllegalOnErrorDelayParam ()
specifier|public
name|void
name|createEndpointWithIllegalOnErrorDelayParam
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"hazelcast-seda:foo?onErrorDelay=-1"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ResolveEndpointFailedException
name|e
parameter_list|)
block|{
name|assertIsInstanceOf
argument_list|(
name|IllegalArgumentException
operator|.
name|class
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

