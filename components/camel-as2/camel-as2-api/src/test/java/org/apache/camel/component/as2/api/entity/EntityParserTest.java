begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|ByteArrayInputStream
import|;
end_import

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
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|math
operator|.
name|BigInteger
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|GeneralSecurityException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|KeyPair
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|KeyPairGenerator
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
name|security
operator|.
name|PublicKey
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|SecureRandom
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|Security
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|cert
operator|.
name|X509Certificate
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|io
operator|.
name|AS2SessionInputBuffer
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
name|EntityUtils
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
name|HttpStatus
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
name|HttpVersion
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
name|BasicHttpEntity
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
name|impl
operator|.
name|EnglishReasonPhraseCatalog
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
name|impl
operator|.
name|io
operator|.
name|HttpTransportMetricsImpl
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
name|BasicHttpResponse
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|asn1
operator|.
name|x500
operator|.
name|X500Name
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|asn1
operator|.
name|x509
operator|.
name|AuthorityKeyIdentifier
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|asn1
operator|.
name|x509
operator|.
name|Extension
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|asn1
operator|.
name|x509
operator|.
name|SubjectKeyIdentifier
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|asn1
operator|.
name|x509
operator|.
name|SubjectPublicKeyInfo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|cert
operator|.
name|X509v3CertificateBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|cert
operator|.
name|bc
operator|.
name|BcX509ExtensionUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|cert
operator|.
name|jcajce
operator|.
name|JcaX509CertificateConverter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|cert
operator|.
name|jcajce
operator|.
name|JcaX509v3CertificateBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|cms
operator|.
name|CMSAlgorithm
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|cms
operator|.
name|CMSEnvelopedDataGenerator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|cms
operator|.
name|jcajce
operator|.
name|JceCMSContentEncryptorBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|cms
operator|.
name|jcajce
operator|.
name|JceKeyTransRecipientInfoGenerator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|jce
operator|.
name|provider
operator|.
name|BouncyCastleProvider
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|operator
operator|.
name|OperatorCreationException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|operator
operator|.
name|OutputEncryptor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|operator
operator|.
name|jcajce
operator|.
name|JcaContentSignerBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertArrayEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

