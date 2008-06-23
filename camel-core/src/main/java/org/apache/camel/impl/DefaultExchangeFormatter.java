begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
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
name|processor
operator|.
name|interceptor
operator|.
name|ExchangeFormatter
import|;
end_import

begin_comment
comment|/**  * A default {@link ExchangeFormatter} which just uses the {@link org.apache.camel.Exchange#toString()} method  *  * @version $Revision: 1.1 $  */
end_comment

begin_class
DECL|class|DefaultExchangeFormatter
specifier|public
class|class
name|DefaultExchangeFormatter
implements|implements
name|ExchangeFormatter
block|{
DECL|field|INSTANCE
specifier|protected
specifier|static
specifier|final
name|DefaultExchangeFormatter
name|INSTANCE
init|=
operator|new
name|DefaultExchangeFormatter
argument_list|()
decl_stmt|;
DECL|method|getInstance ()
specifier|public
specifier|static
name|DefaultExchangeFormatter
name|getInstance
parameter_list|()
block|{
return|return
name|INSTANCE
return|;
block|}
DECL|method|format (Exchange exchange)
specifier|public
name|Object
name|format
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|exchange
return|;
block|}
block|}
end_class

end_unit

