begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.widget
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|widget
package|;
end_package

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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|activemq
operator|.
name|ActiveMQComponent
import|;
end_import

begin_comment
comment|/**  * To configure the widget-gadget application  */
end_comment

begin_class
DECL|class|WidgetApplication
specifier|public
specifier|final
class|class
name|WidgetApplication
block|{
DECL|method|WidgetApplication ()
specifier|private
name|WidgetApplication
parameter_list|()
block|{
comment|// to comply with checkstyle
block|}
comment|/**      * Factory to create the {@link ActiveMQComponent} which is used in this application      * to connect to the remote ActiveMQ broker.      */
annotation|@
name|Produces
DECL|method|createActiveMQComponent ()
specifier|public
specifier|static
name|ActiveMQComponent
name|createActiveMQComponent
parameter_list|()
block|{
comment|// you can set other options but this is the basic just needed
name|ActiveMQComponent
name|amq
init|=
operator|new
name|ActiveMQComponent
argument_list|()
decl_stmt|;
comment|// The ActiveMQ Broker allows anonymous connection by default
comment|// amq.setUserName("admin");
comment|// amq.setPassword("admin");
comment|// the url to the remote ActiveMQ broker
name|amq
operator|.
name|setBrokerURL
argument_list|(
literal|"tcp://localhost:61616"
argument_list|)
expr_stmt|;
return|return
name|amq
return|;
block|}
block|}
end_class

end_unit

