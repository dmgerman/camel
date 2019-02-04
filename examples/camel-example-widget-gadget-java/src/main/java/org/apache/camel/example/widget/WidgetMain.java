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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|main
operator|.
name|Main
import|;
end_import

begin_comment
comment|/**  * A plain Java Main to start the widget and gadget example.  */
end_comment

begin_class
DECL|class|WidgetMain
specifier|public
specifier|final
class|class
name|WidgetMain
block|{
comment|// use Camel Main to setup and run Camel
DECL|field|main
specifier|private
specifier|static
name|Main
name|main
init|=
operator|new
name|Main
argument_list|()
decl_stmt|;
DECL|method|WidgetMain ()
specifier|private
name|WidgetMain
parameter_list|()
block|{
comment|// to comply with checkstyle
block|}
DECL|method|main (String[] args)
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|Exception
block|{
comment|// create the ActiveMQ component
name|main
operator|.
name|bind
argument_list|(
literal|"activemq"
argument_list|,
name|createActiveMQComponent
argument_list|()
argument_list|)
expr_stmt|;
comment|// add the widget/gadget route
name|main
operator|.
name|addRouteBuilder
argument_list|(
operator|new
name|WidgetGadgetRoute
argument_list|()
argument_list|)
expr_stmt|;
comment|// add a 2nd route that routes files from src/main/data to the order queue
name|main
operator|.
name|addRouteBuilder
argument_list|(
operator|new
name|CreateOrderRoute
argument_list|()
argument_list|)
expr_stmt|;
comment|// start and run Camel (block)
name|main
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
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

