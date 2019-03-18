begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.cxf.provider
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|cxf
operator|.
name|provider
package|;
end_package

begin_comment
comment|/**  * An example demonstrating routing of messages to a JAXWS WebServiceProvider  * endpoint through a Camel route. The message could be either a SOAP envelope   * or plain XML over HTTP as defined by the JAX-WS specification.  */
end_comment

begin_class
DECL|class|CamelCxfExample
specifier|public
specifier|final
class|class
name|CamelCxfExample
block|{
DECL|method|CamelCxfExample ()
specifier|private
name|CamelCxfExample
parameter_list|()
block|{     }
DECL|method|main (String args[])
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
name|args
index|[]
parameter_list|)
throws|throws
name|Exception
block|{
name|Server
name|server
init|=
operator|new
name|Server
argument_list|()
decl_stmt|;
try|try
block|{
comment|// start the endpoints
name|server
operator|.
name|start
argument_list|()
expr_stmt|;
comment|// set the client's service access point
name|Client
name|client
init|=
operator|new
name|Client
argument_list|(
literal|"http://localhost:9000/GreeterContext/SOAPMessageService"
argument_list|)
decl_stmt|;
comment|// invoke the services
name|String
name|response
init|=
name|client
operator|.
name|invoke
argument_list|()
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|response
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Get the exception "
operator|+
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|server
operator|.
name|stop
argument_list|()
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

