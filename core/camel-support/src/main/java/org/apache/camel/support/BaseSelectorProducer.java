begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_comment
comment|/**  * A base class for selector-based producers.  */
end_comment

begin_class
DECL|class|BaseSelectorProducer
specifier|public
specifier|abstract
class|class
name|BaseSelectorProducer
extends|extends
name|DefaultProducer
block|{
DECL|method|BaseSelectorProducer (Endpoint endpoint)
specifier|protected
name|BaseSelectorProducer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
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
specifier|final
name|Processor
name|processor
init|=
name|getProcessor
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|processor
operator|!=
literal|null
condition|)
block|{
name|processor
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|onMissingProcessor
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Determine the processor to use to handle the exchange.      *      * @param exchange the message exchange      * @return the processor to processes the message exchange      */
DECL|method|getProcessor (Exchange exchange)
specifier|protected
specifier|abstract
name|Processor
name|getProcessor
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Invoked when no processor has been defined to process the message exchange.      *      * @param exchange the message exchange      */
DECL|method|onMissingProcessor (Exchange exchange)
specifier|protected
specifier|abstract
name|void
name|onMissingProcessor
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_class

end_unit

