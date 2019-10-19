begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.graphql
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|graphql
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|AsyncCallback
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
name|support
operator|.
name|DefaultAsyncProducer
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
name|util
operator|.
name|json
operator|.
name|JsonObject
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
name|HttpEntity
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
name|HttpHeaders
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
name|HttpPost
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
name|entity
operator|.
name|ContentType
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
name|entity
operator|.
name|StringEntity
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
name|util
operator|.
name|EntityUtils
import|;
end_import

begin_class
DECL|class|GraphqlProducer
specifier|public
class|class
name|GraphqlProducer
extends|extends
name|DefaultAsyncProducer
block|{
DECL|method|GraphqlProducer (GraphqlEndpoint endpoint)
specifier|public
name|GraphqlProducer
parameter_list|(
name|GraphqlEndpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|GraphqlEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|GraphqlEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|process (Exchange exchange, AsyncCallback callback)
specifier|public
name|boolean
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
block|{
try|try
block|{
name|CloseableHttpClient
name|httpClient
init|=
name|getEndpoint
argument_list|()
operator|.
name|getHttpclient
argument_list|()
decl_stmt|;
name|URI
name|httpUri
init|=
name|getEndpoint
argument_list|()
operator|.
name|getHttpUri
argument_list|()
decl_stmt|;
name|String
name|requestBody
init|=
name|buildRequestBody
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getQuery
argument_list|()
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|getOperationName
argument_list|()
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|getVariables
argument_list|()
argument_list|)
decl_stmt|;
name|HttpEntity
name|requestEntity
init|=
operator|new
name|StringEntity
argument_list|(
name|requestBody
argument_list|,
name|ContentType
operator|.
name|create
argument_list|(
literal|"application/json"
argument_list|,
literal|"UTF-8"
argument_list|)
argument_list|)
decl_stmt|;
name|HttpPost
name|httpPost
init|=
operator|new
name|HttpPost
argument_list|(
name|httpUri
argument_list|)
decl_stmt|;
name|httpPost
operator|.
name|setHeader
argument_list|(
name|HttpHeaders
operator|.
name|ACCEPT
argument_list|,
literal|"application/json"
argument_list|)
expr_stmt|;
name|httpPost
operator|.
name|setHeader
argument_list|(
name|HttpHeaders
operator|.
name|ACCEPT_ENCODING
argument_list|,
literal|"gzip"
argument_list|)
expr_stmt|;
name|httpPost
operator|.
name|setEntity
argument_list|(
name|requestEntity
argument_list|)
expr_stmt|;
name|String
name|responseContent
init|=
name|httpClient
operator|.
name|execute
argument_list|(
name|httpPost
argument_list|,
name|response
lambda|->
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
decl_stmt|;
name|exchange
operator|.
name|getMessage
argument_list|()
operator|.
name|setBody
argument_list|(
name|responseContent
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
DECL|method|buildRequestBody (String query, String operationName, JsonObject variables)
specifier|protected
specifier|static
name|String
name|buildRequestBody
parameter_list|(
name|String
name|query
parameter_list|,
name|String
name|operationName
parameter_list|,
name|JsonObject
name|variables
parameter_list|)
block|{
name|JsonObject
name|jsonObject
init|=
operator|new
name|JsonObject
argument_list|()
decl_stmt|;
name|jsonObject
operator|.
name|put
argument_list|(
literal|"query"
argument_list|,
name|query
argument_list|)
expr_stmt|;
name|jsonObject
operator|.
name|put
argument_list|(
literal|"operationName"
argument_list|,
name|operationName
argument_list|)
expr_stmt|;
name|jsonObject
operator|.
name|put
argument_list|(
literal|"variables"
argument_list|,
name|variables
operator|!=
literal|null
condition|?
name|variables
else|:
operator|new
name|JsonObject
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|jsonObject
operator|.
name|toJson
argument_list|()
return|;
block|}
block|}
end_class

end_unit

