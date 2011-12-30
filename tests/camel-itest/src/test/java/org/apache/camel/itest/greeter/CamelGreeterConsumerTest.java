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
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|CamelExecutionException
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
name|component
operator|.
name|cxf
operator|.
name|common
operator|.
name|message
operator|.
name|CxfConstants
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
name|hello_world_soap_http
operator|.
name|PingMeFault
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|fail
import|;
end_import

begin_class
annotation|@
name|ContextConfiguration
DECL|class|CamelGreeterConsumerTest
specifier|public
class|class
name|CamelGreeterConsumerTest
extends|extends
name|AbstractJUnit4SpringContextTests
block|{
DECL|field|port
specifier|private
specifier|static
name|int
name|port
init|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|(
literal|20001
argument_list|)
decl_stmt|;
static|static
block|{
comment|//set them as system properties so Spring can use the property placeholder
comment|//things to set them into the URL's in the spring contexts
name|System
operator|.
name|setProperty
argument_list|(
literal|"CamelGreeterConsumerTest.port"
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
name|Test
DECL|method|testInvokeServers ()
specifier|public
name|void
name|testInvokeServers
parameter_list|()
throws|throws
name|Exception
block|{
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
name|List
argument_list|<
name|String
argument_list|>
name|params
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|params
operator|.
name|add
argument_list|(
literal|"Willem"
argument_list|)
expr_stmt|;
name|Object
name|result
init|=
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"cxf://bean:serviceEndpoint"
argument_list|,
name|ExchangePattern
operator|.
name|InOut
argument_list|,
name|params
argument_list|,
name|CxfConstants
operator|.
name|OPERATION_NAME
argument_list|,
literal|"greetMe"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Result is a list instance "
argument_list|,
name|result
operator|instanceof
name|List
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get the wrong response"
argument_list|,
operator|(
operator|(
name|List
operator|)
name|result
operator|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
literal|"HelloWillem"
argument_list|)
expr_stmt|;
try|try
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"cxf://bean:serviceEndpoint"
argument_list|,
name|ExchangePattern
operator|.
name|InOut
argument_list|,
name|params
argument_list|,
name|CxfConstants
operator|.
name|OPERATION_NAME
argument_list|,
literal|"pingMe"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expect exception here."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|assertTrue
argument_list|(
literal|"Get a wrong exception."
argument_list|,
name|ex
operator|instanceof
name|CamelExecutionException
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Get a wrong exception cause. "
argument_list|,
name|ex
operator|.
name|getCause
argument_list|()
operator|instanceof
name|PingMeFault
argument_list|)
expr_stmt|;
block|}
name|template
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

