begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.xstream
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|xstream
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

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
name|List
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

begin_comment
comment|/**  * Marshal tests with List objects.  */
end_comment

begin_class
DECL|class|MarshalListTest
specifier|public
class|class
name|MarshalListTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:result"
argument_list|)
DECL|field|mock
name|MockEndpoint
name|mock
decl_stmt|;
annotation|@
name|Test
DECL|method|testMarshalList ()
specifier|public
name|void
name|testMarshalList
parameter_list|()
throws|throws
name|Exception
block|{
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"<?xml version='1.0' encoding='ISO-8859-1'?>"
operator|+
literal|"<list><string>Hello World</string></list>"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|body
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|body
operator|.
name|add
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndProperty
argument_list|(
literal|"direct:in"
argument_list|,
name|body
argument_list|,
name|Exchange
operator|.
name|CHARSET_NAME
argument_list|,
literal|"ISO-8859-1"
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
DECL|method|testMarshalListWithMap ()
specifier|public
name|void
name|testMarshalListWithMap
parameter_list|()
throws|throws
name|Exception
block|{
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"<?xml version='1.0' encoding='UTF-8'?><list><map><entry><string>city</string>"
operator|+
literal|"<string>London\u0E08</string></entry></map></list>"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Map
argument_list|<
name|Object
argument_list|,
name|String
argument_list|>
argument_list|>
name|body
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|Object
argument_list|,
name|String
argument_list|>
name|row
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|row
operator|.
name|put
argument_list|(
literal|"city"
argument_list|,
literal|"London\u0E08"
argument_list|)
expr_stmt|;
name|body
operator|.
name|add
argument_list|(
name|row
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndProperty
argument_list|(
literal|"direct:in"
argument_list|,
name|body
argument_list|,
name|Exchange
operator|.
name|CHARSET_NAME
argument_list|,
literal|"UTF-8"
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
DECL|method|testSetEncodingOnXstream ()
specifier|public
name|void
name|testSetEncodingOnXstream
parameter_list|()
throws|throws
name|Exception
block|{
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"<?xml version='1.0' encoding='UTF-8'?><list><map><entry><string>city</string>"
operator|+
literal|"<string>London\u0E08</string></entry></map></list>"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Map
argument_list|<
name|Object
argument_list|,
name|String
argument_list|>
argument_list|>
name|body
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|Object
argument_list|,
name|String
argument_list|>
name|row
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|row
operator|.
name|put
argument_list|(
literal|"city"
argument_list|,
literal|"London\u0E08"
argument_list|)
expr_stmt|;
name|body
operator|.
name|add
argument_list|(
name|row
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:in-UTF-8"
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
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
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"direct:in"
argument_list|)
operator|.
name|marshal
argument_list|()
operator|.
name|xstream
argument_list|()
operator|.
name|to
argument_list|(
name|mock
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:in-UTF-8"
argument_list|)
operator|.
name|marshal
argument_list|()
operator|.
name|xstream
argument_list|(
literal|"UTF-8"
argument_list|)
operator|.
name|to
argument_list|(
name|mock
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

