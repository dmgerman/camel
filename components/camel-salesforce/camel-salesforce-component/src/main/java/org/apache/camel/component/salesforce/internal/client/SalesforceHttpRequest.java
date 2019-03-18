begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce.internal.client
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|salesforce
operator|.
name|internal
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
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|client
operator|.
name|HttpClient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|client
operator|.
name|HttpConversation
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|client
operator|.
name|HttpRequest
import|;
end_import

begin_comment
comment|/**  * Salesforce HTTP Request, exposes {@link HttpConversation} field.  */
end_comment

begin_class
DECL|class|SalesforceHttpRequest
specifier|public
class|class
name|SalesforceHttpRequest
extends|extends
name|HttpRequest
block|{
DECL|method|SalesforceHttpRequest (HttpClient client, HttpConversation conversation, URI uri)
specifier|public
name|SalesforceHttpRequest
parameter_list|(
name|HttpClient
name|client
parameter_list|,
name|HttpConversation
name|conversation
parameter_list|,
name|URI
name|uri
parameter_list|)
block|{
name|super
argument_list|(
name|client
argument_list|,
name|conversation
argument_list|,
name|uri
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getConversation ()
specifier|protected
name|HttpConversation
name|getConversation
parameter_list|()
block|{
return|return
name|super
operator|.
name|getConversation
argument_list|()
return|;
block|}
block|}
end_class

end_unit

