begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jpa
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jpa
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
name|examples
operator|.
name|Address
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
name|examples
operator|.
name|Customer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|transaction
operator|.
name|TransactionStatus
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|transaction
operator|.
name|support
operator|.
name|TransactionCallback
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|JpaUseMergeTest
specifier|public
class|class
name|JpaUseMergeTest
extends|extends
name|AbstractJpaMethodTest
block|{
DECL|method|usePersist ()
specifier|public
name|boolean
name|usePersist
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Test
DECL|method|produceExistingEntity ()
specifier|public
name|void
name|produceExistingEntity
parameter_list|()
throws|throws
name|Exception
block|{
name|setUp
argument_list|(
literal|"jpa://"
operator|+
name|Customer
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|"?usePersist=false"
argument_list|)
expr_stmt|;
specifier|final
name|Customer
name|customer
init|=
name|createDefaultCustomer
argument_list|()
decl_stmt|;
name|transactionTemplate
operator|.
name|execute
argument_list|(
operator|new
name|TransactionCallback
argument_list|<
name|Object
argument_list|>
argument_list|()
block|{
specifier|public
name|Object
name|doInTransaction
parameter_list|(
name|TransactionStatus
name|status
parameter_list|)
block|{
name|entityManager
operator|.
name|joinTransaction
argument_list|()
expr_stmt|;
name|entityManager
operator|.
name|persist
argument_list|(
name|customer
argument_list|)
expr_stmt|;
name|entityManager
operator|.
name|flush
argument_list|()
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|assertEntitiesInDatabase
argument_list|(
literal|1
argument_list|,
name|Customer
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEntitiesInDatabase
argument_list|(
literal|1
argument_list|,
name|Address
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|// do detach the persisted entity first before modifying it as we intend to merge it later on below
name|entityManager
operator|.
name|detach
argument_list|(
name|customer
argument_list|)
expr_stmt|;
name|customer
operator|.
name|setName
argument_list|(
literal|"Max Mustermann"
argument_list|)
expr_stmt|;
name|customer
operator|.
name|getAddress
argument_list|()
operator|.
name|setAddressLine1
argument_list|(
literal|"Musterstr. 1"
argument_list|)
expr_stmt|;
name|customer
operator|.
name|getAddress
argument_list|()
operator|.
name|setAddressLine2
argument_list|(
literal|"11111 Enterhausen"
argument_list|)
expr_stmt|;
name|Customer
name|receivedCustomer
init|=
name|template
operator|.
name|requestBody
argument_list|(
name|endpoint
argument_list|,
name|customer
argument_list|,
name|Customer
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|customer
operator|.
name|getName
argument_list|()
argument_list|,
name|receivedCustomer
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|receivedCustomer
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|customer
operator|.
name|getAddress
argument_list|()
operator|.
name|getAddressLine1
argument_list|()
argument_list|,
name|receivedCustomer
operator|.
name|getAddress
argument_list|()
operator|.
name|getAddressLine1
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|customer
operator|.
name|getAddress
argument_list|()
operator|.
name|getAddressLine2
argument_list|()
argument_list|,
name|receivedCustomer
operator|.
name|getAddress
argument_list|()
operator|.
name|getAddressLine2
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|receivedCustomer
operator|.
name|getAddress
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|results
init|=
name|entityManager
operator|.
name|createQuery
argument_list|(
literal|"select o from "
operator|+
name|Customer
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|" o"
argument_list|)
operator|.
name|getResultList
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|results
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Customer
name|persistedCustomer
init|=
operator|(
name|Customer
operator|)
name|results
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|receivedCustomer
operator|.
name|getName
argument_list|()
argument_list|,
name|persistedCustomer
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|receivedCustomer
operator|.
name|getId
argument_list|()
argument_list|,
name|persistedCustomer
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|receivedCustomer
operator|.
name|getAddress
argument_list|()
operator|.
name|getAddressLine1
argument_list|()
argument_list|,
name|persistedCustomer
operator|.
name|getAddress
argument_list|()
operator|.
name|getAddressLine1
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|receivedCustomer
operator|.
name|getAddress
argument_list|()
operator|.
name|getAddressLine2
argument_list|()
argument_list|,
name|persistedCustomer
operator|.
name|getAddress
argument_list|()
operator|.
name|getAddressLine2
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|receivedCustomer
operator|.
name|getAddress
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|,
name|persistedCustomer
operator|.
name|getAddress
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

