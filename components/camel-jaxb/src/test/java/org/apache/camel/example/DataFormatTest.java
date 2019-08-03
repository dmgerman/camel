begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
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
name|converter
operator|.
name|jaxb
operator|.
name|JaxbDataFormat
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
name|foo
operator|.
name|bar
operator|.
name|PersonType
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
DECL|class|DataFormatTest
specifier|public
class|class
name|DataFormatTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|resultEndpoint
specifier|private
name|MockEndpoint
name|resultEndpoint
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
name|resultEndpoint
operator|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"mock:result"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMarshalThenUnmarshalBean ()
specifier|public
name|void
name|testMarshalThenUnmarshalBean
parameter_list|()
throws|throws
name|Exception
block|{
name|PurchaseOrder
name|bean
init|=
operator|new
name|PurchaseOrder
argument_list|()
decl_stmt|;
name|bean
operator|.
name|setName
argument_list|(
literal|"Beer"
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setAmount
argument_list|(
literal|23
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setPrice
argument_list|(
literal|2.5
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
name|bean
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
name|bean
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMarshalPrettyPrint ()
specifier|public
name|void
name|testMarshalPrettyPrint
parameter_list|()
throws|throws
name|Exception
block|{
name|PersonType
name|person
init|=
operator|new
name|PersonType
argument_list|()
decl_stmt|;
name|person
operator|.
name|setFirstName
argument_list|(
literal|"Willem"
argument_list|)
expr_stmt|;
name|person
operator|.
name|setLastName
argument_list|(
literal|"Jiang"
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:prettyPrint"
argument_list|,
name|person
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|Exchange
name|exchange
init|=
name|resultEndpoint
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|String
name|result
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"The result should not be null"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|int
name|indexPerson
init|=
name|result
operator|.
name|indexOf
argument_list|(
literal|"<Person>"
argument_list|)
decl_stmt|;
name|int
name|indexFirstName
init|=
name|result
operator|.
name|indexOf
argument_list|(
literal|"<firstName>"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"we should find the<Person>"
argument_list|,
name|indexPerson
operator|>
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"we should find the<firstName>"
argument_list|,
name|indexFirstName
operator|>
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"There should some sapce between<Person> and<firstName>"
argument_list|,
name|indexFirstName
operator|-
name|indexPerson
operator|>
literal|8
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
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
name|JaxbDataFormat
name|example
init|=
operator|new
name|JaxbDataFormat
argument_list|(
literal|"org.apache.camel.example"
argument_list|)
decl_stmt|;
name|JaxbDataFormat
name|person
init|=
operator|new
name|JaxbDataFormat
argument_list|(
literal|"org.apache.camel.foo.bar"
argument_list|)
decl_stmt|;
name|person
operator|.
name|setPrettyPrint
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|marshal
argument_list|(
name|example
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:marshalled"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:marshalled"
argument_list|)
operator|.
name|unmarshal
argument_list|()
operator|.
name|jaxb
argument_list|(
literal|"org.apache.camel.example"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:prettyPrint"
argument_list|)
operator|.
name|marshal
argument_list|(
name|person
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

