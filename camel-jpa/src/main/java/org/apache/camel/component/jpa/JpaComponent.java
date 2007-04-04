begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|CamelContext
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
name|Component
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
name|EndpointResolver
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
name|Service
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
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|ServiceSupport
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
name|util
operator|.
name|ObjectHelper
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
name|util
operator|.
name|URISupport
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
name|util
operator|.
name|IntrospectionSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|orm
operator|.
name|jpa
operator|.
name|JpaTemplate
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|orm
operator|.
name|jpa
operator|.
name|LocalEntityManagerFactoryBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|orm
operator|.
name|jpa
operator|.
name|JpaTransactionManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|orm
operator|.
name|jpa
operator|.
name|SharedEntityManagerCreator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|orm
operator|.
name|jpa
operator|.
name|LocalContainerEntityManagerFactoryBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|orm
operator|.
name|jpa
operator|.
name|JpaCallback
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|jca
operator|.
name|support
operator|.
name|LocalConnectionFactoryBean
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
name|TransactionTemplate
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

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|transaction
operator|.
name|jta
operator|.
name|JtaTransactionManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|dao
operator|.
name|DataAccessException
import|;
end_import

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
import|import
name|javax
operator|.
name|persistence
operator|.
name|Persistence
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
argument_list|<
name|Exchange
argument_list|>
block|{
DECL|field|entityManagerFactory
specifier|private
name|EntityManagerFactory
name|entityManagerFactory
decl_stmt|;
DECL|method|resolveComponent (CamelContext container, String uri)
specifier|public
name|Component
name|resolveComponent
parameter_list|(
name|CamelContext
name|container
parameter_list|,
name|String
name|uri
parameter_list|)
throws|throws
name|Exception
block|{
return|return
literal|null
return|;
block|}
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
comment|// Implementation methods
comment|//-------------------------------------------------------------------------
annotation|@
name|Override
DECL|method|getUriPrefixes ()
specifier|public
name|String
index|[]
name|getUriPrefixes
parameter_list|()
block|{
return|return
operator|new
name|String
index|[]
block|{
literal|"jpa"
block|}
return|;
block|}
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String path, Map options)
specifier|protected
name|Endpoint
argument_list|<
name|Exchange
argument_list|>
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
name|Map
name|consumerProperties
init|=
name|IntrospectionSupport
operator|.
name|extractProperties
argument_list|(
name|options
argument_list|,
literal|"consumer."
argument_list|)
decl_stmt|;
if|if
condition|(
name|consumerProperties
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|setConsumerProperties
argument_list|(
name|consumerProperties
argument_list|)
expr_stmt|;
block|}
name|Map
name|emProperties
init|=
name|IntrospectionSupport
operator|.
name|extractProperties
argument_list|(
name|options
argument_list|,
literal|"emf."
argument_list|)
decl_stmt|;
if|if
condition|(
name|emProperties
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|setEntityManagerProperties
argument_list|(
name|emProperties
argument_list|)
expr_stmt|;
block|}
comment|// lets interpret the next string as a class
if|if
condition|(
name|path
operator|!=
literal|null
condition|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|type
init|=
name|ObjectHelper
operator|.
name|loadClass
argument_list|(
name|path
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

