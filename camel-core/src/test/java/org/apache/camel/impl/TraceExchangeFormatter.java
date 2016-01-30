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
name|DefaultExchangeFormatter
import|;
end_import

begin_class
DECL|class|TraceExchangeFormatter
specifier|public
class|class
name|TraceExchangeFormatter
extends|extends
name|DefaultExchangeFormatter
block|{
DECL|field|message
specifier|private
name|String
name|message
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
name|message
operator|=
name|super
operator|.
name|format
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
return|return
name|message
return|;
block|}
DECL|method|getMessage ()
specifier|public
name|String
name|getMessage
parameter_list|()
block|{
return|return
name|message
return|;
block|}
block|}
end_class

end_unit

