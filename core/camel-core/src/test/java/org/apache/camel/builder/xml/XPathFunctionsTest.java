begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder.xml
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|xml
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
name|Test
import|;
end_import

begin_comment
comment|/**  * XPath with and without header test.  */
end_comment

begin_class
DECL|class|XPathFunctionsTest
specifier|public
class|class
name|XPathFunctionsTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testChoiceWithHeaderAndPropertiesSelectCamel ()
specifier|public
name|void
name|testChoiceWithHeaderAndPropertiesSelectCamel
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:camel"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"<name>King</name>"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedHeaderReceived
argument_list|(
literal|"type"
argument_list|,
literal|"Camel"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:in"
argument_list|,
literal|"<name>King</name>"
argument_list|,
literal|"type"
argument_list|,
literal|"Camel"
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
DECL|method|testChoiceWithNoHeaderAndPropertiesSelectDonkey ()
specifier|public
name|void
name|testChoiceWithNoHeaderAndPropertiesSelectDonkey
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:donkey"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"<name>Donkey Kong</name>"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:in"
argument_list|,
literal|"<name>Donkey Kong</name>"
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
DECL|method|testChoiceWithNoHeaderAndPropertiesSelectOther ()
specifier|public
name|void
name|testChoiceWithNoHeaderAndPropertiesSelectOther
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:other"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"<name>Other</name>"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:in"
argument_list|,
literal|"<name>Other</name>"
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
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
comment|// START SNIPPET: ex
comment|// setup properties component
name|context
operator|.
name|getPropertiesComponent
argument_list|()
operator|.
name|setLocation
argument_list|(
literal|"classpath:org/apache/camel/builder/xml/myprop.properties"
argument_list|)
expr_stmt|;
comment|// myprop.properties contains the following properties
comment|// foo=Camel
comment|// bar=Kong
name|from
argument_list|(
literal|"direct:in"
argument_list|)
operator|.
name|choice
argument_list|()
comment|// $type is a variable for the header with key type
comment|// here we use the properties function to lookup foo from
comment|// the properties files
comment|// which at runtime will be evaluted to 'Camel'
operator|.
name|when
argument_list|()
operator|.
name|xpath
argument_list|(
literal|"$type = function:properties('foo')"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:camel"
argument_list|)
comment|// here we use the simple language to evaluate the
comment|// expression
comment|// which at runtime will be evaluated to 'Donkey Kong'
operator|.
name|when
argument_list|()
operator|.
name|xpath
argument_list|(
literal|"//name = function:simple('Donkey {{bar}}')"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:donkey"
argument_list|)
operator|.
name|otherwise
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:other"
argument_list|)
operator|.
name|end
argument_list|()
expr_stmt|;
comment|// END SNIPPET: ex
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

