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
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|HttpRequest
import|;
end_import

begin_class
DECL|class|DrinkValidationHandler
specifier|public
class|class
name|DrinkValidationHandler
extends|extends
name|BasicValidationHandler
block|{
DECL|field|header
specifier|private
specifier|final
name|String
name|header
decl_stmt|;
DECL|method|DrinkValidationHandler (String expectedMethod, String expectedQuery, Object expectedContent, String header)
specifier|public
name|DrinkValidationHandler
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
name|header
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
literal|null
argument_list|)
expr_stmt|;
name|this
operator|.
name|header
operator|=
name|header
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|buildResponse (HttpRequest request)
specifier|protected
name|String
name|buildResponse
parameter_list|(
name|HttpRequest
name|request
parameter_list|)
block|{
return|return
literal|"Drinking "
operator|+
name|request
operator|.
name|getFirstHeader
argument_list|(
name|header
argument_list|)
operator|.
name|getValue
argument_list|()
return|;
block|}
block|}
end_class

end_unit

