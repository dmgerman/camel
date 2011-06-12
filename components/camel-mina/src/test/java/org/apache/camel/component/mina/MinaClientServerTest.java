begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mina
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mina
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
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|MinaClientServerTest
specifier|public
class|class
name|MinaClientServerTest
extends|extends
name|BaseMinaTest
block|{
annotation|@
name|Test
DECL|method|testSendToServer ()
specifier|public
name|void
name|testSendToServer
parameter_list|()
block|{
comment|// START SNIPPET: e3
name|String
name|out
init|=
operator|(
name|String
operator|)
name|template
operator|.
name|requestBody
argument_list|(
literal|"mina:tcp://localhost:{{port}}?sync=true&textline=true"
argument_list|,
literal|"Claus"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hello Claus"
argument_list|,
name|out
argument_list|)
expr_stmt|;
comment|// END SNIPPET: e3
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
comment|// START SNIPPET: e1
comment|// lets setup a server on port {{port}}
comment|// we set the sync option so we will send a reply
comment|// and we let the request-reply be processed in the MyServerProcessor
name|from
argument_list|(
literal|"mina:tcp://localhost:{{port}}?sync=true&textline=true"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|MyServerProcessor
argument_list|()
argument_list|)
expr_stmt|;
comment|// END SNIPPET: e1
block|}
block|}
return|;
block|}
comment|// START SNIPPET: e2
DECL|class|MyServerProcessor
specifier|private
class|class
name|MyServerProcessor
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
comment|// get the input from the IN body
name|String
name|name
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
comment|// send back a response on the OUT body
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Hello "
operator|+
name|name
argument_list|)
expr_stmt|;
block|}
block|}
comment|// END SNIPPET: e2
block|}
end_class

end_unit

