begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
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
name|io
operator|.
name|OutputStream
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
name|impl
operator|.
name|DefaultCamelContext
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
name|spi
operator|.
name|DataFormat
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
name|support
operator|.
name|DefaultMessage
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
name|support
operator|.
name|ServiceSupport
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
DECL|class|UnmarshalProcessorTest
specifier|public
class|class
name|UnmarshalProcessorTest
extends|extends
name|TestSupport
block|{
annotation|@
name|Test
DECL|method|testDataFormatReturnsSameExchange ()
specifier|public
name|void
name|testDataFormatReturnsSameExchange
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
name|createExchangeWithBody
argument_list|(
operator|new
name|DefaultCamelContext
argument_list|()
argument_list|,
literal|"body"
argument_list|)
decl_stmt|;
name|Processor
name|processor
init|=
operator|new
name|UnmarshalProcessor
argument_list|(
operator|new
name|MyDataFormat
argument_list|(
name|exchange
argument_list|)
argument_list|)
decl_stmt|;
name|processor
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"UnmarshalProcessor did not copy OUT from IN message"
argument_list|,
literal|"body"
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDataFormatReturnsAnotherExchange ()
specifier|public
name|void
name|testDataFormatReturnsAnotherExchange
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|Exchange
name|exchange
init|=
name|createExchangeWithBody
argument_list|(
name|context
argument_list|,
literal|"body"
argument_list|)
decl_stmt|;
name|Exchange
name|exchange2
init|=
name|createExchangeWithBody
argument_list|(
name|context
argument_list|,
literal|"body2"
argument_list|)
decl_stmt|;
name|Processor
name|processor
init|=
operator|new
name|UnmarshalProcessor
argument_list|(
operator|new
name|MyDataFormat
argument_list|(
name|exchange2
argument_list|)
argument_list|)
decl_stmt|;
name|processor
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|Exception
name|e
init|=
name|exchange
operator|.
name|getException
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The returned exchange "
operator|+
name|exchange2
operator|+
literal|" is not the same as "
operator|+
name|exchange
operator|+
literal|" provided to the DataFormat"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDataFormatReturnsMessage ()
specifier|public
name|void
name|testDataFormatReturnsMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
name|createExchangeWithBody
argument_list|(
operator|new
name|DefaultCamelContext
argument_list|()
argument_list|,
literal|"body"
argument_list|)
decl_stmt|;
name|Message
name|out
init|=
operator|new
name|DefaultMessage
argument_list|(
name|exchange
operator|.
name|getContext
argument_list|()
argument_list|)
decl_stmt|;
name|out
operator|.
name|setBody
argument_list|(
operator|new
name|Object
argument_list|()
argument_list|)
expr_stmt|;
name|Processor
name|processor
init|=
operator|new
name|UnmarshalProcessor
argument_list|(
operator|new
name|MyDataFormat
argument_list|(
name|out
argument_list|)
argument_list|)
decl_stmt|;
name|processor
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
literal|"UnmarshalProcessor did not make use of the returned OUT message"
argument_list|,
name|out
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
literal|"UnmarshalProcessor did change the body bound to the OUT message"
argument_list|,
name|out
operator|.
name|getBody
argument_list|()
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDataFormatReturnsBody ()
specifier|public
name|void
name|testDataFormatReturnsBody
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
name|createExchangeWithBody
argument_list|(
operator|new
name|DefaultCamelContext
argument_list|()
argument_list|,
literal|"body"
argument_list|)
decl_stmt|;
name|Object
name|unmarshalled
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|Processor
name|processor
init|=
operator|new
name|UnmarshalProcessor
argument_list|(
operator|new
name|MyDataFormat
argument_list|(
name|unmarshalled
argument_list|)
argument_list|)
decl_stmt|;
name|processor
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
literal|"UnmarshalProcessor did not make use of the returned object being returned while unmarshalling"
argument_list|,
name|unmarshalled
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|class|MyDataFormat
specifier|private
specifier|static
class|class
name|MyDataFormat
extends|extends
name|ServiceSupport
implements|implements
name|DataFormat
block|{
DECL|field|object
specifier|private
specifier|final
name|Object
name|object
decl_stmt|;
DECL|method|MyDataFormat (Exchange exchange)
name|MyDataFormat
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|object
operator|=
name|exchange
expr_stmt|;
block|}
DECL|method|MyDataFormat (Message message)
name|MyDataFormat
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|object
operator|=
name|message
expr_stmt|;
block|}
DECL|method|MyDataFormat (Object unmarshalled)
name|MyDataFormat
parameter_list|(
name|Object
name|unmarshalled
parameter_list|)
block|{
name|object
operator|=
name|unmarshalled
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|marshal (Exchange exchange, Object graph, OutputStream stream)
specifier|public
name|void
name|marshal
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|graph
parameter_list|,
name|OutputStream
name|stream
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|IllegalAccessException
argument_list|(
literal|"This method is not expected to be used by UnmarshalProcessor"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|unmarshal (Exchange exchange, InputStream stream)
specifier|public
name|Object
name|unmarshal
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|InputStream
name|stream
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|object
return|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
block|}
block|}
block|}
end_class

end_unit

