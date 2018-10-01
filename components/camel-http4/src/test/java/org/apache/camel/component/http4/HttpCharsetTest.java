begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.http4
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|http4
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
name|Processor
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
name|http4
operator|.
name|handler
operator|.
name|BasicValidationHandler
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
name|bootstrap
operator|.
name|HttpServer
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
name|bootstrap
operator|.
name|ServerBootstrap
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

begin_class
DECL|class|HttpCharsetTest
specifier|public
class|class
name|HttpCharsetTest
extends|extends
name|BaseHttpTest
block|{
comment|// default content encoding of the local test server
DECL|field|charset
specifier|private
name|String
name|charset
init|=
literal|"ISO-8859-1"
decl_stmt|;
DECL|field|localServer
specifier|private
name|HttpServer
name|localServer
decl_stmt|;
annotation|@
name|Before
annotation|@
name|Override
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|localServer
operator|=
name|ServerBootstrap
operator|.
name|bootstrap
argument_list|()
operator|.
name|setHttpProcessor
argument_list|(
name|getBasicHttpProcessor
argument_list|()
argument_list|)
operator|.
name|setConnectionReuseStrategy
argument_list|(
name|getConnectionReuseStrategy
argument_list|()
argument_list|)
operator|.
name|setResponseFactory
argument_list|(
name|getHttpResponseFactory
argument_list|()
argument_list|)
operator|.
name|setExpectationVerifier
argument_list|(
name|getHttpExpectationVerifier
argument_list|()
argument_list|)
operator|.
name|setSslContext
argument_list|(
name|getSSLContext
argument_list|()
argument_list|)
operator|.
name|registerHandler
argument_list|(
literal|"/"
argument_list|,
operator|new
name|BasicValidationHandler
argument_list|(
literal|"POST"
argument_list|,
literal|null
argument_list|,
name|getBody
argument_list|()
argument_list|,
name|getExpectedContent
argument_list|()
argument_list|)
argument_list|)
operator|.
name|create
argument_list|()
expr_stmt|;
name|localServer
operator|.
name|start
argument_list|()
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
annotation|@
name|After
annotation|@
name|Override
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
if|if
condition|(
name|localServer
operator|!=
literal|null
condition|)
block|{
name|localServer
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|sendCharsetInExchangeProperty ()
specifier|public
name|void
name|sendCharsetInExchangeProperty
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
name|template
operator|.
name|request
argument_list|(
literal|"http4://"
operator|+
name|localServer
operator|.
name|getInetAddress
argument_list|()
operator|.
name|getHostName
argument_list|()
operator|+
literal|":"
operator|+
name|localServer
operator|.
name|getLocalPort
argument_list|()
operator|+
literal|"/"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|CHARSET_NAME
argument_list|,
name|charset
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|sendByteArrayCharsetInExchangeProperty ()
specifier|public
name|void
name|sendByteArrayCharsetInExchangeProperty
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
name|template
operator|.
name|request
argument_list|(
literal|"http4://"
operator|+
name|localServer
operator|.
name|getInetAddress
argument_list|()
operator|.
name|getHostName
argument_list|()
operator|+
literal|":"
operator|+
name|localServer
operator|.
name|getLocalPort
argument_list|()
operator|+
literal|"/"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|CHARSET_NAME
argument_list|,
name|charset
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|getBody
argument_list|()
operator|.
name|getBytes
argument_list|(
name|charset
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|sendInputStreamCharsetInExchangeProperty ()
specifier|public
name|void
name|sendInputStreamCharsetInExchangeProperty
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
name|template
operator|.
name|request
argument_list|(
literal|"http4://"
operator|+
name|localServer
operator|.
name|getInetAddress
argument_list|()
operator|.
name|getHostName
argument_list|()
operator|+
literal|":"
operator|+
name|localServer
operator|.
name|getLocalPort
argument_list|()
operator|+
literal|"/"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|CHARSET_NAME
argument_list|,
name|charset
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|getBody
argument_list|()
operator|.
name|getBytes
argument_list|(
name|charset
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
DECL|method|getBody ()
specifier|protected
name|String
name|getBody
parameter_list|()
block|{
name|char
name|lattinSmallLetterAWithDiaeresis
init|=
literal|0x00E4
decl_stmt|;
name|char
name|lattinSmallLetterOWithDiaeresis
init|=
literal|0x00F6
decl_stmt|;
name|char
name|lattinSmallLetterUWithDiaeresis
init|=
literal|0x00FC
decl_stmt|;
name|char
name|lattinSmallLetterSharpS
init|=
literal|0x00DF
decl_stmt|;
return|return
literal|"hl=de&q=camel+"
operator|+
name|lattinSmallLetterAWithDiaeresis
operator|+
name|lattinSmallLetterOWithDiaeresis
operator|+
name|lattinSmallLetterUWithDiaeresis
operator|+
name|lattinSmallLetterSharpS
return|;
block|}
block|}
end_class

end_unit

