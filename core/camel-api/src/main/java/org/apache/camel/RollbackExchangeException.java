begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
package|;
end_package

begin_comment
comment|/**  * Exception used for forcing an Exchange to be rolled back.  */
end_comment

begin_class
DECL|class|RollbackExchangeException
specifier|public
class|class
name|RollbackExchangeException
extends|extends
name|CamelExchangeException
block|{
DECL|method|RollbackExchangeException (Exchange exchange)
specifier|public
name|RollbackExchangeException
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|this
argument_list|(
literal|"Intended rollback"
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
DECL|method|RollbackExchangeException (Exchange exchange, Throwable cause)
specifier|public
name|RollbackExchangeException
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Throwable
name|cause
parameter_list|)
block|{
name|this
argument_list|(
literal|"Intended rollback"
argument_list|,
name|exchange
argument_list|,
name|cause
argument_list|)
expr_stmt|;
block|}
DECL|method|RollbackExchangeException (String message, Exchange exchange)
specifier|public
name|RollbackExchangeException
parameter_list|(
name|String
name|message
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
DECL|method|RollbackExchangeException (String message, Exchange exchange, Throwable cause)
specifier|public
name|RollbackExchangeException
parameter_list|(
name|String
name|message
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Throwable
name|cause
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|,
name|exchange
argument_list|,
name|cause
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

