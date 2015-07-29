begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.velocity
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|velocity
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
name|Produce
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
name|ProducerTemplate
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

begin_class
DECL|class|VelocitySupplementalContextTest
specifier|public
class|class
name|VelocitySupplementalContextTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Produce
argument_list|(
name|uri
operator|=
literal|"direct:input"
argument_list|)
DECL|field|inputEndpoint
specifier|protected
name|ProducerTemplate
name|inputEndpoint
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:results"
argument_list|)
DECL|field|outputEndpoint
specifier|protected
name|MockEndpoint
name|outputEndpoint
decl_stmt|;
annotation|@
name|Test
DECL|method|testCamelRoute ()
specifier|public
name|void
name|testCamelRoute
parameter_list|()
throws|throws
name|Exception
block|{
name|outputEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|outputEndpoint
operator|.
name|expectedHeaderReceived
argument_list|(
literal|"body"
argument_list|,
literal|"new_body"
argument_list|)
expr_stmt|;
name|outputEndpoint
operator|.
name|expectedHeaderReceived
argument_list|(
literal|"in.body"
argument_list|,
literal|"old_body"
argument_list|)
expr_stmt|;
name|outputEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"bar"
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
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|VelocityConstants
operator|.
name|VELOCITY_TEMPLATE
argument_list|,
literal|"#set( $headers.body = ${body} )\n#set( $headers['in.body'] = $in.body )\n"
operator|+
literal|"bar"
argument_list|)
expr_stmt|;
name|inputEndpoint
operator|.
name|sendBodyAndHeaders
argument_list|(
literal|"old_body"
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
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|supplementalContext
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|supplementalContext
operator|.
name|put
argument_list|(
literal|"body"
argument_list|,
literal|"new_body"
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
name|from
argument_list|(
literal|"direct:input"
argument_list|)
operator|.
name|setHeader
argument_list|(
name|VelocityConstants
operator|.
name|VELOCITY_SUPPLEMENTAL_CONTEXT
argument_list|)
operator|.
name|constant
argument_list|(
name|supplementalContext
argument_list|)
operator|.
name|to
argument_list|(
literal|"velocity:template-in-header"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:results"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

