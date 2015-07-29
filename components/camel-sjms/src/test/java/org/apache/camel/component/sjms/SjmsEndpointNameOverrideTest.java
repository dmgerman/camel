begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sjms
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
name|ExchangePattern
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
DECL|class|SjmsEndpointNameOverrideTest
specifier|public
class|class
name|SjmsEndpointNameOverrideTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|BEAN_NAME
specifier|private
specifier|static
specifier|final
name|String
name|BEAN_NAME
init|=
literal|"not-sjms"
decl_stmt|;
annotation|@
name|Override
DECL|method|useJmx ()
specifier|protected
name|boolean
name|useJmx
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Test
DECL|method|testDefaults ()
specifier|public
name|void
name|testDefaults
parameter_list|()
throws|throws
name|Exception
block|{
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
name|BEAN_NAME
operator|+
literal|":test"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|endpoint
operator|instanceof
name|SjmsEndpoint
argument_list|)
expr_stmt|;
name|SjmsEndpoint
name|sjms
init|=
operator|(
name|SjmsEndpoint
operator|)
name|endpoint
decl_stmt|;
name|assertEquals
argument_list|(
name|sjms
operator|.
name|getEndpointUri
argument_list|()
argument_list|,
name|BEAN_NAME
operator|+
literal|"://test"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|sjms
operator|.
name|createExchange
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|,
name|ExchangePattern
operator|.
name|InOnly
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testQueueEndpoint ()
specifier|public
name|void
name|testQueueEndpoint
parameter_list|()
throws|throws
name|Exception
block|{
name|Endpoint
name|sjms
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
name|BEAN_NAME
operator|+
literal|":queue:test"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|sjms
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|sjms
operator|instanceof
name|SjmsEndpoint
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|sjms
operator|.
name|getEndpointUri
argument_list|()
argument_list|,
name|BEAN_NAME
operator|+
literal|"://queue:test"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testTopicEndpoint ()
specifier|public
name|void
name|testTopicEndpoint
parameter_list|()
throws|throws
name|Exception
block|{
name|Endpoint
name|sjms
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
name|BEAN_NAME
operator|+
literal|":topic:test"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|sjms
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|sjms
operator|instanceof
name|SjmsEndpoint
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|sjms
operator|.
name|getEndpointUri
argument_list|()
argument_list|,
name|BEAN_NAME
operator|+
literal|"://topic:test"
argument_list|)
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
name|setConnectionCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
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
name|BEAN_NAME
argument_list|,
name|component
argument_list|)
expr_stmt|;
return|return
name|camelContext
return|;
block|}
block|}
end_class

end_unit

