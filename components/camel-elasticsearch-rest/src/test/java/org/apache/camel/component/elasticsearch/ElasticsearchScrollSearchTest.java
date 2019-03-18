begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.elasticsearch
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|elasticsearch
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
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|AggregationStrategies
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
name|ExchangeBuilder
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
name|elasticsearch
operator|.
name|action
operator|.
name|search
operator|.
name|SearchRequest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|elasticsearch
operator|.
name|client
operator|.
name|Request
import|;
end_import

begin_import
import|import
name|org
operator|.
name|elasticsearch
operator|.
name|client
operator|.
name|Response
import|;
end_import

begin_import
import|import
name|org
operator|.
name|elasticsearch
operator|.
name|index
operator|.
name|query
operator|.
name|QueryBuilders
import|;
end_import

begin_import
import|import
name|org
operator|.
name|elasticsearch
operator|.
name|search
operator|.
name|builder
operator|.
name|SearchSourceBuilder
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

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|elasticsearch
operator|.
name|ElasticsearchConstants
operator|.
name|*
import|;
end_import

begin_class
DECL|class|ElasticsearchScrollSearchTest
specifier|public
class|class
name|ElasticsearchScrollSearchTest
extends|extends
name|ElasticsearchBaseTest
block|{
DECL|field|TWITTER_ES_INDEX_NAME
specifier|private
specifier|static
specifier|final
name|String
name|TWITTER_ES_INDEX_NAME
init|=
literal|"twitter"
decl_stmt|;
DECL|field|SPLIT_TWITTER_ES_INDEX_NAME
specifier|private
specifier|static
specifier|final
name|String
name|SPLIT_TWITTER_ES_INDEX_NAME
init|=
literal|"split-"
operator|+
name|TWITTER_ES_INDEX_NAME
decl_stmt|;
annotation|@
name|Test
DECL|method|testScrollSearch ()
specifier|public
name|void
name|testScrollSearch
parameter_list|()
throws|throws
name|IOException
block|{
comment|// add some documents
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|10
condition|;
name|i
operator|++
control|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
name|createIndexedData
argument_list|()
decl_stmt|;
name|String
name|indexId
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:scroll-index"
argument_list|,
name|map
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"indexId should be set"
argument_list|,
name|indexId
argument_list|)
expr_stmt|;
block|}
comment|// perform a refresh
name|Response
name|refreshResponse
init|=
name|getClient
argument_list|()
operator|.
name|performRequest
argument_list|(
operator|new
name|Request
argument_list|(
literal|"post"
argument_list|,
literal|"/"
operator|+
name|TWITTER_ES_INDEX_NAME
operator|+
literal|"/_refresh"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Cannot perform a refresh"
argument_list|,
literal|200
argument_list|,
name|refreshResponse
operator|.
name|getStatusLine
argument_list|()
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
name|SearchRequest
name|req
init|=
name|getScrollSearchRequest
argument_list|(
name|TWITTER_ES_INDEX_NAME
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
name|ExchangeBuilder
operator|.
name|anExchange
argument_list|(
name|context
argument_list|)
operator|.
name|withHeader
argument_list|(
name|PARAM_SCROLL_KEEP_ALIVE_MS
argument_list|,
literal|50000
argument_list|)
operator|.
name|withHeader
argument_list|(
name|PARAM_SCROLL
argument_list|,
literal|true
argument_list|)
operator|.
name|withBody
argument_list|(
name|req
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|exchange
operator|=
name|template
operator|.
name|send
argument_list|(
literal|"direct:scroll-search"
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
try|try
init|(
name|ElasticsearchScrollRequestIterator
name|scrollRequestIterator
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|ElasticsearchScrollRequestIterator
operator|.
name|class
argument_list|)
init|)
block|{
name|assertNotNull
argument_list|(
literal|"response should not be null"
argument_list|,
name|scrollRequestIterator
argument_list|)
expr_stmt|;
name|List
name|result
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|scrollRequestIterator
operator|.
name|forEachRemaining
argument_list|(
name|result
operator|::
name|add
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"response hits should be == 10"
argument_list|,
literal|10
argument_list|,
name|result
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"11 request should have been send to Elasticsearch"
argument_list|,
literal|11
argument_list|,
name|scrollRequestIterator
operator|.
name|getRequestCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|ElasticsearchScrollRequestIterator
name|scrollRequestIterator
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|ElasticsearchScrollRequestIterator
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"iterator should be closed"
argument_list|,
name|scrollRequestIterator
operator|.
name|isClosed
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"11 request should have been send to Elasticsearch"
argument_list|,
literal|11
argument_list|,
operator|(
name|int
operator|)
name|exchange
operator|.
name|getProperty
argument_list|(
name|PROPERTY_SCROLL_ES_QUERY_COUNT
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testScrollAndSplitSearch ()
specifier|public
name|void
name|testScrollAndSplitSearch
parameter_list|()
throws|throws
name|IOException
throws|,
name|InterruptedException
block|{
comment|// add some documents
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|10
condition|;
name|i
operator|++
control|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
name|createIndexedData
argument_list|()
decl_stmt|;
name|String
name|indexId
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:scroll-n-split-index"
argument_list|,
name|map
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"indexId should be set"
argument_list|,
name|indexId
argument_list|)
expr_stmt|;
block|}
comment|// perform a refresh
name|Response
name|refreshResponse
init|=
name|getClient
argument_list|()
operator|.
name|performRequest
argument_list|(
operator|new
name|Request
argument_list|(
literal|"post"
argument_list|,
literal|"/"
operator|+
name|SPLIT_TWITTER_ES_INDEX_NAME
operator|+
literal|"/_refresh"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Cannot perform a refresh"
argument_list|,
literal|200
argument_list|,
name|refreshResponse
operator|.
name|getStatusLine
argument_list|()
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:output"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|setResultWaitTime
argument_list|(
literal|8000
argument_list|)
expr_stmt|;
name|SearchRequest
name|req
init|=
name|getScrollSearchRequest
argument_list|(
name|SPLIT_TWITTER_ES_INDEX_NAME
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
name|ExchangeBuilder
operator|.
name|anExchange
argument_list|(
name|context
argument_list|)
operator|.
name|withBody
argument_list|(
name|req
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|exchange
operator|=
name|template
operator|.
name|send
argument_list|(
literal|"direct:scroll-n-split-search"
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
comment|// wait for aggregation
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|Iterator
argument_list|<
name|Exchange
argument_list|>
name|iterator
init|=
name|mock
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"response should contain 1 exchange"
argument_list|,
name|iterator
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|Collection
name|aggregatedExchanges
init|=
name|iterator
operator|.
name|next
argument_list|()
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|Collection
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"response hits should be == 10"
argument_list|,
literal|10
argument_list|,
name|aggregatedExchanges
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ElasticsearchScrollRequestIterator
name|scrollRequestIterator
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|ElasticsearchScrollRequestIterator
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"iterator should be closed"
argument_list|,
name|scrollRequestIterator
operator|.
name|isClosed
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"11 request should have been send to Elasticsearch"
argument_list|,
literal|11
argument_list|,
name|scrollRequestIterator
operator|.
name|getRequestCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"11 request should have been send to Elasticsearch"
argument_list|,
literal|11
argument_list|,
operator|(
name|int
operator|)
name|exchange
operator|.
name|getProperty
argument_list|(
name|PROPERTY_SCROLL_ES_QUERY_COUNT
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|getScrollSearchRequest (String indexName)
specifier|private
name|SearchRequest
name|getScrollSearchRequest
parameter_list|(
name|String
name|indexName
parameter_list|)
block|{
name|SearchRequest
name|req
init|=
operator|new
name|SearchRequest
argument_list|()
operator|.
name|indices
argument_list|(
name|indexName
argument_list|)
operator|.
name|types
argument_list|(
literal|"tweet"
argument_list|)
decl_stmt|;
name|SearchSourceBuilder
name|searchSourceBuilder
init|=
operator|new
name|SearchSourceBuilder
argument_list|()
decl_stmt|;
name|searchSourceBuilder
operator|.
name|query
argument_list|(
name|QueryBuilders
operator|.
name|matchAllQuery
argument_list|()
argument_list|)
operator|.
name|size
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|req
operator|.
name|source
argument_list|(
name|searchSourceBuilder
argument_list|)
expr_stmt|;
return|return
name|req
return|;
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
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct:scroll-index"
argument_list|)
operator|.
name|to
argument_list|(
literal|"elasticsearch-rest://elasticsearch?operation=Index&indexName="
operator|+
name|TWITTER_ES_INDEX_NAME
operator|+
literal|"&indexType=tweet&hostAddresses=localhost:"
operator|+
name|ES_BASE_HTTP_PORT
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:scroll-search"
argument_list|)
operator|.
name|to
argument_list|(
literal|"elasticsearch-rest://elasticsearch?operation=Search&indexName="
operator|+
name|TWITTER_ES_INDEX_NAME
operator|+
literal|"&indexType=tweet&hostAddresses=localhost:"
operator|+
name|ES_BASE_HTTP_PORT
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:scroll-n-split-index"
argument_list|)
operator|.
name|to
argument_list|(
literal|"elasticsearch-rest://elasticsearch?operation=Index&indexName="
operator|+
name|SPLIT_TWITTER_ES_INDEX_NAME
operator|+
literal|"&indexType=tweet&hostAddresses=localhost:"
operator|+
name|ES_BASE_HTTP_PORT
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:scroll-n-split-search"
argument_list|)
operator|.
name|to
argument_list|(
literal|"elasticsearch-rest://elasticsearch?"
operator|+
literal|"useScroll=true&scrollKeepAliveMs=50000&operation=Search&indexName="
operator|+
name|SPLIT_TWITTER_ES_INDEX_NAME
operator|+
literal|"&indexType=tweet&hostAddresses=localhost:"
operator|+
name|ES_BASE_HTTP_PORT
argument_list|)
operator|.
name|split
argument_list|()
operator|.
name|body
argument_list|()
operator|.
name|streaming
argument_list|()
operator|.
name|parallelProcessing
argument_list|()
operator|.
name|threads
argument_list|(
literal|12
argument_list|)
operator|.
name|aggregate
argument_list|(
name|AggregationStrategies
operator|.
name|groupedExchange
argument_list|()
argument_list|)
operator|.
name|constant
argument_list|(
literal|true
argument_list|)
operator|.
name|completionSize
argument_list|(
literal|20
argument_list|)
operator|.
name|completionTimeout
argument_list|(
literal|2000
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:output"
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

