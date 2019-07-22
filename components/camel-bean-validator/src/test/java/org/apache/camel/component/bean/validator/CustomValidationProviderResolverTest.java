begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.bean.validator
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|bean
operator|.
name|validator
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
import|import static
name|java
operator|.
name|util
operator|.
name|Arrays
operator|.
name|asList
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|validation
operator|.
name|ValidationProviderResolver
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|validation
operator|.
name|spi
operator|.
name|ValidationProvider
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
name|BindToRegistry
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
name|builder
operator|.
name|RouteBuilder
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
name|hibernate
operator|.
name|validator
operator|.
name|HibernateValidator
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
name|mockito
operator|.
name|BDDMockito
operator|.
name|given
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
name|atLeastOnce
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
DECL|class|CustomValidationProviderResolverTest
specifier|public
class|class
name|CustomValidationProviderResolverTest
extends|extends
name|CamelTestSupport
block|{
comment|// Routing fixtures
annotation|@
name|BindToRegistry
argument_list|(
literal|"myValidationProviderResolver"
argument_list|)
DECL|field|validationProviderResolver
name|ValidationProviderResolver
name|validationProviderResolver
init|=
name|mock
argument_list|(
name|ValidationProviderResolver
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Override
DECL|method|doPreSetup ()
specifier|protected
name|void
name|doPreSetup
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|ValidationProvider
argument_list|<
name|?
argument_list|>
argument_list|>
name|validationProviders
init|=
name|asList
argument_list|(
operator|new
name|HibernateValidator
argument_list|()
argument_list|)
decl_stmt|;
name|given
argument_list|(
name|validationProviderResolver
operator|.
name|getValidationProviders
argument_list|()
argument_list|)
operator|.
name|willReturn
argument_list|(
name|validationProviders
argument_list|)
expr_stmt|;
name|super
operator|.
name|doPreSetup
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"direct:test"
argument_list|)
operator|.
name|to
argument_list|(
literal|"bean-validator://ValidationProviderResolverTest?validationProviderResolver=#myValidationProviderResolver"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
comment|// Tests
annotation|@
name|Test
DECL|method|shouldResolveCustomValidationProviderResolver ()
specifier|public
name|void
name|shouldResolveCustomValidationProviderResolver
parameter_list|()
block|{
name|verify
argument_list|(
name|validationProviderResolver
argument_list|,
name|atLeastOnce
argument_list|()
argument_list|)
operator|.
name|getValidationProviders
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

