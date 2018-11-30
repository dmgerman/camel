begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Produce
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
name|kie
operator|.
name|api
operator|.
name|runtime
operator|.
name|process
operator|.
name|WorkItemHandler
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

begin_comment
comment|//http://camel.apache.org/using-getin-or-getout-methods-on-exchange.html
end_comment

begin_comment
comment|//http://camel.apache.org/async.html
end_comment

begin_class
DECL|class|CamelWorkItemHandlerIntegrationTests
specifier|public
class|class
name|CamelWorkItemHandlerIntegrationTests
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:result"
argument_list|)
DECL|field|resultEndpoint
specifier|protected
name|MockEndpoint
name|resultEndpoint
decl_stmt|;
annotation|@
name|Produce
argument_list|(
name|uri
operator|=
literal|"direct:start"
argument_list|)
DECL|field|template
specifier|protected
name|ProducerTemplate
name|template
decl_stmt|;
annotation|@
name|Test
DECL|method|testSyncInOnly ()
specifier|public
name|void
name|testSyncInOnly
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Setup
name|String
name|routeId
init|=
literal|"testSyncInOnlyExceptionRoute"
decl_stmt|;
name|RouteBuilder
name|builder
init|=
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
literal|"direct:start"
argument_list|)
operator|.
name|routeId
argument_list|(
name|routeId
argument_list|)
operator|.
name|setBody
argument_list|(
name|simple
argument_list|(
literal|"${body.getParameter(\"Request\")}"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
name|builder
argument_list|)
expr_stmt|;
try|try
block|{
comment|// Register the Camel Context with the jBPM ServiceRegistry.
name|ServiceRegistry
operator|.
name|get
argument_list|()
operator|.
name|register
argument_list|(
name|JBPMConstants
operator|.
name|GLOBAL_CAMEL_CONTEXT_SERVICE_KEY
argument_list|,
name|context
argument_list|)
expr_stmt|;
comment|// Test
name|String
name|expectedBody
init|=
literal|"helloRequest"
decl_stmt|;
name|resultEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
name|expectedBody
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
literal|"start"
argument_list|)
expr_stmt|;
name|workItem
operator|.
name|setParameter
argument_list|(
literal|"Request"
argument_list|,
name|expectedBody
argument_list|)
expr_stmt|;
name|TestWorkItemManager
name|manager
init|=
operator|new
name|TestWorkItemManager
argument_list|()
decl_stmt|;
name|WorkItemHandler
name|handler
init|=
operator|new
name|InOnlyCamelWorkItemHandler
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
comment|// Assertions
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
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
comment|// Cleanup
name|context
operator|.
name|removeRoute
argument_list|(
name|routeId
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|WorkItemHandlerRuntimeException
operator|.
name|class
argument_list|)
DECL|method|testSyncInOnlyException ()
specifier|public
name|void
name|testSyncInOnlyException
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Setup
name|String
name|routeId
init|=
literal|"testSyncInOnlyExceptionRoute"
decl_stmt|;
name|RouteBuilder
name|builder
init|=
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
literal|"direct:start"
argument_list|)
operator|.
name|routeId
argument_list|(
name|routeId
argument_list|)
operator|.
name|setBody
argument_list|(
name|simple
argument_list|(
literal|"${body.getParameter(\"Request\")}"
argument_list|)
argument_list|)
operator|.
name|throwException
argument_list|(
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Illegal contennt!"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
name|builder
argument_list|)
expr_stmt|;
try|try
block|{
comment|// Register the Camel Context with the jBPM ServiceRegistry.
name|ServiceRegistry
operator|.
name|get
argument_list|()
operator|.
name|register
argument_list|(
name|JBPMConstants
operator|.
name|GLOBAL_CAMEL_CONTEXT_SERVICE_KEY
argument_list|,
name|context
argument_list|)
expr_stmt|;
comment|// Test
name|String
name|expectedBody
init|=
literal|"helloRequest"
decl_stmt|;
name|resultEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
name|expectedBody
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
literal|"start"
argument_list|)
expr_stmt|;
name|workItem
operator|.
name|setParameter
argument_list|(
literal|"Request"
argument_list|,
name|expectedBody
argument_list|)
expr_stmt|;
name|TestWorkItemManager
name|manager
init|=
operator|new
name|TestWorkItemManager
argument_list|()
decl_stmt|;
name|WorkItemHandler
name|handler
init|=
operator|new
name|InOnlyCamelWorkItemHandler
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
comment|// Assertions
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
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
comment|// Cleanup
name|context
operator|.
name|removeRoute
argument_list|(
name|routeId
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testSyncInOut ()
specifier|public
name|void
name|testSyncInOut
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Setup
name|String
name|routeId
init|=
literal|"testSyncInOnlyExceptionRoute"
decl_stmt|;
name|RouteBuilder
name|builder
init|=
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
literal|"direct:start"
argument_list|)
operator|.
name|routeId
argument_list|(
name|routeId
argument_list|)
operator|.
name|setBody
argument_list|(
name|simple
argument_list|(
literal|"${body.getParameter(\"Request\")}"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
name|builder
argument_list|)
expr_stmt|;
try|try
block|{
comment|// Register the Camel Context with the jBPM ServiceRegistry.
name|ServiceRegistry
operator|.
name|get
argument_list|()
operator|.
name|register
argument_list|(
name|JBPMConstants
operator|.
name|GLOBAL_CAMEL_CONTEXT_SERVICE_KEY
argument_list|,
name|context
argument_list|)
expr_stmt|;
comment|// Test
name|String
name|expectedBody
init|=
literal|"helloRequest"
decl_stmt|;
name|resultEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
name|expectedBody
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
literal|"start"
argument_list|)
expr_stmt|;
name|workItem
operator|.
name|setParameter
argument_list|(
literal|"Request"
argument_list|,
name|expectedBody
argument_list|)
expr_stmt|;
name|TestWorkItemManager
name|manager
init|=
operator|new
name|TestWorkItemManager
argument_list|()
decl_stmt|;
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
comment|// Assertions
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
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
comment|// Cleanup
name|context
operator|.
name|removeRoute
argument_list|(
name|routeId
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|WorkItemHandlerRuntimeException
operator|.
name|class
argument_list|)
DECL|method|testSyncInOutException ()
specifier|public
name|void
name|testSyncInOutException
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Setup
name|String
name|routeId
init|=
literal|"testSyncInOutExceptionRoute"
decl_stmt|;
name|RouteBuilder
name|builder
init|=
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
literal|"direct:start"
argument_list|)
operator|.
name|routeId
argument_list|(
name|routeId
argument_list|)
operator|.
name|setBody
argument_list|(
name|simple
argument_list|(
literal|"${body.getParameter(\"Request\")}"
argument_list|)
argument_list|)
operator|.
name|throwException
argument_list|(
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Illegal contennt!"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
name|builder
argument_list|)
expr_stmt|;
try|try
block|{
comment|// Register the Camel Context with the jBPM ServiceRegistry.
name|ServiceRegistry
operator|.
name|get
argument_list|()
operator|.
name|register
argument_list|(
name|JBPMConstants
operator|.
name|GLOBAL_CAMEL_CONTEXT_SERVICE_KEY
argument_list|,
name|context
argument_list|)
expr_stmt|;
comment|// Test
name|String
name|expectedBody
init|=
literal|"helloRequest"
decl_stmt|;
name|resultEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
name|expectedBody
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
literal|"start"
argument_list|)
expr_stmt|;
name|workItem
operator|.
name|setParameter
argument_list|(
literal|"Request"
argument_list|,
name|expectedBody
argument_list|)
expr_stmt|;
name|TestWorkItemManager
name|manager
init|=
operator|new
name|TestWorkItemManager
argument_list|()
decl_stmt|;
name|WorkItemHandler
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
block|}
finally|finally
block|{
comment|// Cleanup
name|context
operator|.
name|removeRoute
argument_list|(
name|routeId
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

