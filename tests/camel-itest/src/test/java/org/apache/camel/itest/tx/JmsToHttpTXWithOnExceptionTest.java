begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.tx
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|tx
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
name|EndpointInject
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
name|ExchangeTimedOutException
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
name|RuntimeCamelException
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
name|mock
operator|.
name|MockEndpoint
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

begin_comment
comment|/**  * Unit test will look for the spring .xml file with the same class name  * but postfixed with -config.xml as filename.  *<p/>  * We use Spring Testing for unit test, eg we extend AbstractJUnit4SpringContextTests  * that is a Spring class.  *  * @version   */
end_comment

begin_class
annotation|@
name|ContextConfiguration
DECL|class|JmsToHttpTXWithOnExceptionTest
specifier|public
class|class
name|JmsToHttpTXWithOnExceptionTest
extends|extends
name|AbstractJUnit4SpringContextTests
block|{
annotation|@
name|Autowired
DECL|field|template
specifier|private
name|ProducerTemplate
name|template
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|ref
operator|=
literal|"data"
argument_list|)
DECL|field|data
specifier|private
name|Endpoint
name|data
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:rollback"
argument_list|)
DECL|field|rollback
specifier|private
name|MockEndpoint
name|rollback
decl_stmt|;
comment|// the ok response to expect
DECL|field|ok
specifier|private
name|String
name|ok
init|=
literal|"<?xml version=\"1.0\"?><reply><status>ok</status></reply>"
decl_stmt|;
DECL|field|noAccess
specifier|private
name|String
name|noAccess
init|=
literal|"<?xml version=\"1.0\"?><reply><status>Access denied</status></reply>"
decl_stmt|;
annotation|@
name|Test
DECL|method|test404 ()
specifier|public
name|void
name|test404
parameter_list|()
throws|throws
name|Exception
block|{
comment|// use requestBody to force a InOut message exchange pattern ( = request/reply)
comment|// will send and wait for a response
name|Object
name|out
init|=
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
name|data
argument_list|,
literal|"<?xml version=\"1.0\"?><request><status id=\"123\"/></request>"
argument_list|,
literal|"user"
argument_list|,
literal|"unknown"
argument_list|)
decl_stmt|;
comment|// compare response
name|assertEquals
argument_list|(
name|noAccess
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRollback ()
specifier|public
name|void
name|testRollback
parameter_list|()
throws|throws
name|Exception
block|{
comment|// will rollback forever so we run 3 times or more
name|rollback
operator|.
name|expectedMinimumMessageCount
argument_list|(
literal|3
argument_list|)
expr_stmt|;
comment|// use requestBody to force a InOut message exchange pattern ( = request/reply)
comment|// will send and wait for a response
try|try
block|{
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
name|data
argument_list|,
literal|"<?xml version=\"1.0\"?><request><status id=\"123\"/></request>"
argument_list|,
literal|"user"
argument_list|,
literal|"guest"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should throw an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeCamelException
name|e
parameter_list|)
block|{
name|assertTrue
argument_list|(
literal|"Should timeout"
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
operator|instanceof
name|ExchangeTimedOutException
argument_list|)
expr_stmt|;
block|}
name|rollback
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testOK ()
specifier|public
name|void
name|testOK
parameter_list|()
throws|throws
name|Exception
block|{
comment|// use requestBody to force a InOut message exchange pattern ( = request/reply)
comment|// will send and wait for a response
name|Object
name|out
init|=
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
name|data
argument_list|,
literal|"<?xml version=\"1.0\"?><request><status id=\"123\"/></request>"
argument_list|,
literal|"user"
argument_list|,
literal|"Claus"
argument_list|)
decl_stmt|;
comment|// compare response
name|assertEquals
argument_list|(
name|ok
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

