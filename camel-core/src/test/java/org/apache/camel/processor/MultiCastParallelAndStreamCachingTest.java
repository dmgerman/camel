begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

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
name|java
operator|.
name|io
operator|.
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Reader
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
name|sax
operator|.
name|SAXSource
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
name|StringSource
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
name|util
operator|.
name|IOHelper
import|;
end_import

begin_comment
comment|/**  * Tests the processing of a stream-cache in the multi-cast processor in the  * parallel processing mode.  */
end_comment

begin_class
DECL|class|MultiCastParallelAndStreamCachingTest
specifier|public
class|class
name|MultiCastParallelAndStreamCachingTest
extends|extends
name|ContextTestSupport
block|{
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
name|context
operator|.
name|setStreamCaching
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|context
operator|.
name|getStreamCachingStrategy
argument_list|()
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|context
operator|.
name|getStreamCachingStrategy
argument_list|()
operator|.
name|setSpoolDirectory
argument_list|(
literal|"target/camel/cache"
argument_list|)
expr_stmt|;
name|context
operator|.
name|getStreamCachingStrategy
argument_list|()
operator|.
name|setSpoolThreshold
argument_list|(
literal|5L
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|multicast
argument_list|()
operator|.
name|parallelProcessing
argument_list|()
operator|.
name|stopOnException
argument_list|()
operator|.
name|to
argument_list|(
literal|"direct:a"
argument_list|,
literal|"direct:b"
argument_list|)
operator|.
name|end
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:a"
argument_list|)
comment|//
comment|// read stream
operator|.
name|process
argument_list|(
operator|new
name|SimpleProcessor
argument_list|(
literal|false
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:resulta"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:b"
argument_list|)
comment|//
comment|// read stream concurrently, because of parallel processing
operator|.
name|process
argument_list|(
operator|new
name|SimpleProcessor
argument_list|(
literal|true
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:resultb"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|SimpleProcessor
specifier|private
specifier|static
class|class
name|SimpleProcessor
implements|implements
name|Processor
block|{
DECL|field|withSleepTime
specifier|private
specifier|final
name|boolean
name|withSleepTime
decl_stmt|;
DECL|method|SimpleProcessor (boolean withSleepTime)
name|SimpleProcessor
parameter_list|(
name|boolean
name|withSleepTime
parameter_list|)
block|{
name|this
operator|.
name|withSleepTime
operator|=
name|withSleepTime
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|process (Exchange exchange)
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
if|if
condition|(
name|withSleepTime
condition|)
block|{
comment|// simulate some processing in order to get easier concurrency effects
name|Thread
operator|.
name|sleep
argument_list|(
literal|900
argument_list|)
expr_stmt|;
block|}
name|Object
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
if|if
condition|(
name|body
operator|instanceof
name|InputStream
condition|)
block|{
name|ByteArrayOutputStream
name|output
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|IOHelper
operator|.
name|copy
argument_list|(
operator|(
name|InputStream
operator|)
name|body
argument_list|,
name|output
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|output
operator|.
name|toByteArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|body
operator|instanceof
name|Reader
condition|)
block|{
name|Reader
name|reader
init|=
operator|(
name|Reader
operator|)
name|body
decl_stmt|;
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
name|reader
operator|.
name|read
argument_list|()
init|;
name|i
operator|>
operator|-
literal|1
condition|;
name|i
operator|=
name|reader
operator|.
name|read
argument_list|()
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
operator|(
name|char
operator|)
name|i
argument_list|)
expr_stmt|;
block|}
name|reader
operator|.
name|close
argument_list|()
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|body
operator|instanceof
name|StreamSource
condition|)
block|{
name|StreamSource
name|ss
init|=
operator|(
name|StreamSource
operator|)
name|body
decl_stmt|;
if|if
condition|(
name|ss
operator|.
name|getInputStream
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|ByteArrayOutputStream
name|output
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|IOHelper
operator|.
name|copy
argument_list|(
name|ss
operator|.
name|getInputStream
argument_list|()
argument_list|,
name|output
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|output
operator|.
name|toByteArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|ss
operator|.
name|getReader
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Reader
name|reader
init|=
operator|(
name|Reader
operator|)
name|ss
operator|.
name|getReader
argument_list|()
decl_stmt|;
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
name|reader
operator|.
name|read
argument_list|()
init|;
name|i
operator|>
operator|-
literal|1
condition|;
name|i
operator|=
name|reader
operator|.
name|read
argument_list|()
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
operator|(
name|char
operator|)
name|i
argument_list|)
expr_stmt|;
block|}
name|reader
operator|.
name|close
argument_list|()
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"StreamSource without InputStream and without Reader not supported"
argument_list|)
throw|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Type "
operator|+
name|body
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|" not supported"
argument_list|)
throw|;
block|}
block|}
block|}
comment|/**      * Tests the ByteArrayInputStreamCache. The send byte array is transformed      * to a ByteArrayInputStreamCache before the multi-cast processor is called.      *       * @throws Exception      */
DECL|method|testByteArrayInputStreamCache ()
specifier|public
name|void
name|testByteArrayInputStreamCache
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:resulta"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"<start></start>"
argument_list|)
expr_stmt|;
name|mock
operator|=
name|getMockEndpoint
argument_list|(
literal|"mock:resultb"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"<start></start>"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
literal|"<start></start>"
operator|.
name|getBytes
argument_list|(
literal|"UTF-8"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
comment|/**      * Tests the FileInputStreamCache.      *       * The sent input stream is transformed to FileInputStreamCache before the      * multi-cast processor is called.      *       * @throws Exception      */
DECL|method|testFileInputStreamCache ()
specifier|public
name|void
name|testFileInputStreamCache
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:resulta"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"James,Guillaume,Hiram,Rob,Roman"
argument_list|)
expr_stmt|;
name|mock
operator|=
name|getMockEndpoint
argument_list|(
literal|"mock:resultb"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"James,Guillaume,Hiram,Rob,Roman"
argument_list|)
expr_stmt|;
name|InputStream
name|in
init|=
name|MultiCastParallelAndStreamCachingTest
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"org/apache/camel/processor/simple.txt"
argument_list|)
decl_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
name|in
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
comment|/**      * Tests the FileInputStreamCache.      *       * The sent input stream is transformed to InputStreamCache before the      * multi-cast processor is called.      *       * @throws Exception      */
DECL|method|testInputStreamCache ()
specifier|public
name|void
name|testInputStreamCache
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:resulta"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"A"
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mockb
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:resultb"
argument_list|)
decl_stmt|;
name|mockb
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"A"
argument_list|)
expr_stmt|;
name|InputStream
name|in
init|=
name|MultiCastParallelAndStreamCachingTest
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"org/apache/camel/processor/oneCharacter.txt"
argument_list|)
decl_stmt|;
comment|// The body is only one character. Therefore InputStreamCache is used for stream caching
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
name|in
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
comment|/**      * Tests the ReaderCache.      *       * The sent InputStreamReader is transformed to a ReaderCache before the      * multi-cast processor is called.      *       * @throws Exception      */
DECL|method|testReaderCache ()
specifier|public
name|void
name|testReaderCache
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|abcScharpS
init|=
literal|"ABC\u00DF"
decl_stmt|;
comment|// sharp-s
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:resulta"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
name|abcScharpS
argument_list|)
expr_stmt|;
name|mock
operator|=
name|getMockEndpoint
argument_list|(
literal|"mock:resultb"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
name|abcScharpS
argument_list|)
expr_stmt|;
name|InputStreamReader
name|isr
init|=
operator|new
name|InputStreamReader
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|abcScharpS
operator|.
name|getBytes
argument_list|(
literal|"ISO-8859-1"
argument_list|)
argument_list|)
argument_list|,
literal|"ISO-8859-1"
argument_list|)
decl_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
name|isr
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|testStreamSourceCacheWithInputStream ()
specifier|public
name|void
name|testStreamSourceCacheWithInputStream
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|input
init|=
literal|"<A>a</A>"
decl_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:resulta"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
name|input
argument_list|)
expr_stmt|;
name|mock
operator|=
name|getMockEndpoint
argument_list|(
literal|"mock:resultb"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
name|input
argument_list|)
expr_stmt|;
name|StreamSource
name|ss
init|=
operator|new
name|StreamSource
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|input
operator|.
name|getBytes
argument_list|(
literal|"UTF-8"
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
name|ss
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|testStreamSourceCacheWithReader ()
specifier|public
name|void
name|testStreamSourceCacheWithReader
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|input
init|=
literal|"ABC\u00DF"
decl_stmt|;
comment|// sharp-s
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:resulta"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
name|input
argument_list|)
expr_stmt|;
name|mock
operator|=
name|getMockEndpoint
argument_list|(
literal|"mock:resultb"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
name|input
argument_list|)
expr_stmt|;
name|InputStreamReader
name|isr
init|=
operator|new
name|InputStreamReader
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|input
operator|.
name|getBytes
argument_list|(
literal|"ISO-8859-1"
argument_list|)
argument_list|)
argument_list|,
literal|"ISO-8859-1"
argument_list|)
decl_stmt|;
name|StreamSource
name|ss
init|=
operator|new
name|StreamSource
argument_list|(
name|isr
argument_list|)
decl_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
name|ss
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|testSourceCache ()
specifier|public
name|void
name|testSourceCache
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|input
init|=
literal|"<A>a</A>"
decl_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:resulta"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
name|input
argument_list|)
expr_stmt|;
name|mock
operator|=
name|getMockEndpoint
argument_list|(
literal|"mock:resultb"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
name|input
argument_list|)
expr_stmt|;
name|StringSource
name|ss
init|=
operator|new
name|StringSource
argument_list|(
name|input
argument_list|)
decl_stmt|;
name|SAXSource
name|saxSource
init|=
operator|new
name|SAXSource
argument_list|(
name|SAXSource
operator|.
name|sourceToInputSource
argument_list|(
name|ss
argument_list|)
argument_list|)
decl_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
name|saxSource
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

