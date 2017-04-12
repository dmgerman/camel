begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.milo.testing
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|milo
operator|.
name|testing
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
name|impl
operator|.
name|DefaultCamelContext
import|;
end_import

begin_class
DECL|class|Application
specifier|public
specifier|final
class|class
name|Application
block|{
DECL|method|Application ()
specifier|private
name|Application
parameter_list|()
block|{     }
DECL|method|main (final String[] args)
specifier|public
specifier|static
name|void
name|main
parameter_list|(
specifier|final
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|Exception
block|{
comment|// camel conext
specifier|final
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
comment|// add paho
comment|// no need to register, gets auto detected
comment|// context.addComponent("paho", new PahoComponent());
comment|// no need to register, gets auto detected
comment|// context.addComponent("milo-server", new MiloClientComponent());
comment|// context.addComponent("milo-client", new MiloClientComponent());
comment|// add routes
name|context
operator|.
name|addRoutes
argument_list|(
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
literal|"paho:javaonedemo/eclipse-greenhouse-9home/sensors/temperature?brokerUrl=tcp://iot.eclipse.org:1883"
argument_list|)
operator|.
name|log
argument_list|(
literal|"Temp update: ${body}"
argument_list|)
operator|.
name|convertBodyTo
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
literal|"milo-server:MyItem"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"milo-server:MyItem"
argument_list|)
operator|.
name|log
argument_list|(
literal|"MyItem: ${body}"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"milo-server:MyItem2"
argument_list|)
operator|.
name|log
argument_list|(
literal|"MyItem2 : ${body}"
argument_list|)
operator|.
name|to
argument_list|(
literal|"paho:de/dentrassi/camel/milo/test1?brokerUrl=tcp://iot.eclipse.org:1883"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"milo-client:tcp://foo:bar@localhost:12685?nodeId=items-MyItem&namespaceUri=urn:camel"
argument_list|)
operator|.
name|log
argument_list|(
literal|"From OPC UA: ${body}"
argument_list|)
operator|.
name|to
argument_list|(
literal|"milo-client:tcp://localhost:12685?nodeId=items-MyItem2&namespaceUri=urn:camel"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"paho:de/dentrassi/camel/milo/test1?brokerUrl=tcp://iot.eclipse.org:1883"
argument_list|)
operator|.
name|log
argument_list|(
literal|"Back from MQTT: ${body}"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
comment|// start
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
comment|// sleep
while|while
condition|(
literal|true
condition|)
block|{
name|Thread
operator|.
name|sleep
argument_list|(
name|Long
operator|.
name|MAX_VALUE
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

