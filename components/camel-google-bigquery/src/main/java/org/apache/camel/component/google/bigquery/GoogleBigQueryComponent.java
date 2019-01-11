begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.google.bigquery
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
package|;
end_package

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
name|CamelContext
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
name|spi
operator|.
name|annotations
operator|.
name|Component
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
name|DefaultComponent
import|;
end_import

begin_class
annotation|@
name|Component
argument_list|(
literal|"google-bigquery"
argument_list|)
DECL|class|GoogleBigQueryComponent
specifier|public
class|class
name|GoogleBigQueryComponent
extends|extends
name|DefaultComponent
block|{
DECL|field|projectId
specifier|private
name|String
name|projectId
decl_stmt|;
DECL|field|datasetId
specifier|private
name|String
name|datasetId
decl_stmt|;
DECL|field|connectionFactory
specifier|private
name|GoogleBigQueryConnectionFactory
name|connectionFactory
decl_stmt|;
DECL|method|GoogleBigQueryComponent ()
specifier|public
name|GoogleBigQueryComponent
parameter_list|()
block|{     }
DECL|method|GoogleBigQueryComponent (CamelContext camelContext)
specifier|public
name|GoogleBigQueryComponent
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|super
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
block|}
comment|// Endpoint represents a single table
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|String
index|[]
name|parts
init|=
name|remaining
operator|.
name|split
argument_list|(
literal|":"
argument_list|)
decl_stmt|;
if|if
condition|(
name|parts
operator|.
name|length
operator|<
literal|2
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Google BigQuery Endpoint format \"projectId:datasetId:tableName\""
argument_list|)
throw|;
block|}
name|GoogleBigQueryConfiguration
name|configuration
init|=
operator|new
name|GoogleBigQueryConfiguration
argument_list|()
decl_stmt|;
name|setProperties
argument_list|(
name|configuration
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|parseRemaining
argument_list|(
name|remaining
argument_list|)
expr_stmt|;
if|if
condition|(
name|configuration
operator|.
name|getConnectionFactory
argument_list|()
operator|==
literal|null
condition|)
block|{
name|configuration
operator|.
name|setConnectionFactory
argument_list|(
name|getConnectionFactory
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|GoogleBigQueryEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|configuration
argument_list|)
return|;
block|}
DECL|method|getProjectId ()
specifier|public
name|String
name|getProjectId
parameter_list|()
block|{
return|return
name|projectId
return|;
block|}
comment|/**      * Google Cloud Project Id      */
DECL|method|setProjectId (String projectId)
specifier|public
name|void
name|setProjectId
parameter_list|(
name|String
name|projectId
parameter_list|)
block|{
name|this
operator|.
name|projectId
operator|=
name|projectId
expr_stmt|;
block|}
DECL|method|getDatasetId ()
specifier|public
name|String
name|getDatasetId
parameter_list|()
block|{
return|return
name|datasetId
return|;
block|}
comment|/**      * BigQuery Dataset Id      */
DECL|method|setDatasetId (String datasetId)
specifier|public
name|void
name|setDatasetId
parameter_list|(
name|String
name|datasetId
parameter_list|)
block|{
name|this
operator|.
name|datasetId
operator|=
name|datasetId
expr_stmt|;
block|}
DECL|method|getConnectionFactory ()
specifier|public
name|GoogleBigQueryConnectionFactory
name|getConnectionFactory
parameter_list|()
block|{
if|if
condition|(
name|connectionFactory
operator|==
literal|null
condition|)
block|{
name|connectionFactory
operator|=
operator|new
name|GoogleBigQueryConnectionFactory
argument_list|()
expr_stmt|;
block|}
return|return
name|connectionFactory
return|;
block|}
comment|/**      * ConnectionFactory to obtain connection to Bigquery Service. If non provided the default one will be used      */
DECL|method|setConnectionFactory (GoogleBigQueryConnectionFactory connectionFactory)
specifier|public
name|void
name|setConnectionFactory
parameter_list|(
name|GoogleBigQueryConnectionFactory
name|connectionFactory
parameter_list|)
block|{
name|this
operator|.
name|connectionFactory
operator|=
name|connectionFactory
expr_stmt|;
block|}
block|}
end_class

end_unit

