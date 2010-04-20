begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jms.tuning
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
name|tuning
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|ConnectionFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|activemq
operator|.
name|ActiveMQConnectionFactory
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

begin_import
import|import static
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
name|JmsComponent
operator|.
name|jmsComponentClientAcknowledge
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|PerformanceRouteTest
specifier|public
class|class
name|PerformanceRouteTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|size
specifier|private
name|int
name|size
init|=
literal|200
decl_stmt|;
annotation|@
name|Test
DECL|method|testPerformance ()
specifier|public
name|void
name|testPerformance
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|canRunOnThisPlatform
argument_list|()
condition|)
block|{
return|return;
block|}
name|long
name|start
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:audit"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
name|size
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:audit"
argument_list|)
operator|.
name|expectsNoDuplicates
argument_list|()
operator|.
name|body
argument_list|()
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:gold"
argument_list|)
operator|.
name|expectedMinimumMessageCount
argument_list|(
operator|(
name|size
operator|/
literal|2
operator|)
operator|-
operator|(
name|size
operator|/
literal|10
operator|)
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:silver"
argument_list|)
operator|.
name|expectedMinimumMessageCount
argument_list|(
name|size
operator|/
literal|10
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|size
condition|;
name|i
operator|++
control|)
block|{
name|String
name|type
decl_stmt|;
if|if
condition|(
name|i
operator|%
literal|10
operator|==
literal|0
condition|)
block|{
name|type
operator|=
literal|"silver"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|i
operator|%
literal|2
operator|==
literal|0
condition|)
block|{
name|type
operator|=
literal|"gold"
expr_stmt|;
block|}
else|else
block|{
name|type
operator|=
literal|"bronze"
expr_stmt|;
block|}
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"activemq:queue:inbox"
argument_list|,
literal|"Message "
operator|+
name|i
argument_list|,
literal|"type"
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|long
name|delta
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|-
name|start
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"RoutePerformanceTest: Sent: "
operator|+
name|size
operator|+
literal|" Took: "
operator|+
name|delta
operator|+
literal|" ms"
argument_list|)
expr_stmt|;
block|}
DECL|method|canRunOnThisPlatform ()
specifier|private
name|boolean
name|canRunOnThisPlatform
parameter_list|()
block|{
name|String
name|os
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"os.name"
argument_list|)
decl_stmt|;
comment|// HP-UX is just to slow to run this test
return|return
operator|!
name|os
operator|.
name|toLowerCase
argument_list|()
operator|.
name|contains
argument_list|(
literal|"hp-ux"
argument_list|)
return|;
block|}
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|camelContext
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
name|ConnectionFactory
name|connectionFactory
init|=
operator|new
name|ActiveMQConnectionFactory
argument_list|(
literal|"vm://localhost?broker.persistent=false"
argument_list|)
decl_stmt|;
name|camelContext
operator|.
name|addComponent
argument_list|(
literal|"activemq"
argument_list|,
name|jmsComponentClientAcknowledge
argument_list|(
name|connectionFactory
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|camelContext
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
literal|"activemq:queue:inbox?concurrentConsumers=10"
argument_list|)
operator|.
name|to
argument_list|(
literal|"activemq:topic:audit"
argument_list|)
operator|.
name|choice
argument_list|()
operator|.
name|when
argument_list|(
name|header
argument_list|(
literal|"type"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"gold"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:gold"
argument_list|)
operator|.
name|when
argument_list|(
name|header
argument_list|(
literal|"type"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"silver"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:silver"
argument_list|)
operator|.
name|otherwise
argument_list|()
operator|.
name|to
argument_list|(
literal|"direct:bronze"
argument_list|)
operator|.
name|end
argument_list|()
expr_stmt|;
name|from
argument_list|(
literal|"direct:gold"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:gold"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:silver"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:silver"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:bronze"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:bronze"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"activemq:topic:audit"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:audit"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

