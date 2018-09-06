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
name|javax
operator|.
name|activation
operator|.
name|DataHandler
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|activation
operator|.
name|FileDataSource
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

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|PipelineTest
specifier|public
class|class
name|PipelineTest
extends|extends
name|ContextTestSupport
block|{
comment|/**      * Simple processor the copies the in to the out and increments a counter.      * Used to verify that the pipeline actually takes the output of one stage of       * the pipe and feeds it in as input into the next stage.      */
DECL|class|InToOut
specifier|private
specifier|static
specifier|final
class|class
name|InToOut
implements|implements
name|Processor
block|{
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
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|copyFrom
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|)
expr_stmt|;
name|Integer
name|counter
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"copy-counter"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|counter
operator|==
literal|null
condition|)
block|{
name|counter
operator|=
literal|0
expr_stmt|;
block|}
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"copy-counter"
argument_list|,
name|counter
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Simple processor the copies the in to the fault and increments a counter.      */
DECL|class|InToFault
specifier|private
specifier|static
specifier|final
class|class
name|InToFault
implements|implements
name|Processor
block|{
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
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setFault
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|Integer
name|counter
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"copy-counter"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|counter
operator|==
literal|null
condition|)
block|{
name|counter
operator|=
literal|0
expr_stmt|;
block|}
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"copy-counter"
argument_list|,
name|counter
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
DECL|field|resultEndpoint
specifier|protected
name|MockEndpoint
name|resultEndpoint
decl_stmt|;
annotation|@
name|Test
DECL|method|testSendMessageThroughAPipeline ()
specifier|public
name|void
name|testSendMessageThroughAPipeline
parameter_list|()
throws|throws
name|Exception
block|{
name|resultEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
literal|4
argument_list|)
expr_stmt|;
name|Exchange
name|results
init|=
name|template
operator|.
name|request
argument_list|(
literal|"direct:a"
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
block|{
comment|// now lets fire in a message
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|in
operator|.
name|setBody
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|in
operator|.
name|setHeader
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Result body"
argument_list|,
literal|4
argument_list|,
name|results
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
DECL|method|testResultsReturned ()
specifier|public
name|void
name|testResultsReturned
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
literal|"direct:b"
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
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hello World"
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
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"copy-counter"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Disabled for now until we figure out fault processing in the pipeline.      *       * @throws Exception      */
annotation|@
name|Test
DECL|method|testFaultStopsPipeline ()
specifier|public
name|void
name|testFaultStopsPipeline
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
literal|"direct:c"
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
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Fault Message"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
comment|// Check the fault..
name|assertTrue
argument_list|(
name|exchange
operator|.
name|getOut
argument_list|()
operator|!=
literal|null
operator|&&
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|isFault
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Fault Message"
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
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"copy-counter"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testOnlyProperties ()
specifier|public
name|void
name|testOnlyProperties
parameter_list|()
block|{
name|Exchange
name|exchange
init|=
name|template
operator|.
name|request
argument_list|(
literal|"direct:b"
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
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"header"
argument_list|,
literal|"headerValue"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"headerValue"
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"header"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"copy-counter"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCopyInOutExchange ()
specifier|public
name|void
name|testCopyInOutExchange
parameter_list|()
block|{
name|Exchange
name|exchange
init|=
name|template
operator|.
name|request
argument_list|(
literal|"direct:start"
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
block|{
name|exchange
operator|.
name|setPattern
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
comment|// there is always breadcrumb header
name|assertEquals
argument_list|(
literal|"There should have no message header"
argument_list|,
literal|1
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeaders
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"There should have no attachments"
argument_list|,
literal|0
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getAttachmentObjects
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"There should have no attachments"
argument_list|,
literal|0
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getAttachments
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong message body"
argument_list|,
literal|"test"
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
name|assertNull
argument_list|(
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"test"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getAttachmentObject
argument_list|(
literal|"test1.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getAttachment
argument_list|(
literal|"test1.xml"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
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
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|resultEndpoint
operator|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
block|{
specifier|final
name|Processor
name|processor
init|=
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
block|{
name|Integer
name|number
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|number
operator|==
literal|null
condition|)
block|{
name|number
operator|=
literal|0
expr_stmt|;
block|}
name|number
operator|=
name|number
operator|+
literal|1
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|number
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
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
comment|// START SNIPPET: example
name|from
argument_list|(
literal|"direct:a"
argument_list|)
operator|.
name|pipeline
argument_list|(
literal|"direct:x"
argument_list|,
literal|"direct:y"
argument_list|,
literal|"direct:z"
argument_list|,
literal|"mock:result"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: example
name|from
argument_list|(
literal|"direct:x"
argument_list|)
operator|.
name|process
argument_list|(
name|processor
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:y"
argument_list|)
operator|.
name|process
argument_list|(
name|processor
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:z"
argument_list|)
operator|.
name|process
argument_list|(
name|processor
argument_list|)
expr_stmt|;
comment|// Create a route that uses the  InToOut processor 3 times. the copy-counter header should be == 3
name|from
argument_list|(
literal|"direct:b"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|InToOut
argument_list|()
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|InToOut
argument_list|()
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|InToOut
argument_list|()
argument_list|)
expr_stmt|;
comment|// Create a route that uses the  InToFault processor.. the last InToOut will not be called since the Fault occurs before.
name|from
argument_list|(
literal|"direct:c"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|InToOut
argument_list|()
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|InToFault
argument_list|()
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|InToOut
argument_list|()
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
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|copyFrom
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|)
expr_stmt|;
comment|//Added the header and attachment
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"test"
argument_list|,
literal|"testValue"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|addAttachment
argument_list|(
literal|"test1.xml"
argument_list|,
operator|new
name|DataHandler
argument_list|(
operator|new
name|FileDataSource
argument_list|(
literal|"pom.xml"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
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
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|copyFrom
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"The test attachment should not be null"
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getAttachmentObject
argument_list|(
literal|"test1.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"The test attachment should not be null"
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getAttachment
argument_list|(
literal|"test1.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"The test header should not be null"
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"test"
argument_list|)
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|removeAttachment
argument_list|(
literal|"test1.xml"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|removeHeader
argument_list|(
literal|"test"
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

