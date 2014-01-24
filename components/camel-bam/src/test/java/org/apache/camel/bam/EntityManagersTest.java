begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.bam
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|bam
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|EntityManagerFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|bam
operator|.
name|EntityManagers
operator|.
name|resolveEntityManager
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|mock
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|verify
import|;
end_import

begin_class
DECL|class|EntityManagersTest
specifier|public
class|class
name|EntityManagersTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
DECL|method|shouldCreateNewEntityManagerIfThereIsNoTransaction ()
specifier|public
name|void
name|shouldCreateNewEntityManagerIfThereIsNoTransaction
parameter_list|()
block|{
comment|// Given
name|EntityManagerFactory
name|entityManagerFactory
init|=
name|mock
argument_list|(
name|EntityManagerFactory
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// When
name|resolveEntityManager
argument_list|(
name|entityManagerFactory
argument_list|)
expr_stmt|;
comment|// Then
name|verify
argument_list|(
name|entityManagerFactory
argument_list|)
operator|.
name|createEntityManager
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

