begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|stream
operator|.
name|StreamSource
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
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|SplitterStreamCacheTest
specifier|public
class|class
name|SplitterStreamCacheTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|TEST_FILE
specifier|private
specifier|static
specifier|final
name|String
name|TEST_FILE
init|=
literal|"org/apache/camel/converter/stream/test.xml"
decl_stmt|;
DECL|field|numMessages
specifier|protected
name|int
name|numMessages
init|=
literal|200
decl_stmt|;
annotation|@
name|Test
DECL|method|testSendStreamSource ()
specifier|public
name|void
name|testSendStreamSource
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|resultEndpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|resultEndpoint
operator|.
name|expectedMessageCount
argument_list|(
name|numMessages
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|c
init|=
literal|0
init|;
name|c
operator|<
name|numMessages
condition|;
name|c
operator|++
control|)
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:parallel"
argument_list|,
operator|new
name|StreamSource
argument_list|(
name|getTestFileStream
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
block|{
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
comment|// ensure stream is spooled to disk
name|context
operator|.
name|getStreamCachingStrategy
argument_list|()
operator|.
name|setSpoolDirectory
argument_list|(
literal|"target/tmp"
argument_list|)
expr_stmt|;
name|context
operator|.
name|getStreamCachingStrategy
argument_list|()
operator|.
name|setSpoolThreshold
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"seda:parallel?concurrentConsumers=5"
argument_list|)
operator|.
name|streamCaching
argument_list|()
operator|.
name|split
argument_list|(
name|xpath
argument_list|(
literal|"//person/city"
argument_list|)
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
DECL|method|getTestFileStream ()
specifier|protected
name|InputStream
name|getTestFileStream
parameter_list|()
block|{
name|InputStream
name|answer
init|=
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|TEST_FILE
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Should have found the file: "
operator|+
name|TEST_FILE
operator|+
literal|" on the classpath"
argument_list|,
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

