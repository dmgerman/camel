begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jacksonxml
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jacksonxml
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|Context
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
name|Endpoint
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
name|processor
operator|.
name|binding
operator|.
name|DataFormatBinding
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
DECL|class|JacksonNameBindingTest
specifier|public
class|class
name|JacksonNameBindingTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|boundEndpoint
specifier|protected
name|Endpoint
name|boundEndpoint
decl_stmt|;
DECL|field|results
specifier|protected
name|MockEndpoint
name|results
decl_stmt|;
annotation|@
name|Test
DECL|method|testMarshalAndUnmarshalPojo ()
specifier|public
name|void
name|testMarshalAndUnmarshalPojo
parameter_list|()
throws|throws
name|Exception
block|{
name|TestPojo
name|in
init|=
operator|new
name|TestPojo
argument_list|()
decl_stmt|;
name|in
operator|.
name|setName
argument_list|(
literal|"Camel"
argument_list|)
expr_stmt|;
name|results
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|results
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
name|TestPojo
operator|.
name|class
argument_list|)
expr_stmt|;
name|results
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
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
name|in
argument_list|)
expr_stmt|;
name|results
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
name|results
operator|=
name|getMockEndpoint
argument_list|(
literal|"mock:results"
argument_list|)
expr_stmt|;
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
comment|// lets use the URI to associate the binding
comment|// though it would be cleaner to use a DSL...
name|boundEndpoint
operator|=
name|endpoint
argument_list|(
literal|"binding:jackson:file:target/queue"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
name|boundEndpoint
argument_list|)
operator|.
name|to
argument_list|(
literal|"file:target/copyOfMessages"
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|boundEndpoint
argument_list|)
operator|.
name|to
argument_list|(
name|results
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
DECL|method|createJndiContext ()
specifier|protected
name|Context
name|createJndiContext
parameter_list|()
throws|throws
name|Exception
block|{
name|Context
name|context
init|=
name|super
operator|.
name|createJndiContext
argument_list|()
decl_stmt|;
name|JacksonXMLDataFormat
name|format
init|=
operator|new
name|JacksonXMLDataFormat
argument_list|(
name|TestPojo
operator|.
name|class
argument_list|)
decl_stmt|;
name|context
operator|.
name|bind
argument_list|(
literal|"jackson"
argument_list|,
operator|new
name|DataFormatBinding
argument_list|(
name|format
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
block|}
end_class

end_unit

