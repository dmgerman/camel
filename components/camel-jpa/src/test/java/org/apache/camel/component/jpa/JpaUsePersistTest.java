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
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|DefaultExchange
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

begin_comment
comment|/**  * @version $Revision: 931444 $  */
end_comment

begin_class
DECL|class|JpaUsePersistTest
specifier|public
class|class
name|JpaUsePersistTest
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
literal|true
return|;
block|}
annotation|@
name|Test
DECL|method|produceExistingEntityShouldThowAnException ()
specifier|public
name|void
name|produceExistingEntityShouldThowAnException
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
literal|"?usePersist=true"
argument_list|)
expr_stmt|;
specifier|final
name|Customer
name|customer
init|=
name|createDefaultCustomer
argument_list|()
decl_stmt|;
name|save
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
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|camelContext
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|customer
argument_list|)
expr_stmt|;
name|Exchange
name|returnedExchange
init|=
name|template
operator|.
name|send
argument_list|(
name|endpoint
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|returnedExchange
operator|.
name|isFailed
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|returnedExchange
operator|.
name|getException
argument_list|()
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
block|}
block|}
end_class

end_unit

