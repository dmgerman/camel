begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.config
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|config
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|Route
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
name|EventDrivenConsumerRoute
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
name|processor
operator|.
name|DeadLetterChannel
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
name|processor
operator|.
name|RedeliveryPolicy
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
name|spring
operator|.
name|SpringTestSupport
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

begin_class
DECL|class|ErrorHandlerTest
specifier|public
class|class
name|ErrorHandlerTest
extends|extends
name|SpringTestSupport
block|{
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
literal|"org/apache/camel/spring/config/errorHandler.xml"
argument_list|)
return|;
block|}
DECL|method|testEndpointConfiguration ()
specifier|public
name|void
name|testEndpointConfiguration
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
name|getMandatoryBean
argument_list|(
name|CamelContext
operator|.
name|class
argument_list|,
literal|"camel"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Route
argument_list|>
name|list
init|=
name|context
operator|.
name|getRoutes
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Number routes created"
operator|+
name|list
argument_list|,
literal|2
argument_list|,
name|list
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Route
name|route
range|:
name|list
control|)
block|{
name|EventDrivenConsumerRoute
name|consumerRoute
init|=
name|assertIsInstanceOf
argument_list|(
name|EventDrivenConsumerRoute
operator|.
name|class
argument_list|,
name|route
argument_list|)
decl_stmt|;
name|Processor
name|processor
init|=
name|consumerRoute
operator|.
name|getProcessor
argument_list|()
decl_stmt|;
name|processor
operator|=
name|unwrap
argument_list|(
name|processor
argument_list|)
expr_stmt|;
name|DeadLetterChannel
name|deadLetterChannel
init|=
name|assertIsInstanceOf
argument_list|(
name|DeadLetterChannel
operator|.
name|class
argument_list|,
name|processor
argument_list|)
decl_stmt|;
name|RedeliveryPolicy
name|redeliveryPolicy
init|=
name|deadLetterChannel
operator|.
name|getRedeliveryPolicy
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"getMaximumRedeliveries()"
argument_list|,
literal|1
argument_list|,
name|redeliveryPolicy
operator|.
name|getMaximumRedeliveries
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"isUseExponentialBackOff()"
argument_list|,
literal|true
argument_list|,
name|redeliveryPolicy
operator|.
name|isUseExponentialBackOff
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

