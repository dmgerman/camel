begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|test
operator|.
name|AvailablePortFinder
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
name|TestSupport
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
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|annotation
operator|.
name|Autowired
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|ContextConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|junit4
operator|.
name|AbstractJUnit4SpringContextTests
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

begin_class
annotation|@
name|ContextConfiguration
DECL|class|CamelGreeterFileDomTest
specifier|public
class|class
name|CamelGreeterFileDomTest
extends|extends
name|AbstractJUnit4SpringContextTests
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
name|CamelGreeterFileDomTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|REQUEST
specifier|private
specifier|static
specifier|final
name|String
name|REQUEST
init|=
literal|"<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
operator|+
literal|"<soap:Body><greetMe xmlns=\"http://apache.org/hello_world_soap_http/types\">"
operator|+
literal|"<requestType>Willem</requestType></greetMe></soap:Body></soap:Envelope>"
decl_stmt|;
DECL|field|endpoint
specifier|private
specifier|static
name|Endpoint
name|endpoint
decl_stmt|;
DECL|field|port
specifier|private
specifier|static
name|int
name|port
init|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
decl_stmt|;
static|static
block|{
comment|//set them as system properties so Spring can use the property placeholder
comment|//things to set them into the URL's in the spring contexts
name|System
operator|.
name|setProperty
argument_list|(
literal|"CamelGreeterFileDomTest.port"
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|port
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Autowired
DECL|field|camelContext
specifier|protected
name|CamelContext
name|camelContext
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
name|Object
name|implementor
init|=
operator|new
name|GreeterImpl
argument_list|()
decl_stmt|;
name|String
name|address
init|=
literal|"http://localhost:"
operator|+
name|port
operator|+
literal|"/SoapContext/SoapPort"
decl_stmt|;
name|endpoint
operator|=
name|Endpoint
operator|.
name|publish
argument_list|(
name|address
argument_list|,
name|implementor
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
DECL|method|testCamelGreeter ()
specifier|public
name|void
name|testCamelGreeter
parameter_list|()
throws|throws
name|Exception
block|{
name|TestSupport
operator|.
name|deleteDirectory
argument_list|(
literal|"target/greeter/response"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
name|ProducerTemplate
name|template
init|=
name|camelContext
operator|.
name|createProducerTemplate
argument_list|()
decl_stmt|;
name|Object
name|result
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:start"
argument_list|,
name|REQUEST
argument_list|)
decl_stmt|;
name|template
operator|.
name|stop
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The result is wrong."
argument_list|,
literal|"Hello Willem"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
literal|"target/greeter/response/response.txt"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"File "
operator|+
name|file
operator|+
literal|" should be there."
argument_list|,
name|file
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

