begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
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
name|Objects
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
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
name|CamelExecutionException
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
name|ContextTestSupport
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
name|Processor
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
name|impl
operator|.
name|saga
operator|.
name|InMemorySagaService
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
name|model
operator|.
name|SagaPropagation
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
name|saga
operator|.
name|CamelSagaService
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

begin_class
DECL|class|SagaPropagationTest
specifier|public
class|class
name|SagaPropagationTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|sagaIds
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|sagaIds
decl_stmt|;
annotation|@
name|Test
DECL|method|testPropagationRequired ()
specifier|public
name|void
name|testPropagationRequired
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|createFluentProducerTemplate
argument_list|()
operator|.
name|to
argument_list|(
literal|"direct:required"
argument_list|)
operator|.
name|request
argument_list|()
expr_stmt|;
name|assertListSize
argument_list|(
name|sagaIds
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|assertUniqueNonNullSagaIds
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPropagationRequiresNew ()
specifier|public
name|void
name|testPropagationRequiresNew
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|createFluentProducerTemplate
argument_list|()
operator|.
name|to
argument_list|(
literal|"direct:requiresNew"
argument_list|)
operator|.
name|request
argument_list|()
expr_stmt|;
name|assertListSize
argument_list|(
name|sagaIds
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|assertUniqueNonNullSagaIds
argument_list|(
literal|3
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPropagationNotSupported ()
specifier|public
name|void
name|testPropagationNotSupported
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|createFluentProducerTemplate
argument_list|()
operator|.
name|to
argument_list|(
literal|"direct:notSupported"
argument_list|)
operator|.
name|request
argument_list|()
expr_stmt|;
name|assertListSize
argument_list|(
name|sagaIds
argument_list|,
literal|4
argument_list|)
expr_stmt|;
name|assertNonNullSagaIds
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPropagationSupports ()
specifier|public
name|void
name|testPropagationSupports
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|createFluentProducerTemplate
argument_list|()
operator|.
name|to
argument_list|(
literal|"direct:supports"
argument_list|)
operator|.
name|request
argument_list|()
expr_stmt|;
name|assertListSize
argument_list|(
name|sagaIds
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|assertNonNullSagaIds
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPropagationMandatory ()
specifier|public
name|void
name|testPropagationMandatory
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|context
operator|.
name|createFluentProducerTemplate
argument_list|()
operator|.
name|to
argument_list|(
literal|"direct:mandatory"
argument_list|)
operator|.
name|request
argument_list|()
expr_stmt|;
name|Assert
operator|.
name|fail
argument_list|(
literal|"Exception not thrown"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|ex
parameter_list|)
block|{
comment|// fine
block|}
block|}
annotation|@
name|Test
DECL|method|testPropagationNever ()
specifier|public
name|void
name|testPropagationNever
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|context
operator|.
name|createFluentProducerTemplate
argument_list|()
operator|.
name|to
argument_list|(
literal|"direct:never"
argument_list|)
operator|.
name|request
argument_list|()
expr_stmt|;
name|Assert
operator|.
name|fail
argument_list|(
literal|"Exception not thrown"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|ex
parameter_list|)
block|{
comment|// fine
block|}
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
name|this
operator|.
name|sagaIds
operator|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
expr_stmt|;
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
name|CamelSagaService
name|sagaService
init|=
operator|new
name|InMemorySagaService
argument_list|()
decl_stmt|;
name|context
operator|.
name|addService
argument_list|(
name|sagaService
argument_list|)
expr_stmt|;
comment|// REQUIRED
name|from
argument_list|(
literal|"direct:required"
argument_list|)
operator|.
name|saga
argument_list|()
operator|.
name|process
argument_list|(
name|addSagaIdToList
argument_list|()
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:required2"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:required2"
argument_list|)
operator|.
name|saga
argument_list|()
operator|.
name|propagation
argument_list|(
name|SagaPropagation
operator|.
name|REQUIRED
argument_list|)
operator|.
name|process
argument_list|(
name|addSagaIdToList
argument_list|()
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:required3"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:required3"
argument_list|)
operator|.
name|saga
argument_list|()
operator|.
name|process
argument_list|(
name|addSagaIdToList
argument_list|()
argument_list|)
expr_stmt|;
comment|// REQUIRES_NEW
name|from
argument_list|(
literal|"direct:requiresNew"
argument_list|)
operator|.
name|saga
argument_list|()
operator|.
name|propagation
argument_list|(
name|SagaPropagation
operator|.
name|REQUIRES_NEW
argument_list|)
operator|.
name|process
argument_list|(
name|addSagaIdToList
argument_list|()
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:requiresNew2"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:requiresNew2"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:requiresNew2"
argument_list|)
operator|.
name|saga
argument_list|()
operator|.
name|propagation
argument_list|(
name|SagaPropagation
operator|.
name|REQUIRES_NEW
argument_list|)
operator|.
name|process
argument_list|(
name|addSagaIdToList
argument_list|()
argument_list|)
expr_stmt|;
comment|// NOT_SUPPORTED
name|from
argument_list|(
literal|"direct:notSupported"
argument_list|)
operator|.
name|process
argument_list|(
name|addSagaIdToList
argument_list|()
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:notSupported2"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:notSupported3"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:notSupported2"
argument_list|)
operator|.
name|saga
argument_list|()
comment|// required
operator|.
name|process
argument_list|(
name|addSagaIdToList
argument_list|()
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:notSupported3"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:notSupported3"
argument_list|)
operator|.
name|saga
argument_list|()
operator|.
name|propagation
argument_list|(
name|SagaPropagation
operator|.
name|NOT_SUPPORTED
argument_list|)
operator|.
name|process
argument_list|(
name|addSagaIdToList
argument_list|()
argument_list|)
expr_stmt|;
comment|// SUPPORTS
name|from
argument_list|(
literal|"direct:supports"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:supports2"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:supports3"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:supports2"
argument_list|)
operator|.
name|saga
argument_list|()
comment|// required
operator|.
name|to
argument_list|(
literal|"direct:supports3"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:supports3"
argument_list|)
operator|.
name|saga
argument_list|()
operator|.
name|propagation
argument_list|(
name|SagaPropagation
operator|.
name|SUPPORTS
argument_list|)
operator|.
name|process
argument_list|(
name|addSagaIdToList
argument_list|()
argument_list|)
expr_stmt|;
comment|// MANDATORY
name|from
argument_list|(
literal|"direct:mandatory"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:mandatory2"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:mandatory2"
argument_list|)
operator|.
name|saga
argument_list|()
operator|.
name|propagation
argument_list|(
name|SagaPropagation
operator|.
name|MANDATORY
argument_list|)
operator|.
name|process
argument_list|(
name|addSagaIdToList
argument_list|()
argument_list|)
expr_stmt|;
comment|// NEVER
name|from
argument_list|(
literal|"direct:never"
argument_list|)
operator|.
name|saga
argument_list|()
operator|.
name|to
argument_list|(
literal|"direct:never2"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:never2"
argument_list|)
operator|.
name|saga
argument_list|()
operator|.
name|propagation
argument_list|(
name|SagaPropagation
operator|.
name|NEVER
argument_list|)
operator|.
name|process
argument_list|(
name|addSagaIdToList
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|addSagaIdToList ()
specifier|private
name|Processor
name|addSagaIdToList
parameter_list|()
block|{
return|return
name|ex
lambda|->
name|sagaIds
operator|.
name|add
argument_list|(
name|ex
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|SAGA_LONG_RUNNING_ACTION
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
return|;
block|}
DECL|method|assertUniqueNonNullSagaIds (int num)
specifier|private
name|void
name|assertUniqueNonNullSagaIds
parameter_list|(
name|int
name|num
parameter_list|)
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|uniqueNonNull
init|=
name|this
operator|.
name|sagaIds
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|Objects
operator|::
name|nonNull
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toSet
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|uniqueNonNull
operator|.
name|size
argument_list|()
operator|!=
name|num
condition|)
block|{
name|Assert
operator|.
name|fail
argument_list|(
literal|"Expeced size "
operator|+
name|num
operator|+
literal|", actual "
operator|+
name|uniqueNonNull
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|assertNonNullSagaIds (int num)
specifier|private
name|void
name|assertNonNullSagaIds
parameter_list|(
name|int
name|num
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|nonNull
init|=
name|this
operator|.
name|sagaIds
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|Objects
operator|::
name|nonNull
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|nonNull
operator|.
name|size
argument_list|()
operator|!=
name|num
condition|)
block|{
name|Assert
operator|.
name|fail
argument_list|(
literal|"Expeced size "
operator|+
name|num
operator|+
literal|", actual "
operator|+
name|nonNull
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit
