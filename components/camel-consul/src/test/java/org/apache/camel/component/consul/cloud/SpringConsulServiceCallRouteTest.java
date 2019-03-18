begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.consul.cloud
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|consul
operator|.
name|cloud
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

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
name|com
operator|.
name|orbitz
operator|.
name|consul
operator|.
name|AgentClient
import|;
end_import

begin_import
import|import
name|com
operator|.
name|orbitz
operator|.
name|consul
operator|.
name|Consul
import|;
end_import

begin_import
import|import
name|com
operator|.
name|orbitz
operator|.
name|consul
operator|.
name|model
operator|.
name|agent
operator|.
name|ImmutableRegistration
import|;
end_import

begin_import
import|import
name|com
operator|.
name|orbitz
operator|.
name|consul
operator|.
name|model
operator|.
name|agent
operator|.
name|Registration
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
name|Navigate
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
name|component
operator|.
name|consul
operator|.
name|ConsulTestSupport
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
name|cloud
operator|.
name|DefaultServiceCallProcessor
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
name|ChoiceProcessor
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
name|FilterProcessor
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
name|testcontainers
operator|.
name|spring
operator|.
name|ContainerAwareSpringTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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
name|testcontainers
operator|.
name|containers
operator|.
name|GenericContainer
import|;
end_import

