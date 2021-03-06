begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|netty
package|;
end_package

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|Charset
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
name|Endpoint
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
name|ExchangePattern
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
name|Producer
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
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|NettyTransferExchangeOptionTest
specifier|public
class|class
name|NettyTransferExchangeOptionTest
extends|extends
name|BaseNettyTest
block|{
annotation|@
name|Test
DECL|method|testNettyTransferExchangeOptionWithoutException ()
specifier|public
name|void
name|testNettyTransferExchangeOptionWithoutException
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
name|sendExchange
argument_list|(
literal|false
argument_list|)
decl_stmt|;
name|assertExchange
argument_list|(
name|exchange
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNettyTransferExchangeOptionWithException ()
specifier|public
name|void
name|testNettyTransferExchangeOptionWithException
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
name|sendExchange
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|assertExchange
argument_list|(
name|exchange
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|sendExchange (boolean setException)
specifier|private
name|Exchange
name|sendExchange
parameter_list|(
name|boolean
name|setException
parameter_list|)
throws|throws
name|Exception
block|{
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"netty:tcp://localhost:{{port}}?transferExchange=true"
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|Message
name|message
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|message
operator|.
name|setBody
argument_list|(
literal|"Hello!"
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
literal|"cheese"
argument_list|,
literal|"feta"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
literal|"ham"
argument_list|,
literal|"old"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
literal|"setException"
argument_list|,
name|setException
argument_list|)
expr_stmt|;
name|Producer
name|producer
init|=
name|endpoint
operator|.
name|createProducer
argument_list|()
decl_stmt|;
name|producer
operator|.
name|start
argument_list|()
expr_stmt|;
comment|// ensure to stop producer after usage
try|try
block|{
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|producer
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
return|return
name|exchange
return|;
block|}
DECL|method|assertExchange (Exchange exchange, boolean hasException)
specifier|private
name|void
name|assertExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|boolean
name|hasException
parameter_list|)
block|{
if|if
condition|(
operator|!
name|hasException
condition|)
block|{
name|Message
name|out
init|=
name|exchange
operator|.
name|getOut
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Goodbye!"
argument_list|,
name|out
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"cheddar"
argument_list|,
name|out
operator|.
name|getHeader
argument_list|(
literal|"cheese"
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Message
name|fault
init|=
name|exchange
operator|.
name|getOut
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|fault
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|fault
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should get the InterruptedException exception"
argument_list|,
name|fault
operator|.
name|getBody
argument_list|()
operator|instanceof
name|InterruptedException
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"nihao"
argument_list|,
name|fault
operator|.
name|getHeader
argument_list|(
literal|"hello"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// in should stay the same
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello!"
argument_list|,
name|in
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"feta"
argument_list|,
name|in
operator|.
name|getHeader
argument_list|(
literal|"cheese"
argument_list|)
argument_list|)
expr_stmt|;
comment|// however the shared properties have changed
name|assertEquals
argument_list|(
literal|"fresh"
argument_list|,
name|exchange
operator|.
name|getProperty
argument_list|(
literal|"salami"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|exchange
operator|.
name|getProperty
argument_list|(
literal|"Charset"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
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
block|{
name|from
argument_list|(
literal|"netty:tcp://localhost:{{port}}?transferExchange=true"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|e
parameter_list|)
throws|throws
name|InterruptedException
block|{
name|assertNotNull
argument_list|(
name|e
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|e
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|e
operator|.
name|getProperties
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello!"
argument_list|,
name|e
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"feta"
argument_list|,
name|e
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"cheese"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"old"
argument_list|,
name|e
operator|.
name|getProperty
argument_list|(
literal|"ham"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|,
name|e
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|Boolean
name|setException
init|=
operator|(
name|Boolean
operator|)
name|e
operator|.
name|getProperty
argument_list|(
literal|"setException"
argument_list|)
decl_stmt|;
if|if
condition|(
name|setException
condition|)
block|{
name|e
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
operator|new
name|InterruptedException
argument_list|()
argument_list|)
expr_stmt|;
name|e
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"hello"
argument_list|,
literal|"nihao"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|e
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Goodbye!"
argument_list|)
expr_stmt|;
name|e
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"cheese"
argument_list|,
literal|"cheddar"
argument_list|)
expr_stmt|;
block|}
name|e
operator|.
name|setProperty
argument_list|(
literal|"salami"
argument_list|,
literal|"fresh"
argument_list|)
expr_stmt|;
name|e
operator|.
name|setProperty
argument_list|(
literal|"Charset"
argument_list|,
name|Charset
operator|.
name|defaultCharset
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

