begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.activemq
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|activemq
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|activemq
operator|.
name|broker
operator|.
name|BrokerService
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
name|ProducerTemplate
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
name|impl
operator|.
name|DefaultCamelContext
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

begin_class
DECL|class|BrokerPreShutdownHookTest
specifier|public
class|class
name|BrokerPreShutdownHookTest
block|{
DECL|class|TestProcessor
specifier|static
class|class
name|TestProcessor
implements|implements
name|Processor
block|{
DECL|field|messageReceived
name|boolean
name|messageReceived
decl_stmt|;
annotation|@
name|Override
DECL|method|process (final Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|messageReceived
operator|=
literal|true
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testShouldCleanlyShutdownCamelBeforeStoppingBroker ()
specifier|public
name|void
name|testShouldCleanlyShutdownCamelBeforeStoppingBroker
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|BrokerService
name|broker
init|=
operator|new
name|BrokerService
argument_list|()
decl_stmt|;
name|broker
operator|.
name|setBrokerName
argument_list|(
literal|"testBroker"
argument_list|)
expr_stmt|;
name|broker
operator|.
name|setUseJmx
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|broker
operator|.
name|setPersistent
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|broker
operator|.
name|addConnector
argument_list|(
literal|"vm://testBroker"
argument_list|)
expr_stmt|;
specifier|final
name|DefaultCamelContext
name|camel
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|camel
operator|.
name|setName
argument_list|(
literal|"test-camel"
argument_list|)
expr_stmt|;
specifier|final
name|CamelShutdownHook
name|hook
init|=
operator|new
name|CamelShutdownHook
argument_list|(
name|broker
argument_list|)
decl_stmt|;
name|hook
operator|.
name|setCamelContext
argument_list|(
name|camel
argument_list|)
expr_stmt|;
name|broker
operator|.
name|start
argument_list|()
expr_stmt|;
name|camel
operator|.
name|addComponent
argument_list|(
literal|"testq"
argument_list|,
name|ActiveMQComponent
operator|.
name|activeMQComponent
argument_list|(
literal|"vm://testBroker?create=false"
argument_list|)
argument_list|)
expr_stmt|;
specifier|final
name|TestProcessor
name|processor
init|=
operator|new
name|TestProcessor
argument_list|()
decl_stmt|;
name|camel
operator|.
name|addRoutes
argument_list|(
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
literal|"testq:test.in"
argument_list|)
operator|.
name|delay
argument_list|(
literal|200
argument_list|)
operator|.
name|process
argument_list|(
name|processor
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|camel
operator|.
name|start
argument_list|()
expr_stmt|;
specifier|final
name|ProducerTemplate
name|producer
init|=
name|camel
operator|.
name|createProducerTemplate
argument_list|()
decl_stmt|;
name|producer
operator|.
name|sendBody
argument_list|(
literal|"testq:test.in"
argument_list|,
literal|"Hi!"
argument_list|)
expr_stmt|;
name|producer
operator|.
name|stop
argument_list|()
expr_stmt|;
name|broker
operator|.
name|stop
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Message should be received"
argument_list|,
name|processor
operator|.
name|messageReceived
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Camel context should be stopped"
argument_list|,
name|camel
operator|.
name|isStopped
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Broker should be stopped"
argument_list|,
name|broker
operator|.
name|isStopped
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

