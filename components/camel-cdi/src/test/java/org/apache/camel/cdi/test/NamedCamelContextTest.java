begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.cdi.test
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|cdi
operator|.
name|test
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|context
operator|.
name|ApplicationScoped
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|Instance
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|Produces
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Named
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
name|cdi
operator|.
name|CdiCamelExtension
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
name|DefaultCamelContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|arquillian
operator|.
name|container
operator|.
name|test
operator|.
name|api
operator|.
name|Deployment
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|arquillian
operator|.
name|junit
operator|.
name|Arquillian
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|shrinkwrap
operator|.
name|api
operator|.
name|Archive
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|shrinkwrap
operator|.
name|api
operator|.
name|ShrinkWrap
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|shrinkwrap
operator|.
name|api
operator|.
name|asset
operator|.
name|EmptyAsset
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|shrinkwrap
operator|.
name|api
operator|.
name|spec
operator|.
name|JavaArchive
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
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|Matchers
operator|.
name|containsInAnyOrder
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|Matchers
operator|.
name|equalTo
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|Matchers
operator|.
name|hasProperty
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertThat
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|Arquillian
operator|.
name|class
argument_list|)
DECL|class|NamedCamelContextTest
specifier|public
class|class
name|NamedCamelContextTest
block|{
annotation|@
name|Named
annotation|@
name|Produces
annotation|@
name|ApplicationScoped
DECL|field|emptyNamedFieldContext
specifier|private
name|CamelContext
name|emptyNamedFieldContext
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
annotation|@
name|Produces
annotation|@
name|ApplicationScoped
annotation|@
name|Named
argument_list|(
literal|"named-field-context"
argument_list|)
DECL|field|namedFieldContext
specifier|private
name|CamelContext
name|namedFieldContext
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
annotation|@
name|Named
annotation|@
name|Produces
annotation|@
name|ApplicationScoped
DECL|method|getEmptyNamedGetterContext ()
specifier|private
name|CamelContext
name|getEmptyNamedGetterContext
parameter_list|()
block|{
return|return
operator|new
name|DefaultCamelContext
argument_list|()
return|;
block|}
annotation|@
name|Named
annotation|@
name|Produces
annotation|@
name|ApplicationScoped
DECL|method|getEmptyNamedMethodContext ()
specifier|private
name|CamelContext
name|getEmptyNamedMethodContext
parameter_list|()
block|{
return|return
operator|new
name|DefaultCamelContext
argument_list|()
return|;
block|}
annotation|@
name|Produces
annotation|@
name|ApplicationScoped
annotation|@
name|Named
argument_list|(
literal|"named-getter-context"
argument_list|)
DECL|method|getNamedGetterContext ()
specifier|private
name|CamelContext
name|getNamedGetterContext
parameter_list|()
block|{
return|return
operator|new
name|DefaultCamelContext
argument_list|()
return|;
block|}
annotation|@
name|Produces
annotation|@
name|ApplicationScoped
annotation|@
name|Named
argument_list|(
literal|"named-method-context"
argument_list|)
DECL|method|getNamedMethodContext ()
specifier|private
name|CamelContext
name|getNamedMethodContext
parameter_list|()
block|{
return|return
operator|new
name|DefaultCamelContext
argument_list|()
return|;
block|}
annotation|@
name|Deployment
DECL|method|deployment ()
specifier|public
specifier|static
name|Archive
argument_list|<
name|?
argument_list|>
name|deployment
parameter_list|()
block|{
return|return
name|ShrinkWrap
operator|.
name|create
argument_list|(
name|JavaArchive
operator|.
name|class
argument_list|)
comment|// Camel CDI
operator|.
name|addPackage
argument_list|(
name|CdiCamelExtension
operator|.
name|class
operator|.
name|getPackage
argument_list|()
argument_list|)
comment|// Bean archive deployment descriptor
operator|.
name|addAsManifestResource
argument_list|(
name|EmptyAsset
operator|.
name|INSTANCE
argument_list|,
literal|"beans.xml"
argument_list|)
return|;
block|}
annotation|@
name|Test
DECL|method|verifyCamelContexts (Instance<CamelContext> contexts)
specifier|public
name|void
name|verifyCamelContexts
parameter_list|(
name|Instance
argument_list|<
name|CamelContext
argument_list|>
name|contexts
parameter_list|)
block|{
name|assertThat
argument_list|(
name|contexts
argument_list|,
name|containsInAnyOrder
argument_list|(
name|hasProperty
argument_list|(
literal|"name"
argument_list|,
name|equalTo
argument_list|(
literal|"emptyNamedFieldContext"
argument_list|)
argument_list|)
argument_list|,
name|hasProperty
argument_list|(
literal|"name"
argument_list|,
name|equalTo
argument_list|(
literal|"emptyNamedGetterContext"
argument_list|)
argument_list|)
argument_list|,
name|hasProperty
argument_list|(
literal|"name"
argument_list|,
name|equalTo
argument_list|(
literal|"emptyNamedMethodContext"
argument_list|)
argument_list|)
argument_list|,
name|hasProperty
argument_list|(
literal|"name"
argument_list|,
name|equalTo
argument_list|(
literal|"named-field-context"
argument_list|)
argument_list|)
argument_list|,
name|hasProperty
argument_list|(
literal|"name"
argument_list|,
name|equalTo
argument_list|(
literal|"named-getter-context"
argument_list|)
argument_list|)
argument_list|,
name|hasProperty
argument_list|(
literal|"name"
argument_list|,
name|equalTo
argument_list|(
literal|"named-method-context"
argument_list|)
argument_list|)
argument_list|,
name|hasProperty
argument_list|(
literal|"name"
argument_list|,
name|equalTo
argument_list|(
literal|"emptyNamedBeanContext"
argument_list|)
argument_list|)
argument_list|,
name|hasProperty
argument_list|(
literal|"name"
argument_list|,
name|equalTo
argument_list|(
literal|"named-bean-context"
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Named
annotation|@
name|ApplicationScoped
DECL|class|EmptyNamedBeanContext
specifier|static
class|class
name|EmptyNamedBeanContext
extends|extends
name|DefaultCamelContext
block|{     }
annotation|@
name|ApplicationScoped
annotation|@
name|Named
argument_list|(
literal|"named-bean-context"
argument_list|)
DECL|class|NamedBeanContext
specifier|static
class|class
name|NamedBeanContext
extends|extends
name|DefaultCamelContext
block|{     }
block|}
end_class

end_unit

