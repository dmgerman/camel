begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.log
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|log
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
name|Producer
import|;
end_import

begin_comment
comment|/**  * Log formatter test.  */
end_comment

begin_class
DECL|class|LogFormatterTest
specifier|public
class|class
name|LogFormatterTest
extends|extends
name|ContextTestSupport
block|{
DECL|method|testSendMessageToLogDefault ()
specifier|public
name|void
name|testSendMessageToLogDefault
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"log:org.apache.camel.TEST"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
block|}
DECL|method|testSendMessageToLogSingleOptions ()
specifier|public
name|void
name|testSendMessageToLogSingleOptions
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"log:org.apache.camel.TEST?showExchangeId=true"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"log:org.apache.camel.TEST?showProperties=true"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"log:org.apache.camel.TEST?showHeaders=true"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"log:org.apache.camel.TEST?showBodyType=true"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"log:org.apache.camel.TEST?showBody=true"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"log:org.apache.camel.TEST?showOut=true"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"log:org.apache.camel.TEST?showAll=true"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
block|}
DECL|method|testSendMessageToLogMultiOptions ()
specifier|public
name|void
name|testSendMessageToLogMultiOptions
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"log:org.apache.camel.TEST?showHeaders=true&showOut=true"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"log:org.apache.camel.TEST?showProperties=true&showHeaders=true&showOut=true"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
block|}
DECL|method|testSendMessageToLogShowFalse ()
specifier|public
name|void
name|testSendMessageToLogShowFalse
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"log:org.apache.camel.TEST?showBodyType=false"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
block|}
DECL|method|testSendMessageToLogMultiLine ()
specifier|public
name|void
name|testSendMessageToLogMultiLine
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"log:org.apache.camel.TEST?multiline=true"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
block|}
DECL|method|testSendExchangeWithOut ()
specifier|public
name|void
name|testSendExchangeWithOut
parameter_list|()
throws|throws
name|Exception
block|{
name|Endpoint
name|endpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"log:org.apache.camel.TEST?showAll=true&multiline=true"
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
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
literal|22
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
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|producer
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

