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
name|java
operator|.
name|io
operator|.
name|BufferedInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

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
DECL|class|HttpStreamCacheFileResponseTest
specifier|public
class|class
name|HttpStreamCacheFileResponseTest
extends|extends
name|BaseJettyTest
block|{
DECL|field|body
specifier|private
name|String
name|body
init|=
literal|"12345678901234567890123456789012345678901234567890"
decl_stmt|;
DECL|field|body2
specifier|private
name|String
name|body2
init|=
literal|"Bye "
operator|+
name|body
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
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testStreamCacheToFileShouldBeDeletedInCaseOfResponse ()
specifier|public
name|void
name|testStreamCacheToFileShouldBeDeletedInCaseOfResponse
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|out
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"http://localhost:{{port}}/myserver"
argument_list|,
name|body
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|body2
argument_list|,
name|out
argument_list|)
expr_stmt|;
comment|// give time for file to be deleted
name|Thread
operator|.
name|sleep
argument_list|(
literal|500
argument_list|)
expr_stmt|;
comment|// the temporary files should have been deleted
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
name|assertEquals
argument_list|(
literal|"There should be no files"
argument_list|,
literal|0
argument_list|,
name|files
operator|.
name|length
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
comment|// enable stream caching and use a low threshold so its forced
comment|// to write to file
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
name|setStreamCaching
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"jetty://http://localhost:{{port}}/myserver"
argument_list|)
comment|// wrap the response in 2 input streams so it will force
comment|// caching to disk
operator|.
name|transform
argument_list|()
operator|.
name|constant
argument_list|(
operator|new
name|BufferedInputStream
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|body2
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:reply"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

