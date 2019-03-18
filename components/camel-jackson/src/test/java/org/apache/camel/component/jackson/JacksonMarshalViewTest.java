begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jackson
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jackson
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
name|model
operator|.
name|dataformat
operator|.
name|JsonLibrary
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
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
DECL|class|JacksonMarshalViewTest
specifier|public
class|class
name|JacksonMarshalViewTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testMarshalAndUnmarshalPojoWithView ()
specifier|public
name|void
name|testMarshalAndUnmarshalPojoWithView
parameter_list|()
throws|throws
name|Exception
block|{
name|TestPojoView
name|in
init|=
operator|new
name|TestPojoView
argument_list|()
decl_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:reversePojoAgeView"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|isInstanceOf
argument_list|(
name|TestPojoView
operator|.
name|class
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|isEqualTo
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|Object
name|marshalled
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:inPojoAgeView"
argument_list|,
name|in
argument_list|)
decl_stmt|;
name|String
name|marshalledAsString
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|marshalled
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"{\"age\":30,\"height\":190}"
argument_list|,
name|marshalledAsString
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:backPojoAgeView"
argument_list|,
name|marshalled
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
DECL|method|testMarshalAndUnmarshalPojoWithAnotherView ()
specifier|public
name|void
name|testMarshalAndUnmarshalPojoWithAnotherView
parameter_list|()
throws|throws
name|Exception
block|{
name|TestPojoView
name|in
init|=
operator|new
name|TestPojoView
argument_list|()
decl_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:reversePojoWeightView"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|isInstanceOf
argument_list|(
name|TestPojoView
operator|.
name|class
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|isEqualTo
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|Object
name|marshalled
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:inPojoWeightView"
argument_list|,
name|in
argument_list|)
decl_stmt|;
name|String
name|marshalledAsString
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|marshalled
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"{\"height\":190,\"weight\":70}"
argument_list|,
name|marshalledAsString
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:backPojoWeightView"
argument_list|,
name|marshalled
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
comment|// START SNIPPET: format
name|from
argument_list|(
literal|"direct:inPojoAgeView"
argument_list|)
operator|.
name|marshal
argument_list|()
operator|.
name|json
argument_list|(
name|TestPojoView
operator|.
name|class
argument_list|,
name|Views
operator|.
name|Age
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// END SNIPPET: format
name|from
argument_list|(
literal|"direct:backPojoAgeView"
argument_list|)
operator|.
name|unmarshal
argument_list|()
operator|.
name|json
argument_list|(
name|JsonLibrary
operator|.
name|Jackson
argument_list|,
name|TestPojoView
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:reversePojoAgeView"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:inPojoWeightView"
argument_list|)
operator|.
name|marshal
argument_list|()
operator|.
name|json
argument_list|(
name|TestPojoView
operator|.
name|class
argument_list|,
name|Views
operator|.
name|Weight
operator|.
name|class
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:backPojoWeightView"
argument_list|)
operator|.
name|unmarshal
argument_list|()
operator|.
name|json
argument_list|(
name|JsonLibrary
operator|.
name|Jackson
argument_list|,
name|TestPojoView
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:reversePojoWeightView"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

