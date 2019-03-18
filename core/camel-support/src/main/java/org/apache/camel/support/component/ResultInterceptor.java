begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.support.component
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
operator|.
name|component
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

begin_comment
comment|/**  * Intercepts API method invocation result Exchange.  */
end_comment

begin_interface
DECL|interface|ResultInterceptor
specifier|public
interface|interface
name|ResultInterceptor
block|{
comment|/**      * Split a complex result into result elements.      * @param result API method invocation result      * @return either the same result if it cannot be split, an array or collection object with split results      */
DECL|method|splitResult (Object result)
name|Object
name|splitResult
parameter_list|(
name|Object
name|result
parameter_list|)
function_decl|;
comment|/**      * Do additional result exchange processing, for example, adding custom headers.      * @param result result of API method invocation.      * @param resultExchange result as a Camel exchange, may be a split result from Arrays or Collections.      */
DECL|method|interceptResult (Object result, Exchange resultExchange)
name|void
name|interceptResult
parameter_list|(
name|Object
name|result
parameter_list|,
name|Exchange
name|resultExchange
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

