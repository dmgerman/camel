begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.http.handler
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|http
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
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
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

begin_comment
comment|/**  * Similar to {@link BasicValidationHandler} but validates the raw query instead.  */
end_comment

begin_class
DECL|class|BasicRawQueryValidationHandler
specifier|public
class|class
name|BasicRawQueryValidationHandler
extends|extends
name|BasicValidationHandler
block|{
DECL|method|BasicRawQueryValidationHandler (String expectedMethod, String expectedQuery, Object expectedContent, String responseContent)
specifier|public
name|BasicRawQueryValidationHandler
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
block|}
annotation|@
name|Override
DECL|method|validateQuery (HttpRequest request)
specifier|protected
name|boolean
name|validateQuery
parameter_list|(
name|HttpRequest
name|request
parameter_list|)
throws|throws
name|IOException
block|{
try|try
block|{
name|String
name|query
init|=
operator|new
name|URI
argument_list|(
name|request
operator|.
name|getRequestLine
argument_list|()
operator|.
name|getUri
argument_list|()
argument_list|)
operator|.
name|getRawQuery
argument_list|()
decl_stmt|;
if|if
condition|(
name|expectedQuery
operator|!=
literal|null
operator|&&
operator|!
name|expectedQuery
operator|.
name|equals
argument_list|(
name|query
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
name|e
argument_list|)
throw|;
block|}
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

