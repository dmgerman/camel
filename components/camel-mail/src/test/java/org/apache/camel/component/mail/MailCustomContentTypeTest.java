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
name|javax
operator|.
name|mail
operator|.
name|Message
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
name|ContextTestSupport
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
name|builder
operator|.
name|RouteBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jvnet
operator|.
name|mock_javamail
operator|.
name|Mailbox
import|;
end_import

begin_comment
comment|/**  * Unit test for contentType option.  */
end_comment

begin_class
DECL|class|MailCustomContentTypeTest
specifier|public
class|class
name|MailCustomContentTypeTest
extends|extends
name|ContextTestSupport
block|{
DECL|method|testSendHtmlMail ()
specifier|public
name|void
name|testSendHtmlMail
parameter_list|()
throws|throws
name|Exception
block|{
name|Mailbox
operator|.
name|clearAll
argument_list|()
expr_stmt|;
name|sendBody
argument_list|(
literal|"direct:a"
argument_list|,
literal|"<html><body><h1>Hello</h1>World</body></html>"
argument_list|)
expr_stmt|;
name|Mailbox
name|box
init|=
name|Mailbox
operator|.
name|get
argument_list|(
literal|"claus@localhost"
argument_list|)
decl_stmt|;
name|Message
name|msg
init|=
name|box
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|msg
operator|.
name|getContentType
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"text/html"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"text/html; charset=UTF-8"
argument_list|,
name|msg
operator|.
name|getContentType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<html><body><h1>Hello</h1>World</body></html>"
argument_list|,
name|msg
operator|.
name|getContent
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testSendHtmlMailIso88591 ()
specifier|public
name|void
name|testSendHtmlMailIso88591
parameter_list|()
throws|throws
name|Exception
block|{
name|Mailbox
operator|.
name|clearAll
argument_list|()
expr_stmt|;
name|sendBody
argument_list|(
literal|"direct:c"
argument_list|,
literal|"<html><body><h1>Hello</h1>World</body></html>"
argument_list|)
expr_stmt|;
name|Mailbox
name|box
init|=
name|Mailbox
operator|.
name|get
argument_list|(
literal|"claus@localhost"
argument_list|)
decl_stmt|;
name|Message
name|msg
init|=
name|box
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|msg
operator|.
name|getContentType
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"text/html"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"text/html; charset=iso-8859-1"
argument_list|,
name|msg
operator|.
name|getContentType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<html><body><h1>Hello</h1>World</body></html>"
argument_list|,
name|msg
operator|.
name|getContent
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testSendPlainMailContentTypeInHeader ()
specifier|public
name|void
name|testSendPlainMailContentTypeInHeader
parameter_list|()
throws|throws
name|Exception
block|{
name|Mailbox
operator|.
name|clearAll
argument_list|()
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:b"
argument_list|,
literal|"Hello World"
argument_list|,
literal|"contentType"
argument_list|,
literal|"text/plain; charset=iso-8859-1"
argument_list|)
expr_stmt|;
name|Mailbox
name|box
init|=
name|Mailbox
operator|.
name|get
argument_list|(
literal|"claus@localhost"
argument_list|)
decl_stmt|;
name|Message
name|msg
init|=
name|box
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"text/plain; charset=iso-8859-1"
argument_list|,
name|msg
operator|.
name|getContentType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|msg
operator|.
name|getContent
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testSendPlainMailContentTypeInHeader2 ()
specifier|public
name|void
name|testSendPlainMailContentTypeInHeader2
parameter_list|()
throws|throws
name|Exception
block|{
name|Mailbox
operator|.
name|clearAll
argument_list|()
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:b"
argument_list|,
literal|"Hello World"
argument_list|,
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
literal|"text/plain; charset=iso-8859-1"
argument_list|)
expr_stmt|;
name|Mailbox
name|box
init|=
name|Mailbox
operator|.
name|get
argument_list|(
literal|"claus@localhost"
argument_list|)
decl_stmt|;
name|Message
name|msg
init|=
name|box
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"text/plain; charset=iso-8859-1"
argument_list|,
name|msg
operator|.
name|getContentType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|msg
operator|.
name|getContent
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testSendPlainMailContentTypeTinyTypeInHeader ()
specifier|public
name|void
name|testSendPlainMailContentTypeTinyTypeInHeader
parameter_list|()
throws|throws
name|Exception
block|{
name|Mailbox
operator|.
name|clearAll
argument_list|()
expr_stmt|;
comment|// Camel will fixup the Content-Type if you do not have a space after the semi colon
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:b"
argument_list|,
literal|"Hello World"
argument_list|,
literal|"contentType"
argument_list|,
literal|"text/plain;charset=iso-8859-1"
argument_list|)
expr_stmt|;
name|Mailbox
name|box
init|=
name|Mailbox
operator|.
name|get
argument_list|(
literal|"claus@localhost"
argument_list|)
decl_stmt|;
name|Message
name|msg
init|=
name|box
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
comment|// the content type should have a space after the semi colon
name|assertEquals
argument_list|(
literal|"text/plain; charset=iso-8859-1"
argument_list|,
name|msg
operator|.
name|getContentType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|msg
operator|.
name|getContent
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"direct:a"
argument_list|)
operator|.
name|to
argument_list|(
literal|"smtp://claus@localhost?contentType=text/html;charset=UTF-8"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:b"
argument_list|)
operator|.
name|to
argument_list|(
literal|"smtp://claus@localhost"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:c"
argument_list|)
operator|.
name|to
argument_list|(
literal|"smtp://claus@localhost?contentType=text/html;charset=iso-8859-1"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

