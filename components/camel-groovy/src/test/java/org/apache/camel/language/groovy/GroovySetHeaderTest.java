begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.language.groovy
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|language
operator|.
name|groovy
package|;
end_package

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
comment|/**  * @version   */
end_comment

begin_class
DECL|class|GroovySetHeaderTest
specifier|public
class|class
name|GroovySetHeaderTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testSetHeader ()
specifier|public
name|void
name|testSetHeader
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
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
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedHeaderReceived
argument_list|(
literal|"one"
argument_list|,
literal|"einz"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedHeaderReceived
argument_list|(
literal|"two"
argument_list|,
literal|"twei"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedHeaderReceived
argument_list|(
literal|"beer"
argument_list|,
literal|"Carlsberg"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedHeaderReceived
argument_list|(
literal|"drink"
argument_list|,
literal|"Carlsberg"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedHeaderReceived
argument_list|(
literal|"camelId"
argument_list|,
name|context
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"one"
argument_list|,
literal|"einz"
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"two"
argument_list|,
literal|"twei"
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"beer"
argument_list|,
literal|"Carlsberg"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeaders
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello World"
argument_list|,
name|headers
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
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|setHeader
argument_list|(
literal|"drink"
argument_list|)
operator|.
name|groovy
argument_list|(
literal|"request.headers.beer"
argument_list|)
comment|// shows how to access the camelContext value
operator|.
name|setHeader
argument_list|(
literal|"camelId"
argument_list|)
operator|.
name|groovy
argument_list|(
literal|"camelContext.name"
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

