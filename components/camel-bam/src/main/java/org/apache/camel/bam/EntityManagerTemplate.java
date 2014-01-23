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
name|EntityManager
import|;
end_import

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
name|closeNonTransactionalEntityManager
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

begin_class
DECL|class|EntityManagerTemplate
specifier|public
class|class
name|EntityManagerTemplate
block|{
DECL|field|entityManagerFactory
specifier|private
specifier|final
name|EntityManagerFactory
name|entityManagerFactory
decl_stmt|;
DECL|method|EntityManagerTemplate (EntityManagerFactory entityManagerFactory)
specifier|public
name|EntityManagerTemplate
parameter_list|(
name|EntityManagerFactory
name|entityManagerFactory
parameter_list|)
block|{
name|this
operator|.
name|entityManagerFactory
operator|=
name|entityManagerFactory
expr_stmt|;
block|}
DECL|method|execute (EntityManagerCallback<T> entityManagerCallback)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|execute
parameter_list|(
name|EntityManagerCallback
argument_list|<
name|T
argument_list|>
name|entityManagerCallback
parameter_list|)
block|{
name|EntityManager
name|entityManager
init|=
literal|null
decl_stmt|;
try|try
block|{
name|entityManager
operator|=
name|resolveEntityManager
argument_list|(
name|entityManagerFactory
argument_list|)
expr_stmt|;
return|return
name|entityManagerCallback
operator|.
name|execute
argument_list|(
name|entityManager
argument_list|)
return|;
block|}
finally|finally
block|{
name|closeNonTransactionalEntityManager
argument_list|(
name|entityManager
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

