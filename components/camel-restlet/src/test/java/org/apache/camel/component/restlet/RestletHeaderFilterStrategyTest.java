begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.restlet
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|restlet
package|;
end_package

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
name|Processor
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
name|DefaultHeaderFilterStrategy
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
DECL|class|RestletHeaderFilterStrategyTest
specifier|public
class|class
name|RestletHeaderFilterStrategyTest
extends|extends
name|RestletTestSupport
block|{
DECL|field|HEADER_FILTER
specifier|private
specifier|static
specifier|final
name|String
name|HEADER_FILTER
init|=
literal|"filter"
decl_stmt|;
annotation|@
name|Test
DECL|method|testRestletProducerInFilterAllowedHeader ()
specifier|public
name|void
name|testRestletProducerInFilterAllowedHeader
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|acceptedHeaderKey
init|=
literal|"dontFilter"
decl_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:out"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedHeaderReceived
argument_list|(
name|acceptedHeaderKey
argument_list|,
literal|"any value"
argument_list|)
expr_stmt|;
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|null
argument_list|,
name|acceptedHeaderKey
argument_list|,
literal|"any value"
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRestletProducerInFilterNotAllowedHeader ()
specifier|public
name|void
name|testRestletProducerInFilterNotAllowedHeader
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|notAcceptedHeaderKey
init|=
name|HEADER_FILTER
operator|+
literal|"ThisHeader"
decl_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:out"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|whenAnyExchangeReceived
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|key
range|:
name|headers
operator|.
name|keySet
argument_list|()
control|)
block|{
name|assertFalse
argument_list|(
literal|"Header should have been filtered: "
operator|+
name|key
argument_list|,
name|key
operator|.
name|startsWith
argument_list|(
name|HEADER_FILTER
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|null
argument_list|,
name|notAcceptedHeaderKey
argument_list|,
literal|"any value"
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
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
comment|// force synchronous processing using restlet and add filtering
name|DefaultHeaderFilterStrategy
name|strategy
init|=
operator|new
name|DefaultHeaderFilterStrategy
argument_list|()
decl_stmt|;
name|strategy
operator|.
name|setInFilterPattern
argument_list|(
name|HEADER_FILTER
operator|+
literal|".*"
argument_list|)
expr_stmt|;
name|strategy
operator|.
name|setOutFilterPattern
argument_list|(
name|HEADER_FILTER
operator|+
literal|".*"
argument_list|)
expr_stmt|;
name|RestletComponent
name|restlet
init|=
name|context
operator|.
name|getComponent
argument_list|(
literal|"restlet"
argument_list|,
name|RestletComponent
operator|.
name|class
argument_list|)
decl_stmt|;
name|restlet
operator|.
name|setHeaderFilterStrategy
argument_list|(
name|strategy
argument_list|)
expr_stmt|;
name|restlet
operator|.
name|setSynchronous
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"restlet:http://localhost:"
operator|+
name|portNum
operator|+
literal|"/users/123/exclude"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:reply"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"restlet:http://localhost:"
operator|+
name|portNum
operator|+
literal|"/users/{id}/{filterExcluded}?restletMethods=GET"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:out"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

