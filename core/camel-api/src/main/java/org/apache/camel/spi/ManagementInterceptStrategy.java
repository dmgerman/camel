begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
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
name|NamedNode
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
name|Ordered
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

begin_interface
DECL|interface|ManagementInterceptStrategy
specifier|public
interface|interface
name|ManagementInterceptStrategy
block|{
DECL|method|createProcessor (NamedNode definition, Processor target)
name|InstrumentationProcessor
argument_list|<
name|?
argument_list|>
name|createProcessor
parameter_list|(
name|NamedNode
name|definition
parameter_list|,
name|Processor
name|target
parameter_list|)
function_decl|;
DECL|method|createProcessor (String type)
name|InstrumentationProcessor
argument_list|<
name|?
argument_list|>
name|createProcessor
parameter_list|(
name|String
name|type
parameter_list|)
function_decl|;
DECL|interface|InstrumentationProcessor
interface|interface
name|InstrumentationProcessor
parameter_list|<
name|T
parameter_list|>
extends|extends
name|AsyncProcessor
extends|,
name|Ordered
block|{
DECL|method|before (Exchange exchange)
name|T
name|before
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
function_decl|;
DECL|method|after (Exchange exchange, T data)
name|void
name|after
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|T
name|data
parameter_list|)
throws|throws
name|Exception
function_decl|;
DECL|method|setProcessor (Processor processor)
name|void
name|setProcessor
parameter_list|(
name|Processor
name|processor
parameter_list|)
function_decl|;
DECL|method|setCounter (Object object)
name|void
name|setCounter
parameter_list|(
name|Object
name|object
parameter_list|)
function_decl|;
block|}
block|}
end_interface

end_unit

