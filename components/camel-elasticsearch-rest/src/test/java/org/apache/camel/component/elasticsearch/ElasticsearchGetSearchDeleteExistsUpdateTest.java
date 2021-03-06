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
name|util
operator|.
name|HashMap
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
name|builder
operator|.
name|RouteBuilder
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
name|DocWriteResponse
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
name|delete
operator|.
name|DeleteRequest
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
name|delete
operator|.
name|DeleteResponse
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
name|get
operator|.
name|GetRequest
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
name|get
operator|.
name|GetResponse
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
name|index
operator|.
name|IndexRequest
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
name|MultiSearchRequest
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
name|MultiSearchResponse
operator|.
name|Item
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
name|search
operator|.
name|SearchHits
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
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|equalTo
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|notNullValue
import|;
end_import

begin_class
DECL|class|ElasticsearchGetSearchDeleteExistsUpdateTest
specifier|public
class|class
name|ElasticsearchGetSearchDeleteExistsUpdateTest
extends|extends
name|ElasticsearchBaseTest
block|{
annotation|@
name|Test
DECL|method|testGet ()
specifier|public
name|void
name|testGet
parameter_list|()
throws|throws
name|Exception
block|{
comment|//first, Index a value
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
name|sendBody
argument_list|(
literal|"direct:index"
argument_list|,
name|map
argument_list|)
expr_stmt|;
name|String
name|indexId
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:index"
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
comment|//now, verify GET succeeded
name|GetResponse
name|response
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:get"
argument_list|,
name|indexId
argument_list|,
name|GetResponse
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"response should not be null"
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"response source should not be null"
argument_list|,
name|response
operator|.
name|getSource
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDelete ()
specifier|public
name|void
name|testDelete
parameter_list|()
throws|throws
name|Exception
block|{
comment|//first, Index a value
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
name|sendBody
argument_list|(
literal|"direct:index"
argument_list|,
name|map
argument_list|)
expr_stmt|;
name|String
name|indexId
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:index"
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
comment|//now, verify GET succeeded
name|GetResponse
name|response
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:get"
argument_list|,
name|indexId
argument_list|,
name|GetResponse
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"response should not be null"
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"response source should not be null"
argument_list|,
name|response
operator|.
name|getSource
argument_list|()
argument_list|)
expr_stmt|;
comment|//now, perform Delete
name|DeleteResponse
operator|.
name|Result
name|deleteResponse
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:delete"
argument_list|,
name|indexId
argument_list|,
name|DeleteResponse
operator|.
name|Result
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"response should not be null"
argument_list|,
name|deleteResponse
argument_list|)
expr_stmt|;
comment|//now, verify GET fails to find the indexed value
name|response
operator|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:get"
argument_list|,
name|indexId
argument_list|,
name|GetResponse
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"response should not be null"
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
literal|"response source should be null"
argument_list|,
name|response
operator|.
name|getSource
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSearchWithMapQuery ()
specifier|public
name|void
name|testSearchWithMapQuery
parameter_list|()
throws|throws
name|Exception
block|{
comment|//first, Index a value
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
literal|"direct:index"
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
comment|//now, verify GET succeeded
name|GetResponse
name|getResponse
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:get"
argument_list|,
name|indexId
argument_list|,
name|GetResponse
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"response should not be null"
argument_list|,
name|getResponse
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"response source should not be null"
argument_list|,
name|getResponse
operator|.
name|getSource
argument_list|()
argument_list|)
expr_stmt|;
comment|//now, verify GET succeeded
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|actualQuery
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|actualQuery
operator|.
name|put
argument_list|(
literal|"testsearchwithmapquery-key"
argument_list|,
literal|"testsearchwithmapquery-value"
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|match
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|match
operator|.
name|put
argument_list|(
literal|"match"
argument_list|,
name|actualQuery
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|query
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|query
operator|.
name|put
argument_list|(
literal|"query"
argument_list|,
name|match
argument_list|)
expr_stmt|;
name|SearchHits
name|response
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:search"
argument_list|,
name|query
argument_list|,
name|SearchHits
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"response should not be null"
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"response hits should be == 0"
argument_list|,
literal|0
argument_list|,
name|response
operator|.
name|getTotalHits
argument_list|()
operator|.
name|value
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSearchWithStringQuery ()
specifier|public
name|void
name|testSearchWithStringQuery
parameter_list|()
throws|throws
name|Exception
block|{
comment|//first, Index a value
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
literal|"direct:index"
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
comment|//now, verify GET succeeded
name|GetResponse
name|getResponse
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:get"
argument_list|,
name|indexId
argument_list|,
name|GetResponse
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"response should not be null"
argument_list|,
name|getResponse
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"response source should not be null"
argument_list|,
name|getResponse
operator|.
name|getSource
argument_list|()
argument_list|)
expr_stmt|;
comment|// need to create a query string
name|String
name|query
init|=
literal|"{\n"
operator|+
literal|"    \"query\" : { \"match\" : { \"key\" : \"value\" }}\n"
operator|+
literal|"}\n"
decl_stmt|;
name|SearchHits
name|response
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:search"
argument_list|,
name|query
argument_list|,
name|SearchHits
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"response should not be null"
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"response hits should be == 0"
argument_list|,
literal|0
argument_list|,
name|response
operator|.
name|getTotalHits
argument_list|()
operator|.
name|value
argument_list|)
expr_stmt|;
comment|// testing
name|response
operator|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:search-1"
argument_list|,
name|query
argument_list|,
name|SearchHits
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"response should not be null"
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"response hits should be == 0"
argument_list|,
literal|0
argument_list|,
name|response
operator|.
name|getTotalHits
argument_list|()
operator|.
name|value
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMultiSearch ()
specifier|public
name|void
name|testMultiSearch
parameter_list|()
throws|throws
name|Exception
block|{
comment|//first, Index a value
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
literal|"direct:index"
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
comment|//now, verify GET succeeded
name|GetResponse
name|getResponse
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:get"
argument_list|,
name|indexId
argument_list|,
name|GetResponse
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"response should not be null"
argument_list|,
name|getResponse
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"response source should not be null"
argument_list|,
name|getResponse
operator|.
name|getSource
argument_list|()
argument_list|)
expr_stmt|;
comment|//now, verify GET succeeded
name|SearchRequest
name|req
init|=
operator|new
name|SearchRequest
argument_list|()
decl_stmt|;
name|req
operator|.
name|indices
argument_list|(
literal|"twitter"
argument_list|)
expr_stmt|;
name|req
operator|.
name|types
argument_list|(
literal|"tweet"
argument_list|)
expr_stmt|;
name|SearchRequest
name|req1
init|=
operator|new
name|SearchRequest
argument_list|()
decl_stmt|;
name|req
operator|.
name|indices
argument_list|(
literal|"twitter"
argument_list|)
expr_stmt|;
name|req
operator|.
name|types
argument_list|(
literal|"tweets"
argument_list|)
expr_stmt|;
name|MultiSearchRequest
name|request
init|=
operator|new
name|MultiSearchRequest
argument_list|()
operator|.
name|add
argument_list|(
name|req1
argument_list|)
operator|.
name|add
argument_list|(
name|req
argument_list|)
decl_stmt|;
name|Item
index|[]
name|response
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:search"
argument_list|,
name|request
argument_list|,
name|Item
index|[]
operator|.
expr|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"response should not be null"
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"response should be == 2"
argument_list|,
literal|2
argument_list|,
name|response
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUpdate ()
specifier|public
name|void
name|testUpdate
parameter_list|()
throws|throws
name|Exception
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
literal|"direct:index"
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
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|newMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|newMap
operator|.
name|put
argument_list|(
name|createPrefix
argument_list|()
operator|+
literal|"key2"
argument_list|,
name|createPrefix
argument_list|()
operator|+
literal|"value2"
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|ElasticsearchConstants
operator|.
name|PARAM_INDEX_ID
argument_list|,
name|indexId
argument_list|)
expr_stmt|;
name|indexId
operator|=
name|template
operator|.
name|requestBodyAndHeaders
argument_list|(
literal|"direct:update"
argument_list|,
name|newMap
argument_list|,
name|headers
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"indexId should be set"
argument_list|,
name|indexId
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetWithHeaders ()
specifier|public
name|void
name|testGetWithHeaders
parameter_list|()
throws|throws
name|Exception
block|{
comment|//first, Index a value
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
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|ElasticsearchConstants
operator|.
name|PARAM_OPERATION
argument_list|,
name|ElasticsearchOperation
operator|.
name|Index
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|ElasticsearchConstants
operator|.
name|PARAM_INDEX_NAME
argument_list|,
literal|"twitter"
argument_list|)
expr_stmt|;
name|String
name|indexId
init|=
name|template
operator|.
name|requestBodyAndHeaders
argument_list|(
literal|"direct:start"
argument_list|,
name|map
argument_list|,
name|headers
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
comment|//now, verify GET
name|headers
operator|.
name|put
argument_list|(
name|ElasticsearchConstants
operator|.
name|PARAM_OPERATION
argument_list|,
name|ElasticsearchOperation
operator|.
name|GetById
argument_list|)
expr_stmt|;
name|GetResponse
name|response
init|=
name|template
operator|.
name|requestBodyAndHeaders
argument_list|(
literal|"direct:start"
argument_list|,
name|indexId
argument_list|,
name|headers
argument_list|,
name|GetResponse
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"response should not be null"
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"response source should not be null"
argument_list|,
name|response
operator|.
name|getSource
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testExistsWithHeaders ()
specifier|public
name|void
name|testExistsWithHeaders
parameter_list|()
throws|throws
name|Exception
block|{
comment|//first, Index a value
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
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|ElasticsearchConstants
operator|.
name|PARAM_OPERATION
argument_list|,
name|ElasticsearchOperation
operator|.
name|Index
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|ElasticsearchConstants
operator|.
name|PARAM_INDEX_NAME
argument_list|,
literal|"twitter"
argument_list|)
expr_stmt|;
name|template
operator|.
name|requestBodyAndHeaders
argument_list|(
literal|"direct:start"
argument_list|,
name|map
argument_list|,
name|headers
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
comment|//now, verify GET
name|headers
operator|.
name|put
argument_list|(
name|ElasticsearchConstants
operator|.
name|PARAM_OPERATION
argument_list|,
name|ElasticsearchOperation
operator|.
name|Exists
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|ElasticsearchConstants
operator|.
name|PARAM_INDEX_NAME
argument_list|,
literal|"twitter"
argument_list|)
expr_stmt|;
name|Boolean
name|exists
init|=
name|template
operator|.
name|requestBodyAndHeaders
argument_list|(
literal|"direct:exists"
argument_list|,
literal|""
argument_list|,
name|headers
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"response should not be null"
argument_list|,
name|exists
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Index should exists"
argument_list|,
name|exists
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNotExistsWithHeaders ()
specifier|public
name|void
name|testNotExistsWithHeaders
parameter_list|()
throws|throws
name|Exception
block|{
comment|//first, Index a value
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
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|ElasticsearchConstants
operator|.
name|PARAM_OPERATION
argument_list|,
name|ElasticsearchOperation
operator|.
name|Index
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|ElasticsearchConstants
operator|.
name|PARAM_INDEX_NAME
argument_list|,
literal|"twitter"
argument_list|)
expr_stmt|;
name|template
operator|.
name|requestBodyAndHeaders
argument_list|(
literal|"direct:start"
argument_list|,
name|map
argument_list|,
name|headers
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
comment|//now, verify GET
name|headers
operator|.
name|put
argument_list|(
name|ElasticsearchConstants
operator|.
name|PARAM_OPERATION
argument_list|,
name|ElasticsearchOperation
operator|.
name|Exists
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|ElasticsearchConstants
operator|.
name|PARAM_INDEX_NAME
argument_list|,
literal|"twitter-tweet"
argument_list|)
expr_stmt|;
name|Boolean
name|exists
init|=
name|template
operator|.
name|requestBodyAndHeaders
argument_list|(
literal|"direct:exists"
argument_list|,
literal|""
argument_list|,
name|headers
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"response should not be null"
argument_list|,
name|exists
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Index should not exists"
argument_list|,
name|exists
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDeleteWithHeaders ()
specifier|public
name|void
name|testDeleteWithHeaders
parameter_list|()
throws|throws
name|Exception
block|{
comment|//first, Index a value
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
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|ElasticsearchConstants
operator|.
name|PARAM_OPERATION
argument_list|,
name|ElasticsearchOperation
operator|.
name|Index
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|ElasticsearchConstants
operator|.
name|PARAM_INDEX_NAME
argument_list|,
literal|"twitter"
argument_list|)
expr_stmt|;
name|String
name|indexId
init|=
name|template
operator|.
name|requestBodyAndHeaders
argument_list|(
literal|"direct:start"
argument_list|,
name|map
argument_list|,
name|headers
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
comment|//now, verify GET
name|headers
operator|.
name|put
argument_list|(
name|ElasticsearchConstants
operator|.
name|PARAM_OPERATION
argument_list|,
name|ElasticsearchOperation
operator|.
name|GetById
argument_list|)
expr_stmt|;
name|GetResponse
name|response
init|=
name|template
operator|.
name|requestBodyAndHeaders
argument_list|(
literal|"direct:start"
argument_list|,
name|indexId
argument_list|,
name|headers
argument_list|,
name|GetResponse
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"response should not be null"
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"response source should not be null"
argument_list|,
name|response
operator|.
name|getSource
argument_list|()
argument_list|)
expr_stmt|;
comment|//now, perform Delete
name|headers
operator|.
name|put
argument_list|(
name|ElasticsearchConstants
operator|.
name|PARAM_OPERATION
argument_list|,
name|ElasticsearchOperation
operator|.
name|Delete
argument_list|)
expr_stmt|;
name|DocWriteResponse
operator|.
name|Result
name|deleteResponse
init|=
name|template
operator|.
name|requestBodyAndHeaders
argument_list|(
literal|"direct:start"
argument_list|,
name|indexId
argument_list|,
name|headers
argument_list|,
name|DocWriteResponse
operator|.
name|Result
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"response should not be null"
argument_list|,
name|DocWriteResponse
operator|.
name|Result
operator|.
name|DELETED
argument_list|,
name|deleteResponse
argument_list|)
expr_stmt|;
comment|//now, verify GET fails to find the indexed value
name|headers
operator|.
name|put
argument_list|(
name|ElasticsearchConstants
operator|.
name|PARAM_OPERATION
argument_list|,
name|ElasticsearchOperation
operator|.
name|GetById
argument_list|)
expr_stmt|;
name|response
operator|=
name|template
operator|.
name|requestBodyAndHeaders
argument_list|(
literal|"direct:start"
argument_list|,
name|indexId
argument_list|,
name|headers
argument_list|,
name|GetResponse
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"response should not be null"
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
literal|"response source should be null"
argument_list|,
name|response
operator|.
name|getSource
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUpdateWithIDInHeader ()
specifier|public
name|void
name|testUpdateWithIDInHeader
parameter_list|()
throws|throws
name|Exception
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
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|ElasticsearchConstants
operator|.
name|PARAM_OPERATION
argument_list|,
name|ElasticsearchOperation
operator|.
name|Index
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|ElasticsearchConstants
operator|.
name|PARAM_INDEX_NAME
argument_list|,
literal|"twitter"
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|ElasticsearchConstants
operator|.
name|PARAM_INDEX_ID
argument_list|,
literal|"123"
argument_list|)
expr_stmt|;
name|String
name|indexId
init|=
name|template
operator|.
name|requestBodyAndHeaders
argument_list|(
literal|"direct:start"
argument_list|,
name|map
argument_list|,
name|headers
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
name|assertEquals
argument_list|(
literal|"indexId should be equals to the provided id"
argument_list|,
literal|"123"
argument_list|,
name|indexId
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|ElasticsearchConstants
operator|.
name|PARAM_OPERATION
argument_list|,
name|ElasticsearchOperation
operator|.
name|Update
argument_list|)
expr_stmt|;
name|indexId
operator|=
name|template
operator|.
name|requestBodyAndHeaders
argument_list|(
literal|"direct:start"
argument_list|,
name|map
argument_list|,
name|headers
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"indexId should be set"
argument_list|,
name|indexId
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"indexId should be equals to the provided id"
argument_list|,
literal|"123"
argument_list|,
name|indexId
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|getRequestBody ()
specifier|public
name|void
name|getRequestBody
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|prefix
init|=
name|createPrefix
argument_list|()
decl_stmt|;
comment|// given
name|GetRequest
name|request
init|=
operator|new
name|GetRequest
argument_list|(
name|prefix
operator|+
literal|"foo"
argument_list|)
operator|.
name|type
argument_list|(
name|prefix
operator|+
literal|"bar"
argument_list|)
decl_stmt|;
comment|// when
name|String
name|documentId
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:index"
argument_list|,
operator|new
name|IndexRequest
argument_list|(
name|prefix
operator|+
literal|"foo"
argument_list|,
name|prefix
operator|+
literal|"bar"
argument_list|,
name|prefix
operator|+
literal|"testId"
argument_list|)
operator|.
name|source
argument_list|(
name|prefix
operator|+
literal|"content"
argument_list|,
name|prefix
operator|+
literal|"hello"
argument_list|)
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|GetResponse
name|response
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:get"
argument_list|,
name|request
operator|.
name|id
argument_list|(
name|documentId
argument_list|)
argument_list|,
name|GetResponse
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// then
name|assertThat
argument_list|(
name|response
argument_list|,
name|notNullValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|prefix
operator|+
literal|"hello"
argument_list|,
name|equalTo
argument_list|(
name|response
operator|.
name|getSourceAsMap
argument_list|()
operator|.
name|get
argument_list|(
name|prefix
operator|+
literal|"content"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|deleteRequestBody ()
specifier|public
name|void
name|deleteRequestBody
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|prefix
init|=
name|createPrefix
argument_list|()
decl_stmt|;
comment|// given
name|DeleteRequest
name|request
init|=
operator|new
name|DeleteRequest
argument_list|(
name|prefix
operator|+
literal|"foo"
argument_list|)
operator|.
name|type
argument_list|(
name|prefix
operator|+
literal|"bar"
argument_list|)
decl_stmt|;
comment|// when
name|String
name|documentId
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:index"
argument_list|,
operator|new
name|IndexRequest
argument_list|(
literal|""
operator|+
name|prefix
operator|+
literal|"foo"
argument_list|,
literal|""
operator|+
name|prefix
operator|+
literal|"bar"
argument_list|,
literal|""
operator|+
name|prefix
operator|+
literal|"testId"
argument_list|)
operator|.
name|source
argument_list|(
name|prefix
operator|+
literal|"content"
argument_list|,
name|prefix
operator|+
literal|"hello"
argument_list|)
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|DeleteResponse
operator|.
name|Result
name|response
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:delete"
argument_list|,
name|request
operator|.
name|id
argument_list|(
name|documentId
argument_list|)
argument_list|,
name|DeleteResponse
operator|.
name|Result
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// then
name|assertThat
argument_list|(
name|response
argument_list|,
name|notNullValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testStringUpdate ()
specifier|public
name|void
name|testStringUpdate
parameter_list|()
throws|throws
name|Exception
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
literal|"direct:index"
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
name|String
name|body
init|=
literal|"{\"teststringupdate-key\" : \"teststringupdate-updated\"}"
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|ElasticsearchConstants
operator|.
name|PARAM_INDEX_ID
argument_list|,
name|indexId
argument_list|)
expr_stmt|;
name|indexId
operator|=
name|template
operator|.
name|requestBodyAndHeaders
argument_list|(
literal|"direct:update"
argument_list|,
name|body
argument_list|,
name|headers
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"indexId should be set"
argument_list|,
name|indexId
argument_list|)
expr_stmt|;
name|GetResponse
name|response
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:get"
argument_list|,
name|indexId
argument_list|,
name|GetResponse
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"teststringupdate-updated"
argument_list|,
name|response
operator|.
name|getSource
argument_list|()
operator|.
name|get
argument_list|(
literal|"teststringupdate-key"
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
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"elasticsearch-rest://elasticsearch?operation=Index&hostAddresses=localhost:"
operator|+
name|ES_BASE_HTTP_PORT
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:index"
argument_list|)
operator|.
name|to
argument_list|(
literal|"elasticsearch-rest://elasticsearch?operation=Index&indexName=twitter&hostAddresses=localhost:"
operator|+
name|ES_BASE_HTTP_PORT
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:get"
argument_list|)
operator|.
name|to
argument_list|(
literal|"elasticsearch-rest://elasticsearch?operation=GetById&indexName=twitter&hostAddresses=localhost:"
operator|+
name|ES_BASE_HTTP_PORT
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:multiget"
argument_list|)
operator|.
name|to
argument_list|(
literal|"elasticsearch-rest://elasticsearch?operation=MultiGet&indexName=twitter&hostAddresses=localhost:"
operator|+
name|ES_BASE_HTTP_PORT
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:delete"
argument_list|)
operator|.
name|to
argument_list|(
literal|"elasticsearch-rest://elasticsearch?operation=Delete&indexName=twitter&hostAddresses=localhost:"
operator|+
name|ES_BASE_HTTP_PORT
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:search"
argument_list|)
operator|.
name|to
argument_list|(
literal|"elasticsearch-rest://elasticsearch?operation=Search&indexName=twitter&hostAddresses=localhost:"
operator|+
name|ES_BASE_HTTP_PORT
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:search-1"
argument_list|)
operator|.
name|to
argument_list|(
literal|"elasticsearch-rest://elasticsearch?operation=Search&hostAddresses=localhost:"
operator|+
name|ES_BASE_HTTP_PORT
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:multiSearch"
argument_list|)
operator|.
name|to
argument_list|(
literal|"elasticsearch-rest://elasticsearch?operation=MultiSearch&hostAddresses=localhost:"
operator|+
name|ES_BASE_HTTP_PORT
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:update"
argument_list|)
operator|.
name|to
argument_list|(
literal|"elasticsearch-rest://elasticsearch?operation=Update&indexName=twitter&hostAddresses=localhost:"
operator|+
name|ES_BASE_HTTP_PORT
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:exists"
argument_list|)
operator|.
name|to
argument_list|(
literal|"elasticsearch-rest://elasticsearch?operation=Exists&hostAddresses=localhost:"
operator|+
name|ES_BASE_HTTP_PORT
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

