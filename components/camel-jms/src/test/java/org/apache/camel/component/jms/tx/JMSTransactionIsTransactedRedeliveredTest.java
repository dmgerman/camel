begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jms.tx
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jms
operator|.
name|tx
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|MBeanServer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|ObjectName
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
name|AdviceWithRouteBuilder
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
name|reifier
operator|.
name|RouteReifier
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
name|spring
operator|.
name|CamelSpringTestSupport
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
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|ClassPathXmlApplicationContext
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|JMSTransactionIsTransactedRedeliveredTest
specifier|public
class|class
name|JMSTransactionIsTransactedRedeliveredTest
extends|extends
name|CamelSpringTestSupport
block|{
annotation|@
name|Override
DECL|method|createApplicationContext ()
specifier|protected
name|ClassPathXmlApplicationContext
name|createApplicationContext
parameter_list|()
block|{
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"/org/apache/camel/component/jms/tx/JMSTransactionIsTransactedRedeliveredTest.xml"
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|isUseAdviceWith ()
specifier|public
name|boolean
name|isUseAdviceWith
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|getMBeanServer ()
specifier|protected
name|MBeanServer
name|getMBeanServer
parameter_list|()
block|{
return|return
name|context
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|getManagementAgent
argument_list|()
operator|.
name|getMBeanServer
argument_list|()
return|;
block|}
annotation|@
name|Test
DECL|method|testTransactionSuccess ()
specifier|public
name|void
name|testTransactionSuccess
parameter_list|()
throws|throws
name|Exception
block|{
name|RouteReifier
operator|.
name|adviceWith
argument_list|(
name|context
operator|.
name|getRouteDefinitions
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
name|context
argument_list|,
operator|new
name|AdviceWithRouteBuilder
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
name|onException
argument_list|(
name|AssertionError
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:error"
argument_list|,
literal|"mock:error"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
comment|// there should be no assertion errors
name|MockEndpoint
name|error
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:error"
argument_list|)
decl_stmt|;
name|error
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Bye World"
argument_list|)
expr_stmt|;
comment|// success at 3rd attempt
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
literal|"count"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"activemq:queue:okay"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|error
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
comment|// check JMX stats
comment|// need a little sleep to ensure JMX is updated
name|Thread
operator|.
name|sleep
argument_list|(
literal|500
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|ObjectName
argument_list|>
name|objectNames
init|=
name|getMBeanServer
argument_list|()
operator|.
name|queryNames
argument_list|(
operator|new
name|ObjectName
argument_list|(
literal|"org.apache.camel:context=camel-*,type=routes,name=\"myRoute\""
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|objectNames
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ObjectName
name|name
init|=
name|objectNames
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|Long
name|total
init|=
operator|(
name|Long
operator|)
name|getMBeanServer
argument_list|()
operator|.
name|getAttribute
argument_list|(
name|name
argument_list|,
literal|"ExchangesTotal"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|total
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|Long
name|completed
init|=
operator|(
name|Long
operator|)
name|getMBeanServer
argument_list|()
operator|.
name|getAttribute
argument_list|(
name|name
argument_list|,
literal|"ExchangesCompleted"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|completed
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|Long
name|failed
init|=
operator|(
name|Long
operator|)
name|getMBeanServer
argument_list|()
operator|.
name|getAttribute
argument_list|(
name|name
argument_list|,
literal|"ExchangesFailed"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|failed
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
comment|// Camel error handler redeliveries (we do not use that in this example)
name|Long
name|redeliveries
init|=
operator|(
name|Long
operator|)
name|getMBeanServer
argument_list|()
operator|.
name|getAttribute
argument_list|(
name|name
argument_list|,
literal|"Redeliveries"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|redeliveries
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
comment|// Camel error handler redeliveries (we do not use that in this example)
comment|// there should be 2 external redeliveries
name|Long
name|externalRedeliveries
init|=
operator|(
name|Long
operator|)
name|getMBeanServer
argument_list|()
operator|.
name|getAttribute
argument_list|(
name|name
argument_list|,
literal|"ExternalRedeliveries"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|externalRedeliveries
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|class|MyProcessor
specifier|public
specifier|static
class|class
name|MyProcessor
implements|implements
name|Processor
block|{
DECL|field|count
specifier|private
name|int
name|count
decl_stmt|;
annotation|@
name|Override
DECL|method|process (Exchange exchange)
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
operator|++
name|count
expr_stmt|;
comment|// the first is not redelivered
if|if
condition|(
name|count
operator|==
literal|1
condition|)
block|{
name|assertFalse
argument_list|(
literal|"Should not be external redelivered"
argument_list|,
name|exchange
operator|.
name|isExternalRedelivered
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|assertTrue
argument_list|(
literal|"Should be external redelivered"
argument_list|,
name|exchange
operator|.
name|isExternalRedelivered
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|count
operator|<
literal|3
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Forced exception"
argument_list|)
throw|;
block|}
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Bye World"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"count"
argument_list|,
name|count
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

