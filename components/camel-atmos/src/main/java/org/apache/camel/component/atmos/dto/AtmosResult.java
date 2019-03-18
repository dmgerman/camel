begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.atmos.dto
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|atmos
operator|.
name|dto
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
DECL|class|AtmosResult
specifier|public
specifier|abstract
class|class
name|AtmosResult
block|{
DECL|field|resultEntries
specifier|protected
name|Object
name|resultEntries
decl_stmt|;
comment|/**      * Populate the camel exchange with the results from atmos method invocations.      * @param exchange      */
DECL|method|populateExchange (Exchange exchange)
specifier|public
specifier|abstract
name|void
name|populateExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
function_decl|;
DECL|method|getResultEntries ()
specifier|public
name|Object
name|getResultEntries
parameter_list|()
block|{
return|return
name|resultEntries
return|;
block|}
DECL|method|setResultEntries (Object resultEntries)
specifier|public
name|void
name|setResultEntries
parameter_list|(
name|Object
name|resultEntries
parameter_list|)
block|{
name|this
operator|.
name|resultEntries
operator|=
name|resultEntries
expr_stmt|;
block|}
block|}
end_class

end_unit

