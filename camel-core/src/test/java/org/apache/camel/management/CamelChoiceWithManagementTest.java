begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.management
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|management
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
import|import static
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
operator|.
name|expectsMessageCount
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|CamelChoiceWithManagementTest
specifier|public
class|class
name|CamelChoiceWithManagementTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|a
specifier|private
name|MockEndpoint
name|a
decl_stmt|;
DECL|field|b
specifier|private
name|MockEndpoint
name|b
decl_stmt|;
DECL|field|c
specifier|private
name|MockEndpoint
name|c
decl_stmt|;
DECL|field|d
specifier|private
name|MockEndpoint
name|d
decl_stmt|;
DECL|field|e
specifier|private
name|MockEndpoint
name|e
decl_stmt|;
DECL|method|setUp ()
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|=
name|createCamelContext
argument_list|()
expr_stmt|;
name|assertValidContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|template
operator|=
name|context
operator|.
name|createProducerTemplate
argument_list|()
expr_stmt|;
name|RouteBuilder
index|[]
name|builders
init|=
name|createRouteBuilders
argument_list|()
decl_stmt|;
for|for
control|(
name|RouteBuilder
name|builder
range|:
name|builders
control|)
block|{
name|context
operator|.
name|addRoutes
argument_list|(
name|builder
argument_list|)
expr_stmt|;
block|}
name|startCamelContext
argument_list|()
expr_stmt|;
name|a
operator|=
name|getMockEndpoint
argument_list|(
literal|"mock:a"
argument_list|)
expr_stmt|;
name|b
operator|=
name|getMockEndpoint
argument_list|(
literal|"mock:b"
argument_list|)
expr_stmt|;
name|c
operator|=
name|getMockEndpoint
argument_list|(
literal|"mock:c"
argument_list|)
expr_stmt|;
name|d
operator|=
name|getMockEndpoint
argument_list|(
literal|"mock:d"
argument_list|)
expr_stmt|;
name|e
operator|=
name|getMockEndpoint
argument_list|(
literal|"mock:e"
argument_list|)
expr_stmt|;
block|}
DECL|method|tearDown ()
specifier|protected
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
block|}
DECL|method|testFirstChoiceRoute ()
specifier|public
name|void
name|testFirstChoiceRoute
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|body
init|=
literal|"<one/>"
decl_stmt|;
name|a
operator|.
name|expectedBodiesReceived
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|a
operator|.
name|expectedHeaderReceived
argument_list|(
literal|"CBR1"
argument_list|,
literal|"Yes"
argument_list|)
expr_stmt|;
name|c
operator|.
name|expectedBodiesReceived
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|c
operator|.
name|expectedHeaderReceived
argument_list|(
literal|"CBR1"
argument_list|,
literal|"Yes"
argument_list|)
expr_stmt|;
name|c
operator|.
name|expectedHeaderReceived
argument_list|(
literal|"Validation"
argument_list|,
literal|"Yes"
argument_list|)
expr_stmt|;
name|expectsMessageCount
argument_list|(
literal|0
argument_list|,
name|b
argument_list|,
name|d
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|template
operator|.
name|send
argument_list|(
literal|"direct:start"
argument_list|,
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
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"CBR1"
argument_list|,
literal|"Yes"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|testOtherwise ()
specifier|public
name|void
name|testOtherwise
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|body
init|=
literal|"<None/>"
decl_stmt|;
name|e
operator|.
name|expectedBodiesReceived
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|expectsMessageCount
argument_list|(
literal|0
argument_list|,
name|a
argument_list|,
name|b
argument_list|,
name|c
argument_list|,
name|d
argument_list|)
expr_stmt|;
name|template
operator|.
name|send
argument_list|(
literal|"direct:start"
argument_list|,
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
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|body
argument_list|)
expr_stmt|;
block|}
block|}
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
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|choice
argument_list|()
operator|.
name|when
argument_list|(
name|header
argument_list|(
literal|"CBR1"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"Yes"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:a"
argument_list|)
operator|.
name|setHeader
argument_list|(
literal|"Validation"
argument_list|,
name|constant
argument_list|(
literal|"Yes"
argument_list|)
argument_list|)
operator|.
name|when
argument_list|(
name|header
argument_list|(
literal|"CBR1"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"No"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:b"
argument_list|)
operator|.
name|end
argument_list|()
operator|.
name|choice
argument_list|()
operator|.
name|when
argument_list|(
name|header
argument_list|(
literal|"Validation"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"Yes"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:c"
argument_list|)
operator|.
name|when
argument_list|(
name|header
argument_list|(
literal|"Validation"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"No"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:d"
argument_list|)
operator|.
name|otherwise
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:e"
argument_list|)
operator|.
name|end
argument_list|()
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

