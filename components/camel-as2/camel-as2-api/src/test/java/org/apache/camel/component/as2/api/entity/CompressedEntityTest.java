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
name|org
operator|.
name|bouncycastle
operator|.
name|cms
operator|.
name|CMSCompressedDataGenerator
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
name|ZlibCompressor
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
name|ZlibExpanderProvider
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
name|OutputCompressor
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
name|assertTrue
import|;
end_import

begin_class
DECL|class|CompressedEntityTest
specifier|public
class|class
name|CompressedEntityTest
block|{
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
operator|+
literal|"\r\n"
decl_stmt|;
DECL|field|APPLICATION_PKCS7_MIME_COMPRESSED_TRANSFER_ENCODING
specifier|public
specifier|static
specifier|final
name|String
name|APPLICATION_PKCS7_MIME_COMPRESSED_TRANSFER_ENCODING
init|=
literal|"base64"
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
DECL|method|createCompressedEntityTest ()
specifier|public
name|void
name|createCompressedEntityTest
parameter_list|()
throws|throws
name|Exception
block|{
name|TextPlainEntity
name|textPlainEntity
init|=
operator|new
name|TextPlainEntity
argument_list|(
name|TEXT_PLAIN_CONTENT
argument_list|,
name|TEXT_PLAIN_CONTENT_CHARSET_NAME
argument_list|,
name|TEXT_PLAIN_CONTENT_TRANSFER_ENCODING
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|CMSCompressedDataGenerator
name|cGen
init|=
operator|new
name|CMSCompressedDataGenerator
argument_list|()
decl_stmt|;
name|OutputCompressor
name|compressor
init|=
operator|new
name|ZlibCompressor
argument_list|()
decl_stmt|;
name|ApplicationPkcs7MimeCompressedDataEntity
name|compressedEntity
init|=
operator|new
name|ApplicationPkcs7MimeCompressedDataEntity
argument_list|(
name|textPlainEntity
argument_list|,
name|cGen
argument_list|,
name|compressor
argument_list|,
name|APPLICATION_PKCS7_MIME_COMPRESSED_TRANSFER_ENCODING
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|MimeEntity
name|decompressedEntity
init|=
name|compressedEntity
operator|.
name|getCompressedEntity
argument_list|(
operator|new
name|ZlibExpanderProvider
argument_list|()
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|""
argument_list|,
name|decompressedEntity
operator|instanceof
name|TextPlainEntity
argument_list|)
expr_stmt|;
name|TextPlainEntity
name|decompressedTextPlainEntity
init|=
operator|(
name|TextPlainEntity
operator|)
name|decompressedEntity
decl_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|EXPECTED_TEXT_PLAIN_CONTENT
argument_list|,
name|decompressedTextPlainEntity
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

