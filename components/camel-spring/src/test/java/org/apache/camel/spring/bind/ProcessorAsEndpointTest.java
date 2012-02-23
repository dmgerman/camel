begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.bind
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|bind
package|;
end_package

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
name|NoSuchEndpointException
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
name|spring
operator|.
name|SpringTestSupport
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
name|AbstractXmlApplicationContext
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
comment|/**  * @version   */
end_comment

begin_class
DECL|class|ProcessorAsEndpointTest
specifier|public
class|class
name|ProcessorAsEndpointTest
extends|extends
name|SpringTestSupport
block|{
DECL|field|body
specifier|protected
name|Object
name|body
init|=
literal|"<hello>world!</hello>"
decl_stmt|;
DECL|method|testSendingToProcessorEndpoint ()
specifier|public
name|void
name|testSendingToProcessorEndpoint
parameter_list|()
throws|throws
name|Exception
block|{
name|ProcessorStub
name|processor
init|=
name|getMandatoryBean
argument_list|(
name|ProcessorStub
operator|.
name|class
argument_list|,
literal|"myProcessor"
argument_list|)
decl_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"myProcessor"
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Exchange
argument_list|>
name|list
init|=
name|processor
operator|.
name|getExchanges
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Received exchange list: "
operator|+
name|list
argument_list|,
literal|1
argument_list|,
name|list
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Found exchanges: "
operator|+
name|list
argument_list|)
expr_stmt|;
block|}
DECL|method|testSendingToNonExistentEndpoint ()
specifier|public
name|void
name|testSendingToNonExistentEndpoint
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|uri
init|=
literal|"unknownEndpoint"
decl_stmt|;
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
literal|"Should not have found an endpoint! Was: "
operator|+
name|endpoint
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
try|try
block|{
name|template
operator|.
name|sendBody
argument_list|(
name|uri
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"We should have failed as this is a bad endpoint URI"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchEndpointException
name|e
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Caught expected exception: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|createApplicationContext ()
specifier|protected
name|AbstractXmlApplicationContext
name|createApplicationContext
parameter_list|()
block|{
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/spring/bind/processorAsEndpoint.xml"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

