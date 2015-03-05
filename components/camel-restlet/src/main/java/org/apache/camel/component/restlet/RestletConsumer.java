begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.restlet
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|restlet
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
name|impl
operator|.
name|DefaultConsumer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|restlet
operator|.
name|Request
import|;
end_import

begin_import
import|import
name|org
operator|.
name|restlet
operator|.
name|Response
import|;
end_import

begin_import
import|import
name|org
operator|.
name|restlet
operator|.
name|Restlet
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
comment|/**  * A Restlet consumer acts as a server to listen client requests.  *  * @version   */
end_comment

begin_class
DECL|class|RestletConsumer
specifier|public
class|class
name|RestletConsumer
extends|extends
name|DefaultConsumer
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
name|RestletConsumer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|restlet
specifier|private
name|Restlet
name|restlet
decl_stmt|;
DECL|method|RestletConsumer (Endpoint endpoint, Processor processor)
specifier|public
name|RestletConsumer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|RestletEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|RestletEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
DECL|method|createRestlet ()
specifier|protected
name|Restlet
name|createRestlet
parameter_list|()
block|{
return|return
operator|new
name|Restlet
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|handle
parameter_list|(
name|Request
name|request
parameter_list|,
name|Response
name|response
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Consumer restlet handle request method: {}"
argument_list|,
name|request
operator|.
name|getMethod
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|Exchange
name|exchange
init|=
name|getEndpoint
argument_list|()
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|RestletBinding
name|binding
init|=
name|getEndpoint
argument_list|()
operator|.
name|getRestletBinding
argument_list|()
decl_stmt|;
name|binding
operator|.
name|populateExchangeFromRestletRequest
argument_list|(
name|request
argument_list|,
name|response
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
try|try
block|{
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
name|binding
operator|.
name|populateRestletResponseFromExchange
argument_list|(
name|exchange
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Cannot process request"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
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
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
name|restlet
operator|=
name|createRestlet
argument_list|()
expr_stmt|;
name|getEndpoint
argument_list|()
operator|.
name|connect
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|restlet
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|public
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|getEndpoint
argument_list|()
operator|.
name|disconnect
argument_list|(
name|this
argument_list|)
expr_stmt|;
if|if
condition|(
name|restlet
operator|!=
literal|null
condition|)
block|{
name|restlet
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
DECL|method|getRestlet ()
specifier|public
name|Restlet
name|getRestlet
parameter_list|()
block|{
return|return
name|restlet
return|;
block|}
block|}
end_class

end_unit

