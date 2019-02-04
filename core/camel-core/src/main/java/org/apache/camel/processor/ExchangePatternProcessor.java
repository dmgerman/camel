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
name|AsyncCallback
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
name|spi
operator|.
name|IdAware
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
name|support
operator|.
name|AsyncProcessorSupport
import|;
end_import

begin_comment
comment|/**  * Processor to set {@link org.apache.camel.ExchangePattern} on the {@link org.apache.camel.Exchange}.  */
end_comment

begin_class
DECL|class|ExchangePatternProcessor
specifier|public
class|class
name|ExchangePatternProcessor
extends|extends
name|AsyncProcessorSupport
implements|implements
name|IdAware
block|{
DECL|field|id
specifier|private
name|String
name|id
decl_stmt|;
DECL|field|exchangePattern
specifier|private
name|ExchangePattern
name|exchangePattern
init|=
name|ExchangePattern
operator|.
name|InOnly
decl_stmt|;
DECL|method|ExchangePatternProcessor ()
specifier|public
name|ExchangePatternProcessor
parameter_list|()
block|{     }
DECL|method|ExchangePatternProcessor (ExchangePattern ep)
specifier|public
name|ExchangePatternProcessor
parameter_list|(
name|ExchangePattern
name|ep
parameter_list|)
block|{
name|setExchangePattern
argument_list|(
name|ep
argument_list|)
expr_stmt|;
block|}
DECL|method|setExchangePattern (ExchangePattern ep)
specifier|public
name|void
name|setExchangePattern
parameter_list|(
name|ExchangePattern
name|ep
parameter_list|)
block|{
name|exchangePattern
operator|=
name|ep
expr_stmt|;
block|}
DECL|method|getId ()
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
DECL|method|setId (String id)
specifier|public
name|void
name|setId
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
DECL|method|getExchangePattern ()
specifier|public
name|ExchangePattern
name|getExchangePattern
parameter_list|()
block|{
return|return
name|exchangePattern
return|;
block|}
DECL|method|process (Exchange exchange, AsyncCallback callback)
specifier|public
name|boolean
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
block|{
name|exchange
operator|.
name|setPattern
argument_list|(
name|exchangePattern
argument_list|)
expr_stmt|;
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"SetExchangePattern["
operator|+
name|exchangePattern
operator|+
literal|"]"
return|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
block|}
block|}
end_class

end_unit

