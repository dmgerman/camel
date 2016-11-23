begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.guice.testing.junit4
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|guice
operator|.
name|testing
operator|.
name|junit4
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
name|guice
operator|.
name|testing
operator|.
name|InjectorManager
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
name|notification
operator|.
name|RunNotifier
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runners
operator|.
name|BlockJUnit4ClassRunner
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runners
operator|.
name|model
operator|.
name|FrameworkMethod
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runners
operator|.
name|model
operator|.
name|InitializationError
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runners
operator|.
name|model
operator|.
name|Statement
import|;
end_import

begin_class
DECL|class|GuiceyJUnit4
specifier|public
class|class
name|GuiceyJUnit4
extends|extends
name|BlockJUnit4ClassRunner
block|{
DECL|field|manager
specifier|protected
specifier|static
name|InjectorManager
name|manager
init|=
operator|new
name|InjectorManager
argument_list|()
decl_stmt|;
DECL|method|GuiceyJUnit4 (Class<?> aClass)
specifier|public
name|GuiceyJUnit4
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|aClass
parameter_list|)
throws|throws
name|InitializationError
block|{
name|super
argument_list|(
name|aClass
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|withBeforeClasses (Statement statement)
specifier|protected
name|Statement
name|withBeforeClasses
parameter_list|(
name|Statement
name|statement
parameter_list|)
block|{
specifier|final
name|Statement
name|parent
init|=
name|super
operator|.
name|withBeforeClasses
argument_list|(
name|statement
argument_list|)
decl_stmt|;
return|return
operator|new
name|Statement
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|evaluate
parameter_list|()
throws|throws
name|Throwable
block|{
name|manager
operator|.
name|beforeClasses
argument_list|()
expr_stmt|;
name|parent
operator|.
name|evaluate
argument_list|()
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
DECL|method|withAfterClasses (Statement statement)
specifier|protected
name|Statement
name|withAfterClasses
parameter_list|(
name|Statement
name|statement
parameter_list|)
block|{
specifier|final
name|Statement
name|parent
init|=
name|super
operator|.
name|withAfterClasses
argument_list|(
name|statement
argument_list|)
decl_stmt|;
return|return
operator|new
name|Statement
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|evaluate
parameter_list|()
throws|throws
name|Throwable
block|{
name|parent
operator|.
name|evaluate
argument_list|()
expr_stmt|;
name|manager
operator|.
name|afterClasses
argument_list|()
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
DECL|method|withBefores (FrameworkMethod frameworkMethod, final Object test, Statement statement)
specifier|protected
name|Statement
name|withBefores
parameter_list|(
name|FrameworkMethod
name|frameworkMethod
parameter_list|,
specifier|final
name|Object
name|test
parameter_list|,
name|Statement
name|statement
parameter_list|)
block|{
specifier|final
name|Statement
name|parent
init|=
name|super
operator|.
name|withBefores
argument_list|(
name|frameworkMethod
argument_list|,
name|test
argument_list|,
name|statement
argument_list|)
decl_stmt|;
return|return
operator|new
name|Statement
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|evaluate
parameter_list|()
throws|throws
name|Throwable
block|{
name|manager
operator|.
name|beforeTest
argument_list|(
name|test
argument_list|)
expr_stmt|;
name|parent
operator|.
name|evaluate
argument_list|()
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
DECL|method|withAfters (FrameworkMethod frameworkMethod, final Object test, Statement statement)
specifier|protected
name|Statement
name|withAfters
parameter_list|(
name|FrameworkMethod
name|frameworkMethod
parameter_list|,
specifier|final
name|Object
name|test
parameter_list|,
name|Statement
name|statement
parameter_list|)
block|{
specifier|final
name|Statement
name|parent
init|=
name|super
operator|.
name|withBefores
argument_list|(
name|frameworkMethod
argument_list|,
name|test
argument_list|,
name|statement
argument_list|)
decl_stmt|;
return|return
operator|new
name|Statement
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|evaluate
parameter_list|()
throws|throws
name|Throwable
block|{
name|parent
operator|.
name|evaluate
argument_list|()
expr_stmt|;
name|manager
operator|.
name|afterTest
argument_list|(
name|test
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
DECL|method|run (RunNotifier runNotifier)
specifier|public
name|void
name|run
parameter_list|(
name|RunNotifier
name|runNotifier
parameter_list|)
block|{
name|super
operator|.
name|run
argument_list|(
name|runNotifier
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

