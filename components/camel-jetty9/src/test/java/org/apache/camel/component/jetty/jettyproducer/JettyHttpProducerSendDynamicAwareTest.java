begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jetty.jettyproducer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jetty
operator|.
name|jettyproducer
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
name|RoutesBuilder
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
name|jetty
operator|.
name|BaseJettyTest
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
DECL|class|JettyHttpProducerSendDynamicAwareTest
specifier|public
class|class
name|JettyHttpProducerSendDynamicAwareTest
extends|extends
name|BaseJettyTest
block|{
annotation|@
name|Test
DECL|method|testDynamicAware ()
specifier|public
name|void
name|testDynamicAware
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|out
init|=
name|fluentTemplate
operator|.
name|to
argument_list|(
literal|"direct:moes"
argument_list|)
operator|.
name|withHeader
argument_list|(
literal|"drink"
argument_list|,
literal|"beer"
argument_list|)
operator|.
name|request
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Drinking beer"
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|out
operator|=
name|fluentTemplate
operator|.
name|to
argument_list|(
literal|"direct:joes"
argument_list|)
operator|.
name|withHeader
argument_list|(
literal|"drink"
argument_list|,
literal|"wine"
argument_list|)
operator|.
name|request
argument_list|(
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Drinking wine"
argument_list|,
name|out
argument_list|)
expr_stmt|;
comment|// and there should only be one http endpoint as they are both on same host
name|boolean
name|found
init|=
name|context
operator|.
name|getEndpointMap
argument_list|()
operator|.
name|containsKey
argument_list|(
literal|"jetty://http://localhost:"
operator|+
name|getPort
argument_list|()
operator|+
literal|"?throwExceptionOnFailure=false"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Should find static uri"
argument_list|,
name|found
argument_list|)
expr_stmt|;
comment|// we only have 2xdirect and 2xjetty
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|context
operator|.
name|getEndpointMap
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RoutesBuilder
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
literal|"direct:moes"
argument_list|)
operator|.
name|toD
argument_list|(
literal|"jetty:http://localhost:{{port}}/moes?throwExceptionOnFailure=false&drink=${header.drink}"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:joes"
argument_list|)
operator|.
name|toD
argument_list|(
literal|"jetty:http://localhost:{{port}}/joes?throwExceptionOnFailure=false&drink=${header.drink}"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"jetty:http://localhost:{{port}}/?matchOnUriPrefix=true"
argument_list|)
operator|.
name|transform
argument_list|()
operator|.
name|simple
argument_list|(
literal|"Drinking ${header.drink[0]}"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

