begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Callable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ExecutorService
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Future
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

begin_comment
comment|/**  * Helper methods for AsyncProcessor objects.  */
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
comment|/**      * Processes the exchange async.      *      * @param executor  executor service      * @param processor the processor      * @param exchange  the exchange      * @return a future handle for the task being executed asynchronously      */
DECL|method|asyncProcess (final ExecutorService executor, final Processor processor, final Exchange exchange)
specifier|public
specifier|static
name|Future
argument_list|<
name|Exchange
argument_list|>
name|asyncProcess
parameter_list|(
specifier|final
name|ExecutorService
name|executor
parameter_list|,
specifier|final
name|Processor
name|processor
parameter_list|,
specifier|final
name|Exchange
name|exchange
parameter_list|)
block|{
name|Callable
argument_list|<
name|Exchange
argument_list|>
name|task
init|=
operator|new
name|Callable
argument_list|<
name|Exchange
argument_list|>
argument_list|()
block|{
specifier|public
name|Exchange
name|call
parameter_list|()
throws|throws
name|Exception
block|{
name|processor
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
block|}
decl_stmt|;
return|return
name|executor
operator|.
name|submit
argument_list|(
name|task
argument_list|)
return|;
block|}
block|}
end_class

end_unit

