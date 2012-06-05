begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.spring.batch
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|spring
operator|.
name|batch
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
name|junit
operator|.
name|Before
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
name|ApplicationContext
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
DECL|class|SpringBatchIntegrationTest
specifier|public
class|class
name|SpringBatchIntegrationTest
block|{
DECL|field|applicationContext
name|ApplicationContext
name|applicationContext
decl_stmt|;
DECL|field|producerTemplate
name|ProducerTemplate
name|producerTemplate
decl_stmt|;
DECL|field|outputEndpoint
name|MockEndpoint
name|outputEndpoint
decl_stmt|;
DECL|field|jobExecutionEventsQueueEndpoint
name|MockEndpoint
name|jobExecutionEventsQueueEndpoint
decl_stmt|;
DECL|field|inputMessages
name|String
index|[]
name|inputMessages
decl_stmt|;
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|applicationContext
operator|=
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"classpath:org/apache/camel/component/spring/batch/springBatchtestContext.xml"
argument_list|)
expr_stmt|;
name|producerTemplate
operator|=
name|applicationContext
operator|.
name|getBean
argument_list|(
name|ProducerTemplate
operator|.
name|class
argument_list|)
expr_stmt|;
name|outputEndpoint
operator|=
name|applicationContext
operator|.
name|getBean
argument_list|(
name|CamelContext
operator|.
name|class
argument_list|)
operator|.
name|getEndpoint
argument_list|(
literal|"mock:output"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
name|jobExecutionEventsQueueEndpoint
operator|=
name|applicationContext
operator|.
name|getBean
argument_list|(
name|CamelContext
operator|.
name|class
argument_list|)
operator|.
name|getEndpoint
argument_list|(
literal|"mock:jobExecutionEventsQueue"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
name|inputMessages
operator|=
operator|new
name|String
index|[]
block|{
literal|"foo"
block|,
literal|"bar"
block|,
literal|"baz"
block|}
expr_stmt|;
for|for
control|(
name|String
name|message
range|:
name|inputMessages
control|)
block|{
name|producerTemplate
operator|.
name|sendBody
argument_list|(
literal|"seda:inputQueue"
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
name|producerTemplate
operator|.
name|sendBody
argument_list|(
literal|"seda:inputQueue"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldEchoInBatch ()
specifier|public
name|void
name|shouldEchoInBatch
parameter_list|()
throws|throws
name|InterruptedException
block|{
comment|// When
name|producerTemplate
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Start batch!"
argument_list|)
expr_stmt|;
comment|// Then
name|outputEndpoint
operator|.
name|setExpectedMessageCount
argument_list|(
name|inputMessages
operator|.
name|length
argument_list|)
expr_stmt|;
name|outputEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|outputEndpoint
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|startsWith
argument_list|(
literal|"Echo "
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldGenerateBatchExecutionEvents ()
specifier|public
name|void
name|shouldGenerateBatchExecutionEvents
parameter_list|()
throws|throws
name|InterruptedException
block|{
comment|// When
name|producerTemplate
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Start batch!"
argument_list|)
expr_stmt|;
comment|// Then
name|jobExecutionEventsQueueEndpoint
operator|.
name|setExpectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|jobExecutionEventsQueueEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

