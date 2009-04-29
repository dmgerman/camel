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
name|Map
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
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Endpoint
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
name|DefaultComponent
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
name|PlatformTransactionManager
import|;
end_import

begin_comment
comment|/**  * A JPA Component  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|JpaComponent
specifier|public
class|class
name|JpaComponent
extends|extends
name|DefaultComponent
block|{
DECL|field|entityManagerFactory
specifier|private
name|EntityManagerFactory
name|entityManagerFactory
decl_stmt|;
DECL|field|transactionManager
specifier|private
name|PlatformTransactionManager
name|transactionManager
decl_stmt|;
comment|// Properties
comment|//-------------------------------------------------------------------------
DECL|method|getEntityManagerFactory ()
specifier|public
name|EntityManagerFactory
name|getEntityManagerFactory
parameter_list|()
block|{
return|return
name|entityManagerFactory
return|;
block|}
DECL|method|setEntityManagerFactory (EntityManagerFactory entityManagerFactory)
specifier|public
name|void
name|setEntityManagerFactory
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
DECL|method|getTransactionManager ()
specifier|public
name|PlatformTransactionManager
name|getTransactionManager
parameter_list|()
block|{
return|return
name|transactionManager
return|;
block|}
DECL|method|setTransactionManager (PlatformTransactionManager transactionManager)
specifier|public
name|void
name|setTransactionManager
parameter_list|(
name|PlatformTransactionManager
name|transactionManager
parameter_list|)
block|{
name|this
operator|.
name|transactionManager
operator|=
name|transactionManager
expr_stmt|;
block|}
comment|// Implementation methods
comment|//-------------------------------------------------------------------------
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String path, Map options)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|path
parameter_list|,
name|Map
name|options
parameter_list|)
throws|throws
name|Exception
block|{
name|JpaEndpoint
name|endpoint
init|=
operator|new
name|JpaEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|)
decl_stmt|;
comment|// lets interpret the next string as a class
if|if
condition|(
name|path
operator|!=
literal|null
condition|)
block|{
comment|// provide the class loader of this component to work in OSGi environments as camel-jpa must be able
comment|// to resolve the entity classes
name|Class
argument_list|<
name|?
argument_list|>
name|type
init|=
name|getCamelContext
argument_list|()
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveClass
argument_list|(
name|path
argument_list|,
name|JpaComponent
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|setEntityType
argument_list|(
name|type
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|endpoint
return|;
block|}
block|}
end_class

end_unit

