begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mybatis
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mybatis
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
name|component
operator|.
name|bean
operator|.
name|BeanProcessor
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
name|support
operator|.
name|DefaultProducer
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
name|support
operator|.
name|ExchangeHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ibatis
operator|.
name|session
operator|.
name|ExecutorType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ibatis
operator|.
name|session
operator|.
name|SqlSession
import|;
end_import

begin_class
DECL|class|MyBatisBeanProducer
specifier|public
class|class
name|MyBatisBeanProducer
extends|extends
name|DefaultProducer
block|{
DECL|field|endpoint
specifier|private
specifier|final
name|MyBatisBeanEndpoint
name|endpoint
decl_stmt|;
DECL|method|MyBatisBeanProducer (MyBatisBeanEndpoint endpoint)
specifier|public
name|MyBatisBeanProducer
parameter_list|(
name|MyBatisBeanEndpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
block|}
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|SqlSession
name|session
decl_stmt|;
name|ExecutorType
name|executorType
init|=
name|endpoint
operator|.
name|getExecutorType
argument_list|()
decl_stmt|;
if|if
condition|(
name|executorType
operator|==
literal|null
condition|)
block|{
name|session
operator|=
name|endpoint
operator|.
name|getSqlSessionFactory
argument_list|()
operator|.
name|openSession
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|session
operator|=
name|endpoint
operator|.
name|getSqlSessionFactory
argument_list|()
operator|.
name|openSession
argument_list|(
name|executorType
argument_list|)
expr_stmt|;
block|}
name|log
operator|.
name|debug
argument_list|(
literal|"Opened MyBatis SqlSession: {}"
argument_list|,
name|session
argument_list|)
expr_stmt|;
try|try
block|{
name|doProcess
argument_list|(
name|exchange
argument_list|,
name|session
argument_list|)
expr_stmt|;
comment|// flush the batch statements and commit the database connection
name|session
operator|.
name|commit
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// discard the pending batch statements and roll the database connection back
name|session
operator|.
name|rollback
argument_list|()
expr_stmt|;
throw|throw
name|e
throw|;
block|}
finally|finally
block|{
comment|// and finally close the session as we're done
name|log
operator|.
name|debug
argument_list|(
literal|"Closing MyBatis SqlSession: {}"
argument_list|,
name|session
argument_list|)
expr_stmt|;
name|session
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|doProcess (Exchange exchange, SqlSession session)
specifier|protected
name|void
name|doProcess
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|SqlSession
name|session
parameter_list|)
throws|throws
name|Exception
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Invoking MyBatisBean on {}:{}"
argument_list|,
name|endpoint
operator|.
name|getBeanName
argument_list|()
argument_list|,
name|endpoint
operator|.
name|getMethodName
argument_list|()
argument_list|)
expr_stmt|;
comment|// if we use input or output header we need to copy exchange to avoid mutating the
name|Exchange
name|copy
init|=
name|ExchangeHelper
operator|.
name|createCopy
argument_list|(
name|exchange
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|Object
name|input
init|=
name|getInput
argument_list|(
name|copy
argument_list|)
decl_stmt|;
name|copy
operator|.
name|getMessage
argument_list|()
operator|.
name|setBody
argument_list|(
name|input
argument_list|)
expr_stmt|;
name|BeanProcessor
name|beanProcessor
init|=
name|createBeanProcessor
argument_list|(
name|session
argument_list|)
decl_stmt|;
name|beanProcessor
operator|.
name|start
argument_list|()
expr_stmt|;
name|beanProcessor
operator|.
name|process
argument_list|(
name|copy
argument_list|)
expr_stmt|;
name|beanProcessor
operator|.
name|stop
argument_list|()
expr_stmt|;
name|Object
name|result
init|=
name|copy
operator|.
name|getMessage
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
if|if
condition|(
name|result
operator|!=
name|input
condition|)
block|{
if|if
condition|(
name|endpoint
operator|.
name|getOutputHeader
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// set the result as header for insert
name|log
operator|.
name|trace
argument_list|(
literal|"Setting result as header [{}]: {}"
argument_list|,
name|endpoint
operator|.
name|getOutputHeader
argument_list|()
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getMessage
argument_list|()
operator|.
name|setHeader
argument_list|(
name|endpoint
operator|.
name|getOutputHeader
argument_list|()
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// set the result as body for insert
name|log
operator|.
name|trace
argument_list|(
literal|"Setting result as body: {}"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getMessage
argument_list|()
operator|.
name|setBody
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getMessage
argument_list|()
operator|.
name|setHeader
argument_list|(
name|MyBatisConstants
operator|.
name|MYBATIS_RESULT
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|createBeanProcessor (SqlSession session)
specifier|private
name|BeanProcessor
name|createBeanProcessor
parameter_list|(
name|SqlSession
name|session
parameter_list|)
throws|throws
name|Exception
block|{
comment|// discover the bean and get the mapper
comment|// is the bean a alias type
name|Class
name|clazz
init|=
name|session
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getTypeAliasRegistry
argument_list|()
operator|.
name|resolveAlias
argument_list|(
name|endpoint
operator|.
name|getBeanName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|clazz
operator|==
literal|null
condition|)
block|{
comment|// its maybe a FQN so try to use Camel to lookup the class
name|clazz
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveMandatoryClass
argument_list|(
name|endpoint
operator|.
name|getBeanName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|log
operator|.
name|debug
argument_list|(
literal|"Resolved MyBatis Bean: {} as class: {}"
argument_list|,
name|endpoint
operator|.
name|getBeanName
argument_list|()
argument_list|,
name|clazz
argument_list|)
expr_stmt|;
comment|// find the mapper
name|Object
name|mapper
init|=
name|session
operator|.
name|getMapper
argument_list|(
name|clazz
argument_list|)
decl_stmt|;
if|if
condition|(
name|mapper
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"No Mapper with typeAlias or class name: "
operator|+
name|endpoint
operator|.
name|getBeanName
argument_list|()
operator|+
literal|" in MyBatis configuration."
argument_list|)
throw|;
block|}
name|log
operator|.
name|debug
argument_list|(
literal|"Resolved MyBatis Bean mapper: {}"
argument_list|,
name|mapper
argument_list|)
expr_stmt|;
name|BeanProcessor
name|answer
init|=
operator|new
name|BeanProcessor
argument_list|(
name|mapper
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
argument_list|)
decl_stmt|;
name|answer
operator|.
name|setMethod
argument_list|(
name|endpoint
operator|.
name|getMethodName
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|getInput (final Exchange exchange)
specifier|private
name|Object
name|getInput
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
block|{
specifier|final
name|String
name|inputHeader
init|=
name|endpoint
operator|.
name|getInputHeader
argument_list|()
decl_stmt|;
if|if
condition|(
name|inputHeader
operator|!=
literal|null
condition|)
block|{
return|return
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|inputHeader
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

