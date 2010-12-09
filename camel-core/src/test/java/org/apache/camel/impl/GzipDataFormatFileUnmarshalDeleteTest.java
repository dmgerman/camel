begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
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

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|GzipDataFormatFileUnmarshalDeleteTest
specifier|public
class|class
name|GzipDataFormatFileUnmarshalDeleteTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Override
DECL|method|setUp ()
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteDirectory
argument_list|(
literal|"target/gzip"
argument_list|)
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
DECL|method|testGzipFileUnmarshalDelete ()
specifier|public
name|void
name|testGzipFileUnmarshalDelete
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file:target/gzip"
argument_list|,
literal|"Hello World"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"hello.txt"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|oneExchangeDone
operator|.
name|matchesMockWaitTime
argument_list|()
expr_stmt|;
name|File
name|in
init|=
operator|new
name|File
argument_list|(
literal|"target/gzip/hello.txt"
argument_list|)
operator|.
name|getAbsoluteFile
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
literal|"Should have been deleted "
operator|+
name|in
argument_list|,
name|in
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|File
name|out
init|=
operator|new
name|File
argument_list|(
literal|"target/gzip/out/hello.txt.gz"
argument_list|)
operator|.
name|getAbsoluteFile
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
literal|"Should have been deleted "
operator|+
name|out
argument_list|,
name|out
operator|.
name|exists
argument_list|()
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
name|from
argument_list|(
literal|"file:target/gzip?delete=true"
argument_list|)
operator|.
name|marshal
argument_list|()
operator|.
name|gzip
argument_list|()
operator|.
name|to
argument_list|(
literal|"file:target/gzip/out?fileName=${file:name}.gz"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"file:target/gzip/out?delete=true"
argument_list|)
operator|.
name|unmarshal
argument_list|()
operator|.
name|gzip
argument_list|()
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

