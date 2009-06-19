begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jms.temp
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
name|temp
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|TemporaryQueue
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|TemporaryTopic
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|Context
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
name|camel
operator|.
name|component
operator|.
name|ActiveMQComponent
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
name|command
operator|.
name|ActiveMQTempQueue
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
name|command
operator|.
name|ActiveMQTempTopic
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
name|jms
operator|.
name|JmsConfiguration
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
name|jms
operator|.
name|JmsEndpoint
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
name|jms
operator|.
name|JmsProviderMetadata
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
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|JmsProviderTest
specifier|public
class|class
name|JmsProviderTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testTemporaryDestinationTypes ()
specifier|public
name|void
name|testTemporaryDestinationTypes
parameter_list|()
throws|throws
name|Exception
block|{
name|JmsEndpoint
name|endpoint
init|=
name|getMandatoryEndpoint
argument_list|(
literal|"activemq:test.queue"
argument_list|,
name|JmsEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|JmsConfiguration
name|configuration
init|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|JmsProviderMetadata
name|providerMetadata
init|=
name|configuration
operator|.
name|getProviderMetadata
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"provider"
argument_list|,
name|providerMetadata
argument_list|)
expr_stmt|;
name|Class
argument_list|<
name|?
extends|extends
name|TemporaryQueue
argument_list|>
name|queueType
init|=
name|endpoint
operator|.
name|getTemporaryQueueType
argument_list|()
decl_stmt|;
name|Class
argument_list|<
name|?
extends|extends
name|TemporaryTopic
argument_list|>
name|topicType
init|=
name|endpoint
operator|.
name|getTemporaryTopicType
argument_list|()
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Found queue type: "
operator|+
name|queueType
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Found topic type: "
operator|+
name|topicType
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"queueType"
argument_list|,
name|queueType
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"topicType"
argument_list|,
name|topicType
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"queueType"
argument_list|,
name|ActiveMQTempQueue
operator|.
name|class
argument_list|,
name|queueType
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"topicType"
argument_list|,
name|ActiveMQTempTopic
operator|.
name|class
argument_list|,
name|topicType
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createJndiContext ()
specifier|protected
name|Context
name|createJndiContext
parameter_list|()
throws|throws
name|Exception
block|{
name|Context
name|context
init|=
name|super
operator|.
name|createJndiContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|bind
argument_list|(
literal|"activemq"
argument_list|,
name|ActiveMQComponent
operator|.
name|activeMQComponent
argument_list|(
literal|"vm://localhost"
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
block|}
end_class

end_unit

