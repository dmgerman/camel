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
name|apache
operator|.
name|http
operator|.
name|impl
operator|.
name|client
operator|.
name|BasicResponseHandler
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
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|ElasticsearchClusterIndexTest
specifier|public
class|class
name|ElasticsearchClusterIndexTest
extends|extends
name|ElasticsearchClusterBaseTest
block|{
annotation|@
name|Test
DECL|method|indexWithIpAndPort ()
specifier|public
name|void
name|indexWithIpAndPort
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
name|PARAM_INDEX_TYPE
argument_list|,
literal|"tweet"
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
literal|"1"
argument_list|)
expr_stmt|;
name|String
name|indexId
init|=
name|template
operator|.
name|requestBodyAndHeaders
argument_list|(
literal|"direct:indexWithIpAndPort"
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
name|headers
operator|.
name|clear
argument_list|()
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
name|PARAM_INDEX_TYPE
argument_list|,
literal|"status"
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
literal|"2"
argument_list|)
expr_stmt|;
name|indexId
operator|=
name|template
operator|.
name|requestBodyAndHeaders
argument_list|(
literal|"direct:indexWithIpAndPort"
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
literal|"Cluster must be of three nodes"
argument_list|,
name|runner
operator|.
name|getNodeSize
argument_list|()
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Index id 1 must exists"
argument_list|,
literal|true
argument_list|,
name|client
operator|.
name|get
argument_list|(
operator|new
name|GetRequest
argument_list|(
literal|"twitter"
argument_list|,
literal|"tweet"
argument_list|,
literal|"1"
argument_list|)
argument_list|)
operator|.
name|isExists
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Index id 2 must exists"
argument_list|,
literal|true
argument_list|,
name|client
operator|.
name|get
argument_list|(
operator|new
name|GetRequest
argument_list|(
literal|"twitter"
argument_list|,
literal|"status"
argument_list|,
literal|"2"
argument_list|)
argument_list|)
operator|.
name|isExists
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|indexWithSnifferEnable ()
specifier|public
name|void
name|indexWithSnifferEnable
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
literal|"facebook"
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|ElasticsearchConstants
operator|.
name|PARAM_INDEX_TYPE
argument_list|,
literal|"post"
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
literal|"4"
argument_list|)
expr_stmt|;
name|String
name|indexId
init|=
name|template
operator|.
name|requestBodyAndHeaders
argument_list|(
literal|"direct:indexWithSniffer"
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
literal|"Cluster must be of three nodes"
argument_list|,
name|runner
operator|.
name|getNodeSize
argument_list|()
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Index id 4 must exists"
argument_list|,
literal|true
argument_list|,
name|client
operator|.
name|get
argument_list|(
operator|new
name|GetRequest
argument_list|(
literal|"facebook"
argument_list|,
literal|"post"
argument_list|,
literal|"4"
argument_list|)
argument_list|)
operator|.
name|isExists
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|BasicResponseHandler
name|responseHandler
init|=
operator|new
name|BasicResponseHandler
argument_list|()
decl_stmt|;
name|String
name|body
init|=
name|responseHandler
operator|.
name|handleEntity
argument_list|(
name|restclient
operator|.
name|performRequest
argument_list|(
literal|"GET"
argument_list|,
literal|"/_cluster/health?pretty"
argument_list|)
operator|.
name|getEntity
argument_list|()
argument_list|)
decl_stmt|;
name|assertStringContains
argument_list|(
name|body
argument_list|,
literal|"\"number_of_data_nodes\" : 3"
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
literal|"direct:indexWithIpAndPort"
argument_list|)
operator|.
name|to
argument_list|(
literal|"elasticsearch5-rest://"
operator|+
name|clusterName
operator|+
literal|"?operation=Index&indexName=twitter&indexType=tweet&hostAddresses=localhost:"
operator|+
name|ES_FIRST_NODE_TRANSPORT_PORT
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:indexWithSniffer"
argument_list|)
operator|.
name|to
argument_list|(
literal|"elasticsearch5-rest://"
operator|+
name|clusterName
operator|+
literal|"?operation=Index&indexName=twitter&indexType=tweet&enableSniffer=true&hostAddresses=localhost:"
operator|+
name|ES_FIRST_NODE_TRANSPORT_PORT
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

