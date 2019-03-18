begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mail
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mail
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|BodyPart
import|;
end_import

begin_comment
comment|/**  * Resolver to determine Content-Transfer-Encoding for file attachments.  *<br/>  * Normally this will be determined automatically, this resolver can be used to  * override this behavior.  */
end_comment

begin_interface
DECL|interface|AttachmentsContentTransferEncodingResolver
specifier|public
interface|interface
name|AttachmentsContentTransferEncodingResolver
block|{
comment|/**      * Resolves the content-transfer-encoding.      *<p/>      * Return<tt>null</tt> if you cannot resolve a content-transfer-encoding or      * want to rely on the mail provider to resolve it for you.      *      * @param messageBodyPart the body part      * @return the content-transfer-encoding or<tt>null</tt> to rely on the mail provider      */
DECL|method|resolveContentTransferEncoding (BodyPart messageBodyPart)
name|String
name|resolveContentTransferEncoding
parameter_list|(
name|BodyPart
name|messageBodyPart
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

