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
name|GoogleBigQueryConnectionFactory
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
DECL|class|GoogleBigQuerySQLConfiguration
specifier|public
class|class
name|GoogleBigQuerySQLConfiguration
block|{
annotation|@
name|UriParam
argument_list|(
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
literal|true
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
literal|"BigQuery standard SQL query"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|query
specifier|private
name|String
name|query
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
name|int
name|indexOfColon
init|=
name|remaining
operator|.
name|indexOf
argument_list|(
literal|":"
argument_list|)
decl_stmt|;
if|if
condition|(
name|indexOfColon
operator|<
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Google BigQuery Endpoint format \"projectId:query\""
argument_list|)
throw|;
block|}
name|projectId
operator|=
name|remaining
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|indexOfColon
argument_list|)
expr_stmt|;
name|query
operator|=
name|remaining
operator|.
name|substring
argument_list|(
name|indexOfColon
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
comment|/**      * ConnectionFactory to obtain connection to Bigquery Service. If non      * provided the default will be used.      */
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
DECL|method|getQuery ()
specifier|public
name|String
name|getQuery
parameter_list|()
block|{
return|return
name|query
return|;
block|}
DECL|method|setQuery (String query)
specifier|public
name|GoogleBigQuerySQLConfiguration
name|setQuery
parameter_list|(
name|String
name|query
parameter_list|)
block|{
name|this
operator|.
name|query
operator|=
name|query
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
name|GoogleBigQuerySQLConfiguration
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
block|}
end_class

end_unit

