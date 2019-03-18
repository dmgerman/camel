begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.reactive.streams.api
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|reactive
operator|.
name|streams
operator|.
name|api
package|;
end_package

begin_comment
comment|/**  * A callback used to signal when a item coming from a Camel route has been delivered to the external stream processor.  */
end_comment

begin_interface
annotation|@
name|FunctionalInterface
DECL|interface|DispatchCallback
specifier|public
interface|interface
name|DispatchCallback
parameter_list|<
name|T
parameter_list|>
block|{
comment|/**      * Signals the delivery of the item.      * If the item cannot be delivered (no subscribers registered, conversion error)      * the related {@link Throwable} is specified as parameter.      *      * @param data the item      * @param error the error occurred, if any      */
DECL|method|processed (T data, Throwable error)
name|void
name|processed
parameter_list|(
name|T
name|data
parameter_list|,
name|Throwable
name|error
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

