begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Exchange
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

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|netty
operator|.
name|channel
operator|.
name|Channel
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|netty
operator|.
name|channel
operator|.
name|ChannelFuture
import|;
end_import

begin_comment
comment|/**  * Helper class used internally by camel-netty using Netty.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|NettyHelper
specifier|public
specifier|final
class|class
name|NettyHelper
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
name|NettyHelper
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|NettyHelper ()
specifier|private
name|NettyHelper
parameter_list|()
block|{
comment|// Utility class
block|}
comment|/**      * Writes the given body to Netty channel. Will wait until the body has been written.      *      * @param channel  the Netty channel      * @param body     the body to write (send)      * @param exchange the exchange      * @throws CamelExchangeException is thrown if the body could not be written for some reasons      *                                (eg remote connection is closed etc.)      */
DECL|method|writeBody (Channel channel, Object body, Exchange exchange)
specifier|public
specifier|static
name|void
name|writeBody
parameter_list|(
name|Channel
name|channel
parameter_list|,
name|Object
name|body
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|CamelExchangeException
block|{
comment|// the write operation is asynchronous. Use future to wait until the session has been written
name|ChannelFuture
name|future
init|=
name|channel
operator|.
name|write
argument_list|(
name|body
argument_list|)
decl_stmt|;
comment|// wait for the write
name|future
operator|.
name|awaitUninterruptibly
argument_list|()
expr_stmt|;
comment|// if it was not a success then thrown an exception
if|if
condition|(
name|future
operator|.
name|isSuccess
argument_list|()
operator|==
literal|false
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Cannot write body: "
operator|+
name|body
operator|+
literal|" using channel: "
operator|+
name|channel
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|CamelExchangeException
argument_list|(
literal|"Cannot write body"
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

