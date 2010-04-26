begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|Body
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
name|ContextTestSupport
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
name|Header
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
name|builder
operator|.
name|RouteBuilder
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
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|JndiRegistry
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
name|language
operator|.
name|XPath
import|;
end_import

begin_class
DECL|class|ClaimCheckTest
specifier|public
class|class
name|ClaimCheckTest
extends|extends
name|ContextTestSupport
block|{
comment|// in memory data store for testing only!
DECL|field|dataStore
specifier|public
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|dataStore
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|testClaimCheck ()
specifier|public
name|void
name|testClaimCheck
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|body
init|=
literal|"<order custId=\"123\"><lotsOfContent/></order>"
decl_stmt|;
comment|// check to make sure the message body gets added back in properly
name|MockEndpoint
name|resultEndpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|resultEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|equals
argument_list|(
name|body
argument_list|)
expr_stmt|;
comment|// check to make sure the claim check is added to the message and
comment|// the body is removed
name|MockEndpoint
name|testCheckpointEndpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:testCheckpoint"
argument_list|)
decl_stmt|;
name|testCheckpointEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|testCheckpointEndpoint
operator|.
name|expectedHeaderReceived
argument_list|(
literal|"claimCheck"
argument_list|,
literal|"123"
argument_list|)
expr_stmt|;
name|testCheckpointEndpoint
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|jndi
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|jndi
operator|.
name|bind
argument_list|(
literal|"checkLuggage"
argument_list|,
operator|new
name|CheckLuggageBean
argument_list|()
argument_list|)
expr_stmt|;
name|jndi
operator|.
name|bind
argument_list|(
literal|"dataEnricher"
argument_list|,
operator|new
name|DataEnricherBean
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|jndi
return|;
block|}
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
comment|// START SNIPPET: e1
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"bean:checkLuggage"
argument_list|,
literal|"mock:testCheckpoint"
argument_list|,
literal|"bean:dataEnricher"
argument_list|,
literal|"mock:result"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: e1
block|}
block|}
return|;
block|}
comment|// START SNIPPET: e2
DECL|class|CheckLuggageBean
specifier|public
specifier|static
specifier|final
class|class
name|CheckLuggageBean
block|{
DECL|method|checkLuggage (Exchange exchange, @Body String body, @XPath(R) String custId)
specifier|public
name|void
name|checkLuggage
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
annotation|@
name|Body
name|String
name|body
parameter_list|,
annotation|@
name|XPath
argument_list|(
literal|"/order/@custId"
argument_list|)
name|String
name|custId
parameter_list|)
block|{
comment|// store the message body into the data store, using the custId as the claim check
name|dataStore
operator|.
name|put
argument_list|(
name|custId
argument_list|,
name|body
argument_list|)
expr_stmt|;
comment|// add the claim check as a header
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"claimCheck"
argument_list|,
name|custId
argument_list|)
expr_stmt|;
comment|// remove the body from the message
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
comment|// END SNIPPET: e2
comment|// START SNIPPET: e3
DECL|class|DataEnricherBean
specifier|public
specifier|static
specifier|final
class|class
name|DataEnricherBean
block|{
DECL|method|addDataBackIn (Exchange exchange, @Header(R) String claimCheck)
specifier|public
name|void
name|addDataBackIn
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
annotation|@
name|Header
argument_list|(
literal|"claimCheck"
argument_list|)
name|String
name|claimCheck
parameter_list|)
block|{
comment|// query the data store using the claim check as the key and add the data
comment|// back into the message body
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|dataStore
operator|.
name|get
argument_list|(
name|claimCheck
argument_list|)
argument_list|)
expr_stmt|;
comment|// remove the message data from the data store
name|dataStore
operator|.
name|remove
argument_list|(
name|claimCheck
argument_list|)
expr_stmt|;
comment|// remove the claim check header
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|removeHeader
argument_list|(
literal|"claimCheck"
argument_list|)
expr_stmt|;
block|}
block|}
comment|// END SNIPPET: e3
block|}
end_class

end_unit

