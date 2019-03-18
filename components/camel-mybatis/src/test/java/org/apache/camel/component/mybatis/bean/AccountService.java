begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mybatis.bean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mybatis
operator|.
name|bean
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
name|mybatis
operator|.
name|Account
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ibatis
operator|.
name|annotations
operator|.
name|Insert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ibatis
operator|.
name|annotations
operator|.
name|Param
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ibatis
operator|.
name|annotations
operator|.
name|ResultMap
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ibatis
operator|.
name|annotations
operator|.
name|Select
import|;
end_import

begin_interface
DECL|interface|AccountService
specifier|public
interface|interface
name|AccountService
block|{
annotation|@
name|Select
argument_list|(
literal|"select ACC_ID as id, ACC_FIRST_NAME as firstName, ACC_LAST_NAME as lastName"
operator|+
literal|", ACC_EMAIL as emailAddress from ACCOUNT where ACC_ID = #{id}"
argument_list|)
DECL|method|selectBeanAccountById (@aramR) int no)
name|Account
name|selectBeanAccountById
parameter_list|(
annotation|@
name|Param
argument_list|(
literal|"id"
argument_list|)
name|int
name|no
parameter_list|)
function_decl|;
annotation|@
name|Select
argument_list|(
literal|"select * from ACCOUNT order by ACC_ID"
argument_list|)
annotation|@
name|ResultMap
argument_list|(
literal|"Account.AccountResult"
argument_list|)
DECL|method|selectBeanAllAccounts ()
name|List
argument_list|<
name|Account
argument_list|>
name|selectBeanAllAccounts
parameter_list|()
function_decl|;
annotation|@
name|Insert
argument_list|(
literal|"insert into ACCOUNT (ACC_ID,ACC_FIRST_NAME,ACC_LAST_NAME,ACC_EMAIL)"
operator|+
literal|" values (#{id}, #{firstName}, #{lastName}, #{emailAddress})"
argument_list|)
DECL|method|insertBeanAccount (Account account)
name|void
name|insertBeanAccount
parameter_list|(
name|Account
name|account
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

