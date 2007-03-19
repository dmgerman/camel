begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|DefaultEndpointResolver
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
name|DefaultExchangeConverter
import|;
end_import

begin_comment
comment|/**  * Represents the container used to configure routes and the policies to use.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|CamelContainer
specifier|public
class|class
name|CamelContainer
parameter_list|<
name|E
parameter_list|>
block|{
DECL|field|endpointResolver
specifier|private
name|EndpointResolver
argument_list|<
name|E
argument_list|>
name|endpointResolver
decl_stmt|;
DECL|field|exchangeConverter
specifier|private
name|ExchangeConverter
name|exchangeConverter
decl_stmt|;
DECL|method|getEndpointResolver ()
specifier|public
name|EndpointResolver
argument_list|<
name|E
argument_list|>
name|getEndpointResolver
parameter_list|()
block|{
if|if
condition|(
name|endpointResolver
operator|==
literal|null
condition|)
block|{
name|endpointResolver
operator|=
name|createEndpointResolver
argument_list|()
expr_stmt|;
block|}
return|return
name|endpointResolver
return|;
block|}
DECL|method|setEndpointResolver (EndpointResolver<E> endpointResolver)
specifier|public
name|void
name|setEndpointResolver
parameter_list|(
name|EndpointResolver
argument_list|<
name|E
argument_list|>
name|endpointResolver
parameter_list|)
block|{
name|this
operator|.
name|endpointResolver
operator|=
name|endpointResolver
expr_stmt|;
block|}
DECL|method|getExchangeConverter ()
specifier|public
name|ExchangeConverter
name|getExchangeConverter
parameter_list|()
block|{
if|if
condition|(
name|exchangeConverter
operator|==
literal|null
condition|)
block|{
name|exchangeConverter
operator|=
name|createExchangeConverter
argument_list|()
expr_stmt|;
block|}
return|return
name|exchangeConverter
return|;
block|}
DECL|method|setExchangeConverter (ExchangeConverter exchangeConverter)
specifier|public
name|void
name|setExchangeConverter
parameter_list|(
name|ExchangeConverter
name|exchangeConverter
parameter_list|)
block|{
name|this
operator|.
name|exchangeConverter
operator|=
name|exchangeConverter
expr_stmt|;
block|}
comment|// Implementation methods
comment|//-----------------------------------------------------------------------
DECL|method|createEndpointResolver ()
specifier|protected
name|EndpointResolver
argument_list|<
name|E
argument_list|>
name|createEndpointResolver
parameter_list|()
block|{
return|return
operator|new
name|DefaultEndpointResolver
argument_list|<
name|E
argument_list|>
argument_list|(
name|this
argument_list|)
return|;
block|}
comment|/**      * Lazily create a default exchange converter implementation      */
DECL|method|createExchangeConverter ()
specifier|protected
name|ExchangeConverter
name|createExchangeConverter
parameter_list|()
block|{
return|return
operator|new
name|DefaultExchangeConverter
argument_list|()
return|;
block|}
block|}
end_class

end_unit

