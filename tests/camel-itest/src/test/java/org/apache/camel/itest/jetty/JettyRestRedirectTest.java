begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.jetty
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|jetty
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
name|Processor
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
name|test
operator|.
name|AvailablePortFinder
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
DECL|class|JettyRestRedirectTest
specifier|public
class|class
name|JettyRestRedirectTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|port
specifier|private
name|int
name|port
decl_stmt|;
annotation|@
name|Test
DECL|method|testRedirectInvocation ()
specifier|public
name|void
name|testRedirectInvocation
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|response
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"http4://localhost:"
operator|+
name|port
operator|+
literal|"/metadata/profile/tag"
argument_list|,
literal|"<hello>Camel</hello>"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"It should support the redirect out of box."
argument_list|,
literal|"Mock profile"
argument_list|,
name|response
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
throws|throws
name|Exception
block|{
name|port
operator|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|(
literal|8000
argument_list|)
expr_stmt|;
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
name|restConfiguration
argument_list|()
operator|.
name|component
argument_list|(
literal|"jetty"
argument_list|)
operator|.
name|host
argument_list|(
literal|"localhost"
argument_list|)
operator|.
name|scheme
argument_list|(
literal|"http"
argument_list|)
operator|.
name|port
argument_list|(
name|port
argument_list|)
expr_stmt|;
name|rest
argument_list|(
literal|"/metadata/profile"
argument_list|)
operator|.
name|get
argument_list|(
literal|"/{id}"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:profileLookup"
argument_list|)
operator|.
name|post
argument_list|(
literal|"/tag"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:tag"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:profileLookup"
argument_list|)
operator|.
name|transform
argument_list|()
operator|.
name|constant
argument_list|(
literal|"Mock profile"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:tag"
argument_list|)
operator|.
name|log
argument_list|(
literal|"${headers}"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|ex
parameter_list|)
throws|throws
name|Exception
block|{
name|ex
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_RESPONSE_CODE
argument_list|,
literal|303
argument_list|)
expr_stmt|;
name|ex
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"Location"
argument_list|,
literal|"/metadata/profile/1"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|log
argument_list|(
literal|"${headers}"
argument_list|)
operator|.
name|transform
argument_list|()
operator|.
name|constant
argument_list|(
literal|"Redirecting..."
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

