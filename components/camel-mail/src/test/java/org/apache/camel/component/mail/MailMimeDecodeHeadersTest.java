begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|internet
operator|.
name|MimeUtility
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
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mock
operator|.
name|MockEndpoint
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
name|CamelTestSupport
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
name|jvnet
operator|.
name|mock_javamail
operator|.
name|Mailbox
import|;
end_import

begin_comment
comment|/**  * Unit test for Mail header decoding/unfolding support.  */
end_comment

begin_class
DECL|class|MailMimeDecodeHeadersTest
specifier|public
class|class
name|MailMimeDecodeHeadersTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|nonAsciiSubject
specifier|private
name|String
name|nonAsciiSubject
init|=
literal|"\uD83D\uDC2A rocks!"
decl_stmt|;
DECL|field|encodedNonAsciiSubject
specifier|private
name|String
name|encodedNonAsciiSubject
init|=
literal|"=?UTF-8?Q?=F0=9F=90=AA_rocks!?="
decl_stmt|;
DECL|field|longSubject
specifier|private
name|String
name|longSubject
decl_stmt|;
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"Camel rocks!"
argument_list|)
decl_stmt|;
name|int
name|mimeFoldingLimit
init|=
literal|76
decl_stmt|;
while|while
condition|(
name|sb
operator|.
name|length
argument_list|()
operator|<=
name|mimeFoldingLimit
condition|)
block|{
name|sb
operator|.
name|insert
argument_list|(
literal|7
argument_list|,
literal|"o"
argument_list|)
expr_stmt|;
block|}
name|longSubject
operator|=
name|sb
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
DECL|field|foldedLongSubject
specifier|private
name|String
name|foldedLongSubject
init|=
name|MimeUtility
operator|.
name|fold
argument_list|(
literal|9
argument_list|,
name|longSubject
argument_list|)
decl_stmt|;
annotation|@
name|Test
DECL|method|testLongMailSubject ()
specifier|public
name|void
name|testLongMailSubject
parameter_list|()
throws|throws
name|Exception
block|{
name|Mailbox
operator|.
name|clearAll
argument_list|()
expr_stmt|;
comment|// The email subject is>76 chars and will get MIME folded.
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:longSubject"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
comment|// When mimeDecodeHeaders=true is used, expect the received subject to be MIME unfolded.
name|MockEndpoint
name|mockDecoded
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:decoded"
argument_list|)
decl_stmt|;
name|mockDecoded
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mockDecoded
operator|.
name|expectedHeaderReceived
argument_list|(
literal|"subject"
argument_list|,
name|longSubject
argument_list|)
expr_stmt|;
name|mockDecoded
operator|.
name|setResultWaitTime
argument_list|(
literal|10000
argument_list|)
expr_stmt|;
name|mockDecoded
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
comment|// When mimeDecodeHeaders=false or missing, expect the received subject to be MIME folded.
name|MockEndpoint
name|mockPlain
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:plain"
argument_list|)
decl_stmt|;
name|mockPlain
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mockPlain
operator|.
name|expectedHeaderReceived
argument_list|(
literal|"subject"
argument_list|,
name|foldedLongSubject
argument_list|)
expr_stmt|;
name|mockPlain
operator|.
name|setResultWaitTime
argument_list|(
literal|10000
argument_list|)
expr_stmt|;
name|mockPlain
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNonAsciiMailSubject ()
specifier|public
name|void
name|testNonAsciiMailSubject
parameter_list|()
throws|throws
name|Exception
block|{
name|Mailbox
operator|.
name|clearAll
argument_list|()
expr_stmt|;
comment|// The email subject contains non-ascii characters and will be encoded.
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:nonAsciiSubject"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
comment|// When mimeDecodeHeaders=true is used, expect the received subject to be MIME encoded.
name|MockEndpoint
name|mockDecoded
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:decoded"
argument_list|)
decl_stmt|;
name|mockDecoded
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mockDecoded
operator|.
name|expectedHeaderReceived
argument_list|(
literal|"subject"
argument_list|,
name|nonAsciiSubject
argument_list|)
expr_stmt|;
name|mockDecoded
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
comment|// When mimeDecodeHeaders=false or missing, expect the received subject to be MIME encoded.
name|MockEndpoint
name|mockPlain
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:plain"
argument_list|)
decl_stmt|;
name|mockPlain
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mockPlain
operator|.
name|expectedHeaderReceived
argument_list|(
literal|"subject"
argument_list|,
name|encodedNonAsciiSubject
argument_list|)
expr_stmt|;
name|mockPlain
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
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
literal|"direct:longSubject"
argument_list|)
operator|.
name|setHeader
argument_list|(
literal|"subject"
argument_list|,
name|constant
argument_list|(
name|longSubject
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"smtp://plain@localhost"
argument_list|,
literal|"smtp://decoded@localhost"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:nonAsciiSubject"
argument_list|)
operator|.
name|setHeader
argument_list|(
literal|"subject"
argument_list|,
name|constant
argument_list|(
name|nonAsciiSubject
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"smtp://plain@localhost"
argument_list|,
literal|"smtp://decoded@localhost"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"pop3://localhost?username=plain&password=secret&initialDelay=100&delay=100"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:plain"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"pop3://localhost?username=decoded&password=secret&initialDelay=100&delay=100&mimeDecodeHeaders=true"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:decoded"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

