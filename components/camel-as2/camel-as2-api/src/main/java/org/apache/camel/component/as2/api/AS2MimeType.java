begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.as2.api
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
package|;
end_package

begin_interface
DECL|interface|AS2MimeType
specifier|public
interface|interface
name|AS2MimeType
block|{
comment|/**      * Mime Type for Multipart Signed Data      */
DECL|field|MULTIPART_SIGNED
specifier|public
specifier|static
specifier|final
name|String
name|MULTIPART_SIGNED
init|=
literal|"multipart/signed"
decl_stmt|;
comment|/**      * Mime Type for Application PKCS7 Signature      */
DECL|field|APPLICATION_PKCS7_SIGNATURE
specifier|public
specifier|static
specifier|final
name|String
name|APPLICATION_PKCS7_SIGNATURE
init|=
literal|"application/pkcs7-signature"
decl_stmt|;
comment|/**      * Mime Type for Application PKCS7 Signature      */
DECL|field|APPLICATION_PKCS7_MIME
specifier|public
specifier|static
specifier|final
name|String
name|APPLICATION_PKCS7_MIME
init|=
literal|"application/pkcs7-mime"
decl_stmt|;
comment|/**      * Mime Type for Text/Plain Data      */
DECL|field|TEXT_PLAIN
specifier|public
specifier|static
specifier|final
name|String
name|TEXT_PLAIN
init|=
literal|"text/plain"
decl_stmt|;
comment|/**      * Mime Type for Application/EDIFACT      */
DECL|field|APPLICATION_EDIFACT
specifier|public
specifier|static
specifier|final
name|String
name|APPLICATION_EDIFACT
init|=
literal|"application/edifact"
decl_stmt|;
comment|/**      * Mime Type for Application/EDI-X12      */
DECL|field|APPLICATION_EDI_X12
specifier|public
specifier|static
specifier|final
name|String
name|APPLICATION_EDI_X12
init|=
literal|"application/edi-x12"
decl_stmt|;
comment|/**      * Mime Type for Application/EDI-consent      */
DECL|field|APPLICATION_EDI_CONSENT
specifier|public
specifier|static
specifier|final
name|String
name|APPLICATION_EDI_CONSENT
init|=
literal|"application/edi-consent"
decl_stmt|;
comment|/**      * Mime Type for Multipart/Report      */
DECL|field|MULTIPART_REPORT
specifier|public
specifier|static
specifier|final
name|String
name|MULTIPART_REPORT
init|=
literal|"multipart/report"
decl_stmt|;
comment|/**      * Mime Type for Message/Disposition-Notification      */
DECL|field|MESSAGE_DISPOSITION_NOTIFICATION
specifier|public
specifier|static
specifier|final
name|String
name|MESSAGE_DISPOSITION_NOTIFICATION
init|=
literal|"message/disposition-notification"
decl_stmt|;
block|}
end_interface

end_unit

