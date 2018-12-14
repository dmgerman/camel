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
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|PrivateKey
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
operator|.
name|Entry
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
name|AS2Charset
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
name|AS2Header
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
name|camel
operator|.
name|component
operator|.
name|as2
operator|.
name|api
operator|.
name|CanonicalOutputStream
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
name|util
operator|.
name|HttpMessageUtils
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
name|util
operator|.
name|MicUtils
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
name|util
operator|.
name|MicUtils
operator|.
name|ReceivedContentMic
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
name|Header
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
name|HeaderIterator
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
name|HttpEntityEnclosingRequest
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
name|HttpException
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
name|HttpResponse
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|message
operator|.
name|BasicHeader
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
name|util
operator|.
name|Args
import|;
end_import

begin_class
DECL|class|AS2MessageDispositionNotificationEntity
specifier|public
class|class
name|AS2MessageDispositionNotificationEntity
extends|extends
name|MimeEntity
block|{
DECL|field|ADDRESS_TYPE_PREFIX
specifier|private
specifier|static
specifier|final
name|String
name|ADDRESS_TYPE_PREFIX
init|=
literal|"rfc822;"
decl_stmt|;
DECL|field|MTA_NAME_TYPE_PREFIX
specifier|private
specifier|static
specifier|final
name|String
name|MTA_NAME_TYPE_PREFIX
init|=
literal|"dns;"
decl_stmt|;
DECL|field|REPORTING_UA
specifier|private
specifier|static
specifier|final
name|String
name|REPORTING_UA
init|=
literal|"Reporting-UA"
decl_stmt|;
DECL|field|MDN_GATEWAY
specifier|private
specifier|static
specifier|final
name|String
name|MDN_GATEWAY
init|=
literal|"MDN-Gateway"
decl_stmt|;
DECL|field|FINAL_RECIPIENT
specifier|private
specifier|static
specifier|final
name|String
name|FINAL_RECIPIENT
init|=
literal|"Final-Recipient"
decl_stmt|;
DECL|field|ORIGINAL_MESSAGE_ID
specifier|private
specifier|static
specifier|final
name|String
name|ORIGINAL_MESSAGE_ID
init|=
literal|"Original-Message-ID"
decl_stmt|;
DECL|field|AS2_DISPOSITION
specifier|private
specifier|static
specifier|final
name|String
name|AS2_DISPOSITION
init|=
literal|"Disposition"
decl_stmt|;
DECL|field|FAILURE
specifier|private
specifier|static
specifier|final
name|String
name|FAILURE
init|=
literal|"Failure"
decl_stmt|;
DECL|field|ERROR
specifier|private
specifier|static
specifier|final
name|String
name|ERROR
init|=
literal|"Error"
decl_stmt|;
DECL|field|WARNING
specifier|private
specifier|static
specifier|final
name|String
name|WARNING
init|=
literal|"Warning"
decl_stmt|;
DECL|field|RECEIVED_CONTENT_MIC
specifier|private
specifier|static
specifier|final
name|String
name|RECEIVED_CONTENT_MIC
init|=
literal|"Received-content-MIC"
decl_stmt|;
DECL|field|reportingUA
specifier|private
name|String
name|reportingUA
decl_stmt|;
comment|// TODO determine if we need to support this field.
DECL|field|mtnName
specifier|private
name|String
name|mtnName
decl_stmt|;
DECL|field|finalRecipient
specifier|private
name|String
name|finalRecipient
decl_stmt|;
DECL|field|originalMessageId
specifier|private
name|String
name|originalMessageId
decl_stmt|;
DECL|field|dispositionMode
specifier|private
name|DispositionMode
name|dispositionMode
decl_stmt|;
DECL|field|dispositionType
specifier|private
name|AS2DispositionType
name|dispositionType
decl_stmt|;
DECL|field|dispositionModifier
specifier|private
name|AS2DispositionModifier
name|dispositionModifier
decl_stmt|;
DECL|field|failureFields
specifier|private
name|String
index|[]
name|failureFields
decl_stmt|;
DECL|field|errorFields
specifier|private
name|String
index|[]
name|errorFields
decl_stmt|;
DECL|field|warningFields
specifier|private
name|String
index|[]
name|warningFields
decl_stmt|;
DECL|field|extensionFields
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|extensionFields
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|receivedContentMic
specifier|private
name|ReceivedContentMic
name|receivedContentMic
decl_stmt|;
DECL|method|AS2MessageDispositionNotificationEntity (HttpEntityEnclosingRequest request, HttpResponse response, DispositionMode dispositionMode, AS2DispositionType dispositionType, AS2DispositionModifier dispositionModifier, String[] failureFields, String[] errorFields, String[] warningFields, Map<String, String> extensionFields, String charset, boolean isMainBody, PrivateKey decryptingPrivateKey)
specifier|public
name|AS2MessageDispositionNotificationEntity
parameter_list|(
name|HttpEntityEnclosingRequest
name|request
parameter_list|,
name|HttpResponse
name|response
parameter_list|,
name|DispositionMode
name|dispositionMode
parameter_list|,
name|AS2DispositionType
name|dispositionType
parameter_list|,
name|AS2DispositionModifier
name|dispositionModifier
parameter_list|,
name|String
index|[]
name|failureFields
parameter_list|,
name|String
index|[]
name|errorFields
parameter_list|,
name|String
index|[]
name|warningFields
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|extensionFields
parameter_list|,
name|String
name|charset
parameter_list|,
name|boolean
name|isMainBody
parameter_list|,
name|PrivateKey
name|decryptingPrivateKey
parameter_list|)
throws|throws
name|HttpException
block|{
name|setMainBody
argument_list|(
name|isMainBody
argument_list|)
expr_stmt|;
name|setContentType
argument_list|(
name|ContentType
operator|.
name|create
argument_list|(
name|AS2MimeType
operator|.
name|MESSAGE_DISPOSITION_NOTIFICATION
argument_list|,
name|charset
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|finalRecipient
operator|=
name|HttpMessageUtils
operator|.
name|getHeaderValue
argument_list|(
name|request
argument_list|,
name|AS2Header
operator|.
name|AS2_TO
argument_list|)
expr_stmt|;
if|if
condition|(
name|this
operator|.
name|finalRecipient
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|HttpException
argument_list|(
literal|"The "
operator|+
name|AS2Header
operator|.
name|AS2_TO
operator|+
literal|" is missing"
argument_list|)
throw|;
block|}
name|this
operator|.
name|originalMessageId
operator|=
name|HttpMessageUtils
operator|.
name|getHeaderValue
argument_list|(
name|request
argument_list|,
name|AS2Header
operator|.
name|MESSAGE_ID
argument_list|)
expr_stmt|;
name|this
operator|.
name|receivedContentMic
operator|=
name|MicUtils
operator|.
name|createReceivedContentMic
argument_list|(
name|request
argument_list|,
name|decryptingPrivateKey
argument_list|)
expr_stmt|;
name|this
operator|.
name|reportingUA
operator|=
name|HttpMessageUtils
operator|.
name|getHeaderValue
argument_list|(
name|response
argument_list|,
name|AS2Header
operator|.
name|SERVER
argument_list|)
expr_stmt|;
name|this
operator|.
name|dispositionMode
operator|=
name|Args
operator|.
name|notNull
argument_list|(
name|dispositionMode
argument_list|,
literal|"Disposition Mode"
argument_list|)
expr_stmt|;
name|this
operator|.
name|dispositionType
operator|=
name|Args
operator|.
name|notNull
argument_list|(
name|dispositionType
argument_list|,
literal|"Disposition Type"
argument_list|)
expr_stmt|;
name|this
operator|.
name|dispositionModifier
operator|=
name|dispositionModifier
expr_stmt|;
name|this
operator|.
name|failureFields
operator|=
name|failureFields
expr_stmt|;
name|this
operator|.
name|errorFields
operator|=
name|errorFields
expr_stmt|;
name|this
operator|.
name|warningFields
operator|=
name|warningFields
expr_stmt|;
if|if
condition|(
name|extensionFields
operator|==
literal|null
operator|||
name|extensionFields
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|this
operator|.
name|extensionFields
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|extensionFields
operator|.
name|putAll
argument_list|(
name|extensionFields
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|AS2MessageDispositionNotificationEntity (String reportingUA, String mtnName, String finalRecipient, String originalMessageId, DispositionMode dispositionMode, AS2DispositionType dispositionType, AS2DispositionModifier dispositionModifier, String[] failureFields, String[] errorFields, String[] warningFields, Map<String, String> extensionFields, ReceivedContentMic receivedContentMic)
specifier|public
name|AS2MessageDispositionNotificationEntity
parameter_list|(
name|String
name|reportingUA
parameter_list|,
name|String
name|mtnName
parameter_list|,
name|String
name|finalRecipient
parameter_list|,
name|String
name|originalMessageId
parameter_list|,
name|DispositionMode
name|dispositionMode
parameter_list|,
name|AS2DispositionType
name|dispositionType
parameter_list|,
name|AS2DispositionModifier
name|dispositionModifier
parameter_list|,
name|String
index|[]
name|failureFields
parameter_list|,
name|String
index|[]
name|errorFields
parameter_list|,
name|String
index|[]
name|warningFields
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|extensionFields
parameter_list|,
name|ReceivedContentMic
name|receivedContentMic
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|this
operator|.
name|reportingUA
operator|=
name|reportingUA
expr_stmt|;
name|this
operator|.
name|mtnName
operator|=
name|mtnName
expr_stmt|;
name|this
operator|.
name|finalRecipient
operator|=
name|finalRecipient
expr_stmt|;
name|this
operator|.
name|originalMessageId
operator|=
name|originalMessageId
expr_stmt|;
name|this
operator|.
name|dispositionMode
operator|=
name|dispositionMode
expr_stmt|;
name|this
operator|.
name|dispositionType
operator|=
name|dispositionType
expr_stmt|;
name|this
operator|.
name|dispositionModifier
operator|=
name|dispositionModifier
expr_stmt|;
name|this
operator|.
name|failureFields
operator|=
name|failureFields
expr_stmt|;
name|this
operator|.
name|errorFields
operator|=
name|errorFields
expr_stmt|;
name|this
operator|.
name|warningFields
operator|=
name|warningFields
expr_stmt|;
name|this
operator|.
name|extensionFields
operator|=
name|extensionFields
expr_stmt|;
name|this
operator|.
name|receivedContentMic
operator|=
name|receivedContentMic
expr_stmt|;
block|}
DECL|method|getReportingUA ()
specifier|public
name|String
name|getReportingUA
parameter_list|()
block|{
return|return
name|reportingUA
return|;
block|}
DECL|method|getMtnName ()
specifier|public
name|String
name|getMtnName
parameter_list|()
block|{
return|return
name|mtnName
return|;
block|}
DECL|method|getFinalRecipient ()
specifier|public
name|String
name|getFinalRecipient
parameter_list|()
block|{
return|return
name|finalRecipient
return|;
block|}
DECL|method|getOriginalMessageId ()
specifier|public
name|String
name|getOriginalMessageId
parameter_list|()
block|{
return|return
name|originalMessageId
return|;
block|}
DECL|method|getDispositionMode ()
specifier|public
name|DispositionMode
name|getDispositionMode
parameter_list|()
block|{
return|return
name|dispositionMode
return|;
block|}
DECL|method|getDispositionType ()
specifier|public
name|AS2DispositionType
name|getDispositionType
parameter_list|()
block|{
return|return
name|dispositionType
return|;
block|}
DECL|method|getDispositionModifier ()
specifier|public
name|AS2DispositionModifier
name|getDispositionModifier
parameter_list|()
block|{
return|return
name|dispositionModifier
return|;
block|}
DECL|method|getFailureFields ()
specifier|public
name|String
index|[]
name|getFailureFields
parameter_list|()
block|{
return|return
name|failureFields
return|;
block|}
DECL|method|getErrorFields ()
specifier|public
name|String
index|[]
name|getErrorFields
parameter_list|()
block|{
return|return
name|errorFields
return|;
block|}
DECL|method|getWarningFields ()
specifier|public
name|String
index|[]
name|getWarningFields
parameter_list|()
block|{
return|return
name|warningFields
return|;
block|}
DECL|method|getExtensionFields ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getExtensionFields
parameter_list|()
block|{
return|return
name|extensionFields
return|;
block|}
DECL|method|getReceivedContentMic ()
specifier|public
name|ReceivedContentMic
name|getReceivedContentMic
parameter_list|()
block|{
return|return
name|receivedContentMic
return|;
block|}
annotation|@
name|Override
DECL|method|writeTo (OutputStream outstream)
specifier|public
name|void
name|writeTo
parameter_list|(
name|OutputStream
name|outstream
parameter_list|)
throws|throws
name|IOException
block|{
name|NoCloseOutputStream
name|ncos
init|=
operator|new
name|NoCloseOutputStream
argument_list|(
name|outstream
argument_list|)
decl_stmt|;
try|try
init|(
name|CanonicalOutputStream
name|canonicalOutstream
init|=
operator|new
name|CanonicalOutputStream
argument_list|(
name|ncos
argument_list|,
name|AS2Charset
operator|.
name|US_ASCII
argument_list|)
init|)
block|{
comment|// Write out mime part headers if this is not the main body of
comment|// message.
if|if
condition|(
operator|!
name|isMainBody
argument_list|()
condition|)
block|{
name|HeaderIterator
name|it
init|=
name|headerIterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Header
name|header
init|=
name|it
operator|.
name|nextHeader
argument_list|()
decl_stmt|;
name|canonicalOutstream
operator|.
name|writeln
argument_list|(
name|header
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|canonicalOutstream
operator|.
name|writeln
argument_list|()
expr_stmt|;
comment|// ensure empty line between
comment|// headers and body; RFC2046 -
comment|// 5.1.1
block|}
if|if
condition|(
name|reportingUA
operator|!=
literal|null
condition|)
block|{
name|Header
name|reportingUAField
init|=
operator|new
name|BasicHeader
argument_list|(
name|REPORTING_UA
argument_list|,
name|reportingUA
argument_list|)
decl_stmt|;
name|canonicalOutstream
operator|.
name|writeln
argument_list|(
name|reportingUAField
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|mtnName
operator|!=
literal|null
condition|)
block|{
name|Header
name|mdnGatewayField
init|=
operator|new
name|BasicHeader
argument_list|(
name|MDN_GATEWAY
argument_list|,
name|MTA_NAME_TYPE_PREFIX
operator|+
name|mtnName
argument_list|)
decl_stmt|;
name|canonicalOutstream
operator|.
name|writeln
argument_list|(
name|mdnGatewayField
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Header
name|finalRecipientField
init|=
operator|new
name|BasicHeader
argument_list|(
name|FINAL_RECIPIENT
argument_list|,
name|ADDRESS_TYPE_PREFIX
operator|+
name|finalRecipient
argument_list|)
decl_stmt|;
name|canonicalOutstream
operator|.
name|writeln
argument_list|(
name|finalRecipientField
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|originalMessageId
operator|!=
literal|null
condition|)
block|{
name|Header
name|originalMessageIdField
init|=
operator|new
name|BasicHeader
argument_list|(
name|ORIGINAL_MESSAGE_ID
argument_list|,
name|originalMessageId
argument_list|)
decl_stmt|;
name|canonicalOutstream
operator|.
name|writeln
argument_list|(
name|originalMessageIdField
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|String
name|as2Disposition
init|=
name|dispositionMode
operator|.
name|toString
argument_list|()
operator|+
literal|";"
operator|+
name|dispositionType
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
name|dispositionModifier
operator|!=
literal|null
condition|)
block|{
name|as2Disposition
operator|=
name|as2Disposition
operator|+
literal|"/"
operator|+
name|dispositionModifier
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
name|Header
name|as2DispositionField
init|=
operator|new
name|BasicHeader
argument_list|(
name|AS2_DISPOSITION
argument_list|,
name|as2Disposition
argument_list|)
decl_stmt|;
name|canonicalOutstream
operator|.
name|writeln
argument_list|(
name|as2DispositionField
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|failureFields
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|field
range|:
name|failureFields
control|)
block|{
name|Header
name|failureField
init|=
operator|new
name|BasicHeader
argument_list|(
name|FAILURE
argument_list|,
name|field
argument_list|)
decl_stmt|;
name|canonicalOutstream
operator|.
name|writeln
argument_list|(
name|failureField
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|errorFields
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|field
range|:
name|errorFields
control|)
block|{
name|Header
name|errorField
init|=
operator|new
name|BasicHeader
argument_list|(
name|ERROR
argument_list|,
name|field
argument_list|)
decl_stmt|;
name|canonicalOutstream
operator|.
name|writeln
argument_list|(
name|errorField
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|failureFields
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|field
range|:
name|failureFields
control|)
block|{
name|Header
name|failureField
init|=
operator|new
name|BasicHeader
argument_list|(
name|WARNING
argument_list|,
name|field
argument_list|)
decl_stmt|;
name|canonicalOutstream
operator|.
name|writeln
argument_list|(
name|failureField
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|extensionFields
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|extensionFields
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|Header
name|failureField
init|=
operator|new
name|BasicHeader
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
decl_stmt|;
name|canonicalOutstream
operator|.
name|writeln
argument_list|(
name|failureField
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|receivedContentMic
operator|!=
literal|null
condition|)
block|{
name|Header
name|as2ReceivedContentMicField
init|=
operator|new
name|BasicHeader
argument_list|(
name|RECEIVED_CONTENT_MIC
argument_list|,
name|receivedContentMic
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|canonicalOutstream
operator|.
name|writeln
argument_list|(
name|as2ReceivedContentMicField
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

