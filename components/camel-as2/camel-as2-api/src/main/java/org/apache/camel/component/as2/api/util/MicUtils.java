begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|security
operator|.
name|MessageDigest
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|NoSuchAlgorithmException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|NoSuchProviderException
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
name|AS2MicAlgorithm
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
name|entity
operator|.
name|DispositionNotificationOptions
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
name|entity
operator|.
name|DispositionNotificationOptionsParser
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
name|HttpEntity
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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
DECL|class|MicUtils
specifier|public
specifier|final
class|class
name|MicUtils
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|MicUtils
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|MicUtils ()
specifier|private
name|MicUtils
parameter_list|()
block|{     }
DECL|class|ReceivedContentMic
specifier|public
specifier|static
class|class
name|ReceivedContentMic
block|{
DECL|field|digestAlgorithmId
specifier|private
specifier|final
name|String
name|digestAlgorithmId
decl_stmt|;
DECL|field|encodedMessageDigest
specifier|private
specifier|final
name|String
name|encodedMessageDigest
decl_stmt|;
DECL|method|ReceivedContentMic (String digestAlgorithmId, byte[] messageDigest)
specifier|public
name|ReceivedContentMic
parameter_list|(
name|String
name|digestAlgorithmId
parameter_list|,
name|byte
index|[]
name|messageDigest
parameter_list|)
throws|throws
name|Exception
block|{
name|this
operator|.
name|digestAlgorithmId
operator|=
name|digestAlgorithmId
expr_stmt|;
name|messageDigest
operator|=
name|EntityUtils
operator|.
name|encode
argument_list|(
name|messageDigest
argument_list|,
literal|"base64"
argument_list|)
expr_stmt|;
name|this
operator|.
name|encodedMessageDigest
operator|=
operator|new
name|String
argument_list|(
name|messageDigest
argument_list|,
name|AS2Charset
operator|.
name|US_ASCII
argument_list|)
expr_stmt|;
block|}
comment|// Used when parsing received content MIC from received string
DECL|method|ReceivedContentMic (String digestAlgorithmId, String encodedMessageDigest)
specifier|protected
name|ReceivedContentMic
parameter_list|(
name|String
name|digestAlgorithmId
parameter_list|,
name|String
name|encodedMessageDigest
parameter_list|)
block|{
name|this
operator|.
name|digestAlgorithmId
operator|=
name|digestAlgorithmId
expr_stmt|;
name|this
operator|.
name|encodedMessageDigest
operator|=
name|encodedMessageDigest
expr_stmt|;
block|}
DECL|method|getDigestAlgorithmId ()
specifier|public
name|String
name|getDigestAlgorithmId
parameter_list|()
block|{
return|return
name|digestAlgorithmId
return|;
block|}
DECL|method|getEncodedMessageDigest ()
specifier|public
name|String
name|getEncodedMessageDigest
parameter_list|()
block|{
return|return
name|encodedMessageDigest
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|encodedMessageDigest
operator|+
literal|","
operator|+
name|digestAlgorithmId
return|;
block|}
block|}
DECL|method|createMic (byte[] content, String algorithmId)
specifier|public
specifier|static
name|byte
index|[]
name|createMic
parameter_list|(
name|byte
index|[]
name|content
parameter_list|,
name|String
name|algorithmId
parameter_list|)
block|{
try|try
block|{
name|MessageDigest
name|messageDigest
init|=
name|MessageDigest
operator|.
name|getInstance
argument_list|(
name|algorithmId
argument_list|,
literal|"BC"
argument_list|)
decl_stmt|;
return|return
name|messageDigest
operator|.
name|digest
argument_list|(
name|content
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NoSuchAlgorithmException
decl||
name|NoSuchProviderException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"failed to get message digets '{}'"
argument_list|,
name|algorithmId
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
DECL|method|createReceivedContentMic (HttpEntityEnclosingRequest request, PrivateKey decryptingPrivateKey)
specifier|public
specifier|static
name|ReceivedContentMic
name|createReceivedContentMic
parameter_list|(
name|HttpEntityEnclosingRequest
name|request
parameter_list|,
name|PrivateKey
name|decryptingPrivateKey
parameter_list|)
throws|throws
name|HttpException
block|{
name|String
name|dispositionNotificationOptionsString
init|=
name|HttpMessageUtils
operator|.
name|getHeaderValue
argument_list|(
name|request
argument_list|,
name|AS2Header
operator|.
name|DISPOSITION_NOTIFICATION_OPTIONS
argument_list|)
decl_stmt|;
if|if
condition|(
name|dispositionNotificationOptionsString
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"do not create MIC: no disposition notification options in request"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
name|DispositionNotificationOptions
name|dispositionNotificationOptions
init|=
name|DispositionNotificationOptionsParser
operator|.
name|parseDispositionNotificationOptions
argument_list|(
name|dispositionNotificationOptionsString
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|String
name|micJdkAlgorithmName
init|=
name|getMicJdkAlgorithmName
argument_list|(
name|dispositionNotificationOptions
operator|.
name|getSignedReceiptMicalg
argument_list|()
operator|.
name|getValues
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|micJdkAlgorithmName
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"do not create MIC: no matching MIC algorithms found"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
name|HttpEntity
name|entity
init|=
name|HttpMessageUtils
operator|.
name|extractEdiPayload
argument_list|(
name|request
argument_list|,
name|decryptingPrivateKey
argument_list|)
decl_stmt|;
name|byte
index|[]
name|content
init|=
name|EntityUtils
operator|.
name|getContent
argument_list|(
name|entity
argument_list|)
decl_stmt|;
name|String
name|micAS2AlgorithmName
init|=
name|AS2MicAlgorithm
operator|.
name|getAS2AlgorithmName
argument_list|(
name|micJdkAlgorithmName
argument_list|)
decl_stmt|;
name|byte
index|[]
name|mic
init|=
name|createMic
argument_list|(
name|content
argument_list|,
name|micJdkAlgorithmName
argument_list|)
decl_stmt|;
try|try
block|{
return|return
operator|new
name|ReceivedContentMic
argument_list|(
name|micAS2AlgorithmName
argument_list|,
name|mic
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|HttpException
argument_list|(
literal|"Failed to encode MIC"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|getMicJdkAlgorithmName (String[] micAs2AlgorithmNames)
specifier|public
specifier|static
name|String
name|getMicJdkAlgorithmName
parameter_list|(
name|String
index|[]
name|micAs2AlgorithmNames
parameter_list|)
block|{
if|if
condition|(
name|micAs2AlgorithmNames
operator|==
literal|null
condition|)
block|{
return|return
name|AS2MicAlgorithm
operator|.
name|SHA_1
operator|.
name|getJdkAlgorithmName
argument_list|()
return|;
block|}
for|for
control|(
name|String
name|micAs2AlgorithmName
range|:
name|micAs2AlgorithmNames
control|)
block|{
name|String
name|micJdkAlgorithmName
init|=
name|AS2MicAlgorithm
operator|.
name|getJdkAlgorithmName
argument_list|(
name|micAs2AlgorithmName
argument_list|)
decl_stmt|;
if|if
condition|(
name|micJdkAlgorithmName
operator|!=
literal|null
condition|)
block|{
return|return
name|micJdkAlgorithmName
return|;
block|}
block|}
return|return
name|AS2MicAlgorithm
operator|.
name|SHA_1
operator|.
name|getJdkAlgorithmName
argument_list|()
return|;
block|}
block|}
end_class

end_unit

