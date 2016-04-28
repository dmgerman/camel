begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.context
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|context
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
name|Message
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
name|Produce
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

begin_comment
comment|/**  * Test defining a context component using the Spring XML DSL  */
end_comment

begin_class
annotation|@
name|ContextConfiguration
DECL|class|SpringDslContextComponentTest
specifier|public
class|class
name|SpringDslContextComponentTest
extends|extends
name|AbstractJUnit4SpringContextTests
block|{
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:results"
argument_list|,
name|context
operator|=
literal|"tester"
argument_list|)
DECL|field|resultEndpoint
specifier|private
name|MockEndpoint
name|resultEndpoint
decl_stmt|;
annotation|@
name|Produce
argument_list|(
name|uri
operator|=
literal|"tester:start"
argument_list|,
name|context
operator|=
literal|"tester"
argument_list|)
DECL|field|template
specifier|private
name|ProducerTemplate
name|template
decl_stmt|;
annotation|@
name|Test
DECL|method|testUsingContextComponent ()
specifier|public
name|void
name|testUsingContextComponent
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
name|accounts
init|=
name|applicationContext
operator|.
name|getBean
argument_list|(
literal|"accounts"
argument_list|)
decl_stmt|;
name|logger
operator|.
name|info
argument_list|(
literal|"Found accounts: "
operator|+
name|accounts
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|expectedHeaderReceived
argument_list|(
literal|"received"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"<purchaseOrder>one</purchaseOrder>"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"<purchaseOrder>two</purchaseOrder>"
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Exchange
argument_list|>
name|receivedExchanges
init|=
name|resultEndpoint
operator|.
name|getReceivedExchanges
argument_list|()
decl_stmt|;
for|for
control|(
name|Exchange
name|exchange
range|:
name|receivedExchanges
control|)
block|{
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|logger
operator|.
name|info
argument_list|(
literal|"Received from: "
operator|+
name|exchange
operator|.
name|getFromEndpoint
argument_list|()
operator|+
literal|" headers: "
operator|+
name|in
operator|.
name|getHeaders
argument_list|()
operator|+
literal|" body: "
operator|+
name|in
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

