begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jcr
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jcr
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|jcr
operator|.
name|Node
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jcr
operator|.
name|Session
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jcr
operator|.
name|Value
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jcr
operator|.
name|ValueFactory
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
name|junit
operator|.
name|Before
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

begin_class
DECL|class|JcrGetNodeByIdTest
specifier|public
class|class
name|JcrGetNodeByIdTest
extends|extends
name|JcrRouteTestSupport
block|{
DECL|field|CONTENT
specifier|public
specifier|static
specifier|final
name|String
name|CONTENT
init|=
literal|"content is here"
decl_stmt|;
DECL|field|APPROVED
specifier|public
specifier|static
specifier|final
name|Boolean
name|APPROVED
init|=
literal|true
decl_stmt|;
DECL|field|identifier
specifier|private
name|String
name|identifier
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
literal|"mock:result"
argument_list|)
DECL|field|result
specifier|private
name|MockEndpoint
name|result
decl_stmt|;
annotation|@
name|Override
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|Session
name|session
init|=
name|openSession
argument_list|()
decl_stmt|;
name|Node
name|node
init|=
name|session
operator|.
name|getRootNode
argument_list|()
operator|.
name|addNode
argument_list|(
literal|"home"
argument_list|)
operator|.
name|addNode
argument_list|(
literal|"test"
argument_list|)
decl_stmt|;
name|node
operator|.
name|setProperty
argument_list|(
literal|"content.approved"
argument_list|,
name|APPROVED
argument_list|)
expr_stmt|;
name|node
operator|.
name|setProperty
argument_list|(
literal|"my.contents.property"
argument_list|,
name|CONTENT
argument_list|)
expr_stmt|;
name|ValueFactory
name|valFact
init|=
name|session
operator|.
name|getValueFactory
argument_list|()
decl_stmt|;
name|Value
index|[]
name|vals
init|=
operator|new
name|Value
index|[]
block|{
name|valFact
operator|.
name|createValue
argument_list|(
literal|"value-1"
argument_list|)
block|,
name|valFact
operator|.
name|createValue
argument_list|(
literal|"value-2"
argument_list|)
block|}
decl_stmt|;
name|node
operator|.
name|setProperty
argument_list|(
literal|"my.multi.valued"
argument_list|,
name|vals
argument_list|)
expr_stmt|;
name|identifier
operator|=
name|node
operator|.
name|getIdentifier
argument_list|()
expr_stmt|;
name|session
operator|.
name|save
argument_list|()
expr_stmt|;
name|session
operator|.
name|logout
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testJcrProducer ()
specifier|public
name|void
name|testJcrProducer
parameter_list|()
throws|throws
name|Exception
block|{
name|result
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|result
operator|.
name|expectedHeaderReceived
argument_list|(
literal|"my.contents.property"
argument_list|,
name|CONTENT
argument_list|)
expr_stmt|;
name|result
operator|.
name|expectedHeaderReceived
argument_list|(
literal|"content.approved"
argument_list|,
name|APPROVED
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|createExchangeWithBody
argument_list|(
name|identifier
argument_list|)
decl_stmt|;
name|template
operator|.
name|send
argument_list|(
literal|"direct:a"
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
comment|// START SNIPPET: jcr-get-node
name|from
argument_list|(
literal|"direct:a"
argument_list|)
operator|.
name|setHeader
argument_list|(
name|JcrConstants
operator|.
name|JCR_OPERATION
argument_list|,
name|constant
argument_list|(
name|JcrConstants
operator|.
name|JCR_GET_BY_ID
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"jcr://user:pass@repository"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: jcr-get-node
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

