begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sjms.tx
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|sjms
operator|.
name|tx
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|sjms
operator|.
name|TransactionCommitStrategy
import|;
end_import

begin_comment
comment|/**  * The default commit strategy for all transaction.  *   */
end_comment

begin_class
DECL|class|DefaultTransactionCommitStrategy
specifier|public
class|class
name|DefaultTransactionCommitStrategy
implements|implements
name|TransactionCommitStrategy
block|{
comment|/**      * @see org.apache.camel.component.sjms.TransactionCommitStrategy#commit(org.apache.camel.Exchange)      *      * @param exchange      * @return      * @throws Exception      */
annotation|@
name|Override
DECL|method|commit (Exchange exchange)
specifier|public
name|boolean
name|commit
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
return|return
literal|true
return|;
block|}
comment|/**      * @see org.apache.camel.component.sjms.TransactionCommitStrategy#rollback(org.apache.camel.Exchange)      *      * @param exchange      * @return      * @throws Exception      */
annotation|@
name|Override
DECL|method|rollback (Exchange exchange)
specifier|public
name|boolean
name|rollback
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

