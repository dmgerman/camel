begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.http4.handler
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|http4
operator|.
name|handler
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|Map
operator|.
name|Entry
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
name|Header
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
name|HttpException
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
name|HttpRequest
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
name|HttpResponse
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
name|HttpStatus
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
name|protocol
operator|.
name|HttpContext
import|;
end_import

begin_class
DECL|class|HeaderValidationHandler
specifier|public
class|class
name|HeaderValidationHandler
extends|extends
name|BasicValidationHandler
block|{
DECL|field|expectedHeaders
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|expectedHeaders
decl_stmt|;
DECL|method|HeaderValidationHandler (String expectedMethod, String expectedQuery, Object expectedContent, String responseContent, Map<String, String> expectedHeaders)
specifier|public
name|HeaderValidationHandler
parameter_list|(
name|String
name|expectedMethod
parameter_list|,
name|String
name|expectedQuery
parameter_list|,
name|Object
name|expectedContent
parameter_list|,
name|String
name|responseContent
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|expectedHeaders
parameter_list|)
block|{
name|super
argument_list|(
name|expectedMethod
argument_list|,
name|expectedQuery
argument_list|,
name|expectedContent
argument_list|,
name|responseContent
argument_list|)
expr_stmt|;
name|this
operator|.
name|expectedHeaders
operator|=
name|expectedHeaders
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|handle (final HttpRequest request, final HttpResponse response, final HttpContext context)
specifier|public
name|void
name|handle
parameter_list|(
specifier|final
name|HttpRequest
name|request
parameter_list|,
specifier|final
name|HttpResponse
name|response
parameter_list|,
specifier|final
name|HttpContext
name|context
parameter_list|)
throws|throws
name|HttpException
throws|,
name|IOException
block|{
if|if
condition|(
name|expectedHeaders
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|expectedHeaders
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|boolean
name|headerExist
init|=
literal|false
decl_stmt|;
name|Header
index|[]
name|headers
init|=
name|request
operator|.
name|getHeaders
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Header
name|header
range|:
name|headers
control|)
block|{
if|if
condition|(
name|header
operator|.
name|getValue
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
condition|)
block|{
name|headerExist
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
operator|!
name|headerExist
condition|)
block|{
name|response
operator|.
name|setStatusCode
argument_list|(
name|HttpStatus
operator|.
name|SC_EXPECTATION_FAILED
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
block|}
name|super
operator|.
name|handle
argument_list|(
name|request
argument_list|,
name|response
argument_list|,
name|context
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

