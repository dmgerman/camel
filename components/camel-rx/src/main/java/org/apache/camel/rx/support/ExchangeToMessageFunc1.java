begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.rx.support
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|rx
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
name|rx
operator|.
name|util
operator|.
name|functions
operator|.
name|Func1
import|;
end_import

begin_comment
comment|/**  * A simple {@link Func1} to convert an {@link Exchange} to its IN {@link Message}  */
end_comment

begin_class
DECL|class|ExchangeToMessageFunc1
specifier|public
class|class
name|ExchangeToMessageFunc1
implements|implements
name|Func1
argument_list|<
name|Exchange
argument_list|,
name|Message
argument_list|>
block|{
DECL|field|instance
specifier|private
specifier|static
name|ExchangeToMessageFunc1
name|instance
init|=
operator|new
name|ExchangeToMessageFunc1
argument_list|()
decl_stmt|;
DECL|method|getInstance ()
specifier|public
specifier|static
name|ExchangeToMessageFunc1
name|getInstance
parameter_list|()
block|{
return|return
name|instance
return|;
block|}
annotation|@
name|Override
DECL|method|call (Exchange exchange)
specifier|public
name|Message
name|call
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|exchange
operator|.
name|getIn
argument_list|()
return|;
block|}
block|}
end_class

end_unit

