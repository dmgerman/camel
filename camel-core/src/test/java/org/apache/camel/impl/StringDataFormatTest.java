begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
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
name|InputStream
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
name|CamelContext
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
name|ProducerTemplate
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
name|TestSupport
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

begin_comment
comment|/**  * Unit test of the string data format.  */
end_comment

begin_class
DECL|class|StringDataFormatTest
specifier|public
class|class
name|StringDataFormatTest
extends|extends
name|TestSupport
block|{
DECL|field|context
specifier|private
name|CamelContext
name|context
decl_stmt|;
DECL|field|template
specifier|private
name|ProducerTemplate
name|template
decl_stmt|;
DECL|method|setUp ()
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|=
operator|new
name|DefaultCamelContext
argument_list|()
expr_stmt|;
name|context
operator|.
name|setTracing
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|template
operator|=
name|context
operator|.
name|createProducerTemplate
argument_list|()
expr_stmt|;
name|template
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
DECL|method|tearDown ()
specifier|protected
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|stop
argument_list|()
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
DECL|method|testMarshalUTF8 ()
specifier|public
name|void
name|testMarshalUTF8
parameter_list|()
throws|throws
name|Exception
block|{
comment|// NOTE: We are using a processor to do the assertions as the mock endpoint (Camel) does not yet support
comment|// type conversion using byte and strings where you can set a charset encoding
comment|// include a UTF-8 char in the text \u0E08 is a Thai elephant
specifier|final
name|String
name|title
init|=
literal|"Hello Thai Elephant \u0E08"
decl_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|marshal
argument_list|()
operator|.
name|string
argument_list|(
literal|"UTF-8"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|MyBookProcessor
argument_list|(
literal|"UTF-8"
argument_list|,
name|title
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|MyBook
name|book
init|=
operator|new
name|MyBook
argument_list|()
decl_stmt|;
name|book
operator|.
name|setTitle
argument_list|(
name|title
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
name|book
argument_list|)
expr_stmt|;
block|}
DECL|method|testMarshalNoEncoding ()
specifier|public
name|void
name|testMarshalNoEncoding
parameter_list|()
throws|throws
name|Exception
block|{
comment|// NOTE: We are using a processor to do the assertions as the mock endpoint (Camel) does not yet support
comment|// type conversion using byte and strings where you can set a charset encoding
specifier|final
name|String
name|title
init|=
literal|"Hello World"
decl_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|marshal
argument_list|()
operator|.
name|string
argument_list|()
operator|.
name|process
argument_list|(
operator|new
name|MyBookProcessor
argument_list|(
literal|null
argument_list|,
name|title
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|MyBook
name|book
init|=
operator|new
name|MyBook
argument_list|()
decl_stmt|;
name|book
operator|.
name|setTitle
argument_list|(
name|title
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
name|book
argument_list|)
expr_stmt|;
block|}
DECL|method|testUnmarshalUTF8 ()
specifier|public
name|void
name|testUnmarshalUTF8
parameter_list|()
throws|throws
name|Exception
block|{
comment|// NOTE: Here we can use a MockEndpoint as we unmarshal the inputstream to String
comment|// include a UTF-8 char in the text \u0E08 is a Thai elephant
specifier|final
name|String
name|title
init|=
literal|"Hello Thai Elephant \u0E08"
decl_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|unmarshal
argument_list|()
operator|.
name|string
argument_list|(
literal|"UTF-8"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:unmarshal"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|byte
index|[]
name|bytes
init|=
name|title
operator|.
name|getBytes
argument_list|(
literal|"UTF-8"
argument_list|)
decl_stmt|;
name|InputStream
name|in
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|bytes
argument_list|)
decl_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
name|in
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mock
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"mock:unmarshal"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|mock
operator|.
name|setExpectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
name|title
argument_list|)
expr_stmt|;
block|}
DECL|method|testUnmarshalNoEncoding ()
specifier|public
name|void
name|testUnmarshalNoEncoding
parameter_list|()
throws|throws
name|Exception
block|{
comment|// NOTE: Here we can use a MockEndpoint as we unmarshal the inputstream to String
specifier|final
name|String
name|title
init|=
literal|"Hello World"
decl_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|unmarshal
argument_list|()
operator|.
name|string
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:unmarshal"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|byte
index|[]
name|bytes
init|=
name|title
operator|.
name|getBytes
argument_list|()
decl_stmt|;
name|InputStream
name|in
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|bytes
argument_list|)
decl_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
name|in
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mock
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"mock:unmarshal"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|mock
operator|.
name|setExpectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
name|title
argument_list|)
expr_stmt|;
block|}
DECL|class|MyBookProcessor
specifier|private
specifier|static
class|class
name|MyBookProcessor
implements|implements
name|Processor
block|{
DECL|field|encoding
specifier|private
name|String
name|encoding
decl_stmt|;
DECL|field|title
specifier|private
name|String
name|title
decl_stmt|;
DECL|method|MyBookProcessor (String encoding, String title)
specifier|public
name|MyBookProcessor
parameter_list|(
name|String
name|encoding
parameter_list|,
name|String
name|title
parameter_list|)
block|{
name|this
operator|.
name|encoding
operator|=
name|encoding
expr_stmt|;
name|this
operator|.
name|title
operator|=
name|title
expr_stmt|;
block|}
DECL|method|process (Exchange exchange)
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
name|byte
index|[]
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|)
decl_stmt|;
name|String
name|text
decl_stmt|;
if|if
condition|(
name|encoding
operator|!=
literal|null
condition|)
block|{
name|text
operator|=
operator|new
name|String
argument_list|(
name|body
argument_list|,
name|encoding
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|text
operator|=
operator|new
name|String
argument_list|(
name|body
argument_list|)
expr_stmt|;
block|}
comment|// does the testing
name|assertEquals
argument_list|(
name|text
argument_list|,
name|title
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|MyBook
specifier|private
specifier|static
class|class
name|MyBook
block|{
DECL|field|title
specifier|private
name|String
name|title
decl_stmt|;
DECL|method|setTitle (String title)
specifier|public
name|void
name|setTitle
parameter_list|(
name|String
name|title
parameter_list|)
block|{
name|this
operator|.
name|title
operator|=
name|title
expr_stmt|;
block|}
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
comment|// Camel will fallback to object toString converter and thus we get this text
return|return
name|title
return|;
block|}
block|}
block|}
end_class

end_unit

