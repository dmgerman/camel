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

begin_class
DECL|class|ExchangeAwareDelayCalcBean
specifier|public
class|class
name|ExchangeAwareDelayCalcBean
block|{
DECL|field|BEAN_DELAYER_HEADER
specifier|static
specifier|final
name|String
name|BEAN_DELAYER_HEADER
init|=
literal|"BEAN_DELAYER_HEADER"
decl_stmt|;
DECL|method|delayMe (Exchange exchange)
specifier|public
name|long
name|delayMe
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
operator|.
name|getHeader
argument_list|(
literal|"BEAN_DELAYER_HEADER"
argument_list|,
name|Long
operator|.
name|class
argument_list|)
return|;
block|}
block|}
end_class

end_unit

