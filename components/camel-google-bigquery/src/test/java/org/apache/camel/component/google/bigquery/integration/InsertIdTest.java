begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.google.bigquery.integration
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|google
operator|.
name|bigquery
operator|.
name|integration
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
name|java
operator|.
name|util
operator|.
name|UUID
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
name|Endpoint
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
name|EndpointInject
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
name|Produce
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
name|ProducerTemplate
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
name|google
operator|.
name|bigquery
operator|.
name|GoogleBigQueryConstants
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
name|support
operator|.
name|DefaultExchange
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
DECL|class|InsertIdTest
specifier|public
class|class
name|InsertIdTest
extends|extends
name|BigQueryTestSupport
block|{
DECL|field|TABLE_ID
specifier|private
specifier|static
specifier|final
name|String
name|TABLE_ID
init|=
literal|"insertId"
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
literal|"direct:withInsertId"
argument_list|)
DECL|field|directInWithInsertId
specifier|private
name|Endpoint
name|directInWithInsertId
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
literal|"direct:in"
argument_list|)
DECL|field|directIn
specifier|private
name|Endpoint
name|directIn
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
literal|"google-bigquery:{{project.id}}:{{bigquery.datasetId}}:"
operator|+
name|TABLE_ID
operator|+
literal|"?useAsInsertId=col1"
argument_list|)
DECL|field|bigqueryEndpointWithInsertId
specifier|private
name|Endpoint
name|bigqueryEndpointWithInsertId
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
literal|"google-bigquery:{{project.id}}:{{bigquery.datasetId}}:"
operator|+
name|TABLE_ID
argument_list|)
DECL|field|bigqueryEndpoint
specifier|private
name|Endpoint
name|bigqueryEndpoint
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
literal|"mock:sendResult"
argument_list|)
DECL|field|sendResult
specifier|private
name|MockEndpoint
name|sendResult
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
literal|"mock:sendResultWithInsertId"
argument_list|)
DECL|field|sendResultWithInsertId
specifier|private
name|MockEndpoint
name|sendResultWithInsertId
decl_stmt|;
annotation|@
name|Produce
argument_list|(
literal|"direct:withInsertId"
argument_list|)
DECL|field|producerWithInsertId
specifier|private
name|ProducerTemplate
name|producerWithInsertId
decl_stmt|;
annotation|@
name|Produce
argument_list|(
literal|"direct:in"
argument_list|)
DECL|field|producer
specifier|private
name|ProducerTemplate
name|producer
decl_stmt|;
annotation|@
name|Before
DECL|method|init ()
specifier|public
name|void
name|init
parameter_list|()
throws|throws
name|Exception
block|{
name|createBqTable
argument_list|(
name|TABLE_ID
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
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
name|directInWithInsertId
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"SingleRowWithInsertId"
argument_list|)
operator|.
name|to
argument_list|(
name|bigqueryEndpointWithInsertId
argument_list|)
operator|.
name|to
argument_list|(
name|sendResultWithInsertId
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|directIn
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"SingleRow"
argument_list|)
operator|.
name|to
argument_list|(
name|bigqueryEndpoint
argument_list|)
operator|.
name|to
argument_list|(
name|sendResult
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Test
DECL|method|sendTwoMessagesExpectOneRowUsingConfig ()
specifier|public
name|void
name|sendTwoMessagesExpectOneRowUsingConfig
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|String
name|uuidCol1
init|=
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|String
name|uuidCol2
init|=
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|object
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|object
operator|.
name|put
argument_list|(
literal|"col1"
argument_list|,
name|uuidCol1
argument_list|)
expr_stmt|;
name|object
operator|.
name|put
argument_list|(
literal|"col2"
argument_list|,
name|uuidCol2
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|Exchange
name|exchange2
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|String
name|uuid2Col2
init|=
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|object
operator|.
name|put
argument_list|(
literal|"col1"
argument_list|,
name|uuidCol1
argument_list|)
expr_stmt|;
name|object
operator|.
name|put
argument_list|(
literal|"col2"
argument_list|,
name|uuid2Col2
argument_list|)
expr_stmt|;
name|exchange2
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|sendResultWithInsertId
operator|.
name|expectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|producerWithInsertId
operator|.
name|send
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|producerWithInsertId
operator|.
name|send
argument_list|(
name|exchange2
argument_list|)
expr_stmt|;
name|sendResultWithInsertId
operator|.
name|assertIsSatisfied
argument_list|(
literal|4000
argument_list|)
expr_stmt|;
name|assertRowExist
argument_list|(
name|TABLE_ID
argument_list|,
name|object
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|sendTwoMessagesExpectOneRowUsingExchange ()
specifier|public
name|void
name|sendTwoMessagesExpectOneRowUsingExchange
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|String
name|uuidCol1
init|=
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|String
name|uuidCol2
init|=
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|object
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|object
operator|.
name|put
argument_list|(
literal|"col1"
argument_list|,
name|uuidCol1
argument_list|)
expr_stmt|;
name|object
operator|.
name|put
argument_list|(
literal|"col2"
argument_list|,
name|uuidCol2
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|GoogleBigQueryConstants
operator|.
name|INSERT_ID
argument_list|,
name|uuidCol1
argument_list|)
expr_stmt|;
name|Exchange
name|exchange2
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|String
name|uuid2Col2
init|=
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|object
operator|.
name|put
argument_list|(
literal|"col1"
argument_list|,
name|uuidCol1
argument_list|)
expr_stmt|;
name|object
operator|.
name|put
argument_list|(
literal|"col2"
argument_list|,
name|uuid2Col2
argument_list|)
expr_stmt|;
name|exchange2
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|exchange2
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|GoogleBigQueryConstants
operator|.
name|INSERT_ID
argument_list|,
name|uuidCol1
argument_list|)
expr_stmt|;
name|sendResult
operator|.
name|expectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|producer
operator|.
name|send
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|producer
operator|.
name|send
argument_list|(
name|exchange2
argument_list|)
expr_stmt|;
name|sendResult
operator|.
name|assertIsSatisfied
argument_list|(
literal|4000
argument_list|)
expr_stmt|;
name|assertRowExist
argument_list|(
name|TABLE_ID
argument_list|,
name|object
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

