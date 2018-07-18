begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jpa.springboot
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
operator|.
name|springboot
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Generated
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
name|spring
operator|.
name|boot
operator|.
name|ComponentConfigurationPropertiesCommon
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|ConfigurationProperties
import|;
end_import

begin_comment
comment|/**  * The jpa component enables you to store and retrieve Java objects from  * databases using JPA.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.SpringBootAutoConfigurationMojo"
argument_list|)
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.component.jpa"
argument_list|)
DECL|class|JpaComponentConfiguration
specifier|public
class|class
name|JpaComponentConfiguration
extends|extends
name|ComponentConfigurationPropertiesCommon
block|{
comment|/**      * Whether to enable auto configuration of the jpa component. This is      * enabled by default.      */
DECL|field|enabled
specifier|private
name|Boolean
name|enabled
decl_stmt|;
comment|/**      * To use the EntityManagerFactory. This is strongly recommended to      * configure. The option is a javax.persistence.EntityManagerFactory type.      */
DECL|field|entityManagerFactory
specifier|private
name|String
name|entityManagerFactory
decl_stmt|;
comment|/**      * To use the PlatformTransactionManager for managing transactions. The      * option is a org.springframework.transaction.PlatformTransactionManager      * type.      */
DECL|field|transactionManager
specifier|private
name|String
name|transactionManager
decl_stmt|;
comment|/**      * The camel-jpa component will join transaction by default. You can use      * this option to turn this off, for example if you use LOCAL_RESOURCE and      * join transaction doesn't work with your JPA provider. This option can      * also be set globally on the JpaComponent, instead of having to set it on      * all endpoints.      */
DECL|field|joinTransaction
specifier|private
name|Boolean
name|joinTransaction
init|=
literal|true
decl_stmt|;
comment|/**      * Whether to use Spring's SharedEntityManager for the consumer/producer.      * Note in most cases joinTransaction should be set to false as this is not      * an EXTENDED EntityManager.      */
DECL|field|sharedEntityManager
specifier|private
name|Boolean
name|sharedEntityManager
init|=
literal|false
decl_stmt|;
comment|/**      * Whether the component should resolve property placeholders on itself when      * starting. Only properties which are of String type can use property      * placeholders.      */
DECL|field|resolvePropertyPlaceholders
specifier|private
name|Boolean
name|resolvePropertyPlaceholders
init|=
literal|true
decl_stmt|;
DECL|method|getEntityManagerFactory ()
specifier|public
name|String
name|getEntityManagerFactory
parameter_list|()
block|{
return|return
name|entityManagerFactory
return|;
block|}
DECL|method|setEntityManagerFactory (String entityManagerFactory)
specifier|public
name|void
name|setEntityManagerFactory
parameter_list|(
name|String
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
name|String
name|getTransactionManager
parameter_list|()
block|{
return|return
name|transactionManager
return|;
block|}
DECL|method|setTransactionManager (String transactionManager)
specifier|public
name|void
name|setTransactionManager
parameter_list|(
name|String
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
DECL|method|getJoinTransaction ()
specifier|public
name|Boolean
name|getJoinTransaction
parameter_list|()
block|{
return|return
name|joinTransaction
return|;
block|}
DECL|method|setJoinTransaction (Boolean joinTransaction)
specifier|public
name|void
name|setJoinTransaction
parameter_list|(
name|Boolean
name|joinTransaction
parameter_list|)
block|{
name|this
operator|.
name|joinTransaction
operator|=
name|joinTransaction
expr_stmt|;
block|}
DECL|method|getSharedEntityManager ()
specifier|public
name|Boolean
name|getSharedEntityManager
parameter_list|()
block|{
return|return
name|sharedEntityManager
return|;
block|}
DECL|method|setSharedEntityManager (Boolean sharedEntityManager)
specifier|public
name|void
name|setSharedEntityManager
parameter_list|(
name|Boolean
name|sharedEntityManager
parameter_list|)
block|{
name|this
operator|.
name|sharedEntityManager
operator|=
name|sharedEntityManager
expr_stmt|;
block|}
DECL|method|getResolvePropertyPlaceholders ()
specifier|public
name|Boolean
name|getResolvePropertyPlaceholders
parameter_list|()
block|{
return|return
name|resolvePropertyPlaceholders
return|;
block|}
DECL|method|setResolvePropertyPlaceholders ( Boolean resolvePropertyPlaceholders)
specifier|public
name|void
name|setResolvePropertyPlaceholders
parameter_list|(
name|Boolean
name|resolvePropertyPlaceholders
parameter_list|)
block|{
name|this
operator|.
name|resolvePropertyPlaceholders
operator|=
name|resolvePropertyPlaceholders
expr_stmt|;
block|}
block|}
end_class

end_unit

