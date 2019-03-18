begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.log
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|log
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
name|spi
operator|.
name|ExchangeFormatter
import|;
end_import

begin_comment
comment|/**  * A test exchange formatter.   */
end_comment

begin_class
DECL|class|TestExchangeFormatter
specifier|public
class|class
name|TestExchangeFormatter
implements|implements
name|ExchangeFormatter
block|{
DECL|field|testProperty
specifier|private
name|String
name|testProperty
decl_stmt|;
annotation|@
name|Override
DECL|method|format (Exchange exchange)
specifier|public
name|String
name|format
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|exchange
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|getTestProperty ()
specifier|public
name|String
name|getTestProperty
parameter_list|()
block|{
return|return
name|testProperty
return|;
block|}
DECL|method|setTestProperty (String testProperty)
specifier|public
name|void
name|setTestProperty
parameter_list|(
name|String
name|testProperty
parameter_list|)
block|{
name|this
operator|.
name|testProperty
operator|=
name|testProperty
expr_stmt|;
block|}
block|}
end_class

end_unit

