begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Message
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
comment|/**  * The processor which implements the ThrowFault DSL  */
end_comment

begin_class
DECL|class|ThrowFaultProcessor
specifier|public
class|class
name|ThrowFaultProcessor
implements|implements
name|Processor
block|{
DECL|field|fault
specifier|private
name|Throwable
name|fault
decl_stmt|;
DECL|method|ThrowFaultProcessor (Throwable fault)
specifier|public
name|ThrowFaultProcessor
parameter_list|(
name|Throwable
name|fault
parameter_list|)
block|{
name|this
operator|.
name|fault
operator|=
name|fault
expr_stmt|;
block|}
comment|/**      * Set the fault message in the exchange      * @see org.apache.camel.Processor#process(org.apache.camel.Exchange)      */
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
name|Message
name|message
init|=
name|exchange
operator|.
name|getFault
argument_list|()
decl_stmt|;
name|message
operator|.
name|setBody
argument_list|(
name|fault
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

