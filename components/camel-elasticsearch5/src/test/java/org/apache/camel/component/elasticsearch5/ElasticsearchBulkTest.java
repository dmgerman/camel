begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.elasticsearch5
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|elasticsearch5
package|;
end_package

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
name|bulk
operator|.
name|BulkRequest
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
name|bulk
operator|.
name|BulkResponse
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
name|hasItem
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
DECL|class|ElasticsearchBulkTest
specifier|public
class|class
name|ElasticsearchBulkTest
extends|extends
name|ElasticsearchBaseTest
block|{
annotation|@
name|Test
DECL|method|testBulkIndex ()
specifier|public
name|void
name|testBulkIndex
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|documents
init|=
operator|new
name|ArrayList
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|document1
init|=
name|createIndexedData
argument_list|(
literal|"1"
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|document2
init|=
name|createIndexedData
argument_list|(
literal|"2"
argument_list|)
decl_stmt|;
name|documents
operator|.
name|add
argument_list|(
name|document1
argument_list|)
expr_stmt|;
name|documents
operator|.
name|add
argument_list|(
name|document2
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|indexIds
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:bulk_index"
argument_list|,
name|documents
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"indexIds should be set"
argument_list|,
name|indexIds
argument_list|)
expr_stmt|;
name|assertCollectionSize
argument_list|(
literal|"Indexed documents should match the size of documents"
argument_list|,
name|indexIds
argument_list|,
name|documents
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|bulkIndexRequestBody ()
specifier|public
name|void
name|bulkIndexRequestBody
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
name|BulkRequest
name|request
init|=
operator|new
name|BulkRequest
argument_list|()
decl_stmt|;
name|request
operator|.
name|add
argument_list|(
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
literal|"baz"
argument_list|)
operator|.
name|source
argument_list|(
literal|"{\""
operator|+
name|prefix
operator|+
literal|"content\": \""
operator|+
name|prefix
operator|+
literal|"hello\"}"
argument_list|)
argument_list|)
expr_stmt|;
comment|// when
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|List
argument_list|<
name|String
argument_list|>
name|indexedDocumentIds
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:bulk_index"
argument_list|,
name|request
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// then
name|assertThat
argument_list|(
name|indexedDocumentIds
argument_list|,
name|notNullValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|indexedDocumentIds
operator|.
name|size
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|indexedDocumentIds
argument_list|,
name|hasItem
argument_list|(
name|prefix
operator|+
literal|"baz"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|bulkRequestBody ()
specifier|public
name|void
name|bulkRequestBody
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
name|BulkRequest
name|request
init|=
operator|new
name|BulkRequest
argument_list|()
decl_stmt|;
name|request
operator|.
name|add
argument_list|(
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
literal|"baz"
argument_list|)
operator|.
name|source
argument_list|(
literal|"{\""
operator|+
name|prefix
operator|+
literal|"content\": \""
operator|+
name|prefix
operator|+
literal|"hello\"}"
argument_list|)
argument_list|)
expr_stmt|;
comment|// when
name|BulkResponse
name|response
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:bulk"
argument_list|,
name|request
argument_list|,
name|BulkResponse
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
name|assertEquals
argument_list|(
name|prefix
operator|+
literal|"baz"
argument_list|,
name|response
operator|.
name|getItems
argument_list|()
index|[
literal|0
index|]
operator|.
name|getId
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
block|{
name|from
argument_list|(
literal|"direct:bulk_index"
argument_list|)
operator|.
name|to
argument_list|(
literal|"elasticsearch5://elasticsearch?operation=BULK_INDEX&indexName=twitter&indexType=tweet&ip=localhost&port=9300"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:bulk"
argument_list|)
operator|.
name|to
argument_list|(
literal|"elasticsearch5://elasticsearch?operation=BULK&indexName=twitter&indexType=tweet&ip=localhost&port=9300"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

