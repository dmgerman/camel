begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.support
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
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
name|AsyncProcessor
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
name|spi
operator|.
name|AsyncProcessorAwaitManager
import|;
end_import

begin_comment
comment|/**  * Helper methods for {@link AsyncProcessor} objects.  */
end_comment

begin_class
DECL|class|AsyncProcessorHelper
specifier|public
specifier|final
class|class
name|AsyncProcessorHelper
block|{
DECL|method|AsyncProcessorHelper ()
specifier|private
name|AsyncProcessorHelper
parameter_list|()
block|{
comment|// utility class
block|}
comment|/**      * Calls the async version of the processor's process method and waits      * for it to complete before returning. This can be used by {@link AsyncProcessor}      * objects to implement their sync version of the process method.      *<p/>      *<b>Important:</b> This method is discouraged to be used, as its better to invoke the asynchronous      * {@link AsyncProcessor#process(org.apache.camel.Exchange, org.apache.camel.AsyncCallback)} method, whenever possible.      *      * @param processor the processor      * @param exchange  the exchange      * @throws Exception can be thrown if waiting is interrupted      */
DECL|method|process (final AsyncProcessor processor, final Exchange exchange)
specifier|public
specifier|static
name|void
name|process
parameter_list|(
specifier|final
name|AsyncProcessor
name|processor
parameter_list|,
specifier|final
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
specifier|final
name|AsyncProcessorAwaitManager
name|awaitManager
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getAsyncProcessorAwaitManager
argument_list|()
decl_stmt|;
name|awaitManager
operator|.
name|process
argument_list|(
name|processor
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

