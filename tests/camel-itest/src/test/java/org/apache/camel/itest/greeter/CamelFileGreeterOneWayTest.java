begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.greeter
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|greeter
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
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
name|test
operator|.
name|junit4
operator|.
name|CamelSpringTestSupport
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

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|AfterClass
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|BeforeClass
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
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

begin_class
DECL|class|CamelFileGreeterOneWayTest
specifier|public
class|class
name|CamelFileGreeterOneWayTest
extends|extends
name|CamelSpringTestSupport
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|CamelGreeterTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|endpoint
specifier|private
specifier|static
name|Endpoint
name|endpoint
decl_stmt|;
DECL|field|greeterImpl
specifier|private
specifier|static
name|GreeterImpl
name|greeterImpl
decl_stmt|;
annotation|@
name|BeforeClass
DECL|method|startServer ()
specifier|public
specifier|static
name|void
name|startServer
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Start the Greeter Server
name|greeterImpl
operator|=
operator|new
name|GreeterImpl
argument_list|()
expr_stmt|;
name|String
name|address
init|=
literal|"http://localhost:9000/SoapContext/SoapPort"
decl_stmt|;
name|endpoint
operator|=
name|Endpoint
operator|.
name|publish
argument_list|(
name|address
argument_list|,
name|greeterImpl
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"The WS endpoint is published! "
argument_list|)
expr_stmt|;
block|}
annotation|@
name|AfterClass
DECL|method|stopServer ()
specifier|public
specifier|static
name|void
name|stopServer
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Shutdown the Greeter Server
if|if
condition|(
name|endpoint
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|stop
argument_list|()
expr_stmt|;
name|endpoint
operator|=
literal|null
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testFileWithOnewayOperation ()
specifier|public
name|void
name|testFileWithOnewayOperation
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteDirectory
argument_list|(
literal|"target/messages/input/"
argument_list|)
expr_stmt|;
name|greeterImpl
operator|.
name|resetOneWayCounter
argument_list|()
expr_stmt|;
name|ProducerTemplate
name|template
init|=
name|context
operator|.
name|createProducerTemplate
argument_list|()
decl_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file://target/messages/input/"
argument_list|,
literal|"Hello World"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"hello.txt"
argument_list|)
expr_stmt|;
comment|// Sleep a while and wait for the message whole processing
name|Thread
operator|.
name|sleep
argument_list|(
literal|4000
argument_list|)
expr_stmt|;
name|template
operator|.
name|stop
argument_list|()
expr_stmt|;
comment|// make sure the greeter is called
name|assertEquals
argument_list|(
literal|"The oneway operation of greeter should be called"
argument_list|,
literal|1
argument_list|,
name|greeterImpl
operator|.
name|getOneWayCounter
argument_list|()
argument_list|)
expr_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
literal|"target/messages/input/hello.txt"
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
literal|"File "
operator|+
name|file
operator|+
literal|" should be deleted"
argument_list|,
name|file
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createApplicationContext ()
specifier|protected
name|AbstractApplicationContext
name|createApplicationContext
parameter_list|()
block|{
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/itest/greeter/CamelFileGreeterOneWayTest.xml"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

