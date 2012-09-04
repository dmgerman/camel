begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.cdi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|cdi
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|PostConstruct
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ejb
operator|.
name|Startup
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|context
operator|.
name|ApplicationScoped
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|Produces
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Named
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
name|cdi
operator|.
name|Uri
import|;
end_import

begin_comment
comment|/**  * Configures all our Camel components, endpoints and beans and create the Camel routes  */
end_comment

begin_class
annotation|@
name|Startup
annotation|@
name|ApplicationScoped
DECL|class|MyRouteConfig
specifier|public
class|class
name|MyRouteConfig
block|{
annotation|@
name|Inject
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
annotation|@
name|Inject
comment|//@Uri("activemq:test.MyQueue")
annotation|@
name|Uri
argument_list|(
literal|"file://target/testdata/queue"
argument_list|)
DECL|field|queueEndpoint
specifier|private
name|Endpoint
name|queueEndpoint
decl_stmt|;
annotation|@
name|Inject
annotation|@
name|Uri
argument_list|(
literal|"file://target/testdata/result?noop=true"
argument_list|)
DECL|field|resultEndpoint
specifier|private
name|Endpoint
name|resultEndpoint
decl_stmt|;
annotation|@
name|Produces
DECL|method|createRoutes ()
specifier|public
name|RouteBuilder
name|createRoutes
parameter_list|()
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
comment|// you can configure the route rule with Java DSL here
comment|// populate the message queue with some messages
name|from
argument_list|(
literal|"file:src/data?noop=true"
argument_list|)
operator|.
name|to
argument_list|(
name|queueEndpoint
argument_list|)
expr_stmt|;
comment|// consume from message queue to a result endpoint and process with a bean
name|from
argument_list|(
name|queueEndpoint
argument_list|)
operator|.
name|to
argument_list|(
name|resultEndpoint
argument_list|)
operator|.
name|bean
argument_list|(
operator|new
name|SomeBean
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
comment|/**      * Configure ActiveMQ endpoints      */
annotation|@
name|Named
argument_list|(
literal|"activemq"
argument_list|)
DECL|method|createActiveMQComponent ()
specifier|public
name|ActiveMQComponent
name|createActiveMQComponent
parameter_list|()
block|{
name|ActiveMQComponent
name|answer
init|=
operator|new
name|ActiveMQComponent
argument_list|()
decl_stmt|;
name|answer
operator|.
name|setBrokerURL
argument_list|(
literal|"vm://localhost.cdi?marshal=false&broker.persistent=false&broker.useJmx=false"
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
comment|/**      * TODO can we avoid this bit and get CDI to automatically create a CamelContext and add its routes?      */
annotation|@
name|PostConstruct
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
throws|throws
name|Exception
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"======= Starting MyRouteConfig!!"
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|addRoutes
argument_list|(
name|createRoutes
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|getResultEndpoint ()
specifier|public
name|Endpoint
name|getResultEndpoint
parameter_list|()
block|{
return|return
name|resultEndpoint
return|;
block|}
block|}
end_class

end_unit

