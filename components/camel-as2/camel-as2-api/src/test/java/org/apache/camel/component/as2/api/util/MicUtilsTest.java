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
name|io
operator|.
name|InputStream
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
name|AS2TransferEncoding
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
name|ApplicationEDIFACTEntity
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
name|message
operator|.
name|BasicHttpEntityEnclosingRequest
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

begin_class
DECL|class|MicUtilsTest
specifier|public
class|class
name|MicUtilsTest
block|{
DECL|field|LOG
specifier|public
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|MicUtilsTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|DISPOSITION_NOTIFICATION_OPTIONS_VALUE
specifier|private
specifier|static
specifier|final
name|String
name|DISPOSITION_NOTIFICATION_OPTIONS_VALUE
init|=
literal|" signed-receipt-protocol   =   optional  , pkcs7-signature  ;    signed-receipt-micalg   =    required  ,  sha1  "
decl_stmt|;
DECL|field|CONTENT_TYPE_VALUE
specifier|private
specifier|static
specifier|final
name|String
name|CONTENT_TYPE_VALUE
init|=
name|AS2MimeType
operator|.
name|APPLICATION_EDIFACT
decl_stmt|;
DECL|field|EDI_MESSAGE
specifier|private
specifier|static
specifier|final
name|String
name|EDI_MESSAGE
init|=
literal|"UNB+UNOA:1+005435656:1+006415160:1+060515:1434+00000000000778'\n"
operator|+
literal|"UNH+00000000000117+INVOIC:D:97B:UN'\n"
operator|+
literal|"BGM+380+342459+9'\n"
operator|+
literal|"DTM+3:20060515:102'\n"
operator|+
literal|"RFF+ON:521052'\n"
operator|+
literal|"NAD+BY+792820524::16++CUMMINS MID-RANGE ENGINE PLANT'\n"
operator|+
literal|"NAD+SE+005435656::16++GENERAL WIDGET COMPANY'\n"
operator|+
literal|"CUX+1:USD'\n"
operator|+
literal|"LIN+1++157870:IN'\n"
operator|+
literal|"IMD+F++:::WIDGET'\n"
operator|+
literal|"QTY+47:1020:EA'\n"
operator|+
literal|"ALI+US'\n"
operator|+
literal|"MOA+203:1202.58'\n"
operator|+
literal|"PRI+INV:1.179'\n"
operator|+
literal|"LIN+2++157871:IN'\n"
operator|+
literal|"IMD+F++:::DIFFERENT WIDGET'\n"
operator|+
literal|"QTY+47:20:EA'\n"
operator|+
literal|"ALI+JP'\n"
operator|+
literal|"MOA+203:410'\n"
operator|+
literal|"PRI+INV:20.5'\n"
operator|+
literal|"UNS+S'\n"
operator|+
literal|"MOA+39:2137.58'\n"
operator|+
literal|"ALC+C+ABG'\n"
operator|+
literal|"MOA+8:525'\n"
operator|+
literal|"UNT+23+00000000000117'\n"
operator|+
literal|"UNZ+1+00000000000778'"
decl_stmt|;
DECL|field|EXPECTED_MESSAGE_DIGEST_ALGORITHM
specifier|private
specifier|static
specifier|final
name|String
name|EXPECTED_MESSAGE_DIGEST_ALGORITHM
init|=
literal|"sha1"
decl_stmt|;
DECL|field|EXPECTED_ENCODED_MESSAGE_DIGEST
specifier|private
specifier|static
specifier|final
name|String
name|EXPECTED_ENCODED_MESSAGE_DIGEST
init|=
literal|"XUt+ug5GEDD0X9+Nv8DGYZZThOQ="
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
block|}
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
DECL|method|createReceivedContentMicTest ()
specifier|public
name|void
name|createReceivedContentMicTest
parameter_list|()
throws|throws
name|Exception
block|{
name|HttpEntityEnclosingRequest
name|request
init|=
operator|new
name|BasicHttpEntityEnclosingRequest
argument_list|(
literal|"POST"
argument_list|,
literal|"/"
argument_list|,
name|HttpVersion
operator|.
name|HTTP_1_1
argument_list|)
decl_stmt|;
name|request
operator|.
name|addHeader
argument_list|(
name|AS2Header
operator|.
name|DISPOSITION_NOTIFICATION_OPTIONS
argument_list|,
name|DISPOSITION_NOTIFICATION_OPTIONS_VALUE
argument_list|)
expr_stmt|;
name|request
operator|.
name|addHeader
argument_list|(
name|AS2Header
operator|.
name|CONTENT_TYPE
argument_list|,
name|CONTENT_TYPE_VALUE
argument_list|)
expr_stmt|;
name|ApplicationEDIFACTEntity
name|edifactEntity
init|=
operator|new
name|ApplicationEDIFACTEntity
argument_list|(
name|EDI_MESSAGE
argument_list|,
name|AS2Charset
operator|.
name|US_ASCII
argument_list|,
name|AS2TransferEncoding
operator|.
name|NONE
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|InputStream
name|is
init|=
name|edifactEntity
operator|.
name|getContent
argument_list|()
decl_stmt|;
name|BasicHttpEntity
name|basicEntity
init|=
operator|new
name|BasicHttpEntity
argument_list|()
decl_stmt|;
name|basicEntity
operator|.
name|setContent
argument_list|(
name|is
argument_list|)
expr_stmt|;
name|basicEntity
operator|.
name|setContentType
argument_list|(
name|CONTENT_TYPE_VALUE
argument_list|)
expr_stmt|;
name|request
operator|.
name|setEntity
argument_list|(
name|basicEntity
argument_list|)
expr_stmt|;
name|ReceivedContentMic
name|receivedContentMic
init|=
name|MicUtils
operator|.
name|createReceivedContentMic
argument_list|(
name|request
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Failed to create Received Content MIC"
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Digest Algorithm: "
operator|+
name|receivedContentMic
operator|.
name|getDigestAlgorithmId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected digest algorithm value"
argument_list|,
name|EXPECTED_MESSAGE_DIGEST_ALGORITHM
argument_list|,
name|receivedContentMic
operator|.
name|getDigestAlgorithmId
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Encoded Message Digest: "
operator|+
name|receivedContentMic
operator|.
name|getEncodedMessageDigest
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected encoded message digest value"
argument_list|,
name|EXPECTED_ENCODED_MESSAGE_DIGEST
argument_list|,
name|receivedContentMic
operator|.
name|getEncodedMessageDigest
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

