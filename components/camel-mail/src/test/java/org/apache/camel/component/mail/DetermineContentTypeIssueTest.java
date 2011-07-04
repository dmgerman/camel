begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Exchange
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
name|test
operator|.
name|junit4
operator|.
name|ExchangeTestSupport
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

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|DetermineContentTypeIssueTest
specifier|public
class|class
name|DetermineContentTypeIssueTest
extends|extends
name|ExchangeTestSupport
block|{
DECL|field|contentType
specifier|private
specifier|final
name|String
name|contentType
init|=
literal|"application/pkcs7-mime; smime-type=enveloped-data; name=\"smime.p7m\""
decl_stmt|;
annotation|@
name|Test
DECL|method|testDetermineContentTypeNoChange ()
specifier|public
name|void
name|testDetermineContentTypeNoChange
parameter_list|()
throws|throws
name|Exception
block|{
name|MailConfiguration
name|configuration
init|=
operator|new
name|MailConfiguration
argument_list|()
decl_stmt|;
name|MailBinding
name|binding
init|=
operator|new
name|MailBinding
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"Content-Type"
argument_list|,
name|contentType
argument_list|)
expr_stmt|;
name|String
name|determinedType
init|=
name|binding
operator|.
name|determineContentType
argument_list|(
name|configuration
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
comment|// no charset
name|assertEquals
argument_list|(
name|contentType
argument_list|,
name|determinedType
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDetermineContentTypeCharSetFromExchange ()
specifier|public
name|void
name|testDetermineContentTypeCharSetFromExchange
parameter_list|()
throws|throws
name|Exception
block|{
name|MailConfiguration
name|configuration
init|=
operator|new
name|MailConfiguration
argument_list|()
decl_stmt|;
name|MailBinding
name|binding
init|=
operator|new
name|MailBinding
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"Content-Type"
argument_list|,
name|contentType
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|CHARSET_NAME
argument_list|,
literal|"iso-8859-1"
argument_list|)
expr_stmt|;
name|String
name|determinedType
init|=
name|binding
operator|.
name|determineContentType
argument_list|(
name|configuration
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|String
name|expected
init|=
name|contentType
operator|+
literal|"; charset=iso-8859-1"
decl_stmt|;
comment|// should append the charset from exchange
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|determinedType
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDetermineContentTypeFallbackCharSetFromExchange ()
specifier|public
name|void
name|testDetermineContentTypeFallbackCharSetFromExchange
parameter_list|()
throws|throws
name|Exception
block|{
name|MailConfiguration
name|configuration
init|=
operator|new
name|MailConfiguration
argument_list|()
decl_stmt|;
name|MailBinding
name|binding
init|=
operator|new
name|MailBinding
argument_list|()
decl_stmt|;
name|String
name|type
init|=
name|contentType
operator|+
literal|"; charset=utf-8"
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"Content-Type"
argument_list|,
name|type
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|CHARSET_NAME
argument_list|,
literal|"iso-8859-1"
argument_list|)
expr_stmt|;
name|String
name|determinedType
init|=
name|binding
operator|.
name|determineContentType
argument_list|(
name|configuration
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
comment|// should keep existing charset
name|assertEquals
argument_list|(
name|type
argument_list|,
name|determinedType
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDetermineContentTypeIgnoreUnsupportedExchangeAsFallback ()
specifier|public
name|void
name|testDetermineContentTypeIgnoreUnsupportedExchangeAsFallback
parameter_list|()
throws|throws
name|Exception
block|{
name|MailConfiguration
name|configuration
init|=
operator|new
name|MailConfiguration
argument_list|()
decl_stmt|;
comment|// ignore unsupported
name|configuration
operator|.
name|setIgnoreUnsupportedCharset
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|MailBinding
name|binding
init|=
operator|new
name|MailBinding
argument_list|()
decl_stmt|;
name|String
name|type
init|=
name|contentType
operator|+
literal|"; charset=ansi_x3.110-1983"
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"Content-Type"
argument_list|,
name|type
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|CHARSET_NAME
argument_list|,
literal|"iso-8859-1"
argument_list|)
expr_stmt|;
name|String
name|determinedType
init|=
name|binding
operator|.
name|determineContentType
argument_list|(
name|configuration
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
comment|// should remove unsupported charset
name|assertEquals
argument_list|(
name|contentType
argument_list|,
name|determinedType
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDetermineContentTypeInvalidCharset ()
specifier|public
name|void
name|testDetermineContentTypeInvalidCharset
parameter_list|()
throws|throws
name|Exception
block|{
name|MailConfiguration
name|configuration
init|=
operator|new
name|MailConfiguration
argument_list|()
decl_stmt|;
name|MailBinding
name|binding
init|=
operator|new
name|MailBinding
argument_list|()
decl_stmt|;
name|String
name|type
init|=
name|contentType
operator|+
literal|"; charset=ansi_x3.110-1983"
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"Content-Type"
argument_list|,
name|type
argument_list|)
expr_stmt|;
name|String
name|determinedType
init|=
name|binding
operator|.
name|determineContentType
argument_list|(
name|configuration
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
comment|// should keep existing charset even if its unsupported as we configured it as that
name|assertEquals
argument_list|(
name|type
argument_list|,
name|determinedType
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDetermineContentTypeWithCharsetInMiddle ()
specifier|public
name|void
name|testDetermineContentTypeWithCharsetInMiddle
parameter_list|()
throws|throws
name|Exception
block|{
name|MailConfiguration
name|configuration
init|=
operator|new
name|MailConfiguration
argument_list|()
decl_stmt|;
name|MailBinding
name|binding
init|=
operator|new
name|MailBinding
argument_list|()
decl_stmt|;
name|String
name|type
init|=
literal|"text/plain; charset=iso-8859-1; foo=bar"
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"Content-Type"
argument_list|,
name|type
argument_list|)
expr_stmt|;
name|String
name|determinedType
init|=
name|binding
operator|.
name|determineContentType
argument_list|(
name|configuration
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
comment|// content-type is left untouched
name|assertEquals
argument_list|(
name|type
argument_list|,
name|determinedType
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

