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
name|example
operator|.
name|server
operator|.
name|Multiplier
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
comment|/**  * Client that uses Camel Spring Remoting for very easy integration with the server.  *<p/>  * Requires that the JMS broker is running, as well as CamelServer  */
end_comment

begin_class
DECL|class|CamelClientRemoting
specifier|public
specifier|final
class|class
name|CamelClientRemoting
block|{
DECL|method|CamelClientRemoting ()
specifier|private
name|CamelClientRemoting
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
literal|"camel-client-remoting.xml"
argument_list|)
decl_stmt|;
comment|// just get the proxy to the service and we as the client can use the "proxy" as it was
comment|// a local object we are invoking. Camel will under the covers do the remote communication
comment|// to the remote ActiveMQ server and fetch the response.
name|Multiplier
name|multiplier
init|=
name|context
operator|.
name|getBean
argument_list|(
literal|"multiplierProxy"
argument_list|,
name|Multiplier
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
literal|"Invoking the multiply with 33"
argument_list|)
expr_stmt|;
name|int
name|response
init|=
name|multiplier
operator|.
name|multiply
argument_list|(
literal|33
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

