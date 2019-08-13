begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.google.bigquery.sql.springboot
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
operator|.
name|springboot
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Generated
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
name|spring
operator|.
name|boot
operator|.
name|ComponentConfigurationPropertiesCommon
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|ConfigurationProperties
import|;
end_import

begin_comment
comment|/**  * Google BigQuery data warehouse for analytics (using SQL queries).  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.SpringBootAutoConfigurationMojo"
argument_list|)
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.component.google-bigquery-sql"
argument_list|)
DECL|class|GoogleBigQuerySQLComponentConfiguration
specifier|public
class|class
name|GoogleBigQuerySQLComponentConfiguration
extends|extends
name|ComponentConfigurationPropertiesCommon
block|{
comment|/**      * Whether to enable auto configuration of the google-bigquery-sql      * component. This is enabled by default.      */
DECL|field|enabled
specifier|private
name|Boolean
name|enabled
decl_stmt|;
comment|/**      * Google Cloud Project Id      */
DECL|field|projectId
specifier|private
name|String
name|projectId
decl_stmt|;
comment|/**      * ConnectionFactory to obtain connection to Bigquery Service. If non      * provided the default one will be used      */
DECL|field|connectionFactory
specifier|private
name|GoogleBigQueryConnectionFactoryNestedConfiguration
name|connectionFactory
decl_stmt|;
comment|/**      * Whether the component should use basic property binding (Camel 2.x) or      * the newer property binding with additional capabilities      */
DECL|field|basicPropertyBinding
specifier|private
name|Boolean
name|basicPropertyBinding
init|=
literal|false
decl_stmt|;
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
DECL|method|getConnectionFactory ()
specifier|public
name|GoogleBigQueryConnectionFactoryNestedConfiguration
name|getConnectionFactory
parameter_list|()
block|{
return|return
name|connectionFactory
return|;
block|}
DECL|method|setConnectionFactory ( GoogleBigQueryConnectionFactoryNestedConfiguration connectionFactory)
specifier|public
name|void
name|setConnectionFactory
parameter_list|(
name|GoogleBigQueryConnectionFactoryNestedConfiguration
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
DECL|method|getBasicPropertyBinding ()
specifier|public
name|Boolean
name|getBasicPropertyBinding
parameter_list|()
block|{
return|return
name|basicPropertyBinding
return|;
block|}
DECL|method|setBasicPropertyBinding (Boolean basicPropertyBinding)
specifier|public
name|void
name|setBasicPropertyBinding
parameter_list|(
name|Boolean
name|basicPropertyBinding
parameter_list|)
block|{
name|this
operator|.
name|basicPropertyBinding
operator|=
name|basicPropertyBinding
expr_stmt|;
block|}
DECL|class|GoogleBigQueryConnectionFactoryNestedConfiguration
specifier|public
specifier|static
class|class
name|GoogleBigQueryConnectionFactoryNestedConfiguration
block|{
DECL|field|CAMEL_NESTED_CLASS
specifier|public
specifier|static
specifier|final
name|Class
name|CAMEL_NESTED_CLASS
init|=
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
operator|.
name|class
decl_stmt|;
DECL|field|serviceAccount
specifier|private
name|String
name|serviceAccount
decl_stmt|;
DECL|field|serviceAccountKey
specifier|private
name|String
name|serviceAccountKey
decl_stmt|;
DECL|field|credentialsFileLocation
specifier|private
name|String
name|credentialsFileLocation
decl_stmt|;
DECL|field|serviceURL
specifier|private
name|String
name|serviceURL
decl_stmt|;
DECL|method|getServiceAccount ()
specifier|public
name|String
name|getServiceAccount
parameter_list|()
block|{
return|return
name|serviceAccount
return|;
block|}
DECL|method|setServiceAccount (String serviceAccount)
specifier|public
name|void
name|setServiceAccount
parameter_list|(
name|String
name|serviceAccount
parameter_list|)
block|{
name|this
operator|.
name|serviceAccount
operator|=
name|serviceAccount
expr_stmt|;
block|}
DECL|method|getServiceAccountKey ()
specifier|public
name|String
name|getServiceAccountKey
parameter_list|()
block|{
return|return
name|serviceAccountKey
return|;
block|}
DECL|method|setServiceAccountKey (String serviceAccountKey)
specifier|public
name|void
name|setServiceAccountKey
parameter_list|(
name|String
name|serviceAccountKey
parameter_list|)
block|{
name|this
operator|.
name|serviceAccountKey
operator|=
name|serviceAccountKey
expr_stmt|;
block|}
DECL|method|getCredentialsFileLocation ()
specifier|public
name|String
name|getCredentialsFileLocation
parameter_list|()
block|{
return|return
name|credentialsFileLocation
return|;
block|}
DECL|method|setCredentialsFileLocation (String credentialsFileLocation)
specifier|public
name|void
name|setCredentialsFileLocation
parameter_list|(
name|String
name|credentialsFileLocation
parameter_list|)
block|{
name|this
operator|.
name|credentialsFileLocation
operator|=
name|credentialsFileLocation
expr_stmt|;
block|}
DECL|method|getServiceURL ()
specifier|public
name|String
name|getServiceURL
parameter_list|()
block|{
return|return
name|serviceURL
return|;
block|}
DECL|method|setServiceURL (String serviceURL)
specifier|public
name|void
name|setServiceURL
parameter_list|(
name|String
name|serviceURL
parameter_list|)
block|{
name|this
operator|.
name|serviceURL
operator|=
name|serviceURL
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

