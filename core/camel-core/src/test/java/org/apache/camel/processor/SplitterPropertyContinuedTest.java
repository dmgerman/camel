begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|builder
operator|.
name|RouteBuilder
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

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|SplitterPropertyContinuedTest
specifier|public
class|class
name|SplitterPropertyContinuedTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testSplitterPropertyContinued ()
specifier|public
name|void
name|testSplitterPropertyContinued
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:end"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"A,Kaboom,B,C"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:end"
argument_list|)
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|exchangeProperty
argument_list|(
literal|"errorCode"
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:error"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Kaboom"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:error"
argument_list|)
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|exchangeProperty
argument_list|(
literal|"errorCode"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"ERR-1"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:split"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"A"
argument_list|,
literal|"B"
argument_list|,
literal|"C"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:split"
argument_list|)
operator|.
name|allMessages
argument_list|()
operator|.
name|exchangeProperty
argument_list|(
literal|"errorCode"
argument_list|)
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
literal|"A,Kaboom,B,C"
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
name|onException
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
operator|.
name|continued
argument_list|(
literal|true
argument_list|)
operator|.
name|setProperty
argument_list|(
literal|"errorCode"
argument_list|,
name|constant
argument_list|(
literal|"ERR-1"
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|split
argument_list|(
name|body
argument_list|()
argument_list|)
operator|.
name|log
argument_list|(
literal|"Step #1 - Body: ${body} with error code: ${exchangeProperty.errorCode}"
argument_list|)
operator|.
name|choice
argument_list|()
operator|.
name|when
argument_list|(
name|body
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Kaboom"
argument_list|)
argument_list|)
operator|.
name|throwException
argument_list|(
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Damn"
argument_list|)
argument_list|)
operator|.
name|end
argument_list|()
operator|.
name|log
argument_list|(
literal|"Step #2 - Body: ${body} with error code: ${exchangeProperty.errorCode}"
argument_list|)
operator|.
name|choice
argument_list|()
operator|.
name|when
argument_list|(
name|simple
argument_list|(
literal|"${exchangeProperty.errorCode} != null"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:error"
argument_list|)
operator|.
name|otherwise
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:split"
argument_list|)
operator|.
name|end
argument_list|()
operator|.
name|end
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:end"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

