begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|MarshalDomainObjectJSONTest
specifier|public
class|class
name|MarshalDomainObjectJSONTest
extends|extends
name|MarshalDomainObjectTest
block|{
annotation|@
name|Test
DECL|method|testMarshalAndUnmarshalWithPrettyPrint ()
specifier|public
name|void
name|testMarshalAndUnmarshalWithPrettyPrint
parameter_list|()
throws|throws
name|Exception
block|{
name|PurchaseOrder
name|order
init|=
operator|new
name|PurchaseOrder
argument_list|()
decl_stmt|;
name|order
operator|.
name|setName
argument_list|(
literal|"pretty printed Camel"
argument_list|)
expr_stmt|;
name|order
operator|.
name|setAmount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|order
operator|.
name|setPrice
argument_list|(
literal|7.91
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:reverse"
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
name|PurchaseOrder
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
name|order
argument_list|)
expr_stmt|;
name|Object
name|marshalled
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:inPretty"
argument_list|,
name|order
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
comment|// the line-separator used by JsonWriter is "\n", even on windows
name|String
name|expected
init|=
literal|"{\"org.apache.camel.dataformat.xstream.PurchaseOrder\": {\n"
operator|+
literal|"  \"name\": \"pretty printed Camel\",\n"
operator|+
literal|"  \"price\": 7.91,\n"
operator|+
literal|"  \"amount\": 1.0\n"
operator|+
literal|"}}"
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|marshalledAsString
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:backPretty"
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
name|json
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
comment|// just used for helping to marshal
name|from
argument_list|(
literal|"direct:marshal"
argument_list|)
operator|.
name|marshal
argument_list|()
operator|.
name|json
argument_list|()
expr_stmt|;
name|from
argument_list|(
literal|"direct:reverse"
argument_list|)
operator|.
name|unmarshal
argument_list|()
operator|.
name|json
argument_list|(
name|JsonLibrary
operator|.
name|XStream
argument_list|,
name|PurchaseOrder
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:reverse"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:inPretty"
argument_list|)
operator|.
name|marshal
argument_list|()
operator|.
name|json
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:backPretty"
argument_list|)
operator|.
name|unmarshal
argument_list|()
operator|.
name|json
argument_list|(
name|JsonLibrary
operator|.
name|XStream
argument_list|,
name|PurchaseOrder
operator|.
name|class
argument_list|,
literal|true
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:reverse"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

