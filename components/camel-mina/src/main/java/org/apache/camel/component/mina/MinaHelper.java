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
name|mina
operator|.
name|common
operator|.
name|IoSession
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|mina
operator|.
name|common
operator|.
name|WriteFuture
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
name|CamelExchangeException
import|;
end_import

begin_comment
comment|/**  * Helper class used internally by camel-mina using Apache MINA.  */
end_comment

begin_class
DECL|class|MinaHelper
specifier|public
class|class
name|MinaHelper
block|{
DECL|method|MinaHelper ()
specifier|private
name|MinaHelper
parameter_list|()
block|{}
comment|/**      * Writes the given body to MINA session. Will wait until the body has been written.      *       * @param session   the MINA session      * @param body      the body to write (send)      * @param exchange  the mina exchange used for error reporting      * @throws CamelExchangeException is thrown if the body could not be written for some reasons      *                                (eg remote connection is closed etc.)      */
DECL|method|writeBody (IoSession session, Object body, Exchange exchange)
specifier|public
specifier|static
name|void
name|writeBody
parameter_list|(
name|IoSession
name|session
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
comment|// the write operation is asynchronous. Use WriteFuture to wait until the session has been written
name|WriteFuture
name|future
init|=
name|session
operator|.
name|write
argument_list|(
name|body
argument_list|)
decl_stmt|;
name|future
operator|.
name|join
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|future
operator|.
name|isWritten
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|CamelExchangeException
argument_list|(
literal|"Could not write body"
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

