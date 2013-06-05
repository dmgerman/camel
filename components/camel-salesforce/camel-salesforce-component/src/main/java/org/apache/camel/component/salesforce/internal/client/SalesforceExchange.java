begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|client
operator|.
name|ContentExchange
import|;
end_import

begin_comment
comment|/**  * Wraps a Salesforce Http Exchange  */
end_comment

begin_class
DECL|class|SalesforceExchange
specifier|public
class|class
name|SalesforceExchange
extends|extends
name|ContentExchange
block|{
DECL|field|client
specifier|private
name|AbstractClientBase
name|client
decl_stmt|;
DECL|method|getClient ()
specifier|public
name|AbstractClientBase
name|getClient
parameter_list|()
block|{
return|return
name|client
return|;
block|}
DECL|method|setClient (AbstractClientBase client)
specifier|public
name|void
name|setClient
parameter_list|(
name|AbstractClientBase
name|client
parameter_list|)
block|{
name|this
operator|.
name|client
operator|=
name|client
expr_stmt|;
block|}
block|}
end_class

end_unit

