begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|component
operator|.
name|milo
operator|.
name|server
operator|.
name|MiloServerComponent
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

begin_comment
comment|/**  * This is a simple example application which tests a few ways of mapping data  * to an OPC UA server instance.  */
end_comment

begin_class
DECL|class|ExampleServer
specifier|public
specifier|final
class|class
name|ExampleServer
block|{
DECL|method|ExampleServer ()
specifier|private
name|ExampleServer
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
comment|// configure milo
operator|(
operator|(
name|MiloServerComponent
operator|)
name|context
operator|.
name|getComponent
argument_list|(
literal|"milo-server"
argument_list|)
operator|)
operator|.
name|setUserAuthenticationCredentials
argument_list|(
literal|"foo:bar"
argument_list|)
expr_stmt|;
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
comment|/*                  * Take an MQTT topic and forward its content to an OPC UA                  * server item. You can e.g. take some MQTT application and an                  * OPC UA client, connect with both applications to their                  * topics/items. When you write on the MQTT item it will pop up                  * on the OPC UA item.                  */
name|from
argument_list|(
literal|"paho:my/foo/bar?brokerUrl=tcp://iot.eclipse.org:1883"
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
comment|/*                  * Creating a simple item which has not data but logs anything                  * which gets written to by an OPC UA write call                  */
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
comment|/*                  * Creating an item which takes write command and forwards them                  * to an MQTT topic                  */
name|from
argument_list|(
literal|"milo-server:MyItem2"
argument_list|)
operator|.
name|log
argument_list|(
literal|"MyItem2: ${body}"
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
literal|"paho:de/dentrassi/camel/milo/temperature?brokerUrl=tcp://iot.eclipse.org:1883"
argument_list|)
expr_stmt|;
comment|/*                  * Re-read the output from the previous route from MQTT to the                  * local logging                  */
name|from
argument_list|(
literal|"paho:de/dentrassi/camel/milo/temperature?brokerUrl=tcp://iot.eclipse.org:1883"
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

