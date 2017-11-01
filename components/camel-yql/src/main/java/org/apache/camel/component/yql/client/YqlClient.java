begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.yql.client
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|yql
operator|.
name|client
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|yql
operator|.
name|configuration
operator|.
name|YqlConfiguration
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
name|NameValuePair
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
name|client
operator|.
name|methods
operator|.
name|CloseableHttpResponse
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
name|client
operator|.
name|methods
operator|.
name|HttpGet
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
name|client
operator|.
name|utils
operator|.
name|URIBuilder
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
name|CloseableHttpClient
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
name|message
operator|.
name|BasicNameValuePair
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
name|util
operator|.
name|EntityUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
DECL|class|YqlClient
specifier|public
class|class
name|YqlClient
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|YqlClient
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|httpClient
specifier|private
specifier|final
name|CloseableHttpClient
name|httpClient
decl_stmt|;
DECL|method|YqlClient (final CloseableHttpClient httpClient)
specifier|public
name|YqlClient
parameter_list|(
specifier|final
name|CloseableHttpClient
name|httpClient
parameter_list|)
block|{
name|this
operator|.
name|httpClient
operator|=
name|httpClient
expr_stmt|;
block|}
DECL|method|get (final YqlConfiguration yqlConfiguration)
specifier|public
name|YqlResponse
name|get
parameter_list|(
specifier|final
name|YqlConfiguration
name|yqlConfiguration
parameter_list|)
throws|throws
name|Exception
block|{
specifier|final
name|URI
name|uri
init|=
operator|new
name|URIBuilder
argument_list|()
operator|.
name|setScheme
argument_list|(
name|yqlConfiguration
operator|.
name|isHttps
argument_list|()
condition|?
literal|"https"
else|:
literal|"http"
argument_list|)
operator|.
name|setHost
argument_list|(
literal|"query.yahooapis.com"
argument_list|)
operator|.
name|setPath
argument_list|(
literal|"/v1/public/yql"
argument_list|)
operator|.
name|setParameters
argument_list|(
name|buildParameters
argument_list|(
name|yqlConfiguration
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"YQL query: {}"
argument_list|,
name|uri
argument_list|)
expr_stmt|;
specifier|final
name|HttpGet
name|httpget
init|=
operator|new
name|HttpGet
argument_list|(
name|uri
argument_list|)
decl_stmt|;
try|try
init|(
specifier|final
name|CloseableHttpResponse
name|response
init|=
name|httpClient
operator|.
name|execute
argument_list|(
name|httpget
argument_list|)
init|)
block|{
specifier|final
name|YqlResponse
name|yqlResponse
init|=
name|YqlResponse
operator|.
name|builder
argument_list|()
operator|.
name|httpRequest
argument_list|(
name|uri
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|status
argument_list|(
name|response
operator|.
name|getStatusLine
argument_list|()
operator|.
name|getStatusCode
argument_list|()
argument_list|)
operator|.
name|body
argument_list|(
name|EntityUtils
operator|.
name|toString
argument_list|(
name|response
operator|.
name|getEntity
argument_list|()
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"YQL response: {}"
argument_list|,
name|yqlResponse
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|yqlResponse
return|;
block|}
block|}
DECL|method|buildParameters (final YqlConfiguration yqlConfiguration)
specifier|private
name|List
argument_list|<
name|NameValuePair
argument_list|>
name|buildParameters
parameter_list|(
specifier|final
name|YqlConfiguration
name|yqlConfiguration
parameter_list|)
block|{
specifier|final
name|List
argument_list|<
name|NameValuePair
argument_list|>
name|nameValuePairs
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|nameValuePairs
operator|.
name|add
argument_list|(
operator|new
name|BasicNameValuePair
argument_list|(
literal|"q"
argument_list|,
name|yqlConfiguration
operator|.
name|getQuery
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|nameValuePairs
operator|.
name|add
argument_list|(
operator|new
name|BasicNameValuePair
argument_list|(
literal|"format"
argument_list|,
name|yqlConfiguration
operator|.
name|getFormat
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|nameValuePairs
operator|.
name|add
argument_list|(
operator|new
name|BasicNameValuePair
argument_list|(
literal|"callback"
argument_list|,
name|yqlConfiguration
operator|.
name|getCallback
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|yqlConfiguration
operator|.
name|getCrossProduct
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|nameValuePairs
operator|.
name|add
argument_list|(
operator|new
name|BasicNameValuePair
argument_list|(
literal|"crossProduct"
argument_list|,
name|yqlConfiguration
operator|.
name|getCrossProduct
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|nameValuePairs
operator|.
name|add
argument_list|(
operator|new
name|BasicNameValuePair
argument_list|(
literal|"diagnostics"
argument_list|,
name|Boolean
operator|.
name|toString
argument_list|(
name|yqlConfiguration
operator|.
name|isDiagnostics
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|nameValuePairs
operator|.
name|add
argument_list|(
operator|new
name|BasicNameValuePair
argument_list|(
literal|"debug"
argument_list|,
name|Boolean
operator|.
name|toString
argument_list|(
name|yqlConfiguration
operator|.
name|isDebug
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|yqlConfiguration
operator|.
name|getEnv
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|nameValuePairs
operator|.
name|add
argument_list|(
operator|new
name|BasicNameValuePair
argument_list|(
literal|"env"
argument_list|,
name|yqlConfiguration
operator|.
name|getEnv
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|yqlConfiguration
operator|.
name|getJsonCompat
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|nameValuePairs
operator|.
name|add
argument_list|(
operator|new
name|BasicNameValuePair
argument_list|(
literal|"jsonCompat"
argument_list|,
name|yqlConfiguration
operator|.
name|getJsonCompat
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|nameValuePairs
return|;
block|}
block|}
end_class

end_unit

