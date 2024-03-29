begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jetty
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
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|JettyOnExceptionHandledTest
specifier|public
class|class
name|JettyOnExceptionHandledTest
extends|extends
name|BaseJettyTest
block|{
annotation|@
name|Test
DECL|method|testJettyOnException ()
specifier|public
name|void
name|testJettyOnException
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|reply
init|=
name|template
operator|.
name|request
argument_list|(
literal|"http://localhost:{{port}}/myserver?throwExceptionOnFailure=false"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|reply
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Dude something went wrong"
argument_list|,
name|reply
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|500
argument_list|,
name|reply
operator|.
name|getOut
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_RESPONSE_CODE
argument_list|)
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
comment|// START SNIPPET: e1
name|from
argument_list|(
literal|"jetty://http://localhost:{{port}}/myserver"
argument_list|)
comment|// use onException to catch all exceptions and return a
comment|// custom reply message
operator|.
name|onException
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
operator|.
name|handled
argument_list|(
literal|true
argument_list|)
comment|// create a custom failure response
operator|.
name|transform
argument_list|(
name|constant
argument_list|(
literal|"Dude something went wrong"
argument_list|)
argument_list|)
comment|// we must remember to set error code 500 as handled(true)
comment|// otherwise would let Camel thing its a OK response (200)
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_RESPONSE_CODE
argument_list|,
name|constant
argument_list|(
literal|500
argument_list|)
argument_list|)
operator|.
name|end
argument_list|()
comment|// now just force an exception immediately
operator|.
name|throwException
argument_list|(
operator|new
name|IllegalArgumentException
argument_list|(
literal|"I cannot do this"
argument_list|)
argument_list|)
expr_stmt|;
comment|// END SNIPPET: e1
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

