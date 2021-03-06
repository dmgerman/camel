begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jbpm.workitem
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jbpm
operator|.
name|workitem
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|Message
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
name|jbpm
operator|.
name|JBPMConstants
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
name|engine
operator|.
name|DefaultHeadersMapFactory
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
name|HeadersMapFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|drools
operator|.
name|core
operator|.
name|process
operator|.
name|instance
operator|.
name|impl
operator|.
name|WorkItemImpl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jbpm
operator|.
name|bpmn2
operator|.
name|handler
operator|.
name|WorkItemHandlerRuntimeException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jbpm
operator|.
name|process
operator|.
name|workitem
operator|.
name|core
operator|.
name|TestWorkItemManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jbpm
operator|.
name|services
operator|.
name|api
operator|.
name|service
operator|.
name|ServiceRegistry
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
name|kie
operator|.
name|api
operator|.
name|runtime
operator|.
name|manager
operator|.
name|RuntimeManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|ArgumentMatchers
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|Mock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|junit
operator|.
name|MockitoJUnitRunner
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|*
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
name|*
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|ArgumentMatchers
operator|.
name|*
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|*
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|MockitoJUnitRunner
operator|.
name|class
argument_list|)
DECL|class|InOutCamelWorkItemHandlerTest
specifier|public
class|class
name|InOutCamelWorkItemHandlerTest
block|{
annotation|@
name|Mock
DECL|field|producerTemplate
name|ProducerTemplate
name|producerTemplate
decl_stmt|;
annotation|@
name|Mock
DECL|field|outExchange
name|Exchange
name|outExchange
decl_stmt|;
annotation|@
name|Mock
DECL|field|outMessage
name|Message
name|outMessage
decl_stmt|;
annotation|@
name|Mock
DECL|field|camelContext
name|CamelContext
name|camelContext
decl_stmt|;
annotation|@
name|Mock
DECL|field|runtimeManager
name|RuntimeManager
name|runtimeManager
decl_stmt|;
annotation|@
name|Test
DECL|method|testExecuteInOutGlobalCamelContext ()
specifier|public
name|void
name|testExecuteInOutGlobalCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|camelEndpointId
init|=
literal|"testCamelRoute"
decl_stmt|;
name|String
name|camelRouteUri
init|=
literal|"direct:"
operator|+
name|camelEndpointId
decl_stmt|;
name|String
name|testReponse
init|=
literal|"testResponse"
decl_stmt|;
name|when
argument_list|(
name|producerTemplate
operator|.
name|send
argument_list|(
name|eq
argument_list|(
name|camelRouteUri
argument_list|)
argument_list|,
name|ArgumentMatchers
operator|.
name|any
argument_list|(
name|Exchange
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|outExchange
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|producerTemplate
operator|.
name|getCamelContext
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|camelContext
operator|.
name|createProducerTemplate
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|producerTemplate
argument_list|)
expr_stmt|;
name|HeadersMapFactory
name|hmf
init|=
operator|new
name|DefaultHeadersMapFactory
argument_list|()
decl_stmt|;
name|when
argument_list|(
name|camelContext
operator|.
name|getHeadersMapFactory
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|hmf
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|outExchange
operator|.
name|getOut
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|outMessage
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|outMessage
operator|.
name|getBody
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|testReponse
argument_list|)
expr_stmt|;
try|try
block|{
name|ServiceRegistry
operator|.
name|get
argument_list|()
operator|.
name|register
argument_list|(
literal|"GlobalCamelService"
argument_list|,
name|camelContext
argument_list|)
expr_stmt|;
name|TestWorkItemManager
name|manager
init|=
operator|new
name|TestWorkItemManager
argument_list|()
decl_stmt|;
name|WorkItemImpl
name|workItem
init|=
operator|new
name|WorkItemImpl
argument_list|()
decl_stmt|;
name|workItem
operator|.
name|setParameter
argument_list|(
literal|"CamelEndpointId"
argument_list|,
name|camelEndpointId
argument_list|)
expr_stmt|;
name|workItem
operator|.
name|setParameter
argument_list|(
literal|"Request"
argument_list|,
literal|"someRequest"
argument_list|)
expr_stmt|;
name|workItem
operator|.
name|setDeploymentId
argument_list|(
literal|"testDeploymentId"
argument_list|)
expr_stmt|;
name|workItem
operator|.
name|setProcessInstanceId
argument_list|(
literal|1L
argument_list|)
expr_stmt|;
name|workItem
operator|.
name|setId
argument_list|(
literal|1L
argument_list|)
expr_stmt|;
name|AbstractCamelWorkItemHandler
name|handler
init|=
operator|new
name|InOutCamelWorkItemHandler
argument_list|()
decl_stmt|;
name|handler
operator|.
name|executeWorkItem
argument_list|(
name|workItem
argument_list|,
name|manager
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|manager
operator|.
name|getResults
argument_list|()
argument_list|,
name|is
argument_list|(
name|notNullValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|manager
operator|.
name|getResults
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|manager
operator|.
name|getResults
argument_list|()
operator|.
name|containsKey
argument_list|(
name|workItem
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|,
name|is
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|results
init|=
name|manager
operator|.
name|getResults
argument_list|(
name|workItem
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|results
operator|.
name|size
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|results
operator|.
name|get
argument_list|(
literal|"Response"
argument_list|)
argument_list|,
name|equalTo
argument_list|(
name|testReponse
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|ServiceRegistry
operator|.
name|get
argument_list|()
operator|.
name|remove
argument_list|(
literal|"GlobalCamelService"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testExecuteInOutLocalCamelContext ()
specifier|public
name|void
name|testExecuteInOutLocalCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|camelEndpointId
init|=
literal|"testCamelRoute"
decl_stmt|;
name|String
name|camelRouteUri
init|=
literal|"direct:"
operator|+
name|camelEndpointId
decl_stmt|;
name|String
name|testReponse
init|=
literal|"testResponse"
decl_stmt|;
name|String
name|runtimeManagerId
init|=
literal|"testRuntimeManager"
decl_stmt|;
name|when
argument_list|(
name|runtimeManager
operator|.
name|getIdentifier
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|runtimeManagerId
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|producerTemplate
operator|.
name|send
argument_list|(
name|eq
argument_list|(
name|camelRouteUri
argument_list|)
argument_list|,
name|ArgumentMatchers
operator|.
name|any
argument_list|(
name|Exchange
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|outExchange
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|producerTemplate
operator|.
name|getCamelContext
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|camelContext
operator|.
name|createProducerTemplate
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|producerTemplate
argument_list|)
expr_stmt|;
name|HeadersMapFactory
name|hmf
init|=
operator|new
name|DefaultHeadersMapFactory
argument_list|()
decl_stmt|;
name|when
argument_list|(
name|camelContext
operator|.
name|getHeadersMapFactory
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|hmf
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|outExchange
operator|.
name|getOut
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|outMessage
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|outMessage
operator|.
name|getBody
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|testReponse
argument_list|)
expr_stmt|;
comment|// Register the RuntimeManager bound camelcontext.
try|try
block|{
name|ServiceRegistry
operator|.
name|get
argument_list|()
operator|.
name|register
argument_list|(
name|runtimeManagerId
operator|+
literal|"_CamelService"
argument_list|,
name|camelContext
argument_list|)
expr_stmt|;
name|WorkItemImpl
name|workItem
init|=
operator|new
name|WorkItemImpl
argument_list|()
decl_stmt|;
name|workItem
operator|.
name|setParameter
argument_list|(
name|JBPMConstants
operator|.
name|CAMEL_ENDPOINT_ID_WI_PARAM
argument_list|,
name|camelEndpointId
argument_list|)
expr_stmt|;
name|workItem
operator|.
name|setParameter
argument_list|(
literal|"Request"
argument_list|,
literal|"someRequest"
argument_list|)
expr_stmt|;
name|workItem
operator|.
name|setDeploymentId
argument_list|(
literal|"testDeploymentId"
argument_list|)
expr_stmt|;
name|workItem
operator|.
name|setProcessInstanceId
argument_list|(
literal|1L
argument_list|)
expr_stmt|;
name|workItem
operator|.
name|setId
argument_list|(
literal|1L
argument_list|)
expr_stmt|;
name|AbstractCamelWorkItemHandler
name|handler
init|=
operator|new
name|InOutCamelWorkItemHandler
argument_list|(
name|runtimeManager
argument_list|)
decl_stmt|;
name|TestWorkItemManager
name|manager
init|=
operator|new
name|TestWorkItemManager
argument_list|()
decl_stmt|;
name|handler
operator|.
name|executeWorkItem
argument_list|(
name|workItem
argument_list|,
name|manager
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|manager
operator|.
name|getResults
argument_list|()
argument_list|,
name|is
argument_list|(
name|notNullValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|manager
operator|.
name|getResults
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|manager
operator|.
name|getResults
argument_list|()
operator|.
name|containsKey
argument_list|(
name|workItem
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|,
name|is
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|results
init|=
name|manager
operator|.
name|getResults
argument_list|(
name|workItem
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|results
operator|.
name|size
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|results
operator|.
name|get
argument_list|(
name|JBPMConstants
operator|.
name|RESPONSE_WI_PARAM
argument_list|)
argument_list|,
name|equalTo
argument_list|(
name|testReponse
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|ServiceRegistry
operator|.
name|get
argument_list|()
operator|.
name|remove
argument_list|(
name|runtimeManagerId
operator|+
literal|"_CamelService"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testExecuteInOutLocalCamelContextLazyInit ()
specifier|public
name|void
name|testExecuteInOutLocalCamelContextLazyInit
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|camelEndpointId
init|=
literal|"testCamelRoute"
decl_stmt|;
name|String
name|camelRouteUri
init|=
literal|"direct:"
operator|+
name|camelEndpointId
decl_stmt|;
name|String
name|testReponse
init|=
literal|"testResponse"
decl_stmt|;
name|String
name|runtimeManagerId
init|=
literal|"testRuntimeManager"
decl_stmt|;
name|when
argument_list|(
name|runtimeManager
operator|.
name|getIdentifier
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|runtimeManagerId
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|producerTemplate
operator|.
name|send
argument_list|(
name|eq
argument_list|(
name|camelRouteUri
argument_list|)
argument_list|,
name|ArgumentMatchers
operator|.
name|any
argument_list|(
name|Exchange
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|outExchange
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|producerTemplate
operator|.
name|getCamelContext
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|camelContext
operator|.
name|createProducerTemplate
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|producerTemplate
argument_list|)
expr_stmt|;
name|HeadersMapFactory
name|hmf
init|=
operator|new
name|DefaultHeadersMapFactory
argument_list|()
decl_stmt|;
name|when
argument_list|(
name|camelContext
operator|.
name|getHeadersMapFactory
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|hmf
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|outExchange
operator|.
name|getOut
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|outMessage
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|outMessage
operator|.
name|getBody
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|testReponse
argument_list|)
expr_stmt|;
name|WorkItemImpl
name|workItem
init|=
operator|new
name|WorkItemImpl
argument_list|()
decl_stmt|;
name|workItem
operator|.
name|setParameter
argument_list|(
name|JBPMConstants
operator|.
name|CAMEL_ENDPOINT_ID_WI_PARAM
argument_list|,
name|camelEndpointId
argument_list|)
expr_stmt|;
name|workItem
operator|.
name|setParameter
argument_list|(
literal|"Request"
argument_list|,
literal|"someRequest"
argument_list|)
expr_stmt|;
name|workItem
operator|.
name|setDeploymentId
argument_list|(
literal|"testDeploymentId"
argument_list|)
expr_stmt|;
name|workItem
operator|.
name|setProcessInstanceId
argument_list|(
literal|1L
argument_list|)
expr_stmt|;
name|workItem
operator|.
name|setId
argument_list|(
literal|1L
argument_list|)
expr_stmt|;
name|AbstractCamelWorkItemHandler
name|handler
init|=
operator|new
name|InOutCamelWorkItemHandler
argument_list|(
name|runtimeManager
argument_list|)
decl_stmt|;
comment|// Register the context after we've created the WIH to test lazy-init.
try|try
block|{
name|ServiceRegistry
operator|.
name|get
argument_list|()
operator|.
name|register
argument_list|(
name|runtimeManagerId
operator|+
literal|"_CamelService"
argument_list|,
name|camelContext
argument_list|)
expr_stmt|;
name|TestWorkItemManager
name|manager
init|=
operator|new
name|TestWorkItemManager
argument_list|()
decl_stmt|;
name|handler
operator|.
name|executeWorkItem
argument_list|(
name|workItem
argument_list|,
name|manager
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|manager
operator|.
name|getResults
argument_list|()
argument_list|,
name|is
argument_list|(
name|notNullValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|manager
operator|.
name|getResults
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|manager
operator|.
name|getResults
argument_list|()
operator|.
name|containsKey
argument_list|(
name|workItem
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|,
name|is
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|results
init|=
name|manager
operator|.
name|getResults
argument_list|(
name|workItem
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|results
operator|.
name|size
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|results
operator|.
name|get
argument_list|(
name|JBPMConstants
operator|.
name|RESPONSE_WI_PARAM
argument_list|)
argument_list|,
name|equalTo
argument_list|(
name|testReponse
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|ServiceRegistry
operator|.
name|get
argument_list|()
operator|.
name|remove
argument_list|(
name|runtimeManagerId
operator|+
literal|"_CamelService"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|IllegalArgumentException
operator|.
name|class
argument_list|)
DECL|method|testExecuteInOutLocalCamelContextLazyInitFail ()
specifier|public
name|void
name|testExecuteInOutLocalCamelContextLazyInitFail
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|camelEndpointId
init|=
literal|"testCamelRoute"
decl_stmt|;
name|String
name|camelRouteUri
init|=
literal|"direct:"
operator|+
name|camelEndpointId
decl_stmt|;
name|String
name|testReponse
init|=
literal|"testResponse"
decl_stmt|;
name|String
name|runtimeManagerId
init|=
literal|"testRuntimeManager"
decl_stmt|;
name|when
argument_list|(
name|runtimeManager
operator|.
name|getIdentifier
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|runtimeManagerId
argument_list|)
expr_stmt|;
name|WorkItemImpl
name|workItem
init|=
operator|new
name|WorkItemImpl
argument_list|()
decl_stmt|;
name|workItem
operator|.
name|setParameter
argument_list|(
name|JBPMConstants
operator|.
name|CAMEL_ENDPOINT_ID_WI_PARAM
argument_list|,
name|camelEndpointId
argument_list|)
expr_stmt|;
name|workItem
operator|.
name|setParameter
argument_list|(
literal|"Request"
argument_list|,
literal|"someRequest"
argument_list|)
expr_stmt|;
name|workItem
operator|.
name|setDeploymentId
argument_list|(
literal|"testDeploymentId"
argument_list|)
expr_stmt|;
name|workItem
operator|.
name|setProcessInstanceId
argument_list|(
literal|1L
argument_list|)
expr_stmt|;
name|workItem
operator|.
name|setId
argument_list|(
literal|1L
argument_list|)
expr_stmt|;
name|AbstractCamelWorkItemHandler
name|handler
init|=
operator|new
name|InOutCamelWorkItemHandler
argument_list|(
name|runtimeManager
argument_list|)
decl_stmt|;
name|TestWorkItemManager
name|manager
init|=
operator|new
name|TestWorkItemManager
argument_list|()
decl_stmt|;
comment|// This is expected to throw an exception.
name|handler
operator|.
name|executeWorkItem
argument_list|(
name|workItem
argument_list|,
name|manager
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testExecuteInOutLocalCamelContextDefaultHandleException ()
specifier|public
name|void
name|testExecuteInOutLocalCamelContextDefaultHandleException
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|camelEndpointId
init|=
literal|"testCamelRoute"
decl_stmt|;
name|String
name|camelRouteUri
init|=
literal|"direct:"
operator|+
name|camelEndpointId
decl_stmt|;
name|String
name|testReponse
init|=
literal|"testResponse"
decl_stmt|;
name|String
name|runtimeManagerId
init|=
literal|"testRuntimeManager"
decl_stmt|;
name|when
argument_list|(
name|runtimeManager
operator|.
name|getIdentifier
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|runtimeManagerId
argument_list|)
expr_stmt|;
comment|//Throw an error back to the WIH
name|when
argument_list|(
name|producerTemplate
operator|.
name|send
argument_list|(
name|eq
argument_list|(
name|camelRouteUri
argument_list|)
argument_list|,
name|ArgumentMatchers
operator|.
name|any
argument_list|(
name|Exchange
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|thenThrow
argument_list|(
operator|new
name|ToBeHandledException
argument_list|()
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|producerTemplate
operator|.
name|getCamelContext
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|camelContext
operator|.
name|createProducerTemplate
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|producerTemplate
argument_list|)
expr_stmt|;
name|HeadersMapFactory
name|hmf
init|=
operator|new
name|DefaultHeadersMapFactory
argument_list|()
decl_stmt|;
name|when
argument_list|(
name|camelContext
operator|.
name|getHeadersMapFactory
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|hmf
argument_list|)
expr_stmt|;
comment|// Register the RuntimeManager bound camelcontext.
try|try
block|{
name|ServiceRegistry
operator|.
name|get
argument_list|()
operator|.
name|register
argument_list|(
name|runtimeManagerId
operator|+
literal|"_CamelService"
argument_list|,
name|camelContext
argument_list|)
expr_stmt|;
name|WorkItemImpl
name|workItem
init|=
operator|new
name|WorkItemImpl
argument_list|()
decl_stmt|;
name|workItem
operator|.
name|setParameter
argument_list|(
name|JBPMConstants
operator|.
name|CAMEL_ENDPOINT_ID_WI_PARAM
argument_list|,
name|camelEndpointId
argument_list|)
expr_stmt|;
name|workItem
operator|.
name|setParameter
argument_list|(
literal|"Request"
argument_list|,
literal|"someRequest"
argument_list|)
expr_stmt|;
name|workItem
operator|.
name|setDeploymentId
argument_list|(
literal|"testDeploymentId"
argument_list|)
expr_stmt|;
name|workItem
operator|.
name|setProcessInstanceId
argument_list|(
literal|1L
argument_list|)
expr_stmt|;
name|workItem
operator|.
name|setId
argument_list|(
literal|1L
argument_list|)
expr_stmt|;
name|AbstractCamelWorkItemHandler
name|handler
init|=
operator|new
name|InOutCamelWorkItemHandler
argument_list|(
name|runtimeManager
argument_list|)
decl_stmt|;
name|TestWorkItemManager
name|manager
init|=
operator|new
name|TestWorkItemManager
argument_list|()
decl_stmt|;
try|try
block|{
name|handler
operator|.
name|executeWorkItem
argument_list|(
name|workItem
argument_list|,
name|manager
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"The test expects an exception. This code should never be reached."
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|wihRe
parameter_list|)
block|{
name|assertThat
argument_list|(
name|wihRe
argument_list|,
name|is
argument_list|(
name|instanceOf
argument_list|(
name|WorkItemHandlerRuntimeException
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|wihRe
operator|.
name|getCause
argument_list|()
argument_list|,
name|is
argument_list|(
name|instanceOf
argument_list|(
name|ToBeHandledException
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|ServiceRegistry
operator|.
name|get
argument_list|()
operator|.
name|remove
argument_list|(
name|runtimeManagerId
operator|+
literal|"_CamelService"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testExecuteInOutLocalCamelContextExplicitHandleException ()
specifier|public
name|void
name|testExecuteInOutLocalCamelContextExplicitHandleException
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|camelEndpointId
init|=
literal|"testCamelRoute"
decl_stmt|;
name|String
name|camelRouteUri
init|=
literal|"direct:"
operator|+
name|camelEndpointId
decl_stmt|;
name|String
name|testReponse
init|=
literal|"testResponse"
decl_stmt|;
name|String
name|runtimeManagerId
init|=
literal|"testRuntimeManager"
decl_stmt|;
name|when
argument_list|(
name|runtimeManager
operator|.
name|getIdentifier
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|runtimeManagerId
argument_list|)
expr_stmt|;
comment|//Throw an error back to the WIH
name|when
argument_list|(
name|producerTemplate
operator|.
name|send
argument_list|(
name|eq
argument_list|(
name|camelRouteUri
argument_list|)
argument_list|,
name|ArgumentMatchers
operator|.
name|any
argument_list|(
name|Exchange
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|thenThrow
argument_list|(
operator|new
name|ToBeHandledException
argument_list|()
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|producerTemplate
operator|.
name|getCamelContext
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|camelContext
operator|.
name|createProducerTemplate
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|producerTemplate
argument_list|)
expr_stmt|;
name|HeadersMapFactory
name|hmf
init|=
operator|new
name|DefaultHeadersMapFactory
argument_list|()
decl_stmt|;
name|when
argument_list|(
name|camelContext
operator|.
name|getHeadersMapFactory
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|hmf
argument_list|)
expr_stmt|;
comment|// Register the RuntimeManager bound camelcontext.
try|try
block|{
name|ServiceRegistry
operator|.
name|get
argument_list|()
operator|.
name|register
argument_list|(
name|runtimeManagerId
operator|+
literal|"_CamelService"
argument_list|,
name|camelContext
argument_list|)
expr_stmt|;
name|WorkItemImpl
name|workItem
init|=
operator|new
name|WorkItemImpl
argument_list|()
decl_stmt|;
name|workItem
operator|.
name|setParameter
argument_list|(
name|JBPMConstants
operator|.
name|CAMEL_ENDPOINT_ID_WI_PARAM
argument_list|,
name|camelEndpointId
argument_list|)
expr_stmt|;
name|workItem
operator|.
name|setParameter
argument_list|(
literal|"Request"
argument_list|,
literal|"someRequest"
argument_list|)
expr_stmt|;
name|workItem
operator|.
name|setParameter
argument_list|(
literal|"HandleExceptions"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|workItem
operator|.
name|setDeploymentId
argument_list|(
literal|"testDeploymentId"
argument_list|)
expr_stmt|;
name|workItem
operator|.
name|setProcessInstanceId
argument_list|(
literal|1L
argument_list|)
expr_stmt|;
name|workItem
operator|.
name|setId
argument_list|(
literal|1L
argument_list|)
expr_stmt|;
name|AbstractCamelWorkItemHandler
name|handler
init|=
operator|new
name|InOutCamelWorkItemHandler
argument_list|(
name|runtimeManager
argument_list|)
decl_stmt|;
name|TestWorkItemManager
name|manager
init|=
operator|new
name|TestWorkItemManager
argument_list|()
decl_stmt|;
try|try
block|{
name|handler
operator|.
name|executeWorkItem
argument_list|(
name|workItem
argument_list|,
name|manager
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"The test expects an exception. This code should never be reached."
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|wihRe
parameter_list|)
block|{
name|assertThat
argument_list|(
name|wihRe
argument_list|,
name|is
argument_list|(
name|instanceOf
argument_list|(
name|WorkItemHandlerRuntimeException
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|wihRe
operator|.
name|getCause
argument_list|()
argument_list|,
name|is
argument_list|(
name|instanceOf
argument_list|(
name|ToBeHandledException
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|ServiceRegistry
operator|.
name|get
argument_list|()
operator|.
name|remove
argument_list|(
name|runtimeManagerId
operator|+
literal|"_CamelService"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testExecuteInOutLocalCamelContextNotHandleException ()
specifier|public
name|void
name|testExecuteInOutLocalCamelContextNotHandleException
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|camelEndpointId
init|=
literal|"testCamelRoute"
decl_stmt|;
name|String
name|camelRouteUri
init|=
literal|"direct:"
operator|+
name|camelEndpointId
decl_stmt|;
name|String
name|testReponse
init|=
literal|"testResponse"
decl_stmt|;
name|String
name|runtimeManagerId
init|=
literal|"testRuntimeManager"
decl_stmt|;
name|when
argument_list|(
name|runtimeManager
operator|.
name|getIdentifier
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|runtimeManagerId
argument_list|)
expr_stmt|;
comment|//Throw an error back to the WIH
name|when
argument_list|(
name|producerTemplate
operator|.
name|send
argument_list|(
name|eq
argument_list|(
name|camelRouteUri
argument_list|)
argument_list|,
name|ArgumentMatchers
operator|.
name|any
argument_list|(
name|Exchange
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|thenThrow
argument_list|(
operator|new
name|NotToBeHandledException
argument_list|()
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|producerTemplate
operator|.
name|getCamelContext
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|camelContext
operator|.
name|createProducerTemplate
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|producerTemplate
argument_list|)
expr_stmt|;
name|HeadersMapFactory
name|hmf
init|=
operator|new
name|DefaultHeadersMapFactory
argument_list|()
decl_stmt|;
name|when
argument_list|(
name|camelContext
operator|.
name|getHeadersMapFactory
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|hmf
argument_list|)
expr_stmt|;
comment|// Register the RuntimeManager bound camelcontext.
try|try
block|{
name|ServiceRegistry
operator|.
name|get
argument_list|()
operator|.
name|register
argument_list|(
name|runtimeManagerId
operator|+
literal|"_CamelService"
argument_list|,
name|camelContext
argument_list|)
expr_stmt|;
name|WorkItemImpl
name|workItem
init|=
operator|new
name|WorkItemImpl
argument_list|()
decl_stmt|;
name|workItem
operator|.
name|setParameter
argument_list|(
name|JBPMConstants
operator|.
name|CAMEL_ENDPOINT_ID_WI_PARAM
argument_list|,
name|camelEndpointId
argument_list|)
expr_stmt|;
name|workItem
operator|.
name|setParameter
argument_list|(
literal|"Request"
argument_list|,
literal|"someRequest"
argument_list|)
expr_stmt|;
name|workItem
operator|.
name|setParameter
argument_list|(
literal|"HandleExceptions"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|workItem
operator|.
name|setDeploymentId
argument_list|(
literal|"testDeploymentId"
argument_list|)
expr_stmt|;
name|workItem
operator|.
name|setProcessInstanceId
argument_list|(
literal|1L
argument_list|)
expr_stmt|;
name|workItem
operator|.
name|setId
argument_list|(
literal|1L
argument_list|)
expr_stmt|;
name|AbstractCamelWorkItemHandler
name|handler
init|=
operator|new
name|InOutCamelWorkItemHandler
argument_list|(
name|runtimeManager
argument_list|)
decl_stmt|;
name|TestWorkItemManager
name|manager
init|=
operator|new
name|TestWorkItemManager
argument_list|()
decl_stmt|;
try|try
block|{
name|handler
operator|.
name|executeWorkItem
argument_list|(
name|workItem
argument_list|,
name|manager
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"The test expects an exception. This code should never be reached."
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|wihRe
parameter_list|)
block|{
name|assertThat
argument_list|(
name|wihRe
argument_list|,
name|is
argument_list|(
name|instanceOf
argument_list|(
name|NotToBeHandledException
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|ServiceRegistry
operator|.
name|get
argument_list|()
operator|.
name|remove
argument_list|(
name|runtimeManagerId
operator|+
literal|"_CamelService"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|ToBeHandledException
specifier|public
specifier|static
class|class
name|ToBeHandledException
extends|extends
name|RuntimeException
block|{     }
DECL|class|NotToBeHandledException
specifier|public
specifier|static
class|class
name|NotToBeHandledException
extends|extends
name|RuntimeException
block|{     }
block|}
end_class

end_unit

