begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.artemis
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|artemis
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|activemq
operator|.
name|artemis
operator|.
name|jms
operator|.
name|client
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
name|component
operator|.
name|jms
operator|.
name|JmsConfiguration
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
comment|/**  * A plain Java Main to start the widget and gadget example using Apache Artemis.  */
end_comment

begin_class
DECL|class|ArtemisMain
specifier|public
specifier|final
class|class
name|ArtemisMain
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
DECL|method|ArtemisMain ()
specifier|private
name|ArtemisMain
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
comment|// create the ActiveMQ Artemis component
name|main
operator|.
name|bind
argument_list|(
literal|"artemis"
argument_list|,
name|createArtemisComponent
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
comment|// add a 2nd route that routes files from src/data to the order queue
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
DECL|method|createArtemisComponent ()
specifier|private
specifier|static
name|JmsComponent
name|createArtemisComponent
parameter_list|()
block|{
comment|// Sets up the Artemis core protocol connection factory
name|ActiveMQConnectionFactory
name|connectionFactory
init|=
operator|new
name|ActiveMQConnectionFactory
argument_list|(
literal|"tcp://localhost:61616"
argument_list|)
decl_stmt|;
name|JmsConfiguration
name|configuration
init|=
operator|new
name|JmsConfiguration
argument_list|()
decl_stmt|;
name|configuration
operator|.
name|setConnectionFactory
argument_list|(
name|connectionFactory
argument_list|)
expr_stmt|;
return|return
operator|new
name|JmsComponent
argument_list|(
name|configuration
argument_list|)
return|;
block|}
block|}
end_class

end_unit

