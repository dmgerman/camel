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
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/**  * A sub unit of work is a way of implement sub-transactions in Camel routing.  * This is needed by some EIPs where you can have sub routes such as the Splitter.  * The Camel end user may want to indicate that the Splitter should act as a  *<b>single combined</b> unit of work.  *  * @see SubUnitOfWorkCallback  */
end_comment

begin_interface
DECL|interface|SubUnitOfWork
specifier|public
interface|interface
name|SubUnitOfWork
block|{
comment|/**      * Is the {@link SubUnitOfWork} marked as failed.      *      * @return<tt>true</tt> to indicate this sub unit of work is failed.      */
DECL|method|isFailed ()
name|boolean
name|isFailed
parameter_list|()
function_decl|;
comment|/**      * If failed then a number of exceptions could have occurred, causing the {@link SubUnitOfWork} to fail.      *      * @return the caused exceptions.      */
DECL|method|getExceptions ()
name|List
argument_list|<
name|Exception
argument_list|>
name|getExceptions
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

