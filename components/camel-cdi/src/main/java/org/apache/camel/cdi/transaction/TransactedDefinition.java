begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.cdi.transaction
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|cdi
operator|.
name|transaction
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
name|spi
operator|.
name|Policy
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
name|RouteContext
import|;
end_import

begin_comment
comment|/**  * Used to expose the method&apos;resolvePolicy&apos; used by  * {@link JtaTransactionErrorHandlerBuilder} to resolve configured policy  * references.  */
end_comment

begin_class
DECL|class|TransactedDefinition
specifier|public
class|class
name|TransactedDefinition
extends|extends
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|TransactedDefinition
block|{
annotation|@
name|Override
DECL|method|resolvePolicy (RouteContext routeContext)
specifier|public
name|Policy
name|resolvePolicy
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
block|{
return|return
name|super
operator|.
name|resolvePolicy
argument_list|(
name|routeContext
argument_list|)
return|;
block|}
block|}
end_class

end_unit

