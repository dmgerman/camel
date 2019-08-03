begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.tarfile
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|tarfile
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|AggregationStrategy
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
DECL|class|TarFileMultipleFilesSplitterTest
specifier|public
class|class
name|TarFileMultipleFilesSplitterTest
extends|extends
name|TarSplitterRouteTest
block|{
DECL|field|PROCESSED_FILES_HEADER_NAME
specifier|static
specifier|final
name|String
name|PROCESSED_FILES_HEADER_NAME
init|=
literal|"processedFiles"
decl_stmt|;
annotation|@
name|Override
annotation|@
name|Test
DECL|method|testSplitter ()
specifier|public
name|void
name|testSplitter
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|MockEndpoint
name|processTarEntry
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:processTarEntry"
argument_list|)
decl_stmt|;
name|MockEndpoint
name|splitResult
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:splitResult"
argument_list|)
decl_stmt|;
name|processTarEntry
operator|.
name|expectedBodiesReceivedInAnyOrder
argument_list|(
literal|"chau"
argument_list|,
literal|"hi"
argument_list|,
literal|"hola"
argument_list|,
literal|"hello"
argument_list|,
literal|"greetings"
argument_list|)
expr_stmt|;
name|splitResult
operator|.
name|expectedBodiesReceivedInAnyOrder
argument_list|(
literal|"chiau.txt"
argument_list|,
literal|"hi.txt"
argument_list|,
literal|"hola.txt"
argument_list|,
literal|"another/hello.txt"
argument_list|,
literal|"other/greetings.txt"
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
comment|// Untar file and Split it according to FileEntry
name|TarFileDataFormat
name|tarFile
init|=
operator|new
name|TarFileDataFormat
argument_list|()
decl_stmt|;
name|tarFile
operator|.
name|setUsingIterator
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"file:src/test/resources/org/apache/camel/dataformat/tarfile/data/?consumer.delay=1000&noop=true"
argument_list|)
operator|.
name|unmarshal
argument_list|(
name|tarFile
argument_list|)
operator|.
name|split
argument_list|(
name|bodyAs
argument_list|(
name|Iterator
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|streaming
argument_list|()
operator|.
name|aggregationStrategy
argument_list|(
name|updateHeader
argument_list|()
argument_list|)
operator|.
name|convertBodyTo
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:processTarEntry"
argument_list|)
operator|.
name|end
argument_list|()
operator|.
name|log
argument_list|(
literal|"Done processing big file: ${header.CamelFileName}"
argument_list|)
operator|.
name|setBody
argument_list|()
operator|.
name|header
argument_list|(
name|PROCESSED_FILES_HEADER_NAME
argument_list|)
operator|.
name|split
argument_list|()
operator|.
name|body
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:splitResult"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|updateHeader ()
specifier|private
name|AggregationStrategy
name|updateHeader
parameter_list|()
block|{
return|return
operator|new
name|AggregationStrategy
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Exchange
name|aggregate
parameter_list|(
name|Exchange
name|oldExchange
parameter_list|,
name|Exchange
name|newExchange
parameter_list|)
block|{
if|if
condition|(
name|oldExchange
operator|!=
literal|null
condition|)
block|{
name|String
name|processedFiles
init|=
name|oldExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|PROCESSED_FILES_HEADER_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|processedFiles
operator|==
literal|null
condition|)
block|{
name|processedFiles
operator|=
name|oldExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|TarIterator
operator|.
name|TARFILE_ENTRY_NAME_HEADER
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
name|processedFiles
operator|=
name|processedFiles
operator|+
literal|","
operator|+
name|newExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|TarIterator
operator|.
name|TARFILE_ENTRY_NAME_HEADER
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|newExchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|PROCESSED_FILES_HEADER_NAME
argument_list|,
name|processedFiles
argument_list|)
expr_stmt|;
block|}
return|return
name|newExchange
return|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

