begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.issues
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|issues
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
DECL|class|JettyHttpFileCacheTest
specifier|public
class|class
name|JettyHttpFileCacheTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|TEST_STRING
specifier|private
specifier|static
specifier|final
name|String
name|TEST_STRING
init|=
literal|"This is a test string and it has enough"
operator|+
literal|" aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa "
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
name|context
operator|.
name|getStreamCachingStrategy
argument_list|()
operator|.
name|setSpoolThreshold
argument_list|(
literal|16
argument_list|)
expr_stmt|;
name|context
operator|.
name|getStreamCachingStrategy
argument_list|()
operator|.
name|setSpoolDirectory
argument_list|(
literal|"target/cachedir"
argument_list|)
expr_stmt|;
name|deleteDirectory
argument_list|(
literal|"target/cachedir"
argument_list|)
expr_stmt|;
name|createDirectory
argument_list|(
literal|"target/cachedir"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetWithRelativePath ()
specifier|public
name|void
name|testGetWithRelativePath
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
literal|"http://localhost:8201/clipboard/download/file"
argument_list|,
literal|"   "
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"should get the right response"
argument_list|,
name|TEST_STRING
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
literal|"target/cachedir"
argument_list|)
decl_stmt|;
name|String
index|[]
name|files
init|=
name|file
operator|.
name|list
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"There should not have any temp file"
argument_list|,
name|files
operator|.
name|length
operator|==
literal|0
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
literal|"jetty:http://localhost:8201/clipboard/download?chunked=true&matchOnUriPrefix=true"
argument_list|)
operator|.
name|to
argument_list|(
literal|"http://localhost:9101?bridgeEndpoint=true"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"jetty:http://localhost:9101?chunked=true&matchOnUriPrefix=true"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|TEST_STRING
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

