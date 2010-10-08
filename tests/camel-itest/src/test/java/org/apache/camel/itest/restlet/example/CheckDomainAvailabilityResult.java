begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.restlet.example
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|restlet
operator|.
name|example
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
import|;
end_import

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"CheckDomainAvailabilityResult"
argument_list|)
DECL|class|CheckDomainAvailabilityResult
specifier|public
class|class
name|CheckDomainAvailabilityResult
block|{
DECL|field|requestId
specifier|private
name|String
name|requestId
decl_stmt|;
DECL|field|responseBody
specifier|private
name|String
name|responseBody
decl_stmt|;
DECL|method|getRequestId ()
specifier|public
name|String
name|getRequestId
parameter_list|()
block|{
return|return
name|requestId
return|;
block|}
DECL|method|setRequestId (String requestId)
specifier|public
name|void
name|setRequestId
parameter_list|(
name|String
name|requestId
parameter_list|)
block|{
name|this
operator|.
name|requestId
operator|=
name|requestId
expr_stmt|;
block|}
DECL|method|getResponseBody ()
specifier|public
name|String
name|getResponseBody
parameter_list|()
block|{
return|return
name|responseBody
return|;
block|}
DECL|method|setResponseBody (String responseBody)
specifier|public
name|void
name|setResponseBody
parameter_list|(
name|String
name|responseBody
parameter_list|)
block|{
name|this
operator|.
name|responseBody
operator|=
name|responseBody
expr_stmt|;
block|}
block|}
end_class

end_unit

