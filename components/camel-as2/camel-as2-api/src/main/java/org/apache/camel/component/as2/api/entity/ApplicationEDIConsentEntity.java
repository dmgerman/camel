begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.as2.api.entity
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|as2
operator|.
name|api
operator|.
name|entity
package|;
end_package

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
name|as2
operator|.
name|api
operator|.
name|AS2MediaType
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

begin_class
DECL|class|ApplicationEDIConsentEntity
specifier|public
class|class
name|ApplicationEDIConsentEntity
extends|extends
name|ApplicationEDIEntity
block|{
DECL|method|ApplicationEDIConsentEntity (String content, String charset, String contentTransferEncoding, boolean isMainBody)
specifier|public
name|ApplicationEDIConsentEntity
parameter_list|(
name|String
name|content
parameter_list|,
name|String
name|charset
parameter_list|,
name|String
name|contentTransferEncoding
parameter_list|,
name|boolean
name|isMainBody
parameter_list|)
block|{
name|super
argument_list|(
name|content
argument_list|,
name|ContentType
operator|.
name|create
argument_list|(
name|AS2MediaType
operator|.
name|APPLICATION_EDI_CONSENT
argument_list|,
name|charset
argument_list|)
argument_list|,
name|contentTransferEncoding
argument_list|,
name|isMainBody
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

