begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
package|;
end_package

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|OtherExampleBean
specifier|public
class|class
name|OtherExampleBean
block|{
DECL|field|customerId
specifier|private
name|int
name|customerId
decl_stmt|;
DECL|field|goldCustomer
specifier|private
name|boolean
name|goldCustomer
decl_stmt|;
DECL|field|silverCustomer
specifier|private
name|Boolean
name|silverCustomer
decl_stmt|;
DECL|field|company
specifier|private
name|String
name|company
decl_stmt|;
DECL|method|getCustomerId ()
specifier|public
name|int
name|getCustomerId
parameter_list|()
block|{
return|return
name|customerId
return|;
block|}
DECL|method|setCustomerId (int customerId)
specifier|public
name|void
name|setCustomerId
parameter_list|(
name|int
name|customerId
parameter_list|)
block|{
name|this
operator|.
name|customerId
operator|=
name|customerId
expr_stmt|;
block|}
DECL|method|isGoldCustomer ()
specifier|public
name|boolean
name|isGoldCustomer
parameter_list|()
block|{
return|return
name|goldCustomer
return|;
block|}
DECL|method|setGoldCustomer (boolean goldCustomer)
specifier|public
name|void
name|setGoldCustomer
parameter_list|(
name|boolean
name|goldCustomer
parameter_list|)
block|{
name|this
operator|.
name|goldCustomer
operator|=
name|goldCustomer
expr_stmt|;
block|}
DECL|method|isSilverCustomer ()
specifier|public
name|Boolean
name|isSilverCustomer
parameter_list|()
block|{
return|return
name|silverCustomer
return|;
block|}
DECL|method|setSilverCustomer (Boolean silverCustomer)
specifier|public
name|void
name|setSilverCustomer
parameter_list|(
name|Boolean
name|silverCustomer
parameter_list|)
block|{
name|this
operator|.
name|silverCustomer
operator|=
name|silverCustomer
expr_stmt|;
block|}
DECL|method|getCompany ()
specifier|public
name|String
name|getCompany
parameter_list|()
block|{
return|return
name|company
return|;
block|}
DECL|method|setCompany (String company)
specifier|public
name|void
name|setCompany
parameter_list|(
name|String
name|company
parameter_list|)
block|{
name|this
operator|.
name|company
operator|=
name|company
expr_stmt|;
block|}
DECL|method|setupSomething (Object value)
specifier|public
name|void
name|setupSomething
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
comment|// noop
block|}
block|}
end_class

end_unit

