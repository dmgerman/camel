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
DECL|interface|AS2MediaType
specifier|public
interface|interface
name|AS2MediaType
block|{
comment|/**      * Media Type for Multipart Signed Data      */
DECL|field|MULTIPART_SIGNED
name|String
name|MULTIPART_SIGNED
init|=
literal|"multipart/signed; protocol=\"application/pkcs7-signature\""
decl_stmt|;
comment|/**      * Media Type for Application PKCS7 Signature      */
DECL|field|APPLICATION_PKCS7_SIGNATURE
name|String
name|APPLICATION_PKCS7_SIGNATURE
init|=
literal|"application/pkcs7-signature; name=smime.p7s; smime-type=signed-data"
decl_stmt|;
comment|/**      * Media Type for Application PKCS7 Enveloped Data      */
DECL|field|APPLICATION_PKCS7_MIME_ENVELOPED
name|String
name|APPLICATION_PKCS7_MIME_ENVELOPED
init|=
literal|"application/pkcs7-mime; smime-type=enveloped-data; name=smime.p7m"
decl_stmt|;
comment|/**      * Media Type for Application PKCS7 Compressed Data      */
DECL|field|APPLICATION_PKCS7_MIME_COMPRESSED
name|String
name|APPLICATION_PKCS7_MIME_COMPRESSED
init|=
literal|"application/pkcs7-mime; smime-type=compressed-data; name=smime.p7z"
decl_stmt|;
comment|/**      * Media Type for Text/Plain Data      */
DECL|field|TEXT_PLAIN
name|String
name|TEXT_PLAIN
init|=
literal|"text/plain"
decl_stmt|;
comment|/**      * Media Type for Application/EDIFACT      */
DECL|field|APPLICATION_EDIFACT
name|String
name|APPLICATION_EDIFACT
init|=
literal|"application/edifact"
decl_stmt|;
comment|/**      * Media Type for Application/EDI-X12      */
DECL|field|APPLICATION_EDI_X12
name|String
name|APPLICATION_EDI_X12
init|=
literal|"application/edi-x12"
decl_stmt|;
comment|/**      * Media Type for Application/EDI-consent      */
DECL|field|APPLICATION_EDI_CONSENT
name|String
name|APPLICATION_EDI_CONSENT
init|=
literal|"application/edi-consent"
decl_stmt|;
block|}
end_interface

end_unit

