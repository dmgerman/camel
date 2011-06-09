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
comment|/**  * To allow unit of work for the {@link UnitOfWork} while processing a number of {@link Exchange}s.  *<p/>  * A sub unit of work is a way of implement sub-transactions in Camel routing.  * This is needed by some EIPs where you can have sub routes such as the Splitter.  * The Camel end user may want to indicate that the Splitter should act as a  *<b>single combined</b> unit of work.  *<p/>  * To implement this, we use this {@link SubUnitOfWorkCallback}  * which allows us to have the sub routes participate in a {@link SubUnitOfWork}  * And then the outcome of the {@link SubUnitOfWork} will be a single atomic commit or rollback.  *<p/>  * When using a {@link SubUnitOfWork} we need to tap into the sub routes, and ensure they callback with the progress  * of the sub {@link Exchange} being processed. For example the error handler, we need to tap into, and  * ensure that any exhausted sub {@link Exchange} is propagated into the result of the {@link SubUnitOfWork}.  * This {@link SubUnitOfWorkCallback} allows us to do that.  *  * @see SubUnitOfWork  */
end_comment

begin_interface
DECL|interface|SubUnitOfWorkCallback
specifier|public
interface|interface
name|SubUnitOfWorkCallback
block|{
comment|/**      * The exchange is exhausted, by a redeliverable error handler.      *      * @param exchange the exchange      */
DECL|method|onExhausted (Exchange exchange)
name|void
name|onExhausted
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
function_decl|;
comment|/**      * The exchange is done.      *      * @param exchange the exchange.      */
DECL|method|onDone (Exchange exchange)
name|void
name|onDone
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

