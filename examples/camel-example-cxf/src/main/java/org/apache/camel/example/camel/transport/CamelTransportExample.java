begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.camel.transport
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|camel
operator|.
name|transport
package|;
end_package

begin_comment
comment|/**  * Main class to run the Camel transport example.  */
end_comment

begin_class
DECL|class|CamelTransportExample
specifier|public
specifier|final
class|class
name|CamelTransportExample
block|{
DECL|method|CamelTransportExample ()
specifier|private
name|CamelTransportExample
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
comment|// setup the Camel context for the Camel transport
name|server
operator|.
name|prepare
argument_list|()
expr_stmt|;
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
literal|"http://localhost:9091/GreeterContext/GreeterPort"
argument_list|)
decl_stmt|;
comment|// invoking the services
name|client
operator|.
name|invoke
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|()
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

