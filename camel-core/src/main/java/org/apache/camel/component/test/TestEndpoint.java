begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.test
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|test
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|Component
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
name|spi
operator|.
name|UriEndpoint
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
name|UriParam
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
name|EndpointHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * A<a href="http://camel.apache.org/test.html">Test Endpoint</a> is a  *<a href="http://camel.apache.org/mock.html">Mock Endpoint</a> for testing but it will  * pull all messages from the nested endpoint and use those as expected message body assertions.  *  * @version   */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"test"
argument_list|)
DECL|class|TestEndpoint
specifier|public
class|class
name|TestEndpoint
extends|extends
name|MockEndpoint
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|TestEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|expectedMessageEndpoint
specifier|private
specifier|final
name|Endpoint
name|expectedMessageEndpoint
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"2000"
argument_list|)
DECL|field|timeout
specifier|private
name|long
name|timeout
init|=
literal|2000L
decl_stmt|;
DECL|method|TestEndpoint (String endpointUri, Component component, Endpoint expectedMessageEndpoint)
specifier|public
name|TestEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|,
name|Endpoint
name|expectedMessageEndpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|expectedMessageEndpoint
operator|=
name|expectedMessageEndpoint
expr_stmt|;
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
name|LOG
operator|.
name|debug
argument_list|(
literal|"Consuming expected messages from: {}"
argument_list|,
name|expectedMessageEndpoint
argument_list|)
expr_stmt|;
specifier|final
name|List
argument_list|<
name|Object
argument_list|>
name|expectedBodies
init|=
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|EndpointHelper
operator|.
name|pollEndpoint
argument_list|(
name|expectedMessageEndpoint
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
name|Object
name|body
init|=
name|getInBody
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Received message body {}"
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|expectedBodies
operator|.
name|add
argument_list|(
name|body
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|,
name|timeout
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Received: {} expected message(s) from: {}"
argument_list|,
name|expectedBodies
operator|.
name|size
argument_list|()
argument_list|,
name|expectedMessageEndpoint
argument_list|)
expr_stmt|;
name|expectedBodiesReceived
argument_list|(
name|expectedBodies
argument_list|)
expr_stmt|;
block|}
comment|/**      * This method allows us to convert or coerce the expected message body into some other type      */
DECL|method|getInBody (Exchange exchange)
specifier|protected
name|Object
name|getInBody
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
return|;
block|}
DECL|method|getTimeout ()
specifier|public
name|long
name|getTimeout
parameter_list|()
block|{
return|return
name|timeout
return|;
block|}
DECL|method|setTimeout (long timeout)
specifier|public
name|void
name|setTimeout
parameter_list|(
name|long
name|timeout
parameter_list|)
block|{
name|this
operator|.
name|timeout
operator|=
name|timeout
expr_stmt|;
block|}
block|}
end_class

end_unit

