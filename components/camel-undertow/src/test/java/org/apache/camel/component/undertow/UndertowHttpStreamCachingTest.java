begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.undertow
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|undertow
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
name|http
operator|.
name|HttpMethods
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
DECL|class|UndertowHttpStreamCachingTest
specifier|public
class|class
name|UndertowHttpStreamCachingTest
extends|extends
name|BaseUndertowTest
block|{
DECL|field|data
specifier|private
name|String
name|data
init|=
literal|"abcdefg"
decl_stmt|;
annotation|@
name|Test
DECL|method|testTwoWayStreaming ()
specifier|public
name|void
name|testTwoWayStreaming
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
name|template
operator|.
name|request
argument_list|(
literal|"undertow:http://localhost:{{port}}/client"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
operator|new
name|String
argument_list|(
operator|(
name|byte
index|[]
operator|)
name|exchange
operator|.
name|getMessage
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
operator|.
name|equals
argument_list|(
name|data
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
name|getContext
argument_list|()
operator|.
name|setStreamCaching
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|getContext
argument_list|()
operator|.
name|getStreamCachingStrategy
argument_list|()
operator|.
name|setSpoolThreshold
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"undertow:http://localhost:{{port}}/client"
argument_list|)
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_METHOD
argument_list|,
name|constant
argument_list|(
name|HttpMethods
operator|.
name|POST
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"http://localhost:{{port}}/server?bridgeEndpoint=true"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:lgName?showBody=true"
argument_list|)
operator|.
name|end
argument_list|()
expr_stmt|;
name|from
argument_list|(
literal|"undertow:http://localhost:{{port}}/server?httpMethodRestrict=POST"
argument_list|)
operator|.
name|setBody
argument_list|(
name|simple
argument_list|(
name|data
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:lgName?showBody=true"
argument_list|)
operator|.
name|end
argument_list|()
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

