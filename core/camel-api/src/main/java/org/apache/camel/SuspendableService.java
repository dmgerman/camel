begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
package|;
end_package

begin_comment
comment|/**  * A {@link Service} which is also capable of suspending and resuming.  *<p/>  * This is useable for services which needs more fine grained control at runtime supporting suspension.  * Other services may select to mimic suspending by just stopping the service.  *<p/>  * For example this is use by the JmsConsumer which suspends the Spring JMS listener instead of stopping  * the consumer totally.  *<p/>  *<b>Important:</b> The service should also implement the {@link Suspendable} marker interface to indicate  * the service supports suspension using custom code logic.  *  * @see Suspendable  */
end_comment

begin_interface
DECL|interface|SuspendableService
specifier|public
interface|interface
name|SuspendableService
extends|extends
name|Service
block|{
comment|/**      * Suspends the service.      *      * @throws Exception is thrown if suspending failed      */
DECL|method|suspend ()
name|void
name|suspend
parameter_list|()
throws|throws
name|Exception
function_decl|;
comment|/**      * Resumes the service.      *      * @throws Exception is thrown if resuming failed      */
DECL|method|resume ()
name|void
name|resume
parameter_list|()
throws|throws
name|Exception
function_decl|;
comment|/**      * Tests whether the service is suspended or not.      *      * @return<tt>true</tt> if suspended      */
DECL|method|isSuspended ()
name|boolean
name|isSuspended
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

