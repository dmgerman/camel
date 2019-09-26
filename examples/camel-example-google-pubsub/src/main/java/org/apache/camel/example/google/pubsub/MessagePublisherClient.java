begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.google.pubsub
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|google
operator|.
name|pubsub
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Calendar
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|google
operator|.
name|pubsub
operator|.
name|GooglePubsubComponent
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

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
DECL|class|MessagePublisherClient
specifier|public
specifier|final
class|class
name|MessagePublisherClient
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|MessagePublisherClient
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|MessagePublisherClient ()
specifier|private
name|MessagePublisherClient
parameter_list|()
block|{     }
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
name|LOG
operator|.
name|info
argument_list|(
literal|"About to run Google-pubsub-camel integration..."
argument_list|)
expr_stmt|;
name|String
name|testPubsubMessage
init|=
literal|"Test Message from  MessagePublisherClient "
operator|+
name|Calendar
operator|.
name|getInstance
argument_list|()
operator|.
name|getTime
argument_list|()
decl_stmt|;
name|CamelContext
name|camelContext
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
comment|// Add route to send messages to Google Pubsub
name|camelContext
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|camelContext
operator|.
name|getPropertiesComponent
argument_list|()
operator|.
name|setLocation
argument_list|(
literal|"classpath:example.properties"
argument_list|)
expr_stmt|;
comment|// setup google pubsub component
name|GooglePubsubComponent
name|googlePubsub
init|=
name|PubsubUtil
operator|.
name|createComponent
argument_list|()
decl_stmt|;
name|camelContext
operator|.
name|addComponent
argument_list|(
literal|"google-pubsub"
argument_list|,
name|googlePubsub
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:googlePubsubStart"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"DirectToGooglePubsub"
argument_list|)
operator|.
name|to
argument_list|(
literal|"google-pubsub:{{pubsub.projectId}}:{{pubsub.topic}}"
argument_list|)
operator|.
name|log
argument_list|(
literal|"${headers}"
argument_list|)
expr_stmt|;
comment|// Takes input from the command line.
name|from
argument_list|(
literal|"stream:in"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:googlePubsubStart"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|ProducerTemplate
name|producerTemplate
init|=
name|camelContext
operator|.
name|createProducerTemplate
argument_list|()
decl_stmt|;
name|camelContext
operator|.
name|start
argument_list|()
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|producerTemplate
operator|.
name|sendBodyAndHeaders
argument_list|(
literal|"direct:googlePubsubStart"
argument_list|,
name|testPubsubMessage
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Successfully published message to Pubsub."
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Enter text on the line below : [Press Ctrl-C to exit.] "
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|5
operator|*
literal|60
operator|*
literal|1000
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

