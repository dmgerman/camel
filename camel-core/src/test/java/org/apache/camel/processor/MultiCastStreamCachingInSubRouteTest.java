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
name|IOException
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
name|nio
operator|.
name|charset
operator|.
name|Charset
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
name|converter
operator|.
name|stream
operator|.
name|CachedOutputStream
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
name|processor
operator|.
name|aggregate
operator|.
name|AggregationStrategy
import|;
end_import

begin_class
DECL|class|MultiCastStreamCachingInSubRouteTest
specifier|public
class|class
name|MultiCastStreamCachingInSubRouteTest
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
literal|1L
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|multicast
argument_list|(
operator|new
name|InternalAggregationStrategy
argument_list|()
argument_list|)
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
literal|"direct:startNestedMultiCast"
argument_list|)
operator|.
name|multicast
argument_list|(
operator|new
name|InternalAggregationStrategy
argument_list|()
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|end
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:resultNested"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:a"
argument_list|)
comment|//
operator|.
name|process
argument_list|(
operator|new
name|InputProcessorWithStreamCache
argument_list|(
literal|1
argument_list|)
argument_list|)
comment|//
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
operator|.
name|process
argument_list|(
operator|new
name|InputProcessorWithStreamCache
argument_list|(
literal|2
argument_list|)
argument_list|)
comment|//
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
DECL|method|testWithAggregationStrategyAndStreamCacheInSubRoute ()
specifier|public
name|void
name|testWithAggregationStrategyAndStreamCacheInSubRoute
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Test Message 1Test Message 2"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"<start></start>"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|testNestedMultiCastWithCachedStreamInAggregationStrategy ()
specifier|public
name|void
name|testNestedMultiCastWithCachedStreamInAggregationStrategy
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:resultNested"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Test Message 1Test Message 2"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:startNestedMultiCast"
argument_list|,
literal|"<start></start>"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|class|InputProcessorWithStreamCache
specifier|public
specifier|static
class|class
name|InputProcessorWithStreamCache
implements|implements
name|Processor
block|{
DECL|field|number
specifier|private
specifier|final
name|int
name|number
decl_stmt|;
DECL|method|InputProcessorWithStreamCache (int number)
specifier|public
name|InputProcessorWithStreamCache
parameter_list|(
name|int
name|number
parameter_list|)
block|{
name|this
operator|.
name|number
operator|=
name|number
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
name|CachedOutputStream
name|cos
init|=
operator|new
name|CachedOutputStream
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|String
name|s
init|=
literal|"Test Message "
operator|+
name|number
decl_stmt|;
name|cos
operator|.
name|write
argument_list|(
name|s
operator|.
name|getBytes
argument_list|(
name|Charset
operator|.
name|forName
argument_list|(
literal|"UTF-8"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|cos
operator|.
name|close
argument_list|()
expr_stmt|;
name|InputStream
name|is
init|=
operator|(
name|InputStream
operator|)
name|cos
operator|.
name|newStreamCache
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|InternalAggregationStrategy
specifier|public
specifier|static
class|class
name|InternalAggregationStrategy
implements|implements
name|AggregationStrategy
block|{
annotation|@
name|Override
DECL|method|aggregate (Exchange oldExchange, Exchange newExchange)
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
operator|==
literal|null
condition|)
block|{
return|return
name|newExchange
return|;
block|}
try|try
block|{
name|String
name|oldBody
init|=
name|oldExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|newBody
init|=
name|newExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|merged
init|=
name|oldBody
operator|+
name|newBody
decl_stmt|;
comment|//also do stream caching in the aggregation strategy
name|CachedOutputStream
name|cos
init|=
operator|new
name|CachedOutputStream
argument_list|(
name|newExchange
argument_list|)
decl_stmt|;
name|cos
operator|.
name|write
argument_list|(
name|merged
operator|.
name|getBytes
argument_list|(
literal|"UTF-8"
argument_list|)
argument_list|)
expr_stmt|;
name|cos
operator|.
name|close
argument_list|()
expr_stmt|;
name|oldExchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|cos
operator|.
name|newStreamCache
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|oldExchange
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
block|}
end_class

end_unit

