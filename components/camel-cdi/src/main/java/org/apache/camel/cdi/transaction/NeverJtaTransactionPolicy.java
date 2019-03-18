begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|javax
operator|.
name|inject
operator|.
name|Named
import|;
end_import

begin_class
annotation|@
name|Named
argument_list|(
literal|"PROPAGATION_NEVER"
argument_list|)
DECL|class|NeverJtaTransactionPolicy
specifier|public
class|class
name|NeverJtaTransactionPolicy
extends|extends
name|TransactionalJtaTransactionPolicy
block|{
annotation|@
name|Override
DECL|method|run (final Runnable runnable)
specifier|public
name|void
name|run
parameter_list|(
specifier|final
name|Runnable
name|runnable
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|hasActiveTransaction
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Policy 'PROPAGATION_NEVER' is configured but an active transaction was found!"
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

