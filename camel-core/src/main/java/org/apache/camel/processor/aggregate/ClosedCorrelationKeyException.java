begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.aggregate
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|aggregate
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

begin_comment
comment|/**  * The correlation key has been closed and the Exchange cannot be aggregated.  *  * @version   */
end_comment

begin_class
DECL|class|ClosedCorrelationKeyException
specifier|public
class|class
name|ClosedCorrelationKeyException
extends|extends
name|CamelExchangeException
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
DECL|field|correlationKey
specifier|private
specifier|final
name|String
name|correlationKey
decl_stmt|;
DECL|method|ClosedCorrelationKeyException (String correlationKey, Exchange exchange)
specifier|public
name|ClosedCorrelationKeyException
parameter_list|(
name|String
name|correlationKey
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|super
argument_list|(
literal|"The correlation key ["
operator|+
name|correlationKey
operator|+
literal|"] has been closed"
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|this
operator|.
name|correlationKey
operator|=
name|correlationKey
expr_stmt|;
block|}
DECL|method|ClosedCorrelationKeyException (String correlationKey, Exchange exchange, Throwable cause)
specifier|public
name|ClosedCorrelationKeyException
parameter_list|(
name|String
name|correlationKey
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
literal|"The correlation key ["
operator|+
name|correlationKey
operator|+
literal|"] has been closed"
argument_list|,
name|exchange
argument_list|,
name|cause
argument_list|)
expr_stmt|;
name|this
operator|.
name|correlationKey
operator|=
name|correlationKey
expr_stmt|;
block|}
DECL|method|getCorrelationKey ()
specifier|public
name|String
name|getCorrelationKey
parameter_list|()
block|{
return|return
name|correlationKey
return|;
block|}
block|}
end_class

end_unit

