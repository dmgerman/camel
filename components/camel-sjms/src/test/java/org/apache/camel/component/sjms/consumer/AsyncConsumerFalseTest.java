begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sjms.consumer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|sjms
operator|.
name|consumer
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
name|component
operator|.
name|sjms
operator|.
name|SjmsComponent
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
name|sjms
operator|.
name|support
operator|.
name|MyAsyncComponent
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

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|AsyncConsumerFalseTest
specifier|public
class|class
name|AsyncConsumerFalseTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testAsyncJmsConsumer ()
specifier|public
name|void
name|testAsyncJmsConsumer
parameter_list|()
throws|throws
name|Exception
block|{
comment|// async is disabled (so we should receive in same order)
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Camel"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"sjms:queue:start"
argument_list|,
literal|"Hello Camel"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"sjms:queue:start"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
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
name|camelContext
operator|.
name|addComponent
argument_list|(
literal|"async"
argument_list|,
operator|new
name|MyAsyncComponent
argument_list|()
argument_list|)
expr_stmt|;
name|ActiveMQConnectionFactory
name|connectionFactory
init|=
operator|new
name|ActiveMQConnectionFactory
argument_list|(
literal|"vm://broker?broker.persistent=false&broker.useJmx=false"
argument_list|)
decl_stmt|;
name|SjmsComponent
name|component
init|=
operator|new
name|SjmsComponent
argument_list|()
decl_stmt|;
name|component
operator|.
name|setConnectionFactory
argument_list|(
name|connectionFactory
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|addComponent
argument_list|(
literal|"sjms"
argument_list|,
name|component
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
comment|// disable async in only mode on the consumer
name|from
argument_list|(
literal|"sjms:queue:start"
argument_list|)
operator|.
name|choice
argument_list|()
operator|.
name|when
argument_list|(
name|body
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Camel"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"async:camel?delay=2000"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|otherwise
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

