begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|file
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
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
name|ValidationException
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
comment|/**  * Unit test for consuming files but the exchange fails and is handled  * by the failure handler (usually the DeadLetterChannel)  */
end_comment

begin_class
DECL|class|FileConsumerFailureHandledTest
specifier|public
class|class
name|FileConsumerFailureHandledTest
extends|extends
name|ContextTestSupport
block|{
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
name|deleteDirectory
argument_list|(
literal|"target/data/messages/input"
argument_list|)
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testParis ()
specifier|public
name|void
name|testParis
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:valid"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello Paris"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file:target/data/messages/input/"
argument_list|,
literal|"Paris"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"paris.txt"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|oneExchangeDone
operator|.
name|matchesMockWaitTime
argument_list|()
expr_stmt|;
name|assertFiles
argument_list|(
literal|"paris.txt"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testLondon ()
specifier|public
name|void
name|testLondon
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:invalid"
argument_list|)
decl_stmt|;
comment|// we get the original input so its not Hello London but only London
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"London"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file:target/data/messages/input/"
argument_list|,
literal|"London"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"london.txt"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|oneExchangeDone
operator|.
name|matchesMockWaitTime
argument_list|()
expr_stmt|;
comment|// london should be deleted as we have failure handled it
name|assertFiles
argument_list|(
literal|"london.txt"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDublin ()
specifier|public
name|void
name|testDublin
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:beer"
argument_list|)
decl_stmt|;
comment|// we get the original input so its not Hello London but only London
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Dublin"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file:target/data/messages/input/"
argument_list|,
literal|"Dublin"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"dublin.txt"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|oneExchangeDone
operator|.
name|matchesMockWaitTime
argument_list|()
expr_stmt|;
comment|// dublin should NOT be deleted, but should be retired on next consumer
name|assertFiles
argument_list|(
literal|"dublin.txt"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMadrid ()
specifier|public
name|void
name|testMadrid
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:error"
argument_list|)
decl_stmt|;
comment|// we get the original input so its not Hello London but only London
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Madrid"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file:target/data/messages/input/"
argument_list|,
literal|"Madrid"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"madrid.txt"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|oneExchangeDone
operator|.
name|matchesMockWaitTime
argument_list|()
expr_stmt|;
comment|// madrid should be deleted as the DLC handles it
name|assertFiles
argument_list|(
literal|"madrid.txt"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|assertFiles (String filename, boolean deleted)
specifier|private
specifier|static
name|void
name|assertFiles
parameter_list|(
name|String
name|filename
parameter_list|,
name|boolean
name|deleted
parameter_list|)
throws|throws
name|InterruptedException
block|{
comment|// file should be deleted as delete=true in parameter in the route below
name|File
name|file
init|=
operator|new
name|File
argument_list|(
literal|"target/data/messages/input/"
operator|+
name|filename
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"File "
operator|+
name|filename
operator|+
literal|" should be deleted: "
operator|+
name|deleted
argument_list|,
name|deleted
argument_list|,
operator|!
name|file
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
comment|// and no lock files
name|String
name|lock
init|=
name|filename
operator|+
name|FileComponent
operator|.
name|DEFAULT_LOCK_FILE_POSTFIX
decl_stmt|;
name|file
operator|=
operator|new
name|File
argument_list|(
literal|"target/data/messages/input/"
operator|+
name|lock
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"File "
operator|+
name|lock
operator|+
literal|" should be deleted"
argument_list|,
name|file
operator|.
name|exists
argument_list|()
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
comment|// make sure mock:error is the dead letter channel
comment|// use no delay for fast unit testing
name|errorHandler
argument_list|(
name|deadLetterChannel
argument_list|(
literal|"mock:error"
argument_list|)
operator|.
name|maximumRedeliveries
argument_list|(
literal|2
argument_list|)
operator|.
name|redeliveryDelay
argument_list|(
literal|0
argument_list|)
operator|.
name|logStackTrace
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
comment|// special for not handled when we got beer
name|onException
argument_list|(
name|ValidationException
operator|.
name|class
argument_list|)
operator|.
name|onWhen
argument_list|(
name|exceptionMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"beer"
argument_list|)
argument_list|)
operator|.
name|handled
argument_list|(
literal|false
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:beer"
argument_list|)
expr_stmt|;
comment|// special failure handler for ValidationException
name|onException
argument_list|(
name|ValidationException
operator|.
name|class
argument_list|)
operator|.
name|handled
argument_list|(
literal|true
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:invalid"
argument_list|)
expr_stmt|;
comment|// our route logic to process files from the input folder
name|from
argument_list|(
literal|"file:target/data/messages/input/?initialDelay=0&delay=10&delete=true"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|MyValidatorProcessor
argument_list|()
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:valid"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|MyValidatorProcessor
specifier|private
specifier|static
class|class
name|MyValidatorProcessor
implements|implements
name|Processor
block|{
annotation|@
name|Override
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
name|String
name|body
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
if|if
condition|(
literal|"London"
operator|.
name|equals
argument_list|(
name|body
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ValidationException
argument_list|(
name|exchange
argument_list|,
literal|"Forced exception by unit test"
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
literal|"Madrid"
operator|.
name|equals
argument_list|(
name|body
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Madrid is not a supported city"
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
literal|"Dublin"
operator|.
name|equals
argument_list|(
name|body
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ValidationException
argument_list|(
name|exchange
argument_list|,
literal|"Dublin have good beer"
argument_list|)
throw|;
block|}
name|exchange
operator|.
name|getMessage
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Hello "
operator|+
name|body
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

