begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.google.bigquery.integration.sql
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
operator|.
name|sql
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
name|integration
operator|.
name|BigQueryTestSupport
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
name|impl
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
DECL|class|SqlQueryTest
specifier|public
class|class
name|SqlQueryTest
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
literal|"test_sql_table"
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
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
name|uri
operator|=
literal|"google-bigquery-sql:{{project.id}}: insert into {{bigquery.datasetId}}."
operator|+
name|TABLE_ID
operator|+
literal|"(col1, col2) values (@col1, @col2)"
argument_list|)
DECL|field|bigqueryEndpoint
specifier|private
name|Endpoint
name|bigqueryEndpoint
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:sendResult"
argument_list|)
DECL|field|sendResult
specifier|private
name|MockEndpoint
name|sendResult
decl_stmt|;
annotation|@
name|Produce
argument_list|(
name|uri
operator|=
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
name|directIn
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"InsertRow"
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
DECL|method|insertRecordBySql ()
specifier|public
name|void
name|insertRecordBySql
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
name|sendResult
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|sendResult
operator|.
name|expectedBodiesReceived
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|producer
operator|.
name|send
argument_list|(
name|exchange
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

