begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|CamelExchangeException
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
name|ConsumerTemplate
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
name|util
operator|.
name|FileUtil
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|FileConsumePollEnrichFileUsingProcessorTest
specifier|public
class|class
name|FileConsumePollEnrichFileUsingProcessorTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Override
DECL|method|setUp ()
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteDirectory
argument_list|(
literal|"target/enrich"
argument_list|)
expr_stmt|;
name|deleteDirectory
argument_list|(
literal|"target/enrichdata"
argument_list|)
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
DECL|method|testPollEnrich ()
specifier|public
name|void
name|testPollEnrich
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:start"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Start"
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Big file"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedFileExists
argument_list|(
literal|"target/enrich/.done/AAA.fin"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedFileExists
argument_list|(
literal|"target/enrichdata/.done/AAA.dat"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedFileExists
argument_list|(
literal|"target/enrichdata/BBB.dat"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file://target/enrichdata"
argument_list|,
literal|"Big file"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"AAA.dat"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file://target/enrichdata"
argument_list|,
literal|"Other Big file"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"BBB.dat"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file://target/enrich"
argument_list|,
literal|"Start"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"AAA.fin"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
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
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"file://target/enrich?initialDelay=0&delay=10&move=.done"
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
name|name
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|FILE_NAME_ONLY
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|name
operator|=
name|FileUtil
operator|.
name|stripExt
argument_list|(
name|name
argument_list|)
operator|+
literal|".dat"
expr_stmt|;
comment|// use a consumer template to get the data file
name|Exchange
name|data
init|=
literal|null
decl_stmt|;
name|ConsumerTemplate
name|con
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|createConsumerTemplate
argument_list|()
decl_stmt|;
try|try
block|{
comment|// try to get the data file
name|data
operator|=
name|con
operator|.
name|receive
argument_list|(
literal|"file://target/enrichdata?initialDelay=0&delay=10&move=.done&fileName="
operator|+
name|name
argument_list|,
literal|5000
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
comment|// stop the consumer as it does not need to poll for files anymore
name|con
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
comment|// if we found the data file then process it by sending it to the direct:data endpoint
if|if
condition|(
name|data
operator|!=
literal|null
condition|)
block|{
name|template
operator|.
name|send
argument_list|(
literal|"direct:data"
argument_list|,
name|data
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// otherwise do a rollback
throw|throw
operator|new
name|CamelExchangeException
argument_list|(
literal|"Cannot find the data file "
operator|+
name|name
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
block|}
block|}
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:start"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:data"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

