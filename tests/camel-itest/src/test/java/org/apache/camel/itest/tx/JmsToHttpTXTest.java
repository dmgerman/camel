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

begin_comment
comment|/**  * Unit test will look for the spring .xml file with the same class name  * but postfixed with -config.xml as filename.  *<p/>  * We use Spring Testing for unit test, eg we extend AbstractJUnit4SpringContextTests  * that is a Spring class.  *   * @version   */
end_comment

begin_class
annotation|@
name|ContextConfiguration
DECL|class|JmsToHttpTXTest
specifier|public
class|class
name|JmsToHttpTXTest
extends|extends
name|AbstractJUnit4SpringContextTests
block|{
comment|// use uri to refer to our mock
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:rollback"
argument_list|)
DECL|field|mock
name|MockEndpoint
name|mock
decl_stmt|;
comment|// use the spring id to refer to the endpoint we should send data to
comment|// notice using this id we can setup the actual endpoint in spring XML
comment|// and we can even use spring ${ } property in the spring XML
annotation|@
name|EndpointInject
argument_list|(
name|ref
operator|=
literal|"data"
argument_list|)
DECL|field|template
specifier|private
name|ProducerTemplate
name|template
decl_stmt|;
comment|// the ok response to expect
DECL|field|ok
specifier|private
name|String
name|ok
init|=
literal|"<?xml version=\"1.0\"?><reply><status>ok</status></reply>"
decl_stmt|;
annotation|@
name|Test
DECL|method|testSendToTXJms ()
specifier|public
name|void
name|testSendToTXJms
parameter_list|()
throws|throws
name|Exception
block|{
comment|// we assume 2 rollbacks
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
comment|// use requestBody to force a InOut message exchange pattern ( = request/reply)
comment|// will send and wait for a response
name|Object
name|out
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"<?xml version=\"1.0\"?><request><status id=\"123\"/></request>"
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
comment|// assert the mock is correct
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

