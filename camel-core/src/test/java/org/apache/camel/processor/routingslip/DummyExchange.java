begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.routingslip
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|routingslip
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
name|CamelContext
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
name|ExchangePattern
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
name|DefaultExchange
import|;
end_import

begin_class
DECL|class|DummyExchange
specifier|public
class|class
name|DummyExchange
extends|extends
name|DefaultExchange
block|{
DECL|method|DummyExchange (CamelContext context, ExchangePattern pattern)
specifier|public
name|DummyExchange
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|ExchangePattern
name|pattern
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|,
name|pattern
argument_list|)
expr_stmt|;
block|}
DECL|method|DummyExchange (DummyExchange dummyExchange)
specifier|public
name|DummyExchange
parameter_list|(
name|DummyExchange
name|dummyExchange
parameter_list|)
block|{
name|super
argument_list|(
name|dummyExchange
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|newInstance ()
specifier|public
name|Exchange
name|newInstance
parameter_list|()
block|{
return|return
operator|new
name|DummyExchange
argument_list|(
name|this
argument_list|)
return|;
block|}
block|}
end_class

end_unit