begin_class
DECL|class|SpringConsulServiceCallRouteTest
specifier|public
specifier|abstract
class|class
name|SpringConsulServiceCallRouteTest
extends|extends
name|ContainerAwareSpringTestSupport
block|{
DECL|field|client
specifier|private
name|AgentClient
name|client
decl_stmt|;
DECL|field|registrations
specifier|private
name|List
argument_list|<
name|Registration
argument_list|>
name|registrations
decl_stmt|;
comment|// *************************************************************************
comment|// Setup / tear down
comment|// *************************************************************************
annotation|@
name|Override
DECL|method|doPreSetup ()
specifier|public
name|void
name|doPreSetup
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doPreSetup
argument_list|()
expr_stmt|;
name|this
operator|.
name|client
operator|=
name|Consul
operator|.
name|builder
argument_list|()
operator|.
name|withUrl
argument_list|(
name|consulUrl
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
operator|.
name|agentClient
argument_list|()
expr_stmt|;
name|this
operator|.
name|registrations
operator|=
name|Arrays
operator|.
name|asList
argument_list|(
name|ImmutableRegistration
operator|.
name|builder
argument_list|()
operator|.
name|id
argument_list|(
literal|"service-1-1"
argument_list|)
operator|.
name|name
argument_list|(
literal|"http-service-1"
argument_list|)
operator|.
name|address
argument_list|(
literal|"127.0.0.1"
argument_list|)
operator|.
name|port
argument_list|(
literal|9011
argument_list|)
operator|.
name|build
argument_list|()
argument_list|,
name|ImmutableRegistration
operator|.
name|builder
argument_list|()
operator|.
name|id
argument_list|(
literal|"service-1-2"
argument_list|)
operator|.
name|name
argument_list|(
literal|"http-service-1"
argument_list|)
operator|.
name|address
argument_list|(
literal|"127.0.0.1"
argument_list|)
operator|.
name|port
argument_list|(
literal|9012
argument_list|)
operator|.
name|build
argument_list|()
argument_list|,
name|ImmutableRegistration
operator|.
name|builder
argument_list|()
operator|.
name|id
argument_list|(
literal|"service-1-3"
argument_list|)
operator|.
name|name
argument_list|(
literal|"http-service-1"
argument_list|)
operator|.
name|address
argument_list|(
literal|"127.0.0.1"
argument_list|)
operator|.
name|port
argument_list|(
literal|9013
argument_list|)
operator|.
name|build
argument_list|()
argument_list|,
name|ImmutableRegistration
operator|.
name|builder
argument_list|()
operator|.
name|id
argument_list|(
literal|"service-2-1"
argument_list|)
operator|.
name|name
argument_list|(
literal|"http-service-2"
argument_list|)
operator|.
name|address
argument_list|(
literal|"127.0.0.1"
argument_list|)
operator|.
name|port
argument_list|(
literal|9021
argument_list|)
operator|.
name|build
argument_list|()
argument_list|,
name|ImmutableRegistration
operator|.
name|builder
argument_list|()
operator|.
name|id
argument_list|(
literal|"service-2-2"
argument_list|)
operator|.
name|name
argument_list|(
literal|"http-service-2"
argument_list|)
operator|.
name|address
argument_list|(
literal|"127.0.0.1"
argument_list|)
operator|.
name|port
argument_list|(
literal|9022
argument_list|)
operator|.
name|build
argument_list|()
argument_list|,
name|ImmutableRegistration
operator|.
name|builder
argument_list|()
operator|.
name|id
argument_list|(
literal|"service-2-3"
argument_list|)
operator|.
name|name
argument_list|(
literal|"http-service-2"
argument_list|)
operator|.
name|address
argument_list|(
literal|"127.0.0.1"
argument_list|)
operator|.
name|port
argument_list|(
literal|9023
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|registrations
operator|.
name|forEach
argument_list|(
name|client
operator|::
name|register
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doPostTearDown ()
specifier|public
name|void
name|doPostTearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doPostTearDown
argument_list|()
expr_stmt|;
if|if
condition|(
name|client
operator|!=
literal|null
condition|)
block|{
name|registrations
operator|.
name|forEach
argument_list|(
name|r
lambda|->
name|client
operator|.
name|deregister
argument_list|(
name|r
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|// *************************************************************************
comment|// Test
comment|// *************************************************************************
annotation|@
name|Test
DECL|method|testServiceCall ()
specifier|public
name|void
name|testServiceCall
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:result-1"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result-1"
argument_list|)
operator|.
name|expectedBodiesReceivedInAnyOrder
argument_list|(
literal|"service-1 9012"
argument_list|,
literal|"service-1 9013"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result-2"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result-2"
argument_list|)
operator|.
name|expectedBodiesReceivedInAnyOrder
argument_list|(
literal|"service-2 9021"
argument_list|,
literal|"service-2 9023"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"service-1"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"service-1"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"service-2"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"service-2"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
comment|// ************************************
comment|// Helpers
comment|// ************************************
DECL|method|findServiceCallProcessors ()
specifier|protected
name|List
argument_list|<
name|DefaultServiceCallProcessor
argument_list|>
name|findServiceCallProcessors
parameter_list|()
block|{
name|Route
name|route
init|=
name|context
argument_list|()
operator|.
name|getRoute
argument_list|(
literal|"scall"
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
literal|"ServiceCall Route should be present"
argument_list|,
name|route
argument_list|)
expr_stmt|;
return|return
name|findServiceCallProcessors
argument_list|(
operator|new
name|ArrayList
argument_list|<>
argument_list|()
argument_list|,
name|route
operator|.
name|navigate
argument_list|()
argument_list|)
return|;
block|}
DECL|method|findServiceCallProcessors (List<DefaultServiceCallProcessor> processors, Navigate<Processor> navigate)
specifier|protected
name|List
argument_list|<
name|DefaultServiceCallProcessor
argument_list|>
name|findServiceCallProcessors
parameter_list|(
name|List
argument_list|<
name|DefaultServiceCallProcessor
argument_list|>
name|processors
parameter_list|,
name|Navigate
argument_list|<
name|Processor
argument_list|>
name|navigate
parameter_list|)
block|{
for|for
control|(
name|Processor
name|processor
range|:
name|navigate
operator|.
name|next
argument_list|()
control|)
block|{
if|if
condition|(
name|processor
operator|instanceof
name|DefaultServiceCallProcessor
condition|)
block|{
name|processors
operator|.
name|add
argument_list|(
operator|(
name|DefaultServiceCallProcessor
operator|)
name|processor
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|processor
operator|instanceof
name|ChoiceProcessor
condition|)
block|{
for|for
control|(
name|FilterProcessor
name|filter
range|:
operator|(
operator|(
name|ChoiceProcessor
operator|)
name|processor
operator|)
operator|.
name|getFilters
argument_list|()
control|)
block|{
name|findServiceCallProcessors
argument_list|(
name|processors
argument_list|,
name|filter
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|processor
operator|instanceof
name|Navigate
condition|)
block|{
return|return
name|findServiceCallProcessors
argument_list|(
name|processors
argument_list|,
operator|(
name|Navigate
argument_list|<
name|Processor
argument_list|>
operator|)
name|processor
argument_list|)
return|;
block|}
block|}
return|return
name|processors
return|;
block|}
annotation|@
name|Override
DECL|method|createContainer ()
specifier|protected
name|GenericContainer
argument_list|<
name|?
argument_list|>
name|createContainer
parameter_list|()
block|{
return|return
name|ConsulTestSupport
operator|.
name|consulContainer
argument_list|()
return|;
block|}
DECL|method|consulUrl ()
specifier|protected
name|String
name|consulUrl
parameter_list|()
block|{
return|return
name|String
operator|.
name|format
argument_list|(
literal|"http://%s:%d"
argument_list|,
name|getContainerHost
argument_list|(
name|ConsulTestSupport
operator|.
name|CONTAINER_NAME
argument_list|)
argument_list|,
name|getContainerPort
argument_list|(
name|ConsulTestSupport
operator|.
name|CONTAINER_NAME
argument_list|,
name|Consul
operator|.
name|DEFAULT_HTTP_PORT
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

