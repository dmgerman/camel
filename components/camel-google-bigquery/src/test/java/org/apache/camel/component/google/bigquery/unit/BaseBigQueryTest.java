begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.google.bigquery.unit
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
name|unit
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|services
operator|.
name|bigquery
operator|.
name|Bigquery
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|services
operator|.
name|bigquery
operator|.
name|model
operator|.
name|TableDataInsertAllResponse
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
name|GoogleBigQueryConfiguration
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
name|GoogleBigQueryEndpoint
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
name|GoogleBigQueryProducer
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
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
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|any
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|anyString
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|mock
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|when
import|;
end_import

begin_class
DECL|class|BaseBigQueryTest
specifier|public
class|class
name|BaseBigQueryTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|endpoint
specifier|protected
name|GoogleBigQueryEndpoint
name|endpoint
init|=
name|mock
argument_list|(
name|GoogleBigQueryEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|mockInsertall
specifier|protected
name|Bigquery
operator|.
name|Tabledata
operator|.
name|InsertAll
name|mockInsertall
init|=
name|mock
argument_list|(
name|Bigquery
operator|.
name|Tabledata
operator|.
name|InsertAll
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|producer
specifier|protected
name|GoogleBigQueryProducer
name|producer
decl_stmt|;
DECL|field|tabledataMock
specifier|protected
name|Bigquery
operator|.
name|Tabledata
name|tabledataMock
decl_stmt|;
DECL|field|tableId
specifier|protected
name|String
name|tableId
init|=
literal|"testTableId"
decl_stmt|;
DECL|field|datasetId
specifier|protected
name|String
name|datasetId
init|=
literal|"testDatasetId"
decl_stmt|;
DECL|field|projectId
specifier|protected
name|String
name|projectId
init|=
literal|"testProjectId"
decl_stmt|;
DECL|field|configuration
specifier|protected
name|GoogleBigQueryConfiguration
name|configuration
init|=
operator|new
name|GoogleBigQueryConfiguration
argument_list|()
decl_stmt|;
DECL|field|bigquery
specifier|protected
name|Bigquery
name|bigquery
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
name|setupBigqueryMock
argument_list|()
expr_stmt|;
name|producer
operator|=
name|createProducer
argument_list|()
expr_stmt|;
block|}
DECL|method|createProducer ()
specifier|protected
name|GoogleBigQueryProducer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
name|configuration
operator|.
name|setProjectId
argument_list|(
name|projectId
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setTableId
argument_list|(
name|tableId
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setDatasetId
argument_list|(
name|datasetId
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setTableId
argument_list|(
literal|"testTableId"
argument_list|)
expr_stmt|;
return|return
operator|new
name|GoogleBigQueryProducer
argument_list|(
name|bigquery
argument_list|,
name|endpoint
argument_list|,
name|configuration
argument_list|)
return|;
block|}
DECL|method|setupBigqueryMock ()
specifier|protected
name|void
name|setupBigqueryMock
parameter_list|()
throws|throws
name|Exception
block|{
name|bigquery
operator|=
name|mock
argument_list|(
name|Bigquery
operator|.
name|class
argument_list|)
expr_stmt|;
name|tabledataMock
operator|=
name|mock
argument_list|(
name|Bigquery
operator|.
name|Tabledata
operator|.
name|class
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|bigquery
operator|.
name|tabledata
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|tabledataMock
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|tabledataMock
operator|.
name|insertAll
argument_list|(
name|anyString
argument_list|()
argument_list|,
name|anyString
argument_list|()
argument_list|,
name|anyString
argument_list|()
argument_list|,
name|any
argument_list|()
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|mockInsertall
argument_list|)
expr_stmt|;
name|TableDataInsertAllResponse
name|mockResponse
init|=
operator|new
name|TableDataInsertAllResponse
argument_list|()
decl_stmt|;
name|when
argument_list|(
name|mockInsertall
operator|.
name|execute
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|mockResponse
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

