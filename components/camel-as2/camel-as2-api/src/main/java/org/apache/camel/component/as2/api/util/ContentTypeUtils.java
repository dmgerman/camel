begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.as2.api.util
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
name|util
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
name|camel
operator|.
name|component
operator|.
name|as2
operator|.
name|api
operator|.
name|AS2MimeType
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
DECL|class|ContentTypeUtils
specifier|public
specifier|final
class|class
name|ContentTypeUtils
block|{
DECL|method|ContentTypeUtils ()
specifier|private
name|ContentTypeUtils
parameter_list|()
block|{     }
DECL|method|isEDIMessageContentType (ContentType ediMessageContentType)
specifier|public
specifier|static
name|boolean
name|isEDIMessageContentType
parameter_list|(
name|ContentType
name|ediMessageContentType
parameter_list|)
block|{
switch|switch
condition|(
name|ediMessageContentType
operator|.
name|getMimeType
argument_list|()
operator|.
name|toLowerCase
argument_list|()
condition|)
block|{
case|case
name|AS2MediaType
operator|.
name|APPLICATION_EDIFACT
case|:
return|return
literal|true
return|;
case|case
name|AS2MediaType
operator|.
name|APPLICATION_EDI_X12
case|:
return|return
literal|true
return|;
case|case
name|AS2MediaType
operator|.
name|APPLICATION_EDI_CONSENT
case|:
return|return
literal|true
return|;
default|default:
return|return
literal|false
return|;
block|}
block|}
DECL|method|isPkcs7SignatureType (ContentType pcks7SignatureType)
specifier|public
specifier|static
name|boolean
name|isPkcs7SignatureType
parameter_list|(
name|ContentType
name|pcks7SignatureType
parameter_list|)
block|{
switch|switch
condition|(
name|pcks7SignatureType
operator|.
name|getMimeType
argument_list|()
operator|.
name|toLowerCase
argument_list|()
condition|)
block|{
case|case
name|AS2MimeType
operator|.
name|APPLICATION_PKCS7_SIGNATURE
case|:
return|return
literal|true
return|;
default|default:
return|return
literal|false
return|;
block|}
block|}
block|}
end_class

end_unit

