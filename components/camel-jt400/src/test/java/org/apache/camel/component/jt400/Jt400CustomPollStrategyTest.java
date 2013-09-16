begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jt400
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jt400
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
name|Endpoint
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
name|PollingConsumer
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
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|PollingConsumerPollStrategy
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
DECL|class|Jt400CustomPollStrategyTest
specifier|public
class|class
name|Jt400CustomPollStrategyTest
extends|extends
name|Jt400TestSupport
block|{
DECL|field|PASSWORD
specifier|private
specifier|static
specifier|final
name|String
name|PASSWORD
init|=
literal|"p4ssw0rd"
decl_stmt|;
DECL|field|poll
specifier|private
name|PollingConsumerPollStrategy
name|poll
init|=
operator|new
name|MyPollStrategy
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|isUseRouteBuilder ()
specifier|public
name|boolean
name|isUseRouteBuilder
parameter_list|()
block|{
return|return
literal|false
return|;
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
name|jndi
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|jndi
operator|.
name|bind
argument_list|(
literal|"jt400PollStrategy"
argument_list|,
name|poll
argument_list|)
expr_stmt|;
return|return
name|jndi
return|;
block|}
annotation|@
name|Test
DECL|method|testCustomPollStrategy ()
specifier|public
name|void
name|testCustomPollStrategy
parameter_list|()
throws|throws
name|Exception
block|{
name|Jt400DataQueueEndpoint
name|endpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"jt400://user:"
operator|+
name|PASSWORD
operator|+
literal|"@host/qsys.lib/library.lib/queue.dtaq?connectionPool=#mockPool&pollStrategy=#jt400PollStrategy"
argument_list|,
name|Jt400DataQueueEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|PollingConsumer
name|consumer
init|=
name|endpoint
operator|.
name|createPollingConsumer
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
block|}
DECL|class|MyPollStrategy
specifier|private
specifier|static
specifier|final
class|class
name|MyPollStrategy
implements|implements
name|PollingConsumerPollStrategy
block|{
annotation|@
name|Override
DECL|method|begin (Consumer consumer, Endpoint endpoint)
specifier|public
name|boolean
name|begin
parameter_list|(
name|Consumer
name|consumer
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|commit (Consumer consumer, Endpoint endpoint, int i)
specifier|public
name|void
name|commit
parameter_list|(
name|Consumer
name|consumer
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|,
name|int
name|i
parameter_list|)
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|rollback (Consumer consumer, Endpoint endpoint, int i, Exception e)
specifier|public
name|boolean
name|rollback
parameter_list|(
name|Consumer
name|consumer
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|,
name|int
name|i
parameter_list|,
name|Exception
name|e
parameter_list|)
throws|throws
name|Exception
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
end_class

end_unit

