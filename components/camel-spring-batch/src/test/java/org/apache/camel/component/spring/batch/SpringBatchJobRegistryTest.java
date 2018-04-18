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
name|ConsumerTemplate
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
name|component
operator|.
name|spring
operator|.
name|batch
operator|.
name|support
operator|.
name|CamelItemProcessor
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
name|spring
operator|.
name|batch
operator|.
name|support
operator|.
name|CamelItemReader
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
name|spring
operator|.
name|batch
operator|.
name|support
operator|.
name|CamelItemWriter
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
name|spring
operator|.
name|batch
operator|.
name|support
operator|.
name|CamelJobExecutionListener
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
name|javaconfig
operator|.
name|SingleRouteCamelConfiguration
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
name|CamelSpringDelegatingTestContextLoader
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
name|CamelSpringRunner
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
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|batch
operator|.
name|core
operator|.
name|Job
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|batch
operator|.
name|core
operator|.
name|JobExecutionListener
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|batch
operator|.
name|core
operator|.
name|Step
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|batch
operator|.
name|core
operator|.
name|configuration
operator|.
name|annotation
operator|.
name|EnableBatchProcessing
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|batch
operator|.
name|core
operator|.
name|configuration
operator|.
name|annotation
operator|.
name|JobBuilderFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|batch
operator|.
name|core
operator|.
name|configuration
operator|.
name|annotation
operator|.
name|StepBuilderFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|batch
operator|.
name|core
operator|.
name|configuration
operator|.
name|support
operator|.
name|ApplicationContextFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|batch
operator|.
name|core
operator|.
name|configuration
operator|.
name|support
operator|.
name|GenericApplicationContextFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|batch
operator|.
name|item
operator|.
name|ItemProcessor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|batch
operator|.
name|item
operator|.
name|ItemReader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|batch
operator|.
name|item
operator|.
name|ItemWriter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|annotation
operator|.
name|Autowired
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
name|annotation
operator|.
name|Bean
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
name|annotation
operator|.
name|Configuration
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
name|annotation
operator|.
name|Import
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|annotation
operator|.
name|DirtiesContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|ContextConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|junit4
operator|.
name|AbstractJUnit4SpringContextTests
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|CamelSpringRunner
operator|.
name|class
argument_list|)
annotation|@
name|ContextConfiguration
argument_list|(
name|classes
operator|=
name|SpringBatchJobRegistryTest
operator|.
name|ContextConfig
operator|.
name|class
argument_list|,
name|loader
operator|=
name|CamelSpringDelegatingTestContextLoader
operator|.
name|class
argument_list|)
DECL|class|SpringBatchJobRegistryTest
specifier|public
class|class
name|SpringBatchJobRegistryTest
extends|extends
name|AbstractJUnit4SpringContextTests
block|{
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:output"
argument_list|)
DECL|field|outputEndpoint
name|MockEndpoint
name|outputEndpoint
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:jobExecutionEventsQueue"
argument_list|)
DECL|field|jobExecutionEventsQueueEndpoint
name|MockEndpoint
name|jobExecutionEventsQueueEndpoint
decl_stmt|;
annotation|@
name|Autowired
DECL|field|template
name|ProducerTemplate
name|template
decl_stmt|;
annotation|@
name|Autowired
DECL|field|consumer
name|ConsumerTemplate
name|consumer
decl_stmt|;
DECL|field|inputMessages
name|String
index|[]
name|inputMessages
init|=
operator|new
name|String
index|[]
block|{
literal|"foo"
block|,
literal|"bar"
block|,
literal|"baz"
block|,
literal|null
block|}
decl_stmt|;
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
for|for
control|(
name|String
name|message
range|:
name|inputMessages
control|)
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:inputQueue"
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|DirtiesContext
annotation|@
name|Test
DECL|method|testJobRegistry ()
specifier|public
name|void
name|testJobRegistry
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|outputEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Echo foo"
argument_list|,
literal|"Echo bar"
argument_list|,
literal|"Echo baz"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Start batch!"
argument_list|)
expr_stmt|;
name|outputEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Configuration
annotation|@
name|Import
argument_list|(
name|value
operator|=
name|BatchConfig
operator|.
name|class
argument_list|)
DECL|class|ContextConfig
specifier|public
specifier|static
class|class
name|ContextConfig
extends|extends
name|SingleRouteCamelConfiguration
block|{
annotation|@
name|Override
DECL|method|route ()
specifier|public
name|RouteBuilder
name|route
parameter_list|()
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
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"spring-batch:echoJob?jobRegistry=#jobRegistry"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:processor"
argument_list|)
operator|.
name|setExchangePattern
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|)
operator|.
name|setBody
argument_list|(
name|simple
argument_list|(
literal|"Echo ${body}"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
annotation|@
name|EnableBatchProcessing
argument_list|(
name|modular
operator|=
literal|true
argument_list|)
DECL|class|BatchConfig
specifier|public
specifier|static
class|class
name|BatchConfig
block|{
annotation|@
name|Bean
DECL|method|testJobs ()
specifier|public
name|ApplicationContextFactory
name|testJobs
parameter_list|()
block|{
return|return
operator|new
name|GenericApplicationContextFactory
argument_list|(
name|ChildBatchConfig
operator|.
name|class
argument_list|)
return|;
block|}
block|}
annotation|@
name|Configuration
DECL|class|ChildBatchConfig
specifier|public
specifier|static
class|class
name|ChildBatchConfig
block|{
annotation|@
name|Autowired
DECL|field|jobs
name|JobBuilderFactory
name|jobs
decl_stmt|;
annotation|@
name|Autowired
DECL|field|steps
name|StepBuilderFactory
name|steps
decl_stmt|;
annotation|@
name|Autowired
DECL|field|consumerTemplate
name|ConsumerTemplate
name|consumerTemplate
decl_stmt|;
annotation|@
name|Autowired
DECL|field|producerTemplate
name|ProducerTemplate
name|producerTemplate
decl_stmt|;
annotation|@
name|Bean
DECL|method|reader ()
specifier|protected
name|ItemReader
argument_list|<
name|Object
argument_list|>
name|reader
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|CamelItemReader
argument_list|<>
argument_list|(
name|consumerTemplate
argument_list|,
literal|"seda:inputQueue"
argument_list|)
return|;
block|}
annotation|@
name|Bean
DECL|method|writer ()
specifier|protected
name|ItemWriter
argument_list|<
name|Object
argument_list|>
name|writer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|CamelItemWriter
argument_list|<>
argument_list|(
name|producerTemplate
argument_list|,
literal|"mock:output"
argument_list|)
return|;
block|}
annotation|@
name|Bean
DECL|method|processor ()
specifier|protected
name|ItemProcessor
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|processor
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|CamelItemProcessor
argument_list|<>
argument_list|(
name|producerTemplate
argument_list|,
literal|"direct:processor"
argument_list|)
return|;
block|}
annotation|@
name|Bean
DECL|method|jobExecutionListener ()
specifier|protected
name|JobExecutionListener
name|jobExecutionListener
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|CamelJobExecutionListener
argument_list|(
name|producerTemplate
argument_list|,
literal|"mock:jobExecutionEventsQueue"
argument_list|)
return|;
block|}
annotation|@
name|Bean
DECL|method|echoJob ()
specifier|public
name|Job
name|echoJob
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|this
operator|.
name|jobs
operator|.
name|get
argument_list|(
literal|"echoJob"
argument_list|)
operator|.
name|start
argument_list|(
name|echoStep
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
annotation|@
name|Bean
DECL|method|echoStep ()
specifier|protected
name|Step
name|echoStep
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|this
operator|.
name|steps
operator|.
name|get
argument_list|(
literal|"echoStep"
argument_list|)
operator|.
name|chunk
argument_list|(
literal|3
argument_list|)
operator|.
name|reader
argument_list|(
name|reader
argument_list|()
argument_list|)
operator|.
name|processor
argument_list|(
name|processor
argument_list|()
argument_list|)
operator|.
name|writer
argument_list|(
name|writer
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

