begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.nats
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|nats
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|TimeoutException
import|;
end_import

begin_import
import|import
name|io
operator|.
name|nats
operator|.
name|client
operator|.
name|Connection
import|;
end_import

begin_import
import|import
name|io
operator|.
name|nats
operator|.
name|client
operator|.
name|Nats
import|;
end_import

begin_import
import|import
name|io
operator|.
name|nats
operator|.
name|client
operator|.
name|Options
import|;
end_import

begin_import
import|import
name|io
operator|.
name|nats
operator|.
name|client
operator|.
name|Options
operator|.
name|Builder
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
name|EndpointInject
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
name|mock
operator|.
name|MockEndpoint
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
name|impl
operator|.
name|JndiRegistry
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
DECL|class|NatsConsumerWithConnectionLoadTest
specifier|public
class|class
name|NatsConsumerWithConnectionLoadTest
extends|extends
name|NatsTestSupport
block|{
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:result"
argument_list|)
DECL|field|mockResultEndpoint
specifier|protected
name|MockEndpoint
name|mockResultEndpoint
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:result1"
argument_list|)
DECL|field|mockResultEndpoint1
specifier|protected
name|MockEndpoint
name|mockResultEndpoint1
decl_stmt|;
DECL|field|connection
specifier|private
name|Connection
name|connection
decl_stmt|;
annotation|@
name|Test
DECL|method|testLoadConsumer ()
specifier|public
name|void
name|testLoadConsumer
parameter_list|()
throws|throws
name|InterruptedException
throws|,
name|IOException
throws|,
name|TimeoutException
block|{
name|mockResultEndpoint
operator|.
name|setExpectedMessageCount
argument_list|(
literal|100
argument_list|)
expr_stmt|;
name|mockResultEndpoint1
operator|.
name|setExpectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|Options
name|options
init|=
operator|new
name|Options
operator|.
name|Builder
argument_list|()
operator|.
name|server
argument_list|(
literal|"nats://"
operator|+
name|getNatsUrl
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|Connection
name|connection
init|=
name|Nats
operator|.
name|connect
argument_list|(
name|options
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|100
condition|;
name|i
operator|++
control|)
block|{
name|connection
operator|.
name|publish
argument_list|(
literal|"test"
argument_list|,
operator|(
literal|"test"
operator|+
name|i
operator|)
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|mockResultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|mockResultEndpoint1
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|registry
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|Builder
name|options
init|=
operator|new
name|Options
operator|.
name|Builder
argument_list|()
decl_stmt|;
name|options
operator|.
name|server
argument_list|(
literal|"nats://"
operator|+
name|getNatsUrl
argument_list|()
argument_list|)
expr_stmt|;
name|connection
operator|=
name|Nats
operator|.
name|connect
argument_list|(
name|options
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"connection"
argument_list|,
name|connection
argument_list|)
expr_stmt|;
return|return
name|registry
return|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"nats://thisismytest?topic=test&connection=#connection"
argument_list|)
operator|.
name|to
argument_list|(
name|mockResultEndpoint
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"nats://thisismytest?topic=test1&connection=#connection"
argument_list|)
operator|.
name|to
argument_list|(
name|mockResultEndpoint1
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

