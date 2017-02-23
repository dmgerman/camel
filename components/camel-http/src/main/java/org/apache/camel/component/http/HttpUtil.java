begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.http
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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Optional
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|httpclient
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
name|commons
operator|.
name|httpclient
operator|.
name|HttpMethod
import|;
end_import

begin_class
DECL|class|HttpUtil
specifier|public
specifier|final
class|class
name|HttpUtil
block|{
DECL|method|HttpUtil ()
specifier|private
name|HttpUtil
parameter_list|()
block|{     }
DECL|method|responseHeader (HttpMethod method, String headerName)
specifier|public
specifier|static
name|Optional
argument_list|<
name|Header
argument_list|>
name|responseHeader
parameter_list|(
name|HttpMethod
name|method
parameter_list|,
name|String
name|headerName
parameter_list|)
block|{
return|return
name|Optional
operator|.
name|ofNullable
argument_list|(
name|method
operator|.
name|getResponseHeader
argument_list|(
name|headerName
argument_list|)
argument_list|)
return|;
block|}
DECL|method|responseHeaderValue (HttpMethod method, String headerName)
specifier|public
specifier|static
name|Optional
argument_list|<
name|String
argument_list|>
name|responseHeaderValue
parameter_list|(
name|HttpMethod
name|method
parameter_list|,
name|String
name|headerName
parameter_list|)
block|{
return|return
name|responseHeader
argument_list|(
name|method
argument_list|,
name|headerName
argument_list|)
operator|.
name|map
argument_list|(
name|m
lambda|->
name|Optional
operator|.
name|ofNullable
argument_list|(
name|m
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
operator|.
name|orElseGet
argument_list|(
name|Optional
operator|::
name|empty
argument_list|)
return|;
block|}
block|}
end_class

end_unit

