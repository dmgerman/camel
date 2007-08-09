begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
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
name|impl
operator|.
name|ServiceSupport
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|IdentityHashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_comment
comment|/**  * @version $Revision: 1.1 $  */
end_comment

begin_class
DECL|class|ErrorHandlerSupport
specifier|public
specifier|abstract
class|class
name|ErrorHandlerSupport
extends|extends
name|ServiceSupport
implements|implements
name|ErrorHandler
block|{
DECL|field|exceptionHandlers
specifier|private
name|Map
argument_list|<
name|Class
argument_list|,
name|Processor
argument_list|>
name|exceptionHandlers
init|=
operator|new
name|IdentityHashMap
argument_list|<
name|Class
argument_list|,
name|Processor
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|addCustomProcessor (Class exceptionType, Processor processor)
specifier|public
name|void
name|addCustomProcessor
parameter_list|(
name|Class
name|exceptionType
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
name|addChildService
argument_list|(
name|processor
argument_list|)
expr_stmt|;
name|exceptionHandlers
operator|.
name|put
argument_list|(
name|exceptionType
argument_list|,
name|processor
argument_list|)
expr_stmt|;
block|}
comment|/**      * Attempts to invoke the handler for this particular exception if one is available      * @param exchange      * @param exception      * @return      */
DECL|method|customProcessorForException (Exchange exchange, Throwable exception)
specifier|protected
name|boolean
name|customProcessorForException
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Throwable
name|exception
parameter_list|)
throws|throws
name|Exception
block|{
name|Processor
name|processor
init|=
name|getProcessorForException
argument_list|(
name|exchange
argument_list|,
name|exception
argument_list|)
decl_stmt|;
if|if
condition|(
name|processor
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|exception
argument_list|)
expr_stmt|;
name|processor
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
DECL|method|getProcessorForException (Exchange exchange, Throwable exception)
specifier|protected
name|Processor
name|getProcessorForException
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Throwable
name|exception
parameter_list|)
block|{
name|Set
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|Class
argument_list|,
name|Processor
argument_list|>
argument_list|>
name|entries
init|=
name|exceptionHandlers
operator|.
name|entrySet
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|Class
argument_list|,
name|Processor
argument_list|>
name|entry
range|:
name|entries
control|)
block|{
name|Class
name|type
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
if|if
condition|(
name|type
operator|.
name|isInstance
argument_list|(
name|exception
argument_list|)
condition|)
block|{
return|return
name|entry
operator|.
name|getValue
argument_list|()
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

