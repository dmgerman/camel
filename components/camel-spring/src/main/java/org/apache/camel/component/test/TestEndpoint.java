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
name|Service
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
name|EndpointHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_comment
comment|/**  * A<a href="http://activemq.apache.org/camel/test.html">Test Endpoint</a> is a  *<a href="http://activemq.apache.org/camel/mock.html">Mock Endpoint</a> for testing but it will  * pull all messages from the nested endpoint and use those as expected message body assertions.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|TestEndpoint
specifier|public
class|class
name|TestEndpoint
extends|extends
name|MockEndpoint
implements|implements
name|Service
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
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
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Consuming expected messages from: "
operator|+
name|expectedMessageEndpoint
argument_list|)
expr_stmt|;
specifier|final
name|List
name|expectedBodies
init|=
operator|new
name|ArrayList
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
literal|"Received: "
operator|+
name|expectedBodies
operator|.
name|size
argument_list|()
operator|+
literal|" expected message(s) from: "
operator|+
name|expectedMessageEndpoint
argument_list|)
expr_stmt|;
name|expectedBodiesReceived
argument_list|(
name|expectedBodies
argument_list|)
expr_stmt|;
block|}
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
throws|throws
name|Exception
block|{     }
comment|/**      * This method allows us to convert or cooerce the expected message body into some other type      */
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
block|}
end_class

end_unit

