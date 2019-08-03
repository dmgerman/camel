begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.google.bigquery.sql
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
name|sql
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Consumer
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
name|Producer
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
name|spi
operator|.
name|UriEndpoint
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
name|spi
operator|.
name|UriParam
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
name|DefaultEndpoint
import|;
end_import

begin_comment
comment|/**  * Google BigQuery data warehouse for analytics (using SQL queries).  *  * BigQuery Endpoint Definition  * Represents a table within a BigQuery dataset  * Contains configuration details for a single table and the utility methods (such as check, create) to ease operations  * URI Parameters:  * * Logger ID - To ensure that logging is unified under Route Logger, the logger ID can be passed on  *               via an endpoint URI parameter  * * Partitioned - to indicate that the table needs to be partitioned - every UTC day to be written into a  *                 timestamped separate table  *                 side effect: Australian operational day is always split between two UTC days, and, therefore, tables  *  * Another consideration is that exceptions are not handled within the class. They are expected to bubble up and be handled  * by Camel.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.23.0"
argument_list|,
name|scheme
operator|=
literal|"google-bigquery-sql"
argument_list|,
name|title
operator|=
literal|"Google BigQuery Standard SQL"
argument_list|,
name|syntax
operator|=
literal|"google-bigquery-sql:projectId:query"
argument_list|,
name|label
operator|=
literal|"cloud,messaging"
argument_list|,
name|producerOnly
operator|=
literal|true
argument_list|)
DECL|class|GoogleBigQuerySQLEndpoint
specifier|public
class|class
name|GoogleBigQuerySQLEndpoint
extends|extends
name|DefaultEndpoint
block|{
annotation|@
name|UriParam
DECL|field|configuration
specifier|protected
specifier|final
name|GoogleBigQuerySQLConfiguration
name|configuration
decl_stmt|;
DECL|method|GoogleBigQuerySQLEndpoint (String endpointUri, GoogleBigQuerySQLComponent component, GoogleBigQuerySQLConfiguration configuration)
specifier|protected
name|GoogleBigQuerySQLEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|GoogleBigQuerySQLComponent
name|component
parameter_list|,
name|GoogleBigQuerySQLConfiguration
name|configuration
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
name|Bigquery
name|bigquery
init|=
name|getConfiguration
argument_list|()
operator|.
name|getConnectionFactory
argument_list|()
operator|.
name|getDefaultClient
argument_list|()
decl_stmt|;
name|GoogleBigQuerySQLProducer
name|producer
init|=
operator|new
name|GoogleBigQuerySQLProducer
argument_list|(
name|bigquery
argument_list|,
name|this
argument_list|,
name|configuration
argument_list|)
decl_stmt|;
return|return
name|producer
return|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Cannot consume from the BigQuery endpoint: "
operator|+
name|getEndpointUri
argument_list|()
argument_list|)
throw|;
block|}
DECL|method|getConfiguration ()
specifier|public
name|GoogleBigQuerySQLConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
annotation|@
name|Override
DECL|method|getComponent ()
specifier|public
name|GoogleBigQuerySQLComponent
name|getComponent
parameter_list|()
block|{
return|return
operator|(
name|GoogleBigQuerySQLComponent
operator|)
name|super
operator|.
name|getComponent
argument_list|()
return|;
block|}
block|}
end_class

end_unit

