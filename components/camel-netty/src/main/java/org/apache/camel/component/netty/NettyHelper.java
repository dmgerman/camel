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
name|java
operator|.
name|net
operator|.
name|SocketAddress
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
name|NoTypeConversionAvailableException
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
name|ChannelFutureListener
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
comment|/**  * Helper class used internally by camel-netty using Netty.  *  * @version   */
end_comment

begin_class
DECL|class|NettyHelper
specifier|public
specifier|final
class|class
name|NettyHelper
block|{
DECL|field|DEFAULT_IO_THREADS
specifier|public
specifier|static
specifier|final
name|int
name|DEFAULT_IO_THREADS
init|=
name|Runtime
operator|.
name|getRuntime
argument_list|()
operator|.
name|availableProcessors
argument_list|()
operator|*
literal|2
decl_stmt|;
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
comment|/**      * Gets the string body to be used when sending with the textline codec.      *      * @param body                 the current body      * @param exchange             the exchange      * @param delimiter            the textline delimiter      * @param autoAppendDelimiter  whether absent delimiter should be auto appended      * @return the string body to send      * @throws NoTypeConversionAvailableException is thrown if the current body could not be converted to a String type      */
DECL|method|getTextlineBody (Object body, Exchange exchange, TextLineDelimiter delimiter, boolean autoAppendDelimiter)
specifier|public
specifier|static
name|String
name|getTextlineBody
parameter_list|(
name|Object
name|body
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|TextLineDelimiter
name|delimiter
parameter_list|,
name|boolean
name|autoAppendDelimiter
parameter_list|)
throws|throws
name|NoTypeConversionAvailableException
block|{
name|String
name|s
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|mandatoryConvertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|body
argument_list|)
decl_stmt|;
comment|// auto append delimiter if missing?
if|if
condition|(
name|autoAppendDelimiter
condition|)
block|{
if|if
condition|(
name|TextLineDelimiter
operator|.
name|LINE
operator|.
name|equals
argument_list|(
name|delimiter
argument_list|)
condition|)
block|{
comment|// line delimiter so ensure it ends with newline
if|if
condition|(
operator|!
name|s
operator|.
name|endsWith
argument_list|(
literal|"\n"
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Auto appending missing newline delimiter to body"
argument_list|)
expr_stmt|;
name|s
operator|=
name|s
operator|+
literal|"\n"
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// null delimiter so ensure it ends with null
if|if
condition|(
operator|!
name|s
operator|.
name|endsWith
argument_list|(
literal|"\u0000"
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Auto appending missing null delimiter to body"
argument_list|)
expr_stmt|;
name|s
operator|=
name|s
operator|+
literal|"\u0000"
expr_stmt|;
block|}
block|}
block|}
return|return
name|s
return|;
block|}
comment|/**      * Writes the given body to Netty channel. Will<b>not</b>wait until the body has been written.      *      * @param log             logger to use      * @param channel         the Netty channel      * @param remoteAddress   the remote address when using UDP      * @param body            the body to write (send)      * @param exchange        the exchange      * @param listener        listener with work to be executed when the operation is complete      */
DECL|method|writeBodyAsync (Logger log, Channel channel, SocketAddress remoteAddress, Object body, Exchange exchange, ChannelFutureListener listener)
specifier|public
specifier|static
name|void
name|writeBodyAsync
parameter_list|(
name|Logger
name|log
parameter_list|,
name|Channel
name|channel
parameter_list|,
name|SocketAddress
name|remoteAddress
parameter_list|,
name|Object
name|body
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|ChannelFutureListener
name|listener
parameter_list|)
block|{
name|ChannelFuture
name|future
decl_stmt|;
if|if
condition|(
name|remoteAddress
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Channel: {} remote address: {} writing body: {}"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|channel
block|,
name|remoteAddress
block|,
name|body
block|}
argument_list|)
expr_stmt|;
block|}
name|future
operator|=
name|channel
operator|.
name|write
argument_list|(
name|body
argument_list|,
name|remoteAddress
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Channel: {} writing body: {}"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|channel
block|,
name|body
block|}
argument_list|)
expr_stmt|;
block|}
name|future
operator|=
name|channel
operator|.
name|write
argument_list|(
name|body
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|listener
operator|!=
literal|null
condition|)
block|{
name|future
operator|.
name|addListener
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Closes the given channel asynchronously      *      * @param channel the channel to close      */
DECL|method|close (Channel channel)
specifier|public
specifier|static
name|void
name|close
parameter_list|(
name|Channel
name|channel
parameter_list|)
block|{
if|if
condition|(
name|channel
operator|!=
literal|null
condition|)
block|{
name|channel
operator|.
name|close
argument_list|()
operator|.
name|addListener
argument_list|(
operator|new
name|ChannelFutureListener
argument_list|()
block|{
specifier|public
name|void
name|operationComplete
parameter_list|(
name|ChannelFuture
name|future
parameter_list|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Channel closed: {}"
argument_list|,
name|future
operator|.
name|getChannel
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

