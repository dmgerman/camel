begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.issues
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|issues
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
name|java
operator|.
name|io
operator|.
name|Reader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringReader
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|stream
operator|.
name|StreamSource
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
name|RuntimeCamelException
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

begin_class
DECL|class|CacheInputStreamInDeadLetterIssue520Test
specifier|public
class|class
name|CacheInputStreamInDeadLetterIssue520Test
extends|extends
name|ContextTestSupport
block|{
DECL|field|count
specifier|private
name|int
name|count
decl_stmt|;
DECL|method|testSendingInputStream ()
specifier|public
name|void
name|testSendingInputStream
parameter_list|()
throws|throws
name|Exception
block|{
name|InputStream
name|message
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
literal|"<hello>Willem</hello>"
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
name|sendingMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
DECL|method|testSendingReader ()
specifier|public
name|void
name|testSendingReader
parameter_list|()
throws|throws
name|Exception
block|{
name|StringReader
name|message
init|=
operator|new
name|StringReader
argument_list|(
literal|"<hello>Willem</hello>"
argument_list|)
decl_stmt|;
name|sendingMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
DECL|method|testSendingSource ()
specifier|public
name|void
name|testSendingSource
parameter_list|()
throws|throws
name|Exception
block|{
name|StreamSource
name|message
init|=
operator|new
name|StreamSource
argument_list|(
operator|new
name|StringReader
argument_list|(
literal|"<hello>Willem</hello>"
argument_list|)
argument_list|)
decl_stmt|;
name|sendingMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
DECL|method|sendingMessage (Object message)
specifier|private
name|void
name|sendingMessage
parameter_list|(
name|Object
name|message
parameter_list|)
throws|throws
name|InterruptedException
block|{
name|count
operator|=
literal|0
expr_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:error"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
try|try
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeCamelException
name|e
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
operator|instanceof
name|Exception
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Forced exception by unit test"
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|"The message should be delivered 4 times"
argument_list|,
name|count
argument_list|,
literal|4
argument_list|)
expr_stmt|;
name|mock
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
name|errorHandler
argument_list|(
name|deadLetterChannel
argument_list|(
literal|"direct:errorHandler"
argument_list|)
operator|.
name|maximumRedeliveries
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
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
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|count
operator|++
expr_stmt|;
comment|// Read the in stream from cache
name|String
name|result
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Should read the inputstream out again"
argument_list|,
literal|"<hello>Willem</hello>"
argument_list|,
name|result
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|Exception
argument_list|(
literal|"Forced exception by unit test"
argument_list|)
throw|;
block|}
block|}
argument_list|)
expr_stmt|;
comment|//Need to set the streamCaching for the deadLetterChannel
name|from
argument_list|(
literal|"direct:errorHandler"
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
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|result
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Should read the inputstream out again"
argument_list|,
literal|"<hello>Willem</hello>"
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:error"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