begin_class
DECL|class|EntityParserTest
specifier|public
class|class
name|EntityParserTest
block|{
DECL|field|REPORT_CONTENT_TYPE_VALUE
specifier|public
specifier|static
specifier|final
name|String
name|REPORT_CONTENT_TYPE_VALUE
init|=
literal|"multipart/report; report-type=disposition-notification; boundary=\"----=_Part_56_1672293592.1028122454656\""
decl_stmt|;
DECL|field|REPORT_TYPE_HEADER_VALUE
specifier|public
specifier|static
specifier|final
name|String
name|REPORT_TYPE_HEADER_VALUE
init|=
literal|"disposition-notification; boundary=\"----=_Part_56_1672293592.1028122454656\"\r\n"
decl_stmt|;
DECL|field|DISPOSITION_NOTIFICATION_REPORT_CONTENT
specifier|public
specifier|static
specifier|final
name|String
name|DISPOSITION_NOTIFICATION_REPORT_CONTENT
init|=
literal|"\r\n"
operator|+
literal|"------=_Part_56_1672293592.1028122454656\r\n"
operator|+
literal|"Content-Type: text/plain\r\n"
operator|+
literal|"Content-Transfer-Encoding: 7bit\r\n"
operator|+
literal|"\r\n"
operator|+
literal|"MDN for -\r\n"
operator|+
literal|" Message ID:<200207310834482A70BF63@\\\"~~foo~~\\\">\r\n"
operator|+
literal|"  From: \"\\\"  as2Name  \\\"\"\r\n"
operator|+
literal|"  To: \"0123456780000\""
operator|+
literal|"  Received on: 2002-07-31 at 09:34:14 (EDT)\r\n"
operator|+
literal|" Status: processed\r\n"
operator|+
literal|" Comment: This is not a guarantee that the message has\r\n"
operator|+
literal|"  been completely processed or&understood by the receiving\r\n"
operator|+
literal|"  translator\r\n"
operator|+
literal|"\r\n"
operator|+
literal|"------=_Part_56_1672293592.1028122454656\r\n"
operator|+
literal|"Content-Type: message/disposition-notification\r\n"
operator|+
literal|"Content-Transfer-Encoding: 7bit\r\n"
operator|+
literal|"\r\n"
operator|+
literal|"Reporting-UA: AS2 Server\r\n"
operator|+
literal|"MDN-Gateway: dns; example.com\r\n"
operator|+
literal|"Original-Recipient: rfc822; 0123456780000\r\n"
operator|+
literal|"Final-Recipient: rfc822; 0123456780000\r\n"
operator|+
literal|"Original-Message-ID:<200207310834482A70BF63@\\\"~~foo~~\\\">\r\n"
operator|+
literal|"Disposition: automatic-action/MDN-sent-automatically;\r\n"
operator|+
literal|"  processed/warning: you're awesome\r\n"
operator|+
literal|"Failure: oops-a-failure\r\n"
operator|+
literal|"Error: oops-an-error\r\n"
operator|+
literal|"Warning: oops-a-warning\r\n"
operator|+
literal|"Received-content-MIC: 7v7F++fQaNB1sVLFtMRp+dF+eG4=, sha1\r\n"
operator|+
literal|"\r\n"
operator|+
literal|"------=_Part_56_1672293592.1028122454656--\r\n"
decl_stmt|;
DECL|field|DISPOSITION_NOTIFICATION_REPORT_CONTENT_BOUNDARY
specifier|public
specifier|static
specifier|final
name|String
name|DISPOSITION_NOTIFICATION_REPORT_CONTENT_BOUNDARY
init|=
literal|"----=_Part_56_1672293592.1028122454656"
decl_stmt|;
DECL|field|DISPOSITION_NOTIFICATION_REPORT_CONTENT_CHARSET_NAME
specifier|public
specifier|static
specifier|final
name|String
name|DISPOSITION_NOTIFICATION_REPORT_CONTENT_CHARSET_NAME
init|=
literal|"US-ASCII"
decl_stmt|;
DECL|field|DISPOSITION_NOTIFICATION_REPORT_CONTENT_TRANSFER_ENCODING
specifier|public
specifier|static
specifier|final
name|String
name|DISPOSITION_NOTIFICATION_REPORT_CONTENT_TRANSFER_ENCODING
init|=
literal|"7bit"
decl_stmt|;
DECL|field|TEXT_PLAIN_CONTENT
specifier|public
specifier|static
specifier|final
name|String
name|TEXT_PLAIN_CONTENT
init|=
literal|"MDN for -\r\n"
operator|+
literal|" Message ID:<200207310834482A70BF63@\\\"~~foo~~\\\">\r\n"
operator|+
literal|"  From: \"\\\"  as2Name  \\\"\"\r\n"
operator|+
literal|"  To: \"0123456780000\""
operator|+
literal|"  Received on: 2002-07-31 at 09:34:14 (EDT)\r\n"
operator|+
literal|" Status: processed\r\n"
operator|+
literal|" Comment: This is not a guarantee that the message has\r\n"
operator|+
literal|"  been completely processed or&understood by the receiving\r\n"
operator|+
literal|"  translator\r\n"
operator|+
literal|"\r\n"
operator|+
literal|"------=_Part_56_1672293592.1028122454656--\r\n"
decl_stmt|;
DECL|field|TEXT_PLAIN_CONTENT_BOUNDARY
specifier|public
specifier|static
specifier|final
name|String
name|TEXT_PLAIN_CONTENT_BOUNDARY
init|=
literal|"----=_Part_56_1672293592.1028122454656"
decl_stmt|;
DECL|field|TEXT_PLAIN_CONTENT_CHARSET_NAME
specifier|public
specifier|static
specifier|final
name|String
name|TEXT_PLAIN_CONTENT_CHARSET_NAME
init|=
literal|"US-ASCII"
decl_stmt|;
DECL|field|TEXT_PLAIN_CONTENT_TRANSFER_ENCODING
specifier|public
specifier|static
specifier|final
name|String
name|TEXT_PLAIN_CONTENT_TRANSFER_ENCODING
init|=
literal|"7bit"
decl_stmt|;
DECL|field|EXPECTED_TEXT_PLAIN_CONTENT
specifier|public
specifier|static
specifier|final
name|String
name|EXPECTED_TEXT_PLAIN_CONTENT
init|=
literal|"MDN for -\r\n"
operator|+
literal|" Message ID:<200207310834482A70BF63@\\\"~~foo~~\\\">\r\n"
operator|+
literal|"  From: \"\\\"  as2Name  \\\"\"\r\n"
operator|+
literal|"  To: \"0123456780000\""
operator|+
literal|"  Received on: 2002-07-31 at 09:34:14 (EDT)\r\n"
operator|+
literal|" Status: processed\r\n"
operator|+
literal|" Comment: This is not a guarantee that the message has\r\n"
operator|+
literal|"  been completely processed or&understood by the receiving\r\n"
operator|+
literal|"  translator\r\n"
decl_stmt|;
DECL|field|DISPOSITION_NOTIFICATION_CONTENT
specifier|public
specifier|static
specifier|final
name|String
name|DISPOSITION_NOTIFICATION_CONTENT
init|=
literal|"Reporting-UA: AS2 Server\r\n"
operator|+
literal|"MDN-Gateway: dns; example.com\r\n"
operator|+
literal|"Original-Recipient: rfc822; 0123456780000\r\n"
operator|+
literal|"Final-Recipient: rfc822; 0123456780000\r\n"
operator|+
literal|"Original-Message-ID:<200207310834482A70BF63@\\\"~~foo~~\\\">\r\n"
operator|+
literal|"Disposition: automatic-action/MDN-sent-automatically;\r\n"
operator|+
literal|"  processed/warning: you're awesome\r\n"
operator|+
literal|"Failure: oops-a-failure\r\n"
operator|+
literal|"Error: oops-an-error\r\n"
operator|+
literal|"Warning: oops-a-warning\r\n"
operator|+
literal|"Received-content-MIC: 7v7F++fQaNB1sVLFtMRp+dF+eG4=, sha1\r\n"
operator|+
literal|"\r\n"
operator|+
literal|"------=_Part_56_1672293592.1028122454656--\r\n"
decl_stmt|;
DECL|field|DISPOSITION_NOTIFICATION_CONTENT_BOUNDARY
specifier|public
specifier|static
specifier|final
name|String
name|DISPOSITION_NOTIFICATION_CONTENT_BOUNDARY
init|=
literal|"----=_Part_56_1672293592.1028122454656"
decl_stmt|;
DECL|field|DISPOSITION_NOTIFICATION_CONTENT_CHARSET_NAME
specifier|public
specifier|static
specifier|final
name|String
name|DISPOSITION_NOTIFICATION_CONTENT_CHARSET_NAME
init|=
literal|"US-ASCII"
decl_stmt|;
DECL|field|DISPOSITION_NOTIFICATION_CONTENT_TRANSFER_ENCODING
specifier|public
specifier|static
specifier|final
name|String
name|DISPOSITION_NOTIFICATION_CONTENT_TRANSFER_ENCODING
init|=
literal|"7bit"
decl_stmt|;
DECL|field|EXPECTED_REPORTING_UA
specifier|public
specifier|static
specifier|final
name|String
name|EXPECTED_REPORTING_UA
init|=
literal|"AS2 Server"
decl_stmt|;
DECL|field|EXPECTED_MTN_NAME
specifier|public
specifier|static
specifier|final
name|String
name|EXPECTED_MTN_NAME
init|=
literal|"example.com"
decl_stmt|;
DECL|field|EXPECTED_ORIGINAL_RECIPIENT
specifier|public
specifier|static
specifier|final
name|String
name|EXPECTED_ORIGINAL_RECIPIENT
init|=
literal|"rfc822; 0123456780000"
decl_stmt|;
DECL|field|EXPECTED_FINAL_RECIPIENT
specifier|public
specifier|static
specifier|final
name|String
name|EXPECTED_FINAL_RECIPIENT
init|=
literal|"0123456780000"
decl_stmt|;
DECL|field|EXPECTED_ORIGINAL_MESSAGE_ID
specifier|public
specifier|static
specifier|final
name|String
name|EXPECTED_ORIGINAL_MESSAGE_ID
init|=
literal|"<200207310834482A70BF63@\\\"~~foo~~\\\">"
decl_stmt|;
DECL|field|EXPECTED_DISPOSITION_MODE
specifier|public
specifier|static
specifier|final
name|DispositionMode
name|EXPECTED_DISPOSITION_MODE
init|=
name|DispositionMode
operator|.
name|AUTOMATIC_ACTION_MDN_SENT_AUTOMATICALLY
decl_stmt|;
DECL|field|EXPECTED_DISPOSITION_MODIFIER
specifier|public
specifier|static
specifier|final
name|String
name|EXPECTED_DISPOSITION_MODIFIER
init|=
literal|"warning: you're awesome"
decl_stmt|;
DECL|field|EXPECTED_DISPOSITION_TYPE
specifier|public
specifier|static
specifier|final
name|AS2DispositionType
name|EXPECTED_DISPOSITION_TYPE
init|=
name|AS2DispositionType
operator|.
name|PROCESSED
decl_stmt|;
DECL|field|EXPECTED_FAILURE
specifier|public
specifier|static
specifier|final
name|String
index|[]
name|EXPECTED_FAILURE
init|=
block|{
literal|"oops-a-failure"
block|}
decl_stmt|;
DECL|field|EXPECTED_ERROR
specifier|public
specifier|static
specifier|final
name|String
index|[]
name|EXPECTED_ERROR
init|=
block|{
literal|"oops-an-error"
block|}
decl_stmt|;
DECL|field|EXPECTED_WARNING
specifier|public
specifier|static
specifier|final
name|String
index|[]
name|EXPECTED_WARNING
init|=
block|{
literal|"oops-a-warning"
block|}
decl_stmt|;
DECL|field|EXPECTED_ENCODED_MESSAGE_DIGEST
specifier|public
specifier|static
specifier|final
name|String
name|EXPECTED_ENCODED_MESSAGE_DIGEST
init|=
literal|"7v7F++fQaNB1sVLFtMRp+dF+eG4="
decl_stmt|;
DECL|field|EXPECTED_DIGEST_ALGORITHM_ID
specifier|public
specifier|static
specifier|final
name|String
name|EXPECTED_DIGEST_ALGORITHM_ID
init|=
literal|"sha1"
decl_stmt|;
DECL|field|DEFAULT_BUFFER_SIZE
specifier|private
specifier|static
specifier|final
name|int
name|DEFAULT_BUFFER_SIZE
init|=
literal|8
operator|*
literal|1024
decl_stmt|;
comment|//
comment|// certificate serial number seed.
comment|//
DECL|field|serialNo
name|int
name|serialNo
init|=
literal|1
decl_stmt|;
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{     }
annotation|@
name|After
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{     }
annotation|@
name|Test
DECL|method|parseMessageDispositionNotificationReportMessageTest ()
specifier|public
name|void
name|parseMessageDispositionNotificationReportMessageTest
parameter_list|()
throws|throws
name|Exception
block|{
name|HttpResponse
name|response
init|=
operator|new
name|BasicHttpResponse
argument_list|(
name|HttpVersion
operator|.
name|HTTP_1_1
argument_list|,
name|HttpStatus
operator|.
name|SC_OK
argument_list|,
name|EnglishReasonPhraseCatalog
operator|.
name|INSTANCE
operator|.
name|getReason
argument_list|(
name|HttpStatus
operator|.
name|SC_OK
argument_list|,
literal|null
argument_list|)
argument_list|)
decl_stmt|;
name|HttpMessageUtils
operator|.
name|setHeaderValue
argument_list|(
name|response
argument_list|,
name|AS2Header
operator|.
name|CONTENT_TRANSFER_ENCODING
argument_list|,
name|DISPOSITION_NOTIFICATION_CONTENT_TRANSFER_ENCODING
argument_list|)
expr_stmt|;
name|BasicHttpEntity
name|entity
init|=
operator|new
name|BasicHttpEntity
argument_list|()
decl_stmt|;
name|entity
operator|.
name|setContentType
argument_list|(
name|REPORT_CONTENT_TYPE_VALUE
argument_list|)
expr_stmt|;
name|InputStream
name|is
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|DISPOSITION_NOTIFICATION_REPORT_CONTENT
operator|.
name|getBytes
argument_list|(
name|DISPOSITION_NOTIFICATION_REPORT_CONTENT_CHARSET_NAME
argument_list|)
argument_list|)
decl_stmt|;
name|entity
operator|.
name|setContent
argument_list|(
name|is
argument_list|)
expr_stmt|;
name|EntityUtils
operator|.
name|setMessageEntity
argument_list|(
name|response
argument_list|,
name|entity
argument_list|)
expr_stmt|;
name|EntityParser
operator|.
name|parseAS2MessageEntity
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|HttpEntity
name|parsedEntity
init|=
name|EntityUtils
operator|.
name|getMessageEntity
argument_list|(
name|response
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Unexpected Null message disposition notification report entity"
argument_list|,
name|parsedEntity
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Unexpected type for message disposition notification report entity"
argument_list|,
name|parsedEntity
operator|instanceof
name|DispositionNotificationMultipartReportEntity
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|parseMessageDispositionNotificationReportBodyTest ()
specifier|public
name|void
name|parseMessageDispositionNotificationReportBodyTest
parameter_list|()
throws|throws
name|Exception
block|{
name|InputStream
name|is
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|DISPOSITION_NOTIFICATION_REPORT_CONTENT
operator|.
name|getBytes
argument_list|(
name|DISPOSITION_NOTIFICATION_REPORT_CONTENT_CHARSET_NAME
argument_list|)
argument_list|)
decl_stmt|;
name|AS2SessionInputBuffer
name|inbuffer
init|=
operator|new
name|AS2SessionInputBuffer
argument_list|(
operator|new
name|HttpTransportMetricsImpl
argument_list|()
argument_list|,
name|DEFAULT_BUFFER_SIZE
argument_list|,
name|DEFAULT_BUFFER_SIZE
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|inbuffer
operator|.
name|bind
argument_list|(
name|is
argument_list|)
expr_stmt|;
name|DispositionNotificationMultipartReportEntity
name|dispositionNotificationMultipartReportEntity
init|=
name|EntityParser
operator|.
name|parseMultipartReportEntityBody
argument_list|(
name|inbuffer
argument_list|,
name|DISPOSITION_NOTIFICATION_REPORT_CONTENT_BOUNDARY
argument_list|,
name|DISPOSITION_NOTIFICATION_REPORT_CONTENT_CHARSET_NAME
argument_list|,
name|DISPOSITION_NOTIFICATION_REPORT_CONTENT_TRANSFER_ENCODING
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Unexpected Null disposition notification multipart entity"
argument_list|,
name|dispositionNotificationMultipartReportEntity
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected number of body parts"
argument_list|,
literal|2
argument_list|,
name|dispositionNotificationMultipartReportEntity
operator|.
name|getPartCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Unexpected type for first body part"
argument_list|,
name|dispositionNotificationMultipartReportEntity
operator|.
name|getPart
argument_list|(
literal|0
argument_list|)
operator|instanceof
name|TextPlainEntity
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Unexpected type for second body part"
argument_list|,
name|dispositionNotificationMultipartReportEntity
operator|.
name|getPart
argument_list|(
literal|1
argument_list|)
operator|instanceof
name|AS2MessageDispositionNotificationEntity
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|parseTextPlainBodyTest ()
specifier|public
name|void
name|parseTextPlainBodyTest
parameter_list|()
throws|throws
name|Exception
block|{
name|InputStream
name|is
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|TEXT_PLAIN_CONTENT
operator|.
name|getBytes
argument_list|(
name|TEXT_PLAIN_CONTENT_CHARSET_NAME
argument_list|)
argument_list|)
decl_stmt|;
name|AS2SessionInputBuffer
name|inbuffer
init|=
operator|new
name|AS2SessionInputBuffer
argument_list|(
operator|new
name|HttpTransportMetricsImpl
argument_list|()
argument_list|,
name|DEFAULT_BUFFER_SIZE
argument_list|,
name|DEFAULT_BUFFER_SIZE
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|inbuffer
operator|.
name|bind
argument_list|(
name|is
argument_list|)
expr_stmt|;
name|TextPlainEntity
name|textPlainEntity
init|=
name|EntityParser
operator|.
name|parseTextPlainEntityBody
argument_list|(
name|inbuffer
argument_list|,
name|TEXT_PLAIN_CONTENT_BOUNDARY
argument_list|,
name|TEXT_PLAIN_CONTENT_CHARSET_NAME
argument_list|,
name|TEXT_PLAIN_CONTENT_TRANSFER_ENCODING
argument_list|)
decl_stmt|;
name|String
name|text
init|=
name|textPlainEntity
operator|.
name|getText
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected text"
argument_list|,
name|EXPECTED_TEXT_PLAIN_CONTENT
argument_list|,
name|text
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|parseMessageDispositionNotificationBodyTest ()
specifier|public
name|void
name|parseMessageDispositionNotificationBodyTest
parameter_list|()
throws|throws
name|Exception
block|{
name|InputStream
name|is
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|DISPOSITION_NOTIFICATION_CONTENT
operator|.
name|getBytes
argument_list|(
name|DISPOSITION_NOTIFICATION_CONTENT_CHARSET_NAME
argument_list|)
argument_list|)
decl_stmt|;
name|AS2SessionInputBuffer
name|inbuffer
init|=
operator|new
name|AS2SessionInputBuffer
argument_list|(
operator|new
name|HttpTransportMetricsImpl
argument_list|()
argument_list|,
name|DEFAULT_BUFFER_SIZE
argument_list|,
name|DEFAULT_BUFFER_SIZE
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|inbuffer
operator|.
name|bind
argument_list|(
name|is
argument_list|)
expr_stmt|;
name|AS2MessageDispositionNotificationEntity
name|messageDispositionNotificationEntity
init|=
name|EntityParser
operator|.
name|parseMessageDispositionNotificationEntityBody
argument_list|(
name|inbuffer
argument_list|,
name|DISPOSITION_NOTIFICATION_CONTENT_BOUNDARY
argument_list|,
name|DISPOSITION_NOTIFICATION_CONTENT_CHARSET_NAME
argument_list|,
name|DISPOSITION_NOTIFICATION_CONTENT_TRANSFER_ENCODING
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected Reporting UA value"
argument_list|,
name|EXPECTED_REPORTING_UA
argument_list|,
name|messageDispositionNotificationEntity
operator|.
name|getReportingUA
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected MTN Name"
argument_list|,
name|EXPECTED_MTN_NAME
argument_list|,
name|messageDispositionNotificationEntity
operator|.
name|getMtnName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected Original Recipient"
argument_list|,
name|EXPECTED_ORIGINAL_RECIPIENT
argument_list|,
name|messageDispositionNotificationEntity
operator|.
name|getExtensionFields
argument_list|()
operator|.
name|get
argument_list|(
literal|"Original-Recipient"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected Final Reciptient"
argument_list|,
name|EXPECTED_FINAL_RECIPIENT
argument_list|,
name|messageDispositionNotificationEntity
operator|.
name|getFinalRecipient
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected Original Message ID"
argument_list|,
name|EXPECTED_ORIGINAL_MESSAGE_ID
argument_list|,
name|messageDispositionNotificationEntity
operator|.
name|getOriginalMessageId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected Disposition Mode"
argument_list|,
name|EXPECTED_DISPOSITION_MODE
argument_list|,
name|messageDispositionNotificationEntity
operator|.
name|getDispositionMode
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"Unexpected Null Disposition Modifier"
argument_list|,
name|messageDispositionNotificationEntity
operator|.
name|getDispositionModifier
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected Disposition Modifier"
argument_list|,
name|EXPECTED_DISPOSITION_MODIFIER
argument_list|,
name|messageDispositionNotificationEntity
operator|.
name|getDispositionModifier
argument_list|()
operator|.
name|getModifier
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected Disposition Type"
argument_list|,
name|EXPECTED_DISPOSITION_TYPE
argument_list|,
name|messageDispositionNotificationEntity
operator|.
name|getDispositionType
argument_list|()
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
literal|"Unexpected Failure Array value"
argument_list|,
name|EXPECTED_FAILURE
argument_list|,
name|messageDispositionNotificationEntity
operator|.
name|getFailureFields
argument_list|()
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
literal|"Unexpected Error Array value"
argument_list|,
name|EXPECTED_ERROR
argument_list|,
name|messageDispositionNotificationEntity
operator|.
name|getErrorFields
argument_list|()
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
literal|"Unexpected Warning Array value"
argument_list|,
name|EXPECTED_WARNING
argument_list|,
name|messageDispositionNotificationEntity
operator|.
name|getWarningFields
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"Unexpected Null Received Content MIC"
argument_list|,
name|messageDispositionNotificationEntity
operator|.
name|getReceivedContentMic
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected Encoded Message Digest"
argument_list|,
name|EXPECTED_ENCODED_MESSAGE_DIGEST
argument_list|,
name|messageDispositionNotificationEntity
operator|.
name|getReceivedContentMic
argument_list|()
operator|.
name|getEncodedMessageDigest
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected Digest Algorithm ID"
argument_list|,
name|EXPECTED_DIGEST_ALGORITHM_ID
argument_list|,
name|messageDispositionNotificationEntity
operator|.
name|getReceivedContentMic
argument_list|()
operator|.
name|getDigestAlgorithmId
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|parseEnvelopedBodyTest ()
specifier|public
name|void
name|parseEnvelopedBodyTest
parameter_list|()
throws|throws
name|Exception
block|{
name|Security
operator|.
name|addProvider
argument_list|(
operator|new
name|BouncyCastleProvider
argument_list|()
argument_list|)
expr_stmt|;
comment|//
comment|// set up our certificates
comment|//
name|KeyPairGenerator
name|kpg
init|=
name|KeyPairGenerator
operator|.
name|getInstance
argument_list|(
literal|"RSA"
argument_list|,
literal|"BC"
argument_list|)
decl_stmt|;
name|kpg
operator|.
name|initialize
argument_list|(
literal|1024
argument_list|,
operator|new
name|SecureRandom
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|issueDN
init|=
literal|"O=Punkhorn Software, C=US"
decl_stmt|;
name|KeyPair
name|issueKP
init|=
name|kpg
operator|.
name|generateKeyPair
argument_list|()
decl_stmt|;
name|X509Certificate
name|issuerCertificate
init|=
name|makeCertificate
argument_list|(
name|issueKP
argument_list|,
name|issueDN
argument_list|,
name|issueKP
argument_list|,
name|issueDN
argument_list|)
decl_stmt|;
comment|//
comment|// certificate we encrypt against
comment|//
name|String
name|encryptDN
init|=
literal|"CN=William J. Collins, E=punkhornsw@gmail.com, O=Punkhorn Software, C=US"
decl_stmt|;
name|KeyPair
name|encryptKP
init|=
name|kpg
operator|.
name|generateKeyPair
argument_list|()
decl_stmt|;
name|X509Certificate
name|encryptionCertificate
init|=
name|makeCertificate
argument_list|(
name|encryptKP
argument_list|,
name|encryptDN
argument_list|,
name|issueKP
argument_list|,
name|issueDN
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|X509Certificate
argument_list|>
name|certList
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|certList
operator|.
name|add
argument_list|(
name|encryptionCertificate
argument_list|)
expr_stmt|;
name|certList
operator|.
name|add
argument_list|(
name|issuerCertificate
argument_list|)
expr_stmt|;
comment|//
comment|// Create generator
comment|//
name|CMSEnvelopedDataGenerator
name|cmsEnvelopeDataGenerator
init|=
operator|new
name|CMSEnvelopedDataGenerator
argument_list|()
decl_stmt|;
name|JceKeyTransRecipientInfoGenerator
name|recipientInfoGenerator
init|=
operator|new
name|JceKeyTransRecipientInfoGenerator
argument_list|(
name|encryptionCertificate
argument_list|)
decl_stmt|;
name|cmsEnvelopeDataGenerator
operator|.
name|addRecipientInfoGenerator
argument_list|(
name|recipientInfoGenerator
argument_list|)
expr_stmt|;
comment|//
comment|// Create encryptor
comment|//
name|OutputEncryptor
name|contentEncryptor
init|=
operator|new
name|JceCMSContentEncryptorBuilder
argument_list|(
name|CMSAlgorithm
operator|.
name|AES128_CCM
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
comment|//
comment|// Build Enveloped Entity
comment|//
name|TextPlainEntity
name|textEntity
init|=
operator|new
name|TextPlainEntity
argument_list|(
literal|"This is a super secret messatge!"
argument_list|,
literal|"US-ASCII"
argument_list|,
literal|"7bit"
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|ApplicationPkcs7MimeEnvelopedDataEntity
name|applicationPkcs7MimeEntity
init|=
operator|new
name|ApplicationPkcs7MimeEnvelopedDataEntity
argument_list|(
name|textEntity
argument_list|,
name|cmsEnvelopeDataGenerator
argument_list|,
name|contentEncryptor
argument_list|,
literal|"binary"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|MimeEntity
name|decryptedMimeEntity
init|=
name|applicationPkcs7MimeEntity
operator|.
name|getEncryptedEntity
argument_list|(
name|encryptKP
operator|.
name|getPrivate
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Decrypted entity has unexpected content type"
argument_list|,
literal|"text/plain; charset=US-ASCII"
argument_list|,
name|decryptedMimeEntity
operator|.
name|getContentTypeValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Decrypted entity has unexpected content"
argument_list|,
literal|"This is a super secret messatge!"
argument_list|,
operator|(
operator|(
name|TextPlainEntity
operator|)
name|decryptedMimeEntity
operator|)
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * create a basic X509 certificate from the given keys      */
DECL|method|makeCertificate (KeyPair subKP, String subDN, KeyPair issKP, String issDN)
specifier|private
name|X509Certificate
name|makeCertificate
parameter_list|(
name|KeyPair
name|subKP
parameter_list|,
name|String
name|subDN
parameter_list|,
name|KeyPair
name|issKP
parameter_list|,
name|String
name|issDN
parameter_list|)
throws|throws
name|GeneralSecurityException
throws|,
name|IOException
throws|,
name|OperatorCreationException
block|{
name|PublicKey
name|subPub
init|=
name|subKP
operator|.
name|getPublic
argument_list|()
decl_stmt|;
name|PrivateKey
name|issPriv
init|=
name|issKP
operator|.
name|getPrivate
argument_list|()
decl_stmt|;
name|PublicKey
name|issPub
init|=
name|issKP
operator|.
name|getPublic
argument_list|()
decl_stmt|;
name|X509v3CertificateBuilder
name|v3CertGen
init|=
operator|new
name|JcaX509v3CertificateBuilder
argument_list|(
operator|new
name|X500Name
argument_list|(
name|issDN
argument_list|)
argument_list|,
name|BigInteger
operator|.
name|valueOf
argument_list|(
name|serialNo
operator|++
argument_list|)
argument_list|,
operator|new
name|Date
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
argument_list|,
operator|new
name|Date
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|+
operator|(
literal|1000L
operator|*
literal|60
operator|*
literal|60
operator|*
literal|24
operator|*
literal|100
operator|)
argument_list|)
argument_list|,
operator|new
name|X500Name
argument_list|(
name|subDN
argument_list|)
argument_list|,
name|subPub
argument_list|)
decl_stmt|;
name|v3CertGen
operator|.
name|addExtension
argument_list|(
name|Extension
operator|.
name|subjectKeyIdentifier
argument_list|,
literal|false
argument_list|,
name|createSubjectKeyId
argument_list|(
name|subPub
argument_list|)
argument_list|)
expr_stmt|;
name|v3CertGen
operator|.
name|addExtension
argument_list|(
name|Extension
operator|.
name|authorityKeyIdentifier
argument_list|,
literal|false
argument_list|,
name|createAuthorityKeyId
argument_list|(
name|issPub
argument_list|)
argument_list|)
expr_stmt|;
return|return
operator|new
name|JcaX509CertificateConverter
argument_list|()
operator|.
name|setProvider
argument_list|(
literal|"BC"
argument_list|)
operator|.
name|getCertificate
argument_list|(
name|v3CertGen
operator|.
name|build
argument_list|(
operator|new
name|JcaContentSignerBuilder
argument_list|(
literal|"MD5withRSA"
argument_list|)
operator|.
name|setProvider
argument_list|(
literal|"BC"
argument_list|)
operator|.
name|build
argument_list|(
name|issPriv
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
DECL|method|createAuthorityKeyId (PublicKey pub)
specifier|private
name|AuthorityKeyIdentifier
name|createAuthorityKeyId
parameter_list|(
name|PublicKey
name|pub
parameter_list|)
throws|throws
name|IOException
block|{
name|SubjectPublicKeyInfo
name|info
init|=
name|SubjectPublicKeyInfo
operator|.
name|getInstance
argument_list|(
name|pub
operator|.
name|getEncoded
argument_list|()
argument_list|)
decl_stmt|;
name|BcX509ExtensionUtils
name|utils
init|=
operator|new
name|BcX509ExtensionUtils
argument_list|()
decl_stmt|;
return|return
name|utils
operator|.
name|createAuthorityKeyIdentifier
argument_list|(
name|info
argument_list|)
return|;
block|}
DECL|method|createSubjectKeyId (PublicKey pub)
specifier|static
name|SubjectKeyIdentifier
name|createSubjectKeyId
parameter_list|(
name|PublicKey
name|pub
parameter_list|)
throws|throws
name|IOException
block|{
name|SubjectPublicKeyInfo
name|info
init|=
name|SubjectPublicKeyInfo
operator|.
name|getInstance
argument_list|(
name|pub
operator|.
name|getEncoded
argument_list|()
argument_list|)
decl_stmt|;
return|return
operator|new
name|BcX509ExtensionUtils
argument_list|()
operator|.
name|createSubjectKeyIdentifier
argument_list|(
name|info
argument_list|)
return|;
block|}
block|}
end_class

end_unit

