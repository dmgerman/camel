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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|Metadata
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
name|spi
operator|.
name|UriParams
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
name|UriPath
import|;
end_import

begin_class
annotation|@
name|UriParams
DECL|class|GoogleBigQueryConfiguration
specifier|public
class|class
name|GoogleBigQueryConfiguration
block|{
annotation|@
name|UriParam
argument_list|(
name|name
operator|=
literal|"concurrentProducers"
argument_list|,
name|description
operator|=
literal|"Maximum number of simultaneous consumers when using async processing"
argument_list|)
DECL|field|concurrentProducers
specifier|private
name|int
name|concurrentProducers
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|name
operator|=
literal|"connectionFactory"
argument_list|,
name|description
operator|=
literal|"ConnectionFactory to obtain connection to Bigquery Service. If non provided the default one will be used"
argument_list|)
DECL|field|connectionFactory
specifier|private
name|GoogleBigQueryConnectionFactory
name|connectionFactory
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|name
operator|=
literal|"loggerId"
argument_list|,
name|description
operator|=
literal|"Logger ID to use when a match to the parent route required"
argument_list|)
DECL|field|loggerId
specifier|private
name|String
name|loggerId
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|name
operator|=
literal|"useAsInsertId"
argument_list|,
name|description
operator|=
literal|"Field name to use as insert id"
argument_list|)
DECL|field|useAsInsertId
specifier|private
name|String
name|useAsInsertId
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|label
operator|=
literal|"common"
argument_list|,
name|description
operator|=
literal|"Google Cloud Project Id"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|projectId
specifier|private
name|String
name|projectId
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|label
operator|=
literal|"common"
argument_list|,
name|description
operator|=
literal|"BigQuery Dataset Id"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|datasetId
specifier|private
name|String
name|datasetId
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|label
operator|=
literal|"common"
argument_list|,
name|description
operator|=
literal|"BigQuery table id"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"false"
argument_list|)
DECL|field|tableId
specifier|private
name|String
name|tableId
decl_stmt|;
DECL|method|parseRemaining (String remaining)
specifier|public
name|void
name|parseRemaining
parameter_list|(
name|String
name|remaining
parameter_list|)
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
literal|"Google BigQuery Endpoint format \"projectId:datasetId[:tableName]\""
argument_list|)
throw|;
block|}
name|int
name|c
init|=
literal|0
decl_stmt|;
name|projectId
operator|=
name|parts
index|[
name|c
operator|++
index|]
expr_stmt|;
name|datasetId
operator|=
name|parts
index|[
name|c
operator|++
index|]
expr_stmt|;
if|if
condition|(
name|parts
operator|.
name|length
operator|>
literal|2
condition|)
block|{
name|tableId
operator|=
name|parts
index|[
name|c
operator|++
index|]
expr_stmt|;
block|}
block|}
DECL|method|getLoggerId ()
specifier|public
name|String
name|getLoggerId
parameter_list|()
block|{
return|return
name|loggerId
return|;
block|}
DECL|method|setLoggerId (String loggerId)
specifier|public
name|GoogleBigQueryConfiguration
name|setLoggerId
parameter_list|(
name|String
name|loggerId
parameter_list|)
block|{
name|this
operator|.
name|loggerId
operator|=
name|loggerId
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * ConnectionFactory to obtain connection to Bigquery Service. If non provided the default will be used.      */
DECL|method|getConnectionFactory ()
specifier|public
name|GoogleBigQueryConnectionFactory
name|getConnectionFactory
parameter_list|()
block|{
return|return
name|connectionFactory
return|;
block|}
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
DECL|method|getConcurrentProducers ()
specifier|public
name|int
name|getConcurrentProducers
parameter_list|()
block|{
return|return
name|concurrentProducers
return|;
block|}
DECL|method|setConcurrentProducers (int concurrentProducers)
specifier|public
name|GoogleBigQueryConfiguration
name|setConcurrentProducers
parameter_list|(
name|int
name|concurrentProducers
parameter_list|)
block|{
name|this
operator|.
name|concurrentProducers
operator|=
name|concurrentProducers
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|getUseAsInsertId ()
specifier|public
name|String
name|getUseAsInsertId
parameter_list|()
block|{
return|return
name|useAsInsertId
return|;
block|}
DECL|method|setUseAsInsertId (String useAsInsertId)
specifier|public
name|GoogleBigQueryConfiguration
name|setUseAsInsertId
parameter_list|(
name|String
name|useAsInsertId
parameter_list|)
block|{
name|this
operator|.
name|useAsInsertId
operator|=
name|useAsInsertId
expr_stmt|;
return|return
name|this
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
DECL|method|setProjectId (String projectId)
specifier|public
name|GoogleBigQueryConfiguration
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
return|return
name|this
return|;
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
DECL|method|setDatasetId (String datasetId)
specifier|public
name|GoogleBigQueryConfiguration
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
return|return
name|this
return|;
block|}
DECL|method|getTableId ()
specifier|public
name|String
name|getTableId
parameter_list|()
block|{
return|return
name|tableId
return|;
block|}
DECL|method|setTableId (String tableId)
specifier|public
name|GoogleBigQueryConfiguration
name|setTableId
parameter_list|(
name|String
name|tableId
parameter_list|)
block|{
name|this
operator|.
name|tableId
operator|=
name|tableId
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
end_class

end_unit

