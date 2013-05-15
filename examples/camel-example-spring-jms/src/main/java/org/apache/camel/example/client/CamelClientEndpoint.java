begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.client
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|client
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
name|Exchange
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
name|ExchangePattern
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
name|Producer
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
name|util
operator|.
name|IOHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|AbstractApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|ClassPathXmlApplicationContext
import|;
end_import

begin_comment
comment|/**  * Client that uses the<a href="http://camel.apache.org/message-endpoint.html">Mesage Endpoint</a>  * pattern to easily exchange messages with the Server.  *<p/>  * Notice this very same API can use for all components in Camel, so if we were using TCP communication instead  * of JMS messaging we could just use<code>camel.getEndpoint("mina:tcp://someserver:port")</code>.  *<p/>  * Requires that the JMS broker is running, as well as CamelServer  */
end_comment

begin_class
DECL|class|CamelClientEndpoint
specifier|public
specifier|final
class|class
name|CamelClientEndpoint
block|{
DECL|method|CamelClientEndpoint ()
specifier|private
name|CamelClientEndpoint
parameter_list|()
block|{
comment|//Helper class
block|}
comment|// START SNIPPET: e1
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
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Notice this client requires that the CamelServer is already running!"
argument_list|)
expr_stmt|;
name|AbstractApplicationContext
name|context
init|=
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"camel-client.xml"
argument_list|)
decl_stmt|;
name|CamelContext
name|camel
init|=
name|context
operator|.
name|getBean
argument_list|(
literal|"camel-client"
argument_list|,
name|CamelContext
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// get the endpoint from the camel context
name|Endpoint
name|endpoint
init|=
name|camel
operator|.
name|getEndpoint
argument_list|(
literal|"jms:queue:numbers"
argument_list|)
decl_stmt|;
comment|// create the exchange used for the communication
comment|// we use the in out pattern for a synchronized exchange where we expect a response
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|)
decl_stmt|;
comment|// set the input on the in body
comment|// must be correct type to match the expected type of an Integer object
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|11
argument_list|)
expr_stmt|;
comment|// to send the exchange we need an producer to do it for us
name|Producer
name|producer
init|=
name|endpoint
operator|.
name|createProducer
argument_list|()
decl_stmt|;
comment|// start the producer so it can operate
name|producer
operator|.
name|start
argument_list|()
expr_stmt|;
comment|// let the producer process the exchange where it does all the work in this oneline of code
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Invoking the multiply with 11"
argument_list|)
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
comment|// get the response from the out body and cast it to an integer
name|int
name|response
init|=
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|(
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"... the result is: "
operator|+
name|response
argument_list|)
expr_stmt|;
comment|// stopping the JMS producer has the side effect of the "ReplyTo Queue" being properly
comment|// closed, making this client not to try any further reads for the replies from the server
name|producer
operator|.
name|stop
argument_list|()
expr_stmt|;
comment|// we're done so let's properly close the application context
name|IOHelper
operator|.
name|close
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
comment|// END SNIPPET: e1
block|}
end_class

end_unit

