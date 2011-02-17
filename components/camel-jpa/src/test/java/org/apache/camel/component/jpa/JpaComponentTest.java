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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|examples
operator|.
name|SendEmail
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
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
name|orm
operator|.
name|jpa
operator|.
name|JpaTransactionManager
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|JpaComponentTest
specifier|public
class|class
name|JpaComponentTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testJpaComponentCtr ()
specifier|public
name|void
name|testJpaComponentCtr
parameter_list|()
throws|throws
name|Exception
block|{
name|JpaComponent
name|comp
init|=
operator|new
name|JpaComponent
argument_list|()
decl_stmt|;
name|comp
operator|.
name|setCamelContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|comp
operator|.
name|getEntityManagerFactory
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|comp
operator|.
name|getTransactionManager
argument_list|()
argument_list|)
expr_stmt|;
name|JpaEndpoint
name|jpa
init|=
operator|(
name|JpaEndpoint
operator|)
name|comp
operator|.
name|createEndpoint
argument_list|(
literal|"jpa://"
operator|+
name|SendEmail
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|jpa
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testJpaComponentEMFandTM ()
specifier|public
name|void
name|testJpaComponentEMFandTM
parameter_list|()
throws|throws
name|Exception
block|{
name|JpaComponent
name|comp
init|=
operator|new
name|JpaComponent
argument_list|()
decl_stmt|;
name|comp
operator|.
name|setCamelContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|comp
operator|.
name|getEntityManagerFactory
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|comp
operator|.
name|getTransactionManager
argument_list|()
argument_list|)
expr_stmt|;
name|EntityManagerFactory
name|fac
init|=
name|Persistence
operator|.
name|createEntityManagerFactory
argument_list|(
literal|"camel"
argument_list|)
decl_stmt|;
name|JpaTransactionManager
name|tm
init|=
operator|new
name|JpaTransactionManager
argument_list|(
name|fac
argument_list|)
decl_stmt|;
name|tm
operator|.
name|afterPropertiesSet
argument_list|()
expr_stmt|;
name|comp
operator|.
name|setEntityManagerFactory
argument_list|(
name|fac
argument_list|)
expr_stmt|;
name|comp
operator|.
name|setTransactionManager
argument_list|(
name|tm
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|fac
argument_list|,
name|comp
operator|.
name|getEntityManagerFactory
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|tm
argument_list|,
name|comp
operator|.
name|getTransactionManager
argument_list|()
argument_list|)
expr_stmt|;
name|JpaEndpoint
name|jpa
init|=
operator|(
name|JpaEndpoint
operator|)
name|comp
operator|.
name|createEndpoint
argument_list|(
literal|"jpa://"
operator|+
name|SendEmail
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|jpa
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

