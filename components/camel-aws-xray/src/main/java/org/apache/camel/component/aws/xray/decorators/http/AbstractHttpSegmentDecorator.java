begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.xray.decorators.http
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|aws
operator|.
name|xray
operator|.
name|decorators
operator|.
name|http
package|;
end_package

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|xray
operator|.
name|entities
operator|.
name|Entity
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
name|component
operator|.
name|aws
operator|.
name|xray
operator|.
name|decorators
operator|.
name|AbstractSegmentDecorator
import|;
end_import

begin_class
DECL|class|AbstractHttpSegmentDecorator
specifier|public
specifier|abstract
class|class
name|AbstractHttpSegmentDecorator
extends|extends
name|AbstractSegmentDecorator
block|{
DECL|field|POST_METHOD
specifier|public
specifier|static
specifier|final
name|String
name|POST_METHOD
init|=
literal|"POST"
decl_stmt|;
DECL|field|GET_METHOD
specifier|public
specifier|static
specifier|final
name|String
name|GET_METHOD
init|=
literal|"GET"
decl_stmt|;
annotation|@
name|Override
DECL|method|getOperationName (Exchange exchange, Endpoint endpoint)
specifier|public
name|String
name|getOperationName
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|)
block|{
return|return
name|getHttpMethod
argument_list|(
name|exchange
argument_list|,
name|endpoint
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|pre (Entity segment, Exchange exchange, Endpoint endpoint)
specifier|public
name|void
name|pre
parameter_list|(
name|Entity
name|segment
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|super
operator|.
name|pre
argument_list|(
name|segment
argument_list|,
name|exchange
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
name|String
name|httpUrl
init|=
name|getHttpUrl
argument_list|(
name|exchange
argument_list|,
name|endpoint
argument_list|)
decl_stmt|;
if|if
condition|(
name|httpUrl
operator|!=
literal|null
condition|)
block|{
name|segment
operator|.
name|putMetadata
argument_list|(
literal|"http.url"
argument_list|,
name|httpUrl
argument_list|)
expr_stmt|;
block|}
name|segment
operator|.
name|putMetadata
argument_list|(
literal|"http.method"
argument_list|,
name|getHttpMethod
argument_list|(
name|exchange
argument_list|,
name|endpoint
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|post (Entity segment, Exchange exchange, Endpoint endpoint)
specifier|public
name|void
name|post
parameter_list|(
name|Entity
name|segment
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|super
operator|.
name|post
argument_list|(
name|segment
argument_list|,
name|exchange
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
if|if
condition|(
name|exchange
operator|.
name|hasOut
argument_list|()
condition|)
block|{
name|Object
name|responseCode
init|=
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_RESPONSE_CODE
argument_list|)
decl_stmt|;
if|if
condition|(
name|responseCode
operator|instanceof
name|Integer
condition|)
block|{
name|segment
operator|.
name|putMetadata
argument_list|(
literal|"htt.response.code"
argument_list|,
name|responseCode
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|getHttpMethod (Exchange exchange, Endpoint endpoint)
specifier|protected
name|String
name|getHttpMethod
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|)
block|{
comment|// 1. Use method provided in header.
name|Object
name|method
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_METHOD
argument_list|)
decl_stmt|;
if|if
condition|(
name|method
operator|instanceof
name|String
condition|)
block|{
return|return
operator|(
name|String
operator|)
name|method
return|;
block|}
comment|// 2. GET if query string is provided in header.
if|if
condition|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_QUERY
argument_list|)
operator|!=
literal|null
condition|)
block|{
return|return
name|GET_METHOD
return|;
block|}
comment|// 3. GET if endpoint is configured with a query string.
if|if
condition|(
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|'?'
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
return|return
name|GET_METHOD
return|;
block|}
comment|// 4. POST if there is data to send (body is not null).
if|if
condition|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|POST_METHOD
return|;
block|}
comment|// 5. GET otherwise.
return|return
name|GET_METHOD
return|;
block|}
DECL|method|getHttpUrl (Exchange exchange, Endpoint endpoint)
specifier|protected
name|String
name|getHttpUrl
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|Object
name|url
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_URL
argument_list|)
decl_stmt|;
if|if
condition|(
name|url
operator|instanceof
name|String
condition|)
block|{
return|return
operator|(
name|String
operator|)
name|url
return|;
block|}
else|else
block|{
name|Object
name|uri
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_URI
argument_list|)
decl_stmt|;
if|if
condition|(
name|uri
operator|instanceof
name|String
condition|)
block|{
return|return
operator|(
name|String
operator|)
name|uri
return|;
block|}
else|else
block|{
comment|// Try to obtain from endpoint
name|int
name|index
init|=
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
operator|.
name|lastIndexOf
argument_list|(
literal|"http:"
argument_list|)
decl_stmt|;
if|if
condition|(
name|index
operator|!=
operator|-
literal|1
condition|)
block|{
return|return
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
operator|.
name|substring
argument_list|(
name|index
argument_list|)
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

