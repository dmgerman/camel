begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
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
comment|/**  * A Strategy pattern for handling exceptions; particularly in asynchronous processes such as consumers.  *<p/>  * Its important to<b>not</b> throw any exceptions when handling exceptions as they handler  * is often invoked in a try .. catch logic already  */
end_comment

begin_interface
DECL|interface|ExceptionHandler
specifier|public
interface|interface
name|ExceptionHandler
block|{
comment|/**      * Handles the given exception      *      * @param exception the exception      */
DECL|method|handleException (Throwable exception)
name|void
name|handleException
parameter_list|(
name|Throwable
name|exception
parameter_list|)
function_decl|;
comment|/**      * Handles the given exception      *      * @param message additional message      * @param exception the exception      */
DECL|method|handleException (String message, Throwable exception)
name|void
name|handleException
parameter_list|(
name|String
name|message
parameter_list|,
name|Throwable
name|exception
parameter_list|)
function_decl|;
comment|/**      * Handles the given exception      *      * @param message additional message      * @param exchange exchange which cause the exception      * @param exception the exception      */
DECL|method|handleException (String message, Exchange exchange, Throwable exception)
name|void
name|handleException
parameter_list|(
name|String
name|message
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Throwable
name|exception
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

