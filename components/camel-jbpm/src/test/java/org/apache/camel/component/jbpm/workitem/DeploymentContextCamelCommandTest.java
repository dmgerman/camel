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
name|executor
operator|.
name|Command
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
name|executor
operator|.
name|CommandContext
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
name|executor
operator|.
name|ExecutionResults
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
DECL|class|DeploymentContextCamelCommandTest
specifier|public
class|class
name|DeploymentContextCamelCommandTest
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
name|Mock
DECL|field|commandContext
name|CommandContext
name|commandContext
decl_stmt|;
annotation|@
name|Test
DECL|method|testExecuteCommandDeploymentCamelContext ()
specifier|public
name|void
name|testExecuteCommandDeploymentCamelContext
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
name|deploymentId
init|=
literal|"testDeployment"
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
name|deploymentId
operator|+
name|JBPMConstants
operator|.
name|DEPLOYMENT_CAMEL_CONTEXT_SERVICE_KEY_POSTFIX
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
name|when
argument_list|(
name|commandContext
operator|.
name|getData
argument_list|(
literal|"workItem"
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|workItem
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|commandContext
operator|.
name|getData
argument_list|(
literal|"deploymentId"
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|deploymentId
argument_list|)
expr_stmt|;
name|Command
name|command
init|=
operator|new
name|DeploymentContextCamelCommand
argument_list|()
decl_stmt|;
name|ExecutionResults
name|results
init|=
name|command
operator|.
name|execute
argument_list|(
name|commandContext
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|results
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|results
operator|.
name|getData
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|testReponse
argument_list|,
name|results
operator|.
name|getData
argument_list|()
operator|.
name|get
argument_list|(
name|JBPMConstants
operator|.
name|RESPONSE_WI_PARAM
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
name|deploymentId
operator|+
name|JBPMConstants
operator|.
name|DEPLOYMENT_CAMEL_CONTEXT_SERVICE_KEY_POSTFIX
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

