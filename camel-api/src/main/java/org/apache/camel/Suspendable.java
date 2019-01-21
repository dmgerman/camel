begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
operator|.
name|service
operator|.
name|ServiceSupport
import|;
end_import

begin_comment
comment|/**  * Marker interface to indicate a custom component has custom implementation for suspending the {@link SuspendableService} service.  *<br/>  * This is needed to let Camel know if there is special code happening during a suspension.  *<p/>  * The {@link ServiceSupport} implementation that most Camel components / endpoints etc use  * as base class is a {@link SuspendableService} but the actual implementation may not have special logic for suspend.  * Therefore this marker interface is introduced to indicate when the implementation has special code for suspension.  *<p/>  * It is assumed that a service having a custom logic for suspension implements also a custom logic for resuming.  *  * @see SuspendableService  */
end_comment

begin_interface
DECL|interface|Suspendable
specifier|public
interface|interface
name|Suspendable
block|{ }
end_interface

end_unit

