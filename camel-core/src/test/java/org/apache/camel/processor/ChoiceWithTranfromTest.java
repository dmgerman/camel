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
name|builder
operator|.
name|RouteBuilder
import|;
end_import

begin_class
DECL|class|ChoiceWithTranfromTest
specifier|public
class|class
name|ChoiceWithTranfromTest
extends|extends
name|ContextTestSupport
block|{
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
name|from
argument_list|(
literal|"direct:outerRoute"
argument_list|)
operator|.
name|id
argument_list|(
literal|"out"
argument_list|)
operator|.
name|choice
argument_list|()
operator|.
name|when
argument_list|(
name|header
argument_list|(
literal|"test-header"
argument_list|)
operator|.
name|isNotNull
argument_list|()
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:mainProcess"
argument_list|)
operator|.
name|otherwise
argument_list|()
operator|.
name|to
argument_list|(
literal|"log:badMessage"
argument_list|)
operator|.
name|transform
argument_list|()
operator|.
name|method
argument_list|(
operator|new
name|MyBean
argument_list|()
argument_list|,
literal|"processRejectedMessage"
argument_list|)
operator|.
name|end
argument_list|()
expr_stmt|;
name|from
argument_list|(
literal|"direct:mainProcess"
argument_list|)
operator|.
name|bean
argument_list|(
operator|new
name|MyBean
argument_list|()
argument_list|,
literal|"processMessage"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|MyBean
specifier|public
specifier|static
class|class
name|MyBean
block|{
DECL|method|processRejectedMessage (@ody String message)
specifier|public
name|String
name|processRejectedMessage
parameter_list|(
annotation|@
name|Body
name|String
name|message
parameter_list|)
block|{
return|return
literal|"Rejecting "
operator|+
name|message
return|;
block|}
DECL|method|processMessage (@ody String message)
specifier|public
name|String
name|processMessage
parameter_list|(
annotation|@
name|Body
name|String
name|message
parameter_list|)
block|{
return|return
literal|"Processing "
operator|+
name|message
return|;
block|}
block|}
DECL|method|testRoute ()
specifier|public
name|void
name|testRoute
parameter_list|()
block|{
name|String
name|result
init|=
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"direct:outerRoute"
argument_list|,
literal|"body"
argument_list|,
literal|"test-header"
argument_list|,
literal|"headerValue"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Processing body"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|result
operator|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:outerRoute"
argument_list|,
literal|"body"
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Rejecting body"
argument_list|,
name|result
argument_list|)
expr_stmt|;
comment|//context.getRouteDefinition("out").toString();
block|}
block|}
end_class

end_unit

