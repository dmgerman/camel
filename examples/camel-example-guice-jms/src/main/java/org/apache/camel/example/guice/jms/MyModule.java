begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.guice.jms
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|guice
operator|.
name|jms
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Provides
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|name
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
name|component
operator|.
name|jms
operator|.
name|JmsComponent
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
name|guice
operator|.
name|CamelModuleWithMatchingRoutes
import|;
end_import

begin_import
import|import
name|org
operator|.
name|guiceyfruit
operator|.
name|jndi
operator|.
name|JndiBind
import|;
end_import

begin_comment
comment|/**  * Configures the CamelContext, RouteBuilder, Component and Endpoint instances using  * Guice  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|MyModule
specifier|public
class|class
name|MyModule
extends|extends
name|CamelModuleWithMatchingRoutes
block|{
annotation|@
name|Override
DECL|method|configure ()
specifier|protected
name|void
name|configure
parameter_list|()
block|{
name|super
operator|.
name|configure
argument_list|()
expr_stmt|;
comment|// lets add in any RouteBuilder instances we want to use
name|bind
argument_list|(
name|MyRouteBuilder
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
comment|/**      * Lets configure the JMS component, parameterizing some properties from the      * jndi.properties file      */
annotation|@
name|Provides
annotation|@
name|JndiBind
argument_list|(
literal|"jms"
argument_list|)
DECL|method|jms (@amedR) String brokerUrl)
name|JmsComponent
name|jms
parameter_list|(
annotation|@
name|Named
argument_list|(
literal|"activemq.brokerURL"
argument_list|)
name|String
name|brokerUrl
parameter_list|)
block|{
return|return
name|JmsComponent
operator|.
name|jmsComponent
argument_list|(
operator|new
name|ActiveMQConnectionFactory
argument_list|(
name|brokerUrl
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

